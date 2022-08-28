package gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_DigitalTank extends GT_Container_BasicTank {
    public boolean OutputFluid = false, mMode = false, mVoidFluidPart = false, mVoidFluidFull = false;

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
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if(aSlotIndex == 3){
            GT_Utility.sendChatToPlayer(aPlayer, "Fluid Output Disabled");
            return null;
        }
        if(aSlotIndex == 4){
            GT_Utility.sendChatToPlayer(aPlayer, "currently none, will be locked to the next that is put in");
            return null;
        }
        if(aSlotIndex == 5){
            GT_Utility.sendChatToPlayer(aPlayer, "Void Part Mode Disabled");
            return null;
        }
        if(aSlotIndex == 6){
            GT_Utility.sendChatToPlayer(aPlayer, "Void Full Mode Disabled");
            return null;
        }

        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void sendProgressBar() {
//        OutputFluid = mte.OutputFluid;
//        mMode = mte.mMode;
//        mVoidFluidPart = mte.mVoidFluidPart;
//        mVoidFluidFull = mte.mVoidFluidFull;

        for (Object crafter : this.crafters) {
            ICrafting player = (ICrafting) crafter;
            if (mTimer % 500 == 0 || oContent != mContent) {
                player.sendProgressBarUpdate(this, 100, mContent & 65535);
                player.sendProgressBarUpdate(this, 101, mContent >>> 16);
                player.sendProgressBarUpdate(this, 103, OutputFluid ? 1 : 0);
                player.sendProgressBarUpdate(this, 104, mMode ? 1 : 0);
                player.sendProgressBarUpdate(this, 105, mVoidFluidPart ? 1 : 0);
                player.sendProgressBarUpdate(this, 106, mVoidFluidFull ? 1 : 0);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        switch (id) {
            case 103:
                OutputFluid = (value != 0);
                break;
            case 104:
                mMode = (value != 0);
                break;
            case 105:
                mVoidFluidPart = (value != 0);
                break;
            case 106:
                mVoidFluidFull = (value != 0);
                break;
        }
    }
}
