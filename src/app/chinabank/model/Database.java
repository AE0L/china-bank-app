package app.chinabank.model;

import app.chinabank.utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

  private static Database INSTANCE;
  private Connection      con;
  private Statement       stm;

  private Database() throws ClassNotFoundException, SQLException {
    Logger.log("Creating database instance");

    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

    this.con = DriverManager.getConnection("jdbc:ucanaccess://assets/china-bank.accdb");
    this.stm = this.con.createStatement();

    this.con.setAutoCommit(false);
  }

  public static Database getInstance() {
    return INSTANCE;
  }

  public Connection getConnection() {
    return this.con;
  }

  public ResultSet query(String sql) throws SQLException {
    return this.stm.executeQuery(sql);
  }

  public static void prepareDatabase() throws ClassNotFoundException, SQLException {
    INSTANCE = new Database();
  }

  /**
   * Closes the connection and statement of the current Database Instance.
   */
  public void close() {
    Logger.log("Closing database instance");

    try {
      if (this.con != null) {
        this.con.close();
      }

      if (this.stm != null) {
        this.stm.close();
      }
    } catch (SQLException e) {
      Logger.error(e);
    }
  }

  public void rollback() throws SQLException {
    this.con.rollback();
  }

  public void commit() throws SQLException {
    this.con.commit();
  }

}
