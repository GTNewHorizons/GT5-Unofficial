package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_AlloyBlastSmelter
extends GregtechMeta_MultiBlockBase {

	private int mMode = 0;
	private boolean isUsingControllerCircuit = false;
	private static Item circuit;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> STRUCTURE_DEFINITION = null;


	public GregtechMetaTileEntity_AlloyBlastSmelter(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_AlloyBlastSmelter(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_AlloyBlastSmelter(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Fluid Alloy Cooker";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Alloy Blast Smelter")
				.addInfo("20% Faster than the Electric Blast Furnace")
				.addInfo("Allows Complex GT++ alloys to be created")
				.addInfo("Circuit for recipe goes in the Input Bus or GUI slot")
				.addPollutionAmount(getPollutionPerTick(null) * 20)
				.addSeparator()
				.beginStructureBlock(3, 4, 3, true)
				.addController("Bottom Center")
				.addCasingInfo("Blast Smelter Casings", 10)
				.addCasingInfo("Blast Smelter Heat Containment Coils", 16)
				.addInputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addOutputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher("GT++");
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_AlloyBlastSmelter>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"HHH", "H-H", "HHH"},
							{"HHH", "H-H", "HHH"},
							{"C~C", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_AlloyBlastSmelter::addAlloyBlastSmelterList, TAE.GTPP_INDEX(15), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 15
											)
									)
							)
					)
					.addElement(
							'H',
							ofBlock(
									ModBlocks.blockCasingsMisc, 14
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 3, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 3, 0) && mCasing >= 10 && mEnergyHatches.size() == 1 && checkHatch();
	}

	public final boolean addAlloyBlastSmelterList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
			}
		}
		return false;
	}


	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(208));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15))};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}	

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ElectricBlastFurnace";
	}	

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		if (this.getBaseMetaTileEntity().isServerSide()) {
			//Get Controller Circuit
			if (circuit == null) {
				circuit = CI.getNumberedCircuit(0).getItem();
			}
			if (aStack != null && aStack.getItem() == circuit) {
				this.mMode = aStack.getItemDamage();	
				return this.isUsingControllerCircuit = true;
			}
			else {
				if (aStack == null) {
					this.isUsingControllerCircuit = false;
					return true; //Allowed empty
				}
				Logger.WARNING("Not circuit in GUI inputs.");
				return this.isUsingControllerCircuit = false;
			}
		}
		Logger.WARNING("No Circuit, clientside.");
		return this.isUsingControllerCircuit = false;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {

		if (this.getBaseMetaTileEntity().isServerSide()) {
			//Get Controller Circuit
			this.isUsingControllerCircuit = isCorrectMachinePart(aStack);

			final ArrayList<ItemStack> tInputList = this.getStoredInputs();
			for (int i = 0; i < (tInputList.size() - 1); i++) {
				for (int j = i + 1; j < tInputList.size(); j++) {
					if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
						if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
							tInputList.remove(j--);
						} else {
							tInputList.remove(i--);
							break;
						}
					}
				}
			}

			//Validity check
			if ((isUsingControllerCircuit && tInputList.size() < 1) || (!isUsingControllerCircuit && tInputList.size() < 2)) {
				Logger.WARNING("Not enough inputs.");
				return false;
			}
			else if (isUsingControllerCircuit  && tInputList.size() >= 1) {
				tInputList.add(CI.getNumberedCircuit(this.mMode));
			}


			final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, tInputList.size());

			final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
			for (int i = 0; i < (tFluidList.size() - 1); i++) {
				for (int j = i + 1; j < tFluidList.size(); j++) {
					if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
						if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
							tFluidList.remove(j--);
						} else {
							tFluidList.remove(i--);
							break;
						}
					}
				}
			}
			final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
			if (tInputList.size() > 1) {
				final long tVoltage = this.getMaxInputVoltage();
				final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
				final GT_Recipe tRecipe = GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
				if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
					Logger.WARNING("Found some Valid Inputs.");
					this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
					this.mEfficiencyIncrease = 10000;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = (tRecipe.mEUt * (1 << (tTier - 1)) * (1 << (tTier - 1)));
						this.mMaxProgresstime = (tRecipe.mDuration / (1 << (tTier - 1)));
					} else {
						this.mEUt = tRecipe.mEUt;
						this.mMaxProgresstime = tRecipe.mDuration;
						while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
							this.mEUt *= 4;
							this.mMaxProgresstime /= 2;
						}
					}
					if (this.mEUt > 0) {
						this.mEUt = (-this.mEUt);
					}
					this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
					this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
					List<ItemStack> tOutPutItems = new ArrayList<ItemStack>();
					for (ItemStack tOut : tRecipe.mOutputs) {
						if (ItemUtils.checkForInvalidItems(tOut)) {
							tOutPutItems.add(tOut);
						}	
					}
					if (tOutPutItems.size() > 0)
					this.mOutputItems = tOutPutItems.toArray(new ItemStack[tOutPutItems.size()]);
					this.updateSlots();
					return true;
				}
			}
		}
		Logger.WARNING("Failed to find some Valid Inputs or Clientside.");
		return false;
	}	
	
	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 10;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 2;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
