package gregtech.api.logic;

import gregtech.api.enums.GT_Values.NBT;
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
    private int type = 0;

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

    public PowerLogic setType(int type) {
        this.type = type;
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

    public boolean isEnergyReceiver() {
        return (type & RECEIVER) > 0;
    }

    public boolean isEnergyEmitter() {
        return (type & EMITTER) > 0;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound powerLogic = new NBTTagCompound();
        powerLogic.setLong(NBT.POWER_LOGIC_ENERGY_CAPACITY, energyCapacity);
        powerLogic.setLong(NBT.POWER_LOGIC_STORED_ENERGY, storedEnergy);
        powerLogic.setLong(NBT.POWER_LOGIC_AMPERAGE, amperage);
        powerLogic.setLong(NBT.POWER_LOGIC_VOLTAGE, voltage);
        powerLogic.setInteger(NBT.POWER_LOGIC_TYPE, type);
        nbt.setTag(NBT.POWER_LOGIC, powerLogic);
    }

    public void loadFromNBT(NBTTagCompound nbt) {
        NBTTagCompound powerLogic = nbt.getCompoundTag(NBT.POWER_LOGIC);
        energyCapacity = powerLogic.getLong(NBT.POWER_LOGIC_ENERGY_CAPACITY);
        storedEnergy = powerLogic.getLong(NBT.POWER_LOGIC_STORED_ENERGY);
        amperage = powerLogic.getLong(NBT.POWER_LOGIC_AMPERAGE);
        voltage = powerLogic.getLong(NBT.POWER_LOGIC_VOLTAGE);
        type = powerLogic.getInteger(NBT.POWER_LOGIC_TYPE);
    }
}
