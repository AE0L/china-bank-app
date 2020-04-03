package app.chinabank.handler;

import app.chinabank.controller.AccountController;
import org.cef.callback.CefQueryCallback;
import org.json.simple.JSONObject;

final class AccountHandler {

  static boolean handle(String type, JSONObject data, CefQueryCallback callback) throws Exception {
    switch (type) {
      case "save": {
        callback.success(AccountController.saveAccount(data));

        return true;
      }

      case "search-account-no": {
        String no = (String) data.get("no");

        callback.success(AccountController.getAccountByNo(no).toJSON());

        return true;
      }

      case "search-account-holder": {
        String last   = (String) data.get("last");
        String first  = (String) data.get("first");
        String middle = (String) data.get("middle");

        callback.success(AccountController.searchByAccountHolder(last, first, middle).toJSON());

        return true;
      }

      case "list-all": {
        callback.success(AccountController.getAll().toJSON());

        return true;
      }

      case "preview": {
        String no = (String) data.get("no");

        callback.success(AccountController.previewAccount(no).toJSONString());

        return true;
      }

      default: {
        throw new Exception("Invalid account method type");
      }
    }
  }

}
