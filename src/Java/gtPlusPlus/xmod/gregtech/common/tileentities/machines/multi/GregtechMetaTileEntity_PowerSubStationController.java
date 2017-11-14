package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.concurrent.TimeUnit;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_PowerSubStationController extends GregtechMeta_MultiBlockBase {

	private static boolean controller;
	protected int mAverageEuUsage = 0;
	protected long mTotalEnergyAdded = 0;
	protected long mTotalEnergyConsumed = 0;
	protected long mTotalEnergyLost = 0;
	protected long mTotalRunTime = 0;

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
				"Stores quite a lot of power",
				"Consumes 1% of the average voltage of all energy type hatches",
				"Energy consumed goes to cooling the Vanadium Redox power storage",
				"Size(WxHxD): 5x4x5, Controller (Bottom, Centre)",
				"--------------------------------------------------------------------------",
				"Bottom layer is made up of Sub-Station external casings (5x1x5)",
				"The inner 3x2x3 area on the next two layers is made up of Vanadium Redox Power Cells",
				"in total, you require 18x VR Power Cells",
				"A single layer of Sub-Station casings goes around the outside of this 3x2x3",
				"On top, another layer of Sub-Station casings",
				"Hatches can be placed nearly anywhere",
				"Minimum 1x Energy Input Hatch",
				"Minimum 1x Energy Dynamo Hatch",
				"1x Charge Bus",
				"1x Discharge Bus",
				"1x Maintenance hatch",
				"--------------------------------------------------------------------------",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(24)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(23)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {



		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Utils.LOG_MACHINE_INFO("Checking structure for Industrial Power Sub-Station.");
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

		/*if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			Utils.LOG_MACHINE_INFO("Don't know why this exists?");
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

							if (h == 2 || h == 1){						
								//If not a hatch, continue, else add hatch and continue.
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
									Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the top layers inner 3x3.");
									Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 7) {
									Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
									return false;
								}
							}
							else {
								if (h==0){
									if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
										if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
											Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the bottom layers inner 3x3.");
											Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
											return false;
										}
										if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
											Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the bottom layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
											return false;
										}
										tAmount++;
									}
								}
								if (h==3){
									if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
										if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
											Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the top layers inner 3x3.");
											Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
											return false;
										}
										if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
											Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing. Found:"+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" with meta:"+aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j));
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
							if (h == 3){
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the top layer edge. 3");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the top layer edge. 3");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							}
							else if (h == 2){
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the top layer edge. 2");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the top layer edge. 2");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									tAmount++;
								}
							}
							else {
								if (!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(24))) {
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasings2Misc) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the second layer. 1");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Utils.LOG_MACHINE_INFO("Station Casings Missing from somewhere in the second layer. 1");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
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
										Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the edges on the top layer.");
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
										Utils.LOG_MACHINE_INFO("Station Casing(s) Missing from one of the edges on the top layer. "+h);
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										if (h ==0){
											if (tTileEntity instanceof GregtechMetaTileEntity_PowerSubStationController){

											}
										}
										else {
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
		if ((this.mInputBusses.size() != 1) || (this.mOutputBusses.size() != 1)
				|| (this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() < 1)
				|| (this.mDynamoHatches.size() < 1)) {
			Utils.LOG_MACHINE_INFO("Returned False 3");
			Utils.LOG_MACHINE_INFO("Input Buses: "+this.mInputBusses.size()+" | expected: 1 | "+(this.mInputBusses.size() != 1));
			Utils.LOG_MACHINE_INFO("Output Buses: "+this.mOutputBusses.size()+" | expected: 1 | "+(this.mOutputBusses.size() != 1));
			Utils.LOG_MACHINE_INFO("Energy Hatches: "+this.mEnergyHatches.size()+" | expected: >= 1 | "+(this.mEnergyHatches.size() < 1));
			Utils.LOG_MACHINE_INFO("Dynamo Hatches: "+this.mDynamoHatches.size()+" | expected: >= 1 | "+(this.mDynamoHatches.size() < 1));
			Utils.LOG_MACHINE_INFO("Maint. Hatches: "+this.mMaintenanceHatches.size()+" | expected: 1 | "+(this.mMaintenanceHatches.size() != 1));
			return false;
		}

		//mAverageEuUsage
		int tempAvg = 0;
		int hatchCount = 0;
		for (GT_MetaTileEntity_Hatch_Energy re : this.mEnergyHatches){
			tempAvg += re.getInputTier();
			hatchCount++;
		}
		for (GT_MetaTileEntity_Hatch_Dynamo re : this.mDynamoHatches){
			tempAvg += re.getOutputTier();
			hatchCount++;
		}
		if (hatchCount > 0){
			this.mAverageEuUsage = (tempAvg/hatchCount);
		}

		Utils.LOG_INFO("Structure Built? "+""+tAmount+" | "+(tAmount>=35));

		return tAmount >= 35;
	}

	public boolean ignoreController(final Block tTileEntity) {
		if (!controller && (tTileEntity == GregTech_API.sBlockMachines)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 1;
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
	long mActualStoredEU = 0;


	//mTotalEnergyAdded
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mPowerStorageBuffer", this.mPowerStorageBuffer);
		aNBT.setInteger("mPowerStorageMultiplier", this.mPowerStorageMultiplier);
		aNBT.setLong("mActualStoredEU", this.mActualStoredEU);
		aNBT.setInteger("mAverageEuUsage", this.mAverageEuUsage);

		//Usage Stats
		aNBT.setLong("mTotalEnergyAdded", this.mTotalEnergyAdded);
		aNBT.setLong("mTotalEnergyLost", this.mTotalEnergyLost);
		aNBT.setLong("mTotalEnergyConsumed", this.mTotalEnergyConsumed);
		aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mPowerStorageBuffer = aNBT.getLong("mPowerStorageBuffer");
		this.mPowerStorageMultiplier = aNBT.getInteger("mPowerStorageMultiplier");
		this.mActualStoredEU = aNBT.getLong("mActualStoredEU");
		this.mAverageEuUsage = aNBT.getInteger("mAverageEuUsage");

		//Usage Stats
		this.mTotalEnergyAdded = aNBT.getLong("mTotalEnergyAdded");
		this.mTotalEnergyLost = aNBT.getLong("mTotalEnergyLost");
		this.mTotalEnergyConsumed = aNBT.getLong("mTotalEnergyConsumed");
		this.mTotalRunTime = aNBT.getLong("mTotalRunTime");

		super.loadNBTData(aNBT);
	}

	@Override
	public int maxProgresstime() {
		return super.maxProgresstime();
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		this.mActualStoredEU = this.getEUVar();

		if (aBaseMetaTileEntity.isServerSide()){
			this.mTotalRunTime++;
		}

		//Handle Progress Time
		if (this.mActualStoredEU >= 0 && this.getBaseMetaTileEntity().isAllowedToWork()){
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
			if (this.mActualStoredEU < this.maxEUStore() && mMaxProgresstime > 0){
				if (this.getBaseMetaTileEntity().isAllowedToWork()){
					this.getBaseMetaTileEntity().enableWorking();
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
			if (this.mActualStoredEU > 0){
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
				addEnergyOutput(1);	
			}
		}		
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		return super.onRunningTick(aStack);
	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		if (aEU <= 0L)
			return true;
		for (GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false))){
				if (this.mActualStoredEU<this.maxEUStore()){
					//this.getBaseMetaTileEntity().increaseStoredEnergyUnits(aEU, false);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addEnergyOutput(long aEU) {
		if (aEU <= 0L)
			return true;
		long nStoredPower = this.getEUVar();
		int hatchCount = 0;
		for (GT_MetaTileEntity_Hatch_Dynamo tHatch : this.mDynamoHatches) {
			if ((isValidMetaTileEntity(tHatch))	&& (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(tHatch.getOutputTier()*2, false))) {
				this.setEUVar(this.getEUVar()-(tHatch.getOutputTier()*2));
				this.mTotalEnergyConsumed+=(tHatch.getOutputTier()*2);
				//this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tHatch.getOutputTier()*2, false);
				//Utils.LOG_INFO("Hatch "+hatchCount+" has "+tHatch.getEUVar()+"eu stored. Avg used is "+(this.mAverageEuUsage));
			}
			hatchCount++;
		}
		long nNewStoredPower = this.getEUVar();
		if (nNewStoredPower < nStoredPower){
			Utils.LOG_ERROR("Used "+(nStoredPower-nNewStoredPower)+"eu.");
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

	//mAverageEuUsage

	@Override
	public String[] getInfoData() {

		long seconds = (this.mTotalRunTime/20);

		int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
		int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
		long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7*weeks);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);



		return new String[]{
				"Ergon Energy - District Sub-Station",
				"EU Required: "+this.mAverageEuUsage+"EU/t",
				"Stats for Nerds",
				"Total Input: "+this.mTotalEnergyAdded+"EU",
				"Total Output: "+this.mTotalEnergyConsumed+"EU",
				"Total Wasted: "+this.mTotalEnergyLost+"EU",

				"Total Time Since Build: ",
				""+weeks+" Weeks.",
				""+days+" Days.",
				""+hours+" Hours.",
				""+minutes+" Minutes.",
				""+second+" Seconds.",
				"Total Time in ticks: "+this.mTotalRunTime};

	};

	@Override
	public boolean isGivingInformation() {
		return true;
	}

}