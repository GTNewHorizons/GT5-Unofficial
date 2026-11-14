package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MaterialMisc.BRINE;
import static gtPlusPlus.core.material.MaterialMisc.HYDROGEN_CHLORIDE_MIX;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_MID;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipesRareEarthProcessing {

    public static void init() {

        // Brine Check and assignment
        FluidStack mBrine = FluidRegistry.getFluidStack("brine", 1_000);
        if (mBrine == null) {
            Fluid f = BRINE.generateFluid();
            BRINE.registerComponentForMaterial(new FluidStack(f, 1_000));
            mBrine = BRINE.getFluidStack(1000);
        } else {
            BRINE.registerComponentForMaterial(new FluidStack(mBrine, 1_000));
        }

        // Add Process for creating Brine
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 16L))
            .fluidInputs(Materials.SaltWater.getFluid(2_000))
            .fluidOutputs(new FluidStack(mBrine, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(brewingRecipes);

        // Chloralkali process
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(2L))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1L))
            .fluidInputs(new FluidStack(mBrine, 2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // Generate Special Laser Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L))
            .circuit(2)
            .itemOutputs(HYDROGEN_CHLORIDE_MIX.getCell(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Laser_Lens_WoodsGlass.get(0))
            .fluidInputs(HYDROGEN_CHLORIDE_MIX.getFluidStack(4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 4_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(laserEngraverRecipes);

        // LV Rare Earth
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
            .itemOutputs(RARE_EARTH_LOW.getCrushed(2), RARE_EARTH_LOW.getCrushed(2), RARE_EARTH_LOW.getCrushed(2))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        // HV Rare Earth
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
            .itemOutputs(RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2))
            .outputChances(10000, 9000, 8000)
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
            .itemOutputs(RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2))
            .outputChances(9000, 8000, 7000)
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

        // IV Rare Earth
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
            .itemOutputs(RARE_EARTH_HIGH.getCrushed(2), RARE_EARTH_HIGH.getCrushed(2), RARE_EARTH_HIGH.getCrushed(2))
            .outputChances(10000, 9000, 8000)
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
            .itemOutputs(RARE_EARTH_HIGH.getCrushed(2), RARE_EARTH_HIGH.getCrushed(2), RARE_EARTH_HIGH.getCrushed(2))
            .outputChances(9000, 8000, 7000)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        if (Forestry.isModLoaded()) {
            // Refined Rare Earth Comb Processing
            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.REFINEDRAREEARTH, 1))
                .itemOutputs(RARE_EARTH_LOW.getDust(1), RARE_EARTH_MID.getDust(1), RARE_EARTH_HIGH.getDust(1))
                .outputChances(3300, 3300, 3300)
                .duration(32 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }
    }
}
