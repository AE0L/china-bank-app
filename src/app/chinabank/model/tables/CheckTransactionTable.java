package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.CheckTransaction;

public class CheckTransactionTable extends Table {

  private static CheckTransactionTable INSTANCE = null;

  private CheckTransactionTable() {
    super("CHECK_TRANSACTION");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("CheckType", Type.STRING);
  }

  public boolean create(CheckTransaction transaction) {
    Record record = new Record();

    record.put("TransactionID", transaction.id);
    record.put("CheckType", transaction.checkType);

    return TransactionTable.getInstance().create(transaction) && this.create(record);
  }

  public static CheckTransactionTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CheckTransactionTable();
    }

    return INSTANCE;
  }

}
