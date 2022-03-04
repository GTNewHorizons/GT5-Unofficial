package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

import java.util.function.Predicate;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_PowerSubStation;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_PowerSubStation;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class GregtechMetaTileEntity_PowerSubStationController extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_PowerSubStationController> {

	private static enum TopState {
		MayBeTop,
		Top,
		NotTop
	}

	protected long mAverageEuUsage = 0;
	protected long mAverageEuAdded = 0;
	protected long mAverageEuConsumed = 0;
	protected long mTotalEnergyAdded = 0;
	protected long mTotalEnergyConsumed = 0;
	protected long mTotalEnergyLost = 0;
	protected boolean mIsOutputtingPower = false;
	protected long mBatteryCapacity = 0;

	private final int ENERGY_TAX = 2;

	private int mCasing;
	private int[] cellCount = new int[6];
	private TopState topState = TopState.MayBeTop;
	private static IStructureDefinition<GregtechMetaTileEntity_PowerSubStationController> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_PowerSubStationController(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_PowerSubStationController(final String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Energy Buffer";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Consumes " + this.ENERGY_TAX + "% of the average voltage of all energy type hatches")
		.addInfo("Does not require maintenance")
		.addInfo("Can be built with variable height between " + (CELL_HEIGHT_MIN + 2) + "-" + (CELL_HEIGHT_MAX + 2) + "")
		.addInfo("Hatches can be placed nearly anywhere")
		.addInfo("HV Energy/Dynamo Hatches are the lowest tier you can use")
		.addInfo("Supports voltages >= UHV using MAX tier components.")
		.addSeparator()
		.addController("Bottom Center")
		.addCasingInfo("Sub-Station External Casings", 10)
		.addDynamoHatch("Any Casing", 1)
		.addEnergyHatch("Any Casing", 1)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(24)),
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER)};
		}
		if (aSide == this.getBaseMetaTileEntity().getBackFacing()) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(24)),
					mIsOutputtingPower ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[(int) this.getOutputTier()] : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[(int) this.getInputTier()]};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(23))};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		//if (mBatteryCapacity <= 0) return false;
		if (!aBaseMetaTileEntity.isClientSide()) {
			aBaseMetaTileEntity.openGUI(aPlayer);
		}
		return true;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_PowerSubStation(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "Ergon Energy - Sub Station");
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_PowerSubStation(aPlayerInventory, aBaseMetaTileEntity);
	}

	private void checkMachineProblem(String msg, int xOff, int yOff, int zOff) {
		final IGregTechTileEntity te = this.getBaseMetaTileEntity();
		final Block tBlock = te.getBlockOffset(xOff, yOff, zOff);
		final byte tMeta = te.getMetaIDOffset(xOff, yOff, zOff);
		String name = tBlock.getLocalizedName();
		String problem = msg + ": (" + xOff + ", " + yOff + ", " + zOff + ") " + name + ":" + tMeta;
		checkMachineProblem(problem);
	}
	private void checkMachineProblem(String msg) {
		if (!AsmConfig.disableAllLogging) {
			Logger.INFO("Power Sub-Station problem: " + msg);			
		}
	}

	public static int getCellTier(Block aBlock, int aMeta) {
		if (aBlock == ModBlocks.blockCasings2Misc && aMeta == 7) {
			return 4;
		} else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 4) {
			return 5;
		} else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 5) {
			return 6;
		} else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 6) {
			return 7;
		} else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 7) {
			return 8;
		} else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 8) {
			return 9;
		} else {
			return -1;
		}
	}

	public static int getMetaFromTier(int tier) {
		if (tier == 4) return 7;
		if (tier >= 5 && tier <= 9) return tier - 1;
		return 0;
	}

	public static Block getBlockFromTier(int tier) {
		switch (tier) {
			case 4: return ModBlocks.blockCasings2Misc;
			case 5: case 6: case 7: case 8: case 9: return ModBlocks.blockCasings3Misc;
			default: return null;
		}
	}

	public static int getMaxHatchTier(int aCellTier) {
		switch(aCellTier) {
			case 9:
				return GT_Values.VOLTAGE_NAMES[9].equals("Ultimate High Voltage") ? 15 : 9;
			default:
				if (aCellTier < 4) {
					return 0;
				}
				else {
					return aCellTier;
				}
		}
	}

	public static final int CELL_HEIGHT_MAX = 16;
	public static final int CELL_HEIGHT_MIN = 2;

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_PowerSubStationController> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_PowerSubStationController>builder()
					.addShape(mName + "bottom", transpose(new String[][]{
						{"CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"}
					}))
					.addShape(mName + "layer", transpose(new String[][]{
						{"CCCCC", "CIIIC", "CIIIC", "CIIIC", "CCCCC"}
					}))
					.addShape(mName + "mid", transpose(new String[][]{
						{"CCCCC", "CHHHC", "CHHHC", "CHHHC", "CCCCC"}
					}))
					.addShape(mName + "top", transpose(new String[][]{
						{"CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"}
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_PowerSubStationController::addPowerSubStationList, TAE.GTPP_INDEX(24), 1
											),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings2Misc, 8
													)
											)
									)
							)
					.addElement(
							'I',
							ofChain(
									onlyIf(
											x -> x.topState != TopState.NotTop,
											onElementPass(
													x -> x.topState = TopState.Top,
													ofHatchAdderOptional(GregtechMetaTileEntity_PowerSubStationController::addPowerSubStationList, TAE.GTPP_INDEX(24), 1, ModBlocks.blockCasings2Misc, 8)
													)
											),
									onlyIf(
											x -> x.topState != TopState.Top,
											onElementPass(
													x -> x.topState = TopState.NotTop,
													ofChain(
															onElementPass(
																	x -> ++x.cellCount[0],
																	ofCell(4)
																	),
															onElementPass(
																	x -> ++x.cellCount[1],
																	ofCell(5)
																	),
															onElementPass(
																	x -> ++x.cellCount[2],
																	ofCell(6)
																	),
															onElementPass(
																	x -> ++x.cellCount[3],
																	ofCell(7)
																	),
															onElementPass(
																	x -> ++x.cellCount[4],
																	ofCell(8)
																	),
															onElementPass(
																	x -> ++x.cellCount[5],
																	ofCell(9)
																	)
															)
													)
											)
									)
							)
					.addElement('H', ofCell(4))
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	public static <T> IStructureElement<T> ofCell(int aIndex) {
		return new IStructureElement<T>() {
			@Override
			public boolean check(T t, World world, int x, int y, int z) {
				Block block = world.getBlock(x, y, z);
				int meta = world.getBlockMetadata(x, y, z);
				int tier = getCellTier(block, meta);
				return aIndex == tier;
			}

			public int getIndex(int size) {
				if (size > 6) size = 6;
				return size + 3;
			}

			@Override
			public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
				StructureLibAPI.hintParticle(world, x, y, z, getBlockFromTier(getIndex(trigger.stackSize)), getMetaFromTier(getIndex(trigger.stackSize)));
				return true;
			}

			@Override
			public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
				return world.setBlock(x, y, z, getBlockFromTier(getIndex(trigger.stackSize)), getMetaFromTier(getIndex(trigger.stackSize)), 3);
			}
		};
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		int layer = Math.min(stackSize.stackSize + 3, 18);
		log("Layer: "+layer);
		log("Building 0");
		buildPiece(mName + "bottom" , stackSize, hintsOnly, 2, 0, 0);
		log("Built 0");
		for (int i = 1; i < layer - 1; i++) {
			log("Building "+i);
			buildPiece(mName + "mid", stackSize, hintsOnly, 2, i, 0);
			log("Built "+i);
		}
		log("Building "+(layer - 1));
		buildPiece(mName + "top", stackSize, hintsOnly, 2, layer - 1, 0);
		log("Built "+(layer - 1));
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mEnergyHatches.clear();
		mDynamoHatches.clear();
		mTecTechEnergyHatches.clear();
		mTecTechDynamoHatches.clear();
		mAllEnergyHatches.clear();
		mAllDynamoHatches.clear();
		for (int i = 0; i < 6; i++) {
			cellCount[i] = 0;
		}
		log("Checking 0");
		if (!checkPiece(mName + "bottom", 2, 0, 0)) {
			log("Failed on Layer 0");
			return false;
		}
		log("Pass 0");
		int layer = 1;
		topState = TopState.MayBeTop;
		while (true) {
			if (!checkPiece(mName + "layer", 2, layer, 0))
				return false;
			layer ++;
			if (topState == TopState.Top)
				break; // top found, break out
			topState = TopState.MayBeTop;
			if (layer > 18)
				return false; // too many layers
		}
		int level = 0;
		for (int i = 0; i < 6; i++) {
			if (cellCount[i] != 0) {
				if (level == 0) {
					level = i + 4;
				}
				else {
					return false;
				}
			}
		}
		int tier = getMaxHatchTier(level);
		long volSum = 0;
		for (GT_MetaTileEntity_Hatch hatch : mAllDynamoHatches) {
			if (hatch.mTier > tier || hatch.mTier < 3) {
				return false;
			}
			volSum += (8L << (hatch.mTier * 2));
		}
		for (GT_MetaTileEntity_Hatch hatch : mAllEnergyHatches) {
			if (hatch.mTier > tier || hatch.mTier < 3) {
				return false;
			}
			volSum += (8L << (hatch.mTier * 2));
		}
		mBatteryCapacity = getCapacityFromCellTier(level) * cellCount[level - 4];
		if (mAllEnergyHatches.size() + mAllDynamoHatches.size() > 0) {
			mAverageEuUsage = volSum / (mAllEnergyHatches.size() + mAllDynamoHatches.size());
		}
		else mAverageEuUsage = 0;
		fixAllMaintenanceIssue();
		return true;
	}

	public final boolean addPowerSubStationList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} 
			else if (LoadedMods.TecTech) {
				if (isThisHatchMultiDynamo(aMetaTileEntity)) {
					return addToMachineList(aTileEntity, aBaseCasingIndex);
				} else if (isThisHatchMultiEnergy(aMetaTileEntity)) {
					return addToMachineList(aTileEntity, aBaseCasingIndex);
				}
			}
		}
		return false;
	}

	// Define storage capacity of smallest cell tier (EV) and compute higher tiers from it
	private static final long CELL_TIER_EV_CAPACITY = 100 * 1000 * 1000; // one lapotronic orb
	private static final long CELL_TIER_MULTIPLIER = 4; // each tier's capacity is this many times the previous tier

	public static long getCapacityFromCellTier(int aOverallCellTier) {
		// Use integer math instead of `Math.pow` to avoid range/precision errors
		if (aOverallCellTier < 4) return 0;
		aOverallCellTier -= 4;
		long capacity = CELL_TIER_EV_CAPACITY;
		while (aOverallCellTier > 0) {
			capacity *= CELL_TIER_MULTIPLIER;
			aOverallCellTier--;
		}
		return capacity;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_PowerSubStationController(this.mName);
	}

	//mTotalEnergyAdded
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mAverageEuUsage", this.mAverageEuUsage);
		aNBT.setLong("mAverageEuAdded", this.mAverageEuAdded);
		aNBT.setLong("mAverageEuConsumed", this.mAverageEuConsumed);

		//Usage Stats
		aNBT.setLong("mTotalEnergyAdded", this.mTotalEnergyAdded);
		aNBT.setLong("mTotalEnergyLost", this.mTotalEnergyLost);
		aNBT.setLong("mTotalEnergyConsumed", this.mTotalEnergyConsumed);
		aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
		aNBT.setBoolean("mIsOutputtingPower", this.mIsOutputtingPower);
		aNBT.setLong("mBatteryCapacity", this.mBatteryCapacity);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {

		// Best not to get a long if the Tag Map is holding an int
		if (aNBT.hasKey("mAverageEuUsage")) {
			this.mAverageEuUsage = aNBT.getLong("mAverageEuUsage");
		}	
		if (aNBT.hasKey("mAverageEuAdded")) {
			this.mAverageEuAdded = aNBT.getLong("mAverageEuAdded");
		}
		if (aNBT.hasKey("mAverageEuConsumed")) {
			this.mAverageEuConsumed = aNBT.getLong("mAverageEuConsumed");
		}

		//Usage Stats
		this.mTotalEnergyAdded = aNBT.getLong("mTotalEnergyAdded");
		this.mTotalEnergyLost = aNBT.getLong("mTotalEnergyLost");
		this.mTotalEnergyConsumed = aNBT.getLong("mTotalEnergyConsumed");
		this.mTotalRunTime = aNBT.getLong("mTotalRunTime");

		this.mIsOutputtingPower = aNBT.getBoolean("mIsOutputtingPower");

		this.mBatteryCapacity = aNBT.getLong("mBatteryCapacity");

		super.loadNBTData(aNBT);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		this.mProgresstime = 0;
		this.mMaxProgresstime = 200;
		this.mEUt = 0;
		this.mEfficiencyIncrease = 10000;
		this.fixAllMaintenanceIssue();
		return true;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	private long drawEnergyFromHatch(MetaTileEntity aHatch)  {
		if (!isValidMetaTileEntity(aHatch)) {
			return 0;
		}

		long stored = aHatch.getEUVar();
		long voltage = aHatch.maxEUInput() * aHatch.maxAmperesIn();

		if (voltage > stored) {
			return 0;
		}

		if (this.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false)) {
			aHatch.setEUVar((stored - voltage));
			this.mTotalEnergyAdded += voltage;
			return voltage;
		}
		return 0;
	}

	private long addEnergyToHatch(MetaTileEntity aHatch) {
		if (!isValidMetaTileEntity(aHatch)) {
			return 0;
		}

		long voltage = aHatch.maxEUOutput() * aHatch.maxAmperesOut();

		if (aHatch.getEUVar() > aHatch.maxEUStore() - voltage) {
			return 0;
		}

		if (this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(voltage, false)) {
			aHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false);
			this.mTotalEnergyConsumed+=voltage;
			return voltage;
		}
		return 0;
	}

	private long computeEnergyTax() {
		float mTax = mAverageEuUsage * (ENERGY_TAX / 100f);

		// Increase tax up to 2x if machine is not fully repaired
		mTax = mTax * (1f + (10000f - mEfficiency) / 10000f);

		return MathUtils.roundToClosestLong(mTax);
	}


	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		this.fixAllMaintenanceIssue();
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		// First, decay overcharge (0.1% of stored energy plus 1000 EU per tick)
		if (this.getEUVar() > this.mBatteryCapacity) {
			long energy = (long) (this.getEUVar() * 0.999f) - 1000;
			this.setEUVar(energy);
		}

		// Pay Tax
		long mDecrease = computeEnergyTax();
		this.mTotalEnergyLost += Math.min(mDecrease, this.getEUVar());
		this.setEUVar(Math.max(0, this.getEUVar() - mDecrease));

		long aInputAverage = 0;
		long aOutputAverage = 0;
		// Input Power
		for (Object THatch : this.mDischargeHatches) {
			GT_MetaTileEntity_Hatch_OutputBattery tHatch = (GT_MetaTileEntity_Hatch_OutputBattery) THatch;
			drawEnergyFromHatch(tHatch);
			aInputAverage += tHatch.maxEUInput() * tHatch.maxAmperesIn();
		}
		for (GT_MetaTileEntity_Hatch tHatch : this.mAllEnergyHatches) {
			drawEnergyFromHatch(tHatch);
			aInputAverage += tHatch.maxEUInput() * tHatch.maxAmperesIn();
		}

		// Output Power
		for (Object THatch : this.mChargeHatches) {
			GT_MetaTileEntity_Hatch_InputBattery tHatch = (GT_MetaTileEntity_Hatch_InputBattery) THatch;
			aOutputAverage += addEnergyToHatch(tHatch);
		}
		for (GT_MetaTileEntity_Hatch tHatch : this.mAllDynamoHatches) {
			aOutputAverage += addEnergyToHatch(tHatch);
		}
		// reset progress time
		mProgresstime = 0;

		this.mAverageEuAdded = aInputAverage;
		this.mAverageEuConsumed = aOutputAverage;

		return true;

	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		// Not applicable to this machine
		return true;
	}

	@Override
	public boolean addEnergyOutput(long aEU) {
		// Not applicable to this machine
		return true;
	}

	@Override
	public long maxEUStore() {
		return mBatteryCapacity;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public String[] getExtraInfoData() {
		String mode;
		if (mIsOutputtingPower) {
			mode = EnumChatFormatting.GOLD + "Output" + EnumChatFormatting.RESET;
		} else {
			mode = EnumChatFormatting.BLUE + "Input" + EnumChatFormatting.RESET;
		}

		String storedEnergyText;
		if (this.getEUVar() > this.mBatteryCapacity) {
			storedEnergyText = EnumChatFormatting.RED + GT_Utility.formatNumbers(this.getEUVar()) + EnumChatFormatting.RESET;
		} else {
			storedEnergyText = EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.getEUVar()) + EnumChatFormatting.RESET;
		}

		int errorCode = this.getBaseMetaTileEntity().getErrorDisplayID();
		boolean mMaint = (errorCode != 0);

		return new String[]{
				"Ergon Energy - District Sub-Station",
				"Stored EU: " + storedEnergyText,
				"Capacity: " + EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(this.maxEUStore()) + EnumChatFormatting.RESET,
				"Running Costs: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.computeEnergyTax()) + EnumChatFormatting.RESET + " EU/t",
				"Controller Mode: " + mode,
				"Requires Maintenance: " + (!mMaint ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)+ mMaint + EnumChatFormatting.RESET +" | Code: ["+(!mMaint ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + errorCode + EnumChatFormatting.RESET +"]",
				"----------------------",
				"Stats for Nerds",
				"Average Input: " + EnumChatFormatting.BLUE + GT_Utility.formatNumbers(this.mAverageEuAdded) + EnumChatFormatting.RESET + " EU",
				"Average Output: " + EnumChatFormatting.GOLD + GT_Utility.formatNumbers(this.mAverageEuConsumed) + EnumChatFormatting.RESET + " EU",
				"Total Input: " + EnumChatFormatting.BLUE + GT_Utility.formatNumbers(this.mTotalEnergyAdded) + EnumChatFormatting.RESET + " EU",
				"Total Output: " + EnumChatFormatting.GOLD + GT_Utility.formatNumbers(this.mTotalEnergyConsumed) + EnumChatFormatting.RESET + " EU",
				"Total Costs: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.mTotalEnergyLost) + EnumChatFormatting.RESET + " EU",
		};
	}

	@Override
	public void explodeMultiblock() {
		// TODO Auto-generated method stub
		super.explodeMultiblock();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		// TODO Auto-generated method stub
		super.doExplosion(aExplosionPower);
	}

	@Override
	public long getMaxInputVoltage() {
		return 32768;
	}

	@Override
	public boolean isElectric() {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return !mIsOutputtingPower;
	}

	@Override
	public boolean isEnetOutput() {
		return mIsOutputtingPower;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return (aSide == this.getBaseMetaTileEntity().getBackFacing() && !mIsOutputtingPower);
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return (aSide == this.getBaseMetaTileEntity().getBackFacing() && mIsOutputtingPower);
	}

	@Override
	public long maxAmperesIn() {
		return 32;
	}

	@Override
	public long maxAmperesOut() {
		return 32;
	}

	@Override
	public long maxEUInput() {
		return 32768;
	}

	@Override
	public long maxEUOutput() {
		return 32768;
	}

	public final long getAverageEuAdded() {
		return this.mAverageEuAdded;
	}

	public final long getAverageEuConsumed() {
		return this.mAverageEuConsumed;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mIsOutputtingPower = Utils.invertBoolean(mIsOutputtingPower);
		if (mIsOutputtingPower) {
			PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now outputting power from the controller.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now inputting power into the controller.");
		}
	}

}