package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

import java.util.Iterator;

/**
 * Created by Tec on 09.04.2017.
 */
public class GT_Container_Rack extends GT_ContainerMetaTile_Machine {
    public boolean heat=false;

    public GT_Container_Rack(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new Slot(this.mTileEntity, 0, 69, 28));
        this.addSlotToContainer(new Slot(this.mTileEntity, 1, 91, 28));
        this.addSlotToContainer(new Slot(this.mTileEntity, 2, 69, 50));
        this.addSlotToContainer(new Slot(this.mTileEntity, 3, 91, 50));
    }

    public int getSlotCount() {
        return 4;
    }

    public int getShiftClickSlotCount() {
        return 4;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((this.mTileEntity.isClientSide()) || (this.mTileEntity.getMetaTileEntity() == null)) {
            return;
        }
        this.heat = ((GT_MetaTileEntity_Hatch_Rack) this.mTileEntity.getMetaTileEntity()).heat>0;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 100, this.heat?1:0);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                this.heat = par2!=0;
                return;
        }
        if(heat || mActive!=0) Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
