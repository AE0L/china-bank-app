package app.chinabank.model.statements;

import app.chinabank.model.Database;
import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectAllStatement implements Closeable {

  private PreparedStatement statement;

  /**
   * Prepares a SELECT statement with the following template.
   *
   * <pre>
   * SELECT * FROM [TABLE] WHERE [CONDITION]
   * </pre>
   *
   * @param table - TABLE name
   * @param condition - WHERE condition
   * @throws SQLException when an error occures while accessing the Database
   */
  public SelectAllStatement(String table, String condition) throws SQLException {
    StringBuilder sql = new StringBuilder("SELECT * FROM " + table);

    if (condition != null) {
      sql.append(" WHERE " + condition);
    }

    sql.append(";");

    this.statement = Database.getInstance().getConnection().prepareStatement(sql.toString());
  }

  public ResultSet execute() throws SQLException {
    return this.statement.executeQuery();
  }

  @Override
  public void close() throws IOException {
    try {
      this.statement.close();
    } catch (SQLException e) {
      throw new IOException(e.getMessage());
    }
  }

}
