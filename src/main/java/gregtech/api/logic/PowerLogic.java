package gregtech.api.logic;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values.NBT;

/**
 * Power logic for machines. This is used to store all the important variables for a machine to have energy and use it
 * in any way.
 * 
 * @author BlueWeabo, Maxim
 */
public class PowerLogic {

    public static final int NONE = 0;
    public static final int RECEIVER = 1;
    public static final int EMITTER = 2;
    public static final int BOTH = RECEIVER | EMITTER;
    private static float wirelessChargeFactor = 0.5F;
    private long storedEnergy = 0;
    private long energyCapacity = 0;
    private long voltage = 0;
    private long amperage = 0;
    private int type = 0;
    private boolean canUseLaser = false;
    private boolean canUseWireless = false;
    private UUID owner;

    public PowerLogic() {}

    /**
     * Sets the max voltage the logic can accept
     */
    @Nonnull
    public PowerLogic setMaxVoltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    /**
     * Sets the maximum amount of energy the machine can store inside of it
     */
    @Nonnull
    public PowerLogic setEnergyCapacity(long energyCapacity) {
        this.energyCapacity = energyCapacity;
        return this;
    }

    /**
     * Sets the maximum amount of amps a machine can receive from an emitter
     */
    @Nonnull
    public PowerLogic setMaxAmperage(long amperage) {
        this.amperage = amperage;
        return this;
    }

    /**
     * Sets the type of power logic this is. Whether it will receive EU or emit it to others, or do both
     */
    @Nonnull
    public PowerLogic setType(int type) {
        this.type = type;
        return this;
    }

    /**
     * If this power logic can use lasers to be used for it
     */
    @Nonnull
    public PowerLogic setCanUseLaser(boolean canUse) {
        canUseLaser = canUse;
        return this;
    }

    /**
     * If the power logic should use wireless EU first before using its internal buffer
     */
    @Nonnull
    public PowerLogic setCanUseWireless(boolean canUse, UUID owner) {
        canUseWireless = canUse;
        this.owner = owner;
        return this;
    }

    /**
     * Adding energy directly to the buffer, but only if it has the capacity.
     */
    public boolean addEnergyUnsafe(long totalEUAdded) {
        if (storedEnergy + totalEUAdded >= energyCapacity) {
            return false;
        }

        storedEnergy += totalEUAdded;
        return true;
    }

    /**
     * Adding energy to the buffer if the voltage given isn't higher than the voltage of the logic
     */
    public boolean addEnergy(long voltage, long amperage) {
        if (voltage > this.voltage) {
            return false;
        }

        return addEnergyUnsafe(voltage * amperage);
    }

    /**
     * Same as {@link #addEnergy(long, long)}, but only 1 amp of it
     */
    public boolean addEnergy(long voltage) {
        return addEnergy(voltage, 1);
    }

    /**
     * Injecting energy in the multiblock ampere per ampere until full or until we have added the maximum possible
     * amperes for this tick
     * 
     * @param voltage           At what voltage are the amps?
     * @param availableAmperage How much amperage do we have available
     * @return Amount of amperes used
     */
    public long injectEnergy(long voltage, long availableAmperage) {
        if (canUseWireless) return 0;
        long usedAmperes = 0;
        while (addEnergy(voltage, 1) && usedAmperes < amperage) {
            usedAmperes++;
        }

        return usedAmperes;
    }

    /**
     * Remove energy from the logic only if it has enough to be removed.
     */
    public boolean removeEnergyUnsafe(long totalEURemoved) {
        if (canUseWireless) {
            if (storedEnergy < energyCapacity * wirelessChargeFactor) {
                if (addEUToGlobalEnergyMap(owner, -(energyCapacity - storedEnergy))) {
                    storedEnergy = energyCapacity;
                }
            }
        }
        if (storedEnergy - totalEURemoved < 0) {
            return false;
        }

        storedEnergy -= totalEURemoved;
        return true;
    }

    /**
     * Remove the given voltage for the amount of amperage if the removed isn't higher than the logic's voltage
     */
    public boolean removeEnergy(long voltage, long amperage) {
        if (voltage > this.voltage) {
            return false;
        }

        return removeEnergyUnsafe(voltage * amperage);
    }

    /**
     * Same as {@link #removeEnergy(long, long)}, but with only 1 amperage
     */
    public boolean removeEnergy(long voltage) {
        return removeEnergy(voltage, 1);
    }

    /**
     * @return The maximum energy that can be stored.
     */
    public long getCapacity() {
        return energyCapacity;
    }

    /**
     * @return The maximum voltage that is available
     */
    public long getVoltage() {
        return voltage;
    }

    /**
     * @return The current energy stored
     */
    public long getStoredEnergy() {
        return storedEnergy;
    }

    /**
     * @return The current maximum Amperage
     */
    public long getAmperage() {
        return amperage;
    }

    /**
     * Is the logic a receiver to receive energy
     */
    public boolean isEnergyReceiver() {
        return (type & RECEIVER) > 0;
    }

    /**
     * Is the logic a emitter to emit energy
     */
    public boolean isEnergyEmitter() {
        return (type & EMITTER) > 0;
    }

    /**
     * Saves the power logic to its own nbt tag before saving it to the given one.
     * 
     * @param nbt Tag where you want to save the power logic tag to.
     */
    public void saveToNBT(NBTTagCompound nbt) {
        NBTTagCompound powerLogic = new NBTTagCompound();
        powerLogic.setLong(NBT.POWER_LOGIC_ENERGY_CAPACITY, energyCapacity);
        powerLogic.setLong(NBT.POWER_LOGIC_STORED_ENERGY, storedEnergy);
        powerLogic.setLong(NBT.POWER_LOGIC_AMPERAGE, amperage);
        powerLogic.setLong(NBT.POWER_LOGIC_VOLTAGE, voltage);
        powerLogic.setInteger(NBT.POWER_LOGIC_TYPE, type);
        nbt.setTag(NBT.POWER_LOGIC, powerLogic);
    }

    /**
     * Loads the power logic from its own nbt after getting it from the given one
     * 
     * @param nbt Tag where the power logic tag was saved to
     */
    public void loadFromNBT(NBTTagCompound nbt) {
        NBTTagCompound powerLogic = nbt.getCompoundTag(NBT.POWER_LOGIC);
        energyCapacity = powerLogic.getLong(NBT.POWER_LOGIC_ENERGY_CAPACITY);
        storedEnergy = powerLogic.getLong(NBT.POWER_LOGIC_STORED_ENERGY);
        amperage = powerLogic.getLong(NBT.POWER_LOGIC_AMPERAGE);
        voltage = powerLogic.getLong(NBT.POWER_LOGIC_VOLTAGE);
        type = powerLogic.getInteger(NBT.POWER_LOGIC_TYPE);
    }

    /**
     * Can we use lasers for inputting EU
     */
    public boolean canUseLaser() {
        return canUseLaser;
    }
}
