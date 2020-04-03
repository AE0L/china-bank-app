package app.chinabank.handler;

import app.chinabank.utils.Logger;
import java.io.File;
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


public final class RedirectMessageHandler extends CefMessageRouterHandlerAdapter {

  private static final CefMessageRouterConfig config_ = new CefMessageRouterConfig(
      "_redirect", "_redirectAbort"
  );

  /**
   * Hooks handler to the givent client.
   *
   * @param client - CEF client
   */
  public static void addHandler(final CefClient client) {
    RedirectMessageHandler handler = new RedirectMessageHandler();
    CefMessageRouter       router  = CefMessageRouter.create(config_, handler);

    client.addMessageRouter(router);
  }

  @Override
  public boolean onQuery(
      CefBrowser browser, CefFrame frame, long id, String request, boolean persistent,
      CefQueryCallback callback
  ) {

    try {
      JSONObject redirect = (JSONObject) new JSONParser().parse(request);
      String     method   = (String) redirect.get("method");
      String     page     = (String) redirect.get("page");
      String     url      = new File("assets/" + page).getAbsolutePath();

      switch (method) {
        case "redirect": {
          Logger.log("Redirecting to " + url);

          callback.success(url);

          return true;
        }

        case "params": {
          String params = (String) redirect.get("params");

          Logger.log("Redirecting to " + url + ", with parameters: " + params);

          callback.success(url + "?" + params);

          return true;
        }

        default: {
          return false;
        }
      }

    } catch (ParseException e) {
      Logger.error(e);
    }

    return false;
  }

}
