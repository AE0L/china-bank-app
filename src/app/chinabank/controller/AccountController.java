package app.chinabank.controller;

import app.chinabank.model.Database;
import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Account;
import app.chinabank.model.schemas.AccountHolder;
import app.chinabank.model.schemas.BusinessAccount;
import app.chinabank.model.tables.AccountHolderTable;
import app.chinabank.model.tables.AccountTable;
import app.chinabank.model.tables.BusinessAccountTable;
import app.chinabank.utils.Logger;
import app.chinabank.utils.TimeUtil;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class AccountController {

  /**
   * Creates the correct type of account, then store it on the Database.
   *
   * @param data - account information.
   * @return account number of the saved record.
   * @throws Exception when an error occured while saving the account
   */
  public static String saveAccount(JSONObject data) throws Exception {
    String     accountType = (String) data.get("account_type");
    JSONObject info        = (JSONObject) data.get("account");

    switch (accountType) {
      case "individual": {
        try {
          Logger.log("Saving individual account");

          Account       account = new Account(info);
          AccountHolder holder  = new AccountHolder(account, (JSONObject) info.get("account_name"));

          AccountTable.getInstance().create(account);
          AccountHolderTable.getInstance().create(holder);
          Database.getInstance().commit();

          return account.no;
        } catch (Exception e) {
          Database.getInstance().rollback();

          throw e;
        }
      }

      case "joint": {
        try {
          Logger.log("Saving joint account");

          Account       account     = new Account(info);
          AccountHolder holder      = new AccountHolder(
              account, (JSONObject) info.get("account_name")
          );
          JSONArray     signatories = (JSONArray) info.get("signatories");

          AccountTable.getInstance().create(account);

          for (Object signatory : signatories) {
            AccountHolderTable.getInstance()
                .create(new AccountHolder(account, (JSONObject) signatory));
          }

          AccountHolderTable.getInstance().create(holder);

          Database.getInstance().commit();

          return account.no;
        } catch (Exception e) {
          Database.getInstance().rollback();

          throw e;
        }
      }

      case "sole-business": {
        try {
          Logger.log("Saving sole business account");

          BusinessAccount account = new BusinessAccount(info);

          BusinessAccountTable.getInstance().create(account);
          Database.getInstance().commit();

          return account.no;
        } catch (Exception e) {
          Database.getInstance().rollback();

          throw e;
        }
      }

      case "joint-business": {
        try {
          Logger.log("Saving joint business account");

          BusinessAccount account     = new BusinessAccount(info);
          JSONArray       signatories = (JSONArray) info.get("signatories");

          BusinessAccountTable.getInstance().create(account);

          for (Object signatory : signatories) {
            AccountHolderTable.getInstance()
                .create(new AccountHolder(account, (JSONObject) signatory));
          }

          Database.getInstance().commit();

          return account.no;
        } catch (Exception e) {
          Logger.error(e);
          Database.getInstance().rollback();

          throw e;
        }
      }

      default: {
        throw new Exception("Invalid account type");
      }
    }
  }

  /**
   * Joins the <tt>ACCOUNT</tt>, <tt>ACCOUNT_HOLDER</tt>, <tt>CUSTOMER</tt> tables then find the
   * records with the matching name. Used in the application account search.
   *
   * @param last - account holder last name
   * @param first - account holder first name
   * @param middle - account holder middle name
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<AccountSearchResult> searchByAccountHolder(
      String last, String first, String middle
  ) throws RecordSearchException {
    try (PreparedStatement stm = Database.getInstance().getConnection().prepareStatement(
        "SELECT ACCOUNT.AccountNo, CUSTOMER.LName & ', ' & CUSTOMER.FName & ' ' & CUSTOMER.MName "
            + "AS AccountHolder, AccountType, ProductType, DateOpen FROM ACCOUNT, ACCOUNT_HOLDER, "
            + "CUSTOMER WHERE ACCOUNT.AccountNo = ACCOUNT_HOLDER.AccountNo AND "
            + "ACCOUNT_HOLDER.CustomerID = CUSTOMER.CustomerID AND CUSTOMER.LName LIKE ? AND "
            + "CUSTOMER.FName LIKE ? AND CUSTOMER.MName LIKE ?"
    );) {
      stm.setString(1, last + "%");
      stm.setString(2, first + "%");
      stm.setString(3, middle + "%");

      LinkedList<Record> records = AccountTable.getInstance().toRecords(
          stm.executeQuery(),
          new String[] {"AccountNo", "AccountHolder", "AccountType", "ProductType", "DateOpen"}
      );

      if (records == null || records.size() == 0) {
        throw new RecordSearchException();
      }

      SearchResult<AccountSearchResult> accounts = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        accounts.add(new AccountSearchResult(record));
      }

      return accounts;
    } catch (SQLException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

  /**
   * Searches for account/s with the matching account number. Used in the application account
   * search.
   *
   * @param no - account number
   * @return search result
   * @throws RecordSearchException when an error occured while searchin for records.
   */
  public static SearchResult<AccountSearchResult> getAccountByNo(String no)
      throws RecordSearchException {
    try (PreparedStatement stm = Database.getInstance().getConnection().prepareStatement(
        "SELECT ACCOUNT.AccountNo, CUSTOMER.LName & ', ' & CUSTOMER.FName & ' ' & CUSTOMER.MName "
            + "AS AccountHolder, AccountType, ProductType, DateOpen FROM ACCOUNT, ACCOUNT_HOLDER, "
            + "CUSTOMER WHERE ACCOUNT.AccountNo = ACCOUNT_HOLDER.AccountNo AND "
            + "ACCOUNT_HOLDER.CustomerID = CUSTOMER.CustomerID AND ACCOUNT.AccountNo = ?"
    );) {
      stm.setString(1, no);

      LinkedList<Record> records = AccountTable.getInstance().toRecords(
          stm.executeQuery(),
          new String[] {"AccountNo", "AccountHolder", "AccountType", "ProductType", "DateOpen"}
      );

      if (records == null || records.size() == 0) {
        throw new RecordSearchException();
      }

      SearchResult<AccountSearchResult> accounts = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        accounts.add(new AccountSearchResult(record));
      }

      return accounts;
    } catch (SQLException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

  /**
   * Find the account with the matching account number.
   *
   * @param no - account number
   * @return account with the mathching account number
   * @throws RecordSearchException when an error occured while searchiing for records.
   */
  public static Account getAccount(String no) throws RecordSearchException {
    String condition = "AccountNo = '" + no + "'";

    LinkedList<Record> records = AccountTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      return new Account(records.get(0));
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

  /**
   * Gets all account records.
   *
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<AccountSearchResult> getAll() throws RecordSearchException {
    LinkedList<Record> records = AccountTable.getInstance().read();

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    SearchResult<AccountSearchResult> accounts = new SearchResult<>();

    for (Record record : records) {
      accounts.add(new AccountSearchResult(record));
    }

    return accounts;
  }

  /**
   * Creates JSON preview information for the account with the matching account number.
   *
   * @param no - account number
   * @return account JSON preview information.
   */
  @SuppressWarnings("unchecked")
  public static JSONObject previewAccount(String no) {
    JSONObject json = new JSONObject();
    try {
      Account account = AccountController.getAccount(no);

      json.put("no", account.no);
      json.put("dateOpen", TimeUtil.dateFormat(account.dateOpen));
      json.put("product", account.product.type);
      json.put("type", account.type);
      json.put("currency", account.product.currency);
      json.put("soa", account.soaDelivery);

      if (Account.isBusinessAccount(account)) {
        json.put("isBusiness", true);
        JSONObject      businessJson = new JSONObject();
        JSONObject      contact      = new JSONObject();
        BusinessAccount business     = BusinessAccountController.getBusinessAccount(no);

        businessJson.put("name", business.businessName);
        contact.put("name", business.contact.name.toJsonObject());
        contact.put("position", business.contact.position);
        contact.put("no", business.contact.contact);
        contact.put("email", business.contact.email);
        businessJson.put("contact", contact);
        json.put("business", businessJson);
      }

      try {
        SearchResult<AccountHolder> holders      = AccountHolderController.getHolders(no);
        JSONArray                   holdersArray = new JSONArray();

        for (AccountHolder holder : holders.result) {
          JSONObject holderJson = new JSONObject();

          holderJson.put("id", holder.customer.id);

          JSONObject birth = new JSONObject();

          birth.put("date", TimeUtil.dateFormat(holder.customer.birthDate));
          birth.put("place", holder.customer.birthPlace);
          birth.put("country", holder.customer.birthCountry);

          holderJson.put("birth", birth);
          holderJson.put("name", holder.customer.name.toJsonObject());
          holderJson.put("gender", holder.customer.gender);
          holderJson.put("nation", holder.customer.nationality);
          holderJson.put("civilStatus", holder.customer.civilStatus);

          holdersArray.add(holderJson);
        }

        json.put("holders", holdersArray);
      } catch (RecordSearchException e) {
        if (e.getMessage() == "No record/s was found or the Database didn't return any result") {
          json.put("holders", new JSONArray());
        }
      }

      return json;
    } catch (RecordSearchException e) {
      Logger.error(e);
    }
    return null;
  }

}
