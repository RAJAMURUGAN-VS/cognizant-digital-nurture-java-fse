package builder;

public class Computer {

    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private boolean hasBluetooth;
    private boolean hasWifi;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.hasBluetooth = builder.hasBluetooth;
        this.hasWifi = builder.hasWifi;
    }

    public void showSpecs() {
        System.out.println("Computer Specs:");
        System.out.println("  CPU      : " + cpu);
        System.out.println("  RAM      : " + ram);
        System.out.println("  Storage  : " + storage);
        System.out.println("  GPU      : " + (gpu != null ? gpu : "Integrated"));
        System.out.println("  Bluetooth: " + (hasBluetooth ? "Yes" : "No"));
        System.out.println("  WiFi     : " + (hasWifi ? "Yes" : "No"));
    }

    public static class Builder {

        private String cpu;
        private String ram;
        private String storage;

        private String gpu;
        private boolean hasBluetooth;
        private boolean hasWifi;

        public Builder(String cpu, String ram, String storage) {
            this.cpu = cpu;
            this.ram = ram;
            this.storage = storage;
        }

        public Builder setGpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder setBluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }

        public Builder setWifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}
