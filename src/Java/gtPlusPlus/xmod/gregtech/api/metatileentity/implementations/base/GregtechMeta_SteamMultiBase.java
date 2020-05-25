package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.V;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMeta_SteamMultiBase extends GregtechMeta_MultiBlockBase {

	private int aMaxParallelForCurrentRecipe = 0;
	
	public GregtechMeta_SteamMultiBase(String aName) {
		super(aName);
	}

	public GregtechMeta_SteamMultiBase(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()], aActive ? getFrontOverlayActive() : getFrontOverlay()};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()]};
	}

	protected abstract GT_RenderedTexture getFrontOverlay();
	
	protected abstract GT_RenderedTexture getFrontOverlayActive();

	private int getCasingTextureIndex() {
		return 0;
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public boolean checkRecipe(ItemStack arg0) {

		log("Running checkRecipeGeneric(0)");	
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		GT_Recipe_Map tMap = this.getRecipeMap();
		if (tMap == null) {
			return false;
		}        
		ItemStack[] aItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] aFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);		
		GT_Recipe tRecipe = tMap.findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, V[1], null, null, aItemInputs);
		if (tRecipe == null) {
			return false;
		}

		int aEUPercent = 100;
		int aSpeedBonusPercent = 0;
		int aOutputChanceRoll = 10000;		

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};


		log("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;
		

		int aMaxParallelRecipes = this.canBufferOutputs(tRecipe, this.getMaxParallelRecipes());
		if (aMaxParallelRecipes == 0) {
			log("BAD RETURN - 2");
			return false;
		}
		aMaxParallelForCurrentRecipe = aMaxParallelRecipes;
		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+aMaxParallelRecipes);
		log("tTotalEUt: "+tTotalEUt);
		log("tRecipeEUt: "+tRecipeEUt);
		
		
		
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (32 - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			log("BAD RETURN - 3");
			return false;
		}

		// -- Try not to fail after this point - inputs have already been consumed! --



		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor * 2);

		this.mEUt = (int)Math.ceil(tTotalEUt * 3);

		//this.mEUt = (3 * tRecipe.mEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= parallelRecipes;
			}
		}

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
		for (int h = 0; h < tRecipe.mOutputs.length; h++) {
			if (tRecipe.getOutput(h) != null) {
				tOutputItems[h] = tRecipe.getOutput(h).copy();
				tOutputItems[h].stackSize = 0;
			}
		}

		// Set output item stack sizes (taking output chance into account)
		for (int f = 0; f < tOutputItems.length; f++) {
			if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
				for (int g = 0; g < parallelRecipes; g++) {
					if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
						tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
				}
			}
		}

		tOutputItems = removeNulls(tOutputItems);

		// Sanitize item stack size, splitting any stacks greater than max stack size
		List<ItemStack> splitStacks = new ArrayList<ItemStack>();
		for (ItemStack tItem : tOutputItems) {
			while (tItem.getMaxStackSize() < tItem.stackSize) {
				ItemStack tmp = tItem.copy();
				tmp.stackSize = tmp.getMaxStackSize();
				tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
				splitStacks.add(tmp);
			}
		}

		if (splitStacks.size() > 0) {
			ItemStack[] tmp = new ItemStack[splitStacks.size()];
			tmp = splitStacks.toArray(tmp);
			tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
		}

		// Strip empty stacks
		List<ItemStack> tSList = new ArrayList<ItemStack>();
		for (ItemStack tS : tOutputItems) {
			if (tS.stackSize > 0) tSList.add(tS);
		}
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		aMaxParallelForCurrentRecipe = 0;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;

	}

	public ArrayList<FluidStack> getAllSteamStacks(){
		ArrayList<FluidStack> aFluids = new ArrayList<FluidStack>();
		FluidStack aSteam = FluidUtils.getSteam(1);
		for (FluidStack aFluid : this.getStoredFluids()) {
			if (aFluid.isFluidEqual(aSteam)) {
				aFluids.add(aFluid);
			}
		}		
		return aFluids;
	}
	
	public int getTotalSteamStored() {
		int aSteam = 0;
		for (FluidStack aFluid : getAllSteamStacks()) {
			aSteam += aFluid.amount;
		}
		return aSteam;		
	}
	
	public boolean tryConsumeSteam(int aAmount) {
		if (getTotalSteamStored() <= 0) {
			return false;
		}
		else {
			return this.depleteInput(FluidUtils.getSteam(aAmount));
		}
	}

	@Override
	public int getMaxEfficiency(ItemStack arg0) {
		return 0;
	}

	
    /**
     * Called every tick the Machine runs
     */
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUt < 0) {
        	long aSteamVal = (((long) -mEUt * 10000) / Math.max(1000, mEfficiency));
        	Logger.INFO("Trying to drain "+aSteamVal+" steam per tick.");
        	//aMaxParallelForCurrentRecipe
            if (!tryConsumeSteam((int) aSteamVal)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("aMaxParallelForCurrentRecipe", aMaxParallelForCurrentRecipe);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		aMaxParallelForCurrentRecipe = aNBT.getInteger("aMaxParallelForCurrentRecipe");
	}

	@Override
	public void stopMachine() {
		super.stopMachine();
		aMaxParallelForCurrentRecipe = 0;
	}

}
