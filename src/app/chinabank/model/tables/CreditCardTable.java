package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.CreditCard;

public class CreditCardTable extends Table {

  private static CreditCardTable INSTANCE = null;

  private CreditCardTable() {
    super("CREDIT_CARD");

    this.metadata.put("CreditCardNo", Type.STRING);
    this.metadata.put("CustomerID", Type.STRING);
    this.metadata.put("CardName", Type.STRING);
    this.metadata.put("CardType", Type.STRING);
    this.metadata.put("DeliveryAdd", Type.STRING);
    this.metadata.put("ExpDate", Type.DATE);
    this.metadata.put("CreditLimit", Type.DOUBLE);
  }

  public boolean create(CreditCard creditcard) {
    Record record = new Record();

    record.put("CreditCardNo", creditcard.cardNo);
    record.put("CustomerID", creditcard.account.id);
    record.put("CardName", creditcard.name);
    record.put("CardType", creditcard.type);
    record.put("DeliveryAdd", creditcard.deliveryAddress);
    record.put("ExpDate", creditcard.expDate);
    record.put("CreditLimit", creditcard.creditLimit);

    return this.create(record);
  }

  public static CreditCardTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CreditCardTable();
    }

    return INSTANCE;
  }

}
