package gregtech.common.tileentities.storage;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK_GLOW;

public abstract class GT_MetaTileEntity_DigitalTankBase extends GT_MetaTileEntity_BasicTank {
    protected boolean mVoidOverflow = false;
    private boolean voidBreak;


    public GT_MetaTileEntity_DigitalTankBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, new String[]{
            "Stores " + GT_Utility.formatNumbers(commonSizeCompute(aTier)) + "L of fluid",
            "Use a screwdriver to enable",
            "voiding fluid on overflow",
            "Can keep its contents when harvested",
            "Sneak when harvesting to void its contents"
        });
    }

    protected static int commonSizeCompute(int tier) {
        switch (tier) {
            case 1:
                return 4000000;
            case 2:
                return 8000000;
            case 3:
                return 16000000;
            case 4:
                return 32000000;
            case 5:
                return 64000000;
            case 6:
                return 128000000;
            case 7:
                return 256000000;
            case 8:
                return 512000000;
            case 9:
                return 1024000000;
            case 10:
                return 2147483640;
            default:
                return 0;
        }
    }


    public GT_MetaTileEntity_DigitalTankBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_DigitalTankBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (!voidBreak) {
            if (mFluid != null && mFluid.amount > 0)
                aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
        }
        super.setItemNBT(aNBT);
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
    }


    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");
    }


    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide != ForgeDirection.UP.ordinal()) return new ITexture[]{MACHINE_CASINGS[mTier][aColorIndex + 1]};
        return new ITexture[]{
            MACHINE_CASINGS[mTier][aColorIndex + 1],
            TextureFactory.of(OVERLAY_QTANK),
            TextureFactory.builder().addIcon(OVERLAY_QTANK_GLOW).glow().build()
        };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aBaseMetaTileEntity.isClientSide()) aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        voidBreak = aPlayer.isSneaking();
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mVoidOverflow = !mVoidOverflow;
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal(mVoidOverflow ? "GT5U.machines.voidoveflow.enabled" : "GT5U.machines.voidoveflow.disabled"));
    }


    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0)
                setFillableStack(null);

            if (mOpenerCount > 0)
                updateFluidDisplayItem();

            if (doesEmptyContainers()) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true);
                if (tFluid != null && isFluidInputAllowed(tFluid)) {
                    if (getFillableStack() == null) {
                        if (isFluidInputAllowed(tFluid)) {
                            if ((tFluid.amount <= getRealCapacity()) || mVoidOverflow) {
                                tFluid = tFluid.copy();
                                tFluid.amount = Math.min(tFluid.amount, getRealCapacity());
                                if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true), 1)) {
                                    setFillableStack(tFluid);
                                    this.onEmptyingContainerWhenEmpty();
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    } else {
                        if (tFluid.isFluidEqual(getFillableStack())) {
                            if ((((long) tFluid.amount + getFillableStack().amount) <= (long) getRealCapacity()) || mVoidOverflow) {
                                if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true), 1)) {
                                    getFillableStack().amount += Math.min(tFluid.amount, getRealCapacity() - getFillableStack().amount);
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    }
                }
            }

            if (doesFillContainers()) {
                ItemStack tOutput = GT_Utility.fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()], false, true);
                if (tOutput != null && aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tOutput, 1)) {
                    FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput, true);
                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                    if (tFluid != null) getDrainableStack().amount -= tFluid.amount;
                    if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) setDrainableStack(null);
                }
            }
        }
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid().getID() <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid))
            return 0;
        if (getFillableStack() != null && !getFillableStack().isFluidEqual(aFluid)) {
            return 0;
        }

        FluidStack fillableStack = getFillableStack();
        if (fillableStack == null) {
            fillableStack = aFluid.copy();
            fillableStack.amount = 0;
        }

        int amount = Math.min(aFluid.amount, getRealCapacity() - fillableStack.amount);
        if (doFill) {
            fillableStack.amount += amount;
            if (getFillableStack() == null) setFillableStack(fillableStack);
            getBaseMetaTileEntity().markDirty();
        }
        return mVoidOverflow ? aFluid.amount : amount;
    }

    @Override
    public final byte getUpdateData() {
        return 0x00;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

    @Override
    public int getCapacity() {
        return mVoidOverflow ? Integer.MAX_VALUE : getRealCapacity();
    }

    private int getRealCapacity(){
        return commonSizeCompute(mTier);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(getFluid(), getRealCapacity());
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        return new FluidTankInfo[]{getInfo()};
    }
}
