package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.*;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.lib.*;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.*;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.*;

public class GregtechMetaTileEntity_LargeRocketEngine extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_LargeRocketEngine>
{
	protected int fuelConsumption;
	protected int fuelValue;
	protected int fuelRemaining;
	protected int freeFuelTicks = 0;
	protected int euProduction = 0;
	protected boolean boostEu;

	public static String mLubricantName = "Carbon Dioxide";
	public static String mCoolantName = "Liquid Hydrogen";

	public static String mCasingName = "Turbodyne Casing";
	public static String mIntakeHatchName = "Tungstensteel Turbine Casing";
	public static String mGearboxName = "Inconel Reinforced Casing";

	private static Fluid sAirFluid = null;
	private static FluidStack sAirFluidStack = null;

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_LargeRocketEngine> STRUCTURE_DEFINITION = null;

	private final static int CASING_ID = TAE.getIndexFromPage(3, 11);

	public GregtechMetaTileEntity_LargeRocketEngine(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		this.fuelConsumption = 0;
		this.fuelValue = 0;
		this.fuelRemaining = 0;
		this.boostEu = false;
		setAir();
	}

	public GregtechMetaTileEntity_LargeRocketEngine(final String aName) {
		super(aName);
		this.fuelConsumption = 0;
		this.fuelValue = 0;
		this.fuelRemaining = 0;
		this.boostEu = false;
		setAir();
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Controller Block for the Large Rocket Engine")
		.addInfo("Supply Rocket Fuels and 1000L of " + mLubricantName + " per hour")
		.addInfo("Produces as much energy as you put fuel in, with optional boosting")
		.addInfo("Supply 4L of " + mCoolantName + " per second, per 2100 EU/t to boost")
		.addInfo("Takes 3x the amount of fuel and " + mLubricantName + "to run 3x faster")
		.addInfo("Consumes 2000L/s of air and pollutes 1500 gibbl/s per 16384 eu/t produced")
		.addInfo("If producing more than 18.4k EU/t, fuel will be consumed less efficiently:")
		.addInfo("- 75% of max fuel efficiency at 44k EU/t output energy")
		.addInfo("- 50% of max fuel efficiency at 105k EU/t output energy")
		.addInfo("- 25% of max fuel efficiency at 294k EU/t output energy")
		.addInfo("(These thresholds are 3x higher when boosted)")
		.addInfo("formula: x = input of energy (10K^(1/3)/ x^(1/3)) * (40K^(1/3)/ x^(1/3))")
		.addSeparator()
		.beginStructureBlock(3, 3, 10, false)
		.addController("Front Center")
		.addCasingInfo(mCasingName, 64)
		.addCasingInfo(mGearboxName, 8)
		.addStructureHint("Air Intake Hatch", 1)
		.addInputBus("Side center line", 1)
		.addInputHatch("Side center line", 1)
		.addMaintenanceHatch("Any Block Touching Inconel Reinforced Casing", 1)
		.addDynamoHatch("Top center line", 2)
		.addMufflerHatch("Back Center", 3)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_LargeRocketEngine> getStructureDefinition() {
		if (this.STRUCTURE_DEFINITION == null) {
			this.STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_LargeRocketEngine>builder()
					.addShape(this.mName, transpose(new String[][]{
						{"CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC"},
						{"C~C", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "CMC"},
						{"CCC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CCC"},
					}))
					.addElement('C', ofBlock(getCasingBlock(), getCasingMeta()))
					.addElement('I', ofBlock(getGearboxBlock(), getGearboxMeta()))
					.addElement('T', ofChain(ofHatchAdder(GregtechMetaTileEntity_LargeRocketEngine::addLargeRocketEngineTopList, getCasingTextureIndex(), 2),
							onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
					.addElement('S', ofChain(ofHatchAdder(GregtechMetaTileEntity_LargeRocketEngine::addLargeRocketEngineSideList, getCasingTextureIndex(), 1),
							onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
					.addElement('M', ofHatchAdder(GregtechMetaTileEntity_LargeRocketEngine::addLargeRocketEngineBackList, getCasingTextureIndex(), 3))
					.build();
		}
		return this.STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(this.mName , stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		this.mCasing = 0;
		this.mTecTechDynamoHatches.clear();
		this.mAllDynamoHatches.clear();
		this.mAirIntakes.clear();
		return checkPiece(this.mName, 1, 1, 0) && this.mCasing >= 64 - 48 && this.mAirIntakes.size() >= 8 && checkHatch();
	}

	public final boolean addLargeRocketEngineTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			if (LoadedMods.TecTech) {
				if (isThisHatchMultiDynamo(aMetaTileEntity)) {
					return addToMachineList(aTileEntity, aBaseCasingIndex);
				}
			}
		}
		return false;
	}

	public final boolean addLargeRocketEngineSideList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	public final boolean addLargeRocketEngineBackList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_ID), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced) };
		}
		return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_ID) };
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return this.getMaxEfficiency(aStack) > 0;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return super.getClientGUI(aID, aPlayerInventory, aBaseMetaTileEntity);
	}

	public static void setAir() {
		if (sAirFluidStack == null) {
			sAirFluidStack = FluidUtils.getFluidStack("air", 1);
		}
		if (sAirFluid == null && sAirFluidStack != null) {
			sAirFluid = sAirFluidStack.getFluid();
		}
	}

	public int getAir() {
		setAir();
		if (this.mAirIntakes.isEmpty() || this.mAirIntakes.size() <= 0) {
			return 0;
		}
		else {
			int totalAir = 0;
			for (GT_MetaTileEntity_Hatch_AirIntake u : this.mAirIntakes) {
				if (u != null && u.mFluid != null) {
					FluidStack f = u.mFluid;
					if (f.isFluidEqual(sAirFluidStack)) {
						totalAir += f.amount;
					}
				}
			}
			return totalAir;
		}
	}

	@Override
	public GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sRocketFuels;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		final ArrayList<FluidStack> tFluids = this.getStoredFluids();
		this.clearRecipeMapForAllInputHatches();
		int aircount = getAir() ;
		int aAirToConsume = this.euProduction/100;
		if (aircount <  aAirToConsume) {
			log("Not Enough Air to Run "+aircount);
			return false;
		}
		else {
			int aTotalAir = 0;
			for (GT_MetaTileEntity_Hatch_AirIntake aAirHatch : this.mAirIntakes) {
				if (aAirHatch.mFluid != null) {
					aTotalAir += aAirHatch.getFluidAmount();
				}
			}
			log("Total Air: "+aTotalAir);
			if (aTotalAir >= aAirToConsume) {
				int aSplitAmount = (aAirToConsume / this.mAirIntakes.size());
				if (aSplitAmount > 0) {
					for (GT_MetaTileEntity_Hatch_AirIntake aAirHatch : mAirIntakes) {						
						boolean hasIntakeAir = aAirHatch.drain(aSplitAmount, true) != null;
						if (!hasIntakeAir) {
							log("Could not consume Air to run "+aSplitAmount);
							this.freeFuelTicks = 0;
							return false;
						}
						log("Consumed Air to run "+aSplitAmount);
					}
				}
			}
		}
		// reset fuel ticks in case it does not reset when it stops
		if (this.freeFuelTicks != 0 && this.mProgresstime == 0 && this.mEfficiency == 0)
			this.freeFuelTicks = 0;

		log("Running "+aircount);
		log("looking at hatch");


		if (tFluids.size() > 0 && getRecipeMap() != null) {
			FluidStack aCO2 = MISC_MATERIALS.CARBON_DIOXIDE.getFluidStack(this.boostEu ? 3 : 1);
			FluidStack aCO2Fallback = FluidUtils.getWildcardFluidStack("carbondioxide", (this.boostEu ? 3 : 1));
			
			
			boolean aHasCO2 = false;
			for (FluidStack aFluid : tFluids) {
				if (aCO2 != null && aFluid.isFluidEqual(aCO2)) {
					log("Found CO2 (1)");
					aHasCO2 = true;
					break;
				}
				if (aCO2Fallback != null && aFluid.isFluidEqual(aCO2Fallback)) {
					log("Found CO2 (2)");
					aHasCO2 = true;
					break;
				}
				log("Found: "+aFluid.getUnlocalizedName());
			}
			if (aHasCO2) {
				if (this.mRuntime % 72 == 0 || this.mRuntime == 0) {
					if (!consumeCO2()) {
						this.freeFuelTicks = 0;
						log("Bad Return 1");
						return false;
					}
				}
			}
			else {
				this.freeFuelTicks = 0;
				log("Bad Return 2 | "+aHasCO2+" | "+(aCO2 != null)+" | "+(aCO2Fallback != null));
				return false;
			}
			if (this.freeFuelTicks == 0) {
				this.boostEu = consumeLOH();
			}
			for (final FluidStack hatchFluid1 : tFluids) {
				if (hatchFluid1.isFluidEqual(sAirFluidStack)) {
					continue;
				}
				if (this.freeFuelTicks == 0) {
					for (final GT_Recipe aFuel : getRecipeMap().mRecipeList) {
						final FluidStack tLiquid;
						tLiquid = aFuel.mFluidInputs[0];
						if (hatchFluid1.isFluidEqual(tLiquid)) {
							if (!consumeFuel(aFuel,hatchFluid1.amount)) {
								continue;
							}
							this.fuelValue = aFuel.mSpecialValue * 3;
							this.fuelRemaining = hatchFluid1.amount;
							this.mEUt = (int) ((this.mEfficiency < 2000) ? 0 : GT_Values.V[5]<<1);
							this.mProgresstime = 1;
							this.mMaxProgresstime = 1;
							this.mEfficiencyIncrease =  this.euProduction/2000;
							return true;
						}
					}
				} 
				else {
					this.mEfficiencyIncrease =  this.euProduction/2000;
					this.freeFuelTicks--;
					this.mEUt = (int) ((this.mEfficiency < 1000) ? 0 : GT_Values.V[5]<<1);
					this.mProgresstime = 1;
					this.mMaxProgresstime = 1;
					return true;
				}
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		this.freeFuelTicks = 0;
		log("Bad Return 3");
		return false;
	}

	/**
	 * Consumes Fuel if required. Free Fuel Ticks are handled here.
	 * @param aFuel
	 * @return
	 */
	public boolean consumeFuel(GT_Recipe aFuel,int amount) {
		amount *= this.boostEu ? 0.3 : 0.9;
		this.freeFuelTicks = 0;
		int value = aFuel.mSpecialValue * 3;
		int energy = value * amount;
		if (amount < 5)
			return false;
		FluidStack tLiquid = FluidUtils.getFluidStack(aFuel.mFluidInputs[0], (this.boostEu ? amount * 3 : amount));
		if (!this.depleteInput(tLiquid)) {
			return false;
		}
		else {
			this.fuelConsumption = this.boostEu ? amount * 3 : amount;
			this.freeFuelTicks = 20;
			setEUProduction(energy);
			return true;
		}
	}

	public void setEUProduction(int energy){
		energy /= 20;
		double energyEfficiency;
		double tDivideEnergy = Math.cbrt(energy);
		if (energy > 10000) {
			//cbrt(10 000) /
			energyEfficiency =  (21.5443469/tDivideEnergy);
			if (energy >= 40000)
				//cbrt(40 000) /
				energyEfficiency *= (34.19951893/tDivideEnergy);
			energyEfficiency *= energy;
		}
		else {
			energyEfficiency = energy;
		}
		this.euProduction = (int) (energyEfficiency * 1.84);
		if (this.boostEu)
			this.euProduction *= 3;
	}

	public boolean consumeCO2() {
		if (this.depleteInput(MISC_MATERIALS.CARBON_DIOXIDE.getFluidStack(this.boostEu ? 3 : 1)) || this.depleteInput(FluidUtils.getFluidStack("carbondioxide", (this.boostEu ? 3 : 1)))) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean consumeLOH() {
		int LOHamount = (3 * this.euProduction)/1000;
		return this.depleteInput(FluidUtils.getFluidStack(RocketFuels.Liquid_Hydrogen, LOHamount)); //(40 * ((long) euProduction / 10000))
	}

	@Override
	public boolean addEnergyOutput(long aEU) {
		if (aEU <= 0) {
			return true;
		}
		if (this.mAllDynamoHatches.size() > 0) {
			return addEnergyOutputMultipleDynamos(aEU, true);
		}
		return false;
	}


	@Override
	public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
		int injected = 0;
		long totalOutput = 0;
		long aFirstVoltageFound = -1;
		boolean aFoundMixedDynamos = false;
		for (GT_MetaTileEntity_Hatch aDynamo : this.mAllDynamoHatches) {
			if( aDynamo == null ) {
				return false;
			}
			if (isValidMetaTileEntity(aDynamo)) {
				long aVoltage = aDynamo.maxEUOutput();
				long aTotal = aDynamo.maxAmperesOut() * aVoltage;
				// Check against voltage to check when hatch mixing
				if (aFirstVoltageFound == -1) {
					aFirstVoltageFound = aVoltage;
				}
				else {
					/**
					 * Calcualtes overclocked ness using long integers
					 * @param aEUt          - recipe EUt
					 * @param aDuration     - recipe Duration
					 * @param mAmperage     - should be 1 ?
					 */
					//Long time calculation
					if (aFirstVoltageFound != aVoltage) {
						aFoundMixedDynamos = true;
					}
				}
				totalOutput += aTotal;
			}
		}

		if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
			explodeMultiblock();
			return false;
		}

		long leftToInject;
		//Long EUt calculation
		long aVoltage;
		//Isnt too low EUt check?
		int aAmpsToInject;
		int aRemainder;

		//xEUt *= 4;//this is effect of everclocking
		for (GT_MetaTileEntity_Hatch aDynamo : this.mAllDynamoHatches) {
			if (isValidMetaTileEntity(aDynamo)) {
				leftToInject = aEU - injected;
				aVoltage = aDynamo.maxEUOutput();
				aAmpsToInject = (int) (leftToInject / aVoltage);
				aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
				long powerGain;
				for (int i = 0; i < Math.min(aDynamo.maxAmperesOut(), aAmpsToInject + 1); i++) {
					if (i == Math.min(aDynamo.maxAmperesOut(), aAmpsToInject)){
						powerGain = aRemainder;
					}else{
						powerGain =  aVoltage;
					}
					aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(powerGain, false);
					injected += powerGain;
				}
			}
		}
		return injected > 0;
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		if (this.mRuntime%20 == 0) {
			if (this.mMufflerHatches.size() == 1 && this.mMufflerHatches.get(0) instanceof GT_MetaTileEntity_Hatch_Muffler_Adv) {
				GT_MetaTileEntity_Hatch_Muffler_Adv tMuffler = (GT_MetaTileEntity_Hatch_Muffler_Adv) this.mMufflerHatches.get(0);
				if (!tMuffler.hasValidFilter()) {
					ArrayList<ItemStack> tInputs = getStoredInputs();
					for (ItemStack tItem : tInputs) {
						if (tMuffler.isAirFilter(tItem)) {
							tMuffler.mInventory[0] = tItem.copy();
							depleteInput(tItem);
							updateSlots();
							break;
						}
					}
				}
			}
		}
		super.onRunningTick(aStack);
		return true;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings4Misc;
	}

	public byte getCasingMeta() {
		return 11;
	}

	public Block getGearboxBlock() {
		return ModBlocks.blockCasings3Misc;
	}

	public byte getGearboxMeta() {
		return 1;
	}

	public byte getCasingTextureIndex() {
		return (byte) CASING_ID;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_LargeRocketEngine(this.mName);
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setInteger("freeFuelTicks", this.freeFuelTicks);
		aNBT.setInteger("euProduction", this.euProduction);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.freeFuelTicks = aNBT.getInteger("freeFuelTicks");
		this.euProduction = aNBT.getInteger("euProduction");
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 1;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return this.euProduction;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return	75 * ( this.euProduction / 10000);
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return true;
	}

	@Override
	public String[] getExtraInfoData() {
		return new String[] {
				"Rocket Engine",
				"Current Air: "+getAir(),
				"Current Pollution: " + getPollutionPerTick(null),
				"Time until next fuel consumption: "+this.freeFuelTicks,
				"Current Output: " + this.mEUt * this.mEfficiency / 10000 + " EU/t",
				"Fuel Consumption: " + (this.fuelConsumption) + "L/s",
				"Fuel Value: " + this.fuelValue + " EU/L",
				"Fuel Remaining: " + this.fuelRemaining + " Litres",
				"Current Efficiency: " + this.mEfficiency / 100 + "%",
				(this.getIdealStatus() == this.getRepairStatus()) ? "No Maintainance issues" : "Needs Maintainance" };
	}

	@Override
	public boolean isGivingInformation() {
		return true;
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
	public String getMachineType() {
		return "Rocket Engine";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
}
