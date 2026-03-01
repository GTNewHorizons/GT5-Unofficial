package tectech.thing.metaTileEntity.multi.godforge.util;

import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.END;

import java.math.BigInteger;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.google.common.math.LongMath;

import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorStorage;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.UpgradeStorage;

public class ForgeOfGodsData {

    // Field default values for non-zero value defaults for item NBT checks
    public static final int DEFAULT_FUEL_CONSUMPTION_FACTOR = 1;
    public static final int DEFAULT_MAX_BATTERY_CHARGE = 100;
    public static final int DEFAULT_RING_AMOUNT = 1;
    public static final int DEFAULT_ROTATION_SPEED = 5;
    public static final int DEFAULT_STAR_SIZE = 20;
    public static final String DEFAULT_STAR_COLOR = ForgeOfGodsStarColor.DEFAULT.getName();
    public static final Formatters DEFAULT_FORMATTER = Formatters.COMMA;
    public static final BigInteger DEFAULT_TOTAL_POWER = BigInteger.ZERO;

    public static final long POWER_MILESTONE_CONSTANT = LongMath.pow(10, 15);
    public static final long RECIPE_MILESTONE_CONSTANT = LongMath.pow(10, 7);
    public static final long FUEL_MILESTONE_CONSTANT = 10_000;
    public static final long RECIPE_MILESTONE_T7_CONSTANT = RECIPE_MILESTONE_CONSTANT * LongMath.pow(4, 6);
    public static final long FUEL_MILESTONE_T7_CONSTANT = FUEL_MILESTONE_CONSTANT * LongMath.pow(3, 6);
    public static final BigInteger POWER_MILESTONE_T7_CONSTANT = BigInteger.valueOf(POWER_MILESTONE_CONSTANT)
        .multiply(BigInteger.valueOf(LongMath.pow(9, 6)));
    public static final double POWER_LOG_CONSTANT = Math.log(9);
    public static final double RECIPE_LOG_CONSTANT = Math.log(4);
    public static final double FUEL_LOG_CONSTANT = Math.log(3);
    // These were calculated externally by taking the consumption formulae and solving for the max fuel factor
    // that's still within max int fuel consumption (doing this here would be a bit annoying since they contain
    // an ln(x) + x situation)
    public static final int MAX_RESIDUE_FACTOR = 70;
    public static final int MAX_RESIDUE_FACTOR_DISCOUNTED = 72;
    public static final int MAX_STELLAR_PLASMA_FACTOR = 181;
    public static final int MAX_STELLAR_PLASMA_FACTOR_DISCOUNTED = 184;

    private int fuelConsumptionFactor = DEFAULT_FUEL_CONSUMPTION_FACTOR;
    private int selectedFuelType;
    private int internalBattery;
    private int maxBatteryCharge = DEFAULT_MAX_BATTERY_CHARGE;
    private int gravitonShardsAvailable;
    private int gravitonShardsSpent;
    private int ringAmount = DEFAULT_RING_AMOUNT;
    private int stellarFuelAmount;
    private int neededStartupFuel;
    private long fuelConsumption;
    private long totalRecipesProcessed;
    private long totalFuelConsumed;
    private float totalExtensionsBuilt;

    private float powerMilestonePercentage;
    private float recipeMilestonePercentage;
    private float fuelMilestonePercentage;
    private float structureMilestonePercentage;
    private float invertedPowerMilestonePercentage;
    private float invertedRecipeMilestonePercentage;
    private float invertedFuelMilestonePercentage;
    private float invertedStructureMilestonePercentage;

    private final int[] milestoneProgress = new int[4];

    private BigInteger totalPowerConsumed = DEFAULT_TOTAL_POWER;
    private boolean batteryCharging;
    private boolean inversion;
    private boolean gravitonShardEjection;
    private Formatters formatter = DEFAULT_FORMATTER;
    private boolean isRenderActive;
    private boolean secretUpgrade;
    private boolean isRendererDisabled;

    private final UpgradeStorage upgrades = new UpgradeStorage();
    private final ItemStack[] storedUpgradeWindowItems = new ItemStack[16];
    private final ItemStackHandler upgradeWindowHandler = new ItemStackHandler(storedUpgradeWindowItems);

    // Star cosmetics fields
    private final StarColorStorage starColors = new StarColorStorage();
    private String selectedStarColor = DEFAULT_STAR_COLOR;
    private int rotationSpeed = DEFAULT_ROTATION_SPEED;
    private int starSize = DEFAULT_STAR_SIZE;

    public int getFuelConsumptionFactor() {
        return fuelConsumptionFactor;
    }

    public void setFuelConsumptionFactor(int fuelConsumptionFactor) {
        this.fuelConsumptionFactor = fuelConsumptionFactor;
    }

    public int getSelectedFuelType() {
        return selectedFuelType;
    }

    public void setSelectedFuelType(int selectedFuelType) {
        this.selectedFuelType = selectedFuelType;
    }

    public int getInternalBattery() {
        return internalBattery;
    }

    public void setInternalBattery(int internalBattery) {
        this.internalBattery = internalBattery;
    }

    public int getMaxBatteryCharge() {
        return maxBatteryCharge;
    }

    public void setMaxBatteryCharge(int maxBatteryCharge) {
        this.maxBatteryCharge = maxBatteryCharge;
    }

    public int getGravitonShardsAvailable() {
        return gravitonShardsAvailable;
    }

    public void setGravitonShardsAvailable(int gravitonShardsAvailable) {
        this.gravitonShardsAvailable = gravitonShardsAvailable;
    }

    public int getGravitonShardsSpent() {
        return gravitonShardsSpent;
    }

    public void setGravitonShardsSpent(int gravitonShardsSpent) {
        this.gravitonShardsSpent = gravitonShardsSpent;
    }

    public int getRingAmount() {
        return ringAmount;
    }

    public void setRingAmount(int ringAmount) {
        this.ringAmount = ringAmount;
    }

    public int getStellarFuelAmount() {
        return stellarFuelAmount;
    }

    public void setStellarFuelAmount(int stellarFuelAmount) {
        this.stellarFuelAmount = stellarFuelAmount;
    }

    public int getNeededStartupFuel() {
        return neededStartupFuel;
    }

    public void setNeededStartupFuel(int neededStartupFuel) {
        this.neededStartupFuel = neededStartupFuel;
    }

    public long getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(long fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public long getTotalRecipesProcessed() {
        return totalRecipesProcessed;
    }

    public void setTotalRecipesProcessed(long totalRecipesProcessed) {
        this.totalRecipesProcessed = totalRecipesProcessed;
    }

    public long getTotalFuelConsumed() {
        return totalFuelConsumed;
    }

    public void setTotalFuelConsumed(long totalFuelConsumed) {
        this.totalFuelConsumed = totalFuelConsumed;
    }

    public float getTotalExtensionsBuilt() {
        return totalExtensionsBuilt;
    }

    public void setTotalExtensionsBuilt(float totalExtensionsBuilt) {
        this.totalExtensionsBuilt = totalExtensionsBuilt;
    }

    public float getPowerMilestonePercentage() {
        return powerMilestonePercentage;
    }

    public void setPowerMilestonePercentage(float powerMilestonePercentage) {
        this.powerMilestonePercentage = powerMilestonePercentage;
    }

    public float getRecipeMilestonePercentage() {
        return recipeMilestonePercentage;
    }

    public void setRecipeMilestonePercentage(float recipeMilestonePercentage) {
        this.recipeMilestonePercentage = recipeMilestonePercentage;
    }

    public float getFuelMilestonePercentage() {
        return fuelMilestonePercentage;
    }

    public void setFuelMilestonePercentage(float fuelMilestonePercentage) {
        this.fuelMilestonePercentage = fuelMilestonePercentage;
    }

    public float getStructureMilestonePercentage() {
        return structureMilestonePercentage;
    }

    public void setStructureMilestonePercentage(float structureMilestonePercentage) {
        this.structureMilestonePercentage = structureMilestonePercentage;
    }

    public float getInvertedPowerMilestonePercentage() {
        return invertedPowerMilestonePercentage;
    }

    public void setInvertedPowerMilestonePercentage(float invertedPowerMilestonePercentage) {
        this.invertedPowerMilestonePercentage = invertedPowerMilestonePercentage;
    }

    public float getInvertedRecipeMilestonePercentage() {
        return invertedRecipeMilestonePercentage;
    }

    public void setInvertedRecipeMilestonePercentage(float invertedRecipeMilestonePercentage) {
        this.invertedRecipeMilestonePercentage = invertedRecipeMilestonePercentage;
    }

    public float getInvertedFuelMilestonePercentage() {
        return invertedFuelMilestonePercentage;
    }

    public void setInvertedFuelMilestonePercentage(float invertedFuelMilestonePercentage) {
        this.invertedFuelMilestonePercentage = invertedFuelMilestonePercentage;
    }

    public float getInvertedStructureMilestonePercentage() {
        return invertedStructureMilestonePercentage;
    }

    public void setInvertedStructureMilestonePercentage(float invertedStructureMilestonePercentage) {
        this.invertedStructureMilestonePercentage = invertedStructureMilestonePercentage;
    }

    public int getMilestoneProgress(int index) {
        return milestoneProgress[index];
    }

    public void setMilestoneProgress(int index, int progress) {
        milestoneProgress[index] = progress;
    }

    public int[] getAllMilestoneProgress() {
        return milestoneProgress;
    }

    public BigInteger getTotalPowerConsumed() {
        return totalPowerConsumed;
    }

    public void setTotalPowerConsumed(BigInteger totalPowerConsumed) {
        this.totalPowerConsumed = totalPowerConsumed;
    }

    public boolean isBatteryCharging() {
        return batteryCharging;
    }

    public void setBatteryCharging(boolean batteryCharging) {
        this.batteryCharging = batteryCharging;
    }

    public boolean isInversion() {
        return inversion;
    }

    public void setInversion(boolean inversion) {
        this.inversion = inversion;
    }

    public boolean isGravitonShardEjection() {
        return gravitonShardEjection;
    }

    public void setGravitonShardEjection(boolean gravitonShardEjection) {
        this.gravitonShardEjection = gravitonShardEjection;
    }

    public Formatters getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatters formatter) {
        this.formatter = formatter;
    }

    public boolean isRenderActive() {
        return isRenderActive;
    }

    public void setRenderActive(boolean renderActive) {
        isRenderActive = renderActive;
    }

    public boolean isSecretUpgrade() {
        return secretUpgrade;
    }

    public void setSecretUpgrade(boolean secretUpgrade) {
        this.secretUpgrade = secretUpgrade;
    }

    public boolean isRendererDisabled() {
        return isRendererDisabled;
    }

    public void setRendererDisabled(boolean rendererDisabled) {
        isRendererDisabled = rendererDisabled;
    }

    public UpgradeStorage getUpgrades() {
        return upgrades;
    }

    public void resetAllUpgrades() {
        upgrades.resetAll();
    }

    public void unlockAllUpgrades() {
        upgrades.unlockAll();
    }

    public void unlockUpgrade(ForgeOfGodsUpgrade upgrade) {
        if (isUpgradeActive(upgrade)) return;
        if (!upgrades.checkPrerequisites(upgrade)) return;
        if (!upgrades.checkSplit(upgrade, ringAmount)) return;
        if (!upgrades.checkCost(upgrade, gravitonShardsAvailable)) return;

        upgrades.unlockUpgrade(upgrade);
        gravitonShardsAvailable -= upgrade.getShardCost();
        gravitonShardsSpent += upgrade.getShardCost();
    }

    public void respecUpgrade(ForgeOfGodsUpgrade upgrade) {
        if (!isUpgradeActive(upgrade)) return;
        if (!upgrades.checkDependents(upgrade)) return;

        upgrades.respecUpgrade(upgrade);
        gravitonShardsAvailable += upgrade.getShardCost();
        gravitonShardsSpent -= upgrade.getShardCost();

        if (upgrade == END) {
            gravitonShardEjection = false;
        }
    }

    public boolean isUpgradeActive(ForgeOfGodsUpgrade upgrade) {
        return upgrades.isUpgradeActive(upgrade);
    }

    public ItemStack[] getStoredUpgradeWindowItems() {
        return storedUpgradeWindowItems;
    }

    public ItemStackHandler getUpgradeWindowHandler() {
        return upgradeWindowHandler;
    }

    public StarColorStorage getStarColors() {
        return starColors;
    }

    public String getSelectedStarColor() {
        return selectedStarColor;
    }

    public void setSelectedStarColor(String selectedStarColor) {
        this.selectedStarColor = selectedStarColor;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getStarSize() {
        return starSize;
    }

    public void setStarSize(int starSize) {
        this.starSize = starSize;
    }

    public void serializeNBT(NBTTagCompound NBT, boolean force) {
        if (force || selectedFuelType != 0) NBT.setInteger("selectedFuelType", selectedFuelType);
        if (force || internalBattery != 0) NBT.setInteger("internalBattery", internalBattery);
        if (force || batteryCharging) NBT.setBoolean("batteryCharging", batteryCharging);
        if (force || gravitonShardsAvailable != 0) NBT.setInteger("gravitonShardsAvailable", gravitonShardsAvailable);
        if (force || gravitonShardsSpent != 0) NBT.setInteger("gravitonShardsSpent", gravitonShardsSpent);
        if (force || totalRecipesProcessed != 0) NBT.setLong("totalRecipesProcessed", totalRecipesProcessed);
        if (force || totalFuelConsumed != 0) NBT.setLong("totalFuelConsumed", totalFuelConsumed);
        if (force || stellarFuelAmount != 0) NBT.setInteger("starFuelStored", stellarFuelAmount);
        if (force || gravitonShardEjection) NBT.setBoolean("gravitonShardEjection", gravitonShardEjection);
        if (force || secretUpgrade) NBT.setBoolean("secretUpgrade", secretUpgrade);
        if (force || inversion) NBT.setBoolean("inversion", inversion);

        // Fields with non-zero defaults
        if (force || fuelConsumptionFactor != DEFAULT_FUEL_CONSUMPTION_FACTOR) {
            NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        }
        if (force || maxBatteryCharge != DEFAULT_MAX_BATTERY_CHARGE) {
            NBT.setInteger("batterySize", maxBatteryCharge);
        }
        if (force || !DEFAULT_TOTAL_POWER.equals(totalPowerConsumed)) {
            NBT.setByteArray("totalPowerConsumed", totalPowerConsumed.toByteArray());
        }
        if (force || formatter != DEFAULT_FORMATTER) {
            NBT.setInteger("formatter", formatter.ordinal());
        }

        // Upgrade window stored items
        if (force) {
            NBTTagCompound upgradeWindowStorageNBTTag = new NBTTagCompound();
            int storageIndex = 0;
            for (ItemStack itemStack : storedUpgradeWindowItems) {
                if (itemStack != null) {
                    upgradeWindowStorageNBTTag
                        .setInteger(storageIndex + "stacksizeOfStoredUpgradeItems", itemStack.stackSize);
                    NBT.setTag(storageIndex + "storedUpgradeItem", itemStack.writeToNBT(new NBTTagCompound()));
                }
                storageIndex++;
            }
            NBT.setTag("upgradeWindowStorage", upgradeWindowStorageNBTTag);
        }

        upgrades.serializeToNBT(NBT, force);
        starColors.serializeToNBT(NBT);
    }

    public void serializeRenderNBT(NBTTagCompound NBT) {
        NBT.setInteger("rotationSpeed", rotationSpeed);
        NBT.setInteger("starSize", starSize);
        NBT.setString("selectedStarColor", selectedStarColor);
        NBT.setInteger("ringAmount", ringAmount);
        NBT.setBoolean("isRenderActive", isRenderActive);
        NBT.setBoolean("isRendererDisabled", isRendererDisabled);
    }

    public void deserializeNBT(NBTTagCompound NBT) {
        selectedFuelType = NBT.getInteger("selectedFuelType");
        internalBattery = NBT.getInteger("internalBattery");
        batteryCharging = NBT.getBoolean("batteryCharging");
        gravitonShardsAvailable = NBT.getInteger("gravitonShardsAvailable");
        gravitonShardsSpent = NBT.getInteger("gravitonShardsSpent");
        totalRecipesProcessed = NBT.getLong("totalRecipesProcessed");
        totalFuelConsumed = NBT.getLong("totalFuelConsumed");
        stellarFuelAmount = NBT.getInteger("starFuelStored");
        gravitonShardEjection = NBT.getBoolean("gravitonShardEjection");
        secretUpgrade = NBT.getBoolean("secretUpgrade");
        inversion = NBT.getBoolean("inversion");

        // Fields with non-zero defaults
        if (NBT.hasKey("fuelConsumptionFactor")) {
            fuelConsumptionFactor = NBT.getInteger("fuelConsumptionFactor");
        }
        if (NBT.hasKey("batterySize")) {
            maxBatteryCharge = NBT.getInteger("batterySize");
        }
        if (NBT.hasKey("totalPowerConsumed")) {
            totalPowerConsumed = new BigInteger(NBT.getByteArray("totalPowerConsumed"));
        }
        // Legacy NBT handling of the old formatting mode
        if (NBT.hasKey("formattingMode")) {
            int index = MathHelper.clamp_int(NBT.getInteger("formattingMode"), 0, Formatters.VALUES.length);
            formatter = Formatters.VALUES[index];
        }
        if (NBT.hasKey("formatter")) {
            int index = MathHelper.clamp_int(NBT.getInteger("formatter"), 0, Formatters.VALUES.length);
            formatter = Formatters.VALUES[index];
        }

        // Stored items
        NBTTagCompound tempItemTag = NBT.getCompoundTag("upgradeWindowStorage");
        if (tempItemTag != null) {
            for (int index = 0; index < 16; index++) {
                int stackSize = tempItemTag.getInteger(index + "stacksizeOfStoredUpgradeItems");
                ItemStack itemStack = ItemStack.loadItemStackFromNBT(NBT.getCompoundTag(index + "storedUpgradeItem"));
                if (itemStack != null) {
                    storedUpgradeWindowItems[index] = itemStack.splitStack(stackSize);
                }
            }
        }

        // Renderer information
        if (NBT.hasKey("rotationSpeed")) rotationSpeed = NBT.getInteger("rotationSpeed");
        if (NBT.hasKey("starSize")) starSize = NBT.getInteger("starSize");
        if (NBT.hasKey("selectedStarColor")) selectedStarColor = NBT.getString("selectedStarColor");
        if (NBT.hasKey("ringAmount")) ringAmount = NBT.getInteger("ringAmount");
        isRenderActive = NBT.getBoolean("isRenderActive");
        isRendererDisabled = NBT.getBoolean("isRendererDisabled");

        upgrades.rebuildFromNBT(NBT);
        starColors.rebuildFromNBT(NBT);
    }
}
