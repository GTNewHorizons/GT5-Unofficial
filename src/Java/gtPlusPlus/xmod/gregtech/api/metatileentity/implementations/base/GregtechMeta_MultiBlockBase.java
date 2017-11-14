package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import java.util.ArrayList;
import java.util.Iterator;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class GregtechMeta_MultiBlockBase
		extends
			GT_MetaTileEntity_MultiBlockBase {

	public static boolean disableMaintenance;
	public ArrayList<GT_MetaTileEntity_Hatch_InputBattery> mChargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_InputBattery>();
	public ArrayList<GT_MetaTileEntity_Hatch_OutputBattery> mDischargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_OutputBattery>();

	public GregtechMeta_MultiBlockBase(final int aID, final String aName,
			final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMeta_MultiBlockBase(final String aName) {
		super(aName);
	}

	public static boolean isValidMetaTileEntity(
			final MetaTileEntity aMetaTileEntity) {
		return (aMetaTileEntity.getBaseMetaTileEntity() != null)
				&& (aMetaTileEntity.getBaseMetaTileEntity()
						.getMetaTileEntity() == aMetaTileEntity)
				&& !aMetaTileEntity.getBaseMetaTileEntity().isDead();
	}

	@Override
	public Object getServerGUI(final int aID,
			final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_MultiMachine(aPlayerInventory,
				aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID,
			final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity,
				this.getLocalName(), "MultiblockDisplay.png");
	}

	@Override
	public String[] getInfoData() {
		return new String[]{"Progress:", (this.mProgresstime / 20) + "secs",
				(this.mMaxProgresstime / 20) + "secs", "Efficiency:",
				(this.mEfficiency / 100.0F) + "%", "Problems:",
				"" + (this.getIdealStatus() - this.getRepairStatus())};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack paramItemStack) {
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack paramItemStack) {
		return 0;
	}

	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
	}

	public void startProcess() {
	}

	public int getValidOutputSlots(final IGregTechTileEntity machineCalling,
			final GT_Recipe sRecipes, final ItemStack[] sInputs) {
		Utils.LOG_WARNING("Finding valid output slots for "
				+ machineCalling.getInventoryName());
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		final GT_Recipe tRecipe = sRecipes;
		final int outputItemCount = tRecipe.mOutputs.length;
		int tValidOutputHatches = 0;

		for (final GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
			if (!isValidMetaTileEntity(tHatch))
				continue;

			int tEmptySlots = 0;
			boolean foundRoom = false;
			final IInventory tHatchInv = tHatch.getBaseMetaTileEntity();
			for (int i = 0; i < tHatchInv.getSizeInventory()
					&& !foundRoom; ++i) {
				if (tHatchInv.getStackInSlot(i) != null)
					continue;

				tEmptySlots++;
				if (tEmptySlots < outputItemCount)
					continue;

				tValidOutputHatches++;
				foundRoom = true;
			}
		}

		return tValidOutputHatches;
	}

	public GT_Recipe reduceRecipeTimeByPercentage(GT_Recipe tRecipe,
			float percentage) {
		int cloneTime = 0;
		GT_Recipe baseRecipe;
		GT_Recipe cloneRecipe = null;

		baseRecipe = tRecipe.copy();
		if (cloneRecipe != baseRecipe || cloneRecipe == null) {
			cloneRecipe = baseRecipe.copy();
			Utils.LOG_WARNING("Setting Recipe");
		}
		if (cloneTime != baseRecipe.mDuration || cloneTime == 0) {
			cloneTime = baseRecipe.mDuration;
			Utils.LOG_WARNING("Setting Time");
		}

		if (cloneRecipe.mDuration > 0) {
			int originalTime = cloneRecipe.mDuration;
			int tempTime = MathUtils.findPercentageOfInt(cloneRecipe.mDuration,
					(100 - percentage));
			cloneRecipe.mDuration = tempTime;
			if (cloneRecipe.mDuration < originalTime) {
				Utils.LOG_INFO("Generated recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return cloneRecipe;
			} else {
				Utils.LOG_INFO("Did not generate recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return tRecipe;
			}
		}
		Utils.LOG_INFO("Error generating recipe, returning null.");
		return null;

	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity,
			long aTick) {
		this.mChargeHatches.clear();
		this.mDischargeHatches.clear();
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void explodeMultiblock() {
		MetaTileEntity tTileEntity;
		for (Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
						.getBaseMetaTileEntity()
						.doExplosion(gregtech.api.enums.GT_Values.V[8]))
			tTileEntity = (MetaTileEntity) localIterator.next();
		tTileEntity = null;
		for (Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
						.getBaseMetaTileEntity()
						.doExplosion(gregtech.api.enums.GT_Values.V[8]))
			tTileEntity = (MetaTileEntity) localIterator.next();
		super.explodeMultiblock();
	}

	public void updateSlots() {
		for (GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches)
			if (isValidMetaTileEntity(tHatch))
				tHatch.updateSlots();
		for (GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches)
			if (isValidMetaTileEntity(tHatch))
				tHatch.updateSlots();
		super.updateSlots();
	}

	public boolean addToMachineList(IGregTechTileEntity aTileEntity,
			int aBaseCasingIndex) {
		if (aTileEntity == null)
			return false;
		IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null)
			return false;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity)
					.updateTexture(aBaseCasingIndex);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery)
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery)
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		return super.addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public boolean addChargeableToMachineList(IGregTechTileEntity aTileEntity,
			int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null)
			return false;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity)
					.updateTexture(aBaseCasingIndex);
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		}
		return false;
	}

	public boolean addDischargeableInputToMachineList(
			IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null)
			return false;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity)
					.updateTexture(aBaseCasingIndex);
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		}
		return false;
	}

}
