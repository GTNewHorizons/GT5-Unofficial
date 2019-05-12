package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler_Adv;


public class CONTAINER_Hatch_Muffler_Advanced extends GT_ContainerMetaTile_Machine {

	public long maxEU = 0;
	public long storedEU = 0;

	public CONTAINER_Hatch_Muffler_Advanced(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_Hatch_Muffler_Advanced(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new Slot(this.mTileEntity, 1, 80, 17));
	}

	@Override
	public int getSlotCount() {
		return 1;
	}

	@Override
	public int getShiftClickSlotCount() {
		return 1;
	}

	@Override
	public void updateProgressBar(final int id, final int value) {
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
		for(final ICrafting crafting : (List<ICrafting>)this.crafters) {
			crafting.sendProgressBarUpdate(this, 100, (int) ((GT_MetaTileEntity_Hatch_Muffler_Adv) this.mTileEntity.getMetaTileEntity()).maxEUStore());
			crafting.sendProgressBarUpdate(this, 101, (int) ((GT_MetaTileEntity_Hatch_Muffler_Adv) this.mTileEntity.getMetaTileEntity()).getEUVar());			
		}
	}

}