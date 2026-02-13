package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_MassFab;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.multiblockMassFabricatorRecipes;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEMassFabricator;

public class GregtechIndustrialMassFabricator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
        generateRecipes();
        run1();
    }

    private static void run1() {
        // Industrial Matter Fabricator Multiblock
        GregtechItemList.Industrial_MassFab.set(
            new MTEMassFabricator(
                Industrial_MassFab.ID,
                "industrialmassfab.controller.tier.single",
                "Matter Fabrication CPU").getStackForm(1L));
    }

    private static void generateRecipes() {

        // Generate Scrap->UUA Recipes

        // Basic UUA1
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9L))
            .circuit(9)
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUA2
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1L))
            .circuit(19)
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUM
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidOutputs(Materials.UUMatter.getFluid(1 * NUGGETS))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUM
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.UUAmplifier.getFluid(1 * NUGGETS))
            .fluidOutputs(Materials.UUMatter.getFluid(1 * NUGGETS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(multiblockMassFabricatorRecipes);

        // Advanced UUM
        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidOutputs(Materials.UUMatter.getFluid(256))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(65536)
            .addTo(multiblockMassFabricatorRecipes);

        // Advanced UUM
        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(Materials.UUAmplifier.getFluid(256))
            .fluidOutputs(Materials.UUMatter.getFluid(256))
            .duration(40 * SECONDS)
            .eut(65536)
            .addTo(multiblockMassFabricatorRecipes);

        Logger.INFO(
            "Generated " + multiblockMassFabricatorRecipes.getAllRecipes()
                .size() + " Matter Fabricator recipes.");
    }
}
