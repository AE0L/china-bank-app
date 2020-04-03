package app.chinabank.controller;

import app.chinabank.model.Database;
import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.CreditCard;
import app.chinabank.model.tables.CreditCardTable;
import app.chinabank.model.tables.CustomerTable;
import app.chinabank.utils.Logger;
import app.chinabank.utils.TimeUtil;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.json.simple.JSONObject;

public final class CreditCardController {

  /**
   * Creates credit card, then stored in the Database.
   *
   * @param info - credit card information
   * @return saved credit card's number
   * @throws Exception when an error occured while saving credit card information.
   */
  public static String saveCreditCard(JSONObject info) throws Exception {
    Logger.log("Ssving credit card");

    try {
      CreditCard creditCard = new CreditCard(info);

      CreditCardTable.getInstance().create(creditCard);

      Database.getInstance().commit();

      return creditCard.cardNo;
    } catch (SchemaCreationException e) {
      Database.getInstance().rollback();

      throw e;
    }
  }

  /**
   * Searches the CREDIT_CARD table with the exact card number.
   *
   * @param no - Credit Card Number
   * @return credit card with the exact credit card number.
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<CreditCard> searchByCardNo(String no) throws RecordSearchException {
    String condition = "CreditCardNo = '" + no + "'";

    return searchCard(condition, "CreditCardNo", no);
  }

  /**
   * Searches the CREDIT_CARD table with similar card name.
   *
   * @param name - Credit card name.
   * @return search results containing credit card records.
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<CreditCard> searchByCardName(String name)
      throws RecordSearchException {
    String condition = "CardName LIKE '" + name + "*'";

    return searchCard(condition, "CardName", name);
  }

  /**
   * Joins CREDIT_CARD and CUSTOMER tables then searches for similar names.
   *
   * @param last - Card holder's last name
   * @param first - Card holder's first name
   * @param middle - Card holder's middle name
   * @return search result containing credit card records.
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<CreditCard> searchByCardHolder(
      String last, String first, String middle
  ) throws RecordSearchException {
    Logger
        .log("Searching credit card/s with a CardHolder of " + last + ", " + first + " " + middle);
    String        table = CustomerTable.getInstance().getName();
    StringBuilder sb    = new StringBuilder();

    sb.append(" CUSTOMER.LName LIKE '" + last + "%' AND");
    sb.append(" CUSTOMER.FName LIKE '" + first + "%' AND");
    sb.append(" CUSTOMER.MName LIKE '" + middle + "%'");

    String             where = sb.toString();
    LinkedList<String> cols  = CreditCardTable.getInstance().columns();

    LinkedList<Record> records = CreditCardTable.getInstance()
        .join(table, cols.toArray(new String[0]), "CustomerID", where);

    return createSearchResult(records);
  }

  /**
   * Gets all credit card information.
   *
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<CreditCard> getAll() throws RecordSearchException {
    LinkedList<Record> records = CreditCardTable.getInstance().read();

    return createSearchResult(records);
  }

  private static SearchResult<CreditCard> searchCard(String condition, String att, String value)
      throws RecordSearchException {
    LinkedList<Record> records = CreditCardTable.getInstance().read(condition);

    return createSearchResult(records);
  }

  private static SearchResult<CreditCard> createSearchResult(LinkedList<Record> records)
      throws RecordSearchException {

    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<CreditCard> cards = new SearchResult<>();

      for (LinkedHashMap<String, Object> record : records) {
        cards.add(new CreditCard(record));
      }

      return cards;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading records from the Database");
    }
  }

  /**
   * Creates a JSON credit card preview information.
   *
   * @param no - credit card number
   * @return credit card JSON preview
   * @throws Exception when an error occured while creating credit card preview.
   */
  @SuppressWarnings("unchecked")
  public static JSONObject previewCard(String no) throws Exception {
    Logger.log("Getting credit card preview with a CardNo of " + no);

    CreditCard card  = CreditCardController.searchByCardNo(no).get(0);
    JSONObject birth = new JSONObject();

    birth.put("date", TimeUtil.formatTimestamp(card.account.birthDate, "yyyy-MM-dd"));
    birth.put("place", card.account.birthPlace);
    birth.put("country", card.account.birthCountry);

    JSONObject customer = new JSONObject();

    customer.put("id", card.account.id);
    customer.put("name", card.account.name.toJsonObject());
    customer.put("birth", birth);
    customer.put("gender", card.account.gender);
    customer.put("nation", card.account.nationality);
    customer.put("civilStatus", card.account.civilStatus);

    JSONObject json = new JSONObject();

    json.put("no", card.cardNo);
    json.put("name", card.name);
    json.put("type", card.type);
    json.put("limit", card.creditLimit);
    json.put("delivery", card.deliveryAddress);
    json.put("exp", TimeUtil.formatTimestamp(card.expDate, "yyyy-MM-dd"));
    json.put("customer", customer);

    return json;
  }

}
