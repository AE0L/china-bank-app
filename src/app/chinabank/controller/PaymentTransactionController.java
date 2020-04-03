package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schema.transaction.PaymentTransaction;
import app.chinabank.model.schema.transaction.Transaction;
import app.chinabank.model.tables.PaymentTransactionTable;
import app.chinabank.utils.Logger;

final class PaymentTransactionController {

  /**
   * Gets payment transaction with the matching transaction ID.
   *
   * @param id - transaction ID
   * @return payment transaction with the matching ID
   * @throws RecordSearchException when an error occured while searching for records.
   */
  static PaymentTransaction getPaymentTransaction(String id) throws RecordSearchException {
    Record record = PaymentTransactionTable.getInstance().read("TransactionID = '" + id + "'")
        .get(0);

    if (record == null || record.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      Transaction transaction = TransactionController
          .getTransaction((String) record.get("TransactionID"));
      return new PaymentTransaction(transaction, record);
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

}
