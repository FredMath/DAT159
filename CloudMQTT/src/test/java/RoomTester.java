import roomcontrol.Heating;
import roomcontrol.Room;
import roomcontrol.TemperatureSensor;

public class RoomTester {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Room room = new Room(0);

        TemperatureSensor sensor = new TemperatureSensor(room);

        Heating heater = new Heating(room);

        SimpleController controller = new SimpleController(heater);

        try {

            Thread t1 = new Thread(String.valueOf(sensor));
            Thread t2 = new Thread(controller);

            t1.start();
            t2.start();

            t1.join();
            t2.join();

        } catch (Exception ex) {
            System.out.println("roomcontrol.TemperatureSensor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}