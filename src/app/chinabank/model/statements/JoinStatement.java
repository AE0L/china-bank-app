package app.chinabank.model.statements;

import app.chinabank.model.Database;
import app.chinabank.utils.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class JoinStatement implements Closeable {

  private PreparedStatement stm;

  /**
   * Prepares an INNER JOIN statement with the following template.
   *
   * <pre>
   * SELECT [COLUMNS] FROM [TABLE_1]
   * INNER JOIN [TABLE_2]
   *    ON [TABLE_1.ATTRIBUTE] = [TABLE_2.ATTRIBUTE]
   * WHERE [CONDITON];
   * </pre>
   *
   * <p>
   * The WHERE condition can be null to get all records.
   * </p>
   *
   * @param table1 - TABLE_1 name
   * @param table2 - TABLE_2 name
   * @param cols - columns to be selected
   * @param join - attribute that exists in both tables
   * @param where - WHERE condition
   * @throws SQLException when an error occured while accessing the Datbase
   */
  public JoinStatement(String table1, String table2, String[] cols, String join, String where)
      throws SQLException {
    String sql = this.baseStatement(table1, table2, cols, join, where).toString();

    this.stm = Database.getInstance().getConnection().prepareStatement(sql);
  }

  private StringBuilder baseStatement(
      String table1, String table2, String[] cols, String join, String where
  ) {
    StringBuilder      sb   = new StringBuilder("SELECT ");
    LinkedList<String> temp = new LinkedList<>();

    for (String col : cols) {
      if (join.equals(col)) {
        temp.add(table1 + "." + col + " AS " + col);
      } else {
        temp.add(col);
      }
    }

    sb.append(String.join(",", temp));
    sb.append(" FROM " + table1);
    sb.append(" INNER JOIN " + table2);
    sb.append(" ON " + table1 + "." + join + " = " + table2 + "." + join);

    if (where != null) {
      sb.append(" WHERE " + where);
    }

    sb.append(";");

    return sb;
  }

  public ResultSet execute() throws SQLException {
    return this.stm.executeQuery();
  }

  @Override
  public void close() throws IOException {
    try {
      this.stm.close();
    } catch (SQLException e) {
      Logger.error(e);

      throw new IOException("can't close join statement");
    }
  }

}
