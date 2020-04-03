package app.chinabank.model.schemas;

import app.chinabank.controller.AccountController;
import app.chinabank.controller.ContactPersonController;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

public class BusinessAccount extends Account {

  public String        businessName;
  public ContactPerson contact;

  /**
   * Constructor.
   *
   * @param info - business account information
   * @throws SchemaCreationException when an error occured while creating business account
   */
  public BusinessAccount(JSONObject info) throws SchemaCreationException {
    super(info);

    this.businessName = (String) info.get("business_name");
    this.contact      = new ContactPerson((JSONObject) info.get("contact_person"));
  }

  /**
   * Constructor.
   *
   * @param record - Database Record
   * @throws RecordSearchException - when an error occured while searching for the associated
   *         account
   * @throws SchemaCreationException - when an error occured while creating business account
   */
  public BusinessAccount(LinkedHashMap<String, Object> record)
      throws RecordSearchException, SchemaCreationException {
    super(AccountController.getAccount((String) record.get("AccountNo")));

    try {
      this.contact = ContactPersonController
          .getContactPerson((String) record.get("ContactPersonID"));
    } catch (RecordSearchException e) {
      throw new SchemaCreationException("can't create business account from table");
    }

    this.businessName = (String) record.get("BusinessName");
  }

  @SuppressWarnings("unchecked")
  @Override
  public String toJSON() {
    JSONObject json = super.toJSONObject();

    json.put("business_name", this.businessName);

    return json.toJSONString();
  }

}
