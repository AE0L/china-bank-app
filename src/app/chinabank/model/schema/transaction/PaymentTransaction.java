package app.chinabank.model.schema.transaction;

import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Account;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class PaymentTransaction extends Transaction {

  public String referenceNo;
  public String currency;
  public String mode;

  /**
   * Constructor.
   *
   * @param account - account used in transaction
   * @param info - payment transaction information
   * @throws SchemaCreationException when an error occured while creating payment transaction.
   */
  public PaymentTransaction(Account account, JSONObject info) throws SchemaCreationException {
    super(account, info);

    this.referenceNo = (String) info.get("reference_no");
    this.currency    = (String) info.get("currency");
    this.mode        = (String) info.get("payment_mode");
  }

  /**
   * Constructor.
   *
   * @param transaction - account used in transaction
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating payment transaction.
   */
  public PaymentTransaction(Transaction transaction, LinkedHashMap<String, Object> record)
      throws SchemaCreationException {
    super(transaction);

    this.referenceNo = (String) record.get("ReferenceNo");
    this.currency    = (String) record.get("PaymentCurrency");
    this.mode        = (String) record.get("PaymentMode");
  }

  @Override
  public String toJSON() {
    // NOT USED
    return null;
  }

  public boolean isLocal() {
    return this.currency.equals("PHP");
  }

}
