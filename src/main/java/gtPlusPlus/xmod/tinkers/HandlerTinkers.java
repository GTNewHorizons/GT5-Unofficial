package gtPlusPlus.xmod.tinkers;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import tconstruct.library.crafting.DryingRackRecipes;
import tconstruct.library.crafting.Smeltery;

public class HandlerTinkers {

    public static void postInit() {
        if (Mods.TinkerConstruct.isModLoaded()) {
            Fluid pyrotheumFluid = TFFluids.fluidPyrotheum;
            if (pyrotheumFluid != null) {
                // Enable Pyrotheum as Fuel for the Smeltery
                // pyrotheum lasts 3.5 seconds per 15 mb
                Smeltery.addSmelteryFuel(pyrotheumFluid, 5000, 70);
            }
            // Generate Drying Rack recipes
            generateAllDryingRecipes();
        }
    }

    private static void generateAllDryingRecipes() {
        for (DryingRackRecipes.DryingRecipe r : DryingRackRecipes.recipes) {
            GTValues.RA.stdBuilder()
                .itemInputs(r.input)
                .circuit(16)
                .itemOutputs(r.result)
                .eut(TierEU.RECIPE_LV)
                .duration(r.time / 10)
                .addTo(chemicalDehydratorRecipes);
        }
    }
}
