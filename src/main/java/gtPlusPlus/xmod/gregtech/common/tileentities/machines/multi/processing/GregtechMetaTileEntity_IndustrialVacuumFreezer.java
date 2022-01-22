package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GT_MetaTileEntity_Hatch_CustomFluidBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_IndustrialVacuumFreezer extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialVacuumFreezer> {

	public static int CASING_TEXTURE_ID;
	public static String mCryoFuelName = "Gelid Cryotheum";
	public static String mCasingName = "Advanced Cryogenic Casing";
	public static String mHatchName = "Cryotheum Hatch";
	public static FluidStack mFuelStack;
	private boolean mHaveHatch;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialVacuumFreezer> STRUCTURE_DEFINITION = null;


	public GregtechMetaTileEntity_IndustrialVacuumFreezer(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
	}

	public GregtechMetaTileEntity_IndustrialVacuumFreezer(final String aName) {
		super(aName);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GregtechMetaTileEntity_IndustrialVacuumFreezer(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Vacuum Freezer";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Factory Grade Advanced Vacuum Freezer")
				.addInfo("Speed: 200% | Eu Usage: 100% | Parallel: 4")
				.addInfo("Consumes 1L of " + mCryoFuelName + "/t during operation")
				.addInfo("Constructed exactly the same as a normal Vacuum Freezer")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front Center")
				.addCasingInfo(mCasingName, 10)
				.addStructureHint(mHatchName, 1)
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
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialVacuumFreezer> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialVacuumFreezer>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"C~C", "C-C", "CCC"},
							{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialVacuumFreezer::addIndustrialVacuumFreezerList, CASING_TEXTURE_ID, 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings3Misc, 10
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
		buildPiece(mName , stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mHaveHatch = false;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && mHaveHatch && checkHatch();
	}

	public final boolean addIndustrialVacuumFreezerList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CustomFluidBase && aMetaTileEntity.getBaseMetaTileEntity().getMetaTileID() == 967) {
				mHaveHatch = true;
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
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


	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
					new GT_RenderedTexture((IIconContainer) (aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced))};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
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
		return "VacuumFreezer";
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {				
		return GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes_GT;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		return this.checkRecipeGeneric(4, 100, 100);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return 4;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialVacuumFreezer;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	private volatile int mGraceTimer = 2;

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		/*if (this.getBaseMetaTileEntity().isActive()) {
			if (!this.depleteInput(mFuelStack.copy())) {
				this.getBaseMetaTileEntity().setActive(false);
			}
		}	*/
		super.onPostTick(aBaseMetaTileEntity, aTick);

		if (this.mStartUpCheck < 0) {
			if (this.mMaxProgresstime > 0 && this.mProgresstime != 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {			
				if (aTick % 10 == 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {
					if (!this.depleteInput(FluidUtils.getFluidStack("cryotheum", 10))) {						
						if (mGraceTimer-- == 0) {
							this.causeMaintenanceIssue();
							this.stopMachine();
							mGraceTimer = 2;
						}						
					}
				}			
			}
		}
	}
}