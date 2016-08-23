import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageProducer producer = null;


    private static final String BROKER_URL_INTANCE_A =  "failover://tcp://localhost:61618";
    private static final String QUEUE_NAME =  "Sunil_queue";

    public Sender() {

    }

    public void sendMessage() {

        try {
            factory = new ActiveMQConnectionFactory(
                    BROKER_URL_INTANCE_A);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(QUEUE_NAME);
            producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(/*Receiver.END_MESSAGE*/"Message sent 9");
            producer.send(message);
            System.out.println("Sent: " + message.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.sendMessage();
    }

}