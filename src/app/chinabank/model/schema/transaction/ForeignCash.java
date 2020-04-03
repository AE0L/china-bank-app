package app.chinabank.model.schema.transaction;

import app.chinabank.controller.TransactionController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Schema;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class ForeignCash implements Schema {

  public Transaction transaction;
  public Double      denomination;
  public String      serial;

  /**
   * Constructor.
   *
   * @param transaction - transaction where cash is used
   * @param info - foreign cash information
   */
  public ForeignCash(Transaction transaction, JSONObject info) {
    this.transaction  = transaction;
    this.denomination = Double.parseDouble((String) info.get("denom"));
    this.serial       = (String) info.get("serial");
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating foreign cash
   */
  public ForeignCash(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    String id = (String) record.get("TransactionID");

    try {
      this.transaction = TransactionController.getTransaction((String) record.get("TransactionID"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("can't create local currency with id of " + id);
    }

    this.denomination = (Double) record.get("Denomination");
    this.serial       = (String) record.get("SerialNo");
  }

  @Override
  public String toJSON() {
    // TODO foreigncurrencyschema tojson
    return null;
  }

}
