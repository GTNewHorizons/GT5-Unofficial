package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_PowerSubStationController extends GregtechMeta_MultiBlockBase {

	private static boolean controller;
	protected int mAverageEuUsage = 0;
	protected long mTotalEnergyAdded = 0;
	protected long mTotalEnergyConsumed = 0;
	protected long mTotalEnergyLost = 0;
	protected boolean mIsOutputtingPower = false;
	
	//TecTech Support
	public ArrayList<GT_MetaTileEntity_Hatch> mAllDynamoHatches = new ArrayList<GT_MetaTileEntity_Hatch>();

	public GregtechMetaTileEntity_PowerSubStationController(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_PowerSubStationController(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Power Sub-Station",
				"Consumes 1% of the average voltage of all energy type hatches",
				"Power can be Input/Extracted from the rear face at any time, change with screwdriver",
				"Size(WxHxD): External 5x4x5, Sub-Station Casings, Controller (Bottom, Centre)",
				"Size(WxHxD): Internal 3x2x3, Vanadium Redox Batteries",
				"Hatches can be placed nearly anywhere",
				"(Dis) Charging Hatches are valid",
				"1x Energy Input Hatch (Minimum)",
				"1x Energy Dynamo Hatch (Minimum)",
				"1x Maintenance hatch",
				CORE.GT_Tooltip};
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
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Logger.INFO("Checking structure for Industrial Power Sub-Station.");
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

		/*if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			Utils.LOG_INFO("Don't know why this exists?");
			return false;
		}*/

		int tAmount = 0;
		controller = false;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					// Station Floor/Roof inner 5x5
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {

						// Station Floor & Roof (Inner 5x5) + Mufflers, Dynamos and Fluid outputs.
						if ((h == 0 || h == 3) || (h == 2 || h == 1)) {

							if (h == 2 || h == 1) {
								//If not a hatch, continue, else add hatch and continue.
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Logger.INFO("Station Casing(s) Missing from one of the top layers inner 3x3.");
									Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 7) {
									Logger.INFO("Station Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing. Found:" + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName() + " with meta:" + aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
									return false;
								}
							} else {
								if (h == 0) {
									if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
										if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
											Logger.INFO("Station Casing(s) Missing from one of the bottom layers inner 3x3.");
											Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
											return false;
										}
										if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
											Logger.INFO("Station Casing(s) Missing from one of the bottom layers inner 3x3. Wrong Meta for Casing. Found:" + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName() + " with meta:" + aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
											return false;
										}
										tAmount++;
									}
								}
								if (h == 3) {
									if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
										if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
											Logger.INFO("Station Casing(s) Missing from one of the top layers inner 3x3.");
											Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
											return false;
										}
										if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
											Logger.INFO("Station Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing. Found:" + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName() + " with meta:" + aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
											return false;
										}
										tAmount++;
									}
								}
							}
						}
					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {

						//Deal with all 4 sides (Station walls)
						if ((h == 1) || (h == 2) || (h == 3)) {
							if (h == 3) {
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Logger.INFO("Station Casings Missing from somewhere in the top layer edge. 3");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Logger.INFO("Station Casings Missing from somewhere in the top layer edge. 3");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							} else if (h == 2) {
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Logger.INFO("Station Casings Missing from somewhere in the top layer edge. 2");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Logger.INFO("Station Casings Missing from somewhere in the top layer edge. 2");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							} else {
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Logger.INFO("Station Casings Missing from somewhere in the second layer. 1");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Logger.INFO("Station Casings Missing from somewhere in the second layer. 1");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							}
						}

						//Deal with top and Bottom edges (Inner 5x5)
						else if ((h == 0) || (h == 3)) {
							if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller

									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Logger.INFO("Station Casing(s) Missing from one of the edges on the top layer.");
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Logger.INFO("Station Casing(s) Missing from one of the edges on the top layer. " + h);
										Logger.INFO("Instead, found " + aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										if (h == 0) {
											if (tTileEntity instanceof GregtechMetaTileEntity_PowerSubStationController) {

											}
										} else {
											return false;
										}
									}
								}
							}
						}
					}

				}
			}
		}

		/**
		 * TecTech Support, this allows adding Multi-Amp dynamos.
		 */
		if (this.mDynamoHatches.size() > 0) {
			for (GT_MetaTileEntity_Hatch_Dynamo o : this.mDynamoHatches) {
				mAllDynamoHatches.add(o);
			}
		}
		if (LoadedMods.TecTech && this.mMultiDynamoHatches.size() > 0) {
			for (GT_MetaTileEntity_Hatch o : this.mMultiDynamoHatches) {
				mAllDynamoHatches.add(o);
			}
		}


		if ((this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() < 1)
				|| (this.mAllDynamoHatches.size() < 1)) {
			Logger.INFO("Returned False 3");
			Logger.INFO("Charge Buses: " + this.mChargeHatches.size() + " | expected: >= 1 | " + (this.mChargeHatches.size() >= 1));
			Logger.INFO("Discharge Buses: " + this.mDischargeHatches.size() + " | expected: >= 1 | " + (this.mDischargeHatches.size() >= 1));
			Logger.INFO("Energy Hatches: " + this.mEnergyHatches.size() + " | expected: >= 1 | " + (this.mEnergyHatches.size() < 1));
			Logger.INFO("Dynamo Hatches: " + this.mAllDynamoHatches.size() + " | expected: >= 1 | " + (this.mAllDynamoHatches.size() < 1));
			Logger.INFO("Maint. Hatches: " + this.mMaintenanceHatches.size() + " | expected: 1 | " + (this.mMaintenanceHatches.size() != 1));
			return false;
		}

		//mAverageEuUsage
		int tempAvg = 0;
		int hatchCount = 0;
		for (GT_MetaTileEntity_Hatch_Energy re : this.mEnergyHatches){
			tempAvg += re.getInputTier();
			hatchCount++;
		}
		for (GT_MetaTileEntity_Hatch re : this.mAllDynamoHatches){
			tempAvg += re.getOutputTier();
			hatchCount++;
		}
		if (hatchCount > 0) {
			this.mAverageEuUsage = (tempAvg / hatchCount);
		} else {
			this.mAverageEuUsage = 0;
		}

		Logger.INFO("Structure Built? " + "" + tAmount + " | " + (tAmount >= 35));

		return tAmount >= 35;
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

	//NBT Power Storage handling
	long mPowerStorageBuffer = 0;
	int mPowerStorageMultiplier = 32;

	//mTotalEnergyAdded
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mPowerStorageBuffer", this.mPowerStorageBuffer);
		aNBT.setInteger("mPowerStorageMultiplier", this.mPowerStorageMultiplier);
		aNBT.setInteger("mAverageEuUsage", this.mAverageEuUsage);

		//Usage Stats
		aNBT.setLong("mTotalEnergyAdded", this.mTotalEnergyAdded);
		aNBT.setLong("mTotalEnergyLost", this.mTotalEnergyLost);
		aNBT.setLong("mTotalEnergyConsumed", this.mTotalEnergyConsumed);
		aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
		aNBT.setBoolean("mIsOutputtingPower", this.mIsOutputtingPower);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mPowerStorageBuffer = aNBT.getLong("mPowerStorageBuffer");
		this.mPowerStorageMultiplier = aNBT.getInteger("mPowerStorageMultiplier");
		this.mAverageEuUsage = aNBT.getInteger("mAverageEuUsage");

		//Usage Stats
		this.mTotalEnergyAdded = aNBT.getLong("mTotalEnergyAdded");
		this.mTotalEnergyLost = aNBT.getLong("mTotalEnergyLost");
		this.mTotalEnergyConsumed = aNBT.getLong("mTotalEnergyConsumed");
		this.mTotalRunTime = aNBT.getLong("mTotalRunTime");

		this.mIsOutputtingPower = aNBT.getBoolean("mIsOutputtingPower");

		super.loadNBTData(aNBT);
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (this.getEUVar() < 0){
			this.setEUVar(0);
		}

		//Handle Progress Time
		if (this.getBaseMetaTileEntity().isAllowedToWork()){
			this.mProgresstime = 20;
			this.mMaxProgresstime = 40;	
			//Use 10% of average EU determined by adding in/output voltage of all hatches and averaging.
			int mDecrease = MathUtils.roundToClosestInt(mAverageEuUsage);
			this.mTotalEnergyLost+=mDecrease;
			this.setEUVar(this.getEUVar()-mDecrease);
			//this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mDecrease, false);
		}
		else {
			this.mProgresstime = 0;
			this.mMaxProgresstime = 0;
		}

		//Do work
		if (this.getBaseMetaTileEntity().isAllowedToWork()){

			//Input Power
			if (this.getEUVar() < this.maxEUStore()){
				if (this.getBaseMetaTileEntity().isAllowedToWork()){
					this.getBaseMetaTileEntity().enableWorking();
				}
				for (GT_MetaTileEntity_Hatch_OutputBattery energy : this.mDischargeHatches){
					long stored = energy.getEUVar();
					long voltage = energy.maxEUInput();
					if (stored > 0){
						energy.setEUVar((stored-voltage));
						this.mTotalEnergyAdded+=voltage;
						if (this.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false)){
							//Utils.LOG_INFO("Draining Discharge Hatch #1");
						}
					}
				}
				for (GT_MetaTileEntity_Hatch_Energy energy : this.mEnergyHatches){
					long stored = energy.getEUVar();
					long voltage = energy.maxEUInput();
					if (stored > 0){
						energy.setEUVar((stored-voltage));
						this.mTotalEnergyAdded+=voltage;
						this.getBaseMetaTileEntity().increaseStoredEnergyUnits(voltage, false);
					}
				}
			}
			else {

			}

			//Output Power
			addEnergyOutput(1);

		}		
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		if (aEU <= 0L)
			return true;
		long nStoredPower = this.getEUVar();
		for (GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false))){

				Logger.INFO("Draining Discharge Hatch #2");
			}
		}
		for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false))){

			}
		}		
		long nNewStoredPower = this.getEUVar();
		if (nNewStoredPower < nStoredPower){
			Logger.ERROR("Used "+(nStoredPower-nNewStoredPower)+"eu.");
			return true;
		}

		return false;
	}

	@Override
	public boolean addEnergyOutput(long aEU) {
		if (aEU <= 0L)
			return true;
		long nStoredPower = this.getEUVar();
		int hatchCount = 0;
		for (GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(tHatch.maxEUInput(), false))) {
				this.setEUVar(this.getEUVar()-(tHatch.maxEUInput()));
				this.mTotalEnergyConsumed+=(tHatch.maxEUInput());
			}
			hatchCount++;
		}
		for (GT_MetaTileEntity_Hatch tHatch : this.mAllDynamoHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(GT_Values.V[(int) tHatch.getOutputTier()], false))) {
				this.setEUVar(this.getEUVar()-(GT_Values.V[(int) tHatch.getOutputTier()]));
				this.mTotalEnergyConsumed+=(GT_Values.V[(int) tHatch.getOutputTier()]);
			}
			hatchCount++;
		}
		long nNewStoredPower = this.getEUVar();
		if (nNewStoredPower < nStoredPower){
			Logger.ERROR("Used "+(nStoredPower-nNewStoredPower)+"eu.");
			return true;
		}
		return false;
	}

	@Override
	public long maxEUStore() {
		return 9200000000000000000L;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public String[] getInfoData() {
		long seconds = (this.mTotalRunTime/20);

		int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
		int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
		long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7*weeks);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		String mode;
		if (mIsOutputtingPower) {
			mode = EnumChatFormatting.GOLD + "Output" + EnumChatFormatting.RESET;
		} else {
			mode = EnumChatFormatting.BLUE + "Input" + EnumChatFormatting.RESET;
		}
		return new String[]{
				"Ergon Energy - District Sub-Station",
				"Stored EU:" + EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.getEUVar()) + EnumChatFormatting.RESET,
				"Capacity: " + EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(this.maxEUStore()) + EnumChatFormatting.RESET,
				"Running Costs: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.mAverageEuUsage) + EnumChatFormatting.RESET + " EU/t",
				"Controller Mode: " + mode,
				"Stats for Nerds",
				"Total Input: " + EnumChatFormatting.BLUE + GT_Utility.formatNumbers(this.mTotalEnergyAdded) + EnumChatFormatting.RESET + " EU",
				"Total Output: " + EnumChatFormatting.GOLD + GT_Utility.formatNumbers(this.mTotalEnergyConsumed) + EnumChatFormatting.RESET + " EU",
				"Total Costs: " + EnumChatFormatting.RED + GT_Utility.formatNumbers(this.mTotalEnergyLost) + EnumChatFormatting.RESET + " EU",

				"Total Time Since Built: ",
				""+weeks+" Weeks.",
				""+days+" Days.",
				""+hours+" Hours.",
				""+minutes+" Minutes.",
				""+second+" Seconds.",
				"Total Time in ticks: "+this.mTotalRunTime
		};

	};

	@Override
	public boolean isGivingInformation() {
		return true;
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
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mIsOutputtingPower = Utils.invertBoolean(mIsOutputtingPower);
		if (mIsOutputtingPower) {
			PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now outputting power from the controller.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now inputting power into the controller.");
		}
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

}