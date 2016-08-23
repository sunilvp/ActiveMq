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
    private Destination destination_queue = null;
    private Destination destination_topic = null;
    private MessageProducer producer = null;


    public static final String BROKER_URL_INTANCE_A =  "failover://tcp://localhost:61618";
    public static final String BROKER_URL_INTANCE_B =  "failover://tcp://localhost:61619";
    public static final String QUEUE_NAME =  "Sunil_queue";
    public static final String TOPIC_NAME =  "Sunil_topic";
    public static final String END_MESSAGE =  "END";

    public Sender() {

    }

    public void sendMessageQueue() {

        try {
            factory = new ActiveMQConnectionFactory(
                    BROKER_URL_INTANCE_A);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination_queue = session.createQueue(QUEUE_NAME);
            producer = session.createProducer(destination_queue);
            TextMessage message = session.createTextMessage();
            message.setText(/*Receiver.END_MESSAGE*/"Message queue sent 10");
            producer.send(message);
            System.out.println("Sent to queue: " + message.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageTopic() {

        try {
            factory = new ActiveMQConnectionFactory(
                    BROKER_URL_INTANCE_A);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination_topic = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(destination_topic);
            TextMessage message = session.createTextMessage();
            message.setText(/*Receiver.END_MESSAGE*/"Message topic sent 10");
            producer.send(message);
            System.out.println("Sent to topic: " + message.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Sender sender = new Sender();
       // sender.sendMessageQueue();
        sender.sendMessageTopic();
    }

}