package app.chinabank.model;

import org.json.simple.JSONObject;

public class Name {

  public String first;
  public String middle;
  public String last;

  /**
   * Constructor.
   *
   * @param information - name information
   */
  public Name(JSONObject information) {
    this.first  = (String) information.get("first");
    this.middle = (String) information.get("middle");
    this.last   = (String) information.get("last");
  }

  /**
   * Constructor.
   *
   * @param last - last name
   * @param first - first name
   * @param middle - middle name
   */
  public Name(String last, String first, String middle) {
    this.last   = last;
    this.first  = first;
    this.middle = middle;
  }

  public Name() {}

  /**
   * Converts name to JSON.
   *
   * @return JSON object
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJsonObject() {
    JSONObject json = new JSONObject();

    json.put("last", this.last);
    json.put("first", this.first);
    json.put("middle", this.middle);

    return json;
  }

  @Override
  public String toString() {
    return last + ", " + first + " " + middle;
  }

}
