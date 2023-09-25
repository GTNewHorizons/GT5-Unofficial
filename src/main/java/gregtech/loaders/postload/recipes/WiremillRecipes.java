package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMap.sWiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeRegistrator.registerWiremillRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
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
            .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1))
            .itemOutputs(GT_ModHandler.getIC2Item("miningPipe", 1))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sWiremillRecipes);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("copperCableItem", 3L))
                .duration(5 * SECONDS)
                .eut(2)
                .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("copperCableItem", 3L))
                .duration(5 * SECONDS)
                .eut(2)
                .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("tinCableItem", 4L))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(1)
                .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("ironCableItem", 6L))
                .duration(10 * SECONDS)
                .eut(2)
                .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("ironCableItem", 6L))
                .duration(10 * SECONDS)
                .eut(2)
                .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L))
                .itemOutputs(GT_ModHandler.getIC2Item("goldCableItem", 6L))
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(sWiremillRecipes);
        }
    }
}
