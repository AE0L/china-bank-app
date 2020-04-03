package app.chinabank.model.schemas;

import app.chinabank.controller.ProductProfileController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.tables.AccountTable;
import app.chinabank.utils.Logger;
import app.chinabank.utils.TimeUtil;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONObject;

public class Account implements Schema {

  public String         no;
  public Timestamp      dateOpen;
  public String         type;
  public ProductProfile product;
  public String         soaDelivery;

  /**
   * Constructor.
   *
   * @param info - account information
   * @throws SchemaCreationException when an error occured while creating account
   */
  public Account(JSONObject info) throws SchemaCreationException {
    String productType = (String) info.get("product_type");
    String passbook    = (String) info.get("passbook");

    if (Account.isPassbook(productType)) {
      productType += " " + passbook;
    }

    try {
      this.product = ProductProfileController.getProductProfile(productType);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("can't find product profile");
    }

    this.no          = this.generateNo();
    this.dateOpen    = TimeUtil.currentTimestamp();
    this.type        = (String) info.get("account_type");
    this.soaDelivery = (String) info.get("soa_disposition");
  }

  /**
   * Constructor.
   *
   * @param account - account
   */
  Account(Account account) {
    this.no          = account.no;
    this.dateOpen    = account.dateOpen;
    this.type        = account.type;
    this.soaDelivery = account.soaDelivery;
    this.product     = account.product;
  }

  /**
   * Constructor.
   *
   * @param record - Database Record
   * @throws SchemaCreationException when an error occured while creating account
   */
  public Account(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    String productType = (String) record.get("ProductType");

    try {
      this.product = ProductProfileController.getProductProfile(productType);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("can't find product profile");
    }

    this.no          = (String) record.get("AccountNo");
    this.dateOpen    = (Timestamp) record.get("DateOpen");
    this.type        = (String) record.get("AccountType");
    this.soaDelivery = (String) record.get("SoaDelivery");
  }

  private static boolean isPassbook(String type) {
    return type.equals("Passbook Savings");
  }

  private String generateNo() throws SchemaCreationException {
    int count = AccountTable.getInstance().count();

    if (count != -1) {
      int    pad         = 6;
      int    numericSize = 9;
      int    checksum    = 0;
      String id          = "A-";
      String countString = Integer.toString(count);
      String numeric     = RandomStringUtils.randomNumeric(numericSize);

      while (countString.length() != pad) {
        countString = '0' + countString;
      }

      id += countString + "-";
      id += numeric + "-";

      char[] numericArr = numeric.toCharArray();

      for (char num : numericArr) {
        int numInt = Integer.parseInt(String.valueOf(num));

        checksum += numInt;
      }

      checksum %= numericSize;

      id += checksum;

      return id;
    } else {
      throw new SchemaCreationException("error generating customer id");
    }
  }

  public static Boolean isBusinessAccount(Account account) {
    return account.type.equals("Corporation") || account.type.equals("Partnership")
        || account.type.equals("Sole Proprietorship");
  }

  @Override
  public String toJSON() {
    return this.toJSONObject().toJSONString();
  }

  @SuppressWarnings("unchecked")
  JSONObject toJSONObject() {
    JSONObject json = new JSONObject();

    json.put("account_no", this.no);
    json.put("date_open", TimeUtil.dateFormat(this.dateOpen));
    json.put("account_type", this.type);
    json.put("product_type", this.product.type);
    json.put("soa_delivery", this.soaDelivery);

    return json;
  }

}
