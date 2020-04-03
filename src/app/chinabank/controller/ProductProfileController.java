package app.chinabank.controller;

import app.chinabank.model.Record;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.schemas.ProductProfile;
import app.chinabank.model.tables.ProductProfileTable;
import java.util.LinkedList;

public class ProductProfileController {

  /**
   * Gets product profile with the matching product type.
   *
   * @param type - product type
   * @return proudct profile with the mathchin type
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static ProductProfile getProductProfile(String type) throws RecordSearchException {
    String condition = "ProductType = '" + type + "'";

    LinkedList<Record> records = ProductProfileTable.getInstance().read(condition);

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    return new ProductProfile(records.get(0));
  }

}
