package gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalTankBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_DigitalTank extends GT_Container_BasicTank {
    public boolean outputFluid = false,
            mLockFluid = false,
            mVoidFluidPart = false,
            mVoidFluidFull = false,
            mAllowInputFromOutputSide = false;

    public GT_Container_DigitalTank(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 0, 81, 17));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 1, 81, 44));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 59, 42));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 3, 8, 64, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 4, 26, 64, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 5, 44, 64, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 6, 62, 64, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 7, 80, 64, false, true, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {

        GT_MetaTileEntity_DigitalTankBase mte = ((GT_MetaTileEntity_DigitalTankBase) mTileEntity.getMetaTileEntity());

        if (aSlotIndex == 3) {
            mte.mOutputFluid = !mte.mOutputFluid;
            if (!mte.mOutputFluid) {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("262", "Fluid Auto Output Disabled"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("263", "Fluid Auto Output Enabled"));
            }
            return null;
        }
        if (aSlotIndex == 4) {
            String inBrackets;
            mte.mLockFluid = !mte.mLockFluid;
            if (mte.mLockFluid) {
                if (mte.mFluid == null) {
                    mte.lockedFluidName = null;
                    inBrackets = GT_Utility.trans("264", "currently none, will be locked to the next that is put in");
                } else {
                    mte.lockedFluidName = mte.getDrainableStack().getUnlocalizedName();
                    inBrackets = mte.getDrainableStack().getLocalizedName();
                }
                GT_Utility.sendChatToPlayer(
                        aPlayer, String.format("%s (%s)", GT_Utility.trans("265", "1 specific Fluid"), inBrackets));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("266", "Lock Fluid Mode Disabled"));
            }
            return null;
        }
        if (aSlotIndex == 5) {
            mte.mVoidFluidPart = !mte.mVoidFluidPart;
            if (!mte.mVoidFluidPart) {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("267", "Overflow Voiding Mode Disabled"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("268", "Overflow Voiding Mode Enabled"));
            }
            return null;
        }
        if (aSlotIndex == 6) {
            mte.mVoidFluidFull = !mte.mVoidFluidFull;
            if (!mte.mVoidFluidFull) {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("269", "Void Full Mode Disabled"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("270", "Void Full Mode Enabled"));
                return null;
            }
        }
        if (aSlotIndex == 7) {
            mte.mAllowInputFromOutputSide = !mte.mAllowInputFromOutputSide;
            if (!mte.mAllowInputFromOutputSide) {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.getTrans("096"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.getTrans("095"));
            }
            return null;
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void sendProgressBar() {
        GT_MetaTileEntity_DigitalTankBase mte = ((GT_MetaTileEntity_DigitalTankBase) mTileEntity.getMetaTileEntity());

        for (Object crafter : this.crafters) {
            ICrafting player = (ICrafting) crafter;
            if (mContent != oContent) {
                player.sendProgressBarUpdate(this, 100, mContent & 65535);
                player.sendProgressBarUpdate(this, 101, mContent >>> 16);
            }
            if (outputFluid != mte.mOutputFluid) {
                player.sendProgressBarUpdate(this, 103, mte.mOutputFluid ? 1 : 0);
            }
            if (mLockFluid != mte.mLockFluid) {
                player.sendProgressBarUpdate(this, 104, mte.mLockFluid ? 1 : 0);
            }
            if (mVoidFluidPart != mte.mVoidFluidPart) {
                player.sendProgressBarUpdate(this, 105, mte.mVoidFluidPart ? 1 : 0);
            }
            if (mVoidFluidFull != mte.mVoidFluidFull) {
                player.sendProgressBarUpdate(this, 106, mte.mVoidFluidFull ? 1 : 0);
            }
            if (mAllowInputFromOutputSide != mte.mAllowInputFromOutputSide) {
                player.sendProgressBarUpdate(this, 107, mte.mAllowInputFromOutputSide ? 1 : 0);
            }
        }

        outputFluid = mte.mOutputFluid;
        mLockFluid = mte.mLockFluid;
        mVoidFluidPart = mte.mVoidFluidPart;
        mVoidFluidFull = mte.mVoidFluidFull;
        mAllowInputFromOutputSide = mte.mAllowInputFromOutputSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        switch (id) {
            case 103:
                outputFluid = (value != 0);
                break;
            case 104:
                mLockFluid = (value != 0);
                break;
            case 105:
                mVoidFluidPart = (value != 0);
                break;
            case 106:
                mVoidFluidFull = (value != 0);
                break;
            case 107:
                mAllowInputFromOutputSide = (value != 0);
                break;
        }
    }
}
