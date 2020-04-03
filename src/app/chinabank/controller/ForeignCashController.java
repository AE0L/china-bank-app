package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.ForeignCash;
import app.chinabank.model.tables.ForeignCashTable;
import app.chinabank.utils.Logger;
import java.util.LinkedHashMap;
import java.util.LinkedList;

final class ForeignCashController {

  /**
   * Searches for foreign cash breakdown with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static SearchResult<ForeignCash> searchByTransactionID(String id) throws RecordSearchException {
    String condition = "TransactionID = '" + id + "'";

    LinkedList<Record> records = ForeignCashTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<ForeignCash> currencies = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        currencies.add(new ForeignCash(record));
      }

      return currencies;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
