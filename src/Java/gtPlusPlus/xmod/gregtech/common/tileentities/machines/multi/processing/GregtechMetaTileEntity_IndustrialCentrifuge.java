package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
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
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_IndustrialCentrifuge
extends GregtechMeta_MultiBlockBase {
	
	private boolean mIsAnimated;
	private static ITexture frontFace;
	private static ITexture frontFaceActive;
	private static CustomIcon GT9_5_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
	private static CustomIcon GT9_5 = new CustomIcon("iconsets/LARGECENTRIFUGE5");
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialCentrifuge> STRUCTURE_DEFINITION = null;
	//public static double recipesComplete = 0;

	public GregtechMetaTileEntity_IndustrialCentrifuge(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		frontFaceActive = new GT_RenderedTexture(GT9_5_Active);
		frontFace = new GT_RenderedTexture(GT9_5);
		mIsAnimated = true;
	}

	public GregtechMetaTileEntity_IndustrialCentrifuge(final String aName) {
		super(aName);
		mIsAnimated = true;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCentrifuge(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Centrifuge";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Industrial Centrifuge")
				.addInfo("125% faster than using single block machines of the same voltage")
				.addInfo("Disable animations with a screwdriver")
				.addInfo("Only uses 90% of the eu/t normally required")
				.addInfo("Processes six items per voltage tier")
				.addPollutionAmount(getPollutionPerTick(null) * 20)
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front Center")
				.addCasingInfo("Centrifuge Casings", 10)
				.addInputBus("Any Casing except front", 1)
				.addOutputBus("Any Casing except front", 1)
				.addInputHatch("Any Casing except front", 1)
				.addOutputHatch("Any Casing except front", 1)
				.addEnergyHatch("Any Casing except front", 1)
				.addMaintenanceHatch("Any Casing except front", 1)
				.addMufflerHatch("Any Casing except front", 1)
				.toolTipFinisher("GT++");
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialCentrifuge> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialCentrifuge>builder()
					.addShape(mName, transpose(new String[][]{
							{"XXX", "CCC", "CCC"},
							{"X~X", "C-C", "CCC"},
							{"XXX", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialCentrifuge::addIndustrialCentrifugeList, getCasingTextureIndex(), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 0
											)
									)
							)
					)
					.addElement(
							'X',
							ofBlock(
									ModBlocks.blockCasingsMisc, 0
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 - 8 && checkHatch();
	}

	public final boolean addIndustrialCentrifugeList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
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
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(0)), aFacing == aSide ? aActive ? getFrontFacingTurbineTexture(aActive) : getFrontFacingTurbineTexture(aActive) : Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(0))};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialCentrifuge";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(6* GT_Utility.getTier(this.getMaxInputVoltage()), 90, 125);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (6 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 90;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasingsMisc;
	}

	public byte getCasingMeta() {
		return 0;
	}

	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(0);
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 15;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		this.mIsAnimated = !mIsAnimated;
		Logger.INFO("Is Centrifuge animated "+this.mIsAnimated);	
		if (this.mIsAnimated) {
			PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture. ");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture. ");			
		}		
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mIsAnimated", mIsAnimated);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);		
		if (aNBT.hasKey("mIsAnimated")) {
			mIsAnimated = aNBT.getBoolean("mIsAnimated");			
		}
		else {
			mIsAnimated = true;			
		}		
	}
	
	public boolean usingAnimations() {
		//Logger.INFO("Is animated? "+this.mIsAnimated);
		return this.mIsAnimated;
	}
	
	private ITexture getFrontFacingTurbineTexture(boolean isActive) {
		if (usingAnimations()) {
			if (isActive) {
				return frontFaceActive;
			}
		}
		return frontFace;
	}

}