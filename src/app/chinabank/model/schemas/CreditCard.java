package app.chinabank.model.schemas;

import app.chinabank.controller.CreditCardController;
import app.chinabank.controller.CustomerController;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.utils.Logger;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class CreditCard implements Schema {

  public final String    cardNo;
  public final Customer  account;
  public final String    name;
  public final String    type;
  public final String    deliveryAddress;
  public final Timestamp expDate;
  public final Double    creditLimit;

  /**
   * Constructor.
   *
   * @param info - credit card information
   * @throws SchemaCreationException when an error occured while creating credit card
   */
  public CreditCard(JSONObject info) throws SchemaCreationException {
    JSONObject accountName   = (JSONObject) info.get("accname");
    String     accountLast   = (String) accountName.get("last");
    String     accountFirst  = (String) accountName.get("first");
    String     accountMiddle = (String) accountName.get("middle");

    try {
      SearchResult<Customer> search = CustomerController
          .searchCustomerFullName(accountLast, accountFirst, accountMiddle);

      this.account = search.get(0);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException(
          "Can't create credit card with the given customer name, please make sure the customer has"
              + " created a Customer Information."
      );
    }

    this.cardNo          = this.generateCardNo();
    this.name            = (String) info.get("name");
    this.type            = (String) info.get("type");
    this.deliveryAddress = (String) info.get("deliveryaddress");
    this.expDate         = this.generateExpDate();
    this.creditLimit     = this.generateCreditLimit();
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating credit card
   */
  public CreditCard(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    this.cardNo = (String) record.get("CreditCardNo");

    try {
      String id = (String) record.get("CustomerID");

      this.account = CustomerController.searchByID(id);
    } catch (RecordSearchException e) {
      Logger.error(e);

      throw new SchemaCreationException("error creating credit card with the given customer id");
    }

    this.name            = (String) record.get("CardName");
    this.type            = (String) record.get("CardType");
    this.deliveryAddress = (String) record.get("DeliveryAdd");
    this.expDate         = (Timestamp) record.get("ExpDate");
    this.creditLimit     = (Double) record.get("CreditLimit");
  }

  private String generateCardNo() throws SchemaCreationException {
    StringBuilder no         = new StringBuilder("555555");
    String        identifier = this.account.getIdentifier();
    String        checksum   = this.account.getChecksum();

    no.append(identifier.substring(0, 2));
    no.append(identifier.substring(2, 6));
    no.append(identifier.substring(6, 9) + checksum);

    try {
      CreditCard card = CreditCardController.searchByCardNo(no.toString()).get(0);

      System.out.println(card.cardNo + ", " + card.account.id + ", " + card.name);
      throw new SchemaCreationException("credit card number already existed");
    } catch (RecordSearchException e) {
      if (e.getMessage() == "No record/s was found or the Database didn't return any result") {
        return no.toString();
      }

      Logger.error(e);

      throw new SchemaCreationException("Error generating credit card no");
    }
  }

  private Timestamp generateExpDate() {
    return new Timestamp(new Date().getTime());
  }

  private Double generateCreditLimit() {
    return 1000d;
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("card_no", this.cardNo);
    json.put("account_name", this.account.name.toJsonObject());
    json.put("card_type", this.type);
    json.put("card_name", this.name);
    json.put("exp_date", this.expDate.toLocalDateTime().format(DateTimeFormatter.ISO_DATE));
    json.put("deliveryaddress", this.deliveryAddress);

    return json.toJSONString();
  }

}
