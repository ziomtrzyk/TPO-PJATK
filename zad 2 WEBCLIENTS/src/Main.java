/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class Main {
  public static void main(String[] args) {
    Service s = new Service("Germany");
    String weatherJson = s.getWeather("berlin");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    System.out.println(rate2);
  }
}
