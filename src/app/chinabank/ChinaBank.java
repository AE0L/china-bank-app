package app.chinabank;

import app.chinabank.handler.ControllerMessageHandler;
import app.chinabank.handler.RedirectMessageHandler;
import app.chinabank.model.Database;
import app.chinabank.utils.Logger;
import app.chinabank.view.BrowserFrame;
import java.io.File;
import java.sql.SQLException;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

public class ChinaBank extends BrowserFrame {

  public static final String BRANCH_ID = "PSG-PNG";

  private final CefClient   client;
  private static CefBrowser browser = null;

  /**
   * Main method.
   *
   * @param args - application arguments.
   * @throws ClassNotFoundException when an error occured while registering UCanAccess driver.
   * @throws SQLException when an error occured while preparing the Database instance.
   */
  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Logger.log("Starting Application");

    String[] cefArgs = {"--disable-gpu"};

    final ChinaBank gui = new ChinaBank(cefArgs);

    gui.setVisible(true);
    gui.setExtendedState(MAXIMIZED_BOTH);
  }

  private ChinaBank(String[] args) throws ClassNotFoundException, SQLException {
    Logger.log("Preparing CEF browser");

    CefApp app;

    if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
      CefSettings settings = new CefSettings();

      settings.windowless_rendering_enabled = false;
      settings.log_severity                 = CefSettings.LogSeverity.LOGSEVERITY_ERROR;
      settings.remote_debugging_port        = 8080;

      app = CefApp.getInstance(args, settings);
    } else {
      app = CefApp.getInstance();
    }

    client = app.createClient();

    Logger.log("Adding message handlers");

    RedirectMessageHandler.addHandler(client);
    ControllerMessageHandler.addHandler(client);

    String index = new File("assets/index.html").getAbsolutePath();

    Logger.log("Creating chromium browser");

    browser = client.createBrowser(index, false, false, null);

    setBrowser(browser);
    Database.prepareDatabase();

    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

      @Override
      public void run() {
        Database.getInstance().close();
      }
    }));
  }

}
