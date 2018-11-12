package publish;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import roomcontrol.TemperatureSensor;

public class MQTTPubTemp extends  MQTTPub implements Runnable{

    private String topic;
    private int qos;

    private TemperatureSensor sensor;


    public MQTTPubTemp(TemperatureSensor sensor) {
        super();
        topic = "Temp";
        qos = 1;
        this.sensor = sensor;
    }

    public void publish() throws MqttException, InterruptedException {
        while (true) {

            String temp = String.valueOf(sensor.read());
            System.out.println("Publishing message: " + temp);
            MqttMessage message = new MqttMessage(temp.getBytes());
            message.setQos(qos);
            getPublisherClient().publish(topic, message);
            Thread.sleep(10000);
        }

    }
    @Override
    public void run() {
        try {
            connect();
            System.out.println("Sensor publisher running");
            publish();
            Disconnect();
            System.out.println("Sensor publisher stopping");
        } catch (InterruptedException | MqttException e){
            System.out.println("sensor publishing: " + e.getMessage());
            e.printStackTrace();

        }
    }

}