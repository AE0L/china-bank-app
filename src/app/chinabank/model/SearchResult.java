package app.chinabank.model;

import app.chinabank.model.schemas.Schema;
import java.util.LinkedList;

public class SearchResult<T extends Schema> {

  public final LinkedList<T> result;

  public SearchResult() {
    this.result = new LinkedList<>();
  }

  public T get(int index) {
    return this.result.get(index);
  }

  public void add(T value) {
    this.result.add(value);
  }

  /**
   * Converts result list to JSON string.
   *
   * @return JSON string
   */
  public String toJSON() {
    StringBuilder      sb      = new StringBuilder("[");
    LinkedList<String> records = new LinkedList<>();

    for (T record : this.result) {
      records.add(record.toJSON());
    }

    String array = String.join(",", records.toArray(new String[0]));
    sb.append(array + "]");

    return sb.toString();
  }

}
