package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.ContactPerson;

public class ContactPersonTable extends Table {

  private static ContactPersonTable INSTANCE = null;

  private ContactPersonTable() {
    super("CONTACT_PERSON");

    this.metadata.put("ContactPersonID", Type.STRING);
    this.metadata.put("CPFName", Type.STRING);
    this.metadata.put("CPMName", Type.STRING);
    this.metadata.put("CPLName", Type.STRING);
    this.metadata.put("CPPosition", Type.STRING);
    this.metadata.put("CPContactNo", Type.STRING);
    this.metadata.put("CPEmail", Type.STRING);
  }

  boolean create(ContactPerson contactPerson) {
    Record record = new Record();

    record.put("ContactPersonID", contactPerson.id);
    record.put("CPFName", contactPerson.name.first);
    record.put("CPMName", contactPerson.name.middle);
    record.put("CPLName", contactPerson.name.last);
    record.put("CPPosition", contactPerson.position);
    record.put("CPContactNo", contactPerson.contact);
    record.put("CPEmail", contactPerson.email);

    return super.create(record);
  }

  public static ContactPersonTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactPersonTable();
    }

    return INSTANCE;
  }

}
