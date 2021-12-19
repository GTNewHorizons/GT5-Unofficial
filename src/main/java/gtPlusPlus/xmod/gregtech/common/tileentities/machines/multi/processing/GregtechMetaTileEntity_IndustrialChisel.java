package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import team.chisel.carving.Carving;

public class GregtechMetaTileEntity_IndustrialChisel extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialChisel> {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialChisel> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialChisel(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialChisel(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialChisel(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Chisel";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Factory Grade Auto Chisel")
				.addInfo("Target block goes in GUI slot")
				.addInfo("If no target provided, firdt chisel result is used")
				.addInfo("Speed: 200% | Eu Usage: 75% | Parallel: Tier x 16")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front center")
				.addCasingInfo("Sturdy Printer Casing", 10)
				.addInputBus("Any casing", 1)
				.addOutputBus("Any casing", 1)
				.addEnergyHatch("Any casing", 1)
				.addMaintenanceHatch("Any casing", 1)
				.addMufflerHatch("Any casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialChisel> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialChisel>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"C~C", "C-C", "CCC"},
							{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialChisel::addAdvChiselList, 90, 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings5Misc, 5
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	public final boolean addAdvChiselList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(90), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(90)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ImplosionCompressor";
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}
	
	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private static boolean canBeMadeFrom(ItemStack from, ItemStack to) {
		List<ItemStack> results = getItemsForChiseling(from);
		for (ItemStack s : results) {
			if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
				return true;
			}
		}
		return false;
	}
	
	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private static boolean hasChiselResults(ItemStack from) {
		List<ItemStack> results = getItemsForChiseling(from);		
		return results.size() > 0;
	}
	
	private static List<ItemStack> getItemsForChiseling(ItemStack aStack){
		return Carving.chisel.getItemsForChiseling(aStack);
	}
	
	private GTPP_Recipe generateChiselRecipe(ItemStack aInput, ItemStack aTarget) {
		if (aInput != null && hasChiselResults(aInput) && aInput.stackSize > 0) {
			ItemStack tOutput = null;
			if (aTarget != null && canBeMadeFrom(aInput, aTarget)) {
				tOutput = aTarget;
			}
			else {
				tOutput = getItemsForChiseling(aInput).get(0);
			}
			if (tOutput != null) {
				// We can chisel this
				log("Generated Chisel recipe good.");
				GTPP_Recipe aRecipe = new GTPP_Recipe(
						false,
						new ItemStack[] {ItemUtils.getSimpleStack(aInput, 1)},
						new ItemStack[] {ItemUtils.getSimpleStack(tOutput, 1)},
						null, 
						new int[] {10000}, 
						new FluidStack[] {}, 
						new FluidStack[] {},
						20,
						16,
						0);
				return aRecipe;
			}
		}
		Logger.INFO("Recipe bad.");
		return null;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		ArrayList<ItemStack> aItems = this.getStoredInputs();
		if (!aItems.isEmpty()) {		

			GT_Recipe tRecipe = generateChiselRecipe(aItems.get(0), this.getGUIItemStack());
			
			if (tRecipe == null) {
				log("BAD RETURN - 0");
				return false;	
			}

			// Based on the Processing Array. A bit overkill, but very flexible.	
			ItemStack[] aItemInputs = aItems.toArray(new ItemStack[aItems.size()]);	
			FluidStack[] aFluidInputs = new FluidStack[] {};	

			// Reset outputs and progress stats
			this.mEUt = 0;
			this.mMaxProgresstime = 0;
			this.mOutputItems = new ItemStack[]{};
			this.mOutputFluids = new FluidStack[]{};

			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
			long tEnergy = getMaxInputEnergy();
			log("Running checkRecipeGeneric(0)");	

			log("Running checkRecipeGeneric(1)");
			// Remember last recipe - an optimization for findRecipe()
			this.mLastRecipe = tRecipe;

			int aMaxParallelRecipes = getMaxParallelRecipes();
			int aEUPercent = getEuDiscountForParallelism();
			int aSpeedBonusPercent = 200;

			aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
			if (aMaxParallelRecipes == 0) {
				log("BAD RETURN - 2");
				return false;
			}

			// EU discount
			float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
			float tTotalEUt = 0.0f;

			int parallelRecipes = 0;

			log("parallelRecipes: "+parallelRecipes);
			log("aMaxParallelRecipes: "+aMaxParallelRecipes);
			log("tTotalEUt: "+tTotalEUt);
			log("tVoltage: "+tVoltage);
			log("tRecipeEUt: "+tRecipeEUt);
			// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
			for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
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
			log("parallelRecipes: "+parallelRecipes);
			log("aMaxParallelRecipes: "+aMaxParallelRecipes);
			log("tTotalEUt: "+tTotalEUt);
			log("tVoltage: "+tVoltage);
			log("tRecipeEUt: "+tRecipeEUt);


			// Convert speed bonus to duration multiplier
			// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
			aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
			float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
			this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor);

			this.mEUt = (int)Math.ceil(tTotalEUt);

			this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;		

			// Overclock
			if (this.mEUt <= 16) {
				this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
				this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
			} else {
				while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
					this.mEUt *= 4;
					this.mMaxProgresstime /= 2;
				}
			}

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
						if (getBaseMetaTileEntity().getRandomNumber(10000) <= tRecipe.getOutputChance(f))
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
			updateSlots();

			// Play sounds (GT++ addition - GT multiblocks play no sounds)
			startProcess();

			log("GOOD RETURN - 1");
			return true;
		}

		return false;
	}	

	@Override
	public int getMaxParallelRecipes() {
		return (16 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 75;
	}	
	
	private static String sChiselSound = null;
	
	private static final String getChiselSound() {
		if (sChiselSound == null) {
			sChiselSound = Carving.chisel.getVariationSound(Blocks.stone, 0);
		}
		return sChiselSound;
	}

	@Override
	public String getSound() {
		return getChiselSound(); 
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiAdvImplosion;
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

}