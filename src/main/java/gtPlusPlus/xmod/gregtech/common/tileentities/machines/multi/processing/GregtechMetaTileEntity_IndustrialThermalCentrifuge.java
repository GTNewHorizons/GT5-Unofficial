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

public class GregtechMetaTileEntity_IndustrialThermalCentrifuge extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialThermalCentrifuge> {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialThermalCentrifuge> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialThermalCentrifuge(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialThermalCentrifuge(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Thermal Centrifuge";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Industrial Thermal Centrifuge")
				.addInfo("150% faster than using single block machines of the same voltage")
				.addInfo("Only uses 80% of the eu/t normally required")
				.addInfo("Processes eight items per voltage tier")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 2, 3, false)
				.addController("Front Center")
				.addCasingInfo("Thermal Processing Casings/Noise Hazard Sign Blocks", 8)
				.addInputBus("Bottom Casing", 1)
				.addOutputBus("Bottom Casing", 1)
				.addEnergyHatch("Bottom Casing", 1)
				.addMaintenanceHatch("Bottom Casing", 1)
				.addMufflerHatch("Bottom Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialThermalCentrifuge> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialThermalCentrifuge>builder()
					.addShape(mName, transpose(new String[][]{
							{"X~X", "XXX", "XXX"},
							{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialThermalCentrifuge::addIndustrialThermalCentrifugeList, getCasingTextureIndex(), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings2Misc, 0
											)
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													GregTech_API.sBlockCasings3, 9
											)
									)
							)
					)
					.addElement(
							'X',
							ofChain(
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings2Misc, 0
											)
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													GregTech_API.sBlockCasings3, 9
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 0, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 0, 0) && mCasing >= 8 && checkHatch();
	}

	public final boolean addIndustrialThermalCentrifugeList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex())};
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
		return GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((8* GT_Utility.getTier(this.getMaxInputVoltage())), 80, 150);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 80;
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

	public Block getCasingBlock() {
		return ModBlocks.blockCasings2Misc;
	}

	public byte getCasingMeta() {
		return 0;
	}

	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(16);
	}

}