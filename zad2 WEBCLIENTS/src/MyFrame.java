package zad1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MyFrame extends JFrame {
    private String country;
    private String city;
    private String currency;

    private JButton button1, button2, button3, button4;
    private JLabel label;
    public MyFrame() {

        country = JOptionPane.showInputDialog("Enter country");

        Service service = new Service(country);

        city = JOptionPane.showInputDialog("Enter city");

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label = new JLabel("Country: " + country + " City: " + city);

        button1 = new JButton("Weather in the city "+city);
        button2 = new JButton("Currency exchange rate");
        button3 = new JButton("NBP zloty exchange rate to "+ city);
        button4 = new JButton("Wiki with city description");

        button1.addActionListener(e -> label.setText("Temperature in the city " + city + " is: "+service.getInfoWeather(city)));
        button3.addActionListener(e -> label.setText("NBP zloty exchange rate to "+ country + ": " + service.getNBPRate()));
        button4.addActionListener(e -> {
            JFrame wikiFrame = new JFrame();
            wikiFrame.setSize(600, 600);
            wikiFrame.setLocationRelativeTo(null);

            JEditorPane webPane = new JEditorPane();
            webPane.setEditable(false);
            webPane.setContentType("text/html");

            JScrollPane scrollPane = new JScrollPane(webPane);
            wikiFrame.add(scrollPane, BorderLayout.CENTER);

            try {
                URL url = new URL("https://pl.wikipedia.org/wiki/" + city);
                webPane.setPage(url);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            wikiFrame.setVisible(true);
        });

        button2.addActionListener(e -> {
            currency = JOptionPane.showInputDialog(
                    MyFrame.this,
                    "Wpisz dane:",
                    "Wprowadzanie danych",
                    JOptionPane.PLAIN_MESSAGE
            );
            label.setText("Currency exchange rate of the country "+ country + " to "+currency+ " is " +service.getRateFor(currency));
        });


        panel.add(Box.createVerticalStrut(10));
        panel.add(button1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button3);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button4);
        panel.add(Box.createVerticalStrut(10));

        add(panel, BorderLayout.CENTER);
        add(label, BorderLayout.NORTH);

    }

}
