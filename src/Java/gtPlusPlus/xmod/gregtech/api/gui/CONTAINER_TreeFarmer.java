package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotBuzzSaw;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityTreeFarm;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class CONTAINER_TreeFarmer extends GT_ContainerMetaTile_Machine {

    public long maxEU = 0;
    public long storedEU = 0;

    public CONTAINER_TreeFarmer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public CONTAINER_TreeFarmer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean bindInventory) {
        super(aInventoryPlayer, aTileEntity, bindInventory);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new SlotBuzzSaw(mTileEntity, 1, 80, 17));
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }

    @Override
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
        switch (id) {
            case 100:
                this.maxEU = value;
                return;
            case 101:
                this.storedEU = value;
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(ICrafting crafting : (List<ICrafting>)crafters) {
            crafting.sendProgressBarUpdate(this, 100, (int) ((GregtechMetaTileEntityTreeFarm) this.mTileEntity.getMetaTileEntity()).maxEUStore());
            crafting.sendProgressBarUpdate(this, 101, (int) ((GregtechMetaTileEntityTreeFarm) this.mTileEntity.getMetaTileEntity()).getStoredInternalPower());
        }
    }
    
}