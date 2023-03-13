package gregtech.api.logic;

public class PowerLogic {
    private long storedEnergy = 0;
    private long energyCapacity = 0;
    private long voltage = 0;

    public PowerLogic() {}

    public PowerLogic setMaxVoltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    public PowerLogic setEnergyCapacity(long energyCapacity) {
        this.energyCapacity = energyCapacity;
        return this;
    }
    
    public boolean addEnergyUnsafe(long totalEUAdded) {
        if (storedEnergy + totalEUAdded > energyCapacity) {
            return false;
        }

        storedEnergy += totalEUAdded;
        return true;
    }

    public boolean addEnergy(long voltage, long amps) {
        if (voltage > this.voltage) {
            return false;
        }

        return addEnergyUnsafe(voltage * amps);
    }

    public boolean addEnergy(long voltage) {
        return addEnergy(voltage, 1);
    }

    public boolean removeEnergyUnsafe(long totalEURemoved) {
        if (storedEnergy - totalEURemoved < 0) {
            return false;
        }
        
        storedEnergy -= totalEURemoved;
        return true;
    }

    public boolean removeEnergy(long voltage, long amps) {
        if (voltage > this.voltage) {
            return false;
        }

        return removeEnergyUnsafe(voltage * amps);
    }

    public boolean removeEnergy(long voltage) {
        return removeEnergy(voltage, 1);
    }

    public long getCapacity() {
        return energyCapacity;
    }

    public long getVoltage() {
        return voltage;
    }

    public long getStoredEnergy() {
        return storedEnergy;
    }
}
