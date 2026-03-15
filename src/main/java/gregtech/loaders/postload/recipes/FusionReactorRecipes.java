package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.MaterialsElements;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FusionReactorRecipes implements Runnable {

    @Override
    public void run() {
        // Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        // Fusion with margin 32700 65450 131000
        // Startup max 160M EU 320M EU 640M EU
        // Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        // F FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than
        // available on the tier)

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lithium.getMolten(1 * NUGGETS), Materials.Tungsten.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Iridium.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes); // FT1+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Deuterium.getGas(125), Materials.Tritium.getGas(125))
            .fluidOutputs(Materials.Helium.getPlasma(125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_IV / 2)
            .metadata(FUSION_THRESHOLD, 40_000_000L)
            .addTo(fusionRecipes); // FT1 Cheap - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Deuterium.getGas(125), Materials.Helium3.getGas(125))
            .fluidOutputs(Materials.Helium.getPlasma(125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(FUSION_THRESHOLD, 60_000_000L)
            .addTo(fusionRecipes); // FT1 Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Aluminium.getMolten(1 * NUGGETS), Materials.Lithium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Sulfur.getPlasma(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(10240)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT1+ Cheap

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Beryllium.getMolten(1 * NUGGETS), Materials.Deuterium.getGas(375))
            .fluidOutputs(Materials.Nitrogen.getPlasma(125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 180_000_000L)
            .addTo(fusionRecipes); // FT1+ Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Silicon.getMolten(1 * NUGGETS), Materials.Magnesium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Iron.getPlasma(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 360_000_000L)
            .addTo(fusionRecipes); // FT1++ Cheap //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Potassium.getMolten(1 * NUGGETS), Materials.Fluorine.getGas(1 * INGOTS))
            .fluidOutputs(Materials.Nickel.getPlasma(1 * INGOTS))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes); // FT1++ Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Beryllium.getMolten(1 * NUGGETS), Materials.Tungsten.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Platinum.getMolten(1 * NUGGETS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Neodymium.getMolten(1 * NUGGETS), Materials.Hydrogen.getGas(3 * NUGGETS))
            .fluidOutputs(Materials.Europium.getMolten(1 * NUGGETS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Neodymium.getMolten(2 * NUGGETS), Materials.Promethium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Europium.getMolten(2 * NUGGETS))
            .duration(8 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - alternative europium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lutetium.getMolten(1 * NUGGETS), Materials.Chrome.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Americium.getMolten(1 * NUGGETS))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lutetium.getMolten(2 * NUGGETS), Materials.Promethium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Americium.getMolten(2 * NUGGETS))
            .duration(24 * TICKS)
            .eut(38764)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - alternative americium

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium.getMolten(1 * NUGGETS), Materials.Thorium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Naquadah.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes); // FT1+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getMolten(1 * INGOTS), Materials.Naquadria.getMolten(1 * INGOTS))
            .fluidOutputs(Materials.Neutronium.getMolten(1 * INGOTS))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 600_000_000L)
            .addTo(fusionRecipes); // FT3 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Glowstone.getMolten(1 * NUGGETS), Materials.Helium.getPlasma(4))
            .fluidOutputs(Materials.Sunnarium.getMolten(1 * NUGGETS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 40_000_000L)
            .addTo(fusionRecipes); // Mark 1 Expensive
                                   // //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tungsten.getMolten(1 * NUGGETS), Materials.Helium.getGas(1 * NUGGETS))
            .fluidOutputs(Materials.Osmium.getMolten(1 * NUGGETS))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(24578)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Manganese.getMolten(1 * NUGGETS), Materials.Hydrogen.getGas(1 * NUGGETS))
            .fluidOutputs(Materials.Iron.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 120_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Magnesium.getMolten(8 * NUGGETS), Materials.Oxygen.getGas(8 * NUGGETS))
            .fluidOutputs(Materials.Calcium.getPlasma(1 * NUGGETS))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 120_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Mercury.getFluid(1 * NUGGETS), Materials.Magnesium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Uranium.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gold.getMolten(1 * NUGGETS), Materials.Aluminium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Uranium.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Uranium.getMolten(1 * NUGGETS), Materials.Helium.getGas(1 * NUGGETS))
            .fluidOutputs(Materials.Plutonium.getMolten(1 * NUGGETS))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes); // FT2+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Vanadium.getMolten(1 * NUGGETS), Materials.Hydrogen.getGas(125))
            .fluidOutputs(Materials.Chrome.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 140_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gallium.getMolten(1 * NUGGETS), Materials.Radon.getGas(125))
            .fluidOutputs(Materials.Duranium.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 140_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Titanium.getMolten(3 * NUGGETS), Materials.Duranium.getMolten(2 * NUGGETS))
            .fluidOutputs(Materials.Tritanium.getMolten(1 * NUGGETS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tantalum.getMolten(1 * NUGGETS), Materials.Tritium.getGas(1 * NUGGETS))
            .fluidOutputs(Materials.Tungsten.getMolten(1 * NUGGETS))
            .duration(16 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Silver.getMolten(1 * NUGGETS), Materials.Lithium.getMolten(1 * NUGGETS))
            .fluidOutputs(Materials.Indium.getMolten(1 * NUGGETS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 380_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Copper.getMolten(1 * HALF_INGOTS), Materials.Tritium.getGas(250))
            .fluidOutputs(Materials.Zinc.getPlasma(1 * HALF_INGOTS))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 180_000_000L)
            .addTo(fusionRecipes); // FT2 - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Cobalt.getMolten(1 * INGOTS), Materials.Silicon.getMolten(1 * INGOTS))
            .fluidOutputs(Materials.Niobium.getPlasma(1 * INGOTS))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gold.getMolten(1 * INGOTS), Materials.Arsenic.getMolten(1 * INGOTS))
            .fluidOutputs(Materials.Silver.getPlasma(1 * INGOTS))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 350_000_000L)
            .addTo(fusionRecipes); // FT2+

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Silver.getMolten(1 * INGOTS), Materials.Helium3.getGas(375))
            .fluidOutputs(Materials.Tin.getPlasma(2 * INGOTS))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 280_000_000L)
            .addTo(fusionRecipes); // FT2

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tantalum.getMolten(1 * INGOTS), Materials.Zinc.getPlasma(72))
            .fluidOutputs(Materials.Bismuth.getPlasma(1 * INGOTS))
            .duration(16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 350_000_000L)
            .addTo(fusionRecipes); // FT3 - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iridium.getMolten(1 * INGOTS), Materials.Fluorine.getGas(500))
            .fluidOutputs(Materials.Radon.getPlasma(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 450_000_000L)
            .addTo(fusionRecipes); // FT3 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium241.getMolten(1 * INGOTS), Materials.Hydrogen.getGas(2_000))
            .fluidOutputs(Materials.Americium.getPlasma(1 * INGOTS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes); // FT3

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Cobalt.getMolten(1 * INGOTS),
                MaterialsElements.getInstance().NEON.getFluidStack(1 * INGOTS))
            .fluidOutputs(Materials.Rubidium.getMolten(1 * INGOTS))
            .duration(1 * SECONDS + 4 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes); // FT3

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lutetium.getMolten(4 * INGOTS), Materials.Vanadium.getMolten(4 * INGOTS))
            .fluidOutputs(Materials.Plutonium241.getPlasma(4 * INGOTS))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5
                                   // because
                                   // of UEV
                                   // voltage

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tellurium.getMolten(4 * INGOTS), Materials.Zinc.getMolten(4 * INGOTS))
            .fluidOutputs(Materials.Lead.getPlasma(4 * INGOTS))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Osmium.getMolten(4 * INGOTS), Materials.Silicon.getMolten(4 * INGOTS))
            .fluidOutputs(Materials.Thorium.getPlasma(4 * INGOTS))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage
    }
}
