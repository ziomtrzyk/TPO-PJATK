import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;


public class JMS{
    private TopicPublisher publisher;
    private TopicSession session;
    private TextDisplayFrame frame;

    public JMS() throws NamingException, JMSException {
        createJMS();
        frame = new TextDisplayFrame(this);
        frame.setVisible(true);
    }

    public void sendMessage(String msg) throws JMSException {
        TextMessage message = session.createTextMessage(msg);
        publisher.publish(message);
    }

    public void createJMS() throws NamingException, JMSException{
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        hashtable.put(Context.PROVIDER_URL, "tcp://localhost:3035");

        Context context = new InitialContext(hashtable);

        TopicConnectionFactory fact = (TopicConnectionFactory) context.lookup("ConnectionFactory");

        Topic topic = (Topic) context.lookup("topic1");

        TopicConnection connection = fact.createTopicConnection();
        connection.start();

        session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        publisher = session.createPublisher(topic);

        TopicSubscriber subscriber = session.createSubscriber(topic);

        subscriber.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("RECEIVED: " + textMessage.getText());
                    frame.writeMessage(textMessage.getText());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
