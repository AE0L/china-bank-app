package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.schemas.BusinessAccount;
import app.chinabank.model.tables.BusinessAccountTable;
import app.chinabank.utils.Logger;
import java.util.LinkedList;

final class BusinessAccountController {

  /**
   * Gets business account with the matching account number.
   *
   * @param no - account number
   * @return business account record
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static BusinessAccount getBusinessAccount(String no) throws RecordSearchException {
    String condition = "AccountNo = '" + no + "'";

    LinkedList<Record> records = BusinessAccountTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      return new BusinessAccount(records.get(0));
    } catch (Exception e) {
      throw new RecordSearchException("Error reading records from the Database");
    }
  }

  /**
   * Gets business account with the matching business name.
   *
   * @param name - business name
   * @return account with the matching business name
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static BusinessAccount searchByName(String name) throws RecordSearchException {
    String condition = "BusinessName LIKE '" + name + "*'";

    LinkedList<Record> records = BusinessAccountTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      return new BusinessAccount(records.get(0));
    } catch (Exception e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }


}
