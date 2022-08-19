package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

public class GT_Container_DataReader extends GT_Container_BasicTank {
    public boolean mStuttering = false;

    public GT_Container_DataReader(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        //this.addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 0, 8, 63, false, true, 1));
        //this.addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 0, 26, 63, false, true, 1));
        //this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, 2, 107, 63));

        int tStartIndex = ((GT_MetaTileEntity_BasicMachine)this.mTileEntity.getMetaTileEntity()).getInputSlot();
        this.addSlotToContainer(new Slot(this.mTileEntity, tStartIndex, 53, 153));

        tStartIndex = ((GT_MetaTileEntity_BasicMachine)this.mTileEntity.getMetaTileEntity()).getOutputSlot();
        this.addSlotToContainer(new GT_Slot_Output(this.mTileEntity, tStartIndex, 107, 153));

        this.addSlotToContainer(new Slot(this.mTileEntity, 1, 17, 153));
        //this.addSlotToContainer(new Slot(this.mTileEntity, 3, 125, 63));
        //this.addSlotToContainer(new GT_Slot_Render(this.mTileEntity, tStartIndex, 53, 63));
    }

    @Override
    protected void bindPlayerInventory(InventoryPlayer aInventoryPlayer) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(aInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 174 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(aInventoryPlayer, i, 8 + i * 18, 232));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {
            this.mStuttering = ((GT_MetaTileEntity_BasicMachine)this.mTileEntity.getMetaTileEntity()).mStuttering;

            for (Object crafter : this.crafters) {
                ICrafting var1 = (ICrafting) crafter;
                var1.sendProgressBarUpdate(this, 102, this.mStuttering ? 1 : 0);
            }

        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        if (par1 == 102) {
            this.mStuttering = par2 != 0;
        }

    }

    @Override
    public int getSlotStartIndex() {
        return 0;
    }

    @Override
    public int getShiftClickStartIndex() {
        return 0;
    }

    @Override
    public int getSlotCount() {
        return 3;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 1;
    }
}
