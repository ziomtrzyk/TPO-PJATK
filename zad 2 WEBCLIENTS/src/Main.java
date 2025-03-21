import javax.swing.*;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("berlin");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(() -> new MyFrame().setVisible(true));
  }
}
