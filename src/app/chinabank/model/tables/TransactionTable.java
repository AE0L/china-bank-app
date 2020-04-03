package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.Transaction;

public class TransactionTable extends Table {

  private static TransactionTable INSTANCE = null;

  private TransactionTable() {
    super("TRANSACTIONS");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("TotalAmount", Type.DOUBLE);
    this.metadata.put("TransactionType", Type.STRING);
    this.metadata.put("AccountNo", Type.STRING);
    this.metadata.put("BranchID", Type.STRING);
    this.metadata.put("TransactionDate", Type.DATE);
    this.metadata.put("CustomerID", Type.STRING);
  }

  boolean create(Transaction transaction) {
    Record record = new Record();

    record.put("TransactionID", transaction.id);
    record.put("TotalAmount", transaction.amount);
    record.put("TransactionType", transaction.type);
    record.put("AccountNo", transaction.account.no);
    record.put("BranchID", transaction.branch);
    record.put("TransactionDate", transaction.date);
    record.put("CustomerID", transaction.customer.id);

    return this.create(record);
  }

  public static TransactionTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TransactionTable();
    }

    return INSTANCE;
  }

}
