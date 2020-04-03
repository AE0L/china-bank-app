package app.chinabank.model.schemas;

import app.chinabank.controller.AccountController;
import app.chinabank.controller.CustomerController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class AccountHolder implements Schema {

  public Account  account;
  public Customer customer;

  /**
   * Constructor.
   *
   * @param account - account opened
   * @param info - account holder information
   * @throws SchemaCreationException when an error occured while creating account holder
   */
  public AccountHolder(Account account, JSONObject info) throws SchemaCreationException {
    String last   = (String) info.get("last");
    String first  = (String) info.get("first");
    String middle = (String) info.get("middle");

    try {
      this.customer = CustomerController.searchCustomerFullName(last, first, middle).get(0);
    } catch (RecordSearchException e) {
      throw new SchemaCreationException(
          "Can't find signatory: " + last + ", " + first + " " + middle
              + ", please make sure each signatory has created a Customer Information"
      );
    }

    this.account = account;
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating account holder
   */
  public AccountHolder(LinkedHashMap<String, Object> record) throws SchemaCreationException {

    String accountNo  = (String) record.get("AccountNo");
    String customerID = (String) record.get("CustomerID");

    try {
      this.account  = AccountController.getAccount(accountNo);
      this.customer = CustomerController.searchByID(customerID);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("Error getting account holder from the Database");
    }
  }

  @Override
  public String toJSON() {
    JSONObject json = new JSONObject();

    return json.toJSONString();
  }

}
