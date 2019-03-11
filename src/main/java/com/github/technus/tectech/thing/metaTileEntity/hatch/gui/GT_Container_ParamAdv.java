package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_ParamAdv extends GT_ContainerMetaTile_Machine {
    public int pointer=0;
    public int param = 0;
    public double value1f = 0;
    public double value0f = 0;
    public double input0f = 0;
    public double input1f = 0;

    public GT_Container_ParamAdv(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 59, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            boolean doStuff = true;
            GT_MetaTileEntity_Hatch_Param paramH = (GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity();
            int columnPointer=paramH.pointer &0xff;
            boolean secondRow=(paramH.pointer &0x0100)!=0;
            switch (aSlotIndex) {
                case 0:
                    paramH.param -= aShifthold == 1 ? 16 : 4;
                    break;
                case 1:
                    if (secondRow) {
                        secondRow = false;
                    } else {
                        columnPointer -= aShifthold == 1 ? 16 : 4;
                    }
                    break;
                case 2:
                    if (secondRow) {
                        columnPointer -= aShifthold == 1 ? 16 : 4;
                    } else {
                        secondRow = true;
                    }
                    break;
                case 3:
                    if (aShifthold == 1) {
                        if (secondRow) {
                            paramH.value1D = Double.longBitsToDouble(0xFFFF_FFFF_FFFF_FFFFL);
                        } else {
                            paramH.value0D = Double.longBitsToDouble(0xFFFF_FFFF_FFFF_FFFFL);
                        }
                    } else {
                        if (secondRow) {
                            long temp=Double.doubleToLongBits(paramH.value1D);
                            temp |= 1 << columnPointer;
                            paramH.value1D=Double.longBitsToDouble(temp);
                        } else {
                            long temp=Double.doubleToLongBits(paramH.value0D);
                            temp |= 1 << columnPointer;
                            paramH.value0D=Double.longBitsToDouble(temp);
                        }
                    }
                    break;
                case 4:
                    paramH.param -= aShifthold == 1 ? 2 : 1;
                    break;
                case 5:
                    if (secondRow) {
                        secondRow = false;
                    } else {
                        columnPointer -= aShifthold == 1 ? 2 : 1;
                    }
                    break;
                case 6:
                    if (secondRow) {
                        columnPointer -= aShifthold == 1 ? 2 : 1;
                    } else {
                        secondRow = true;
                    }
                    break;
                case 7:
                    if (aShifthold == 1) {
                        if (secondRow) {
                            paramH.value1D = Double.longBitsToDouble(0);
                        } else {
                            paramH.value0D = Double.longBitsToDouble(0);
                        }
                    } else {
                        if (secondRow) {
                            long temp=Double.doubleToLongBits(paramH.value1D);
                            temp &= ~(1 << columnPointer);
                            paramH.value1D=Double.longBitsToDouble(temp);
                        } else {
                            long temp=Double.doubleToLongBits(paramH.value0D);
                            temp &= ~(1 << columnPointer);
                            paramH.value0D=Double.longBitsToDouble(temp);
                        }
                    }
                    break;
                case 8:
                    paramH.param += aShifthold == 1 ? 16 : 4;
                    break;
                case 9:
                    if (secondRow) {
                        secondRow = false;
                    } else {
                        columnPointer += aShifthold == 1 ? 16 : 4;
                    }
                    break;
                case 10:
                    if (secondRow) {
                        columnPointer += aShifthold == 1 ? 16 : 4;
                    } else {
                        secondRow = true;
                    }
                    break;
                case 11:
                    paramH.value0D=paramH.input0D;
                    paramH.value1D=paramH.input1D;
                    break;
                case 12:
                    paramH.param += aShifthold == 1 ? 2 : 1;
                    break;
                case 13:
                    if (secondRow) {
                        secondRow = false;
                    } else {
                        columnPointer += aShifthold == 1 ? 2 : 1;
                    }
                    break;
                case 14:
                    if (secondRow) {
                        columnPointer += aShifthold == 1 ? 2 : 1;
                    } else {
                        secondRow = true;
                    }
                    break;
                case 15:
                    if (aShifthold == 1) {
                        if (secondRow) {
                            paramH.value1D = Double.longBitsToDouble(~Double.doubleToLongBits(paramH.value1D));
                        } else {
                            paramH.value0D = Double.longBitsToDouble(~Double.doubleToLongBits(paramH.value1D));
                        }
                    } else {
                        if (secondRow) {
                            long temp=Double.doubleToLongBits(paramH.value1D);
                            temp ^= 1 << columnPointer;
                            paramH.value1D=Double.longBitsToDouble(temp);
                        } else {
                            long temp=Double.doubleToLongBits(paramH.value0D);
                            temp ^= 1 << columnPointer;
                            paramH.value0D=Double.longBitsToDouble(temp);
                        }
                    }
                    break;
                default:
                    doStuff = false;
            }
            if (doStuff) {
                IGregTechTileEntity base=paramH.getBaseMetaTileEntity();
                TecTech.proxy.playSound(base,"fx_click");
                if(columnPointer>=32) {
                    columnPointer = 31;
                } else if(columnPointer<0) {
                    columnPointer = 0;
                }
                paramH.pointer=secondRow?columnPointer+0x100:columnPointer;
                if (paramH.param > 9) {
                    paramH.param = 9;
                } else if (paramH.param < -1) {
                    paramH.param = -1;
                }
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
        param = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).param;
        value0f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).value0D;
        value1f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).value1D;
        input0f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).input0D;
        input1f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).input1D;
        pointer = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).pointer;

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            Util.sendInteger(param,this,var1,100);
            Util.sendDouble(value0f,this,var1,102);
            Util.sendDouble(value1f,this,var1, 106);
            Util.sendDouble(input0f,this,var1, 110);
            Util.sendDouble(input1f,this,var1, 114);
            Util.sendInteger(pointer,this,var1,118);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
            case 101:
                param=Util.receiveInteger(param,100,par1,par2);
                return;
            case 102:
            case 103:
            case 104:
            case 105:
                value0f=Util.receiveDouble(value0f,102,par1,par2);
                return;
            case 106:
            case 107:
            case 108:
            case 109:
                value1f=Util.receiveDouble(value1f,106,par1,par2);
                return;
            case 110:
            case 111:
            case 112:
            case 113:
                input0f=Util.receiveDouble(input0f,110,par1,par2);
                return;
            case 114:
            case 115:
            case 116:
            case 117:
                input1f=Util.receiveDouble(input1f,114,par1,par2);
                return;
            case 118:
            case 119:
                pointer=Util.receiveInteger(pointer,118,par1,par2);
                return;
            default:
        }
    }
}
