package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_WaterGenerator extends GT_MetaTileEntity_BasicTank {
    public GT_MetaTileEntity_WaterGenerator(int aID, String aName, String aNameRegional, int aTier, Object[] aRecipe) {
        super(aID, aName, aNameRegional, aTier, 3, "Condense " + 100 * (1 << aTier - 1) * (1 << aTier - 1) + "L per tick of water from Air.", new ITexture[0]);
    }

    public GT_MetaTileEntity_WaterGenerator(String mName, byte mTier, String aDescription, ITexture[][][] mTextures) {
        super(mName, mTier, 3, aDescription, mTextures);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_WaterGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == 1 ? new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], new GT_RenderedTexture(BlockIcons.OVERLAY_QTANK)} : new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1]};
    }
    
    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.VENT_ADVANCED)};
    }


    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.getBaseMetaTileEntity().isServerSide()) {
            if (this.getBaseMetaTileEntity().isAllowedToWork()) {
                if (this.getFluidAmount() + this.generateWaterAmount() <= this.getCapacity() && this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(GT_Values.V[this.mTier], false)) {
                    if (this.mFluid != null && this.mFluid.getFluidID() != Materials.Water.getFluid(1L).getFluidID()) {
                        this.mFluid = null;
                    }

                    this.fill(Materials.Water.getFluid((long)this.generateWaterAmount()), true);
                }

                this.getBaseMetaTileEntity().setActive(true);
            } else {
                this.getBaseMetaTileEntity().setActive(false);
            }

            for(byte tSide = 0; tSide < 6; ++tSide) {
                IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(tSide);
                if (tTileEntity != null) {
                    if (tTileEntity instanceof IGregTechTileEntity && aBaseMetaTileEntity.getColorization() >= 0) {
                        byte tColor = ((IGregTechTileEntity)tTileEntity).getColorization();
                        if (tColor >= 0 && (tColor & 15) != (aBaseMetaTileEntity.getColorization() & 15)) {
                            continue;
                        }
                    }

                    FluidTankInfo[] inf = tTileEntity.getTankInfo(ForgeDirection.getOrientation(tSide).getOpposite());
                    FluidTankInfo[] var7 = inf;
                    int var8 = inf.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        FluidTankInfo info = var7[var9];
                        if (info != null && (info.fluid == null || info.fluid.getFluidID() < 0 || info.fluid.getFluidID() == Materials.Water.getFluid(1L).getFluidID())) {
                            int amount = info.capacity;
                            if (info.fluid != null && info.fluid.amount != info.capacity) {
                                amount = info.capacity - info.fluid.amount;
                            }

                            if (this.getFluidAmount() >= amount && tTileEntity.fill(ForgeDirection.getOrientation(tSide).getOpposite(), this.drain(amount, false), false) > 0) {
                                tTileEntity.fill(ForgeDirection.getOrientation(tSide).getOpposite(), this.drain(amount, true), true);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    public boolean isLiquidOutput(byte aSide) {
        return true;
    }

    private int generateWaterAmount() {
        return 100 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
    }

    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            aBaseMetaTileEntity.openGUI(aPlayer);
            return true;
        }
    }

    public boolean isSimpleMachine() {
        return true;
    }

    public boolean isElectric() {
        return true;
    }

    public boolean isEnetInput() {
        return true;
    }

    public long getMinimumStoredEU() {
        return GT_Values.V[this.mTier] * 16L;
    }

    public long maxEUStore() {
        return GT_Values.V[this.mTier] * 64L;
    }
	
	public long maxEUInput() {
    	return GT_Values.V[this.mTier];
    }

    public long maxSteamStore() {
        return this.maxEUStore();
    }

    public long maxAmperesIn() {
        return 1L;
    }

    public int getStackDisplaySlot() {
        return 2;
    }

    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    public boolean isInputFacing(byte aSide) {
        return true;
    }

    public boolean isOutputFacing(byte aSide) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    public int getCapacity() {
        return 100 * (1 << this.mTier - 1) * (1 << this.mTier - 1) * 20;
    }

    public int getTankPressure() {
        return 2147483647;
    }

    public boolean isFluidChangingAllowed() {
        return false;
    }

    public boolean doesFillContainers() {
        return true;
    }

    public boolean doesEmptyContainers() {
        return true;
    }

    public boolean canTankBeFilled() {
        return true;
    }

    public boolean canTankBeEmptied() {
        return true;
    }

    public boolean displaysItemStack() {
        return true;
    }

    public boolean displaysStackSize() {
        return false;
    }

    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }
}
