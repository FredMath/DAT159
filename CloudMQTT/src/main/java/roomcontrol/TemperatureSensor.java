package roomcontrol;

public class TemperatureSensor {

    private Room room;

    public TemperatureSensor(Room room) {

        this.room = room;
    }

    public double read() {

        return room.sense();
    }

}