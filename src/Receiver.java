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
    private Destination destination = null;
    private MessageConsumer consumer = null;

    private static final String BROKER_URL_INTANCE_A =  "failover://tcp://localhost:61618";
    private static final String QUEUE_NAME =  "Sunil_queue";
    public static final String END_MESSAGE =  "END";

    public Receiver() {

    }

    public void receiveMessage() {
        try {
            factory = new ActiveMQConnectionFactory(BROKER_URL_INTANCE_A);
            connection = factory.createConnection();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(QUEUE_NAME);
            Consumer lConsumer1 = new Consumer(destination, "Consumer1");
            Consumer lConsumer2 = new Consumer(destination, "Consumer2");
            Thread lThread1 = new Thread(lConsumer1);
            Thread lThread2 = new Thread(lConsumer2);

            lThread1.start();
            lThread2.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        receiver.receiveMessage();
    }

    class Consumer implements Runnable{


        Destination destination_;
        String consumerName_;
        MessageConsumer consumer_;

        Consumer(Destination aInDestination, String aInConsumerName) throws JMSException {
            destination_ = aInDestination;
            consumer_ = session.createConsumer(destination_);
            consumerName_ = aInConsumerName;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
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
                        if (text.getText().equals(END_MESSAGE)){
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