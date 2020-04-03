package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.CashTransaction;
import app.chinabank.model.schema.transaction.Transaction;
import app.chinabank.model.tables.CashTransactionTable;
import app.chinabank.utils.Logger;
import java.util.LinkedList;

final class CashTransactionController {

  /**
   * Gets cash transaction with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return cash transaction with the matching ID
   * @throws RecordSearchException an error occured while searching for records.
   */
  static CashTransaction getCashTransaction(String id) throws RecordSearchException {
    String condition = "TransactionID = '" + id + "'";

    LinkedList<Record> records = CashTransactionTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      Transaction transaction = TransactionController
          .getTransaction((String) records.get(0).get("TransactionID"));

      return new CashTransaction(transaction, records.get(0));
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the databse");
    }
  }

}
