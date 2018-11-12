package subscribe;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import roomcontrol.Display;

import java.sql.Timestamp;

public class MQTTSubDisplay extends MQTTSub implements  Runnable{
    private Display display;

    public MQTTSubDisplay(Display display) {
        super();
        this.display = display;
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String tmp = "Time:\t" + time +
                "  Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "  QoS:\t" + message.getQos();
        display.write(tmp);
    }

}
