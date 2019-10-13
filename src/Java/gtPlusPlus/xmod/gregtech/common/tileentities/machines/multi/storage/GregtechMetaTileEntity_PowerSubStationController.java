package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.handler.BookHandler;
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
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_PowerSubStationController extends GregtechMeta_MultiBlockBase {

	protected int mAverageEuUsage = 0;
	protected long mTotalEnergyAdded = 0;
	protected long mTotalEnergyConsumed = 0;
	protected long mTotalEnergyLost = 0;
	protected boolean mIsOutputtingPower = false;
	protected long mBatteryCapacity = 0;

	private final int ENERGY_TAX = 2;

	//TecTech Support
	public ArrayList<GT_MetaTileEntity_Hatch> mAllDynamoHatches = new ArrayList<GT_MetaTileEntity_Hatch>();

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
	public String[] getTooltip() {
		return new String[]{
				"[BUG] GUI does not work until structure is assembled correctly. (Do Not Report issue)",
				"Consumes " + this.ENERGY_TAX + "% of the average voltage of all energy type hatches",
				"Does not require maintenance",
				"Can be built with variable height between " + (CELL_HEIGHT_MIN + 2) + "-" + (CELL_HEIGHT_MAX + 2) + "",
				"Hatches can be placed nearly anywhere",
				"HV Energy/Dynamo Hatches are the lowest tier you can use",
				CORE.GTNH ? "Supports voltages >= UHV using MAX tier components." : "Supports upto "+GT_Values.VOLTAGE_NAMES[GT_Values.VOLTAGE_NAMES.length-1],
						"Controller (Bottom, Centre)",
						"Size(WxHxD): External 5xHx5, Sub-Station External Casings", 
						"Size(WxHxD): Internal 3x(H-2)x3, Vanadium Redox Power Cells",
						"Read '"+BookHandler.ItemBookWritten_MultiPowerStorage.getDisplayName()+"' for more info.",
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(24)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER)};
		}
		if (aSide == this.getBaseMetaTileEntity().getBackFacing()) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(24)],
					mIsOutputtingPower ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[(int) this.getOutputTier()] : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[(int) this.getInputTier()]};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(23)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
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
			return CORE.GTNH ? GT_Values.V.length : 9;
		} else {
			return -1;
		}
	}

	public static final int CELL_HEIGHT_MAX = 16;
	public static final int CELL_HEIGHT_MIN = 2;

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Logger.INFO("Checking structure for Industrial Power Sub-Station.");
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

		this.mMultiDynamoHatches.clear();
		this.mAllDynamoHatches.clear();

		boolean tFoundCeiling = false;
		int tCasingCount = 0;
		int tOverallCellTier = -1;
		int tCellCount = 0;

		for (int yOff = 0; yOff < CELL_HEIGHT_MAX + 2; yOff++) {
			if (tFoundCeiling) continue;
			if (yOff == CELL_HEIGHT_MAX + 1) tFoundCeiling = true;

			for (int i = -2; i < 3; i++) {
				for (int j = -2; j < 3; j++) {
					int xOff = xDir + i, zOff = zDir + j;

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xOff, yOff, zOff);
					if (tTileEntity == aBaseMetaTileEntity) continue;

					final Block tBlock = aBaseMetaTileEntity.getBlockOffset(xOff, yOff, zOff);
					final byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xOff, yOff, zOff);

					if (yOff == 0) {
						if (tBlock == ModBlocks.blockCasings2Misc && tMeta == 8) {
							tCasingCount++;
						} else if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
							checkMachineProblem("Unexpected block in machine floor", xOff, yOff, zOff);
							return false;
						}
					} else {
						int tCellTier = -1;
						if (i == -2 || i == 2 || j == -2 || j == 2) {
							if (tBlock == ModBlocks.blockCasings2Misc && tMeta == 8) {
								tCasingCount++;
							} else if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
								checkMachineProblem("Unexpected block in machine wall", xOff, yOff, zOff);
								return false;
							}
						} else {
							if (tBlock == ModBlocks.blockCasings2Misc && tMeta == 8) {
								if (yOff > CELL_HEIGHT_MIN && i == -1 && j == -1) {
									tFoundCeiling = true;
								} else if (!tFoundCeiling) {
									checkMachineProblem("Casing found where cell expected", xOff, yOff, zOff);
									return false;
								}
								tCasingCount++;
							} else if (this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
								if (yOff > 2 && i == -1 && j == -1) {
									tFoundCeiling = true;
								} else if (!tFoundCeiling) {
									checkMachineProblem("Hatch found where cell expected", xOff, yOff, zOff);
									return false;
								}
							} else if ((tCellTier = getCellTier(tBlock, tMeta)) != -1) {
								if (tFoundCeiling) {
									checkMachineProblem("Cell found where casing/hatch expected", xOff, yOff, zOff);
									return false;
								} else {
									if (tOverallCellTier == -1) {
										tOverallCellTier = tCellTier;
										tCellCount++;
									} else if (tOverallCellTier != tCellTier) {
										checkMachineProblem("Mismatched cell found, expected tier " + tOverallCellTier + " cell", xOff, yOff, zOff);
										return false;
									} else {
										tCellCount++;
									}
								}
							} else {
								checkMachineProblem("Unexpected block in battery core", xOff, yOff, zOff);
								return false;
							}
						}
					}

				}
			}
		}

		if (tOverallCellTier == -1) {
			checkMachineProblem("No cells in machine (this really shouldn't happen!)");
			return false;
		}

		if (tCasingCount < 35) {
			checkMachineProblem("Not enough casings (needed 35, found " + tCasingCount + ")");
			return false;
		}

		/**
		 * TecTech Support, this allows adding Multi-Amp dynamos.
		 */
		mAllDynamoHatches.addAll(this.mDynamoHatches);

		if (LoadedMods.TecTech) {
			mAllDynamoHatches.addAll(this.mMultiDynamoHatches);
		}


		if (this.mMaintenanceHatches.size() != 1) {
			checkMachineProblem("Needed 1 maintenance hatch, found " + this.mMaintenanceHatches.size());
			return false;
		}
		if (this.mEnergyHatches.size() < 1) {
			checkMachineProblem("Needed at least 1 energy hatch, found 0");
			return false;
		}
		if (this.mAllDynamoHatches.size() < 1) {
			checkMachineProblem("Needed at least 1 dynamo hatch, found 0");
			return false;
		}

		// Find average EU throughput
		int totalEuThroughput = 0;
		int hatchCount = 0;

		for (GT_MetaTileEntity_Hatch_Energy re : this.mEnergyHatches) {
			long tier = re.getOutputTier();
			if(tier > tOverallCellTier) {
				checkMachineProblem("Energy hatch (tier " + tier + ") is too strong for cells (tier " + tOverallCellTier + ")");
				return false;
			}
			if(tier < 3) {
				checkMachineProblem("Energy hatch (tier " + tier + ") is too weak for cells (tier " + tOverallCellTier + ")");
				return false;
			}
			totalEuThroughput += re.maxEUInput();
			hatchCount++;
		}

		for (GT_MetaTileEntity_Hatch re : this.mAllDynamoHatches) {
			long tier = re.getInputTier();
			if(tier > tOverallCellTier) {
				checkMachineProblem("Dynamo hatch (tier " + tier + ") is too strong for cells (tier " + tOverallCellTier + ")");
				return false;
			}
			if(tier < 3) {
				checkMachineProblem("Energy hatch (tier " + tier + ") is too weak for cells (tier " + tOverallCellTier + ")");
				return false;
			}
			totalEuThroughput += re.maxEUOutput();
			hatchCount++;
		}

		if (hatchCount > 0) {
			this.mAverageEuUsage = (totalEuThroughput / hatchCount);
		} else {
			// Shouldn't happen
			this.mAverageEuUsage = 0;
		}

		// Only set this here, after the machine check is 100% passed.
		this.fixAllMaintenanceIssue();
		this.mBatteryCapacity = getCapacityFromCellTier(tOverallCellTier) * tCellCount;
		return true;
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
		aNBT.setInteger("mAverageEuUsage", this.mAverageEuUsage);

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
		this.mAverageEuUsage = aNBT.getInteger("mAverageEuUsage");

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
		this.mProgresstime = 1;
		this.mMaxProgresstime = 1;
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

	private void drawEnergyFromHatch(MetaTileEntity aHatch)  {
		if (!isValidMetaTileEntity(aHatch)) return;

		long stored = aHatch.getEUVar();
		long voltage = aHatch.maxEUInput() * aHatch.maxAmperesIn();

		if (voltage > stored) return;

		if (this.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false)) {
			aHatch.setEUVar((stored - voltage));
			this.mTotalEnergyAdded += voltage;
		}
	}

	private void addEnergyToHatch(MetaTileEntity aHatch) {
		if (!isValidMetaTileEntity(aHatch)) return;

		long voltage = aHatch.maxEUOutput() * aHatch.maxAmperesOut();

		if (aHatch.getEUVar() > aHatch.maxEUStore() - voltage) return;

		if (this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(voltage, false)) {
			aHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false);
			this.mTotalEnergyConsumed+=voltage;
		}

	}

	private long computeEnergyTax() {
		float mTax = mAverageEuUsage * (ENERGY_TAX / 100f);

		// Increase tax up to 2x if machine is not fully repaired
		mTax = mTax * (1f + (10000f - mEfficiency) / 10000f);

		return MathUtils.roundToClosestInt(mTax);
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

		// Input Power
		for (GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
			drawEnergyFromHatch(tHatch);
		}
		for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			drawEnergyFromHatch(tHatch);
		}

		// Output Power
		for (GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
			addEnergyToHatch(tHatch);
		}
		for (GT_MetaTileEntity_Hatch tHatch : this.mAllDynamoHatches) {
			addEnergyToHatch(tHatch);
		}

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