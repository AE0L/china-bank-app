package app.chinabank.model.schemas;

import app.chinabank.model.Name;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.tables.ContactPersonTable;
import java.util.LinkedHashMap;
import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONObject;

public class ContactPerson implements Schema {

  public String id;
  public Name   name;
  public String contact;
  public String position;
  public String email;

  /**
   * Constructor.
   *
   * @param info - contact person inforamation
   * @throws SchemaCreationException when an error occured while creating contact perosn
   */
  ContactPerson(JSONObject info) throws SchemaCreationException {
    this.id = this.generateID();

    JSONObject personName = (JSONObject) info.get("name");
    String     last       = (String) personName.get("last");
    String     first      = (String) personName.get("first");
    String     middle     = (String) personName.get("middle");

    this.name     = new Name(last, first, middle);
    this.contact  = (String) info.get("contact");
    this.position = (String) info.get("position");
    this.email    = (String) info.get("email");
  }

  /**
   * Constructor.
   *
   * @param record - Database record
   * @throws SchemaCreationException when an error occured while creating contact person.
   */
  public ContactPerson(LinkedHashMap<String, Object> record) throws SchemaCreationException {
    this.id = (String) record.get("ContactPersonID");

    String last   = (String) record.get("CPLName");
    String first  = (String) record.get("CPFName");
    String middle = (String) record.get("CPMName");

    this.name     = new Name(last, first, middle);
    this.position = (String) record.get("CPPosition");
    this.contact  = (String) record.get("CPContactNo");
    this.email    = (String) record.get("CPEmail");
  }

  private String generateID() throws SchemaCreationException {
    int count = ContactPersonTable.getInstance().count();

    if (count != -1) {
      int    pad         = 6;
      int    numericSize = 8;
      int    checksum    = 0;
      String id          = "CP-";
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

  @Override
  public String toJSON() {
    // TODO contact person tojson
    return null;
  }

}
