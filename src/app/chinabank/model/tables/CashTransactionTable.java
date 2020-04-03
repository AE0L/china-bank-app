package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.CashTransaction;

public class CashTransactionTable extends Table {

  private static CashTransactionTable INSTANCE = null;

  private CashTransactionTable() {
    super("CASH_TRANSACTIONS");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("CashCurrency", Type.STRING);
  }

  public boolean create(CashTransaction transaction) {
    Record record = new Record();

    record.put("TransactionID", transaction.id);
    record.put("CashCurrency", transaction.currency);

    return TransactionTable.getInstance().create(transaction) && this.create(record);
  }

  public static CashTransactionTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CashTransactionTable();
    }

    return INSTANCE;
  }

}
