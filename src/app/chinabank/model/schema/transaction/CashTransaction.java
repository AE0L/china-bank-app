package app.chinabank.model.schema.transaction;

import app.chinabank.model.exceptions.SchemaCreationException;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class CashTransaction extends Transaction {

  public String currency;

  /**
   * Constructor.
   *
   * @param info - cash transaction information
   * @throws SchemaCreationException when an error occured while creating cash transaction
   */
  public CashTransaction(JSONObject info) throws SchemaCreationException {
    super(info);

    this.currency = (String) info.get("currency");
  }

  /**
   * Constructor.
   *
   * @param transaction - transaction where cash breakdown was used
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creatin cash transaction
   */
  public CashTransaction(Transaction transaction, LinkedHashMap<String, Object> record)
      throws SchemaCreationException {
    super(transaction);

    this.currency = (String) record.get("CashCurrency");
  }

  public boolean isLocal() {
    return this.currency.equals("PHP");
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSON() {
    JSONObject json = super.toJSONObject();

    json.put("currency", this.currency);

    return json.toJSONString();
  }

}
