package app.chinabank.handler;

import app.chinabank.controller.CreditCardController;
import org.cef.callback.CefQueryCallback;
import org.json.simple.JSONObject;

final class CreditCardHandler {

  static boolean handle(String type, JSONObject data, CefQueryCallback callback) throws Exception {
    switch (type) {
      case "save": {
        callback.success(CreditCardController.saveCreditCard(data));

        return true;
      }

      case "search-holder": {
        String last   = (String) data.get("last");
        String first  = (String) data.get("first");
        String middle = (String) data.get("middle");

        callback.success(CreditCardController.searchByCardHolder(last, first, middle).toJSON());

        return true;
      }

      case "search-name": {
        String name = (String) data.get("name");

        callback.success(CreditCardController.searchByCardName(name).toJSON());

        return true;
      }

      case "search-no": {
        String no = (String) data.get("no");

        callback.success(CreditCardController.searchByCardNo(no).toJSON());

        return true;
      }

      case "preview": {
        String no = (String) data.get("no");

        callback.success(CreditCardController.previewCard(no).toJSONString());

        return true;
      }

      case "list-all": {
        callback.success(CreditCardController.getAll().toJSON());

        return true;
      }

      default: {
        throw new Exception("invalid credit card method type");
      }
    }
  }

}
