package gregtech.common.tileentities.boilers;

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
import net.minecraftforge.common.util.ForgeDirection;


public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {
    // Calcification start time is 43200*25/20=54000s or 15 hours of game time.
    static final int CALCIFICATION_TIME = 43200;
    private static final String localizedDescFormat = GT_LanguageManager.addStringLocalization(
            "gt.blockmachines.boiler.solar.desc.format",
            "Steam Power by the Sun%n" +
                    "Produces %sL of Steam per second%n" +
                    "Calcifies over time, reducing Steam output to %sL/s%n" +
                    "Break and replace to descale");
    protected int minOutputPer25Ticks = 50;
    protected int maxOutputPer25Ticks = 150;
    protected int basicTemperatureMod = 5;
    protected int basicLossTimerLimit = 45;
    private int mRunTime = 0;

    public GT_MetaTileEntity_Boiler_Solar(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[0]);
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Solar(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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
        aNBT.setInteger("mRunTime", this.mRunTime);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mRunTime = aNBT.getInteger("mRunTime");
    }

    @Override
    protected void produceSteam(int aAmount) {
        super.produceSteam(aAmount);
        mRunTime++;
    }

    @Override
    protected void pushSteamToInventories(IGregTechTileEntity aBaseMetaTileEntity) {
        pushSteamToSide(aBaseMetaTileEntity, aBaseMetaTileEntity.getFrontFacing());
    }

    @Override
    protected int getPollution() {
        return 0;
    }

    @Override
    protected int getProductionPerSecond() {
        if (this.mTemperature < 100) {
            return 0;
        }
        if (mRunTime > CALCIFICATION_TIME) {
            // Calcification takes about 2/3 CALCIFICATION_TIME to completely calcify on basic solar.
            // For HP solar, it takes about 2x CALCIFICATION_TIME
            return Math.max(minOutputPer25Ticks,
                    // Every 288*25 ticks, or 6 minutes, lose 1 L output.
                    maxOutputPer25Ticks - (mRunTime - CALCIFICATION_TIME) / CALCIFICATION_TIME * maxOutputPer25Ticks);
        } else {
            return maxOutputPer25Ticks;
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
        return basicLossTimerLimit / basicTemperatureMod;
    }

    @Override
    protected int getHeatUpAmount() {
        return basicTemperatureMod;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        World world = aBaseMetaTileEntity.getWorld();
        if ((aTick % 256L != 0L) || (world.isThundering())) {
            return;
        }
        if (!aBaseMetaTileEntity.getSkyAtSide((byte) ForgeDirection.UP.ordinal())) {
            return;
        }
        boolean weatherClear = !world.isRaining() || !(aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
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
                        "Min output: " + EnumChatFormatting.RED + "%s L/s" + EnumChatFormatting.RESET +
                        "    Max output: " + EnumChatFormatting.RED + "%s L/s" + EnumChatFormatting.RESET + "%n" +
                        "Current Output: " + EnumChatFormatting.YELLOW + "%s L/s" + EnumChatFormatting.RESET,
                GT_Utility.formatNumbers(getHeatCapacityPercent()),
                GT_Utility.formatNumbers(getHotTimeSeconds()),
                GT_Utility.formatNumbers(getMinOutputPerSecond()),
                GT_Utility.formatNumbers(getMaxOutputPerSecond()),
                GT_Utility.formatNumbers(getCurrentOutputPerSecond()))
                .split("\\R");
    }

    protected long getHeatCapacityPercent() {
        return mTemperature * 100L / maxProgresstime();
    }

    protected long getHotTimeSeconds() {
        return mRunTime * 25L / 20L;
    }

    protected long getMinOutputPerSecond() {
        return minOutputPer25Ticks * 20L / 25L;
    }

    protected long getMaxOutputPerSecond() {
        return maxOutputPer25Ticks * 20L / 25L;
    }

    protected long getCurrentOutputPerSecond() {
        return getProductionPerSecond() * 20L / 25L;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }
}
