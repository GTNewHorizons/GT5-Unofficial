package gregtech.loaders.load;

import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

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
            .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), Materials.Lithium.getDustTiny(1))
            .itemOutputs(ItemList.RodLithium.get(1))
            .duration(16 * TICKS)
            .eut(64)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodLithium.get(1))
            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(1))
            .fluidOutputs(Materials.Tritium.getGas(32))
            .duration(16 * TICKS)
            .eut(64)
            .addTo(fluidExtractionRecipes);

        // Glowstone
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), Materials.Glowstone.getDust(9))
            .itemOutputs(ItemList.RodGlowstone.get(1))
            .fluidInputs(Materials.Helium.getGas(250))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodGlowstone.get(1))
            .itemOutputs(Materials.Sunnarium.getDust(1), Materials.Glowstone.getDust(2), Materials.Iron.getDust(1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(thermalCentrifugeRecipes);

        if (!Mods.NuclearHorizons.isModLoaded()) {
            // Thorium
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), Materials.Thorium.getDust(3))
                .itemOutputs(ItemList.RodThorium.get(1L))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodThorium.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
                .circuit(2)
                .itemOutputs(ItemList.RodThorium2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodThorium.get(4),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6))
                .circuit(4)
                .itemOutputs(ItemList.RodThorium4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodThorium2.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
                .circuit(5)
                .itemOutputs(ItemList.RodThorium4.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium.get(1))
                .itemOutputs(
                    Materials.Lutetium.getDustSmall(2),
                    Materials.Thorium.getDust(1),
                    Materials.Iron.getDust(1))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium2.get(1))
                .itemOutputs(Materials.Lutetium.getDust(1), Materials.Thorium.getDust(2), Materials.Iron.getDust(3))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium4.get(1))
                .itemOutputs(Materials.Lutetium.getDust(2), Materials.Thorium.getDust(4), Materials.Iron.getDust(6))
                .duration(25 * SECONDS)
                .eut(48)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Thorium.getDust(2),
                    MaterialsElements.getInstance().THORIUM232.getDust(1),
                    Materials.Lutetium.getDustSmall(1),
                    MaterialsElements.getInstance().POLONIUM.getSmallDust(1),
                    MaterialsElements.getInstance().THALLIUM.getTinyDust(1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(30))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Thorium.getDust(2),
                    MaterialsElements.getInstance().THORIUM232.getDust(1),
                    Materials.Lutetium.getDustSmall(1),
                    MaterialsElements.getInstance().POLONIUM.getSmallDust(1),
                    MaterialsElements.getInstance().THALLIUM.getTinyDust(1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(30))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodThorium4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Thorium.getDust(2),
                    MaterialsElements.getInstance().THORIUM232.getDust(1),
                    Materials.Lutetium.getDustSmall(1),
                    MaterialsElements.getInstance().POLONIUM.getSmallDust(1),
                    MaterialsElements.getInstance().THALLIUM.getTinyDust(1))
                .outputChances(100_00, 100_00, 50_00, 50_00, 50_00, 25_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(30))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);

            // Uranium
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Uranium_238.get(6), ItemList.IC2_Uranium_235_Small.get(3))
                .itemOutputs(ItemList.IC2_Uranium_Fuel.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(1_000))
                .duration(20 * SECONDS)
                .eut(120)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), ItemList.IC2_Uranium_Fuel.get(1))
                .itemOutputs(ItemList.RodUranium.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodUranium.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
                .circuit(2)
                .itemOutputs(ItemList.RodUranium2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.RodUranium.get(4),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6))
                .circuit(4)
                .itemOutputs(ItemList.RodUranium4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodUranium2.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
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
                    Materials.Iron.getDust(1))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium2.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(2),
                    ItemList.IC2_Uranium_238.get(8),
                    Materials.Iron.getDust(3))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium4.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(4),
                    ItemList.IC2_Uranium_238.get(16),
                    Materials.Iron.getDust(6))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Uranium.getDust(2),
                    MaterialsElements.getInstance().URANIUM232.getSmallDust(1),
                    MaterialsElements.getInstance().URANIUM233.getSmallDust(1),
                    Materials.Uranium235.getDustSmall(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(60))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Uranium.getDust(2),
                    MaterialsElements.getInstance().URANIUM232.getSmallDust(1),
                    MaterialsElements.getInstance().URANIUM233.getSmallDust(1),
                    Materials.Uranium235.getDustSmall(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(60))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodUranium4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Uranium.getDust(2),
                    MaterialsElements.getInstance().URANIUM232.getSmallDust(1),
                    MaterialsElements.getInstance().URANIUM233.getSmallDust(1),
                    Materials.Uranium235.getDustSmall(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 10_00, 10_00, 10_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(60))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);

            // MOX
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Uranium_238.get(6), ItemList.IC2_Plutonium.get(3))
                .itemOutputs(ItemList.IC2_MOX_Fuel.get(1))
                .fluidInputs(GTModHandler.getIC2Coolant(1_000))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Fuel_Rod_Empty.get(1), ItemList.IC2_MOX_Fuel.get(1))
                .itemOutputs(ItemList.RodMOX.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodMOX.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
                .circuit(2)
                .itemOutputs(ItemList.RodMOX2.get(1))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodMOX.get(4), GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6))
                .circuit(4)
                .itemOutputs(ItemList.RodMOX4.get(1))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.RodMOX2.get(2), GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4))
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
                    Materials.Iron.getDust(1))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX2.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(2),
                    ItemList.IC2_Plutonium.get(6),
                    Materials.Iron.getDust(3))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX4.get(1))
                .itemOutputs(
                    ItemList.IC2_Plutonium_Small.get(4),
                    ItemList.IC2_Plutonium.get(12),
                    Materials.Iron.getDust(6))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(thermalCentrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX.get(8))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Plutonium.getDust(2),
                    Materials.Plutonium241.getDustTiny(1),
                    Materials.Plutonium.getDustTiny(1),
                    MaterialsElements.getInstance().PLUTONIUM238.getTinyDust(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(90))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX2.get(4))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Plutonium.getDust(2),
                    Materials.Plutonium241.getDustTiny(1),
                    Materials.Plutonium.getDustTiny(1),
                    MaterialsElements.getInstance().PLUTONIUM238.getTinyDust(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(90))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.DepletedRodMOX4.get(2))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    Materials.Plutonium.getDust(2),
                    Materials.Plutonium241.getDustTiny(1),
                    Materials.Plutonium.getDustTiny(1),
                    MaterialsElements.getInstance().PLUTONIUM238.getTinyDust(1),
                    Materials.Plutonium.getDustTiny(1))
                .outputChances(100_00, 100_00, 5_00, 5_00, 5_00, 5_00)
                .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(90))
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
            .itemInputs(ItemList.RodHighDensityUranium.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodHighDensityUranium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodHighDensityUranium2.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodHighDensityUranium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodHighDensityUranium.get(4), GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodHighDensityUranium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Uranium.getDust(2),
                Materials.Plutonium.getDust(1),
                Materials.Graphite.getDust(2),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1))
            .outputChances(100_00, 50_00, 100_00, 90_00, 12_50, 7_50)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(32), Materials.TungstenCarbide.getMolten(1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Uranium.getDust(4),
                Materials.Plutonium.getDust(1),
                Materials.Graphite.getDust(4),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1))
            .outputChances(100_00, 100_00, 100_00, 90_00, 25_00, 15_00)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(64), Materials.TungstenCarbide.getMolten(2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityUranium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Uranium.getDust(8),
                Materials.Plutonium.getDust(2),
                Materials.Graphite.getDust(8),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(128), Materials.TungstenCarbide.getMolten(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        // High Density Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1), ItemRefer.High_Density_Plutonium_Nugget.get(1))
            .itemOutputs(ItemList.RodHighDensityPlutonium.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodHighDensityPlutonium.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodHighDensityPlutonium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodHighDensityPlutonium2.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodHighDensityPlutonium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodHighDensityPlutonium.get(4), GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodHighDensityPlutonium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Plutonium.getDust(4),
                Materials.Plutonium241.getDust(2),
                Materials.Carbon.getDust(2),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(Materials.Argon.getGas(32), Materials.HSSS.getMolten(1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Plutonium.getDust(8),
                Materials.Plutonium241.getDust(4),
                Materials.Carbon.getDust(4),
                Materials.Uranium.getDust(2),
                Materials.Uranium235.getDust(2))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(Materials.Argon.getGas(64), Materials.HSSS.getMolten(2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodHighDensityPlutonium4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Plutonium.getDust(16),
                Materials.Plutonium241.getDust(8),
                Materials.Carbon.getDust(8),
                Materials.Uranium.getDust(4),
                Materials.Uranium235.getDust(4))
            .outputChances(100_00, 100_00, 100_00, 90_00, 50_00, 30_00)
            .fluidOutputs(Materials.Argon.getGas(128), Materials.HSSS.getMolten(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        // Excited Uranium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(GGMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(250))
            .itemOutputs(ItemList.RodExcitedUranium.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(RecipeMaps.cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedUranium.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodExcitedUranium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedUranium2.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodExcitedUranium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedUranium.get(4), GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodExcitedUranium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedUranium4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Excited Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(GGMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(250))
            .itemOutputs(ItemList.RodExcitedPlutonium.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(RecipeMaps.cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedPlutonium.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodExcitedPlutonium2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedPlutonium2.get(2), GGMaterial.zircaloy2.get(OrePrefixes.stick, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodExcitedPlutonium4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodExcitedPlutonium.get(4), GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodExcitedPlutonium4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodExcitedPlutonium4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Naquadah
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), Materials.NaquadahEnriched.getDust(3))
            .itemOutputs(ItemList.RodNaquadah.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodNaquadah2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodNaquadah4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah2.get(2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodNaquadah4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadah.get(32L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                WerkstoffLoader.Tiberium.get(dust, 64),
                WerkstoffLoader.Tiberium.get(dust, 64))
            .itemOutputs(ItemList.RodNaquadah32.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(1),
                Materials.Naquadah.getDust(1),
                Materials.Naquadria.getDustSmall(2),
                Materials.NaquadahEnriched.getDustTiny(2),
                Materials.TungstenSteel.getDust(8),
                Materials.Platinum.getDust(1))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah2.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(2),
                Materials.Naquadah.getDust(2),
                Materials.Naquadria.getDust(1),
                Materials.NaquadahEnriched.getDustTiny(4),
                Materials.TungstenSteel.getDust(18),
                Materials.Platinum.getDust(2))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah4.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(4),
                Materials.Naquadah.getDust(4),
                Materials.Naquadria.getDust(2),
                Materials.NaquadahEnriched.getDustTiny(8),
                Materials.TungstenSteel.getDust(38),
                Materials.Platinum.getDust(4))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadah32.get(1))
            .itemOutputs(
                ItemList.DepletedRodNaquadah4.get(8),
                WerkstoffLoader.Zirconium.get(dust, 64),
                WerkstoffLoader.Zirconium.get(dust, 64),
                Materials.TungstenSteel.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.TungstenSteel.getDust(16))
            .outputChances(100_00, 50_00, 50_00, 100_00, 100_00, 100_00)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        // Naquadria
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), Materials.Naquadria.getDust(3))
            .itemOutputs(ItemList.RodNaquadria.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria.get(2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .circuit(2)
            .itemOutputs(ItemList.RodNaquadria2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria.get(4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6))
            .circuit(4)
            .itemOutputs(ItemList.RodNaquadria4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.RodNaquadria2.get(2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .circuit(5)
            .itemOutputs(ItemList.RodNaquadria4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(1),
                Materials.Naquadah.getDust(1),
                Materials.NaquadahEnriched.getDustSmall(2),
                Materials.Naquadria.getDustTiny(2),
                Materials.TungstenSteel.getDust(8),
                Materials.Platinum.getDust(1))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria2.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(2),
                Materials.Naquadah.getDust(2),
                Materials.NaquadahEnriched.getDust(1),
                Materials.Naquadria.getDustTiny(4),
                Materials.TungstenSteel.getDust(18),
                Materials.Platinum.getDust(2))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodNaquadria4.get(1))
            .itemOutputs(
                Materials.Naquadah.getDust(4),
                Materials.Naquadah.getDust(4),
                Materials.NaquadahEnriched.getDust(2),
                Materials.Naquadria.getDustTiny(8),
                Materials.TungstenSteel.getDust(38),
                Materials.Platinum.getDust(4))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        // Tiberium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), WerkstoffLoader.Tiberium.get(dust, 3))
            .itemOutputs(ItemList.RodTiberium.get(1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodTiberium.get(2), GTOreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(ItemList.RodTiberium2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodTiberium.get(4), GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 6L))
            .itemOutputs(ItemList.RodTiberium4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RodTiberium2.get(2), GTOreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(ItemList.RodTiberium4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust),
                WerkstoffLoader.Zirconium.get(dust),
                WerkstoffLoader.Tiberium.get(dustSmall, 2),
                WerkstoffLoader.Zirconium.get(dust, 2),
                Materials.TungstenSteel.getDust(8),
                Materials.Platinum.getDust(1))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium2.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust, 2),
                WerkstoffLoader.Zirconium.get(dust, 2),
                WerkstoffLoader.Tiberium.get(dust),
                WerkstoffLoader.Zirconium.get(dust, 4),
                Materials.TungstenSteel.getDust(18),
                Materials.Platinum.getDust(2))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(2))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.DepletedRodTiberium4.get(1))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(dust, 4),
                WerkstoffLoader.Zirconium.get(dust, 4),
                WerkstoffLoader.Tiberium.get(dust, 2),
                WerkstoffLoader.Zirconium.get(dust, 8),
                Materials.TungstenSteel.getDust(38),
                Materials.Platinum.getDust(4))
            .outputChances(100_00, 50_00, 50_00, 25_00, 100_00, 100_00)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);
    }
}
