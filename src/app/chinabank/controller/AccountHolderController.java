package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.AccountHolder;
import app.chinabank.model.tables.AccountHolderTable;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import java.util.LinkedList;

final class AccountHolderController {

  /**
   * Gets all the holders for the account with the matching account number.
   *
   * @param no - account number
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static SearchResult<AccountHolder> getHolders(String no) throws RecordSearchException {
    String condition = "AccountNo = '" + no + "'";

    LinkedList<Record> records = AccountHolderTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<AccountHolder> holders = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        holders.add(new AccountHolder(record));
      }

      return holders;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
