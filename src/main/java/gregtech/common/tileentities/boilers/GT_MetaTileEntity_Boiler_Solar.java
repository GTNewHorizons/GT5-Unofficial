package gregtech.common.tileentities.boilers;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.enums.ConfigCategories.machineconfig;

@SuppressWarnings("unused")
public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {
    public static final String DEFAULT_STR = "Default: ";
    public static final String LPS_FMT = "%s L/s";
    private static final String localizedDescFormat = GT_LanguageManager.addStringLocalization(
            "gt.blockmachines.boiler.solar.desc.format",
            "Steam Power by the Sun%n" +
                    "Produces %sL of Steam per second%n" +
                    "Calcifies over time, reducing Steam output to %sL/s%n" +
                    "Break and replace to descale");
    protected int calcificationTicks;
    protected int minOutputPerSecond;
    protected int maxOutputPerSecond;
    protected int basicTemperatureMod = 5; // Base Celsius gain or loss
    protected int coolDownTicks;
    private int mRunTimeTicks = 0;

    public GT_MetaTileEntity_Boiler_Solar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[0]);
        onConfigLoad();
    }

    protected void onConfigLoad() {
        final Configuration config = GregTech_API.sMachineFile.mConfig;
        final String configCategory = machineconfig + ".boiler.solar.bronze";

        final int defaultCalcificationTicks = 1080000; // 15 hours
        final int defaultMinOutputPerSecond = 40;
        final int defaultMaxOutputPerSecond = 120;
        final int defaultCoolDownTicks = 45;

        calcificationTicks = config.get(configCategory, "CalcificationTicks", defaultCalcificationTicks,
                "Number of run-time ticks before boiler starts calcification.\n" +
                        "100% calcification and minimal output will be reached at 2 times this.\n" +
                        DEFAULT_STR + defaultCalcificationTicks).getInt();
        minOutputPerSecond = config.get(configCategory, "MinOutputPerSecond", defaultMinOutputPerSecond,
                DEFAULT_STR + defaultMinOutputPerSecond).getInt();
        maxOutputPerSecond = config.get(configCategory, "MaxOutputPerSecond", defaultMaxOutputPerSecond,
                DEFAULT_STR + defaultMaxOutputPerSecond).getInt();
        coolDownTicks = config.get(configCategory, "CoolDownTicks", defaultCoolDownTicks,
                "Number of ticks it takes to lose 1°C.\n" +
                        DEFAULT_STR + defaultCoolDownTicks).getInt();
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    /**
     * for WAILA
     */
    public int getBasicOutput() {
        return getMaxOutputPerSecond();
    }

    public int getMaxOutputPerSecond() {
        return maxOutputPerSecond;
    }

    /**
     * for WAILA
     */
    public int getCalcificationOutput() {
        return getMinOutputPerSecond();
    }

    public int getMinOutputPerSecond() {
        return minOutputPerSecond;
    }

    @Override
    public String[] getDescription() {
        return String.format(localizedDescFormat,
                GT_Utility.formatNumbers(getMaxOutputPerSecond()),
                GT_Utility.formatNumbers(getMinOutputPerSecond()))
                .split("\\R");
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[4][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color, Dyes._NULL.mRGBa);
            rTextures[0][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM, colorModulation)};
            rTextures[1][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_BRONZEBRICKS_TOP, colorModulation),
                    new GT_RenderedTexture(BlockIcons.BOILER_SOLAR)};
            rTextures[2][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation)};
            rTextures[3][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_BRONZEBRICKS_SIDE, colorModulation),
                    new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE)};
        }
        return rTextures;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity, getCapacity());
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "SolarBoiler.png", getCapacity());
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        int i = aColorIndex + 1;
        if (aSide >= 2) {
            if (aSide != aFacing) return mTextures[2][i];
            return mTextures[3][i];
        }
        return mTextures[aSide][i];
    }

    @Override
    public int maxProgresstime() {
        return 500;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mRunTime", mRunTimeTicks);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mRunTimeTicks = aNBT.getInteger("mRunTime");
    }

    @Override
    protected void produceSteam(int aAmount) {
        super.produceSteam(aAmount);
        // produceSteam is getting called every 10 ticks
        if (mRunTimeTicks > 0 && mRunTimeTicks < (Integer.MAX_VALUE - 10)) mRunTimeTicks += 10;
        else mRunTimeTicks = Integer.MAX_VALUE; // Prevent Integer overflow wrap
    }

    @Override
    protected void pushSteamToInventories(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mSteam == null || mSteam.amount == 0) return;
        pushSteamToSide(aBaseMetaTileEntity, aBaseMetaTileEntity.getFrontFacing());
    }

    @Override
    protected int getPollution() {
        return 0;
    }

    @Override
    protected int getProductionPerSecond() {
        if (mTemperature < 100) {
            return 0;
        }
        if (mRunTimeTicks > calcificationTicks) {
            /* When reaching calcification ticks; discount the proportion of run-time spent on calcification
             *  from the maximum output per second, and return this or the minimum output per second
             */
            return Math.max(minOutputPerSecond,
                    maxOutputPerSecond - (mRunTimeTicks - calcificationTicks) / calcificationTicks * maxOutputPerSecond);
        } else {
            return maxOutputPerSecond;
        }
    }

    @Override
    protected int getMaxTemperature() {
        return 500;
    }

    @Override
    protected int getEnergyConsumption() {
        return basicTemperatureMod;
    }

    @Override
    protected int getCooldownInterval() {
        return coolDownTicks / basicTemperatureMod;
    }

    @Override
    protected int getHeatUpAmount() {
        return basicTemperatureMod;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        World world = aBaseMetaTileEntity.getWorld();
        // Heat-up every 12s (240 ticks), has to be multiple of 20 ticks
        if ((aTick % 240L != 0L) || (world.isThundering())) {
            return;
        }
        if (!aBaseMetaTileEntity.getSkyAtSide((byte) ForgeDirection.UP.ordinal())) {
            return;
        }
        boolean weatherClear = !world.isRaining() || aBaseMetaTileEntity.getBiome().rainfall == 0.0F;
        if (!weatherClear && world.skylightSubtracted >= 4) {
            return;
        }
        if (weatherClear) {
            if (world.isDaytime()) {
                mProcessingEnergy += 8 * basicTemperatureMod;
            } else {
                mProcessingEnergy += basicTemperatureMod;
            }
        } else {
            mProcessingEnergy += basicTemperatureMod;
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return String.format("Heat Capacity: " + EnumChatFormatting.GREEN + "%s %%" + EnumChatFormatting.RESET +
                        "    Hot time: " + EnumChatFormatting.RED + "%s s" + EnumChatFormatting.RESET + "%n" +
                        "Min output: " + EnumChatFormatting.RED + LPS_FMT + EnumChatFormatting.RESET +
                        "    Max output: " + EnumChatFormatting.RED + LPS_FMT + EnumChatFormatting.RESET + "%n" +
                        "Current Output: " + EnumChatFormatting.YELLOW + LPS_FMT + EnumChatFormatting.RESET,
                GT_Utility.formatNumbers(getHeatCapacityPercent()),
                GT_Utility.formatNumbers(getHotTimeSeconds()),
                GT_Utility.formatNumbers(getMinOutputPerSecond()),
                GT_Utility.formatNumbers(getMaxOutputPerSecond()),
                GT_Utility.formatNumbers(getProductionPerSecond()))
                .split("\\R");
    }

    public int getHeatCapacityPercent() {
        return mTemperature * 100 / maxProgresstime();
    }

    public int getHotTimeSeconds() {
        return mRunTimeTicks / 20;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar(mName, mTier, mDescriptionArray, mTextures);
    }
}
