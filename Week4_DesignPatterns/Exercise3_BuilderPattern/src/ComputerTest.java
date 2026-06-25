package builder;

public class ComputerTest {

    public static void main(String[] args) {

        System.out.println("-- Builder Pattern Test --\n");

        Computer gamingPC = new Computer.Builder("Intel Core i9", "32GB DDR5", "2TB NVMe SSD")
                .setGpu("NVIDIA RTX 4090")
                .setBluetooth(true)
                .setWifi(true)
                .build();

        System.out.println("Gaming PC:");
        gamingPC.showSpecs();

        System.out.println();

        Computer officePC = new Computer.Builder("Intel Core i5", "16GB DDR4", "512GB SSD")
                .setWifi(true)
                .build();

        System.out.println("Office PC:");
        officePC.showSpecs();

        System.out.println();

        Computer basicPC = new Computer.Builder("AMD Ryzen 3", "8GB DDR4", "256GB HDD")
                .build();

        System.out.println("Basic PC:");
        basicPC.showSpecs();
    }
}
