import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destinationQueue = null;
    private Destination destinationTopic = null;
    private MessageConsumer consumer = null;

    public Receiver() {

    }

    public void receiveMessageQueue() {
        try {
            factory = new ActiveMQConnectionFactory(Sender.BROKER_URL_INTANCE_B);
            connection = factory.createConnection();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destinationQueue = session.createQueue(Sender.QUEUE_NAME);
            ConsumerQueue lConsumerQueue1 = new ConsumerQueue(destinationQueue, "Consumer-Queue-1");
            ConsumerQueue lConsumerQueue2 = new ConsumerQueue(destinationQueue, "Consumer-Queue-2");
            Thread lThread1 = new Thread(lConsumerQueue1);
            Thread lThread2 = new Thread(lConsumerQueue2);

            lThread1.start();
            lThread2.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageTopic() {
        try {
            factory = new ActiveMQConnectionFactory(Sender.BROKER_URL_INTANCE_B);
            connection = factory.createConnection();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destinationTopic = session.createTopic(Sender.TOPIC_NAME);
            ConsumerTopic lConsumerTopic1 = new ConsumerTopic(destinationTopic, "Consumer-Topic-1");
            ConsumerTopic lConsumerTopic2 = new ConsumerTopic(destinationTopic, "Consumer-Topic-2");
            Thread lThread1 = new Thread(lConsumerTopic1);
            Thread lThread2 = new Thread(lConsumerTopic2);

            lThread1.start();
            lThread2.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        //receiver.receiveMessageQueue();
        receiver.receiveMessageTopic();
    }

    class ConsumerQueue implements Runnable{

        Destination destination_;
        String consumerName_;
        MessageConsumer consumer_;

        ConsumerQueue(Destination aInDestination, String aInConsumerName) throws JMSException {
            destination_ = aInDestination;
            consumer_ = session.createConsumer(destination_);
            consumerName_ = aInConsumerName;
        }

        @Override
        public void run() {
            System.out.println("Starting the Receive Thread \t"+ consumerName_);
            while (true){
                Message message = null;
                try {
                    message = consumer_.receive();
                    if (message instanceof TextMessage) {
                        TextMessage text = (TextMessage) message;
                        System.out.println("Message From :" + consumerName_ +" \t Message is : " + text.getText());
                        if (text.getText().equals(Sender.END_MESSAGE)){
                            break;
                        }
                    }
                } catch (JMSException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            System.out.println("Exiting the Receive Thread \t"+consumerName_);

            try {
                consumer_.close();
            } catch (JMSException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    class ConsumerTopic implements Runnable{

        Destination destination_;
        String consumerName_;
        MessageConsumer consumer_;

        ConsumerTopic(Destination aInDestination, String aInConsumerName) throws JMSException {
            destination_ = aInDestination;
            consumer_ = session.createConsumer(destination_);
            consumerName_ = aInConsumerName;
        }

        @Override
        public void run() {
            System.out.println("Starting the Receive Thread \t"+ consumerName_);
            while (true){
                Message message = null;
                try {
                    message = consumer_.receive();
                    if (message instanceof TextMessage) {
                        TextMessage text = (TextMessage) message;
                        System.out.println("Message From :" + consumerName_ +" \t Message is : " + text.getText());
                        if (text.getText().equals(Sender.END_MESSAGE)){
                            break;
                        }
                    }
                } catch (JMSException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            System.out.println("Exiting the Receive Thread \t"+consumerName_);

            try {
                consumer_.close();
            } catch (JMSException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}