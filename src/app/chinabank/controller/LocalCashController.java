package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.LocalCash;
import app.chinabank.model.tables.LocalCashTable;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import java.util.LinkedList;

final class LocalCashController {

  /**
   * Searches for the local cash breakdown with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static SearchResult<LocalCash> searchByTransactionID(String id) throws RecordSearchException {
    String condition = "TransactionID = '" + id + "'";

    LinkedList<Record> records = LocalCashTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<LocalCash> currencies = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        currencies.add(new LocalCash(record));
      }

      return currencies;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
