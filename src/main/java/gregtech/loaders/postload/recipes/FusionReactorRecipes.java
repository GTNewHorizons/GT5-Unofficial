package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFusionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class FusionReactorRecipes implements Runnable {

    @Override
    public void run() {
        // Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        // Fusion with margin 32700 65450 131000
        // Startup max 160M EU 320M EU 640M EU
        // Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        // F FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than
        // available on the tier)

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Lithium.getMolten(16), Materials.Tungsten.getMolten(16))
                    .fluidOutputs(Materials.Iridium.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .metadata(FUSION_THRESHOLD, 300000000)
                    .addTo(sFusionRecipes); // FT1+ - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Deuterium.getGas(125), Materials.Tritium.getGas(125))
                    .fluidOutputs(Materials.Helium.getPlasma(125))
                    .duration(16 * TICKS)
                    .eut(4096)
                    .metadata(FUSION_THRESHOLD, 40000000)
                    .addTo(sFusionRecipes); // FT1 Cheap - farmable

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Deuterium.getGas(125), Materials.Helium_3.getGas(125))
                    .fluidOutputs(Materials.Helium.getPlasma(125))
                    .duration(16 * TICKS)
                    .eut(2048)
                    .metadata(FUSION_THRESHOLD, 60000000)
                    .addTo(sFusionRecipes); // FT1 Expensive //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Carbon.getMolten(125), Materials.Helium_3.getGas(125))
                    .fluidOutputs(Materials.Oxygen.getPlasma(125))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(4096)
                    .metadata(FUSION_THRESHOLD, 80000000)
                    .addTo(sFusionRecipes); // FT1 Expensive //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Aluminium.getMolten(16), Materials.Lithium.getMolten(16))
                    .fluidOutputs(Materials.Sulfur.getPlasma(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(10240)
                    .metadata(FUSION_THRESHOLD, 240000000)
                    .addTo(sFusionRecipes); // FT1+ Cheap

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Beryllium.getMolten(16), Materials.Deuterium.getGas(375))
                    .fluidOutputs(Materials.Nitrogen.getPlasma(125))
                    .duration(16 * TICKS)
                    .eut(16384)
                    .metadata(FUSION_THRESHOLD, 180000000)
                    .addTo(sFusionRecipes); // FT1+ Expensive //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Silicon.getMolten(16), Materials.Magnesium.getMolten(16))
                    .fluidOutputs(Materials.Iron.getPlasma(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(8192)
                    .metadata(FUSION_THRESHOLD, 360000000)
                    .addTo(sFusionRecipes); // FT1++ Cheap //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Potassium.getMolten(16), Materials.Fluorine.getGas(144))
                    .fluidOutputs(Materials.Nickel.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .metadata(FUSION_THRESHOLD, 480000000)
                    .addTo(sFusionRecipes); // FT1++ Expensive //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Beryllium.getMolten(16), Materials.Tungsten.getMolten(16))
                    .fluidOutputs(Materials.Platinum.getMolten(16))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .metadata(FUSION_THRESHOLD, 150000000)
                    .addTo(sFusionRecipes); // FT1 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Neodymium.getMolten(16), Materials.Hydrogen.getGas(48))
                    .fluidOutputs(Materials.Europium.getMolten(16))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(24576)
                    .metadata(FUSION_THRESHOLD, 150000000)
                    .addTo(sFusionRecipes); // FT1 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Lutetium.getMolten(16), Materials.Chrome.getMolten(16))
                    .fluidOutputs(Materials.Americium.getMolten(16))
                    .duration(4 * SECONDS + 16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 200000000)
                    .addTo(sFusionRecipes); // FT2 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Plutonium.getMolten(16), Materials.Thorium.getMolten(16))
                    .fluidOutputs(Materials.Naquadah.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .metadata(FUSION_THRESHOLD, 300000000)
                    .addTo(sFusionRecipes); // FT1+ - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Americium.getMolten(144), Materials.Naquadria.getMolten(144))
                    .fluidOutputs(Materials.Neutronium.getMolten(144))
                    .duration(12 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .metadata(FUSION_THRESHOLD, 640000000)
                    .addTo(sFusionRecipes); // FT3 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Glowstone.getMolten(16), Materials.Helium.getPlasma(4))
                    .fluidOutputs(Materials.Sunnarium.getMolten(16))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(TierEU.RECIPE_IV)
                    .metadata(FUSION_THRESHOLD, 40000000)
                    .addTo(sFusionRecipes); // Mark 1 Expensive
                                            // //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Tungsten.getMolten(16), Materials.Helium.getGas(16))
                    .fluidOutputs(Materials.Osmium.getMolten(16))
                    .duration(12 * SECONDS + 16 * TICKS)
                    .eut(24578)
                    .metadata(FUSION_THRESHOLD, 150000000)
                    .addTo(sFusionRecipes); // FT1 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Manganese.getMolten(16), Materials.Hydrogen.getGas(16))
                    .fluidOutputs(Materials.Iron.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(8192)
                    .metadata(FUSION_THRESHOLD, 120000000)
                    .addTo(sFusionRecipes); // FT1 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Magnesium.getMolten(128), Materials.Oxygen.getGas(128))
                    .fluidOutputs(Materials.Calcium.getPlasma(16))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(8192)
                    .metadata(FUSION_THRESHOLD, 120000000)
                    .addTo(sFusionRecipes); //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Mercury.getFluid(16), Materials.Magnesium.getMolten(16))
                    .fluidOutputs(Materials.Uranium.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 240000000)
                    .addTo(sFusionRecipes); // FT2 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Gold.getMolten(16), Materials.Aluminium.getMolten(16))
                    .fluidOutputs(Materials.Uranium.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 240000000)
                    .addTo(sFusionRecipes); // FT2 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Uranium.getMolten(16), Materials.Helium.getGas(16))
                    .fluidOutputs(Materials.Plutonium.getMolten(16))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 480000000)
                    .addTo(sFusionRecipes); // FT2+ - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Vanadium.getMolten(16), Materials.Hydrogen.getGas(125))
                    .fluidOutputs(Materials.Chrome.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(24576)
                    .metadata(FUSION_THRESHOLD, 140000000)
                    .addTo(sFusionRecipes); // FT1 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Gallium.getMolten(16), Materials.Radon.getGas(125))
                    .fluidOutputs(Materials.Duranium.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(16384)
                    .metadata(FUSION_THRESHOLD, 140000000)
                    .addTo(sFusionRecipes);

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Titanium.getMolten(48), Materials.Duranium.getMolten(32))
                    .fluidOutputs(Materials.Tritanium.getMolten(16))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .metadata(FUSION_THRESHOLD, 200000000)
                    .addTo(sFusionRecipes);

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Tantalum.getMolten(16), Materials.Tritium.getGas(16))
                    .fluidOutputs(Materials.Tungsten.getMolten(16))
                    .duration(16 * TICKS)
                    .eut(24576)
                    .metadata(FUSION_THRESHOLD, 200000000)
                    .addTo(sFusionRecipes); //

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Silver.getMolten(16), Materials.Lithium.getMolten(16))
                    .fluidOutputs(Materials.Indium.getMolten(16))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(24576)
                    .metadata(FUSION_THRESHOLD, 380000000)
                    .addTo(sFusionRecipes); //

        // NEW RECIPES FOR FUSION
        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Magnesium.getMolten(144), Materials.Carbon.getMolten(144))
                    .fluidOutputs(Materials.Argon.getPlasma(125))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(24576)
                    .metadata(FUSION_THRESHOLD, 180000000)
                    .addTo(sFusionRecipes); // FT1+ - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Copper.getMolten(72), Materials.Tritium.getGas(250))
                    .fluidOutputs(Materials.Zinc.getPlasma(72))
                    .duration(16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 180000000)
                    .addTo(sFusionRecipes); // FT2 - farmable

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Cobalt.getMolten(144), Materials.Silicon.getMolten(144))
                    .fluidOutputs(Materials.Niobium.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 200000000)
                    .addTo(sFusionRecipes); // FT2 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Gold.getMolten(144), Materials.Arsenic.getMolten(144))
                    .fluidOutputs(Materials.Silver.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 350000000)
                    .addTo(sFusionRecipes); // FT2+

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Silver.getMolten(144), Materials.Helium_3.getGas(375))
                    .fluidOutputs(Materials.Tin.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 280000000)
                    .addTo(sFusionRecipes); // FT2

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Tungsten.getMolten(144), Materials.Carbon.getMolten(144))
                    .fluidOutputs(Materials.Mercury.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 300000000)
                    .addTo(sFusionRecipes); // FT2

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Tantalum.getMolten(144), Materials.Zinc.getPlasma(72))
                    .fluidOutputs(Materials.Bismuth.getPlasma(144))
                    .duration(16 * TICKS)
                    .eut(98304)
                    .metadata(FUSION_THRESHOLD, 350000000)
                    .addTo(sFusionRecipes); // FT3 - farmable

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Caesium.getMolten(144), Materials.Carbon.getMolten(144))
                    .fluidOutputs(Materials.Promethium.getMolten(144))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(49152)
                    .metadata(FUSION_THRESHOLD, 400000000)
                    .addTo(sFusionRecipes); // FT3

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Iridium.getMolten(144), Materials.Fluorine.getGas(500))
                    .fluidOutputs(Materials.Radon.getPlasma(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(98304)
                    .metadata(FUSION_THRESHOLD, 450000000)
                    .addTo(sFusionRecipes); // FT3 - utility

        GT_Values.RA.stdBuilder()
                    .noItemInputs()
                    .noItemOutputs()
                    .fluidInputs(Materials.Plutonium241.getMolten(144), Materials.Hydrogen.getGas(2000))
                    .fluidOutputs(Materials.Americium.getPlasma(144))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(98304)
                    .metadata(FUSION_THRESHOLD, 500000000)
                    .addTo(sFusionRecipes); // FT3
    }
}
