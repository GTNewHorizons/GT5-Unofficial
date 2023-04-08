package gtPlusPlus.xmod.railcraft;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;

public class HANDLER_Railcraft {

    public static void preInit() {
        // Register Custom Coal Coke
        ModItems.itemCoalCoke = new BaseItemBurnable(
                "itemCoalCoke",
                "Coking Coal",
                tabMisc,
                64,
                0,
                "Used for metallurgy.",
                "fuelCoke",
                3200,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemCoalCoke");

        // Add in things that once existed in 1.5.2
        ModItems.itemCactusCharcoal = new BaseItemBurnable(
                "itemCactusCharcoal",
                "Cactus Charcoal",
                tabMisc,
                64,
                0,
                "Used for smelting.",
                "fuelCactusCharcoal",
                400,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemCactusCharcoal");
        ModItems.itemSugarCharcoal = new BaseItemBurnable(
                "itemSugarCharcoal",
                "Sugar Charcoal",
                tabMisc,
                64,
                0,
                "Used for smelting.",
                "fuelSugarCharcoal",
                400,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemSugarCharcoal");
        ModItems.itemCactusCoke = new BaseItemBurnable(
                "itemCactusCoke",
                "Cactus Coke",
                tabMisc,
                64,
                0,
                "Used for smelting.",
                "fuelCactusCoke",
                800,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemCactusCoke");
        ModItems.itemSugarCoke = new BaseItemBurnable(
                "itemSugarCoke",
                "Sugar Coke",
                tabMisc,
                64,
                0,
                "Used for smelting.",
                "fuelSugarCoke",
                800,
                0).setTextureName(GTPlusPlus.ID + ":burnables/itemSugarCoke");

        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal), "itemCharcoalCactus");
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemCactusCoke), "itemCokeCactus");
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal), "itemCharcoalSugar");
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemSugarCoke), "itemCokeSugar");
    }

    public static void postInit() {
        generateCokeOvenRecipes();
    }

    private static void generateCokeOvenRecipes() {
        ItemStack[] aInputs1 = new ItemStack[] { ItemUtils.getSimpleStack(Blocks.cactus),
                ItemUtils.getSimpleStack(Items.reeds) };
        ItemStack[] aInputs2 = new ItemStack[] { ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal),
                ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal) };
        ItemStack[] aOutputs = new ItemStack[] { ItemUtils.getSimpleStack(ModItems.itemCactusCoke),
                ItemUtils.getSimpleStack(ModItems.itemSugarCoke) };
        for (int i = 0; i < aOutputs.length; i++) {
            // Recipes for the Charcoals and Cokes, outputting either Creosote or Charcoal Byproducts depending on the
            // fluid input
            CORE.RA.addCokeOvenRecipe(
                    aInputs1[i],
                    CI.getNumberedCircuit(3),
                    null,
                    FluidUtils.getFluidStack("creosote", 100),
                    aInputs2[i],
                    20,
                    16);
            CORE.RA.addCokeOvenRecipe(
                    aInputs1[i],
                    CI.getNumberedCircuit(4),
                    FluidUtils.getFluidStack("nitrogen", 100),
                    FluidUtils.getFluidStack("charcoal_byproducts", 200),
                    aInputs2[i],
                    10,
                    16);
            CORE.RA.addCokeOvenRecipe(
                    aInputs2[i],
                    CI.getNumberedCircuit(3),
                    null,
                    FluidUtils.getFluidStack("creosote", 200),
                    aOutputs[i],
                    40,
                    16);
            CORE.RA.addCokeOvenRecipe(
                    aInputs2[i],
                    CI.getNumberedCircuit(4),
                    FluidUtils.getFluidStack("nitrogen", 50),
                    FluidUtils.getFluidStack("charcoal_byproducts", 100),
                    aOutputs[i],
                    20,
                    16);

            // Generate Wood Tar and Wood Gas from these Cokes
            CORE.RA.addCokeOvenRecipe(
                    aOutputs[i],
                    CI.getNumberedCircuit(5),
                    FluidUtils.getFluidStack("steam", 100),
                    Materials.WoodTar.getFluid(200),
                    Materials.Ash.getDustSmall(1),
                    60,
                    240);
            CORE.RA.addCokeOvenRecipe(
                    aOutputs[i],
                    CI.getNumberedCircuit(6),
                    FluidUtils.getFluidStack("steam", 100),
                    Materials.WoodGas.getFluid(300),
                    Materials.Ash.getDustSmall(1),
                    60,
                    240);

            // Fluid Extracting the Charcoals for Wood Tar
            GT_Values.RA.addFluidExtractionRecipe(
                    aInputs2[i],
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L),
                    Materials.WoodTar.getFluid(50L),
                    1000,
                    30,
                    16);

            // Processing the Charcoals with Oxygen to get CO and CO2
            // C + O = CO
            GT_Values.RA.addChemicalRecipe(
                    aInputs2[i],
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.Oxygen.getGas(500),
                    Materials.CarbonMonoxide.getGas(500),
                    Materials.Ash.getDustTiny(1),
                    80,
                    8);
            // C + 2O = CO2
            GT_Values.RA.addChemicalRecipe(
                    aInputs2[i],
                    GT_Utility.getIntegratedCircuit(2),
                    Materials.Oxygen.getGas(2000),
                    Materials.CarbonDioxide.getGas(1000),
                    Materials.Ash.getDustTiny(1),
                    40,
                    8);
        }
        if (Railcraft.isModLoaded()) {
            for (int i = 0; i < aOutputs.length; i++) {
                RailcraftUtils.addCokeOvenRecipe(
                        aInputs1[i],
                        true,
                        true,
                        aInputs2[i],
                        FluidUtils.getFluidStack("creosote", 30),
                        500);
            }
            for (int i = 0; i < aOutputs.length; i++) {
                RailcraftUtils.addCokeOvenRecipe(
                        aInputs2[i],
                        true,
                        true,
                        aOutputs[i],
                        FluidUtils.getFluidStack("creosote", 30),
                        500);
            }

            if (NewHorizonsCoreMod.isModLoaded()) {
                for (int i = 0; i < aOutputs.length; i++) {
                    RailcraftUtils.addAdvancedCokeOvenRecipe(aInputs1[i], true, true, aInputs2[i], 20);
                }
                for (int i = 0; i < aOutputs.length; i++) {
                    RailcraftUtils.addAdvancedCokeOvenRecipe(aInputs2[i], true, true, aInputs2[i], 20);
                }
            }
        }
    }
}
