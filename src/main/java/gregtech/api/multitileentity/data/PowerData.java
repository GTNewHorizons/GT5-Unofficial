package gregtech.api.multitileentity.data;

public class PowerData {

    private long voltage;
    private long amperage;
    private long storedEnergy;
    private long capacity;
    private boolean laser;
    private boolean wireless;

    public PowerData() {}

    public PowerData setVoltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    public PowerData setAmperage(long amperage) {
        this.amperage = amperage;
        return this;
    }

    public PowerData setStoredEnergy(long storedEnergy) {
        this.storedEnergy = storedEnergy;
        return this;
    }

    public PowerData setCapacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    public PowerData setLaser(boolean laser) {
        this.laser = laser;
        return this;
    }

    public PowerData setWireless(boolean wireless) {
        this.wireless = wireless;
        return this;
    }

    public long getVoltage() {
        return voltage;
    }

    public long getAmperage() {
        return amperage;
    }

    public long getStoredEnergy() {
        return storedEnergy;
    }

    public long getCapacity() {
        return capacity;
    }

    public boolean isLaser() {
        return laser;
    }

    public boolean isWireless() {
        return wireless;
    }
}
