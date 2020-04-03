package app.chinabank.controller;

import app.chinabank.model.Database;
import app.chinabank.model.Record;
import app.chinabank.model.SearchResult;
import app.chinabank.model.exceptions.RecordSearchException;
import app.chinabank.model.exceptions.SchemaCreationException;
import app.chinabank.model.schemas.Customer;
import app.chinabank.model.tables.CustomerTable;
import app.chinabank.utils.Logger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import org.json.simple.JSONObject;

public final class CustomerController {

  /**
   * Creates customer information, then stored in the Database.
   *
   * @param info - customer information
   * @return saved customer's ID
   * @throws Exception when an error occured while saving customer information.
   */
  public static String saveCustomer(JSONObject info) throws Exception {
    try {
      Customer customer = new Customer(info);

      CustomerTable.getInstance().create(customer);
      Database.getInstance().commit();

      return customer.id;
    } catch (SchemaCreationException e) {
      Database.getInstance().rollback();

      throw e;
    }
  }

  /**
   * Searches customer/s with the matching name.
   *
   * @param last - customer's last name
   * @param first - customer's first name
   * @param middle - customer's middle name
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<Customer> searchCustomerFullName(
      String last, String first, String middle
  ) throws RecordSearchException {
    StringBuilder condition = new StringBuilder();

    condition.append("LName LIKE '" + last + "*' AND ");
    condition.append("FName LIKE '" + first + "*' AND ");
    condition.append("MName LIKE '" + middle + "*'");

    LinkedList<Record> records = CustomerTable.getInstance().read(condition.toString());

    return createResult(records);
  }

  /**
   * Gets customer with the matching customer ID.
   *
   * @param id - customer ID
   * @return customer with the matching ID
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static Customer searchByID(String id) throws RecordSearchException {
    String condition = "CustomerID = '" + id + "'";

    LinkedList<Record> records = CustomerTable.getInstance().read(condition);

    return createResult(records).get(0);
  }

  /**
   * Searches customer/s with the matching name.
   *
   * @param part - customer name part (last, middle, first)
   * @param name - customer name for the specified part
   * @return search result
   * @throws RecordSearchException when an error occured while searching for records.
   */
  public static SearchResult<Customer> searchByName(String part, String name)
      throws RecordSearchException {
    String condition;

    switch (part) {
      case "last":
        condition = "LName LIKE '" + name + "*'";
        part = "LName";
        break;

      case "first":
        condition = "FName LIKE '" + name + "*'";
        part = "FName";
        break;

      default:
        condition = "MName LIKE '" + name + "*'";
        part = "MName";
    }

    LinkedList<Record> records = CustomerTable.getInstance().read(condition);

    return createResult(records);
  }

  private static SearchResult<Customer> createResult(LinkedList<Record> records)
      throws RecordSearchException {
    if (records == null || records.size() == 0) {
      throw new RecordSearchException();
    }

    try {
      SearchResult<Customer> customers = new SearchResult<>();

      for (Record record : records) {
        customers.add(new Customer(record));
      }

      return customers;
    } catch (SchemaCreationException e) {
      Logger.error(e);

      throw new RecordSearchException("Error reading customer records");
    }
  }

  /**
   * Updates the customer with the matching customer ID.
   *
   * @param id - customer ID
   * @param info - new customer information
   * @return true if update was successful, otherwise false.
   * @throws Exception when an error occured while updating record.
   */
  public static Boolean updateCustomer(String id, JSONObject info) throws Exception {
    Logger.log("Updating customer with an ID of " + id);

    String             sql  = "UPDATE CUSTOMER SET ";
    LinkedList<String> cols = new LinkedList<>();

    CustomerTable.getInstance().getMetadata().forEach((k, v) -> {
      if (k != "CustomerID" && k != "DateCreated" && k != "BranchID") {
        cols.add(k + "= ?");
      }
    });

    sql += String.join(",", cols);

    sql += " WHERE CustomerID = '" + id + "'";

    try (PreparedStatement stm = Database.getInstance().getConnection().prepareStatement(sql)) {
      Customer customer = new Customer(info);

      stm.setString(1, customer.name.first);
      stm.setString(2, customer.name.middle);
      stm.setString(3, customer.name.last);
      stm.setString(4, customer.gender);
      stm.setTimestamp(5, customer.birthDate);
      stm.setString(6, customer.birthPlace);
      stm.setString(7, customer.birthCountry);
      stm.setString(8, customer.nationality);
      stm.setString(9, customer.civilStatus);
      stm.setString(10, customer.motherName.last);
      stm.setString(11, customer.motherName.first);
      stm.setString(12, customer.motherName.middle);

      stm.setString(13, customer.permanentAddress.unit);
      stm.setString(14, customer.permanentAddress.street);
      stm.setString(15, customer.permanentAddress.village);
      stm.setString(16, customer.permanentAddress.city);
      stm.setString(17, customer.permanentAddress.province);
      stm.setString(18, customer.permanentAddress.zip);

      stm.setString(19, customer.presentAddress.unit);
      stm.setString(20, customer.presentAddress.street);
      stm.setString(21, customer.presentAddress.village);
      stm.setString(22, customer.presentAddress.city);
      stm.setString(23, customer.presentAddress.province);
      stm.setString(24, customer.presentAddress.zip);

      stm.setString(25, customer.contact.get("mobile"));
      stm.setString(26, customer.contact.get("phone"));
      stm.setString(27, customer.contact.get("email"));

      stm.setString(28, customer.employment.get("gmi"));
      stm.setString(29, customer.employment.get("type"));
      stm.setString(30, customer.employment.get("industry"));
      stm.setString(31, customer.employment.get("name"));
      stm.setString(32, customer.employment.get("position"));
      stm.setString(33, customer.employment.get("contact"));

      stm.setString(34, customer.workAddress.unit);
      stm.setString(35, customer.workAddress.street);
      stm.setString(36, customer.workAddress.village);
      stm.setString(37, customer.workAddress.city);
      stm.setString(38, customer.workAddress.province);
      stm.setString(39, customer.workAddress.zip);

      stm.setString(40, customer.residency);
      stm.setString(41, customer.mailingAddress);
      return stm.executeUpdate() == 1;
    } catch (SQLException e) {
      Logger.error(e);

      throw new Exception("Can't update customer record");
    }
  }

}
