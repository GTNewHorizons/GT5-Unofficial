package tectech.thing.metaTileEntity.multi.godforge.util;

import java.math.BigInteger;
import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import com.google.common.math.LongMath;

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
    public static final MilestoneFormatter DEFAULT_FORMATTING_MODE = MilestoneFormatter.COMMA;
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
    private BigInteger totalPowerConsumed = DEFAULT_TOTAL_POWER;
    private boolean batteryCharging;
    private boolean inversion;
    private boolean gravitonShardEjection;
    private MilestoneFormatter formattingMode = DEFAULT_FORMATTING_MODE;
    private boolean isRenderActive;
    private boolean secretUpgrade;
    private boolean isRendererDisabled;

    private final UpgradeStorage upgrades = new UpgradeStorage();
    private ForgeOfGodsUpgrade currentUpgradeWindow;

    // Star cosmetics fields
    // actual star cosmetics
    private final StarColorStorage starColors = new StarColorStorage();
    private String selectedStarColor = DEFAULT_STAR_COLOR;
    private int rotationSpeed = DEFAULT_ROTATION_SPEED;
    private int starSize = DEFAULT_STAR_SIZE;
    // editing star color
    private ForgeOfGodsStarColor newStarColor = starColors.newTemplateColor();
    private int starColorR, starColorG, starColorB;
    private float starGamma;
    private int editingStarIndex; // editing a full color preset
    private int editingColorIndex; // editing a single color in a preset
    private ForgeOfGodsStarColor importedStarColor;

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

    public void toggleBatteryCharging() {
        this.batteryCharging = !this.batteryCharging;
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

    public void toggleGravitonShardEjection() {
        this.gravitonShardEjection = !this.gravitonShardEjection;
    }

    public MilestoneFormatter getFormattingMode() {
        return formattingMode;
    }

    public void setFormattingMode(MilestoneFormatter formattingMode) {
        this.formattingMode = formattingMode;
    }

    public void cycleFormattingMode() {
        this.formattingMode = this.formattingMode.cycle();
    }

    public String format(Number number) {
        return this.formattingMode.format(number);
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

    public void toggleSecretUpgrade() {
        this.secretUpgrade = !this.secretUpgrade;
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

    public Collection<ForgeOfGodsUpgrade> getAllUpgrades() {
        return upgrades.getAllUpgrades();
    }

    public void resetAllUpgrades() {
        upgrades.resetAll();
    }

    public void unlockAllUpgrades() {
        upgrades.unlockAll();
    }

    public void unlockUpgrade(ForgeOfGodsUpgrade upgrade) {
        upgrades.unlockUpgrade(upgrade);
    }

    public boolean isUpgradeActive(ForgeOfGodsUpgrade upgrade) {
        return upgrades.isUpgradeActive(upgrade);
    }

    public ForgeOfGodsUpgrade getCurrentUpgradeWindow() {
        return currentUpgradeWindow;
    }

    public void setCurrentUpgradeWindow(ForgeOfGodsUpgrade currentUpgradeWindow) {
        this.currentUpgradeWindow = currentUpgradeWindow;
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

    public ForgeOfGodsStarColor getNewStarColor() {
        return newStarColor;
    }

    public void setNewStarColor(ForgeOfGodsStarColor newStarColor) {
        this.newStarColor = newStarColor;
    }

    public int getStarColorR() {
        return starColorR;
    }

    public void setStarColorR(int starColorR) {
        this.starColorR = starColorR;
    }

    public int getStarColorG() {
        return starColorG;
    }

    public void setStarColorG(int starColorG) {
        this.starColorG = starColorG;
    }

    public int getStarColorB() {
        return starColorB;
    }

    public void setStarColorB(int starColorB) {
        this.starColorB = starColorB;
    }

    public float getStarGamma() {
        return starGamma;
    }

    public void setStarGamma(float starGamma) {
        this.starGamma = starGamma;
    }

    public int getEditingStarIndex() {
        return editingStarIndex;
    }

    public void setEditingStarIndex(int editingStarIndex) {
        this.editingStarIndex = editingStarIndex;
    }

    public int getEditingColorIndex() {
        return editingColorIndex;
    }

    public void setEditingColorIndex(int editingColorIndex) {
        this.editingColorIndex = editingColorIndex;
    }

    public ForgeOfGodsStarColor getImportedStarColor() {
        return importedStarColor;
    }

    public void setImportedStarColor(ForgeOfGodsStarColor importedStarColor) {
        this.importedStarColor = importedStarColor;
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
        if (force || fuelConsumptionFactor != ForgeOfGodsData.DEFAULT_FUEL_CONSUMPTION_FACTOR) {
            NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        }
        if (force || maxBatteryCharge != ForgeOfGodsData.DEFAULT_MAX_BATTERY_CHARGE) {
            NBT.setInteger("batterySize", maxBatteryCharge);
        }
        if (force || !ForgeOfGodsData.DEFAULT_TOTAL_POWER.equals(totalPowerConsumed)) {
            NBT.setByteArray("totalPowerConsumed", totalPowerConsumed.toByteArray());
        }
        if (force || formattingMode != ForgeOfGodsData.DEFAULT_FORMATTING_MODE) {
            NBT.setInteger("formattingMode", formattingMode.ordinal());
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
        if (NBT.hasKey("formattingMode")) {
            int index = MathHelper.clamp_int(NBT.getInteger("formattingMode"), 0, MilestoneFormatter.VALUES.length);
            formattingMode = MilestoneFormatter.VALUES[index];
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
