package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.V;

import java.util.ArrayList;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMeta_MultiBlockBase extends GT_MetaTileEntity_MultiBlockBase {

	public static boolean disableMaintenance;

	public GregtechMeta_MultiBlockBase(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMeta_MultiBlockBase(final String aName) {
		super(aName);
	}

	public static boolean isValidMetaTileEntity(final MetaTileEntity aMetaTileEntity) {
		return (aMetaTileEntity.getBaseMetaTileEntity() != null) && (aMetaTileEntity.getBaseMetaTileEntity().getMetaTileEntity() == aMetaTileEntity) && !aMetaTileEntity.getBaseMetaTileEntity().isDead();
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MultiblockDisplay.png");
	}

	@Override
	public String[] getInfoData() {
		return new String[]{"Progress:", (this.mProgresstime / 20) + "secs", (this.mMaxProgresstime / 20) + "secs", "Efficiency:", (this.mEfficiency / 100.0F) + "%", "Problems:", "" + (this.getIdealStatus() - this.getRepairStatus())};
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
	
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {}

    public void startProcess() {}

	public int getValidOutputSlots(final IGregTechTileEntity machineCalling, final GT_Recipe sRecipes, final ItemStack[] sInputs){
		//Utils.LOG_INFO("Finding valid output slots for "+machineCalling.getInventoryName());

		try{
			
			if (sRecipes == null){
				return 0;
			}
			
			final ArrayList<ItemStack> tInputList = this.getStoredInputs();
			final GT_Recipe tRecipe = sRecipes;
			final int outputItemCount;
			if (tRecipe.mOutputs != null){
				outputItemCount= tRecipe.mOutputs.length;	    	
			}
			else {
				outputItemCount= 0;
			}
			int tValidOutputHatches = 0;

			for (final GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
				if (!isValidMetaTileEntity(tHatch)) continue;

				int tEmptySlots = 0;
				boolean foundRoom = false;
				final IInventory tHatchInv = tHatch.getBaseMetaTileEntity();
				for(int i = 0; i < tHatchInv.getSizeInventory() && !foundRoom; ++i)
				{
					if(tHatchInv.getStackInSlot(i) != null) continue;

					tEmptySlots++;
					if(tEmptySlots < outputItemCount) continue;

					tValidOutputHatches++;
					foundRoom = true;
				}
			}
			if (tValidOutputHatches < 0){
				tValidOutputHatches = 0;
			}

			return tValidOutputHatches;
		} catch (Throwable t){
			t.printStackTrace();
			return 0;
		}
	}

	public GT_Recipe reduceRecipeTimeByPercentage(GT_Recipe tRecipe, float percentage){
		int cloneTime = 0;
		GT_Recipe baseRecipe;
		GT_Recipe cloneRecipe = null;

		baseRecipe = tRecipe.copy();
		if (cloneRecipe != baseRecipe || cloneRecipe == null){
			cloneRecipe = baseRecipe.copy();
			Utils.LOG_WARNING("Setting Recipe");
		}	
		if (cloneTime != baseRecipe.mDuration || cloneTime == 0){
			cloneTime = baseRecipe.mDuration;
			Utils.LOG_WARNING("Setting Time");
		}

		if (cloneRecipe.mDuration > 0){
			int originalTime = cloneRecipe.mDuration;
			int tempTime = MathUtils.findPercentageOfInt(cloneRecipe.mDuration, (100-percentage));
			cloneRecipe.mDuration = tempTime;
			if (cloneRecipe.mDuration < originalTime){
				Utils.LOG_INFO("Generated recipe with a smaller time. | "+originalTime+" | "+cloneRecipe.mDuration+" |");
				return cloneRecipe;
			}
			else {
				Utils.LOG_INFO("Did not generate recipe with a smaller time. | "+originalTime+" | "+cloneRecipe.mDuration+" |");
				return tRecipe;
			}
		}
		Utils.LOG_INFO("Error generating recipe, returning null.");
		return null;



	}


}
