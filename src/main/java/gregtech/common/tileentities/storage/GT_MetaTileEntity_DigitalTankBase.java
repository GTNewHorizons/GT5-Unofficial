package gregtech.common.tileentities.storage;

import gregtech.api.gui.GT_Container_DigitalTank;
import gregtech.api.gui.GT_GUIContainer_DigitalTank;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import static gregtech.api.enums.Textures.BlockIcons.*;

public abstract class GT_MetaTileEntity_DigitalTankBase extends GT_MetaTileEntity_BasicTank {
    public boolean mOutputFluid = false, mVoidFluidPart = false, mVoidFluidFull = false;
    public String lockedFluidName = null;
    public boolean mLockFluid = false;
    private boolean voidBreak;

    public GT_MetaTileEntity_DigitalTankBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, new String[] {
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
            if (mFluid != null && mFluid.amount > 0) {
                aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
            }
            aNBT.setBoolean("mOutputFluid", this.mOutputFluid);
            aNBT.setBoolean("mVoidOverflow", this.mVoidFluidPart);
            aNBT.setBoolean("mVoidFluidFull", this.mVoidFluidFull);
            aNBT.setBoolean("mLockFluid", mLockFluid);
            if (lockedFluidName != null && lockedFluidName.length() != 0) aNBT.setString("lockedFluidName", lockedFluidName);
            else aNBT.removeTag("lockedFluidName");
        }
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mOutputFluid", this.mOutputFluid);
        aNBT.setBoolean("mVoidOverflow", this.mVoidFluidPart);
        aNBT.setBoolean("mVoidFluidFull", this.mVoidFluidFull);
        aNBT.setBoolean("mLockFluid", mLockFluid);
        if (lockedFluidName != null && lockedFluidName.length() != 0) aNBT.setString("lockedFluidName", lockedFluidName);
        else aNBT.removeTag("lockedFluidName");
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOutputFluid    = aNBT.getBoolean("mOutputFluid");
        mVoidFluidPart = aNBT.getBoolean("mVoidOverflow");
        mVoidFluidFull = aNBT.getBoolean("mVoidFluidFull");
        mLockFluid               = aNBT.getBoolean("mLockFluid");
        lockedFluidName     = aNBT.getString("lockedFluidName");
        lockedFluidName     = lockedFluidName.length() == 0 ? null : lockedFluidName;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return !mLockFluid || lockedFluidName == null || lockedFluidName.equals(aFluid.getUnlocalizedName());
    }

    @Override
    public boolean isFluidChangingAllowed() {
        return !mLockFluid || lockedFluidName == null;
    }

    @Override
    public void onEmptyingContainerWhenEmpty() {
        if (this.lockedFluidName == null && this.mFluid != null) {
            this.lockedFluidName = this.mFluid.getUnlocalizedName();
        }
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
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide != ForgeDirection.UP.ordinal()) return new ITexture[] {MACHINE_CASINGS[mTier][aColorIndex + 1]};
        return new ITexture[] {
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
        mVoidFluidPart = !mVoidFluidPart;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal(
                        mVoidFluidPart ? "GT5U.machines.voidoveflow.enabled" : "GT5U.machines.voidoveflow.disabled"));
    }

    @Override
    public FluidStack setFillableStack(FluidStack aFluid) {
        mFluid = aFluid;
        if (mFluid != null) {
            mFluid.amount = Math.min(mFluid.amount, getRealCapacity());
        }
        return mFluid;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack aFluid) {
        mFluid = aFluid;
        if (mFluid != null) {
            mFluid.amount = Math.min(mFluid.amount, getRealCapacity());
        }
        return mFluid;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DigitalTank(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DigitalTank(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0)
                setFillableStack(null);

            if (mVoidFluidFull && getFillableStack() != null) {
                mVoidFluidPart = false;
                mLockFluid = false;
                setFillableStack(null);
            }

            if (mOpenerCount > 0) updateFluidDisplayItem();

            if (doesEmptyContainers()) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true);
                if (tFluid != null && isFluidInputAllowed(tFluid)) {
                    if (getFillableStack() == null) {
                        if (isFluidInputAllowed(tFluid)) {
                            if ((tFluid.amount <= getRealCapacity()) || mVoidFluidPart) {
                                tFluid = tFluid.copy();
                                if (aBaseMetaTileEntity.addStackToSlot(
                                        getOutputSlot(),
                                        GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                        1)) {
                                    setFillableStack(tFluid);
                                    this.onEmptyingContainerWhenEmpty();
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    } else {
                        if (tFluid.isFluidEqual(getFillableStack())) {
                            if ((((long) tFluid.amount + getFillableStack().amount) <= (long) getRealCapacity())
                                    || mVoidFluidPart || mVoidFluidFull) {
                                if (aBaseMetaTileEntity.addStackToSlot(
                                        getOutputSlot(),
                                        GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                        1)) {
                                    getFillableStack().amount +=
                                            Math.min(tFluid.amount, getRealCapacity() - getFillableStack().amount);
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    }
                }
            }

            if (doesFillContainers()) {
                ItemStack tOutput =
                        GT_Utility.fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()], false, true);
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
        if (aFluid == null
                || aFluid.getFluid().getID() <= 0
                || aFluid.amount <= 0
                || !canTankBeFilled()
                || !isFluidInputAllowed(aFluid)) return 0;
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
        return (mVoidFluidPart || mVoidFluidFull) ? aFluid.amount : amount;
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
        return (mVoidFluidPart || mVoidFluidFull) ? Integer.MAX_VALUE : getRealCapacity();
    }

    public int getRealCapacity() {
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
        return new FluidTankInfo[] {getInfo()};
    }
}
