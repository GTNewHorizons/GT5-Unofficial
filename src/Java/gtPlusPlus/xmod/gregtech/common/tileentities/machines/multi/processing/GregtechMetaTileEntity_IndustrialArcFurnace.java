package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class GregtechMetaTileEntity_IndustrialArcFurnace extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialArcFurnace> {

	//862
	private static final int mCasingTextureID = TAE.getIndexFromPage(3, 3);
	public static String mCasingName = "Tempered Arc Furnace Casing";
	private boolean mPlasmaMode = false;
	private int mSize = 0;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialArcFurnace> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialArcFurnace(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
	}

	public GregtechMetaTileEntity_IndustrialArcFurnace(final String aName) {
		super(aName);
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialArcFurnace(this.mName);
	}

	@Override
	public String getMachineType() {
		return "(Plasma/Electric) Arc Furnace";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 3);
		}
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for Industrial Arc Furnace")
				.addInfo("250% faster than using single block machines of the same voltage")
				.addInfo("Processes 8 items per voltage tier * W/L")
				.addInfo("Max Size required to process Plasma recipes")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.addController("Top center")
				.addStructureInfo("Size: nx3xn [WxHxL] (Hollow)")
				.addStructureInfo("n can be 3, 5 or 7")
				.addCasingInfo(mCasingName, 10)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addOutputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialArcFurnace> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialArcFurnace>builder()
					.addShape(mName + "3", new String[][]{
							{"CCC", "C~C", "CCC"},
							{"CCC", "C-C", "CCC"},
							{"CCC", "CCC", "CCC"},
					})
					.addShape(mName + "5", new String[][]{
							{"CCCCC", "CCCCC", "CC~CC", "CCCCC", "CCCCC"},
							{"CCCCC", "C---C", "C---C", "C---C", "CCCCC"},
							{"CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"},
					})
					.addShape(mName + "7", new String[][]{
							{"CCCCCCC", "CCCCCCC", "CCCCCCC", "CCC~CCC", "CCCCCCC", "CCCCCCC", "CCCCCCC"},
							{"CCCCCCC", "C-----C", "C-----C", "C-----C", "C-----C", "C-----C", "CCCCCCC"},
							{"CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC"},
					})
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialArcFurnace::addIndustrialArcFurnaceList, mCasingTextureID, 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings4Misc, 3
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	public void clearHatches() {
		mOutputHatches.clear();
		mInputHatches.clear();
		mOutputBusses.clear();
		mInputBusses.clear();
		mEnergyHatches.clear();
		mMaintenanceHatches.clear();
		mMufflerHatches.clear();
	}

	public final boolean addIndustrialArcFurnaceList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		int size;
		switch (stackSize.stackSize) {
			case 1: size = 3; break;
			case 2: size = 5; break;
			default: size = 7; break;
		}
		buildPiece(mName + size, stackSize, hintsOnly, (size - 1) / 2, (size - 1) / 2, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mSize = 0;
		if (checkPiece(mName + "3", 1, 1, 0)) {
			mSize = 3;
			return mCasing >= 10 && checkHatch();
		}
		mCasing = 0;
		clearHatches();
		if (checkPiece(mName + "5", 2, 2, 0)) {
			mSize = 5;
			return mCasing >= 10 && checkHatch();
		}
		mCasing = 0;
		clearHatches();
		if (checkPiece(mName + "7", 3, 3, 0)) {
			mSize = 7;
			return mCasing >= 10 && checkHatch();
		}
		return false;
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(mCasingTextureID),
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(mCasingTextureID)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialExtruder";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return mPlasmaMode ? GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes : GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return this.checkRecipeGeneric(getMaxParallelRecipes(), 100, 250);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (this.mSize * 8 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialArcFurnace;
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
		return ModBlocks.blockCasings4Misc;
	}


	public byte getCasingMeta() {
		return 3;
	}

	public Block getCasingBlock2() {
		return ModBlocks.blockCasings3Misc;
	}


	public byte getCasingMeta2() {
		return 15;
	}

	public byte getCasingTextureIndex() {
		return (byte) mCasingTextureID;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (this.mSize > 5) {
			this.mPlasmaMode = Utils.invertBoolean(mPlasmaMode);
			if (mPlasmaMode) {
				PlayerUtils.messagePlayer(aPlayer, "["+EnumChatFormatting.RED+"MODE"+EnumChatFormatting.RESET+"] "+EnumChatFormatting.LIGHT_PURPLE+"Plasma"+EnumChatFormatting.RESET);
			}
			else {
				PlayerUtils.messagePlayer(aPlayer, "["+EnumChatFormatting.RED+"MODE"+EnumChatFormatting.RESET+"] "+EnumChatFormatting.YELLOW+"Electric"+EnumChatFormatting.RESET);			
			}
		}	
		else {
			PlayerUtils.messagePlayer(aPlayer, "["+EnumChatFormatting.RED+"MODE"+EnumChatFormatting.RESET+"] "+EnumChatFormatting.GRAY+"Cannot change mode, structure not large enough."+EnumChatFormatting.RESET);
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mPlasmaMode", mPlasmaMode);
		aNBT.setInteger("mSize", mSize);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mPlasmaMode = aNBT.getBoolean("mPlasmaMode");
		mSize = aNBT.getInteger("mSize");
	}

	@Override
	public void onMachineBlockUpdate() {
		mUpdate = 100;
	}
}
