package command;

public class CommandTest {

    public static void main(String[] args) {

        System.out.println("-- Command Pattern Test --\n");

        Light livingRoomLight = new Light("Living Room");
        Light bedroomLight = new Light("Bedroom");

        RemoteControl remote = new RemoteControl();

        remote.setCommand(new LightOnCommand(livingRoomLight));
        remote.pressButton();

        remote.setCommand(new LightOnCommand(bedroomLight));
        remote.pressButton();

        System.out.println();

        remote.setCommand(new LightOffCommand(livingRoomLight));
        remote.pressButton();

        remote.setCommand(new LightOffCommand(bedroomLight));
        remote.pressButton();
    }
}
