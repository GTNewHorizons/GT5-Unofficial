package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_MassFab;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.multiblockMassFabricatorRecipes;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEMassFabricator;

public class GregtechIndustrialMassFabricator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_MatterFabricator) {
            generateRecipes();
            run1();
        }
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
            .itemInputs(
                GTUtility.getIntegratedCircuit(9),
                ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrap"), 9))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUA2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrapbox")))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUM
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.UUMatter.getFluid(16))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(4096)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        // Basic UUM
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.UUAmplifier.getFluid(16))
            .fluidOutputs(Materials.UUMatter.getFluid(16))
            .duration(40 * SECONDS)
            .eut(4096)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        // Advanced UUM
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .fluidOutputs(Materials.UUMatter.getFluid(256))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(65536)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        // Advanced UUM
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(Materials.UUAmplifier.getFluid(256))
            .fluidOutputs(Materials.UUMatter.getFluid(256))
            .duration(40 * SECONDS)
            .eut(65536)
            .noOptimize()
            .addTo(multiblockMassFabricatorRecipes);

        Logger.INFO(
            "Generated " + multiblockMassFabricatorRecipes.getAllRecipes()
                .size() + " Matter Fabricator recipes.");
    }
}
