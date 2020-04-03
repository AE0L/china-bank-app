package app.chinabank.model.tables;

import app.chinabank.model.Database;
import app.chinabank.model.Record;
import app.chinabank.model.statements.InsertStatement;
import app.chinabank.model.statements.JoinStatement;
import app.chinabank.model.statements.SelectAllStatement;
import app.chinabank.utils.Logger;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

class Table {

  private final String              name;
  final LinkedHashMap<String, Type> metadata;

  Table(String name) {
    this.name     = name;
    this.metadata = new LinkedHashMap<>();
  }

  public String getName() {
    return this.name;
  }

  public LinkedHashMap<String, Type> getMetadata() {
    return this.metadata;
  }

  Boolean create(Record record) {
    try (InsertStatement stm = new InsertStatement(this.getName(), record, this.getMetadata());) {
      stm.execute();

      return true;
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public LinkedList<Record> read(String condition) {
    try (SelectAllStatement stm = new SelectAllStatement(this.getName(), condition);) {
      return this.toRecords(stm.execute(), this.columns().toArray(new String[0]));
    } catch (IOException | SQLException e) {
      Logger.error(e);

      return null;
    }
  }

  public LinkedList<Record> read() {
    try (PreparedStatement stm = Database.getInstance().getConnection()
        .prepareStatement("SELECT * FROM " + this.getName())) {
      return this.toRecords(stm.executeQuery(), this.columns().toArray(new String[0]));
    } catch (SQLException e) {
      Logger.error(e);

      return null;
    }
  }

  public LinkedList<Record> readStatement(PreparedStatement stm) {
    try {
      return this.toRecords(stm.executeQuery(), this.columns().toArray(new String[0]));
    } catch (SQLException e) {
      Logger.error(e);

      return null;
    }
  }

  public LinkedList<Record> join(String table, String[] columns, String join, String where) {
    try (JoinStatement stm = new JoinStatement(this.getName(), table, columns, join, where)) {
      return this.toRecords(stm.execute(), columns);
    } catch (IOException | SQLException e) {
      Logger.error(e);

      return null;
    }
  }

  public LinkedList<Record> toRecords(ResultSet res, String[] columns) throws SQLException {
    LinkedList<Record> records = new LinkedList<>();

    while (res.next()) {
      Record record = new Record();

      for (String col : columns) {
        record.put(col, res.getObject(col));
      }

      records.add(record);
    }

    return records;
  }

  public LinkedList<String> columns() {
    LinkedList<String> cols = new LinkedList<>();

    this.metadata.forEach((k, v) -> cols.add(k));

    return cols;
  }

  public int count() {
    try (ResultSet res = Database.getInstance()
        .query("SELECT COUNT(*) AS ROWCOUNT FROM " + this.name + ";")) {

      res.next();

      return res.getInt("ROWCOUNT");
    } catch (SQLException e) {
      e.printStackTrace();

      return -1;
    }
  }

}
