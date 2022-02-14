package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

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
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_IndustrialFluidHeater extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialFluidHeater> {

	private int mCasing1;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialFluidHeater> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialFluidHeater(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialFluidHeater(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialFluidHeater(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Fluid Heater";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Industrial Fluid Heater")
				.addInfo("120% faster than using single block machines of the same voltage")
				.addInfo("Only uses 90% of the eu/t normally required")
				.addInfo("Processes eight items per voltage tier")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(5, 6, 5, true)
				.addController("Front Center")
				.addCasingInfo("Top/Bottom layer: Multi-use Casings", 34)
				.addCasingInfo("Middle layers: Thermal Containment Casing", 47)
				.addInputBus("Bottom Layer (optional)", 1)
				.addInputHatch("Bottom Layer", 1)
				.addOutputBus("Top Layer (optional)", 1)
				.addOutputHatch("Top Layer", 1)
				.addEnergyHatch("Any Multi-use Casing", 1)
				.addMaintenanceHatch("Any Multi-use Casing", 1)
				.addMufflerHatch("Any Multi-use Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialFluidHeater> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialFluidHeater>builder()
					.addShape(mName, transpose(new String[][]{
							{" TTT ", "TTTTT", "TTTTT", "TTTTT", " TTT "},
							{" XXX ", "X---X", "X---X", "X---X", " XXX "},
							{" XXX ", "X---X", "X---X", "X---X", " XXX "},
							{" XXX ", "X---X", "X---X", "X---X", " XXX "},
							{" X~X ", "X---X", "X---X", "X---X", " XXX "},
							{" BBB ", "BBBBB", "BBBBB", "BBBBB", " BBB "},
					}))
					
					.addElement('B', ofChain(
									ofHatchAdder(GregtechMetaTileEntity_IndustrialFluidHeater::addIndustrialFluidHeaterInputList, getCasingTextureIndex(), 1),
									onElementPass(x -> ++x.mCasing1, ofBlock(getCasingBlock2(), getCasingMeta2()))))
					
					.addElement('X', ofBlock(getCasingBlock1(), getCasingMeta1()))
					
					.addElement('T', ofChain(
							ofHatchAdder(GregtechMetaTileEntity_IndustrialFluidHeater::addIndustrialFluidHeaterOutputList, getCasingTextureIndex(), 1),
							onElementPass(x -> ++x.mCasing1, ofBlock(getCasingBlock2(), getCasingMeta2()))))
					
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 2, 4, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing1 = 0;
		boolean didBuild = checkPiece(mName, 2, 4, 0);
		log("Built? "+didBuild+", "+mCasing1);
		return didBuild && mCasing1 >= 34 && checkHatch();
	}

	public final boolean addIndustrialFluidHeaterInputList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}
	
	public final boolean addIndustrialFluidHeaterOutputList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} 
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 1)), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 1))};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialThermalCentrifuge";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 120);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 90;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialThermalCentrifuge;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock1() {
		return ModBlocks.blockCasings2Misc;
	}

	public byte getCasingMeta1() {
		return 11;
	}
	
	public Block getCasingBlock2() {
		return ModBlocks.blockCasings3Misc;
	}

	public byte getCasingMeta2() {
		return 2;
	}

	public byte getCasingTextureIndex() {
		return (byte) TAE.getIndexFromPage(2, 2);
	}

}