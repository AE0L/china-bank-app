package app.chinabank.model;

import org.json.simple.JSONObject;

public final class Address {

  public String unit;
  public String street;
  public String village;
  public String city;
  public String province;
  public String zip;

  /**
   * Constructor.
   *
   * @param info - address information
   */
  public Address(JSONObject info) {
    this.unit     = (String) info.get("no");
    this.street   = (String) info.get("street");
    this.village  = (String) info.get("barangay");
    this.city     = (String) info.get("city");
    this.province = (String) info.get("province");
    this.zip      = (String) info.get("zip");
  }

  public Address() {}

  /**
   * Converts address to JSON.
   *
   * @return JSON object
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJsonObject() {
    JSONObject json = new JSONObject();

    json.put("unit", this.unit);
    json.put("street", this.street);
    json.put("village", this.village);
    json.put("city", this.city);
    json.put("province", this.province);
    json.put("zip", this.zip);

    return json;
  }

}
