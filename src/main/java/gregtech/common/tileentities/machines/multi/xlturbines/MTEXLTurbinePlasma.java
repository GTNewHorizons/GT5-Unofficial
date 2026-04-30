package gregtech.common.tileentities.machines.multi.xlturbines;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.util.math.MathUtils;

public class MTEXLTurbinePlasma extends MTEXLTurbineBase {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 1;
    private static IStructureDefinition<MTEXLTurbineBase> STRUCTURE_DEFINITION = null;

    public MTEXLTurbinePlasma(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbinePlasma(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEXLTurbinePlasma(mName);
    }

    @Override
    public IStructureDefinition<MTEXLTurbineBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEXLTurbineBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "         ", "         ", "   HHH   ", "  H   H  ", "  H   H  ", "  H   H  ", "   HHH   ",
                            "         ", "         " },
                        { "         ", "         ", "   HFH   ", "  HFFFH  ", "  FF~FF  ", "  HFFFH  ", "   HFH   ",
                            "         ", "         " },
                        { "         ", "         ", "   HFH   ", "  H D H  ", "  FDGDF  ", "  H D H  ", "   HFH   ",
                            "         ", "         " },
                        { "         ", "         ", "   HFH   ", "  H   H  ", "  F G F  ", "  H   H  ", "   HFH   ",
                            "         ", "         " },
                        { "         ", "         ", "   HHH   ", "  H   H  ", "  H G H  ", "  H   H  ", "   HHH   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A   A  ", "  A G A  ", "  A   A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A   A  ", "  A G A  ", "  A   A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A   A  ", "  A G A  ", "  A   A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   HHH   ", "  H   H  ", "  H G H  ", "  H   H  ", "   HHH   ",
                            "         ", "         " },
                        { "         ", "    H    ", "  HH HH  ", "  H   H  ", " H  G  H ", "  H   H  ", "  HH HH  ",
                            "    H    ", "         " },
                        { "         ", "    H    ", "  HH HH  ", "  H   H  ", " H  G  H ", "  H   H  ", "  HH HH  ",
                            "    H    ", "         " },
                        { "         ", "   H H   ", "  E E E  ", " H  D  H ", "  EDCDE  ", " H  D  H ", "  E E E  ",
                            "   H H   ", "         " },
                        { "         ", "   H H   ", "  E E E  ", " H     H ", "  E G E  ", " H     H ", "  E E E  ",
                            "   H H   ", "         " },
                        { "    H    ", "  HHHHH  ", " HH   HH ", " H  D  H ", "HH DCD HH", " H  D  H ", " HH   HH ",
                            "  HHHHH  ", "    H    " },
                        { "    H    ", " HHHHHHH ", " HH   HH ", " H     H ", "HH  G  HH", " H     H ", " HH   HH ",
                            " HHHHHHH ", "    H    " },
                        { "    H    ", " FBBBBBF ", " B     B ", " B  D  B ", "HB DCD BH", " B  D  B ", " B     B ",
                            " FBBBBBF ", "    H    " },
                        { "    H    ", " FBBBBBF ", " B     B ", " B     B ", "HB  G  BH", " B     B ", " B     B ",
                            " FBBBBBF ", "    H    " },
                        { "    H    ", " FBBBBBF ", " B D   B ", " B  D DB ", "HB DCD BH", " BD D  B ", " B   D B ",
                            " FBBBBBF ", "    H    " },
                        { "  HHHHH  ", " H     H ", "H       H", "H       H", "H   G   H", "H       H", "H       H",
                            " H     H ", "  HHHHH  " },
                        { "  HHHHH  ", " H     H ", "H    D  H", "H D D   H", "H  DCD  H", "H   D D H", "H  D    H",
                            " H     H ", "  HHHHH  " },
                        { "  BBBBB  ", " B     B ", "B       B", "B       B", "B   G   B", "B       B", "B       B",
                            " B     B ", "  BBBBB  " },
                        { "  BBBBB  ", " BD   DB ", "BDDD  DDB", "B  D DD B", "B   C   B", "B DD D  B", "BDD  DDDB",
                            " BD   DB ", "  BBBBB  " },
                        { "  BBBBB  ", " B     B ", "B       B", "B       B", "B   G   B", "B       B", "B       B",
                            " B     B ", "  BBBBB  " },
                        { "  HHHHH  ", " HD   DH ", "HDD  DDDH", "H DD D  H", "H   C   H", "H  D DD H", "HDDD  DDH",
                            " HD   DH ", "  HHHHH  " },
                        { "  HHHHH  ", " H     H ", "H       H", "H       H", "H   G   H", "H       H", "H       H",
                            " H     H ", "  HHHHH  " },
                        { "    H    ", "  HH HH  ", " H     H ", " H     H ", "H       H", " H     H ", " H     H ",
                            "  HH HH  ", "    H    " } })
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.ZPMSolenoidSuperconductorCoil.asElement())
                .addElement('C', Casings.SteelGearBoxCasing.asElement())
                .addElement('D', getRotorCasing().asElement())
                .addElement('E', Casings.NaquadahCoilBlock.asElement())
                .addElement('F', ofFrame(getFrameMaterial()))
                .addElement('G', Casings.TurbineShaft.asElement())
                .addElement(
                    'H',
                    lazy(
                        t -> buildHatchAdder(MTEXLTurbineBase.class).atLeastList(t.getAllowedHatches())
                            .casingIndex(t.getCasing().textureId)
                            .hint(1)
                            .buildAndChain(
                                onElementPass(
                                    x -> ++casingAmount,
                                    t.getCasing()
                                        .asElement()))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected int getStructureOffsetX() {
        return OFFSET_X;
    }

    @Override
    protected int getStructureOffsetY() {
        return OFFSET_Y;
    }

    @Override
    protected int getStructureOffsetZ() {
        return OFFSET_Z;
    }

    @Override
    public boolean requiresOutputHatch() {
        return true;
    }

    @Override
    protected Casings getCasing() {
        return Casings.ReinforcedPlasmaTurbineCasing;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.TungstenSteel;
    }

    @Override
    protected Casings getRotorCasing() {
        return Casings.TungstensteelPipeCasing;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Plasma Turbine, XLPT")
            .addInfo("Runs as fast as 16 Large Turbines of the same type, takes the space of 12")
            .addInfo("Right-click with screwdriver to enable loose fit")
            .addInfo("Optimal flow will increase or decrease depending on fitting")
            .addInfo("Loose fit increases flow in exchange for efficiency")
            .addInfo("Plasma fuel efficiency is lower for high tier turbines when using low-grade plasmas")
            .addInfo("Efficiency = ((FuelValue / 200,000)^2) / (EU per Turbine)")
            .addTecTechHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 9, 26, false)
            .addController("Front center")
            .addCasingInfoMin("Reinforced Plasma Turbine Casing", 291, false)
            .addCasingInfoExactly("ZPM Solenoid Superconductor Coil", 132, false)
            .addCasingInfoExactly("Tungstensteel Pipe Casing", 72, false)
            .addCasingInfoExactly("Any Tiered Glass", 36, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 34, false)
            .addCasingInfoExactly("Naquadah Coil Block", 16, false)
            .addCasingInfoExactly("Turbine Shaft", 16, false)
            .addCasingInfoExactly("Steel Gear Box Casing", 7, false)
            .addInputBus("Any Turbine Casing (Min 1)", 1)
            .addInputHatch("Any Turbine Casing (Min 1)", 1)
            .addOutputHatch("Any Turbine Casing (Min 1)", 1)
            .addDynamoHatch("Any Turbine Casing (Min 1)", 1)
            .addMaintenanceHatch("Any Turbine Casing (Min 1)", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    private int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) {
            return 0;
        }
        GTRecipe tFuel = getRecipeMap().getBackend()
            .findFuel(aLiquid);
        if (tFuel != null) {
            return tFuel.mSpecialValue;
        }
        return 0;
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.plasmaFuels;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -20;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public int minCasingAmount() {
        return 285;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        try {
            if (!areAllTurbinesTheSame()) {
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
                return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
            }

            ItemStack turbineItem = getPrimaryTurbine();
            if (turbineItem == null) {
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
                return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
            }
            TurbineStatCalculator turbine = new TurbineStatCalculator(
                (MetaGeneratedTool) turbineItem.getItem(),
                turbineItem);

            ArrayList<FluidStack> tFluids = getStoredFluids();

            if (!tFluids.isEmpty()) {
                if (baseEff == 0 || optFlow == 0
                    || counter >= 512
                    || getBaseMetaTileEntity().hasWorkJustBeenEnabled()
                    || getBaseMetaTileEntity().hasInventoryBeenModified()) {
                    counter = 0;

                    float aTotalBaseEff = turbine.getPlasmaEfficiency() * 10000;
                    float aTotalOptimalFlow = turbine.getOptimalPlasmaFlow() * getSpeedMultiplier();
                    double aEUPerTurbine = turbine.getOptimalPlasmaEUt();

                    if (aTotalOptimalFlow < 0) {
                        aTotalOptimalFlow = 100;
                    }

                    Materials turbineMaterial = MetaGeneratedTool.getPrimaryMaterial(turbineItem);
                    flowMultipliers[0] = turbineMaterial.mSteamMultiplier;
                    flowMultipliers[1] = turbineMaterial.mGasMultiplier;
                    flowMultipliers[2] = turbineMaterial.mPlasmaMultiplier;
                    baseEff = MathUtils.roundToClosestInt(aTotalBaseEff);
                    optFlow = MathUtils.roundToClosestInt(aTotalOptimalFlow);
                    euPerTurbine = MathUtils.roundToClosestInt(aEUPerTurbine);
                    if (optFlow <= 0 || baseEff <= 0) {
                        stopMachine(ShutDownReasonRegistry.NONE);
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                } else {
                    counter++;
                }
            }

            long newPower = fluidIntoPower(tFluids, turbine);

            int fuelValue = 0;
            if (!tFluids.isEmpty()) {
                fuelValue = getFuelValue(new FluidStack(tFluids.get(0), 0));
            }
            if (euPerTurbine > 0) {
                float magicValue = (fuelValue * 0.005f) * (fuelValue * 0.005f);
                float efficiencyLoss = Math.min(1.0f, magicValue / euPerTurbine);
                newPower *= efficiencyLoss;
            }

            long difference = newPower - lEUt;
            int maxChangeAllowed = Math.max(200, GTUtility.safeInt(Math.abs(difference) / 5));

            if (Math.abs(difference) > maxChangeAllowed) {
                int change = maxChangeAllowed * (difference > 0 ? 1 : -1);
                lEUt += change;
            } else {
                lEUt = newPower;
            }
            if (lEUt <= 0) {
                lEUt = 0;
                mEfficiency = 0;
                return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            } else {
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 200;
                return CheckRecipeResultRegistry.GENERATING;
            }
        } catch (Exception t) {
            t.printStackTrace();
        }
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        if (!aFluids.isEmpty()) {
            int tEU = 0;
            int actualOptimalFlow = 0;

            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0);
            int fuelValue = getFuelValue(firstFuelType);
            if (fuelValue <= 0) {
                return 0;
            }

            actualOptimalFlow = GTUtility.safeInt(
                (long) ((getSpeedMultiplier()
                    * (isLooseMode() ? turbine.getOptimalLoosePlasmaFlow() : turbine.getOptimalPlasmaFlow())
                    * 20) / (double) fuelValue));
            realOptFlow = actualOptimalFlow;

            int remainingFlow = GTUtility.safeInt((long) (actualOptimalFlow * 1.25f));
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow);
                    depleteInput(new FluidStack(aFluid, flow));
                    storedFluid += flow;
                    remainingFlow -= flow;
                    totalFlow += flow;
                }
            }
            String fn = FluidRegistry.getFluidName(firstFuelType);
            String[] nameSegments = fn.split("\\.", 2);
            if (nameSegments.length == 2) {
                String outputName = nameSegments[1];
                FluidStack output = FluidRegistry.getFluidStack(outputName, totalFlow);
                if (output == null) {
                    output = FluidRegistry.getFluidStack("molten." + outputName, totalFlow);
                }
                if (output != null) {
                    addOutput(output);
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GTUtility.safeInt((long) ((fuelValue / 20D) * (double) totalFlow));

            if (totalFlow != actualOptimalFlow) {
                double efficiency = 1.0D - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
                tEU = (int) (tEU * efficiency);
            }
            tEU = GTUtility.safeInt(
                (long) ((isLooseMode() ? turbine.getLoosePlasmaEfficiency() : turbine.getPlasmaEfficiency()) * tEU));

            return tEU;
        }
        return 0;
    }
}
