package gtPlusPlus.xmod.gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.GregtechMetaTileEntity_PowerSubStationController;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;

public class CONTAINER_PowerSubStation extends GT_Container_MultiMachine {
	private final SyncedValueManager manager = new SyncedValueManager(21);
	public final SyncedLong mStoredEU = manager.allocateLong();
	public final SyncedLong mMaxStoredEU = manager.allocateLong();
	public final SyncedLong mAverageEuAdded = manager.allocateLong();
	public final SyncedLong mAverageEuConsumed = manager.allocateLong();

	public CONTAINER_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}

	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new Slot((IInventory) this.mTileEntity, 1, 155, 5));
		this.addSlotToContainer(new SlotNoInput((IInventory) this.mTileEntity, 2, 155, 23));

		for (int i = 0; i < 9; ++i) { this.addSlotToContainer(new Slot((IInventory) aInventoryPlayer, i, 8 + i * 18, 167)); }

	}

	public int getSlotCount() {
		return 10;
	}

	public int getShiftClickSlotCount() {
		return 1;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		GregtechMetaTileEntity_PowerSubStationController aPSS = (GregtechMetaTileEntity_PowerSubStationController) mTileEntity.getMetaTileEntity();
		mStoredEU.setValue(mTileEntity.getStoredEU());
		mMaxStoredEU.setValue(mTileEntity.getEUCapacity());
		mAverageEuAdded.setValue(aPSS.getAverageEuAdded());
		mAverageEuConsumed.setValue(aPSS.getAverageEuConsumed());

		manager.detectAndSendChanges(this::sendProgressBarUpdate, mTimer);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		manager.updateProgressBar(par1, par2);
	}

	private void sendProgressBarUpdate(int short1, int short2) {
		for (Object crafter : this.crafters) {
			((ICrafting) crafter).sendProgressBarUpdate(this, short1, short2);
		}
	}
}
