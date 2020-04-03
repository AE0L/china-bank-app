package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.ForeignCash;

public class ForeignCashTable extends Table {

  private static ForeignCashTable INSTANCE = null;

  private ForeignCashTable() {
    super("FOREIGN_CASH_BREAKDOWN");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("SerialNo", Type.STRING);
    this.metadata.put("Denomination", Type.DOUBLE);
  }

  public boolean create(ForeignCash cash) {
    Record record = new Record();

    record.put("TransactionID", cash.transaction.id);
    record.put("SerialNo", cash.serial);
    record.put("Denomination", cash.denomination);

    return this.create(record);
  }

  public static ForeignCashTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ForeignCashTable();
    }

    return INSTANCE;
  }

}
