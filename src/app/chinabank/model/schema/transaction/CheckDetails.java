package app.chinabank.model.schema.transaction;

import app.chinabank.controller.TransactionController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Schema;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class CheckDetails implements Schema {

  public Transaction transaction;
  public String      no;
  public Double      amount;
  public String      bank;

  /**
   * Constructor.
   *
   * @param transaction - transaction where check is used.
   * @param info - check information.
   */
  public CheckDetails(Transaction transaction, JSONObject info) {
    this.transaction = transaction;
    this.no          = (String) info.get("no");
    this.amount      = Double.parseDouble((String) info.get("amount"));
    this.bank        = (String) info.get("branch");
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating check details
   */
  public CheckDetails(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    try {
      this.transaction = TransactionController.getTransaction((String) record.get("TransactionID"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("can't create check details with given transaction id");
    }

    this.no     = (String) record.get("CheckNo");
    this.amount = (Double) record.get("CheckAmount");
    this.bank   = (String) record.get("CheckBank");
  }

  @Override
  public String toJSON() {
    // NOT USED
    return null;
  }

}
