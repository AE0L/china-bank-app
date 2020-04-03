package app.chinabank.handler;

import app.chinabank.controller.TransactionController;
import app.chinabank.utils.TimeUtil;
import java.sql.Timestamp;
import org.cef.callback.CefQueryCallback;
import org.json.simple.JSONObject;

final class TransactionHandler {

  static boolean handle(String type, JSONObject data, CefQueryCallback callback) throws Exception {

    switch (type) {
      case "save": {
        callback.success(TransactionController.saveTransaction(data));

        return true;
      }

      case "search-transaction-id": {
        String id = (String) data.get("id");

        callback.success(TransactionController.getTransaction(id).toJSON());

        return true;
      }

      case "search-account-no": {
        String no = (String) data.get("no");

        callback.success(TransactionController.searchByAccountNo(no).toJSON());

        return true;
      }

      case "search-customer-name": {
        String last   = (String) data.get("last");
        String first  = (String) data.get("first");
        String middle = (String) data.get("middle");

        callback.success(TransactionController.searchByCustomerName(last, first, middle).toJSON());

        return true;
      }

      case "search-transaction-date": {
        Timestamp date = TimeUtil.toTimestamp((String) data.get("date"));

        callback.success(TransactionController.searchByDate(date).toJSON());

        return true;
      }

      case "preview": {
        String id = (String) data.get("id");

        callback.success(TransactionController.getPreview(id).toJSONString());

        return true;
      }

      case "list-all": {
        callback.success(TransactionController.getAll().toJSON());

        return true;
      }

      default: {
        throw new Exception("Invalid transacation method");
      }
    }
  }

}
