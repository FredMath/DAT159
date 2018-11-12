package publish;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPub {

    private String broker = "tcp:m20.cloudmqtt.com:18403";
    private String username= "ftrzraoc";
    private String password = "fF1-j_VU_KqM";;

    private MqttClient publisherClient;


    MQTTPub() {
    }

    void connect() {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            publisherClient = new MqttClient(broker, MqttClient.generateClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            System.out.println("Connecting to broker: " + broker);
            publisherClient.connect(connOpts);
            System.out.println("Connected");

        } catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
    }

    void Disconnect() throws MqttException{
        publisherClient.disconnect();

    }

    MqttClient getPublisherClient() {
        return publisherClient;
    }



}
