package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_MultiMachineEM extends GT_ContainerMetaTile_Machine {
    public byte[] eParamsInStatus = new byte[20];//unused 0,G ok 1, B too low 2, R too high 3, Y blink dangerous 4,5
    public byte[] eParamsOutStatus = new byte[20];
    public byte eCertainMode = 5, eCertainStatus = 127;
    public boolean ePowerPass = false, eSafeVoid = false, allowedToWork = false;
    public final boolean ePowerPassButton, eSafeVoidButton, allowedToWorkButton;

    public GT_Container_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean enablePowerPass, boolean enableSafeVoid, boolean enablePowerButton) {
        super(aInventoryPlayer, aTileEntity);
        ePowerPassButton=enablePowerPass;
        eSafeVoidButton=enableSafeVoid;
        allowedToWorkButton=enablePowerButton;
    }

    public GT_Container_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
        ePowerPassButton=eSafeVoidButton=allowedToWorkButton=true;
    }

    public GT_Container_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean bindInventory) {
        super(aInventoryPlayer, aTileEntity, bindInventory);
        ePowerPassButton=eSafeVoidButton=allowedToWorkButton=true;
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 1, 152, -21));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, -2, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 15, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 32, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0)
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_MultiblockBase_EM base = (GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity();
            switch (aSlotIndex) {
                case 1:
                    if(ePowerPassButton) {
                        base.ePowerPass ^= true;
                        if (!allowedToWorkButton) {//TRANSFORMER HACK
                            if (base.ePowerPass) base.getBaseMetaTileEntity().enableWorking();
                            else base.getBaseMetaTileEntity().disableWorking();
                        }
                    }
                    break;
                case 2:
                    if(eSafeVoidButton) base.eSafeVoid ^= true;
                    break;
                case 3:
                    if(allowedToWorkButton) {
                        if (base.getBaseMetaTileEntity().isAllowedToWork()) {
                            base.getBaseMetaTileEntity().disableWorking();
                        } else {
                            base.getBaseMetaTileEntity().enableWorking();
                        }
                    }
                    break;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null || eParamsInStatus == null)
            return;
        eParamsInStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eParamsInStatus;
        eParamsOutStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eParamsOutStatus;
        eCertainMode = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eCertainMode;
        eCertainStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eCertainStatus;
        ePowerPass = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).ePowerPass;
        eSafeVoid = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eSafeVoid;
        allowedToWork = mTileEntity.isAllowedToWork();

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            int i = 100;
            for (int j = 0; j < eParamsInStatus.length; j++)
                var1.sendProgressBarUpdate(this, i++, eParamsInStatus[j] | eParamsOutStatus[j] << 8);
            var1.sendProgressBarUpdate(this, 120, eCertainMode | eCertainStatus << 8);
            var1.sendProgressBarUpdate(this, 121, (ePowerPass ? 1 : 0) + (eSafeVoid ? 2 : 0) + (allowedToWork ? 4 : 0));
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        if (eParamsInStatus == null) return;
        if (par1 >= 100 && par1 < 120) {
            eParamsInStatus[par1 - 100] = (byte) (par2 & 0xff);
            eParamsOutStatus[par1 - 100] = (byte) (par2 >>> 8);
        } else if (par1 == 120) {
            eCertainMode = (byte) (par2 & 0xff);
            eCertainStatus = (byte) (par2 >>> 8);
        } else if (par1 == 121) {
            ePowerPass = (par2 & 1) == 1;
            eSafeVoid = (par2 & 2) == 2;
            allowedToWork = (par2 & 4) == 4;
        }
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 1;
    }
}