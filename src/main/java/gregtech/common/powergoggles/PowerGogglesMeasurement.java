package gregtech.common.powergoggles;

import java.math.BigInteger;

public class PowerGogglesMeasurement {

    private final boolean isWireless;
    private final BigInteger measurement;
    private long capacity;

    public PowerGogglesMeasurement(boolean isWireless, BigInteger measurement, long capacity) {
        this.isWireless = isWireless;
        this.measurement = measurement;
        this.capacity = capacity;
    }

    public PowerGogglesMeasurement(boolean isWireless, BigInteger measurement) {
        this.measurement = measurement;
        this.isWireless = isWireless;
    }

    public boolean isWireless() {
        return isWireless;
    }

    public BigInteger getMeasurement() {
        return measurement;
    }

    public long getCapacity() {
        return capacity;
    }
}
