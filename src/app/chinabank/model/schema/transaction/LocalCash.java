package app.chinabank.model.schema.transaction;

import app.chinabank.controller.TransactionController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Schema;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class LocalCash implements Schema {

  public Transaction transaction;
  public Double      denomination;
  public Double      amount;
  public int         pieces;

  /**
   * Constructor.
   *
   * @param transaction - transaction where cash is used
   * @param info - local cash information
   */
  public LocalCash(Transaction transaction, JSONObject info) {
    this.transaction  = transaction;
    this.denomination = Double.parseDouble((String) info.get("denom"));
    this.pieces       = Integer.parseInt((String) info.get("piece"));
    this.amount       = this.denomination * this.pieces;
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating loal cash
   */
  public LocalCash(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    String id = (String) record.get("TransactionID");

    try {
      this.transaction = TransactionController.getTransaction((String) record.get("TransactionID"));
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("can't create local currency with id of " + id);
    }

    this.denomination = (Double) record.get("Denomination");
    this.pieces       = (int) record.get("Pieces");
    this.amount       = (Double) record.get("Amount");
  }

  @Override
  public String toJSON() {
    // TODO localcurrencyschema tojson
    return null;
  }

}
