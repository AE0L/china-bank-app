package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.ContactPerson;
import app.chinabank.model.tables.ContactPersonTable;
import app.chinabank.utils.Logger;
import java.util.LinkedList;

public final class ContactPersonController {

  /**
   * Gets contact person with the matching contact person ID.
   *
   * @param id - contact person ID
   * @return contact person with the matchin ID
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static ContactPerson getContactPerson(String id) throws RecordSearchException {
    String condition = "ContactPersonID = '" + id + "'";

    LinkedList<Record> records = ContactPersonTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      return new ContactPerson(records.get(0));
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
