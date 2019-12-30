package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_AirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_LargeRocketEngine extends GregtechMeta_MultiBlockBase
{
	protected int fuelConsumption;
	protected int fuelValue;
	protected int fuelRemaining;
	protected int freeFuelTicks = 0;
	protected boolean boostEu;

	public static String mLubricantName = "Carbon Dioxide";
	public static String mCoolantName = "Liquid Hydrogen";

	public static String mCasingName = "Turbodyne Casing";
	public static String mIntakeHatchName = "Tungstensteel Turbine Casing";
	public static String mGearboxName = "Inconel Reinforced Casing";


	private final static int CASING_ID = TAE.getIndexFromPage(3, 11);

	public GregtechMetaTileEntity_LargeRocketEngine(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		this.fuelConsumption = 0;
		this.fuelValue = 0;
		this.fuelRemaining = 0;
		this.boostEu = false;
	}

	public GregtechMetaTileEntity_LargeRocketEngine(final String aName) {
		super(aName);
		this.fuelConsumption = 0;
		this.fuelValue = 0;
		this.fuelRemaining = 0;
		this.boostEu = false;
	}

	@Override
	public String[] getTooltip() {
		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 11);
		}
		if (mIntakeHatchName.toLowerCase().contains(".name")) {
			mIntakeHatchName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 11);
		}
		if (mGearboxName.toLowerCase().contains(".name")) {
			mGearboxName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 1);
		}
		if (mLubricantName.toLowerCase().contains(".")) {
			mLubricantName = FluidUtils.getFluidStack("carbondioxide", 1).getLocalizedName();
		}
		if (mCoolantName.toLowerCase().contains(".")) {
			mCoolantName = FluidUtils.getFluidStack("liquidhydrogen", 1).getLocalizedName();
		}
		return new String[] { 
				"Controller Block for the Large Combustion Engine",
				"Supply Rocket Fuels and 1000L of "+mLubricantName+" per hour to run",
				"Supply 40L of "+mCoolantName+" per second to boost output (optional)", 
				"Consumes upto 5000L of Air per second",
				"Default: Produces "+GT_Values.V[5]+"EU/t at 100% efficiency", 
				"Boosted: Produces "+(GT_Values.V[5]*3)+"EU/t at 150% efficiency",
				"Size(WxHxD): 3x3x10, Controller (front centered)",
				"3x3x10 of Stable "+mCasingName+" (hollow, Min 64!)",
				"8x "+mGearboxName+" inside the Hollow Casing",
				"1x Dynamo Hatch (Top Middle, Max 8)",
				"8x Air Intake Hatch (one of the Casings next to a Gear Box, top row allowed)",
				"2x Input Hatch (Rocket Fuel/Booster) (one of the Casings next to a Gear Box, top row not allowed)",
				"1x Maintenance Hatch (one of the Casings next to a Gear Box)", 
				"1x Muffler Hatch (Back Centre)",
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_ID], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced) };
		}
		return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_ID] };
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return this.getMaxEfficiency(aStack) > 0;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return super.getClientGUI(aID, aPlayerInventory, aBaseMetaTileEntity);
	}

	public int getAir() {
		if (this.mAirIntakes.isEmpty() || this.mAirIntakes.size() <= 0) {
			return 0;
		}
		else {
			int totalAir = 0;
			FluidStack airstack = FluidUtils.getFluidStack("air", 1);
			for (GT_MetaTileEntity_Hatch_AirIntake u : this.mAirIntakes) {
				if (u != null) {
					FluidStack f = u.mFluid;
					if (f.isFluidEqual(airstack)) {
						totalAir += f.amount;
					}
				}
			}
			return totalAir;
		}		
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		final ArrayList<FluidStack> tFluids = this.getStoredFluids();
		FluidStack air = FluidUtils.getFluidStack("air", 1);

		int aircount = getAir() ;
		if (aircount < (boostEu ? 500 : 200)) {
			//Logger.INFO("Not Enough Air to Run "+aircount);
			return false;
		}
		else {			
			boolean hasIntakeAir = this.depleteInput(FluidUtils.getFluidStack(air, boostEu ? 500 : 200));
			if (!hasIntakeAir) {
				//Logger.INFO("Could not consume Air to run "+aircount);
				return false;
			}			
		}
		//Logger.INFO("Running "+aircount);

		final Collection<GT_Recipe> tRecipeList = Recipe_GT.Gregtech_Recipe_Map.sRocketFuels.mRecipeList;
		if (tFluids.size() > 0 && tRecipeList != null) {
			for (final FluidStack hatchFluid1 : tFluids) {
				if (hatchFluid1.isFluidEqual(air)) {
					continue;
				}			
				for (final GT_Recipe aFuel : tRecipeList) {
					final FluidStack tLiquid;
					tLiquid = aFuel.mFluidInputs[0];
					if (hatchFluid1.isFluidEqual(tLiquid)) {
						
						final int n = (int) (this.boostEu ? ((GT_Values.V[5]*2) / aFuel.mSpecialValue) : (GT_Values.V[5] / aFuel.mSpecialValue));	

						if (!consumeFuel(aFuel)) {
							continue;
						}

						//Logger.INFO("Consumed some input fuel");
						this.boostEu = consumeLOH();
						//Logger.INFO("Did we consume LOH? "+boostEu);

						if (tFluids.contains(MISC_MATERIALS.CARBON_DIOXIDE.getFluid(this.boostEu ? 2 : 1)) || tFluids.contains(FluidUtils.getFluidStack("carbondioxide", (this.boostEu ? 2 : 1)))) {
							//Logger.INFO("Found CO2");
							if (this.mRuntime % 72 == 0 || this.mRuntime == 0) {
								if (!consumeCO2()) {
									return false;
								}
							}
							this.fuelValue = aFuel.mSpecialValue;
							this.fuelRemaining = hatchFluid1.amount;
							this.mEUt = (int) ((this.mEfficiency < 2000) ? 0 : GT_Values.V[5]);
							this.mProgresstime = 1;
							this.mMaxProgresstime = 1;
							this.mEfficiencyIncrease = 5;
							return true;
						}
						return false;
					}
				}
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		return false;
	}

	/**
	 * Consumes Fuel if required. Free Fuel Ticks are handled here.
	 * @param aFuel
	 * @return
	 */
	public boolean consumeFuel(GT_Recipe aFuel) {
		if (freeFuelTicks > 0) {
			freeFuelTicks--;
			return true;
		}
		else {		
			Logger.INFO("Consuming fuel.");
			freeFuelTicks = 0;
			int value = aFuel.mSpecialValue * 3000;
			Logger.INFO("Value: "+value);
			value /= GT_Values.V[4];
			value /= 10;
			Logger.INFO("Value: "+value);
			FluidStack tLiquid = FluidUtils.getFluidStack(aFuel.mFluidInputs[0], value);			
			if (!this.depleteInput(tLiquid)) {
				return false;
			}
			else {					
				this.fuelConsumption = value;						
				this.freeFuelTicks = value*2;
				Logger.INFO("Consumed "+value+"L. Waiting "+freeFuelTicks+" ticks to consume more.");
				return true;
			}		
		}
	}

	public boolean consumeCO2() {
		if (this.depleteInput(MISC_MATERIALS.CARBON_DIOXIDE.getFluid(this.boostEu ? 2 : 1)) || this.depleteInput(FluidUtils.getFluidStack("carbondioxide", (this.boostEu ? 2 : 1)))) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean consumeLOH() {
		return this.depleteInput(FluidUtils.getFluidStack(RocketFuels.Liquid_Hydrogen, 2));
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		byte tSide = getBaseMetaTileEntity().getBackFacing();
		int tX = getBaseMetaTileEntity().getXCoord();
		int tY = getBaseMetaTileEntity().getYCoord();
		int tZ = getBaseMetaTileEntity().getZCoord();
		final int MAX_LENGTH = 8;
		for (int length=0;length<MAX_LENGTH;length++) {
			if(getBaseMetaTileEntity().getBlockAtSideAndDistance(tSide, length+1) != getGearboxBlock()) {
				Logger.INFO("Bad Gearbox Block");
				return false;
			}
			if(getBaseMetaTileEntity().getMetaIDAtSideAndDistance(tSide, length+1) != getGearboxMeta()) {
				Logger.INFO("Bad Gearbox Meta");
				return false;
			}
		}
		Logger.INFO("Found "+MAX_LENGTH+" "+mGearboxName+"s.");		
		for (byte i = -1; i < 2; i = (byte) (i + 1)) {
			for (byte j = -1; j < 2; j = (byte) (j + 1)) {
				if ((i != 0) || (j != 0)) {
					for (byte aLength = 0; aLength < (MAX_LENGTH+2); aLength = (byte) (aLength + 1)) { // Length


						final int fX = tX - (tSide == 5 ? 1 : tSide == 4 ? -1 : i),
								fZ = tZ - (tSide == 2 ? -1 : tSide == 3 ? 1 : i),
								aY = tY + j,
								aX = tX + (tSide == 5 ? aLength : tSide == 4 ? -aLength : i),
								aZ = tZ + (tSide == 2 ? -aLength : tSide == 3 ? aLength : i);


						//Why check for air in world when each intake requires 1 air block?
						//final Block frontAir = getBaseMetaTileEntity().getBlock(fX, aY, fZ);
						//final String frontAirName = frontAir.getUnlocalizedName();						
						//if(!(getBaseMetaTileEntity().getAir(fX, aY, fZ) || frontAirName.equalsIgnoreCase("tile.air") || frontAirName.equalsIgnoreCase("tile.railcraft.residual.heat"))) {
						//Logger.INFO("Bad Air Check");
						//return false; //Fail if vent blocks are obstructed
						//}

						if (((i == 0) || (j == 0)) && ((aLength > 0) && (aLength <= MAX_LENGTH))) {
							Logger.INFO("Checking for Hatches. "+aLength);
							//Top Row
							if (j == 1) {
								if (addDynamoToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
									// Do Nothing
								}
								else if (addAirIntakeToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
									// Do Nothing
								}	
								else if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
									// Do nothing
								}
								else {
									Logger.INFO("Top Row - "+aLength+" | Did not find casing or Dynamo");
									return false;
								}
							}
							else {
								IGregTechTileEntity aCheck = getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ);
								if (aCheck != null) {
									final IMetaTileEntity bCheck = aCheck.getMetaTileEntity();							        
									// Only allow Dynamos on Top
									if (bCheck instanceof GT_MetaTileEntity_Hatch_Dynamo) {
										Logger.INFO("Found dynamo in disallowed location | "+aX+", "+aY+", "+aZ+" | "+i+", "+j+", "+aLength);
										return false;
									}
								}	
								if (addAirIntakeToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
									// Do Nothing
								}	
								else if (addInputToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
									// Do Nothing
								}
								else if (addOutputToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
									// Do Nothing
								}
								else if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
									// Do nothing
								}
								else {Logger.INFO("Bad block.");
								return false;
								}

							}
							Logger.INFO("Passed check. "+aLength);

						} else if (aLength == 0) {
							Logger.INFO("Searching for Gearbox");							
							if (addMaintenanceToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ), getCasingTextureIndex())) {
								// Do Nothing
							}
							else if(!(getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta())) {
								Logger.INFO("Bad Missing Casing || Bad Meta");
								return false;
							}
							else {								
								Logger.INFO("Found "+mCasingName+".");								
							}
						} else if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
							Logger.INFO("Found Casing.");
							// Do nothing
						} else {
							Logger.INFO("Bad XXX");
							return false;
						}
					}
				}
			}
		}

		this.mMufflerHatches.clear();
		IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), MAX_LENGTH+1);
		if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
			if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Muffler)) {
				this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) tTileEntity.getMetaTileEntity());
				this.updateTexture(tTileEntity, getCasingTextureIndex());
			}
		}

		if (this.mDynamoHatches.size() <= 0 || this.mDynamoHatches.isEmpty()) {
			Logger.INFO("Wrong count for Dynamos");			
			return false;			
		}
		if (this.mMufflerHatches.size() != 1 || this.mMufflerHatches.isEmpty()) {
			Logger.INFO("Wrong count for Mufflers");			
			return false;			
		}
		if (this.mAirIntakes.size() < 8 || this.mAirIntakes.isEmpty()) {
			Logger.INFO("Wrong count for Air Intakes | "+this.mAirIntakes.size());			
			return false;			
		}
		if (this.mMaintenanceHatches.size() < 1 || this.mMaintenanceHatches.isEmpty()) {
			Logger.INFO("Wrong count for Maint. Hatches");			
			return false;			
		}


		Logger.INFO("Formed Rocket Engine.");
		return true;
	}	

	public Block getCasingBlock() {
		return ModBlocks.blockCasings4Misc;
	}

	public byte getCasingMeta() {
		return 11;
	}

	public Block getIntakeBlock() {
		return GregTech_API.sBlockCasings4;
	}

	public byte getIntakeMeta() {
		return 12;
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
		aNBT.setInteger("freeFuelTicks", freeFuelTicks);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		freeFuelTicks = aNBT.getInteger("freeFuelTicks");
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 1;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return this.boostEu ? 30000 : 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return this.boostEu ? 150 : 75;
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
				"Time until next fuel consumption: "+freeFuelTicks,
				"Current Output: " + this.mEUt * this.mEfficiency / 10000 + " EU/t",
				"Fuel Consumption: " + this.fuelConsumption + "L/t",
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
