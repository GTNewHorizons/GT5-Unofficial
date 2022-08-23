package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;


import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_Adv_DistillationTower extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Adv_DistillationTower> implements ISurvivalConstructable {

	private byte mMode = 0;
	private boolean mUpgraded = false;

	protected static final String STRUCTURE_PIECE_BASE = "base";
	protected static final String STRUCTURE_PIECE_LAYER = "layer";
	protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
	protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";

	protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
	protected int mHeight;
	protected int mCasing;
	protected boolean mTopLayerFound;

	private static IStructureDefinition<GregtechMetaTileEntity_Adv_DistillationTower> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_Adv_DistillationTower(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Adv_DistillationTower(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_DistillationTower(this.mName);
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_Adv_DistillationTower> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			IHatchElement<GregtechMetaTileEntity_Adv_DistillationTower> layeredOutputHatch = OutputHatch
					.withCount(GregtechMetaTileEntity_Adv_DistillationTower::getCurrentLayerOutputHatchCount)
					.withAdder(GregtechMetaTileEntity_Adv_DistillationTower::addLayerOutputHatch);
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Adv_DistillationTower>builder()
					.addShape(STRUCTURE_PIECE_BASE, transpose(new String[][]{
							{"b~b", "bbb", "bbb"},
					}))
					.addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][]{
							{"lll", "lcl", "lll"}
					}))
					.addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][]{
							{"lll", "l-l", "lll"}
					}))
					.addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][]{
							{"ttt", "ttt", "ttt"}
					}))
					.addElement('b', ofChain(
							buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
									.atLeast(Energy, OutputBus, InputHatch, InputBus, Maintenance)
									.casingIndex(getCasingTextureID())
									.dot(1)
									.build(),
							ofBlock(GregTech_API.sBlockCasings4, 1)
					))
					.addElement('l', ofChain(
							buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
									.atLeast(layeredOutputHatch)
									.casingIndex(getCasingTextureID())
									.dot(2)
									.build(),
							ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addMufflerToMachineList, getCasingTextureID(), 2),
							ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addEnergyInputToMachineList, getCasingTextureID(), 2),
							ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addLayerOutputHatch, getCasingTextureID(), 2),
							ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addMaintenanceToMachineList, getCasingTextureID(), 2),
							ofBlock(GregTech_API.sBlockCasings4, 1)
					))
					.addElement('c', ofChain(
							onElementPass(GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound, ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addMufflerToMachineList, getCasingTextureID(), 3)),
							onElementPass(GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound, ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addOutputToMachineList, getCasingTextureID(), 3)),
							onElementPass(GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound, ofHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower::addMaintenanceToMachineList, getCasingTextureID(), 3)),
							onElementPass(GregtechMetaTileEntity_Adv_DistillationTower::onTopLayerFound, ofBlock(GregTech_API.sBlockCasings4, 1)),
							isAir()
					))
					.addElement('t', buildHatchAdder(GregtechMetaTileEntity_Adv_DistillationTower.class)
							.atLeast(layeredOutputHatch, Muffler)
							.casingIndex(getCasingTextureID())
							.dot(2)
							.buildAndChain(GregTech_API.sBlockCasings4, 1)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}


	protected int getCurrentLayerOutputHatchCount() {
		return mOutputHatchesByLayer.size() < mHeight ? 0 : mOutputHatchesByLayer.get(mHeight - 1).size();
	}

	protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null || aTileEntity.isDead() || !(aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output))
			return false;
		while (mOutputHatchesByLayer.size() < mHeight)
			mOutputHatchesByLayer.add(new ArrayList<>());
		GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aTileEntity.getMetaTileEntity();
		tHatch.updateTexture(aBaseCasingIndex);
		return mOutputHatchesByLayer.get(mHeight - 1).add(tHatch);
	}

	protected void onTopLayerFound() {
		mTopLayerFound = true;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Advanced Distillation Tower")
				.addInfo("250% faster than a standard DT in DT mode")
				.addInfo("T1 and T2 constructed identical to standard DT")
				.addInfo("Right click the controller with screwdriver to change mode.")
				.addInfo("Max parallel dictated by tower tier and mode")
				.addInfo("DTower Mode: T1=4, T2=12")
				.addInfo("Distilery Mode: Tower Tier * (4*InputTier)")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.addCasingInfo("Clean Stainless Steel Machine Casing", 7)
				.addInputBus("Bottom Casing", 1)
				.addOutputBus("Bottom Casing", 1)
				.addInputHatch("Bottom Casing", 1)
				.addMaintenanceHatch("Bottom Casing", 1)
				.addEnergyHatch("Bottom Casing", 1)
				.addOutputHatch("One per layer except bottom", 2)
				.addMufflerHatch("Top Casing", 3)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
		int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
		for (int i = 1; i < tTotalHeight - 1; i++) {
			buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
		}
		buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
	}

	@Override
	public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
		mHeight = 0;
		int built = survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 1, 0, 0, elementBudget, source, actor, false, true);
		if (built >= 0) return built;
		int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
		for (int i = 1; i < tTotalHeight - 1; i++) {
			mHeight = i;
			built = survivialBuildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, 1, i, 0, elementBudget, source, actor, false, true);
			if (built >= 0) return built;
		}
		mHeight = tTotalHeight;
		return survivialBuildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, 1, tTotalHeight - 1, 0, elementBudget, source, actor, false, true);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		// reset
		mOutputHatchesByLayer.forEach(List::clear);
		mHeight = 1;
		mTopLayerFound = false;

		// check base
		if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0))
			return false;

		// check each layer
		while (mHeight < 12) {
			if (!checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0)) {
				return false;
			}
			if (mOutputHatchesByLayer.size() < mHeight || mOutputHatchesByLayer.get(mHeight - 1).isEmpty())
				// layer without output hatch
				return false;
			if (mTopLayerFound || !mMufflerHatches.isEmpty()) {
				break;
			}
			// not top
			mHeight++;
		}
		return mTopLayerFound && mHeight >= 2 && checkHatch();
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName()+(mUpgraded ? " T2" : ""), "MultiblockDisplay.png");
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return mMode == 0 ? GT_Recipe.GT_Recipe_Map.sDistillationRecipes : GT_Recipe.GT_Recipe_Map.sDistilleryRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	protected IAlignmentLimits getInitialAlignmentLimits() {
		// don't rotate a freaking tower, it won't work
		return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(ItemStack aStack) {
		if (this.mMode == 1) return CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
		return CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDT;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mMode", mMode);
		aNBT.setInteger("mCasingTier", this.mCasingTier);
		aNBT.setBoolean("mUpgraded", mUpgraded);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mMode = aNBT.getByte("mMode");
		mCasingTier = aNBT.getInteger("mCasingTier");
		mUpgraded = aNBT.getBoolean("mUpgraded");
		super.loadNBTData(aNBT);
	}	

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(203));
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mMode++;		
		if (mMode > 1){
			mMode = 0;
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillation Tower Mode.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillery Mode.");
		}		
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	private short getControllerY() {
		return getBaseMetaTileEntity().getYCoord();
	}

	@Override
	public boolean addOutput(FluidStack aLiquid) {
		if (aLiquid == null) return false;
		FluidStack tLiquid = aLiquid.copy();
		for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
			if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam() : tHatch.outputsLiquids()) {
				if (tHatch.getBaseMetaTileEntity().getYCoord() == getControllerY() + 1) {
					int tAmount = tHatch.fill(tLiquid, false);
					if (tAmount >= tLiquid.amount) {
						return tHatch.fill(tLiquid, true) >= tLiquid.amount;
					} else if (tAmount > 0) {
						tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
		for (int i = 0; i < mOutputFluids2.length; i++) {
			if (mOutputHatches.size() > i && mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity(mOutputHatches.get(i))) {
				if (mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord() == getControllerY() + 1 + i) {
					mOutputHatches.get(i).fill(mOutputFluids2[i], true);
				}
			}
		}

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
		return "DistillationTower";
	}

	@Override
	public String getMachineType() {
		return "Distillery, Distillation Tower";
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {		
		// Run standard recipe handling for distillery recipes
		if (mMode == 1) {
			return this.checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 100);
		}
		else {
			ItemStack[] inputs = getCompactedInputs();
	
			for (GT_MetaTileEntity_Hatch_Input hatch : mInputHatches) {
				FluidStack tFluid = hatch.getFluid();
				if (tFluid != null) {
					int para = (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
					if (checkRecipeGeneric(inputs, new FluidStack[]{tFluid}, para, 100, 250, 10000)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public int getMaxParallelRecipes() {		
		if (this.mMode == 0) {
			return getTierOfTower() == 1 ? 4 : getTierOfTower() == 2 ? 12 : 0;			
		}
		else if (this.mMode == 1) {
			return getTierOfTower() * (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
		}		
		return 0;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 15;
	}

	private int getTierOfTower() {
		return mUpgraded ? 2 : 1;
	}

	private int mCasingTier = 0;

	private int getMachineCasingTier() {
		return mCasingTier;
	}

	@Override
	protected IIconContainer getActiveOverlay() {
		return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
	}

	@Override
	protected IIconContainer getInactiveOverlay() {
		return Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
	}

	@Override
	protected int getCasingTextureId() {
		if (mCasingTier == 0) {
			return 49;
		}
		else if (mCasingTier == 1) {
			return 43;
		}
		else {
			return 49;
		}
	}

	private int getCasingTextureID() {
		// Check the Tier Client Side
		int aTier = mCasingTier;		

		if (aTier == 1) {
			return 49;
		}
		else if (aTier == 2) {
			return 43;
		}
		else {
			return 49;
		}
	}

	public boolean addToMachineList(IGregTechTileEntity aTileEntity) {		
		int aMaxTier = getMachineCasingTier();		
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();		
		if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
			GT_MetaTileEntity_TieredMachineBlock aMachineBlock = (GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity;
			int aTileTier = aMachineBlock.mTier;
			if (aTileTier > aMaxTier) {
				Logger.INFO("Hatch tier too high.");
				return false;
			}
			else {
				return addToMachineList(aTileEntity, getCasingTextureID());
			}
		}
		else {
			Logger.INFO("Bad Tile Entity being added to hatch map."); // Shouldn't ever happen, but.. ya know..
			return false;
		}		
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aTick % 20 == 0 && !mUpgraded) {
			ItemStack aGuiStack = this.getGUIItemStack();
			if (aGuiStack != null) {
				if (GT_Utility.areStacksEqual(aGuiStack, GregtechItemList.Distillus_Upgrade_Chip.get(1))) {
					this.mUpgraded = true;
					ItemUtils.depleteStack(aGuiStack);
				}
			}
		}
		// Silly Client Syncing
		if (aBaseMetaTileEntity.isClientSide()) {
			this.mCasingTier = getCasingTierOnClientSide();
		}
	}



	@SideOnly(Side.CLIENT)
	private final int getCasingTierOnClientSide() {
		if (this == null || this.getBaseMetaTileEntity().getWorld() == null) {
			return 0;
		}
		try {
			Block aInitStructureCheck;
			int aInitStructureCheckMeta;
			IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();	
			if (aBaseMetaTileEntity == null || aBaseMetaTileEntity.getWorld() == null || aBaseMetaTileEntity.getWorld().getChunkFromBlockCoords(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getZCoord()) == null) {
				return 0;
			}
			for (int i=1;i<10;i++) {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, i, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, i, 0);	
				if (aInitStructureCheck == null) {
					continue;
				}
				if (aInitStructureCheck == GregTech_API.sBlockCasings4 && aInitStructureCheckMeta == 1) {
					return 0;
				}
				else if (aInitStructureCheck == ModBlocks.blockCasingsTieredGTPP) {
					return 1;
				}				
			}
		}
		catch (Throwable t) {
			//t.printStackTrace();
		}
		return 0;

	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		aNBT.setBoolean("mUpgraded", mUpgraded);
		super.setItemNBT(aNBT);
	}
}