package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.CheckDetails;
import app.chinabank.model.schema.transaction.Transaction;
import app.chinabank.model.tables.CheckDetailsTable;
import app.chinabank.utils.Logger;
import java.util.LinkedList;
import org.json.simple.JSONObject;

final class CheckDetailsController {

  /**
   * Creates check deposit details, then stores in Database.
   *
   * @param transaction - check deposit transaction
   * @param info - check details
   */
  static void saveCheckDetails(Transaction transaction, JSONObject info) {
    CheckDetailsTable.getInstance().create(new CheckDetails(transaction, info));
  }

  /**
   * Searches check details with the matching transaction ID.
   *
   * @param id - transaction id
   * @return search result
   * @throws RecordSearchException an error occured while searching for records.
   */
  static SearchResult<CheckDetails> getCheckDetails(String id) throws RecordSearchException {
    LinkedList<Record> records = CheckDetailsTable.getInstance()
        .read("TransactionID = '" + id + "'");

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<CheckDetails> checks = new SearchResult<>();

      for (Record record : records) {
        checks.add(new CheckDetails(record));
      }

      return checks;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
