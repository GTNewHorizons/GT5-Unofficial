package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.AirIntake;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTDynamo;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_AirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler_Adv;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_LargeRocketEngine extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_LargeRocketEngine> implements ISurvivalConstructable {

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
    private static IStructureDefinition<GregtechMetaTileEntity_LargeRocketEngine> STRUCTURE_DEFINITION = null;

    private static final int CASING_ID = TAE.getIndexFromPage(3, 11);

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
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Large Rocket Engine")
                .addInfo("Generating Power from Rocket Fuels - Supports TecTech Multi-Amp Dynamos!")
                .addInfo("Supply GT++ Rocket Fuels and 1000L of " + mLubricantName + " per hour")
                .addInfo("Produces as much energy as you put fuel in, with optional boosting")
                .addInfo("This multi doesn't accept fluids if not enabled - enable it first!")
                .addInfo("Consumes 2000L/s of air and pollutes 1500 gibbl/s per 16384 eu/t produced")
                .addInfo("Place 1-8 Air Intake Hatches on the sides to maintain Air input")
                .addInfo("If it runs out of air, it will shut down and have to be manually restarted")
                .addInfo("Supply 3L of " + mCoolantName + " per second, per 1000 EU/t to boost")
                .addInfo("Takes 3x the amount of " + mLubricantName + " and maintains efficiency")
                .addInfo("Fuel efficiency starts at ~160%, falls more slowly at higher EU/t if boosted")
                .addInfo("If producing more than 30k EU/t, fuel efficiency will be lower:")
                .addInfo("(These thresholds are 3x higher when boosted, boosted values displayed second)")
                .addInfo("- 75% of max fuel efficiency at 53k or 159k EU/t output energy")
                .addInfo("- 50% of max fuel efficiency at 69k or 207k EU/t output energy")
                .addInfo("- 25% of max fuel efficiency at 98k or 294k EU/t output energy")
                .addInfo("formula: x = input of energy (30000^(1/3)/ x^(1/3)) * (80000^(1/3)/ x^(1/3))").addSeparator()
                .beginStructureBlock(3, 3, 10, false).addController("Front Center")
                .addCasingInfoMin(mCasingName, 64, false).addCasingInfoMin(mGearboxName, 8, false)
                .addStructureHint("Air Intake Hatch", 1).addInputBus("Side center line", 1)
                .addInputHatch("Side center line", 1)
                .addMaintenanceHatch("Any Block Touching Inconel Reinforced Casing", 1)
                .addDynamoHatch("Top center line", 2).addMufflerHatch("Back Center", 3)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_LargeRocketEngine> getStructureDefinition() {
        if (this.STRUCTURE_DEFINITION == null) {
            this.STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_LargeRocketEngine>builder()
                    .addShape(
                            this.mName,
                            transpose(
                                    new String[][] {
                                            { "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC", "CTC" },
                                            { "C~C", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "SIS", "CMC" },
                                            { "CCC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC", "CSC",
                                                    "CCC" }, }))
                    .addElement('C', ofBlock(getCasingBlock(), getCasingMeta()))
                    .addElement('I', ofBlock(getGearboxBlock(), getGearboxMeta()))
                    // side
                    .addElement(
                            'S',
                            buildHatchAdder(GregtechMetaTileEntity_LargeRocketEngine.class)
                                    .atLeast(ImmutableMap.of(AirIntake, 8, InputBus, 1, InputHatch, 3, Maintenance, 1))
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta()))))
                    // top
                    .addElement(
                            'T',
                            buildHatchAdder(GregtechMetaTileEntity_LargeRocketEngine.class)
                                    .atLeast(ImmutableMap.of(AirIntake, 8, Dynamo.or(TTDynamo), 1, Maintenance, 1))
                                    .casingIndex(getCasingTextureIndex()).dot(2).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta()))))
                    .addElement('M', Muffler.newAny(getCasingTextureIndex(), 3)).build();
        }
        return this.STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(this.mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;
        this.mTecTechDynamoHatches.clear();
        this.mAllDynamoHatches.clear();
        this.mAirIntakes.clear();
        return checkPiece(this.mName, 1, 1, 0) && this.mCasing >= 64 - 48
                && this.mAirIntakes.size() >= 1
                && checkHatch();
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
        return CASING_ID;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return this.getMaxEfficiency(aStack) > 0;
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
        } else {
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
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.rocketFuels;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        final ArrayList<FluidStack> tFluids = this.getStoredFluids();
        this.clearRecipeMapForAllInputHatches();
        int aircount = getAir();
        int aAirToConsume = this.euProduction / 100;
        if (aircount < aAirToConsume) {
            criticalStopMachine();
            return SimpleCheckRecipeResult.ofFailure("no_air");
        } else {
            int aTotalAir = 0;
            for (GT_MetaTileEntity_Hatch_AirIntake aAirHatch : this.mAirIntakes) {
                if (aAirHatch.mFluid != null) {
                    aTotalAir += aAirHatch.getFluidAmount();
                }
            }
            if (aTotalAir >= aAirToConsume) {
                int aSplitAmount = (aAirToConsume / this.mAirIntakes.size());
                if (aSplitAmount > 0) {
                    for (GT_MetaTileEntity_Hatch_AirIntake aAirHatch : mAirIntakes) {
                        boolean hasIntakeAir = aAirHatch.drain(aSplitAmount, true) != null;
                        if (!hasIntakeAir) {
                            this.freeFuelTicks = 0;
                            return SimpleCheckRecipeResult.ofFailure("no_air");
                        }
                    }
                }
            }
        }
        // reset fuel ticks in case it does not reset when it stops
        if (this.freeFuelTicks != 0 && this.mProgresstime == 0 && this.mEfficiency == 0) this.freeFuelTicks = 0;

        if (tFluids.size() > 0 && getRecipeMap() != null) {
            if (this.mRuntime % 72 == 0) {
                if (!consumeCO2()) {
                    this.freeFuelTicks = 0;
                    return SimpleCheckRecipeResult.ofFailure("no_co2");
                }
            }
            if (this.freeFuelTicks == 0) {
                this.boostEu = consumeLOH();
            }
            for (final FluidStack hatchFluid1 : tFluids) {
                if (hatchFluid1.isFluidEqual(sAirFluidStack)) {
                    continue;
                }
                if (this.freeFuelTicks == 0) {
                    for (final GT_Recipe aFuel : getRecipeMap().getAllRecipes()) {
                        final FluidStack tLiquid;
                        tLiquid = aFuel.mFluidInputs[0];
                        if (hatchFluid1.isFluidEqual(tLiquid)) {
                            if (!consumeFuel(aFuel, hatchFluid1.amount)) {
                                continue;
                            }
                            this.fuelValue = aFuel.mSpecialValue * 3;
                            this.fuelRemaining = hatchFluid1.amount;
                            this.lEUt = ((this.mEfficiency < 2000) ? 0 : GT_Values.V[5] << 1);
                            this.mProgresstime = 1;
                            this.mMaxProgresstime = 1;
                            this.mEfficiencyIncrease = this.euProduction / 2000;
                            return CheckRecipeResultRegistry.GENERATING;
                        }
                    }
                } else {
                    this.mEfficiencyIncrease = this.euProduction / 2000;
                    this.freeFuelTicks--;
                    this.lEUt = ((this.mEfficiency < 1000) ? 0 : GT_Values.V[5] << 1);
                    this.mProgresstime = 1;
                    this.mMaxProgresstime = 1;
                    return CheckRecipeResultRegistry.GENERATING;
                }
            }
        }
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.freeFuelTicks = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    /**
     * Consumes Fuel if required. Free Fuel Ticks are handled here.
     * 
     * @param aFuel
     * @return
     */
    public boolean consumeFuel(GT_Recipe aFuel, int amount) {
        amount *= this.boostEu ? 0.3 : 0.9;
        this.freeFuelTicks = 0;
        int value = aFuel.mSpecialValue * 3;
        int energy = value * amount;
        if (amount < 5) return false;
        FluidStack tLiquid = FluidUtils.getFluidStack(aFuel.mFluidInputs[0], (this.boostEu ? amount * 3 : amount));
        if (!this.depleteInput(tLiquid)) {
            return false;
        } else {
            this.fuelConsumption = this.boostEu ? amount * 3 : amount;
            this.freeFuelTicks = 20;
            setEUProduction(energy);
            return true;
        }
    }

    public void setEUProduction(int energy) {
        energy /= 20;
        double energyEfficiency;
        double tDivideEnergy = Math.cbrt(energy);
        if (energy > 30000) {
            // cbrt(30 000) /
            energyEfficiency = (31.072325 / tDivideEnergy);
            if (energy >= 80000)
                // cbrt(80 000) /
                energyEfficiency *= (43.0886938 / tDivideEnergy);
            energyEfficiency *= energy;
        } else {
            energyEfficiency = energy;
        }
        this.euProduction = (int) (energyEfficiency);
        if (this.boostEu) this.euProduction *= 3;
    }

    public boolean consumeCO2() {
        return this.depleteInput(MISC_MATERIALS.CARBON_DIOXIDE.getFluidStack(this.boostEu ? 3 : 1))
                || this.depleteInput(FluidUtils.getFluidStack("carbondioxide", (this.boostEu ? 3 : 1)));
    }

    public boolean consumeLOH() {
        int LOHamount = (3 * this.euProduction) / 1000;
        return this.depleteInput(FluidUtils.getFluidStack(RocketFuels.Liquid_Hydrogen, LOHamount)); // (40 * ((long)
                                                                                                    // euProduction /
                                                                                                    // 10000))
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
        for (GT_MetaTileEntity_Hatch aDynamo : filterValidMTEs(this.mAllDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            long aTotal = aDynamo.maxAmperesOut() * aVoltage;
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            } else {
                if (aFirstVoltageFound != aVoltage) {
                    aFoundMixedDynamos = true;
                }
            }
            totalOutput += aTotal;
        }

        if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
            explodeMultiblock();
            return false;
        }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;

        for (GT_MetaTileEntity_Hatch aDynamo : filterValidMTEs(this.mAllDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            long powerGain;
            for (int i = 0; i < Math.min(aDynamo.maxAmperesOut(), aAmpsToInject + 1); i++) {
                if (i == Math.min(aDynamo.maxAmperesOut(), aAmpsToInject)) {
                    powerGain = aRemainder;
                } else {
                    powerGain = aVoltage;
                }
                aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(powerGain, false);
                injected += powerGain;
            }
        }
        return injected > 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mRuntime % 20 == 0) {
            if (this.mMufflerHatches.size() == 1
                    && this.mMufflerHatches.get(0) instanceof GT_MetaTileEntity_Hatch_Muffler_Adv tMuffler) {
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
        return 75 * (this.euProduction / 10000);
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "Rocket Engine", "Current Air: " + getAir(),
                "Current Pollution: " + getPollutionPerTick(null),
                "Time until next fuel consumption: " + this.freeFuelTicks,
                "Current Output: " + this.lEUt * this.mEfficiency / 10000 + " EU/t",
                "Fuel Consumption: " + (this.fuelConsumption) + "L/s", "Fuel Value: " + this.fuelValue + " EU/L",
                "Fuel Remaining: " + this.fuelRemaining + " Litres",
                "Current Efficiency: " + this.mEfficiency / 100 + "%",
                (this.getIdealStatus() == this.getRepairStatus()) ? "No Maintainance issues" : "Needs Maintainance" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
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
    public boolean doesBindPlayerInventory() {
        return false;
    }
}
