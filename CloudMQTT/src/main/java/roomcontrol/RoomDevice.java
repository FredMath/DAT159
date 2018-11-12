package roomcontrol;

import publish.MQTTPubTemp;

public class RoomDevice {

    public static void main(String[] args) {

        Room room = new Room(20);

        TemperatureSensor sensor = new TemperatureSensor(room);

        MQTTPubTemp sensorpub = new MQTTPubTemp(sensor);

        try {

            Thread temppublisher = new Thread(sensorpub);

            // TODO: add heating subscriber running in its own thread
            temppublisher.start();

            temppublisher.join();

        } catch (Exception ex) {

            System.out.println("roomcontrol.RoomDevice: " + ex.getMessage());
            ex.printStackTrace();
        }



    }

}