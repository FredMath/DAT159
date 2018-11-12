package publish;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTPubHeat extends MQTTPub{

    private String topic;
    private int qos;


    public MQTTPubHeat(String topic, int qos) {
        super();
        this.topic = "Heat";
        this.qos = 1;
        connect();
    }

    public void publish(String heatState){
        System.out.println("Publishing message: " + heatState);
        MqttMessage message = new MqttMessage(heatState.getBytes());
        message.setQos(qos);

        try {
            getPublisherClient().publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();

        }
    }

}