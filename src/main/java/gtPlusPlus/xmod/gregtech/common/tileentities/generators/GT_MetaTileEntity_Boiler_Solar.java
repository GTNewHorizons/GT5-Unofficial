package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_Boiler_Solar extends GT_MetaTileEntity_Boiler {

    public GT_MetaTileEntity_Boiler_Solar(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, "Steam Power by the Sun");
    }

    public GT_MetaTileEntity_Boiler_Solar(final String aName, final int aTier, final String aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, "Produces " + (this.getPollution() * 20) + " pollution/sec",
                CORE.GT_Tooltip.get() };
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[4][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            final ITexture[] tmp0 = { new GT_RenderedTexture(
                    Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM,
                    Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
            rTextures[0][(i + 1)] = tmp0;
            final ITexture[] tmp1 = {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP,
                            Dyes.getModulation(i, Dyes._NULL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.BOILER_SOLAR) };
            rTextures[1][(i + 1)] = tmp1;
            final ITexture[] tmp2 = { new GT_RenderedTexture(
                    Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE,
                    Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
            rTextures[2][(i + 1)] = tmp2;
            final ITexture[] tmp3 = {
                    new GT_RenderedTexture(
                            Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE,
                            Dyes.getModulation(i, Dyes._NULL.mRGBa)),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) };
            rTextures[3][(i + 1)] = tmp3;
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[side.offsetY == 0 ? ((byte) (side != facing ? 2 : 3)) : side.ordinal()][aColorIndex + 1];
    }

    @Override
    public int maxProgresstime() {
        return 500;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    private int mRunTime = 0;

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mRunTime", this.mRunTime);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mRunTime = aNBT.getInteger("mRunTime");
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
            if (this.mTemperature <= 20) {
                this.mTemperature = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > 45) {
                this.mTemperature -= 1;
                this.mLossTimer = 0;
            }
            if (this.mSteam != null) {
                final ForgeDirection side = aBaseMetaTileEntity.getFrontFacing();
                final IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(side);
                if (tTileEntity != null) {
                    final FluidStack tDrained = aBaseMetaTileEntity
                            .drain(side, Math.max(1, this.mSteam.amount / 2), false);
                    if (tDrained != null) {
                        final int tFilledAmount = tTileEntity.fill(side.getOpposite(), tDrained, false);
                        if (tFilledAmount > 0) {
                            tTileEntity.fill(
                                    side.getOpposite(),
                                    aBaseMetaTileEntity.drain(side, tFilledAmount, true),
                                    true);
                        }
                    }
                }
            }
            if ((aTick % 25L) == 0L) {
                if (this.mTemperature > 100) {
                    if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                        this.mHadNoWater = true;
                    } else {
                        if (this.mHadNoWater) {
                            aBaseMetaTileEntity.doExplosion(2048L);
                            return;
                        }
                        this.mFluid.amount -= 1;
                        this.mRunTime += 1;
                        int tOutput = 150;
                        if (this.mRunTime > 10000) {
                            tOutput = Math.max(50, 150 - ((this.mRunTime - 10000) / 100));
                        }
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
            if ((this.mSteam != null) && (this.mSteam.amount > 16000)) {
                this.sendSound((byte) 1);
                this.mSteam.amount = 12000;
            }
            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork())
                    && ((aTick % 256L) == 0L)
                    && (!aBaseMetaTileEntity.getWorld().isThundering())) {
                final boolean bRain = aBaseMetaTileEntity.getWorld().isRaining()
                        && (aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
                this.mProcessingEnergy += (bRain && (aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4))
                        || !aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP) ? 0
                                : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 : 1;
            }
            if ((this.mTemperature < 500) && (this.mProcessingEnergy > 0) && ((aTick % 12L) == 0L)) {
                this.mProcessingEnergy -= 1;
                this.mTemperature += 1;
            }
            aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
        }
    }

    @Override
    protected int getPollution() {
        return 0;
    }

    @Override
    protected int getProductionPerSecond() {
        return 0;
    }

    @Override
    protected int getMaxTemperature() {
        return 0;
    }

    @Override
    protected int getEnergyConsumption() {
        return 0;
    }

    @Override
    protected int getCooldownInterval() {
        return 0;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity iGregTechTileEntity, long l) {}
}
