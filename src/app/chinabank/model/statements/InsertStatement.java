package app.chinabank.model.statements;

import app.chinabank.model.Database;
import app.chinabank.model.tables.Type;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class InsertStatement implements Closeable {

  private String                        table;
  private LinkedHashMap<String, Object> entries;
  private LinkedHashMap<String, Type>   metadata;
  private LinkedList<String>            columns;
  private String                        template;
  private PreparedStatement             statement;

  /**
   * Prepares an insert statement with the following template.
   *
   * <pre>
   * INSERT INTO [TABLE] VALUES (...);
   * </pre>
   *
   * @param table - TABLE name
   * @param entries - values to be inserted
   * @param metadata - TABLE metadata
   * @throws SQLException when an error occured while accessing the Database.
   */
  public InsertStatement(
      String table, LinkedHashMap<String, Object> entries, LinkedHashMap<String, Type> metadata
  ) throws SQLException {
    this.table     = table;
    this.entries   = entries;
    this.metadata  = metadata;
    this.template  = this.createTemplate();
    this.statement = Database.getInstance().getConnection().prepareStatement(this.template);
    this.setValues();
  }

  private String createTemplate() {
    final StringBuilder stm   = new StringBuilder();
    String[]            marks = new String[this.entries.size()];

    this.columns = new LinkedList<>();

    Arrays.fill(marks, "?");
    this.entries.forEach((k, v) -> this.columns.add(k));

    stm.append("INSERT INTO");
    stm.append(" " + this.table + " ");
    stm.append("(" + String.join(",", this.columns) + ")");
    stm.append(" VALUES ");
    stm.append("(" + String.join(",", marks) + ");");

    return stm.toString();
  }

  private void setValues() throws SQLException {
    int i = 0;

    for (String column : this.columns) {
      i += 1;

      Object value = this.entries.get(column);
      Type   type  = this.metadata.get(column);

      switch (type) {
        case STRING:
          this.statement.setString(i, (String) value);
          break;

        case DOUBLE:
          this.statement.setDouble(i, (Double) value);
          break;

        case DATE:
          if (value instanceof Timestamp) {
            this.statement.setTimestamp(i, (Timestamp) value);
          } else if (value instanceof String) {
            this.statement.setDate(i, Date.valueOf(((String) value)));
          }
          break;

        case INTEGER:
          this.statement.setInt(i, (Integer) value);
          break;

        default:
          throw new SQLException("Invalid attribute type " + type);
      }
    }
  }

  public Boolean execute() throws SQLException {
    return this.statement.execute();
  }

  @Override
  public void close() throws IOException {
    try {
      this.statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IOException(e.getMessage());
    }
  }

}
