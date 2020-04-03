package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.DebitPayment;
import app.chinabank.model.tables.DebitPaymentTable;
import app.chinabank.utils.Logger;
import java.util.LinkedList;

final class DebitPaymentController {

  /**
   * Gets debit payment with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return debit payment with the matching ID
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static DebitPayment getDebitPayment(String id) throws RecordSearchException {
    LinkedList<Record> records = DebitPaymentTable.getInstance()
        .read("TransactionID = '" + id + "'");

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      return new DebitPayment(records.get(0));
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("can't search debit payment with TransactionID: " + id);
    }
  }

}
