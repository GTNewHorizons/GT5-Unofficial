package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lithium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes); // FT1+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, 125))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_IV / 2)
            .metadata(FUSION_THRESHOLD, 40_000_000L)
            .addTo(fusionRecipes); // FT1 Cheap - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 125),
                MaterialLibAPI.getFluidStack(Materials.Helium3, FluidShapes.fluidGas, 125))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(FUSION_THRESHOLD, 60_000_000L)
            .addTo(fusionRecipes); // FT1 Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Lithium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(10240)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT1+ Cheap

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Beryllium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 375))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 125))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 180_000_000L)
            .addTo(fusionRecipes); // FT1+ Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Silicon, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Magnesium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 360_000_000L)
            .addTo(fusionRecipes); // FT1++ Cheap //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes); // FT1++ Expensive //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Beryllium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Platinum, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Neodymium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (3 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Europium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Neodymium, FluidShapes.fluidMolten, (int) (2 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Promethium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Europium, FluidShapes.fluidMolten, (int) (2 * NUGGETS)))
            .duration(8 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - alternative europium

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Chrome, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidMolten, (int) (2 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Promethium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidMolten, (int) (2 * NUGGETS)))
            .duration(24 * TICKS)
            .eut(38764)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - alternative americium

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes); // FT1+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 600_000_000L)
            .addTo(fusionRecipes); // FT3 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Glowstone, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Sunnarium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 40_000_000L)
            .addTo(fusionRecipes); // Mark 1 Expensive
                                   // //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(24578)
            .metadata(FUSION_THRESHOLD, 150_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Manganese, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 120_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Magnesium, FluidShapes.fluidMolten, (int) (8 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (8 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, (int) (1 * NUGGETS)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 120_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Magnesium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Gold, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 240_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes); // FT2+ - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Vanadium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 125))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chrome, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 140_000_000L)
            .addTo(fusionRecipes); // FT1 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Gallium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 125))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Duranium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 140_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (3 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Duranium, FluidShapes.fluidMolten, (int) (2 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Tritanium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Tantalum, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, (int) (1 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(16 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidMolten, (int) (1 * NUGGETS)),
                MaterialLibAPI.getFluidStack(Materials.Lithium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Indium, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(24576)
            .metadata(FUSION_THRESHOLD, 380_000_000L)
            .addTo(fusionRecipes); //

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Copper, FluidShapes.fluidMolten, (int) (1 * HALF_INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, 250))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, (int) (1 * HALF_INGOTS)))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 180_000_000L)
            .addTo(fusionRecipes); // FT2 - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Cobalt, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Silicon, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes); // FT2 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Gold, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Arsenic, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 350_000_000L)
            .addTo(fusionRecipes); // FT2+

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Helium3, FluidShapes.fluidGas, 375))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidPlasma, (int) (2 * INGOTS)))
            .duration(16 * TICKS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 280_000_000L)
            .addTo(fusionRecipes); // FT2

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Tantalum, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, 72))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Bismuth, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 350_000_000L)
            .addTo(fusionRecipes); // FT3 - farmable

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 450_000_000L)
            .addTo(fusionRecipes); // FT3 - utility

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes); // FT3

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Cobalt, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialUtils.anyFluid(Materials.Neon, 1 * INGOTS))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Rubidium, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 4 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes); // FT3

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidMolten, (int) (4 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Vanadium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidPlasma, (int) (4 * INGOTS)))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Tellurium, FluidShapes.fluidMolten, (int) (4 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, (int) (4 * INGOTS)))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, (int) (4 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Silicon, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, (int) (4 * INGOTS)))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Redstone, FluidShapes.fluidPlasma, (int) (4 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Ichorium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.UnformedHexanite, FluidShapes.fluidLiquid, (int) (4 * INGOTS)))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_UEV / 2)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes); // FT5 because of UEV voltage
    }
}
