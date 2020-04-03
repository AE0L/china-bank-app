package app.chinabank.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
        browser.close(true);
      }
    });
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
