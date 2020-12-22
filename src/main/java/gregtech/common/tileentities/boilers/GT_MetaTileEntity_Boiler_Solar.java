package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;


public class GT_MetaTileEntity_Boiler_Solar
        extends GT_MetaTileEntity_Boiler {
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
        for (int i = -1; i < 16; i++) {
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
                "Current Output: " + EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getCalcificationOutput()*20/25) +EnumChatFormatting.RESET+" L/s"};
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
    
    public int getCalcificationOutput() { // Returns how much output the boiler can do.
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

    public int getBasicOutput() {
        return this.basicOutput;
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
            if (this.mTemperature <= 20) {
                this.mTemperature = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > basicLossTimerLimit) {
                this.mTemperature -= basicTemperatureMod;
                this.mLossTimer = 0;
            }
            if (this.mSteam != null) {
                byte i = aBaseMetaTileEntity.getFrontFacing();
                IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
                if (tTileEntity != null) {
                    FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), Math.max(1, this.mSteam.amount / 2), false);
                    if (tDrained != null) {
                        int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), tDrained, false);
                        if (tFilledAmount > 0) {
                            tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
                        }
                    }
                }
            }
            if (aTick % 25L == 0L) { // Every 25 ticks since 1L of water = 150L of steam. So for 120L, have to use 25 instead of 20.
                if (this.mTemperature > 100) {
                    if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                        this.mHadNoWater = true;
                    } else {
                        if (this.mHadNoWater) {
                            GT_Log.exp.println("Boiler "+this.mName+" had no Water!");
                            aBaseMetaTileEntity.doExplosion(2048L);
                            return;
                        }
                        this.mFluid.amount -= (basicOutput/150);
                        mRunTime += 1;

                        int tOutput = getCalcificationOutput();

                        if (this.mSteam == null) {
                            this.mSteam = GT_ModHandler.getSteam(tOutput);
                        } else if (GT_ModHandler.isSteam(this.mSteam)) {
                            this.mSteam.amount += tOutput;
                        } else {
                            this.mSteam = GT_ModHandler.getSteam(tOutput);
                        }
                    }
                } else {
                    this.mHadNoWater = false;
                }
            }
            if ((this.mSteam != null) &&
                    (this.mSteam.amount > this.getCapacity())) {
                sendSound((byte) 1);
                this.mSteam.amount = 3*(this.getCapacity()/4);
            }
            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 256L == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering())) {
                boolean bRain = aBaseMetaTileEntity.getWorld().isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F;
                mProcessingEnergy += bRain && aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4 || !aBaseMetaTileEntity.getSkyAtSide((byte) 1) ? 0 : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8*basicTemperatureMod : basicTemperatureMod;
            }
            if ((this.mTemperature < maxProgresstime()) && (this.mProcessingEnergy > 0) && (aTick % 12L == 0L)) {
                this.mProcessingEnergy -= basicTemperatureMod;
                this.mTemperature += basicTemperatureMod;
            }
            aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
        }
    }
}
