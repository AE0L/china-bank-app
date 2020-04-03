package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.DebitPayment;

public class DebitPaymentTable extends Table {

  private static DebitPaymentTable INSTANCE = null;

  private DebitPaymentTable() {
    super("DEBIT_PAYMENT");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("AccountNo", Type.STRING);
  }

  public boolean create(DebitPayment payment) {
    Record record = new Record();

    record.put("TransactionID", payment.transaction.id);
    record.put("AccountNo", payment.account.no);

    return this.create(record);
  }

  public static DebitPaymentTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DebitPaymentTable();
    }

    return INSTANCE;
  }

}
