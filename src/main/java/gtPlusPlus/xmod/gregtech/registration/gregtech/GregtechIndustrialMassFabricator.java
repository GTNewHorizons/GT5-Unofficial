package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_MassFabricator;

public class GregtechIndustrialMassFabricator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_MatterFabricator) {
            generateRecipes();
            run1();
        }
    }

    private static void run1() {
        // Industrial Matter Fabricator Multiblock
        GregtechItemList.Industrial_MassFab.set(
                new GregtechMetaTileEntity_MassFabricator(
                        799,
                        "industrialmassfab.controller.tier.single",
                        "Matter Fabrication CPU").getStackForm(1L));
    }

    private static void generateRecipes() {

        // Generate Scrap->UUA Recipes

        // Basic UUA1
        GT_Recipe UUA_From_Scrap = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(9), ItemUtils.getSimpleStack(getScrapPile(), 9) },
                new ItemStack[] { GT_Values.NI },
                null,
                null,
                new FluidStack[] { GT_Values.NF },
                new FluidStack[] { Materials.UUAmplifier.getFluid(1) },
                9 * 20,
                32,
                0);
        // Basic UUA2
        GT_Recipe UUA_From_ScrapBoxes = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(19), ItemUtils.getSimpleStack(getScrapBox(), 1) },
                new ItemStack[] { GT_Values.NI },
                null,
                null,
                new FluidStack[] { GT_Values.NF },
                new FluidStack[] { Materials.UUAmplifier.getFluid(1) },
                9 * 20,
                32,
                0);

        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(UUA_From_Scrap);
        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(UUA_From_ScrapBoxes);

        // Basic UUM
        GT_Recipe generateUUM_LV = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(1) },
                new ItemStack[] { GT_Values.NI },
                null,
                null,
                new FluidStack[] { GT_Values.NF },
                new FluidStack[] { Materials.UUMatter.getFluid(16) },
                160 * 20,
                4096,
                0);

        // Basic UUM
        GT_Recipe generateUUMFromUUA_LV = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(2) },
                new ItemStack[] { GT_Values.NI },
                null,
                null,
                new FluidStack[] { Materials.UUAmplifier.getFluid(16) },
                new FluidStack[] { Materials.UUMatter.getFluid(16) },
                40 * 20,
                4096,
                0);

        // Advanced UUM
        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(
                new GT_Recipe(
                        false,
                        new ItemStack[] { CI.getNumberedCircuit(3) },
                        new ItemStack[] { GT_Values.NI },
                        null,
                        null,
                        new FluidStack[] { GT_Values.NF },
                        new FluidStack[] { Materials.UUMatter.getFluid(256) },
                        160 * 20,
                        65536,
                        0));

        // Advanced UUM
        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(
                new GT_Recipe(
                        false,
                        new ItemStack[] { CI.getNumberedCircuit(4) },
                        new ItemStack[] { GT_Values.NI },
                        null,
                        null,
                        new FluidStack[] { Materials.UUAmplifier.getFluid(256) },
                        new FluidStack[] { Materials.UUMatter.getFluid(256) },
                        40 * 20,
                        65536,
                        0));

        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(generateUUM_LV);
        GTPPRecipeMaps.multiblockMassFabricatorRecipes.add(generateUUMFromUUA_LV);

        Logger.INFO(
                "Generated " + GTPPRecipeMaps.multiblockMassFabricatorRecipes.getAllRecipes().size()
                        + " Matter Fabricator recipes.");
    }

    public static ItemStack getScrapPile() {
        return ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrap"));
    }

    public static ItemStack getScrapBox() {
        return ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrapbox"));
    }
}
