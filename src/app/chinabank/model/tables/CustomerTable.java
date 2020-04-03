package app.chinabank.model.tables;

import app.chinabank.model.Record;
import app.chinabank.model.schemas.Customer;

public class CustomerTable extends Table {

  private static CustomerTable INSTANCE = null;

  private CustomerTable() {
    super("CUSTOMER");

    this.metadata.put("CustomerID", Type.STRING);

    this.metadata.put("DateCreated", Type.DATE);
    this.metadata.put("BranchID", Type.STRING);

    this.metadata.put("FName", Type.STRING);
    this.metadata.put("MName", Type.STRING);
    this.metadata.put("LName", Type.STRING);

    this.metadata.put("Gender", Type.STRING);

    this.metadata.put("BirthDate", Type.DATE);
    this.metadata.put("BirthPlace", Type.STRING);
    this.metadata.put("BirthCountry", Type.STRING);

    this.metadata.put("Nationality", Type.STRING);
    this.metadata.put("CivilStatus", Type.STRING);

    this.metadata.put("MotherFName", Type.STRING);
    this.metadata.put("MotherMName", Type.STRING);
    this.metadata.put("MotherLName", Type.STRING);

    this.metadata.put("PerAddUnit", Type.STRING);
    this.metadata.put("PerAddStreet", Type.STRING);
    this.metadata.put("PerAddVillage", Type.STRING);
    this.metadata.put("PerAddCity", Type.STRING);
    this.metadata.put("PerAddProvince", Type.STRING);
    this.metadata.put("PerAddZip", Type.STRING);

    this.metadata.put("PreAddUnit", Type.STRING);
    this.metadata.put("PreAddStreet", Type.STRING);
    this.metadata.put("PreAddVillage", Type.STRING);
    this.metadata.put("PreAddCity", Type.STRING);
    this.metadata.put("PreAddProvince", Type.STRING);
    this.metadata.put("PreAddZip", Type.STRING);

    this.metadata.put("MobileNo", Type.STRING);
    this.metadata.put("PhoneNo", Type.STRING);
    this.metadata.put("Email", Type.STRING);

    this.metadata.put("GMI", Type.STRING);
    this.metadata.put("EmpType", Type.STRING);
    this.metadata.put("EmpIndustry", Type.STRING);
    this.metadata.put("EmpName", Type.STRING);
    this.metadata.put("EmpPosition", Type.STRING);
    this.metadata.put("EmpContact", Type.STRING);

    this.metadata.put("EmpAddUnit", Type.STRING);
    this.metadata.put("EmpAddStreet", Type.STRING);
    this.metadata.put("EmpAddVillage", Type.STRING);
    this.metadata.put("EmpAddCity", Type.STRING);
    this.metadata.put("EmpAddProvince", Type.STRING);
    this.metadata.put("EmpAddZip", Type.STRING);

    this.metadata.put("Residency", Type.STRING);
    this.metadata.put("MailingAdd", Type.STRING);

  }

  public boolean create(Customer customer) {
    Record record = new Record();

    record.put("CustomerID", customer.id);

    record.put("DateCreated", customer.dateCreated);
    record.put("BranchID", customer.branchCreated);

    record.put("FName", customer.name.first);
    record.put("MName", customer.name.middle);
    record.put("LName", customer.name.last);

    record.put("Gender", customer.gender);

    record.put("BirthDate", customer.birthDate);
    record.put("BirthPlace", customer.birthPlace);
    record.put("BirthCountry", customer.birthCountry);

    record.put("Nationality", customer.nationality);
    record.put("CivilStatus", customer.civilStatus);

    record.put("MotherFName", customer.motherName.first);
    record.put("MotherMName", customer.motherName.middle);
    record.put("MotherLName", customer.motherName.last);

    record.put("PerAddUnit", customer.permanentAddress.unit);
    record.put("PerAddStreet", customer.permanentAddress.street);
    record.put("PerAddVillage", customer.permanentAddress.village);
    record.put("PerAddCity", customer.permanentAddress.city);
    record.put("PerAddProvince", customer.permanentAddress.province);
    record.put("PerAddZip", customer.permanentAddress.zip);

    record.put("PreAddUnit", customer.presentAddress.unit);
    record.put("PreAddStreet", customer.presentAddress.street);
    record.put("PreAddVillage", customer.presentAddress.village);
    record.put("PreAddCity", customer.presentAddress.city);
    record.put("PreAddProvince", customer.presentAddress.province);
    record.put("PreAddZip", customer.presentAddress.zip);

    record.put("MobileNo", customer.contact.get("mobile"));
    record.put("PhoneNo", customer.contact.get("phone"));
    record.put("Email", customer.contact.get("email"));

    record.put("GMI", customer.employment.get("gmi"));
    record.put("EmpType", customer.employment.get("type"));
    record.put("EmpIndustry", customer.employment.get("industry"));
    record.put("EmpName", customer.employment.get("name"));
    record.put("EmpPosition", customer.employment.get("position"));
    record.put("EmpContact", customer.employment.get("contact"));

    record.put("EmpAddUnit", customer.workAddress.unit);
    record.put("EmpAddStreet", customer.workAddress.street);
    record.put("EmpAddVillage", customer.workAddress.village);
    record.put("EmpAddCity", customer.workAddress.city);
    record.put("EmpAddProvince", customer.workAddress.province);
    record.put("EmpAddZip", customer.workAddress.zip);

    record.put("Residency", customer.residency);
    record.put("MailingAdd", customer.mailingAddress);

    return this.create(record);
  }

  public static CustomerTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CustomerTable();
    }

    return INSTANCE;
  }

}
