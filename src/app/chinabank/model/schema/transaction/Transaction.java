package app.chinabank.model.schema.transaction;

import app.chinabank.ChinaBank;
import app.chinabank.controller.AccountController;
import app.chinabank.controller.CustomerController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Account;
import app.chinabank.model.schemas.Customer;
import app.chinabank.model.schemas.Schema;
import app.chinabank.model.tables.TransactionTable;
import app.chinabank.utils.Logger;
import app.chinabank.utils.TimeUtil;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;
import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONObject;

public class Transaction implements Schema {

  public String    id;
  public String    type;
  public Timestamp date;
  public Account   account;
  public Customer  customer;
  public Double    amount;
  public String    branch;

  /**
   * Constructor.
   *
   * @param info - transaction information
   * @throws SchemaCreationException when an error occured while creating transaction.
   */
  Transaction(JSONObject info) throws SchemaCreationException {
    try {
      String accountNo = (String) info.get("account_no");

      this.account = AccountController.getAccount(accountNo);
    } catch (RecordSearchException e) {
      throw new SchemaCreationException("Invalid account number");
    }

    try {
      JSONObject name   = (JSONObject) info.get("customer_name");
      String     last   = (String) name.get("last");
      String     first  = (String) name.get("first");
      String     middle = (String) name.get("middle");

      this.customer = CustomerController.searchCustomerFullName(last, first, middle).get(0);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException(
          "Can't find customer information, please make sure that the customer has created a "
              + "Customer Information"
      );
    }

    this.date   = TimeUtil.currentTimestamp();
    this.type   = (String) info.get("type");
    this.amount = Double.parseDouble((String) info.get("total_amount"));
    this.branch = ChinaBank.BRANCH_ID;
    this.id     = this.generateID();
  }

  /**
   * Constructor.
   *
   * @param account - account used in transaction
   * @param info - transaction information
   * @throws SchemaCreationException when an error occured while creating transaction.
   */
  Transaction(Account account, JSONObject info) throws SchemaCreationException {
    try {
      JSONObject name   = (JSONObject) info.get("customer_name");
      String     last   = (String) name.get("last");
      String     first  = (String) name.get("first");
      String     middle = (String) name.get("middle");

      this.customer = CustomerController.searchCustomerFullName(last, first, middle).get(0);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException(
          "Can't find customer information, please make sure that the customer has created a "
              + "Customer Information"
      );
    }

    this.account = account;
    this.date    = TimeUtil.currentTimestamp();
    this.type    = (String) info.get("type");
    this.amount  = Double.parseDouble((String) info.get("total_amount"));
    this.branch  = ChinaBank.BRANCH_ID;
    this.id      = this.generateID();
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating transaction.
   */
  public Transaction(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    try {
      this.account  = AccountController.getAccount((String) record.get("AccountNo"));
      this.customer = CustomerController.searchByID((String) record.get("CustomerID"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("Error getting transaction from the Database");
    }

    this.id     = (String) record.get("TransactionID");
    this.type   = (String) record.get("TransactionType");
    this.date   = (Timestamp) record.get("TransactionDate");
    this.amount = (Double) record.get("TotalAmount");
    this.branch = (String) record.get("BranchID");
  }

  Transaction(Transaction transaction) {
    this.account  = transaction.account;
    this.customer = transaction.customer;
    this.id       = transaction.id;
    this.type     = transaction.type;
    this.date     = transaction.date;
    this.amount   = transaction.amount;
    this.branch   = transaction.branch;
  }

  private String generateID() throws SchemaCreationException {
    int count = TransactionTable.getInstance().count();

    if (count == -1) {
      throw new SchemaCreationException("error counting transaction table");
    }

    String countString = Integer.toString(count);
    int    countPad    = 5;

    while (countString.length() != countPad) {
      countString = "0" + countString;
    }

    String   randNum      = RandomStringUtils.randomNumeric(5);
    String[] randNumChars = randNum.split("");
    int      sum          = Arrays.asList(randNumChars).stream().map(Integer::valueOf)
        .reduce(0, Integer::sum);

    int checksum = sum % 5;

    return branch + "-" + countString + "-" + randNum + "-" + checksum;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("transaction_id", this.id);
    json.put("transaction_type", this.type);
    json.put("total_amount", this.amount);
    json.put("customer_name", this.customer.name.toJsonObject());
    json.put("account_no", this.account.no);
    json.put("transaction_date", TimeUtil.timestampFormat(this.date));

    return json.toJSONString();
  }

  /**
   * Converts transaction to JSON.
   *
   * @return JSON object
   */
  @SuppressWarnings("unchecked")
  JSONObject toJSONObject() {
    JSONObject json = new JSONObject();

    json.put("account_no", this.account.no);
    json.put("transaction_id", this.id);
    json.put("transaction_type", this.type);
    json.put("transaction_date", this.date);
    json.put("total_amount", this.amount);
    json.put("transaction_branch", this.branch);

    return json;
  }

}
