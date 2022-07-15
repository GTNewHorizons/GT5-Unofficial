package gregtech.common.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.ForestryAPI;
import forestry.api.core.IErrorState;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_ApiaryUpgrade;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;
import net.bdew.gendustry.api.items.IApiaryUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;

public class GT_Container_IndustrialApiary extends GT_ContainerMetaTile_Machine {

    GT_Slot_Holo slotItemTransferToggle;
    GT_Slot_Holo slotSpeedToggle;
    GT_Slot_Holo slotPollenToggle;
    GT_Slot_Holo slotCancelProcess;
    Slot slotBattery;
    Slot slotSpecial;

    boolean mItemTransfer;
    boolean mStuttering;

    int mSpeed = 0; // scale 0 - 8
    boolean mLockedSpeed = true;
    boolean retrievePollen;

    ArrayList<String> mErrorStates = new ArrayList<>(50);

    public GT_Container_IndustrialApiary(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(slotItemTransferToggle = new GT_Slot_Holo(mTileEntity, 0, 8, 63, false, true, 1));
        addSlotToContainer(slotSpeedToggle = new GT_Slot_Holo(mTileEntity, 0, 26, 63, false, true, 1));
        addSlotToContainer(slotPollenToggle = new GT_Slot_Holo(mTileEntity, 0, 8, 45, false, true, 1));
        addSlotToContainer(slotCancelProcess = new GT_Slot_Holo(mTileEntity, 0, 8, 27, false, true, 1));
        int tStartIndex = 5;

        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 37, 22));
        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 37, 42));

        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 62, 24));
        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 80, 24));
        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 62, 42));
        addSlotToContainer(new ApiarySlot(this.mTileEntity, tStartIndex++, 80, 42));

        for(int i = 107; i <= 143; i += 18)
            for(int j = 6; j <= 42; j += 18)
                addSlotToContainer(new GT_Slot_Output(this.mTileEntity, tStartIndex++, i, j));

        addSlotToContainer(slotBattery = new Slot(mTileEntity, 1, 80, 63));
        addSlotToContainer(slotSpecial = new Slot(mTileEntity, 3, 125, 63));

    }

    @Override
    public ItemStack slotClick(int aSlotNumber, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (mTileEntity.getMetaTileEntity() == null) return null;
        GT_MetaTileEntity_IndustrialApiary machine = getMachine();
        if (machine == null) return null;
        switch (aSlotNumber) {
            case 0:
                machine.mItemTransfer = !machine.mItemTransfer;
                return null;
            case 1:
                if(aMouseclick == 0) {
                    if(machine.mLockedSpeed)
                        return null;
                    if(aShifthold == 0) {
                        machine.mSpeed++;
                        if (machine.mSpeed > machine.getMaxSpeed()) machine.mSpeed = 0;
                    }
                    else if(aShifthold == 1)
                    {
                        machine.mSpeed--;
                        if (machine.mSpeed < 0) machine.mSpeed = machine.getMaxSpeed();
                    }
                }
                else if(aMouseclick == 1)
                {
                    machine.mLockedSpeed = !machine.mLockedSpeed;
                    if(machine.mLockedSpeed)
                        machine.mSpeed = machine.getMaxSpeed();
                }
                return null;
            case 2:
                machine.retreviePollen = !machine.retreviePollen;
                return null;
            case 3:
                machine.cancelProcess();
                detectAndSendChanges();
                return null;
        }

        if(!(aSlotNumber >= getSlotStartIndex()+2 && aSlotNumber < getSlotStartIndex()+2+4))
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        if(aShifthold == 5)
            return null;
        if(aShifthold != 0)
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        if(aMouseclick > 1)
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        ItemStack s = aPlayer.inventory.getItemStack();
        if(s == null)
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        Slot slot = getSlot(aSlotNumber);
        ItemStack slotstack = slot.getStack();
        if(slotstack != null && !GT_Utility.areStacksEqual(slotstack, s))
            return null; // super would replace item
        if(slotstack == null && !slot.isItemValid(s))
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        if(!(s.getItem() instanceof IApiaryUpgrade) && !OrePrefixes.apiaryUpgrade.contains(s))
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        int max = 1;
        if (s.getItem() instanceof IApiaryUpgrade)
            max = ((IApiaryUpgrade) s.getItem()).getMaxNumber(s);
        else
            max = GT_ApiaryUpgrade.getUpgrade(s).getMaxNumber();
        if(slotstack != null)
            max = Math.max(0, max - slotstack.stackSize);
        max = Math.min(max, s.stackSize);
        if(max == 0)
            return null;
        if(aMouseclick == 1)
            max = 1;
        if(max == s.stackSize)
            return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        ItemStack newstack = s.splitStack(s.stackSize - max);
        ItemStack result = super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
        aPlayer.inventory.setItemStack(newstack);
        return result;
    }

    @Override
    public int getSlotStartIndex() {
        return 4;
    }

    @Override
    public int getSlotCount() {
        return 6+9+2;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 6;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((this.mTileEntity.isClientSide()) || (this.mTileEntity.getMetaTileEntity() == null)) {
            return;
        }

        this.mSpeed = getMachine().mSpeed;
        this.mItemTransfer = getMachine().mItemTransfer;
        this.mStuttering = getMachine().mStuttering;
        this.retrievePollen = getMachine().retreviePollen;
        this.mLockedSpeed = getMachine().mLockedSpeed;

        for (Object crafter : this.crafters) {
            ICrafting var1 = (ICrafting) crafter;
            var1.sendProgressBarUpdate(this, 100, this.mSpeed);
            var1.sendProgressBarUpdate(this, 101, this.mItemTransfer ? 1 : 0);
            var1.sendProgressBarUpdate(this, 102, 0);
            for(IErrorState s : getMachine().mErrorStates)
                var1.sendProgressBarUpdate(this, 103, s.getID());
            var1.sendProgressBarUpdate(this, 104, this.mStuttering ? 1 : 0);
            var1.sendProgressBarUpdate(this, 105, this.retrievePollen ? 1 : 0);
            var1.sendProgressBarUpdate(this, 106, this.mLockedSpeed ? 1 : 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                this.mSpeed = par2;
                break;
            case 101:
                this.mItemTransfer = par2 == 1;
                break;
            case 102:
                this.mErrorStates.clear();
                break;
            case 103:
                this.mErrorStates.add(EnumChatFormatting.RED + StatCollector.translateToLocal("for." + ForestryAPI.errorStateRegistry.getErrorState((short) par2).getDescription()));
                break;
            case 104:
                this.mStuttering = par2 == 1;
                break;
            case 105:
                this.retrievePollen = par2 == 1;
                break;
            case 106:
                this.mLockedSpeed = par2 == 1;
                break;
        }
    }

    public GT_MetaTileEntity_IndustrialApiary getMachine() {
        return (GT_MetaTileEntity_IndustrialApiary) mTileEntity.getMetaTileEntity();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        Slot s = getSlot(aSlotIndex);
        if(s == null)
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        if(!(s.inventory instanceof InventoryPlayer))
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        ItemStack aStack = s.getStack();
        if(aStack == null)
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        if(!(aStack.getItem() instanceof IApiaryUpgrade) && !OrePrefixes.apiaryUpgrade.contains(aStack))
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        for(int i = getSlotStartIndex()+2; i < getSlotStartIndex()+2+4; i++) {
            Slot iSlot = getSlot(i);
            ItemStack iStack = iSlot.getStack();
            if(iStack == null)
            {
                if(!iSlot.isItemValid(aStack))
                    continue;
            }
            else
            {
                if(!GT_Utility.areStacksEqual(aStack, iStack))
                    continue;
            }
            int max = 1;
            if (aStack.getItem() instanceof IApiaryUpgrade)
                max = ((IApiaryUpgrade) aStack.getItem()).getMaxNumber(aStack);
            else
                max = GT_ApiaryUpgrade.getUpgrade(aStack).getMaxNumber();
            if (iStack == null) {
                max = Math.min(max, aStack.stackSize);
                ItemStack newstack = aStack.splitStack(max);
                iSlot.putStack(newstack);
            } else {
                max = Math.max(0, max - iStack.stackSize);
                max = Math.min(max, aStack.stackSize);
                iStack.stackSize += max;
                aStack.stackSize -= max;
                iSlot.onSlotChanged();
            }
            if (aStack.stackSize == 0)
                s.putStack(null);
            else
                s.onSlotChanged();
            break;
        }
        return null;
    }

    private static class ApiarySlot extends Slot{

        public ApiarySlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean isItemValid(ItemStack p_75214_1_) {
            return this.inventory.isItemValidForSlot(this.getSlotIndex(), p_75214_1_);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            ((GT_MetaTileEntity_IndustrialApiary)((IGregTechTileEntity)this.inventory).getMetaTileEntity()).onInventoryUpdate(this.getSlotIndex());
        }
    }
}
