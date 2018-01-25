package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.PollutionUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
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
	public boolean isCorrectMachinePart(final ItemStack paramItemStack) {
		return true;
	}

	@Override
	public int getDamageToComponent(final ItemStack paramItemStack) {
		return 0;
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
	}

	public void startProcess() {
	}

	public int getValidOutputSlots(final IGregTechTileEntity machineCalling,
			final GT_Recipe sRecipes, final ItemStack[] sInputs) {
		Logger.WARNING("Finding valid output slots for "
				+ machineCalling.getInventoryName());
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		final GT_Recipe tRecipe = sRecipes;
		final int outputItemCount = tRecipe.mOutputs.length;
		int tValidOutputHatches = 0;

		for (final GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
			if (!isValidMetaTileEntity(tHatch)) {
				continue;
			}

			int tEmptySlots = 0;
			boolean foundRoom = false;
			final IInventory tHatchInv = tHatch.getBaseMetaTileEntity();
			for (int i = 0; (i < tHatchInv.getSizeInventory())
					&& !foundRoom; ++i) {
				if (tHatchInv.getStackInSlot(i) != null) {
					continue;
				}

				tEmptySlots++;
				if (tEmptySlots < outputItemCount) {
					continue;
				}

				tValidOutputHatches++;
				foundRoom = true;
			}
		}

		return tValidOutputHatches;
	}

	public GT_Recipe reduceRecipeTimeByPercentage(final GT_Recipe tRecipe,
			final float percentage) {
		int cloneTime = 0;
		GT_Recipe baseRecipe;
		GT_Recipe cloneRecipe = null;

		baseRecipe = tRecipe.copy();
		if ((cloneRecipe != baseRecipe) || (cloneRecipe == null)) {
			cloneRecipe = baseRecipe.copy();
			Logger.WARNING("Setting Recipe");
		}
		if ((cloneTime != baseRecipe.mDuration) || (cloneTime == 0)) {
			cloneTime = baseRecipe.mDuration;
			Logger.WARNING("Setting Time");
		}

		if (cloneRecipe.mDuration > 0) {
			final int originalTime = cloneRecipe.mDuration;
			final int tempTime = MathUtils.findPercentageOfInt(cloneRecipe.mDuration,
					(100 - percentage));
			cloneRecipe.mDuration = tempTime;
			if (cloneRecipe.mDuration < originalTime) {
				Logger.MACHINE_INFO("Generated recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return cloneRecipe;
			} else {
				Logger.MACHINE_INFO("Did not generate recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return tRecipe;
			}
		}
		Logger.MACHINE_INFO("Error generating recipe, returning null.");
		return null;

	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity,
			final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		//this.mChargeHatches.clear();
		//this.mDischargeHatches.clear();
	}

	@Override
	public void explodeMultiblock() {
		MetaTileEntity tTileEntity;
		for (final Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		tTileEntity = null;
		for (final Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		super.explodeMultiblock();
	}

	@Override
	public void updateSlots() {
		for (final GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		for (final GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		super.updateSlots();
	}

	public boolean isToolCreative(ItemStack mStack){
		Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(mStack);
		Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(mStack);
		if (t1 == Materials._NULL && t2 == Materials._NULL){
			return true;
		}
		return false;
	}

	@Override
	public boolean addToMachineList(final IGregTechTileEntity aTileEntity,
			final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}

		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		}
		if (LoadedMods.TecTech){
			if (isThisHatchMultiDynamo()) {
				updateTexture(aTileEntity, aBaseCasingIndex);
				return this.mMultiDynamoHatches.add(
						(GT_MetaTileEntity_Hatch) aMetaTileEntity);
			}

		}
		return super.addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public boolean addChargeableToMachineList(final IGregTechTileEntity aTileEntity,
			final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		}
		return false;
	}

	public boolean addDischargeableInputToMachineList(
			final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		}
		return false;
	}


	public boolean addFluidInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
			return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
		}
		return false;
	}

	public boolean addFluidOutputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
		}
		return false;
	}

	/**
	 * Enable Texture Casing Support if found in GT 5.09
	 */

	public boolean updateTexture(final IGregTechTileEntity aTileEntity, int aCasingID){
		try {
			Method mProper = Class.forName("gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch").getDeclaredMethod("updateTexture", int.class);
			if (mProper != null){
				if (aTileEntity instanceof GT_MetaTileEntity_Hatch){				
					mProper.setAccessible(true);
					mProper.invoke(this, aCasingID);
					return true;
				}					
			}
			else {
				return false;
			}
		}
		catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}		
		return false;
	}









	/**
	 * TecTech Support
	 */


	/**
	 * This is the array Used to Store the Tectech Multi-Amp hatches.
	 */

	public ArrayList<GT_MetaTileEntity_Hatch> mMultiDynamoHatches = new ArrayList();	

	/**
	 * TecTech Multi-Amp Dynamo Support
	 * @param aTileEntity - The Dynamo Hatch
	 * @param aBaseCasingIndex - Casing Texture
	 * @return
	 */

	public boolean addMultiAmpDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex){
		//GT_MetaTileEntity_Hatch_DynamoMulti
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (isThisHatchMultiDynamo()) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mMultiDynamoHatches.add((GT_MetaTileEntity_Hatch) aMetaTileEntity);
		}
		return false;
	}

	public boolean isThisHatchMultiDynamo(){
		Class mDynamoClass;
		try {
			mDynamoClass = Class.forName("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti");
			if (mDynamoClass != null){
				if (mDynamoClass.isInstance(this)){
					return true;
				}
			}
		}
		catch (ClassNotFoundException e) {}
		return false;
	}

	@Override
	public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (LoadedMods.TecTech){
			if (isThisHatchMultiDynamo()) {
				addMultiAmpDynamoToMachineList(aTileEntity, aBaseCasingIndex);
			}

		}
		return super.addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
	}


	/**
	 * Pollution Management
	 */

	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}

	public boolean polluteEnvironment(int aPollutionLevel) {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			try {
				Integer mPollution = 0;
				Field f = ReflectionUtils.getField(this.getClass(), "mPollution");
				if (f != null){
					try {
						mPollution = (Integer) f.get(this);
					}
					catch (IllegalArgumentException | IllegalAccessException e) {}
				}
				if (f != null){
					try {
						if (mPollution != null){
							//Reflectively set the pollution back to the TE
							int temp = (mPollution += aPollutionLevel);
							f.set(this, temp);
							Logger.REFLECTION("Set pollution to "+temp+", it was "+mPollution+" before.");

							//Iterate Mufflers
							for (final GT_MetaTileEntity_Hatch_Muffler tHatch : this.mMufflerHatches) {
								if (isValidMetaTileEntity(tHatch)) {
									if (mPollution < 10000) {
										break;
									}
									if (!polluteEnvironmentHatch(tHatch)) {
										continue;
									}
									mPollution -= 10000;
								}
							}
							return mPollution < 10000;

						}
					}
					catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}
			catch (Throwable t){}
		}
		return false;
	}

	public boolean polluteEnvironmentHatch(GT_MetaTileEntity_Hatch_Muffler tHatch) {
		if (tHatch.getBaseMetaTileEntity().getAirAtSide(tHatch.getBaseMetaTileEntity().getFrontFacing())) {
			Logger.REFLECTION("doing pollution");
			PollutionUtils.addPollution(tHatch.getBaseMetaTileEntity(), calculatePollutionReduction(tHatch, 10000));
			return true;
		} else {
			return false;
		}
	}

	public int calculatePollutionReduction(GT_MetaTileEntity_Hatch_Muffler tHatch, int aPollution) {
		return (int) ((double) aPollution * Math.pow(0.7D, (double) (tHatch.mTier - 1)));
	}

}
