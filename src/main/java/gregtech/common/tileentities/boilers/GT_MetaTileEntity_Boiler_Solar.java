package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;


public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {
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
        return new String[]{
                "Steam Power by the Sun",
                "Produces 120L of Steam per second",
                "Calcifies over time, reducing Steam output to 40L/s",
                "Break and replace to decalcify"};
    }


    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[4][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM, Dyes.getModulation(i, Dyes._NULL.mRGBa))};
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.BOILER_SOLAR)};
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa))};
            rTextures[2][(i + 1)] = tmp2;
            ITexture[] tmp3 = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE, Dyes.getModulation(i, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE)};
            rTextures[3][(i + 1)] = tmp3;
        }
        return rTextures;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return mTextures[aSide >= 2 ? ((byte) (aSide != aFacing ? 2 : 3)) : aSide][aColorIndex + 1];
    }

    public int maxProgresstime() {
        return 500;
    }

    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity, 16000);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "SolarBoiler.png", 16000);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }
    
    private int mRunTime = 0;
    
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
    public String[] getInfoData() {
        return new String[]{
                "Heat Capacity: " + EnumChatFormatting.GREEN +  GT_Utility.formatNumbers(this.mTemperature * 100 / maxProgresstime()) + " % " + EnumChatFormatting.RESET
                + "    Hot time: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.mRunTime*25/20)+EnumChatFormatting.RESET+" s",
                "Min output: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.basicMaxOuput*20/25)+EnumChatFormatting.RESET+ " L/s"
                + "    Max output: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.basicOutput*20/25)+EnumChatFormatting.RESET+" L/s",
                "Current Output: " + EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getProductionPerSecond()*20/25) +EnumChatFormatting.RESET+" L/s"};
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    protected int basicOutput = 150;
    protected int basicMaxOuput = 50;
    protected int basicTemperatureMod = 5;
    protected int basicLossTimerLimit = 45;

    // Calcification start time is 43200*25/20=54,000s or 15 hours of game time.
    static final int CALCIFICATION_TIME = 43200;

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
        if (this.mTemperature < 100 ) {
            return 0;
        }
        if (this.mRunTime > CALCIFICATION_TIME) {
            // Calcification takes about 2/3 CALCIFICATION_TIME to completely calcify on basic solar. For HP solar, it takes about 2x CALCIFICATION_TIME
            return Math.max(this.basicMaxOuput, this.basicOutput - ((this.mRunTime - CALCIFICATION_TIME) / (CALCIFICATION_TIME/150))); // Every 288*25 ticks, or 6 minutes, lose 1 L output.
        } else {
            return this.basicOutput;
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
        if ((aTick % 256L == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering())) {
            boolean bRain = aBaseMetaTileEntity.getWorld().isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F;
            mProcessingEnergy += bRain && aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4 || !aBaseMetaTileEntity.getSkyAtSide((byte) 1) ? 0 : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 * basicTemperatureMod : basicTemperatureMod;
        }
    }

    /** for waila */
    public int getBasicOutput() {
        return basicOutput;
    }

    /** for waila */
    public int getCalcificationOutput() {
        return getProductionPerSecond();
    }
}
