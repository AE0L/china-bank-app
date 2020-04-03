package app.chinabank.controller;

import app.chinabank.model.Database;
import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.CashTransaction;
import app.chinabank.model.schema.transaction.CheckDetails;
import app.chinabank.model.schema.transaction.CheckTransaction;
import app.chinabank.model.schema.transaction.DebitPayment;
import app.chinabank.model.schema.transaction.ForeignCash;
import app.chinabank.model.schema.transaction.LocalCash;
import app.chinabank.model.schema.transaction.PaymentTransaction;
import app.chinabank.model.schema.transaction.Transaction;
import app.chinabank.model.schemas.BusinessAccount;
import app.chinabank.model.tables.CashTransactionTable;
import app.chinabank.model.tables.CheckTransactionTable;
import app.chinabank.model.tables.CustomerTable;
import app.chinabank.model.tables.DebitPaymentTable;
import app.chinabank.model.tables.ForeignCashTable;
import app.chinabank.model.tables.LocalCashTable;
import app.chinabank.model.tables.PaymentTransactionTable;
import app.chinabank.model.tables.TransactionTable;
import app.chinabank.utils.Logger;
import app.chinabank.utils.TimeUtil;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TransactionController {

  /**
   * Creates the correct transaction type, then store it in the Database.
   *
   * @param data - transaction details.
   * @return saved transaction's ID
   * @throws Exception when an error occured while saving transaction details.
   */
  public static String saveTransaction(JSONObject data) throws Exception {
    Logger.log("Saving transaction details");

    String     type = (String) data.get("transactionType");
    JSONObject info = (JSONObject) data.get("transaction");

    switch (type) {
      case "cash": {
        Logger.log("Saving cash transaction");

        try {
          CashTransaction transaction   = new CashTransaction(info);
          JSONArray       cashBreakdown = (JSONArray) info.get("cash_breakdown");

          CashTransactionTable.getInstance().create(transaction);

          if (transaction.isLocal()) {
            for (Object cash : cashBreakdown) {
              LocalCashTable.getInstance().create(new LocalCash(transaction, (JSONObject) cash));
            }
          } else {
            for (Object cash : cashBreakdown) {
              ForeignCashTable.getInstance()
                  .create(new ForeignCash(transaction, (JSONObject) cash));
            }
          }

          Database.getInstance().commit();

          return transaction.id;
        } catch (Exception e) {
          Logger.error(e);
          Database.getInstance().rollback();

          throw e;
        }
      }

      case "check": {
        Logger.log("Saving check deposit transaction");

        try {
          CheckTransaction transaction = new CheckTransaction(info);
          JSONArray        checks      = (JSONArray) info.get("check_details");

          CheckTransactionTable.getInstance().create(transaction);

          for (Object check : checks) {
            CheckDetailsController.saveCheckDetails(transaction, (JSONObject) check);
          }

          Database.getInstance().commit();

          return transaction.id;
        } catch (Exception e) {
          Logger.error(e);
          Database.getInstance().rollback();

          throw e;
        }
      }

      case "payment": {
        Logger.log("Saving payment transaction");

        try {
          BusinessAccount account = null;
          try {
            account = BusinessAccountController.searchByName((String) info.get("business_name"));
          } catch (RecordSearchException e) {
            throw new SchemaCreationException(
                "Corporation/Insitute is not registered or name is invalid"
            );
          }

          PaymentTransaction transaction = new PaymentTransaction(account, info);

          PaymentTransactionTable.getInstance().create(transaction);

          switch (transaction.mode) {
            case "cash": {
              JSONArray cashBreakdown = (JSONArray) info.get("cash_breakdown");

              if (transaction.isLocal()) {
                for (Object cash : cashBreakdown) {
                  LocalCashTable.getInstance()
                      .create(new LocalCash(transaction, (JSONObject) cash));
                }
              } else {
                for (Object cash : cashBreakdown) {
                  ForeignCashTable.getInstance()
                      .create(new ForeignCash(transaction, (JSONObject) cash));
                }
              }

              break;
            }

            case "check": {
              JSONArray checks = (JSONArray) info.get("check_details");

              for (Object check : checks) {
                CheckDetailsController.saveCheckDetails(transaction, (JSONObject) check);
              }

              break;
            }

            case "debit": {
              Logger.log("Saving debit payment details");

              DebitPaymentTable.getInstance().create(new DebitPayment(transaction, info));

              break;
            }

            default: {
              throw new Exception("Invalid payment mode");
            }
          }

          Database.getInstance().commit();

          return transaction.id;
        } catch (Exception e) {
          Logger.error(e);
          Database.getInstance().rollback();

          throw e;
        }
      }

      default:
        throw new Exception("invalid transaction type");
    }
  }

  /**
   * Searches for transaction/s within the given date.
   *
   * @param date - transaction date
   * @return search result
   * @throws RecordSearchException when error occured while searching for records.
   */
  public static SearchResult<Transaction> searchByDate(Timestamp date)
      throws RecordSearchException {
    try {
      PreparedStatement stm = Database.getInstance().getConnection().prepareStatement(
          "SELECT * FROM TRANSACTIONS WHERE TransactionDate > ? AND TransactionDate < ?;"
      );

      LocalDateTime endDate       = date.toLocalDateTime().plusDays(1);
      String        endDateString = endDate.format(DateTimeFormatter.ofPattern("yyyy-M-d"));

      stm.setTimestamp(1, date);
      stm.setTimestamp(2, new Timestamp(Date.valueOf(endDateString).getTime()));

      LinkedList<Record> records = TransactionTable.getInstance().readStatement(stm);

      return createSearchResult(records);
    } catch (SQLException e) {
      Logger.error(e);

      throw new RecordSearchException("");
    }
  }

  /**
   * Gets the transaction with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return transaction with the matching ID
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static Transaction getTransaction(String id) throws RecordSearchException {
    String condition = "TransactionID = '" + id + "'";

    LinkedList<Record> records = TransactionTable.getInstance().read(condition);

    return createSearchResult(records).get(0);
  }

  /**
   * Searches for transaction/s with the matching account number.
   *
   * @param no - account no
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<Transaction> searchByAccountNo(String no)
      throws RecordSearchException {
    String condition = "AccountNo = '" + no + "'";

    LinkedList<Record> records = TransactionTable.getInstance().read(condition);

    return createSearchResult(records);
  }

  /**
   * Searches for transaction/s made by the given customer.
   *
   * @param last - customer last name
   * @param first - customer first name
   * @param middle - customer middle name
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<Transaction> searchByCustomerName(
      String last, String first, String middle
  ) throws RecordSearchException {
    String        table = CustomerTable.getInstance().getName();
    StringBuilder sb    = new StringBuilder();

    sb.append(" CUSTOMER.LName LIKE '" + last + "%' AND");
    sb.append(" CUSTOMER.FName LIKE '" + first + "%' AND");
    sb.append(" CUSTOMER.MName LIKE '" + middle + "%'");

    String             where = sb.toString();
    LinkedList<String> cols  = TransactionTable.getInstance().columns();

    LinkedList<Record> records = TransactionTable.getInstance()
        .join(table, cols.toArray(new String[0]), "CustomerID", where);

    return createSearchResult(records);
  }

  /**
   * Gets all transaction details.
   *
   * @return search result.
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<Transaction> getAll() throws RecordSearchException {
    String             table = CustomerTable.getInstance().getName();
    LinkedList<String> cols  = TransactionTable.getInstance().columns();

    LinkedList<Record> records = TransactionTable.getInstance()
        .join(table, cols.toArray(new String[0]), "CustomerID", null);

    return createSearchResult(records);
  }

  private static SearchResult<Transaction> createSearchResult(LinkedList<Record> records)
      throws RecordSearchException {
    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<Transaction> transactions = new SearchResult<>();

      for (Record record : records) {
        transactions.add(new Transaction(record));
      }

      return transactions;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading transaction records");
    }
  }

  /**
   * Creates a transaction's details JSON preview.
   *
   * @param id - transaction id
   * @return JSON transaction preview details
   * @throws Exception when an error occured while getting transaction details.
   */
  @SuppressWarnings("unchecked")
  public static JSONObject getPreview(String id) throws Exception {
    Logger.log("Getting transaction preivew with an ID of " + id);

    JSONObject  json        = new JSONObject();
    Transaction transaction = TransactionController.getTransaction(id);

    json.put("id", transaction.id);
    json.put("amount", transaction.amount);
    json.put("type", transaction.type);
    json.put("account", transaction.account.no);
    json.put("date", TimeUtil.timestampFormat(transaction.date));
    json.put("customer", transaction.customer.name.toJsonObject());

    switch (transaction.type) {
      case "deposit":
      case "withdraw": {
        CashTransaction cash = CashTransactionController.getCashTransaction(id);

        json.put("cashBreakdown", getCashBreakdown(id, cash.currency));
        json.put("currency", cash.currency);

        break;
      }

      case "check": {
        json.put("checkDetails", getCheckDetails(id));
        json.put("currency", "PHP");

        break;
      }

      case "payment": {
        PaymentTransaction payment  = PaymentTransactionController.getPaymentTransaction(id);
        BusinessAccount    account  = BusinessAccountController
            .getBusinessAccount(payment.account.no);
        JSONObject         jpayment = new JSONObject();

        jpayment.put("business", account.businessName);
        jpayment.put("reference", payment.referenceNo);
        jpayment.put("mode", payment.mode);

        json.put("currency", payment.currency);
        json.put("payment", jpayment);

        switch (payment.mode) {
          case "cash": {
            CashTransaction cash = CashTransactionController.getCashTransaction(id);

            json.put("cashBreakdown", getCashBreakdown(id, cash.currency));

            break;
          }

          case "check": {
            json.put("checkDetails", getCheckDetails(id));

            break;
          }

          case "debit": {
            JSONObject   jdebit = new JSONObject();
            DebitPayment debit  = DebitPaymentController.getDebitPayment(id);

            jdebit.put("no", debit.account.no);
            jdebit.put("product", debit.account.product.type);
            jdebit.put("type", debit.account.type);

            json.put("debit", jdebit);

            break;
          }

          default: {
            throw new Exception("Invalid payment mode");
          }
        }

        break;
      }

      default: {
        throw new Exception("Invalid transaction type");
      }
    }

    return json;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getCashBreakdown(String id, String currency) throws Exception {
    Logger.log("Getting cash breakdown with a TransactionID of " + id);

    JSONArray jcashBreakdown = new JSONArray();

    if (currency.equals("PHP")) {
      SearchResult<LocalCash> cashBreakdown = LocalCashController.searchByTransactionID(id);

      for (LocalCash local : cashBreakdown.result) {
        JSONObject jlocal = new JSONObject();

        jlocal.put("denom", local.denomination);
        jlocal.put("pieces", local.pieces);

        jcashBreakdown.add(jlocal);
      }
    } else {
      SearchResult<ForeignCash> cashBreakdown = ForeignCashController.searchByTransactionID(id);

      for (ForeignCash local : cashBreakdown.result) {
        JSONObject jlocal = new JSONObject();

        jlocal.put("serial", local.serial);
        jlocal.put("denom", local.denomination);

        jcashBreakdown.add(jlocal);
      }
    }

    return jcashBreakdown;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getCheckDetails(String id) throws Exception {
    Logger.log("Getting check details with a TransactionID of " + id);

    SearchResult<CheckDetails> checks       = CheckDetailsController.getCheckDetails(id);
    JSONArray                  checkDetails = new JSONArray();

    for (CheckDetails check : checks.result) {
      JSONObject jcheck = new JSONObject();

      jcheck.put("bank", check.bank);
      jcheck.put("no", check.no);
      jcheck.put("amount", check.amount);

      checkDetails.add(jcheck);
    }

    return checkDetails;
  }

}
