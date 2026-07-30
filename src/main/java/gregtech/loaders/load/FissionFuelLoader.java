package gregtech.loaders.load;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.SKIP_CELL_RECIPE_GENERATION;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;

public class FissionFuelLoader implements Runnable {

    @Override
    public void run() {
        // Remove IC2 recipes
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Uranium_238.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Uranium_235.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Uranium_235_Small.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Plutonium.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Plutonium_Small.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Uranium_Fuel.get(1));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_MOX_Fuel.get(1));

        // Lithium
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Fuel_Rod_Empty.get(1),
                MaterialLibAPI.getStack(Materials.Lithium, Shapes.dustTiny, (int) (1)))
            .itemOutputs(ItemList.RodLithium.get(1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodLithium.get(1))
            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Tritium, Materials2FluidShapes.fluidGas, (int) (32)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(fluidExtractionRecipes);

        // Glowstone
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Fuel_Rod_Empty.get(1),
                MaterialLibAPI.getStack(Materials.Glowstone, Shapes.dust, (int) (9)))
            .itemOutputs(ItemList.RodGlowstone.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (250)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodGlowstone.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sunnarium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Glowstone, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        if (!Mods.NuclearHorizons.isModLoaded()) {
            // Thorium
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(1),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (3)))
                .itemOutputs(ItemList.RodThorium.get(1L))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodThorium.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(2)
                .itemOutputs(ItemList.RodThorium2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodThorium.get(4),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stickLong, (int) (6)))
                .circuit(4)
                .itemOutputs(ItemList.RodThorium4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodThorium2.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(5)
                .itemOutputs(ItemList.RodThorium4.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium.get(1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dustSmall, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium2.get(1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (3)))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium4.get(1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (4)),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (6)))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Polonium, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Thallium, Shapes.dustTiny, 1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (30)))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Polonium, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Thallium, Shapes.dustTiny, 1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (30)))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Polonium, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Thallium, Shapes.dustTiny, 1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (30)))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);

            // Uranium
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Uranium_238.get(6), ItemList.IC2_Uranium_235_Small.get(3))
                .itemOutputs(ItemList.IC2_Uranium_Fuel.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(1_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), ItemList.IC2_Uranium_Fuel.get(1))
                .itemOutputs(ItemList.RodUranium.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodUranium.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(2)
                .itemOutputs(ItemList.RodUranium2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodUranium.get(4),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stickLong, (int) (6)))
                .circuit(4)
                .itemOutputs(ItemList.RodUranium4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodUranium2.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(5)
                .itemOutputs(ItemList.RodUranium4.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(1),
                    ItemList.IC2_Uranium_238.get(4),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium2.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(2),
                    ItemList.IC2_Uranium_238.get(8),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (3)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium4.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(4),
                    ItemList.IC2_Uranium_238.get(16),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (6)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Uranium232, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (60)))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Uranium232, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (60)))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Uranium232, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dustSmall, 1),
                    MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dustSmall, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (60)))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);

            // MOX
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Uranium_238.get(6), ItemList.IC2_Plutonium.get(3))
                .itemOutputs(ItemList.IC2_MOX_Fuel.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(1_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), ItemList.IC2_MOX_Fuel.get(1))
                .itemOutputs(ItemList.RodMOX.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodMOX.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(2)
                .itemOutputs(ItemList.RodMOX2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodMOX.get(4),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stickLong, (int) (6)))
                .circuit(4)
                .itemOutputs(ItemList.RodMOX4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodMOX2.get(2),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.stick, (int) (4)))
                .circuit(5)
                .itemOutputs(ItemList.RodMOX4.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(1),
                    ItemList.IC2_Plutonium.get(3),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX2.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(2),
                    ItemList.IC2_Plutonium.get(6),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (3)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX4.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(4),
                    ItemList.IC2_Plutonium.get(12),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (6)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium238, Shapes.dustTiny, 1),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (90)))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium238, Shapes.dustTiny, 1),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (90)))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (2)),
                    MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)),
                    MaterialLibAPI.getStack(Materials.Plutonium238, Shapes.dustTiny, 1),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dustTiny, (int) (1)))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (90)))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }

        // High Density Uranium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1), ItemRefer.High_Density_Uranium_Nugget.get(1))
            .itemOutputs(ItemList.RodHighDensityUranium.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityUranium.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodHighDensityUranium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityUranium2.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodHighDensityUranium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityUranium.get(4),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodHighDensityUranium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 50_00, 100_00, 90_00, 12_50, 7_50)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (32)),
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 50_00, 100_00, 90_00, 12_50, 7_50)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 25_00, 15_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (64)),
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 25_00, 15_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (128)),
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TungstenCarbide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);

        // High Density Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1), ItemRefer.High_Density_Plutonium_Nugget.get(1))
            .itemOutputs(ItemList.RodHighDensityPlutonium.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityPlutonium.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodHighDensityPlutonium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityPlutonium2.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodHighDensityPlutonium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodHighDensityPlutonium.get(4),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodHighDensityPlutonium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (32)),
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (2)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (64)),
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (2)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (4)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (128)),
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (4)))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HSSS, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SKIP_CELL_RECIPE_GENERATION, true)
            .addTo(centrifugeRecipes);

        // Excited Uranium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuelExcitedState,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .itemOutputs(ItemList.RodExcitedUranium.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(RecipeMaps.cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedUranium.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodExcitedUranium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedUranium2.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodExcitedUranium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedUranium.get(4),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodExcitedUranium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (500)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UraniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Excited Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuelExcitedState,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .itemOutputs(ItemList.RodExcitedPlutonium.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(RecipeMaps.cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedPlutonium.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodExcitedPlutonium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedPlutonium2.get(2),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodExcitedPlutonium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodExcitedPlutonium.get(4),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodExcitedPlutonium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (500)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PlutoniumBasedLiquidFuelDepleted,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Naquadah
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, (int) (3)))
            .itemOutputs(ItemList.RodNaquadah.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4)))
            .circuit(2)
            .itemOutputs(ItemList.RodNaquadah2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (6)))
            .circuit(4)
            .itemOutputs(ItemList.RodNaquadah4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah2.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4)))
            .circuit(5)
            .itemOutputs(ItemList.RodNaquadah4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(32L),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (64L)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (64L)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (64L)),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 64))
            .itemOutputs(ItemList.RodNaquadah32.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustSmall, (int) (2)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dustTiny, (int) (2)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (1)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah2.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dustTiny, (int) (4)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (18)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (2)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah4.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dustTiny, (int) (8)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (38)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (4)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah32.get(1))
            .itemOutputs(
                ItemList.DepletedRodNaquadah4.get(8),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (16)))
            .outputChances(100_00, 50_00, 50_00, 100_00, 100_00, 100_00)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Naquadria
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, (int) (3)))
            .itemOutputs(ItemList.RodNaquadria.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4)))
            .circuit(2)
            .itemOutputs(ItemList.RodNaquadria2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria.get(4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (6)))
            .circuit(4)
            .itemOutputs(ItemList.RodNaquadria4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria2.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4)))
            .circuit(5)
            .itemOutputs(ItemList.RodNaquadria4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dustSmall, (int) (2)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustTiny, (int) (2)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (1)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria2.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustTiny, (int) (4)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (18)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (2)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria4.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustTiny, (int) (8)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (38)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (4)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        // Tiberium
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 3))
            .itemOutputs(ItemList.RodTiberium.get(1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodTiberium.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4L)))
            .itemOutputs(ItemList.RodTiberium2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodTiberium.get(4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, (int) (6L)))
            .itemOutputs(ItemList.RodTiberium4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodTiberium2.get(2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (4L)))
            .itemOutputs(ItemList.RodTiberium4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dustSmall, 2),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (1)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (1)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium2.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (18)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (2)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (2)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium4.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (38)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (4)))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (4)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
    }
}
