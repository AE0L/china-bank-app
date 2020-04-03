package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.Account;

public class AccountTable extends Table {

  private static AccountTable INSTANCE = null;

  private AccountTable() {
    super("ACCOUNT");

    this.metadata.put("AccountNo", Type.STRING);
    this.metadata.put("DateOpen", Type.DATE);
    this.metadata.put("AccountType", Type.STRING);
    this.metadata.put("SoaDelivery", Type.STRING);
    this.metadata.put("ProductType", Type.STRING);
  }

  public Boolean create(Account account) {
    Record record = new Record();

    record.put("AccountNo", account.no);
    record.put("ProductType", account.product.type);
    record.put("DateOpen", account.dateOpen);
    record.put("AccountType", account.type);
    record.put("SoaDelivery", account.soaDelivery);

    return this.create(record);
  }

  public static AccountTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AccountTable();
    }

    return INSTANCE;
  }

}
