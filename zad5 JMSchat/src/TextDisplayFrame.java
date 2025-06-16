import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextDisplayFrame extends JFrame {
    private JTextField textField;
    private JTextArea displayArea;

    public TextDisplayFrame(JMS jms) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        textField = new JTextField(20);
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton showButton = new JButton("Wyslij");

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Wpisz tekst: "));
        inputPanel.add(textField);
        inputPanel.add(showButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText();
                    textField.setText("");
                    try {
                        jms.sendMessage(inputText);
                    } catch (JMSException ex) {
                        throw new RuntimeException(ex);
                    }
            }
        });
    }
    public void writeMessage(String message) {
        displayArea.insert(message + "\n", 0);
    }
}