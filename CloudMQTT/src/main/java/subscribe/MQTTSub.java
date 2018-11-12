package subscribe;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public abstract class MQTTSub implements MqttCallback, Runnable {

    private String broker= "tcp:m20.cloudmqtt.com:18403";;
    private String username ="ftrzraoc";
    private String password ="fF1-j_VU_KqM";

    private MqttClient client;


    MQTTSub() {
    }

    void connect() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());

        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), new MemoryPersistence());
            client.setCallback(this);
            client.connect(connOpts);

        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Connected");
    }

    void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);

    }
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public abstract void messageArrived(String topic, MqttMessage message) throws Exception;

    @Override
    public void run() {
        try {
            System.out.println("Connecting to broker: " + getBroker());
            connect();

            String topic = "Temp";
            int qos = 1;

            getClient().subscribe(topic, qos);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    String getBroker() {
        return broker;
    }

    MqttClient getClient() {
        return client;
    }
}
