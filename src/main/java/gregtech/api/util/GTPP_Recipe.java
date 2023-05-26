package gregtech.api.util;

import static gregtech.api.enums.GT_Values.E;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.ProgressBar.Direction;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler.FixedPositionedStack;
import gregtech.nei.NEIRecipeInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;

/**
 * Custom GT Recipe Class
 * 
 * @author Alkalus
 *
 */
public class GTPP_Recipe extends GT_Recipe {

    public GTPP_Recipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs,
            final Object aSpecialItems, final int[] aChances, final FluidStack[] aFluidInputs,
            final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
        super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
        // Logger.SPECIFIC_WARNING(this.getClass().getName()+" | [GregtechRecipe]", "Created new recipe instance for
        // "+ItemUtils.getArrayStackNames(aInputs), 167);
    }

    public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final int aFuelValue, final int aType) {
        this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
    }

    // aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
    public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final ItemStack aOutput2,
            final ItemStack aOutput3, final ItemStack aOutput4, final int aSpecialValue, final int aType) {
        this(
                true,
                new ItemStack[] { aInput1 },
                new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
                null,
                null,
                null,
                null,
                0,
                0,
                Math.max(1, aSpecialValue));

        Logger.WARNING("Switch case method for adding fuels");
        if ((this.mInputs.length > 0) && (aSpecialValue > 0)) {
            switch (aType) {
                // Diesel Generator
                case 0:
                    Logger.WARNING("Added fuel " + aInput1.getDisplayName() + " is ROCKET FUEL - continuing");
                    GTPP_Recipe_Map.sRocketFuels.addRecipe(this);
                    break;
                // Gas Turbine
                case 1:
                    GTPP_Recipe_Map.sGeoThermalFuels.addRecipe(this);
                    break;
                // Thermal Generator
                case 2:
                    GTPP_Recipe_Map.sRTGFuels.addRecipe(this);
                    break;
                // Plasma Generator
                case 4:
                    // Gregtech_Recipe_Map.sPlasmaFuels.addRecipe(this);
                    break;
                // Magic Generator
                case 5:
                    // Gregtech_Recipe_Map.sMagicFuels.addRecipe(this);
                    break;
                // Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
                default:
                    // Gregtech_Recipe_Map.sDenseLiquidFuels.addRecipe(this);
                    break;
            }
        }
    }

    /**
     * Even though this is deprecated, it's still used to keep binary compatibility. (GoodGenerator and GTNHLanthanides
     * reference to `sSimpleWasherRecipes` and `sChemicalDehydratorRecipes`)
     */
    public static class GTPP_Recipe_Map_Internal extends GT_Recipe_Map {

        @Deprecated
        public static final Collection<GTPP_Recipe_Map_Internal> sMappingsEx = new ArrayList<>();

        public GTPP_Recipe_Map_Internal(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }
    }

    public static class GTPP_Recipe_Map {

        public static final GT_Recipe_Map sCokeOvenRecipes = new GT_Recipe_Map(
                new HashSet<>(200),
                "gtpp.recipe.cokeoven",
                "Coke Oven",
                null,
                "",
                2,
                9,
                1,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN);
        public static final GT_Recipe_Map sMatterFab2Recipes = new GT_Recipe_Map(
                new HashSet<>(200),
                "gtpp.recipe.matterfab2",
                "Matter Fabricator",
                null,
                "",
                2,
                0,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).useModularUI(true);

        public static final GT_Recipe_Map_Fuel sRocketFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
                new HashSet<>(10),
                "gtpp.recipe.rocketenginefuel",
                "Rocket Engine Fuel",
                null,
                "",
                0,
                0,
                0,
                0,
                1,
                "Fuel Value: ",
                3000,
                " EU",
                true,
                true).useModularUI(true);

        public static final GT_Recipe_Map sQuantumForceTransformerRecipes = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(20),
                "gtpp.recipe.quantumforcesmelter",
                "Quantum Force Transformer",
                null,
                "",
                6,
                6,
                1,
                0,
                1,
                "Tier: ",
                1,
                E,
                true,
                true).useModularUI(true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, Direction.RIGHT)
                        .setUsualFluidInputCount(6).setUsualFluidOutputCount(6);

        public static final GT_Recipe_Map sGeoThermalFuels = new GT_Recipe_Map(
                new HashSet<>(10),
                "gtpp.recipe.geothermalfuel",
                "GeoThermal Fuel",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                "Fuel Value: ",
                1000,
                " EU",
                true,
                true).useModularUI(true);
        public static final GTPP_Recipe_Map_Internal sChemicalDehydratorRecipes = (GTPP_Recipe_Map_Internal) new GTPP_Recipe_Map_Internal(
                new HashSet<>(200),
                "gtpp.recipe.chemicaldehydrator",
                "Dehydrator",
                null,
                "",
                2,
                9,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN);
        public static final GT_Recipe_Map sVacuumFurnaceRecipes = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(500),
                "gtpp.recipe.vacfurnace",
                "Vacuum Furnace",
                null,
                "",
                9,
                9,
                1,
                0,
                1,
                "Heat Capacity: ",
                1,
                " K",
                false,
                true).setUsualFluidInputCount(3).setUsualFluidOutputCount(3);
        public static final GT_Recipe_Map sAlloyBlastSmelterRecipes = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(200),
                "gtpp.recipe.alloyblastsmelter",
                "Alloy Blast Smelter",
                null,
                "",
                9,
                9,
                1,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setUsualFluidInputCount(3).setUsualFluidOutputCount(3);
        public static final GT_Recipe_Map sSteamTurbineFuels = new GT_Recipe_Map(
                new HashSet<>(10),
                "gtpp.recipe.steamturbinefuel",
                "GeoThermal Fuel",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                "Fuel Value: ",
                1000,
                " EU",
                true,
                false).useModularUI(true);

        // LFTR recipes
        public static final GT_Recipe_Map sLiquidFluorineThoriumReactorRecipes = new GT_Recipe_Map_FluidOnly(
                new HashSet<>(50),
                "gtpp.recipe.lftr",
                "Liquid Fluoride Thorium Reactor",
                null,
                "",
                0,
                0,
                0,
                2,
                0,
                "Power: ",
                1,
                " EU/t per Dynamo",
                true,
                true).setUsualFluidInputCount(6).setUsualFluidOutputCount(6)
                        .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                            final long tEUt = recipeInfo.recipe.mSpecialValue;
                            final int tDuration = recipeInfo.recipe.mDuration;
                            return Arrays.asList(
                                    applyPrefixAndSuffix.apply(recipeInfo.recipe.mSpecialValue),
                                    "Dynamo: " + MathUtils.formatNumbers(tDuration * tEUt) + " EU",
                                    "Total: " + MathUtils.formatNumbers(tDuration * tEUt * 4) + " EU");
                        });

        public static final GT_Recipe_Map sNuclearSaltProcessingPlantRecipes = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(50),
                "gtpp.recipe.nuclearsaltprocessingplant",
                "Nuclear Salt Processing Plant",
                null,
                "",
                0,
                6,
                0,
                0,
                1,
                "",
                0,
                "",
                true,
                true).setUsualFluidInputCount(2).setUsualFluidOutputCount(3);

        // Ore Milling Map
        public static final GT_Recipe_Map sOreMillRecipes = new GT_Recipe_Map(
                new HashSet<>(10000),
                "gtpp.recipe.oremill",
                "Milling",
                null,
                "",
                3,
                3,
                1,
                0,
                1,
                E,
                1,
                E,
                true,
                true) {

            @Override
            protected List<String> handleNEIItemInputTooltip(List<String> currentTip, FixedPositionedStack pStack) {
                if (ItemUtils.isMillingBall(pStack.item)) {
                    currentTip.add(GRAY + "Does not always get consumed in the process");
                } else {
                    super.handleNEIItemInputTooltip(currentTip, pStack);
                }
                return currentTip;
            }

            @Override
            protected void drawNEIOverlayForInput(FixedPositionedStack stack) {
                if (ItemUtils.isMillingBall(stack.item)) {
                    drawNEIOverlayText("NC*", stack);
                } else {
                    super.drawNEIOverlayForInput(stack);
                }
            }
        }.useModularUI(true);

        // Fission Fuel Plant Recipes
        public static final GT_Recipe_Map sFissionFuelProcessing = new GT_Recipe_Map_FluidOnly(
                new HashSet<>(50),
                "gtpp.recipe.fissionfuel",
                "Nuclear Fuel Processing",
                null,
                "",
                0,
                0,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setUsualFluidInputCount(6).setUsualFluidOutputCount(6);

        // Cold Trap
        public static final GT_Recipe_Map sColdTrapRecipes = new GT_Recipe_Map(
                new HashSet<>(10000),
                "gtpp.recipe.coldtrap",
                "Cold Trap",
                null,
                "",
                2,
                9,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN);

        // Reactor Processing Unit
        public static final GT_Recipe_Map sReactorProcessingUnitRecipes = new GT_Recipe_Map(
                new HashSet<>(10000),
                "gtpp.recipe.reactorprocessingunit",
                "Reactor Processing Unit",
                null,
                "",
                2,
                9,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN);

        // Basic Washer Map
        public static final GTPP_Recipe_Map_Internal sSimpleWasherRecipes = (GTPP_Recipe_Map_Internal) new GTPP_Recipe_Map_Internal(
                new HashSet<>(3),
                "gtpp.recipe.simplewasher",
                "Simple Dust Washer",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CAULDRON)
                        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE);

        // Molecular Transformer Map
        public static final GT_Recipe_Map sMolecularTransformerRecipes = new GT_Recipe_Map(
                new HashSet<>(3),
                "gtpp.recipe.moleculartransformer",
                "Molecular Transformer",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MICROSCOPE);

        // Elemental Duplicator Map
        public static final GT_Recipe_Map sElementalDuplicatorRecipes = new GT_Recipe_Map(
                new HashSet<>(3),
                "gtpp.recipe.elementaldupe",
                "Elemental Duplicator",
                null,
                "",
                1,
                1,
                0,
                1,
                1,
                E,
                1,
                E,
                true,
                false);

        public static final GT_Recipe_Map sChemicalPlantRecipes = new GTPP_Recipe_Map_ChemicalPlant(
                new HashSet<>(100),
                "gtpp.recipe.fluidchemicaleactor",
                "Chemical Plant",
                null,
                "",
                4,
                4,
                0,
                2,
                1,
                "Tier: ",
                1,
                E,
                true,
                true);

        // RTG Fuel Map
        public static final GT_Recipe.GT_Recipe_Map_Fuel sRTGFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
                new HashSet<>(10),
                "gtpp.recipe.RTGgenerators",
                "RTG",
                null,
                "",
                1,
                0,
                0,
                0,
                1,
                "Fuel Value: ",
                365,
                " Minecraft Days",
                true,
                true).useModularUI(true);

        // Thermal Boiler map
        public static final GT_Recipe_Map sThermalFuels = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(10),
                "gtpp.recipe.thermalgeneratorfuel",
                "Thermal Generator Fuel",
                null,
                "",
                9,
                9,
                0,
                0,
                1,
                null,
                1000,
                null,
                true,
                true).setUsualFluidInputCount(3).setUsualFluidOutputCount(3);

        // Solar Tower map
        public static final GT_Recipe_Map sSolarTowerRecipes = new GT_Recipe_Map_FluidOnly(
                new HashSet<>(10),
                "gtpp.recipe.solartower",
                "Solar Tower",
                null,
                "",
                0,
                0,
                0,
                0,
                1,
                null,
                1000,
                null,
                true,
                true).useModularUI(true).setNEISpecialInfoFormatter(
                        (recipeInfo, applyPrefixAndSuffix) -> Arrays.asList(
                                "Solar Heater rings boost tier",
                                "R1:T1, R2:T2, R3:T4, R4:T8, R5:T16",
                                "Input Amount = 1000 x T"));

        // Cyclotron recipe map
        public static final GT_Recipe_Map sCyclotronRecipes = new GT_Recipe_Map(
                new HashSet<>(200),
                "gtpp.recipe.cyclotron",
                "COMET - Compact Cyclotron",
                null,
                "",
                9,
                9,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).useModularUI(true);

        // Mini Fusion
        public static final GT_Recipe_Map sSlowFusionRecipes = new GT_Recipe_Map(
                new HashSet<>(50),
                "gtpp.recipe.slowfusionreactor",
                "Mimir - Slow Fusion",
                null,
                "",
                0,
                0,
                0,
                2,
                1,
                "Start: ",
                1,
                " EU",
                true,
                true).useModularUI(true);

        // Special Maps for Multis
        public static final GT_Recipe_Map sFishPondRecipes = new GT_Recipe_Map(
                new HashSet<>(3),
                "gtpp.recipe.fishpond",
                "Zhuhai - Fishing Port",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CAULDRON)
                        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE);
        public static final GT_Recipe_Map sSpargeTowerRecipes = new GT_Recipe_Map(
                new HashSet<>(10000),
                "gtpp.recipe.spargetower",
                "Sparging",
                null,
                "",
                9,
                9,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                false);

        public static final GT_Recipe_Map sAdvFreezerRecipes_GT = new GT_Recipe_Map(
                new HashSet<>(2000),
                "gtpp.recipe.cryogenicfreezer",
                "Cryogenic Freezer",
                null,
                "",
                1,
                1,
                0,
                0,
                1,
                "",
                0,
                "",
                false,
                true).setUsualFluidInputCount(2);
        public static final GT_Recipe_Map sMultiblockCentrifugeRecipes_GT = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(2000),
                "gtpp.recipe.multicentrifuge",
                "Multiblock Centrifuge",
                null,
                "",
                6,
                6,
                0,
                0,
                1,
                "",
                0,
                "",
                false,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT).setUsualFluidInputCount(6)
                        .setUsualFluidOutputCount(6);
        public static final GT_Recipe_Map sMultiblockElectrolyzerRecipes_GT = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(2000),
                "gtpp.recipe.multielectro",
                "Multiblock Electrolyzer",
                null,
                "",
                6,
                6,
                0,
                0,
                1,
                "",
                0,
                "",
                false,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT).setUsualFluidInputCount(6)
                        .setUsualFluidOutputCount(6);

        // internal copy of sChemicalPlantRecipes
        public static final GT_Recipe_Map sChemicalPlant_GT = new GT_Recipe_Map(
                new HashSet<>(2000),
                "gtpp.recipe.temp4",
                "temp4",
                null,
                "",
                0,
                0,
                0,
                0,
                0,
                "",
                0,
                "",
                false,
                false);
        public static final GT_Recipe_Map sMultiblockMixerRecipes_GT = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(2000),
                "gtpp.recipe.multimixer",
                "Multiblock Mixer",
                null,
                "",
                9,
                9,
                0,
                0,
                1,
                "",
                0,
                "",
                false,
                true).setProgressBar(GT_UITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
                        .setUsualFluidInputCount(6).setUsualFluidOutputCount(6);

        public static final GT_Recipe_Map sMultiblockChemicalDehydratorRecipes = new GT_Recipe_Map_LargeNEI(
                new HashSet<>(2000),
                "gtpp.recipe.multidehydrator",
                "Multiblock Dehydrator",
                null,
                "",
                6,
                9,
                0,
                0,
                1,
                "",
                0,
                "",
                false,
                true).setUsualFluidInputCount(3).setUsualFluidOutputCount(3);

        // Semi-Fluid Fuel Map
        public static final GT_Recipe_Map_Fuel sSemiFluidLiquidFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
                new HashSet<>(10),
                "gtpp.recipe.semifluidgeneratorfuels",
                "Semifluid Generator Fuels",
                null,
                "",
                0,
                0,
                0,
                0,
                1,
                "Fuel Value: ",
                1000,
                " EU",
                true,
                true).useModularUI(true);

        // Flotation Cell
        public static final GT_Recipe_Map sFlotationCellRecipes = new GT_Recipe_Map(
                new HashSet<>(10000),
                "gtpp.recipe.flotationcell",
                "Flotation Cell",
                null,
                "",
                6,
                0,
                1,
                1,
                1,
                "",
                1,
                E,
                true,
                true).useModularUI(true);

        // Tree Growth Simulator
        public static final GT_Recipe_Map sTreeSimFakeRecipes = new GT_Recipe_Map(
                new HashSet<>(100),
                "gtpp.recipe.treefarm",
                "Tree Growth Simulator",
                null,
                "",
                1,
                2,
                1,
                0,
                1,
                "",
                1,
                "",
                false,
                true) {

            @Override
            protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {}

            @Override
            protected void drawNEIDurationInfo(NEIRecipeInfo recipeInfo) {}

            @Override
            protected List<String> handleNEIItemOutputTooltip(List<String> currentTip, FixedPositionedStack pStack) {
                if (ModItems.fluidFertBasic != null && pStack.isChanceBased()) {
                    // noinspection deprecation
                    currentTip
                            .add(GRAY + "Outputted if " + ModItems.fluidFertBasic.getLocalizedName() + " is provided");
                } else {
                    super.handleNEIItemOutputTooltip(currentTip, pStack);
                }
                return currentTip;
            }

            @Override
            protected void drawNEIOverlayForOutput(FixedPositionedStack stack) {}
        }.useModularUI(true).setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
            List<String> result = new ArrayList<>();
            if (ModItems.fluidFertBasic != null) {
                result.add("The sapling is not consumed.");
                // noinspection deprecation
                result.add("If " + ModItems.fluidFertBasic.getLocalizedName() + " is provided,");
                result.add("Saplings are made instead");
            }
            return result;
        });
    }

    public static class GTPP_Recipe_Map_ChemicalPlant extends GT_Recipe_Map {

        private static final List<String> tierMaterialNames = Arrays.asList(
                "Bronze",
                "Steel",
                "Aluminium",
                "Stainless Steel",
                "Titanium",
                "Tungsten Steel",
                "Laurenium",
                "Botmium");

        public GTPP_Recipe_Map_ChemicalPlant(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
                String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
            setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_1);
            setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_VIAL_1);
            setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_3);
            setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_VIAL_2);
            setProgressBar(GTPP_UITextures.PROGRESSBAR_FLUID_REACTOR, ProgressBar.Direction.CIRCULAR_CW);
            setProgressBarPos(82, 24);
            setUsualFluidInputCount(4);
            setUsualFluidOutputCount(2);
            setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                int specialValue = recipeInfo.recipe.mSpecialValue;
                String tierMaterial = "";
                for (int i = 0; i < tierMaterialNames.size(); i++) {
                    if (i == specialValue) {
                        tierMaterial = tierMaterialNames.get(i);
                    }
                }
                // blockrenderer uses 1-indexed
                return Collections.singletonList(applyPrefixAndSuffix.apply(specialValue + 1) + " - " + tierMaterial);
            });
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 7, 6, itemInputCount, 1);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper.getGridPositions(itemOutputCount, 106, 15, 2);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 7, 41, fluidInputCount, 1);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getGridPositions(fluidOutputCount, 142, 15, 1, fluidOutputCount);
        }

        @Override
        protected List<String> handleNEIItemInputTooltip(List<String> currentTip, FixedPositionedStack pStack) {
            if (ItemUtils.isCatalyst(pStack.item)) {
                currentTip.add(GRAY + "Does not always get consumed in the process");
                currentTip.add(GRAY + "Higher tier pipe casings allow this item to last longer");
            } else {
                super.handleNEIItemInputTooltip(currentTip, pStack);
            }
            return currentTip;
        }

        @Override
        protected void drawNEIOverlayForInput(FixedPositionedStack stack) {
            if (ItemUtils.isCatalyst(stack.item)) {
                drawNEIOverlayText("NC*", stack);
            } else {
                super.drawNEIOverlayForInput(stack);
            }
        }
    }
}
