package app.chinabank.model.schema.transaction;

import app.chinabank.model.exceptions.SchemaCreationException;
import org.json.simple.JSONObject;

public class CheckTransaction extends Transaction {

  public String checkType;

  /**
   * Constructor.
   *
   * @param info - check transction information
   * @throws SchemaCreationException when an error occured while creating check transaction
   */
  public CheckTransaction(JSONObject info) throws SchemaCreationException {
    super(info);

    this.checkType = (String) info.get("check_type");
  }

  @Override
  public String toJSON() {
    // NOT USED
    return null;
  }

}
