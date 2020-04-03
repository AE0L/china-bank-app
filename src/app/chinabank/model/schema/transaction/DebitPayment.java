package app.chinabank.model.schema.transaction;

import app.chinabank.controller.AccountController;
import app.chinabank.controller.TransactionController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Account;
import app.chinabank.model.schemas.Schema;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class DebitPayment implements Schema {

  public Transaction transaction;
  public Account     account;

  /**
   * Constructor.
   *
   * @param transaction - payment transaction where debit account is used
   * @param info - debit payment information
   * @throws SchemaCreationException when an error occured while creating debit payment
   */
  public DebitPayment(Transaction transaction, JSONObject info) throws SchemaCreationException {
    try {
      this.account = AccountController.getAccount((String) info.get("debit_no"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("Invalid debit account number");
    }

    this.transaction = transaction;
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating debit payment
   */
  public DebitPayment(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    try {
      this.transaction = TransactionController.getTransaction((String) record.get("TransactionID"));
      this.account     = AccountController.getAccount((String) record.get("AccountNo"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("error creating debit payment with given TransactionID");
    }
  }

  @Override
  public String toJSON() {
    // TODO debit payment tojson()
    return null;
  }

}
