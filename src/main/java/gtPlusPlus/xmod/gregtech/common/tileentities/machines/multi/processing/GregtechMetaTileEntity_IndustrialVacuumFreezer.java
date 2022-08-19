package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GT_MetaTileEntity_Hatch_CustomFluidBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;


import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

public class GregtechMetaTileEntity_IndustrialVacuumFreezer extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialVacuumFreezer> implements ISurvivalConstructable {

	public static int CASING_TEXTURE_ID;
	public static String mCryoFuelName = "Gelid Cryotheum";
	public static String mCasingName = "Advanced Cryogenic Casing";
	public static String mHatchName = "Cryotheum Hatch";
	public static FluidStack mFuelStack;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialVacuumFreezer> STRUCTURE_DEFINITION = null;

	private final ArrayList<GT_MetaTileEntity_Hatch_CustomFluidBase> mCryotheumHatches = new ArrayList<>();


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
									buildHatchAdder(GregtechMetaTileEntity_IndustrialVacuumFreezer.class)
											.adder(GregtechMetaTileEntity_IndustrialVacuumFreezer::addCryotheumHatch)
											.hatchId(967)
											.shouldReject(t -> !t.mCryotheumHatches.isEmpty())
											.casingIndex(CASING_TEXTURE_ID)
											.dot(1)
											.build(),
									buildHatchAdder(GregtechMetaTileEntity_IndustrialVacuumFreezer.class)
											.atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
											.casingIndex(CASING_TEXTURE_ID)
											.dot(1)
											.build(),
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
	public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
		if (mMachine) return -1;
		return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, source, actor, false, true);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mCryotheumHatches.clear();
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
	}

	@Override
	public boolean checkHatch() {
		return super.checkHatch() && !mCryotheumHatches.isEmpty();
	}

	private boolean depleteFuel(int aAmount) {
		for (final GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : this.mCryotheumHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				FluidStack tLiquid = tHatch.getFluid();
				if (tLiquid == null || tLiquid.amount < aAmount) {
					continue;
				}
				tLiquid = tHatch.drain(aAmount, false);
				if (tLiquid != null && tLiquid.amount >= aAmount) {
					tLiquid = tHatch.drain(aAmount, true);
					return tLiquid != null && tLiquid.amount >= aAmount;
				}
			}
		}
		return false;
	}

	private boolean addCryotheumHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CustomFluidBase && aMetaTileEntity.getBaseMetaTileEntity().getMetaTileID() == 967) {
				return addToMachineListInternal(mCryotheumHatches, aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public void updateSlots() {
		for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mCryotheumHatches)
			if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
		super.updateSlots();
	}

	@Override
	protected IIconContainer getActiveOverlay() {
		return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
	}

	@Override
	protected IIconContainer getInactiveOverlay() {
		return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
	}

	@Override
	protected int getCasingTextureId() {
		return CASING_TEXTURE_ID;
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

	private int mGraceTimer = 2;

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
					if (!this.depleteFuel(10)) {
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