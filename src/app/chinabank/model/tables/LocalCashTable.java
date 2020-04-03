package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.LocalCash;

public class LocalCashTable extends Table {

  private static LocalCashTable INSTANCE = null;

  public LocalCashTable() {
    super("LOCAL_CASH_BREAKDOWN");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("Denomination", Type.DOUBLE);
    this.metadata.put("Pieces", Type.INTEGER);
    this.metadata.put("Amount", Type.DOUBLE);
  }

  public boolean create(LocalCash cash) {
    Record record = new Record();

    record.put("TransactionID", cash.transaction.id);
    record.put("Denomination", cash.denomination);
    record.put("Pieces", cash.pieces);
    record.put("Amount", cash.amount);

    return this.create(record);
  }

  public static LocalCashTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new LocalCashTable();
    }

    return INSTANCE;
  }

}
