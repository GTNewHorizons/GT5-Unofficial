package gregtech.api.logic;

import net.minecraft.nbt.NBTTagCompound;

public class PowerLogic {
    public static int NONE = 0;
    public static int RECEIVER = 1;
    public static int EMITTER = 2;
    public static int BOTH = RECEIVER | EMITTER;
    private long storedEnergy = 0;
    private long energyCapacity = 0;
    private long voltage = 0;
    private long amperage = 0;

    public PowerLogic() {}

    public PowerLogic setMaxVoltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    public PowerLogic setEnergyCapacity(long energyCapacity) {
        this.energyCapacity = energyCapacity;
        return this;
    }

    public PowerLogic setAmperage(long amperage) {
        this.amperage = amperage;
        return this;
    }
    
    public boolean addEnergyUnsafe(long totalEUAdded) {
        if (storedEnergy + totalEUAdded >= energyCapacity) {
            return false;
        }

        storedEnergy += totalEUAdded;
        return true;
    }

    public boolean addEnergy(long voltage, long amperage) {
        if (voltage > this.voltage) {
            return false;
        }

        return addEnergyUnsafe(voltage * amperage);
    }

    public boolean addEnergy(long voltage) {
        return addEnergy(voltage, 1);
    }

    public long injectEnergy(long voltage, long availableAmperage) {
        long usedAmperes = 0;
        while (addEnergy(voltage, 1) && usedAmperes < amperage) {
            usedAmperes++;
        }

        return usedAmperes;
    }

    public boolean removeEnergyUnsafe(long totalEURemoved) {
        if (storedEnergy - totalEURemoved < 0) {
            return false;
        }
        
        storedEnergy -= totalEURemoved;
        return true;
    }

    public boolean removeEnergy(long voltage, long amperage) {
        if (voltage > this.voltage) {
            return false;
        }

        return removeEnergyUnsafe(voltage * amperage);
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

    public void writeToNBT(NBTTagCompound nbt) {
        
    }
}
