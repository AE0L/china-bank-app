package app.chinabank.handler;

import app.chinabank.utils.Logger;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class ControllerMessageHandler extends CefMessageRouterHandlerAdapter {

  private static final JSONParser             parser  = new JSONParser();
  private static final CefMessageRouterConfig config_ = new CefMessageRouterConfig(
      "_controller", "controllerAbort"
  );

  /**
   * Hooks handler to the given client.
   *
   * @param client - CEF client
   */
  public static void addHandler(CefClient client) {
    ControllerMessageHandler handler = new ControllerMessageHandler();
    CefMessageRouter         router  = CefMessageRouter.create(config_, handler);

    client.addMessageRouter(router);
  }

  @Override
  public boolean onQuery(
      CefBrowser browser, CefFrame frame, long id, String json, boolean persistent,
      CefQueryCallback callback
  ) {
    try {
      JSONObject request    = (JSONObject) parser.parse(json);
      String     controller = (String) request.get("controller");
      JSONObject method     = (JSONObject) request.get("method");
      String     type       = (String) method.get("type");
      JSONObject data       = (JSONObject) method.get("data");

      Logger.log("Controller: " + controller);
      Logger.log("Method:     " + type);

      switch (controller) {
        case "customer": {
          return CustomerHandler.handle(type, data, callback);
        }

        case "credit-card": {
          return CreditCardHandler.handle(type, data, callback);
        }

        case "account": {
          return AccountHandler.handle(type, data, callback);
        }

        case "transaction": {
          return TransactionHandler.handle(type, data, callback);
        }

        default: {
          callback.failure(-1, "invalid controller was requested");

          return false;
        }
      }
    } catch (ParseException e) {
      Logger.error(e);

      callback.failure(-1, "cannot parse request");

      return false;
    } catch (Exception e) {
      Logger.error(e);

      callback.failure(-1, e.getMessage());

      return true;
    }
  }

}
