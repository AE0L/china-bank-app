package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.AccountHolder;

public class AccountHolderTable extends Table {

  private static AccountHolderTable INSTANCE = null;

  private AccountHolderTable() {
    super("ACCOUNT_HOLDER");

    this.metadata.put("AccountNo", Type.STRING);
    this.metadata.put("CustomerID", Type.STRING);
  }

  public Boolean create(AccountHolder accountHolder) {
    Record record = new Record();

    record.put("AccountNo", accountHolder.account.no);
    record.put("CustomerID", accountHolder.customer.id);

    return this.create(record);
  }

  public static AccountHolderTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AccountHolderTable();
    }

    return INSTANCE;
  }

}
