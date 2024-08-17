package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeRegistrator.registerWiremillRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class WiremillRecipes implements Runnable {

    @Override
    public void run() {

        registerWiremillRecipes(Materials.Graphene, 20 * SECONDS, 2, OrePrefixes.dust, OrePrefixes.stick, 1);

        registerWiremillRecipes(MaterialsUEVplus.SpaceTime, 20 * SECONDS, (int) TierEU.RECIPE_LuV);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L))
            .itemOutputs(new ItemStack(Items.string, 32))
            .duration(4 * SECONDS)
            .eut(48)
            .addTo(wiremillRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1))
            .itemOutputs(GT_ModHandler.getIC2Item("miningPipe", 1))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(wiremillRecipes);
    }
}
