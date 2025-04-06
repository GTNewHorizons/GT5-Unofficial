package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.recipe.RecipeMaps.steamManufacturerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.stream.IntStream;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class FullPackSteamRecipes implements Runnable {

    // spotless:off
    @Override
    public void run() {

        // ======================================
        // GT/IC2
        // ======================================

        // Muffler Upgrade
        GTModHandler.addCraftingRecipe(ItemList.Upgrade_Muffler.get(1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
            " W ",
            "WIW",
            " W ",
            'W', Materials.Wood.getPlates(1),
            'I', Materials.Iron.getPlates(1),
        });

        // Tool Box
        GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemToolbox", 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
            " B ",
            "BCB",
            " B ",
            'B', GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Bronze, 1),
            'C', new ItemStack(Blocks.chest, 1, GTValues.W),
        });

        // Large Steel Cell
        GTModHandler.addCraftingRecipe(ItemList.Large_Fluid_Cell_Steel.get(1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
            "RPR",
            "P P",
            "RPR",
            'R', GTOreDictUnificator.get(OrePrefixes.ring, Materials.Bronze, 1),
            'P', GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Steel, 1),
        });

        // ======================================
        // Draconic Evolution
        // ======================================

        if (Mods.DraconicEvolution.isModLoaded()) {

            // Item Dislocator
            GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.DraconicEvolution.ID, "magnet", 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                "LLL",
                "  S",
                "LLL",
                'L', GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.IronMagnetic, 1),
                'S', GTOreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1),
            });
        }

        // ======================================
        // Extra Utilities
        // ======================================

        if (Mods.ExtraUtilities.isModLoaded()) {

            // Builder's Wand
            GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.ExtraUtilities.ID, "builderswand", 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                "  S",
                " S ",
                "B  ",
                'S', Materials.Stronze.getIngots(1),
                'B', GTOreDictUnificator.get(OrePrefixes.stick, Materials.Breel, 1),
            });

            // Super Builder's Wand
            GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.ExtraUtilities.ID, "creativebuilderswand", 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                " SS",
                " SS",
                "B  ",
                'S', Materials.Stronze.getIngots(1),
                'B', GTOreDictUnificator.get(OrePrefixes.stick, Materials.Breel, 1),
            });

            // Magnum Torch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.torch, 64),
                    new ItemStack(Items.blaze_rod, 4))
                .itemOutputs(GTModHandler.getModItem(Mods.ExtraUtilities.ID, "magnumTorch", 1))
                .duration(5 * SECONDS).eut(16).addTo(steamCarpenterRecipes);

            // Angel Ring
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.Gold, 4),
                    new ItemStack(Items.feather, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.CompressedSteam, 4))
                .itemOutputs(GTModHandler.getModItem(Mods.ExtraUtilities.ID, "angelRing", 1))
                .duration(10 * SECONDS).eut(16).addTo(steamManufacturerRecipes);

            // Trash Can (Fluids)
            if (Mods.BuildCraftFactory.isModLoaded()) {
                GTModHandler.addShapelessCraftingRecipe(GTModHandler.getModItem(Mods.ExtraUtilities.ID, "trashcan", 1, 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    GTModHandler.getModItem(Mods.ExtraUtilities.ID, "trashcan", 1),
                    GTModHandler.getModItem(Mods.BuildCraftFactory.ID, "tankBlock", 1),
                });
            }
        }

        // ======================================
        // Backpack Mod
        // ======================================

        if (Mods.Backpack.isModLoaded()) {

            // Allows for Backpacks to keep inventory on upgrade
            for (Integer offset : IntStream.rangeClosed(0, 16).toArray()) {

                // Middle Backpack
                GameRegistry.addRecipe(
                    new UpgradeBackpackRecipe(
                        GTModHandler.getModItem(Mods.Backpack.ID, "backpack", 1, 100 + offset),
                        "aba",
                        "bcb",
                        "dbd",
                        'a',
                        "ringStronze",
                        'd',
                        "ringBreel",
                        'b',
                        GTModHandler.getModItem(Mods.Backpack.ID, "tannedLeather", 1),
                        'c',
                        GTModHandler.getModItem(Mods.Backpack.ID, "backpack", 1, offset)));

                // Big Backpack
                if (Mods.PamsHarvestCraft.isModLoaded()) {
                    GameRegistry.addRecipe(
                        new UpgradeBackpackRecipe(
                            GTModHandler.getModItem(Mods.Backpack.ID, "backpack", 1, 200 + offset),
                            "aba",
                            "bcb",
                            "aba",
                            'a',
                            "ringCompressedSteam",
                            'b',
                            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "hardenedleatherItem", 1),
                            'c',
                            GTModHandler.getModItem(Mods.Backpack.ID, "backpack", 1, 100 + offset)));
                }
            }

            if (Mods.PamsHarvestCraft.isModLoaded()) {

                // Hardened Leather
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTModHandler.getModItem(Mods.Backpack.ID, "tannedLeather", 2),
                        GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "waxItem", 1))
                    .itemOutputs(GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "hardenedleatherItem", 1))
                    .duration(15 * SECONDS).eut(4).addTo(compressorRecipes);
            }
        }

        // ======================================
        // RandomThings
        // ======================================

        if (Mods.RandomThings.isModLoaded()) {

            // Voiding Drop Filter
            GTModHandler.addShapelessCraftingRecipe(GTModHandler.getModItem(Mods.RandomThings.ID, "dropFilter", 1, 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                GTModHandler.getModItem(Mods.RandomThings.ID, "voidStone", 1),
                GTModHandler.getModItem(Mods.RandomThings.ID, "dropFilter", 1, 0),
            });

            // Item Filter
            GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.RandomThings.ID, "filter", 1, 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                " C ",
                "CPC",
                " C ",
                'C', GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Iron, 1),
                'P', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1),
            });
        }

        // ======================================
        // BuildCraft: Factory
        // ======================================

        if (Mods.BuildCraftFactory.isModLoaded()) {

            // Tank
            if (Mods.Railcraft.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTModHandler.getModItem(Mods.Railcraft.ID, "glass", 1),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2))
                    .itemOutputs(GTModHandler.getModItem(Mods.BuildCraftFactory.ID, "tankBlock", 1))
                    .duration(5 * SECONDS).eut(4).addTo(steamManufacturerRecipes);
            }

            if (Mods.ExtraUtilities.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTModHandler.getModItem(Mods.ExtraUtilities.ID, "decorativeBlock2", 1, 5),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2))
                    .itemOutputs(GTModHandler.getModItem(Mods.BuildCraftFactory.ID, "tankBlock", 1))
                    .duration(5 * SECONDS).eut(4).addTo(steamManufacturerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(Blocks.glass, 1),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 4L))
                    .itemOutputs(getModItem(ExtraUtilities.ID, "decorativeBlock2", 1, 5)).duration(10 * SECONDS)
                    .eut(8).addTo(alloySmelterRecipes);
            }
        }

        // ======================================
        // Tinkers' Construct
        // ======================================

        if (Mods.TinkerConstruct.isModLoaded()) {

            // Traveller's Gloves
            if (Mods.PamsHarvestCraft.isModLoaded()) {
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.TinkerConstruct.ID, "travelGlove", 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[]{
                    " LP",
                    "LLL",
                    " L ",
                    'L', GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "hardenedleatherItem", 1),
                    'P', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                });
            }
        }

        // ======================================
        // Binnie Core
        // ======================================

        if (Mods.BinnieCore.isModLoaded()) {

            // Compartment
            GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 0), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                "SCS",
                "CFC",
                "SCS",
                'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Wood, 1),
                'C', new ItemStack(Blocks.chest, 1),
                'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
            });

            if (Mods.IronChests.isModLoaded()) {

                // Copper Compartment
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 1), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    "SCS",
                    "CFC",
                    "SCS",
                    'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Copper, 1),
                    'C', GTModHandler.getModItem(Mods.IronChests.ID, "BlockIronChest", 1, 3),
                    'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Copper, 1),
                });

                // Bronze Compartment
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 2), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    "SCS",
                    "CFC",
                    "SCS",
                    'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 1),
                    'C', GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 1),
                    'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1),
                });

                // Iron Compartment
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 3), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    "SCS",
                    "CFC",
                    "SCS",
                    'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 1),
                    'C', GTModHandler.getModItem(Mods.IronChests.ID, "BlockIronChest", 1, 0),
                    'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1),
                });

                // Gold Compartment
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 4), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    "SCS",
                    "CFC",
                    "SCS",
                    'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Gold, 1),
                    'C', GTModHandler.getModItem(Mods.IronChests.ID, "BlockIronChest", 1, 1),
                    'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Gold, 1),
                });

                // Diamond Compartment
                GTModHandler.addCraftingRecipe(GTModHandler.getModItem(Mods.BinnieCore.ID, "storage", 1, 5), GTModHandler.RecipeBits.FUCK_OFF_COREMOD, new Object[] {
                    "SCS",
                    "CFC",
                    "SCS",
                    'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Diamond, 1),
                    'C', GTModHandler.getModItem(Mods.IronChests.ID, "BlockIronChest", 1, 2),
                    'F', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Diamond, 1),
                });
            }
        }
    }
    // spotless:on

    // Allows for Backpacks to keep inventory on upgrade
    public static class UpgradeBackpackRecipe extends ShapedOreRecipe {

        public UpgradeBackpackRecipe(ItemStack result, Object... recipe) {
            super(result, recipe);
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting crafting) {
            ItemStack result = super.getCraftingResult(crafting);
            Item resultItem = result.getItem();
            for (int i = 0, imax = crafting.getSizeInventory(); i < imax; i++) {
                ItemStack stack = crafting.getStackInSlot(i);
                if (stack != null && stack.getItem() == resultItem) {
                    result.stackTagCompound = stack.stackTagCompound;
                    break;
                }
            }
            return result;
        }
    }
}
