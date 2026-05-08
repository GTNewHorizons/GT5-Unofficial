package gregtech.common.tileentities.machines.multi.xlturbines;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gtPlusPlus.core.lib.GTPPCore.RANDOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

public class MTEXLTurbineGas extends MTEXLTurbineBase {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private static IStructureDefinition<MTEXLTurbineBase> STRUCTURE_DEFINITION = null;
    private static final HashSet<Fluid> BLACKLIST = new HashSet<>();

    static {
        BLACKLIST.add(
            Materials.Benzene.getFluid(0)
                .getFluid());
    }

    public MTEXLTurbineGas(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbineGas(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEXLTurbineGas(mName);
    }

    @Override
    public IStructureDefinition<MTEXLTurbineBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEXLTurbineBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "         ", "   GGG   ", "  GEEEG  ", " GEEEEEG ", " GEE~EEG ", " GEEEEEG ", "  GEEEG  ",
                            "   GGG   ", "         " },
                        { "         ", "   GGG   ", "  GD  G  ", " G  D DG ", " G DFD G ", " GD D  G ", "  G  DG  ",
                            "   GGG   ", "         " },
                        { "         ", "    G    ", "   GGG   ", "  G B G  ", " GGBFBGG ", "  G B G  ", "  GGGG   ",
                            "    G    ", "         " },
                        { "         ", "         ", "   GGG   ", "  G B G  ", "  GBFBG  ", "  G B G  ", "   GGG   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A B A  ", "  ABFBA  ", "  A B A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A   A  ", "  A F A  ", "  A   A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A B A  ", "  ABFBA  ", "  A B A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   GGG   ", "  G B G  ", "  GBFBG  ", "  G B G  ", "   GGG   ",
                            "         ", "         " },
                        { "         ", "   GGG   ", "  GGGGG  ", " GG   GG ", " GG F GG ", " GG   GG ", "  GGGGG  ",
                            "   GGG   ", "         " },
                        { "         ", "   G G   ", "  E E E  ", " G     G ", "  E F E  ", " G     G ", "  E E E  ",
                            "   G G   ", "         " },
                        { "         ", "   G G   ", "  E E E  ", " G  D  G ", "  EDCDE  ", " G  D  G ", "  E E E  ",
                            "   G G   ", "         " },
                        { "         ", "   G G   ", "  E E E  ", " G     G ", "  E F E  ", " G     G ", "  E E E  ",
                            "   G G   ", "         " },
                        { "         ", "   GGG   ", "  G   G  ", " G  D  G ", " G DCD G ", " G  D  G ", "  G   G  ",
                            "   GGG   ", "         " },
                        { "   G G   ", "  G G G  ", " G     G ", "G       G", " G  F  G ", "G       G", " G     G ",
                            "  G G G  ", "   G G   " },
                        { "   GGG   ", " GG   GG ", " G     G ", "G   D   G", "G  DCD  G", "G   D   G", " G     G ",
                            " GG   GG ", "   GGG   " },
                        { "  GGGGG  ", " E     E ", "G       G", "G       G", "G   F   G", "G       G", "G       G",
                            " E     E ", "  GGGGG  " },
                        { "  GGAGG  ", " E     E ", "G  D    G", "G   D D G", "A  DCD  A", "G D D   G", "G    D  G",
                            " E     E ", "  GGAGG  " },
                        { "  GAAAG  ", " E     E ", "G       G", "A       A", "A   F   A", "A       A", "G       G",
                            " E     E ", "  GAAAG  " },
                        { "  GAAAG  ", " E     E ", "G    D  G", "A D D   A", "A  DCD  A", "A   D D A", "G  D    G",
                            " E     E ", "  GAAAG  " },
                        { "  GAAAG  ", " E     E ", "G       G", "A       A", "A   F   A", "A       A", "G       G",
                            " E     E ", "  GAAAG  " },
                        { "  GAAAG  ", " ED   DE ", "GDDD  DDG", "A  D DD A", "A   C   A", "A DD D  A", "GDD  DDDG",
                            " ED   DE ", "  GAAAG  " },
                        { "  GAAAG  ", " E     E ", "G       G", "A       A", "A   F   A", "A       A", "G       G",
                            " E     E ", "  GAAAG  " },
                        { "  GGAGG  ", " ED   DE ", "GDD  DDDG", "G DD D  G", "A   C   A", "G  D DD G", "GDDD  DDG",
                            " ED   DE ", "  GGAGG  " },
                        { "  GGGGG  ", " E     E ", "G       G", "G       G", "G   F   G", "G       G", "G       G",
                            " E     E ", "  GGGGG  " },
                        { " GGGGGGG ", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G",
                            "G       G", " GGGGGGG " },
                        { " GGGGGGG ", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G",
                            "G       G", " GGGGGGG " } })
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.MVSolenoidSuperconductorCoil.asElement())
                .addElement('C', Casings.SteelGearBoxCasing.asElement())
                .addElement('D', getRotorCasing().asElement())
                .addElement('E', ofFrame(getFrameMaterial()))
                .addElement('F', Casings.TurbineShaft.asElement())
                .addElement(
                    'G',
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
    protected Casings getCasing() {
        return Casings.ReinforcedGasTurbineCasing;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.StainlessSteel;
    }

    @Override
    protected Casings getRotorCasing() {
        return Casings.SteelPipeCasing;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Gas Turbine, XLGT")
            .addInfo("Runs as fast as 16 Large Turbines of the same type, takes the space of 12")
            .addInfo("Right-click with screwdriver to enable loose fit")
            .addInfo("Optimal flow will increase or decrease depending on fitting")
            .addInfo("Loose fit increases flow in exchange for efficiency")
            .addTecTechHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 9, 26, false)
            .addController("Front center")
            .addCasingInfoMin("Reinforced Gas Turbine Casing", 340, false)
            .addCasingInfoExactly("Any Tiered Glass", 104, false)
            .addCasingInfoExactly("Stainless Steel Frame Box", 80, false)
            .addCasingInfoExactly("Steel Pipe Casing", 76, false)
            .addCasingInfoExactly("MV Solenoid Superconductor Coil", 20, false)
            .addCasingInfoExactly("Turbine Shaft", 16, false)
            .addCasingInfoExactly("Steel Gear Box", 7, false)
            .addInputBus("Any Turbine Casing (Min 1)", 1)
            .addInputHatch("Any Turbine Casing (Min 1)", 1)
            .addDynamoHatch("Any Turbine Casing (Min 1)", 1)
            .addMaintenanceHatch("Any Turbine Casing (Min 1)", 1)
            .addMufflerHatch("Any Turbine Casing (x4)", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public int minCasingAmount() {
        return 340;
    }

    @Override
    public boolean requiresMufflerHatch() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 4000;
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
        return RecipeMaps.gasTurbineFuels;
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
    public @NotNull CheckRecipeResult checkProcessing() {
        List<FluidStack> fluids = getStoredFluids();
        for (FluidStack fluid : fluids) {
            if (fluid != null && BLACKLIST.contains(fluid.getFluid())) {
                return SimpleCheckRecipeResult.ofFailure("fuel_blacklisted");
            }
        }
        return super.checkProcessing();
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        if (!aFluids.isEmpty()) {
            int tEU = 0;
            int actualOptimalFlow = 0;
            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0); // Identify a SINGLE type of fluid to process.
            // Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);
            if (fuelValue <= 0) {
                return 0;
            }

            if (turbine.getOptimalGasEUt() < fuelValue) {
                // turbine too weak and/or fuel too powerful
                // at least consume 1L
                this.realOptFlow = 1;
                // wastes the extra fuel and generates aOptFlow directly
                depleteInput(new FluidStack(firstFuelType, 1));
                this.storedFluid += 1;
                return GTUtility.safeInt((long) (turbine.getOptimalGasEUt()));
            }

            actualOptimalFlow = GTUtility.safeInt(
                (long) (getSpeedMultiplier()
                    * ((isLooseMode() ? turbine.getOptimalLooseGasFlow() : turbine.getOptimalGasFlow()) / fuelValue)));
            this.realOptFlow = actualOptimalFlow;

            int remainingFlow = GTUtility.safeInt((long) (actualOptimalFlow * 1.25f)); // Allowed to use up to 125% of
            // optimal flow.
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up to 125% of optimal flow
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += flow;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GTUtility.safeInt((long) totalFlow * fuelValue);

            if (totalFlow != actualOptimalFlow) {
                float efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
                tEU *= efficiency;
            }
            tEU = GTUtility
                .safeInt((long) (tEU * (isLooseMode() ? turbine.getLooseGasEfficiency() : turbine.getGasEfficiency())));

            return tEU;
        }
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (RANDOM.nextInt(4) == 0) ? 0 : 1;
    }
}
