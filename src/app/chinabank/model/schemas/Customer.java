package app.chinabank.model.schemas;

import app.chinabank.ChinaBank;
import app.chinabank.model.Address;
import app.chinabank.model.Name;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.tables.CustomerTable;
import app.chinabank.utils.TimeUtil;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONObject;

public class Customer implements Schema {

  public String                  id;
  public Timestamp               dateCreated;
  public String                  branchCreated;
  public String                  gender;
  public String                  nationality;
  public String                  civilStatus;
  public String                  residency;
  public String                  mailingAddress;
  public Name                    name;
  public Name                    motherName;
  public Address                 permanentAddress;
  public Address                 presentAddress;
  public Address                 workAddress;
  public Timestamp               birthDate;
  public String                  birthPlace;
  public String                  birthCountry;
  public HashMap<String, String> contact;
  public HashMap<String, String> employment;

  /**
   * Constructor.
   *
   * @param info - customer information
   * @throws SchemaCreationException when an error occured while creating customer.
   */
  @SuppressWarnings("unchecked")
  public Customer(JSONObject info) throws SchemaCreationException {
    this.id               = this.generateID();
    this.dateCreated      = TimeUtil.currentTimestamp();
    this.branchCreated    = ChinaBank.BRANCH_ID;
    this.gender           = (String) info.get("gender");
    this.nationality      = (String) info.get("nation");
    this.civilStatus      = (String) info.get("civil");
    this.residency        = (String) info.get("residency");
    this.mailingAddress   = (String) info.get("mailing");
    this.name             = new Name((JSONObject) info.get("name"));
    this.motherName       = new Name((JSONObject) info.get("mother"));
    this.permanentAddress = new Address((JSONObject) info.get("per_add"));
    this.presentAddress   = new Address((JSONObject) info.get("pre_add"));
    this.workAddress      = new Address((JSONObject) info.get("emp_add"));

    JSONObject birth = (JSONObject) info.get("birth");

    this.birthDate    = TimeUtil.toTimestamp((String) birth.get("date"));
    this.birthPlace   = (String) birth.get("place");
    this.birthCountry = (String) birth.get("country");

    this.contact    = (HashMap<String, String>) info.get("contact");
    this.employment = (HashMap<String, String>) info.get("employment");
  }

  /**
   * Constructor.
   *
   * @param info - Database record
   * @throws SchemaCreationException when an error occured while creating customer
   */
  public Customer(LinkedHashMap<String, Object> info) throws SchemaCreationException {
    this.name                      = new Name();
    this.motherName                = new Name();
    this.presentAddress            = new Address();
    this.permanentAddress          = new Address();
    this.workAddress               = new Address();
    this.contact                   = new HashMap<>();
    this.employment                = new HashMap<>();
    this.id                        = (String) info.get("CustomerID");
    this.dateCreated               = (Timestamp) info.get("DateCreated");
    this.branchCreated             = (String) info.get("BranchID");
    this.name.last                 = (String) info.get("LName");
    this.name.first                = (String) info.get("FName");
    this.name.middle               = (String) info.get("MName");
    this.motherName.last           = (String) info.get("MotherFName");
    this.motherName.first          = (String) info.get("MotherMName");
    this.motherName.middle         = (String) info.get("MotherLName");
    this.gender                    = (String) info.get("Gender");
    this.nationality               = (String) info.get("Nationality");
    this.civilStatus               = (String) info.get("CivilStatus");
    this.residency                 = (String) info.get("Residency");
    this.mailingAddress            = (String) info.get("MailingAdd");
    this.birthDate                 = (Timestamp) info.get("BirthDate");
    this.birthPlace                = (String) info.get("BirthPlace");
    this.birthCountry              = (String) info.get("BirthCountry");
    this.permanentAddress.unit     = (String) info.get("PerAddUnit");
    this.permanentAddress.street   = (String) info.get("PerAddStreet");
    this.permanentAddress.village  = (String) info.get("PerAddVillage");
    this.permanentAddress.city     = (String) info.get("PerAddCity");
    this.permanentAddress.province = (String) info.get("PerAddProvince");
    this.permanentAddress.zip      = (String) info.get("PerAddZip");
    this.presentAddress.unit       = (String) info.get("PreAddUnit");
    this.presentAddress.street     = (String) info.get("PreAddStreet");
    this.presentAddress.village    = (String) info.get("PreAddVillage");
    this.presentAddress.city       = (String) info.get("PreAddCity");
    this.presentAddress.province   = (String) info.get("PreAddProvince");
    this.presentAddress.zip        = (String) info.get("PreAddZip");
    this.workAddress.unit          = (String) info.get("EmpAddUnit");
    this.workAddress.street        = (String) info.get("EmpAddStreet");
    this.workAddress.village       = (String) info.get("EmpAddVillage");
    this.workAddress.city          = (String) info.get("EmpAddCity");
    this.workAddress.province      = (String) info.get("EmpAddProvince");
    this.workAddress.zip           = (String) info.get("EmpAddZip");
    this.contact.put("mobile", (String) info.get("MobileNo"));
    this.contact.put("phone", (String) info.get("PhoneNo"));
    this.contact.put("email", (String) info.get("Email"));
    this.employment.put("gmi", (String) info.get("GMI"));
    this.employment.put("type", (String) info.get("EmpType"));
    this.employment.put("industry", (String) info.get("EmpIndustry"));
    this.employment.put("name", (String) info.get("EmpName"));
    this.employment.put("position", (String) info.get("EmpPosition"));
    this.employment.put("contact", (String) info.get("EmpContact"));
  }

  private String generateID() throws SchemaCreationException {
    int count = CustomerTable.getInstance().count();

    if (count != -1) {
      int    pad         = 6;
      int    numericSize = 9;
      int    checksum    = 0;
      String id          = "C-";
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

  public String getIdentifier() {
    return this.id.substring(9, 18);
  }

  public String getChecksum() {
    return "" + this.id.charAt(19);
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSON() {
    JSONObject birth = new JSONObject();

    birth.put("date", TimeUtil.dateFormat(this.birthDate));
    birth.put("place", this.birthPlace);
    birth.put("country", this.birthCountry);

    JSONObject json = new JSONObject();

    json.put("id", this.id);
    json.put("date_created", TimeUtil.dateFormat(this.dateCreated));
    json.put("branch_created", this.branchCreated);
    json.put("name", this.name.toJsonObject());
    json.put("birth", birth);
    json.put("mother", this.motherName.toJsonObject());
    json.put("per_add", this.permanentAddress.toJsonObject());
    json.put("pre_add", this.presentAddress.toJsonObject());
    json.put("emp_add", this.workAddress.toJsonObject());
    json.put("contact", new JSONObject(this.contact));
    json.put("employment", new JSONObject(this.employment));
    json.put("gender", this.gender);
    json.put("nation", this.nationality);
    json.put("civil", this.civilStatus);
    json.put("residency", this.residency);
    json.put("mailing", this.mailingAddress);

    return json.toJSONString();
  }

  /**
   * Convert customer to a preview JSON string.
   *
   * @return JSON string
   */
  @SuppressWarnings("unchecked")
  public String toFullJSON() {
    JSONObject birth = new JSONObject();

    birth.put("date", TimeUtil.dateFormat(this.birthDate));
    birth.put("place", this.birthPlace);
    birth.put("country", this.birthCountry);

    JSONObject address = new JSONObject();

    address.put("permanent", this.permanentAddress.toJsonObject());
    address.put("present", this.presentAddress.toJsonObject());
    address.put("employment", this.workAddress.toJsonObject());

    JSONObject contact = new JSONObject();

    contact.put("mobile", this.contact.get("mobile"));
    contact.put("phone", this.contact.get("phone"));
    contact.put("email", this.contact.get("email"));
    contact.put("mailing", this.mailingAddress);

    JSONObject json = new JSONObject();

    json.put("id", this.id);
    json.put("name", this.name.toJsonObject());
    json.put("gender", this.gender);
    json.put("nationality", this.nationality);
    json.put("civilStatus", this.civilStatus);
    json.put("birth", birth);
    json.put("mother", this.motherName.toJsonObject());
    json.put("residency", this.residency);
    json.put("dateCreated", TimeUtil.timestampFormat(this.dateCreated));
    json.put("contact", contact);
    json.put("address", address);
    json.put("employment", employment);

    return json.toJSONString();
  }

}
