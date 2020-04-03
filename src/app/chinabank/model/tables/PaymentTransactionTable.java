package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.PaymentTransaction;

public class PaymentTransactionTable extends Table {

  private static PaymentTransactionTable INSTANCE = null;

  private PaymentTransactionTable() {
    super("PAYMENT_TRANSACTION");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("ReferenceNo", Type.STRING);
    this.metadata.put("PaymentCurrency", Type.STRING);
    this.metadata.put("PaymentMode", Type.STRING);
  }

  public boolean create(PaymentTransaction transaction) {
    Record record = new Record();

    record.put("TransactionID", transaction.id);
    record.put("ReferenceNo", transaction.referenceNo);
    record.put("PaymentCurrency", transaction.currency);
    record.put("PaymentMode", transaction.mode);

    return TransactionTable.getInstance().create(transaction) && this.create(record);
  }

  public static PaymentTransactionTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PaymentTransactionTable();
    }

    return INSTANCE;
  }

}
