package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schema.transaction.CheckDetails;

public class CheckDetailsTable extends Table {

  private static CheckDetailsTable INSTANCE = null;

  private CheckDetailsTable() {
    super("CHECK_DETAILS");

    this.metadata.put("TransactionID", Type.STRING);
    this.metadata.put("CheckNo", Type.STRING);
    this.metadata.put("CheckAmount", Type.DOUBLE);
    this.metadata.put("CheckBank", Type.STRING);
  }

  public boolean create(CheckDetails check) {
    Record record = new Record();

    record.put("TransactionID", check.transaction.id);
    record.put("CheckNo", check.no);
    record.put("CheckAmount", check.amount);
    record.put("CheckBank", check.bank);

    return this.create(record);
  }

  public static CheckDetailsTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CheckDetailsTable();
    }

    return INSTANCE;
  }


}
