package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.BusinessAccount;

public class BusinessAccountTable extends Table {

  private static BusinessAccountTable INSTANCE = null;

  private BusinessAccountTable() {
    super("BUSINESS_ACCOUNT");

    this.metadata.put("AccountNo", Type.STRING);
    this.metadata.put("BusinessName", Type.STRING);
    this.metadata.put("ContactPersonID", Type.STRING);
  }

  public Boolean create(BusinessAccount account) {
    Record record = new Record();

    record.put("AccountNo", account.no);
    record.put("BusinessName", account.businessName);
    record.put("ContactPersonID", account.contact.id);

    return ContactPersonTable.getInstance().create(account.contact)
        && AccountTable.getInstance().create(account) && this.create(record);
  }

  public static BusinessAccountTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BusinessAccountTable();
    }

    return INSTANCE;
  }

}
