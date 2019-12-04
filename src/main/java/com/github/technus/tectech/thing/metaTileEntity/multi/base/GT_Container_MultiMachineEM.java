package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_MultiMachineEM extends GT_ContainerMetaTile_Machine {
    public LedStatus[] eParamsInStatus = LedStatus.makeArray(20,LedStatus.STATUS_UNDEFINED);
    public LedStatus[] eParamsOutStatus = LedStatus.makeArray(20,LedStatus.STATUS_UNDEFINED);
    public double[] eParamsIn = new double[20];//number I from parametrizers
    public long[] eParamsInl = new long[20];
    public double[] eParamsOut = new double[20];//number O to parametrizers
    public long[] eParamsOutl = new long[20];
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
    protected void bindPlayerInventory(InventoryPlayer aInventoryPlayer) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(aInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(aInventoryPlayer, i, 8 + i * 18, 168));
        }
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 174, 116, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 174, 132, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 174, 148, false, false, 1));
        addSlotToContainer(new Slot(mTileEntity, 1, 174, 168));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0 || aSlotIndex > 2) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_MultiblockBase_EM mte = (GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity();
            IGregTechTileEntity base = mte.getBaseMetaTileEntity();
            switch (aSlotIndex) {
                case 0:
                    if(ePowerPassButton) {
                        TecTech.proxy.playSound(base,"fx_click");
                        mte.ePowerPass ^= true;
                        if (!allowedToWorkButton) {//TRANSFORMER HACK
                            if (mte.ePowerPass) {
                                mte.getBaseMetaTileEntity().enableWorking();
                            } else {
                                mte.getBaseMetaTileEntity().disableWorking();
                            }
                        }
                    }
                    break;
                case 1:
                    if(eSafeVoidButton) {
                        TecTech.proxy.playSound(base,"fx_click");
                        mte.eSafeVoid ^= true;
                    }
                    break;
                case 2:
                    if(allowedToWorkButton) {
                        TecTech.proxy.playSound(base,"fx_click");
                        if (mte.getBaseMetaTileEntity().isAllowedToWork()) {
                            mte.getBaseMetaTileEntity().disableWorking();
                        } else {
                            mte.getBaseMetaTileEntity().enableWorking();
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
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null || eParamsInStatus == null) {
            return;
        }
        eParamsInStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).parametrization.eParamsInStatus;
        eParamsOutStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).parametrization.eParamsOutStatus;
        eParamsIn= ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).parametrization.iParamsIn;
        eParamsOut= ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).parametrization.iParamsOut;
        eCertainMode = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eCertainMode;
        eCertainStatus = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eCertainStatus;
        ePowerPass = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).ePowerPass;
        eSafeVoid = ((GT_MetaTileEntity_MultiblockBase_EM) mTileEntity.getMetaTileEntity()).eSafeVoid;
        allowedToWork = mTileEntity.isAllowedToWork();

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            for (int i=100, j = 0; j < eParamsInStatus.length; j++) {
                var1.sendProgressBarUpdate(this, i++, (eParamsInStatus[j].getOrdinalByte() | (eParamsOutStatus[j].getOrdinalByte() << 8)));
            }
            var1.sendProgressBarUpdate(this, 120, eCertainMode | (eCertainStatus << 8));
            var1.sendProgressBarUpdate(this, 121, (ePowerPass ? 1 : 0) + (eSafeVoid ? 2 : 0) + (allowedToWork ? 4 : 0));
            for(int i=128,k=208,j=0;j<20;j++,i+=4,k+=4) {
                Util.sendDouble(eParamsOut[j], this, var1, i);
                Util.sendDouble(eParamsIn[j], this, var1, k);
            }
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        if (eParamsInStatus == null) {
            return;
        }
        if (par1 >= 100 && par1 < 120) {
            eParamsInStatus[par1 - 100] = LedStatus.getStatus ((byte) (par2 & 0xff));
            eParamsOutStatus[par1 - 100] = LedStatus.getStatus ((byte) (par2 >>> 8));
        } else if (par1 == 120) {
            eCertainMode = (byte) (par2 & 0xff);
            eCertainStatus = (byte) (par2 >>> 8);
        } else if (par1 == 121) {
            ePowerPass = (par2 & 1) == 1;
            eSafeVoid = (par2 & 2) == 2;
            allowedToWork = (par2 & 4) == 4;
        } else if(par1>=128 && par1<208){
            int pos=(par1-128)>>2;
            eParamsOut[pos]=Double.longBitsToDouble(eParamsOutl[pos]=Util.receiveLong(eParamsOutl[pos],par1&0xFFFFFFFC,par1,par2));
        }else if(par1>=208 && par1<288){
            int pos=(par1-208)>>2;
            eParamsIn[pos]=Double.longBitsToDouble(eParamsInl[pos]=Util.receiveLong(eParamsInl[pos],par1&0xFFFFFFFC,par1,par2));
        }
    }

    @Override
    public int getSlotStartIndex() {
        return 3;
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