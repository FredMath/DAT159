package subscribe;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import publish.MQTTPubHeat;

public abstract class MQTTSubTemperature extends MQTTSub implements Runnable {

    private MQTTPubHeat pubHeating;


    public MQTTSubTemperature(MQTTPubHeat pubHeating) throws MqttException {
        super();
        this.pubHeating = pubHeating;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            double temp = Double.parseDouble(new String(message.getPayload()));
            pubHeating.publish(temp < 20 ? "ON" : "OFF");

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }


}