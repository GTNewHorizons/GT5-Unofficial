package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeRegistrator.registerWiremillRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class WiremillRecipes implements Runnable {

    @Override
    public void run() {

        registerWiremillRecipes(Materials.Graphene, 20 * SECONDS, 2, OrePrefixes.dust, OrePrefixes.stick, 1);

        registerWiremillRecipes(Materials.SpaceTime, 8 * SECONDS + 8 * TICKS, (int) TierEU.RECIPE_LuV);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L))
            .itemOutputs(new ItemStack(Items.string, 32))
            .duration(4 * SECONDS)
            .eut(48)
            .addTo(wiremillRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1))
            .itemOutputs(GTModHandler.getIC2Item("miningPipe", 1))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(wiremillRecipes);
    }
}
