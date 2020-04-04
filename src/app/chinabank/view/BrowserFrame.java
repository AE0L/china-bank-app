package app.chinabank.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.cef.browser.CefBrowser;

/**
 * Swing JFrame wrapper for the CEF browser.
 */
public class BrowserFrame extends JFrame {

  private CefBrowser browser = null;

  /**
   * Constructor.
   */
  public BrowserFrame() {
    super("China Bank Application");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void close() {
    this.dispose();
    this.browser.doClose();
  }

  /**
   * Adds the browser to the JFrame.
   *
   * @param browser - browser to be used.
   */
  public void setBrowser(CefBrowser browser) {
    if (browser == null) {
      this.browser = browser;
    }

    getContentPane().add(browser.getUIComponent(), BorderLayout.CENTER);
  }

}
