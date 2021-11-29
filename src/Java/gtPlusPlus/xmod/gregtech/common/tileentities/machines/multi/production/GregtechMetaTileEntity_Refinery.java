package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.item.ItemStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_Refinery extends GregtechMeta_MultiBlockBase {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_Refinery> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_Refinery(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Refinery(final String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Fuel Refinery";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Fission Fuel Processing Unit")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 9, 3, false)
				.addController("Bottom Center")
				.addCasingInfo("Hastelloy-X Structural Casing", 7)
				.addCasingInfo("Incoloy-DS Fluid Containment Block", 5)
				.addCasingInfo("Zeron-100 Reactor Shielding", 4)
				.addCasingInfo("Hastelloy-N Sealant Blocks", 17)
				.addInputHatch("Base platform", 1)
				.addOutputHatch("Base platform", 1)
				.addOutputBus("Base platform", 1)
				.addMufflerHatch("Base platform", 1)
				.addMaintenanceHatch("Base platform", 1)
				.addEnergyHatch("Base platform", 1)
				.addStructureInfo("Muffler's Tier must be ZPM/ZPM+")
				.addStructureInfo("4x Input Hatches, 2x Output Hatches, 1x Output Bus")
				.addStructureInfo("1x Muffler, 1x Maintenance Hatch, 1x Energy Hatch")
				.toolTipFinisher("GT++");
		return tt;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(18)), new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(18))};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "MatterFabricator";
	}	
	
	@Override
	public GT_Recipe_Map getRecipeMap() {		
		return GTPP_Recipe.GTPP_Recipe_Map.sFissionFuelProcessing;
	}

	@Override
    public boolean checkRecipe(ItemStack aStack) {	
		//this.resetRecipeMapForAllInputHatches();		
		for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
			g.mRecipeMap = null;
		}		
		boolean ab = super.checkRecipeGeneric();
		//Logger.INFO("Did Recipe? "+ab);
		return ab;
    }	
	
	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	public final boolean addRefineryList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler && ((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity).mTier >= 7) {
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
	public IStructureDefinition<GregtechMetaTileEntity_Refinery> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Refinery>builder()
					.addShape(mName, transpose(new String[][]{
							{"   ", " N ", "   "},
							{" N ", "NIN", " N "},
							{" N ", "NIN", " N "},
							{" N ", "NIN", " N "},
							{" Z ", "ZIZ", " Z "},
							{" N ", "NIN", " N "},
							{"XXX", "XXX", "XXX"},
							{"X~X", "XXX", "XXX"},
					}))
					.addElement(
							'X',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_Refinery::addRefineryList, TAE.GTPP_INDEX(18), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings2Misc, 2
											)
									)
							)
					)
					.addElement(
							'I',
							ofBlock(
									ModBlocks.blockCasings2Misc, 3
							)
					)
					.addElement(
							'N',
							ofBlock(
									ModBlocks.blockCasings2Misc, 1
							)
					)
					.addElement(
							'Z',
							ofBlock(
									ModBlocks.blockCasingsMisc, 13
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 7, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		if (checkPiece(mName, 1, 7, 0) && mCasing >= 7) {
			if (this.mInputHatches.size() == 4 && this.mOutputHatches.size() == 2 &&
					this.mOutputBusses.size() == 1 && this.mMufflerHatches.size() == 1 &&
					this.mMaintenanceHatches.size() == 1 && this.mEnergyHatches.size() == 1) {
				this.resetRecipeMapForAllInputHatches(this.getRecipeMap());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiRefinery;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 5;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Refinery(this.mName);
	}

}
