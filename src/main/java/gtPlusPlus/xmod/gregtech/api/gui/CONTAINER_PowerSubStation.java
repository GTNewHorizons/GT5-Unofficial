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

	public long mStoredEU;
	public int mStoredEU1;
	public int mStoredEU2;
	public long mMaxStoredEU;
	public int mMaxStoredEU1;
	public int mMaxStoredEU2;
	public long mAverageEuAdded;
	public int mAverageEuAdded1;
	public int mAverageEuAdded2;
	public long mAverageEuConsumed;
	public int mAverageEuConsumed1;
	public int mAverageEuConsumed2;

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

		mStoredEU = mTileEntity.getStoredEU();
		mMaxStoredEU = mTileEntity.getEUCapacity();
		GregtechMetaTileEntity_PowerSubStationController aPSS = (GregtechMetaTileEntity_PowerSubStationController) mTileEntity.getMetaTileEntity();		
		mAverageEuAdded = aPSS.getAverageEuAdded();
		mAverageEuConsumed = aPSS.getAverageEuConsumed();
		int[] aStored = MathUtils.splitLongIntoTwoIntegers(mStoredEU);
		int[] aMaxStorage = MathUtils.splitLongIntoTwoIntegers(mMaxStoredEU);
		int[] aAvgIn = MathUtils.splitLongIntoTwoIntegers(mAverageEuAdded);
		int[] aAvgOut = MathUtils.splitLongIntoTwoIntegers(mAverageEuConsumed);
		mStoredEU1 = aStored[0];
		mStoredEU2 = aStored[1];
		mMaxStoredEU1 = aMaxStorage[0];
		mMaxStoredEU2 = aMaxStorage[1];
		mAverageEuAdded1 = aAvgIn[0];
		mAverageEuAdded2 = aAvgIn[1];
		mAverageEuConsumed1 = aAvgOut[0];
		mAverageEuConsumed2 = aAvgOut[1];

		for (Object crafter : this.crafters) {
			ICrafting var1 = (ICrafting) crafter;
			if (mTimer % 20 == 0) {
				var1.sendProgressBarUpdate(this, 21, mStoredEU1);
				var1.sendProgressBarUpdate(this, 22, mStoredEU2);
				var1.sendProgressBarUpdate(this, 23, mMaxStoredEU1);
				var1.sendProgressBarUpdate(this, 24, mMaxStoredEU2);
				var1.sendProgressBarUpdate(this, 25, mAverageEuAdded1);
				var1.sendProgressBarUpdate(this, 26, mAverageEuAdded2);
				var1.sendProgressBarUpdate(this, 27, mAverageEuConsumed1);
				var1.sendProgressBarUpdate(this, 28, mAverageEuConsumed2);
			}
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 21 :
				mStoredEU1 = par2;
				break;
			case 22 :
				mStoredEU2 = par2;
				mStoredEU = MathUtils.combineTwoIntegersToLong(mStoredEU1, mStoredEU2);
				break;
			case 23 :
				mMaxStoredEU1 = par2;
				break;
			case 24 :
				mMaxStoredEU2 = par2;
				mMaxStoredEU = MathUtils.combineTwoIntegersToLong(mMaxStoredEU1, mMaxStoredEU2);
				break;
			case 25 :
				mAverageEuAdded1 = par2;
				break;
			case 26 :
				mAverageEuAdded2 = par2;
				mAverageEuAdded = MathUtils.combineTwoIntegersToLong(mAverageEuAdded1, mAverageEuAdded2);
				break;
			case 27 :
				mAverageEuConsumed1 = par2;
				break;
			case 28 :
				mAverageEuConsumed2 = par2;
				mAverageEuConsumed = MathUtils.combineTwoIntegersToLong(mAverageEuConsumed1, mAverageEuConsumed2);
				break;
		}
	}
}
