package app.chinabank.handler;

import app.chinabank.controller.CustomerController;
import org.cef.callback.CefQueryCallback;
import org.json.simple.JSONObject;

final class CustomerHandler {

  static boolean handle(String type, JSONObject data, CefQueryCallback callback) throws Exception {
    switch (type) {
      case "save": {
        callback.success(CustomerController.saveCustomer(data));

        return true;
      }

      case "search-name": {
        String part = (String) data.get("part");
        String name = (String) data.get("name");

        callback.success(CustomerController.searchByName(part, name).toJSON());

        return true;
      }

      case "search-id": {
        String id = (String) data.get("id");

        callback.success(CustomerController.searchByID(id).toJSON());

        return true;
      }

      case "preview": {
        String id = (String) data.get("id");

        callback.success(CustomerController.searchByID(id).toFullJSON());

        return true;
      }

      case "update": {
        String     id      = (String) data.get("id");
        JSONObject info    = (JSONObject) data.get("customer");
        Boolean    success = false;

        success = CustomerController.updateCustomer(id, info);

        if (success) {
          callback.success("");
        } else {
          callback.failure(-1, "an error occured while updating customer");
        }

        return true;
      }

      case "list-all": {
        callback.success(CustomerController.getAll().toJSON());

        return true;
      }

      default: {
        throw new Exception("invalid customer controller method");
      }
    }
  }

}
