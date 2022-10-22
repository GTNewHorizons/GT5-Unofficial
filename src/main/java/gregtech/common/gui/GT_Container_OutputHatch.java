package gregtech.common.gui;

import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.gui.GT_Slot_Render;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_Container_OutputHatch extends GT_Container_BasicTank {

    public GT_Container_OutputHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 0, 80, 17));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 1, 80, 53));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 59, 42));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 3, 150, 42));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex == 3 && aMouseclick < 2) {
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) mTileEntity.getMetaTileEntity();
            FluidStack tReadyLockFluid = GT_Utility.getFluidForFilledItem(aPlayer.inventory.getItemStack(), true);
            byte tMode = tHatch.getMode();
            // If player click the locker slot with empty or the same fluid cell, clear the lock fluid
            if (tReadyLockFluid == null || (tMode >= 8 && tReadyLockFluid.getFluid().getName().equals(tHatch.getLockedFluidName()))) {
                tHatch.setLockedFluidName(null);
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("300", "Fluid Lock Cleared."));
                tHatch.mMode = 0;
            }
            else {
                tHatch.setLockedFluidName(tReadyLockFluid.getFluid().getName());
                GT_Utility.sendChatToPlayer(aPlayer, String.format(GT_Utility.trans("151.4", "Sucessfully locked Fluid to %s"), tReadyLockFluid.getLocalizedName()));
                tHatch.mMode = 9;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
}