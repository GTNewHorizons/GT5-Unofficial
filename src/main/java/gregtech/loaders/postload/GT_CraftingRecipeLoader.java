package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NotEnoughItems;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;

public class GT_CraftingRecipeLoader implements Runnable {

    private static final String aTextIron1 = "X X";
    private static final String aTextIron2 = "XXX";
    private static final long bits_no_remove_buffered = GT_ModHandler.RecipeBits.NOT_REMOVABLE
        | GT_ModHandler.RecipeBits.BUFFERED;
    private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
        | GT_ModHandler.RecipeBits.BUFFERED;
    private static final String aTextPlateWrench = "PwP";

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding nerfed Vanilla Recipes.");
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.bucket, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "XhX", " X ", 'X', OrePrefixes.plate.get(Materials.AnyIron) });
        ItemStack tMat = new ItemStack(Items.iron_ingot);
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, tMat, null, null, null, null, null, null, null))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "ShS", "XZX", "SdS", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'Z', OrePrefixes.spring.get(Materials.Steel) });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, tMat, null, tMat, tMat, null, tMat, tMat, null))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "XX ", "XXh", "XX ", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'S',
                        OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron) });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, null, tMat, tMat, null, tMat, tMat, tMat, tMat))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { aTextIron1, "XhX", aTextIron2, 'X', OrePrefixes.plate.get(Materials.AnyIron), 'S',
                        OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron) });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler
                .removeRecipe(tMat, null, tMat, tMat, new ItemStack(Blocks.chest, 1, 0), tMat, null, tMat, null))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "XwX", "XCX", " X ", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'S',
                        OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron), 'C',
                        "craftingChest" });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, tMat, tMat, tMat, tMat, tMat, null, null, null))) {
                tStack.stackSize /= 2;
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { " w ", aTextIron2, aTextIron2, 'X', OrePrefixes.stick.get(Materials.AnyIron), 'S',
                        OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron) });
            }
        }

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("ironFence", 6L),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextIron2, aTextIron2, " w ", 'X', OrePrefixes.stick.get(Materials.AnyIron), 'S',
                OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron) });

        tMat = new ItemStack(Items.gold_ingot);
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, tMat, null, null, null, null, null, null, null))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "ShS", "XZX", "SdS", 'X', OrePrefixes.plate.get(Materials.Gold), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'Z', OrePrefixes.spring.get(Materials.Steel) });
            }
        }
        tMat = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L);
        {
            ItemStack tStack;
            if (null != (tStack = GT_ModHandler.removeRecipe(tMat, tMat, tMat, tMat, tMat, tMat, null, null, null))) {
                GT_ModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { aTextIron2, aTextIron2, 'X', OrePrefixes.plate.get(Materials.Rubber) });
            }
        }
        GT_ModHandler.removeRecipeByOutputDelayed(ItemList.Bottle_Empty.get(1L));
        GT_ModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Spray_WeedEx.get(1L));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("reBattery", 1L));
        GT_ModHandler.removeRecipeByOutputDelayed(new ItemStack(Blocks.tnt));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("dynamite", 1L));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("industrialTnt", 1L));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 0));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 1));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 2));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 3));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 4));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 5));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "stamps", 1L, 6));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "engine", 1L, 0));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "engine", 1L, 1));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "engine", 1L, 2));
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getModItem(Forestry.ID, "engine", 1L, 4));

        ItemStack tStack = GT_ModHandler
            .removeRecipe(new ItemStack(Blocks.planks, 1, 0), null, null, new ItemStack(Blocks.planks, 1, 0));
        if (tStack != null) {
            GT_ModHandler.addCraftingRecipe(
                GT_Utility.copyAmount(
                    GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                    tStack),
                bits_no_remove_buffered,
                new Object[] { "s", "P", "P", 'P', OrePrefixes.plank.get(Materials.Wood) });
            GT_ModHandler.addCraftingRecipe(
                GT_Utility.copyAmount(
                    GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize / 2 : tStack.stackSize,
                    tStack),
                bits_no_remove_buffered,
                new Object[] { "P", "P", 'P', OrePrefixes.plank.get(Materials.Wood) });
        }
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stone_button, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "S", "S", 'S', OrePrefixes.stone });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.stone_button, 1, 0),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stone });

        GT_Log.out.println("GT_Mod: Adding Vanilla Convenience Recipes.");

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 3),
            bits_no_remove_buffered,
            new Object[] { "f", "X", 'X', new ItemStack(Blocks.double_stone_slab, 1, 8) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.gravel, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.cobblestone, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.sand, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.gravel, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.cobblestone, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.stone, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 2),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.stonebrick, 1, 0) });

        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 8),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.double_stone_slab, 1, 0) });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 0),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.double_stone_slab, 1, 8) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.cobblestone, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 3) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.brick_block, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 4) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 5) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.nether_brick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 6) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.quartz_block, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 7) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 8),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 8) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 1),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 1) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 2),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 2) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 3),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 3) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 4),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 4) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 5),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 5) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 6),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 6) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 7),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 7) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', new ItemStack(Blocks.deadbush, 1, 32767) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', new ItemStack(Blocks.tallgrass, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', OrePrefixes.treeSapling });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.comparator, 1, 0),
            bits_no_remove_buffered,
            new Object[] { " T ", "TQT", "SSS", 'Q', OreDictNames.craftingQuartz, 'S', OrePrefixes.stoneSmooth, 'T',
                OreDictNames.craftingRedstoneTorch });

        GT_Log.out.println("GT_Mod: Adding Tool Recipes.");
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.minecart, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { " h ", "PwP", "WPW", 'P', OrePrefixes.plate.get(Materials.AnyIron), 'W',
                ItemList.Component_Minecart_Wheels_Iron });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.minecart, 1),
            bits_no_remove_buffered,
            new Object[] { " h ", "PwP", "WPW", 'P', OrePrefixes.plate.get(Materials.Steel), 'W',
                ItemList.Component_Minecart_Wheels_Steel });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.chest_minecart, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.furnace_minecart, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.hopper_minecart, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X',
                new ItemStack(Blocks.hopper, 1, 32767) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.tnt_minecart, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', new ItemStack(Blocks.tnt, 1, 32767) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_helmet, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RRR", "RhR", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_chestplate, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RhR", "RRR", "RRR", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_leggings, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RRR", "RhR", "R R", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_boots, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "R R", "RhR", 'R', OrePrefixes.ring.get(Materials.Steel) });

        GT_Log.out.println("GT_Mod: Adding Wool and Color releated Recipes.");
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 1),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeOrange });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 2),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeMagenta });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 3),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLightBlue });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 4),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeYellow });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 5),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLime });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 6),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyePink });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 7),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeGray });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 8),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLightGray });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 9),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeCyan });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 10),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyePurple });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 11),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBlue });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 12),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBrown });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 13),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeGreen });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 14),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeRed });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 15),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBlack });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stained_glass, 8, 0),
            bits_no_remove_buffered,
            new Object[] { "GGG", "GDG", "GGG", 'G', new ItemStack(Blocks.glass, 1), 'D', Dyes.dyeWhite });

        GT_Log.out.println("GT_Mod: Putting a Potato on a Stick.");
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_PotatoChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.foil.get(Materials.Aluminium), ItemList.Food_PotatoChips });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_ChiliChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.foil.get(Materials.Aluminium), ItemList.Food_ChiliChips });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_Fries.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.plateDouble.get(Materials.Paper), ItemList.Food_Fries });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Chum_On_Stick.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stick.get(Materials.Wood), ItemList.Food_Chum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Potato_On_Stick.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stick.get(Materials.Wood), ItemList.Food_Raw_Potato });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Potato_On_Stick_Roasted.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stick.get(Materials.Wood), ItemList.Food_Baked_Potato });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough.get(1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.bucket.get(Materials.Water), OrePrefixes.dust.get(Materials.Wheat) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Sugar.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", OrePrefixes.dust.get(Materials.Sugar) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Chocolate.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", OrePrefixes.dust.get(Materials.Cocoa) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Chocolate.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", OrePrefixes.dust.get(Materials.Chocolate) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Flat_Dough.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", ToolDictNames.craftingToolRollingPin });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Bun.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough" });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Bread.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "foodDough" });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Baguette.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "foodDough", "foodDough" });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Cake.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Dough_Sugar, ItemList.Food_Dough_Sugar, ItemList.Food_Dough_Sugar,
                ItemList.Food_Dough_Sugar });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_ChiliChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_PotatoChips, OrePrefixes.dust.get(Materials.Chili) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Buns.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Breads.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Baguettes.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Bun.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Bread.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Baguette.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, OrePrefixes.dust.get(Materials.MeatCooked) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Chum.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Chum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun,
                OrePrefixes.dust.get(Materials.MeatCooked) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Chum.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Chum });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, new ItemStack(Items.cooked_porkchop, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, new ItemStack(Items.cooked_beef, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread,
                new ItemStack(Items.cooked_porkchop, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread,
                new ItemStack(Items.cooked_beef, 1) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, new ItemStack(Items.cooked_porkchop, 1),
                new ItemStack(Items.cooked_porkchop, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, new ItemStack(Items.cooked_beef, 1),
                new ItemStack(Items.cooked_beef, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette,
                new ItemStack(Items.cooked_porkchop, 1), new ItemStack(Items.cooked_porkchop, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette,
                new ItemStack(Items.cooked_beef, 1), new ItemStack(Items.cooked_beef, 1) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, OrePrefixes.dust.get(Materials.MeatCooked) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Cheese.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "foodCheese" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Lemon.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropLemon" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Tomato.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropTomato" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Onion.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropOnion" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Cucumber.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropCucumber" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Bun.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Bun });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Bread.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Bread });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Baguette.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Baguette });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Raw_PotatoChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropPotato" });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Raw_Cookie.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Dough_Chocolate });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Food_Raw_Fries.get(1L),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', "cropPotato" });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.bowl, 1),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', OrePrefixes.plank.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Rubber, 1L),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', OrePrefixes.plate.get(Materials.Rubber) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.arrow, 1),
            bits_no_remove_buffered | GT_ModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "  H", " S ", "F  ", 'H', new ItemStack(Items.flint, 1, 32767), 'S',
                OrePrefixes.stick.get(Materials.Wood), 'F', OreDictNames.craftingFeather });

        GT_ModHandler.removeRecipe(
            new ItemStack(Blocks.planks),
            null,
            new ItemStack(Blocks.planks),
            null,
            new ItemStack(Blocks.planks));
        GT_ModHandler.removeRecipeByOutputDelayed(ItemList.Food_Baked_Bread.get(1L));
        GT_ModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.cookie, 1));
        GT_ModHandler.removeRecipe(
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L));
        if (null != GT_Utility.setStack(
            GT_ModHandler.getRecipeOutput(
                true,
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
                null,
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L)),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 1L))) {
            GT_Log.out.println("GT_Mod: Changed Forestrys Bronze Recipe");
        }
        tStack = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L);

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getRecipeOutput(
                null,
                new ItemStack(Blocks.sand, 1, 0),
                null,
                null,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Apatite, 1L),
                null,
                null,
                new ItemStack(Blocks.sand, 1, 0),
                null),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "S", "A", "S", 'A', OrePrefixes.dust.get(Materials.Apatite), 'S',
                new ItemStack(Blocks.sand, 1, 32767) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getRecipeOutput(
                tStack,
                tStack,
                tStack,
                tStack,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Apatite, 1L),
                tStack,
                tStack,
                tStack,
                tStack),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "SSS", "SAS", "SSS", 'A', OrePrefixes.dust.get(Materials.Apatite), 'S',
                OrePrefixes.dust.get(Materials.Ash) });

        GT_Log.out.println("GT_Mod: Adding Mixed Metal Ingot Recipes.");
        GT_ModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Mixed_Metal_Ingot.get(1L));

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.AnyIron), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Nickel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Invar), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Steel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.StainlessSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Titanium), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Tungsten), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(6L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Bronze), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(6L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Brass), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.Zinc) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.TungstenSteel), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.AnnealedCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.RoseGold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.AstralSilver) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z',
                OrePrefixes.plate.get(Materials.AnnealedCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.RoseGold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Iridium), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.AstralSilver) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSG), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z',
                OrePrefixes.plate.get(Materials.AnnealedCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSG), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.RoseGold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSG), 'Y',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'Z', OrePrefixes.plate.get(Materials.AstralSilver) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSE), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.AnnealedCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSE), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.RoseGold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSE), 'Y',
                OrePrefixes.plate.get(Materials.Chrome), 'Z', OrePrefixes.plate.get(Materials.AstralSilver) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSS), 'Y',
                OrePrefixes.plate.get(Materials.TungstenSteel), 'Z', OrePrefixes.plate.get(Materials.AnnealedCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSS), 'Y',
                OrePrefixes.plate.get(Materials.TungstenSteel), 'Z', OrePrefixes.plate.get(Materials.RoseGold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.HSSS), 'Y',
                OrePrefixes.plate.get(Materials.TungstenSteel), 'Z', OrePrefixes.plate.get(Materials.AstralSilver) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Fluid.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GPG", "IwI", "GPG", 'G', GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 1L),
                'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L), 'I',
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 1L) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Item.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GPG", "IwI", "GPG", 'G', GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 1L),
                'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L), 'I',
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Tin, 1L) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(32L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "IwI", "PPP", 'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L),
                'I', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 1L) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Item_Pipe.get(32L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "IwI", "PPP", 'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L),
                'I', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Tin, 1L) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(22L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Naquadah), 'Y',
                OrePrefixes.plate.get(Materials.Iridium), 'Z', OrePrefixes.plate.get(Materials.HSSG) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(24L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Naquadah), 'Y',
                OrePrefixes.plate.get(Materials.Iridium), 'Z', OrePrefixes.plate.get(Materials.HSSE) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(26L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Naquadah), 'Y',
                OrePrefixes.plate.get(Materials.Iridium), 'Z', OrePrefixes.plate.get(Materials.HSSS) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(28L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.NaquadahAlloy), 'Y',
                OrePrefixes.plate.get(Materials.Osmiridium), 'Z', OrePrefixes.plate.get(Materials.HSSE) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(30L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.NaquadahAlloy), 'Y',
                OrePrefixes.plate.get(Materials.Osmiridium), 'Z', OrePrefixes.plate.get(Materials.HSSG) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(32L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.NaquadahAlloy), 'Y',
                OrePrefixes.plate.get(Materials.Osmiridium), 'Z', OrePrefixes.plate.get(Materials.HSSS) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(34L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Neutronium), 'Y',
                OrePrefixes.plate.get(Materials.EnergeticAlloy), 'Z', OrePrefixes.plate.get(Materials.Naquadah) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(36L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Neutronium), 'Y',
                OrePrefixes.plate.get(Materials.EnergeticAlloy), 'Z', OrePrefixes.plate.get(Materials.NaquadahAlloy) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(38L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.Neutronium), 'Y',
                OrePrefixes.plate.get(Materials.EnergeticAlloy), 'Z', OrePrefixes.plate.get(Materials.Draconium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(40L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.BlackPlutonium), 'Y',
                OrePrefixes.plate.get(Materials.Sunnarium), 'Z', OrePrefixes.plate.get(Materials.Naquadah) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(42L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.BlackPlutonium), 'Y',
                OrePrefixes.plate.get(Materials.Sunnarium), 'Z', OrePrefixes.plate.get(Materials.NaquadahAlloy) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(44L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.BlackPlutonium), 'Y',
                OrePrefixes.plate.get(Materials.Sunnarium), 'Z', OrePrefixes.plate.get(Materials.Draconium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(48L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.DraconiumAwakened), 'Y',
                OrePrefixes.plate.get(Materials.Neutronium), 'Z', OrePrefixes.plate.get(Materials.HSSS) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(52L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.DraconiumAwakened), 'Y',
                OrePrefixes.plate.get(Materials.Neutronium), 'Z', OrePrefixes.plate.get(Materials.Naquadah) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(56L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.DraconiumAwakened), 'Y',
                OrePrefixes.plate.get(Materials.Neutronium), 'Z', OrePrefixes.plate.get(Materials.NaquadahAlloy) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(64L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', OrePrefixes.plate.get(Materials.DraconiumAwakened), 'Y',
                OrePrefixes.plate.get(Materials.Neutronium), 'Z', OrePrefixes.plate.get(Materials.BlackPlutonium) });

        GT_Log.out.println("GT_Mod: Beginning to add regular Crafting Recipes.");
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("scaffold", 4L),
            bits_no_remove_buffered,
            new Object[] { "WWW", " S ", "S S", 'W', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stick.get(Materials.AnyIron), OrePrefixes.dust.get(Materials.Redstone),
                OrePrefixes.dust.get(Materials.Redstone), OrePrefixes.dust.get(Materials.Redstone),
                OrePrefixes.dust.get(Materials.Redstone) });
        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Paper, 1L),
            bits_no_remove_buffered,
            new Object[] { "PPk", 'P', OrePrefixes.plate.get(Materials.Paper) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.torch, 2),
            bits_no_remove_buffered,
            new Object[] { "C", "S", 'C', OrePrefixes.dust.get(Materials.Sulfur), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.torch, 6),
            bits_no_remove_buffered,
            new Object[] { "C", "S", 'C', OrePrefixes.dust.get(Materials.TricalciumPhosphate), 'S',
                OrePrefixes.stick.get(Materials.Wood) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', OrePrefixes.plank.get(Materials.Wood), 'C',
                OrePrefixes.stoneCobble, 'R', OrePrefixes.dust.get(Materials.Redstone), 'B',
                OrePrefixes.ingot.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', OrePrefixes.plank.get(Materials.Wood), 'C',
                OrePrefixes.stoneCobble, 'R', OrePrefixes.dust.get(Materials.Redstone), 'B',
                OrePrefixes.ingot.get(Materials.AnyBronze) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', OrePrefixes.plank.get(Materials.Wood), 'C',
                OrePrefixes.stoneCobble, 'R', OrePrefixes.dust.get(Materials.Redstone), 'B',
                OrePrefixes.ingot.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', OrePrefixes.plank.get(Materials.Wood), 'C',
                OrePrefixes.stoneCobble, 'R', OrePrefixes.dust.get(Materials.Redstone), 'B',
                OrePrefixes.ingot.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', OrePrefixes.plank.get(Materials.Wood), 'C',
                OrePrefixes.stoneCobble, 'R', OrePrefixes.dust.get(Materials.Redstone), 'B',
                OrePrefixes.ingot.get(Materials.Titanium) });

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorVent", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "AIA", "I I", "AIA", 'I', new ItemStack(Blocks.iron_bars, 1), 'A',
                OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorPlatingExplosive", 1L),
            bits_no_remove_buffered,
            new Object[] { GT_ModHandler.getIC2Item("reactorPlating", 1L), OrePrefixes.plate.get(Materials.Lead) });
        if (!Materials.Steel.mBlastFurnaceRequired) {
            GT_ModHandler.addShapelessCraftingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1L),
                bits_no_remove_buffered,
                new Object[] { OrePrefixes.dust.get(Materials.Iron), OrePrefixes.dust.get(Materials.Coal),
                    OrePrefixes.dust.get(Materials.Coal) });
        }

        GT_ModHandler.removeRecipeByOutputDelayed(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1L));
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 3L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.AnyCopper),
                OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.Zinc) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Brass, 9L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Tetrahedrite), OrePrefixes.dust.get(Materials.Tetrahedrite),
                OrePrefixes.dust.get(Materials.Tetrahedrite), OrePrefixes.dust.get(Materials.Zinc) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 3L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.AnyCopper),
                OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.Tin) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Bronze, 9L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Tetrahedrite), OrePrefixes.dust.get(Materials.Tetrahedrite),
                OrePrefixes.dust.get(Materials.Tetrahedrite), OrePrefixes.dust.get(Materials.Tin) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Invar, 9L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Iron), OrePrefixes.dust.get(Materials.Iron),
                OrePrefixes.dust.get(Materials.Nickel) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Cupronickel, 6L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Nickel), OrePrefixes.dust.get(Materials.AnyCopper) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RoseGold, 4L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Gold), OrePrefixes.dust.get(Materials.Gold),
                OrePrefixes.dust.get(Materials.Gold), OrePrefixes.dust.get(Materials.Gold),
                OrePrefixes.dust.get(Materials.AnyCopper) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SterlingSilver, 4L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Silver), OrePrefixes.dust.get(Materials.Silver),
                OrePrefixes.dust.get(Materials.Silver), OrePrefixes.dust.get(Materials.Silver),
                OrePrefixes.dust.get(Materials.AnyCopper) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackBronze, 4L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Gold), OrePrefixes.dust.get(Materials.Silver),
                OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.AnyCopper),
                OrePrefixes.dust.get(Materials.AnyCopper) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BismuthBronze, 4L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Bismuth), OrePrefixes.dust.get(Materials.Zinc),
                OrePrefixes.dust.get(Materials.AnyCopper), OrePrefixes.dust.get(Materials.AnyCopper),
                OrePrefixes.dust.get(Materials.AnyCopper) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CobaltBrass, 8L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Brass), OrePrefixes.dust.get(Materials.Brass),
                OrePrefixes.dust.get(Materials.Brass), OrePrefixes.dust.get(Materials.Brass),
                OrePrefixes.dust.get(Materials.Brass), OrePrefixes.dust.get(Materials.Brass),
                OrePrefixes.dust.get(Materials.Brass), OrePrefixes.dust.get(Materials.Aluminium),
                OrePrefixes.dust.get(Materials.Cobalt) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DamascusSteel, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Steel), OrePrefixes.dustSmall.get(Materials.Nickel),
                OrePrefixes.dustSmall.get(Materials.Nickel), OrePrefixes.dustSmall.get(Materials.Nickel),
                OrePrefixes.dustTiny.get(Materials.Coal), OrePrefixes.dustTiny.get(Materials.Silicon),
                OrePrefixes.dustTiny.get(Materials.Manganese), OrePrefixes.dustTiny.get(Materials.Chrome),
                OrePrefixes.dustTiny.get(Materials.Molybdenum) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DamascusSteel, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Steel), OrePrefixes.dustSmall.get(Materials.Manganese),
                OrePrefixes.dustSmall.get(Materials.Manganese), OrePrefixes.dustSmall.get(Materials.Chrome),
                OrePrefixes.dustSmall.get(Materials.Chrome), OrePrefixes.dustTiny.get(Materials.Coal),
                OrePrefixes.dustTiny.get(Materials.Silicon), OrePrefixes.dustTiny.get(Materials.Vanadium) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RedstoneAlloy, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Redstone), OrePrefixes.dust.get(Materials.Silicon),
                OrePrefixes.dust.get(Materials.Coal) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CrudeSteel, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Clay), OrePrefixes.dust.get(Materials.Flint),
                OrePrefixes.dust.get(Materials.Stone) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ConductiveIron, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.RedstoneAlloy), OrePrefixes.dust.get(Materials.Iron),
                OrePrefixes.dust.get(Materials.Silver) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnergeticAlloy, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.ConductiveIron), OrePrefixes.dust.get(Materials.Gold),
                OrePrefixes.dust.get(Materials.BlackSteel) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnergeticSilver, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.ConductiveIron), OrePrefixes.dust.get(Materials.Silver),
                OrePrefixes.dust.get(Materials.BlackSteel) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectricalSteel, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Steel), OrePrefixes.dust.get(Materials.Coal),
                OrePrefixes.dust.get(Materials.Silicon) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soularium, 2L),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.soul_sand, 1, 32767), OrePrefixes.dust.get(Materials.Gold),
                OrePrefixes.dust.get(Materials.Ash) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkSteel, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.ElectricalSteel), OrePrefixes.dust.get(Materials.Coal),
                OrePrefixes.dust.get(Materials.Obsidian) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manyullyn, 3L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Ardite), OrePrefixes.dust.get(Materials.Ardite),
                OrePrefixes.dust.get(Materials.Ardite), OrePrefixes.dust.get(Materials.Ardite),
                OrePrefixes.dust.get(Materials.Cobalt), OrePrefixes.dust.get(Materials.Cobalt),
                OrePrefixes.dust.get(Materials.Cobalt), OrePrefixes.dust.get(Materials.Cobalt) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Haderoth, 1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Rubracium), OrePrefixes.dust.get(Materials.Mithril) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Celenegil, 1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Platinum), OrePrefixes.dust.get(Materials.Orichalcum) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Iron), OrePrefixes.dust.get(Materials.LiveRoot),
                OrePrefixes.dustTiny.get(Materials.Gold) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Hepatizon, 3L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Copper), OrePrefixes.dust.get(Materials.Copper),
                OrePrefixes.dust.get(Materials.Copper), OrePrefixes.dustTiny.get(Materials.Gold),
                OrePrefixes.dustTiny.get(Materials.Gold), OrePrefixes.dustTiny.get(Materials.Gold),
                OrePrefixes.dustTiny.get(Materials.Silver), OrePrefixes.dustTiny.get(Materials.Silver),
                OrePrefixes.dustTiny.get(Materials.Silver) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Angmallen, 2L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Iron), OrePrefixes.dust.get(Materials.Gold) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Inolashite, 1L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Alduorite), OrePrefixes.dust.get(Materials.Ceruclase) });

        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Coal), OrePrefixes.dust.get(Materials.Coal),
                OrePrefixes.dust.get(Materials.Coal), OrePrefixes.dust.get(Materials.Sulfur),
                OrePrefixes.dust.get(Materials.Saltpeter), OrePrefixes.dust.get(Materials.Saltpeter) });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Charcoal), OrePrefixes.dust.get(Materials.Charcoal),
                OrePrefixes.dust.get(Materials.Charcoal), OrePrefixes.dust.get(Materials.Sulfur),
                OrePrefixes.dust.get(Materials.Saltpeter), OrePrefixes.dust.get(Materials.Saltpeter) });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Carbon), OrePrefixes.dust.get(Materials.Carbon),
                OrePrefixes.dust.get(Materials.Carbon), OrePrefixes.dust.get(Materials.Sulfur),
                OrePrefixes.dust.get(Materials.Saltpeter), OrePrefixes.dust.get(Materials.Saltpeter) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IndiumGalliumPhosphide, 3L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Indium), OrePrefixes.dust.get(Materials.Gallium),
                OrePrefixes.dust.get(Materials.Phosphorus) });

        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 5L),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.dust.get(Materials.Potassium), OrePrefixes.cell.get(Materials.Nitrogen),
                OrePrefixes.cell.get(Materials.Oxygen), OrePrefixes.cell.get(Materials.Oxygen),
                OrePrefixes.cell.get(Materials.Oxygen) });
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("carbonFiber", 1L));

        ItemStack[] tChestAndTank = new ItemStack[] { ItemList.Super_Chest_EV.get(1), ItemList.Super_Chest_IV.get(1),
            ItemList.Super_Chest_HV.get(1), ItemList.Super_Chest_MV.get(1), ItemList.Super_Chest_LV.get(1),
            ItemList.Quantum_Chest_EV.get(1), ItemList.Quantum_Chest_IV.get(1), ItemList.Quantum_Chest_HV.get(1),
            ItemList.Quantum_Chest_MV.get(1), ItemList.Quantum_Chest_LV.get(1), ItemList.Super_Tank_EV.get(1),
            ItemList.Super_Tank_IV.get(1), ItemList.Super_Tank_HV.get(1), ItemList.Super_Tank_MV.get(1),
            ItemList.Super_Tank_LV.get(1), ItemList.Quantum_Tank_EV.get(1), ItemList.Quantum_Tank_IV.get(1),
            ItemList.Quantum_Tank_HV.get(1), ItemList.Quantum_Tank_MV.get(1), ItemList.Quantum_Tank_LV.get(1) };
        for (ItemStack tItem : tChestAndTank) {
            GT_ModHandler.addShapelessCraftingRecipe(tItem, new Object[] { tItem });
        }

        List<ItemStack> iToRemoveAndHide = Arrays
            .stream(
                new String[] { "copperCableItem", "insulatedCopperCableItem", "goldCableItem", "insulatedGoldCableItem",
                    "insulatedIronCableItem", "glassFiberCableItem", "tinCableItem", "ironCableItem",
                    "insulatedTinCableItem", "detectorCableItem", "splitterCableItem", "electrolyzer", "cutter" })
            .map(x -> GT_ModHandler.getIC2Item(x, 1L))
            .collect(Collectors.toList());

        if (NotEnoughItems.isModLoaded()) {
            iToRemoveAndHide.forEach(item -> {
                codechicken.nei.api.API.hideItem(item);
                GT_ModHandler.removeRecipeByOutputDelayed(item);
            });
        }

        Arrays
            .stream(
                new String[] { "batBox", "mfeUnit", "lvTransformer", "mvTransformer", "hvTransformer", "evTransformer",
                    "cesuUnit", "luminator", "teleporter", "energyOMat", "advBattery", "boatElectric", "cropnalyzer",
                    "coil", "powerunit", "powerunitsmall", "remote", "odScanner", "ovScanner", "solarHelmet",
                    "staticBoots", "ecMeter", "obscurator", "overclockerUpgrade", "transformerUpgrade",
                    "energyStorageUpgrade", "ejectorUpgrade", "suBattery", "frequencyTransmitter", "pullingUpgrade" })
            .map(x -> GT_ModHandler.getIC2Item(x, 1L))
            .forEach(GT_ModHandler::removeRecipeByOutputDelayed);

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("batBox", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "BBB", "PPP", 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'P',
                OrePrefixes.plank.get(Materials.Wood), 'B', OrePrefixes.battery.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("mfeUnit", 1L),
            bits_no_remove_buffered,
            new Object[] { "CEC", "EME", "CEC", 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'E',
                OrePrefixes.battery.get(Materials.IV), 'M', GT_ModHandler.getIC2Item("machine", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("lvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "POP", "PCP", 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'O',
                GT_ModHandler.getIC2Item("coil", 1L), 'P', OrePrefixes.plank.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("mvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { "CMC", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'M',
                GT_ModHandler.getIC2Item("machine", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("hvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "IMB", " C ", 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'M',
                GT_ModHandler.getIC2Item("mvTransformer", 1L), 'I', OrePrefixes.circuit.get(Materials.LV), 'B',
                OrePrefixes.battery.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("evTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "IMB", " C ", 'C', OrePrefixes.cableGt01.get(Materials.Aluminium), 'M',
                GT_ModHandler.getIC2Item("hvTransformer", 1L), 'I', OrePrefixes.circuit.get(Materials.HV), 'B',
                OrePrefixes.battery.get(Materials.LuV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("cesuUnit", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "BBB", "PPP", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'P',
                OrePrefixes.plate.get(Materials.Bronze), 'B', OrePrefixes.battery.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("teleporter", 1L),
            bits_no_remove_buffered,
            new Object[] { "GFG", "CMC", "GDG", 'C', OrePrefixes.cableGt01.get(Materials.Platinum), 'G',
                OrePrefixes.circuit.get(Materials.HV), 'D', OrePrefixes.gem.get(Materials.Diamond), 'M',
                GT_ModHandler.getIC2Item("machine", 1L), 'F', GT_ModHandler.getIC2Item("frequencyTransmitter", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("energyOMat", 1L),
            bits_no_remove_buffered,
            new Object[] { "RBR", "CMC", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'R',
                OrePrefixes.dust.get(Materials.Redstone), 'B', OrePrefixes.battery.get(Materials.LV), 'M',
                GT_ModHandler.getIC2Item("machine", 1L) });

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("boatElectric", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "XWX", aTextIron2, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'X',
                OrePrefixes.plate.get(Materials.Iron), 'W', GT_ModHandler.getIC2Item("waterMill", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("cropnalyzer", 1L),
            bits_no_remove_buffered,
            new Object[] { "CC ", "RGR", "RIR", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'R',
                OrePrefixes.dust.get(Materials.Redstone), 'G', OrePrefixes.block.get(Materials.Glass), 'I',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("coil", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "CXC", "CCC", 'C', OrePrefixes.wireGt01.get(Materials.Copper), 'X',
                OrePrefixes.ingot.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("powerunit", 1L),
            bits_no_remove_buffered,
            new Object[] { "BCA", "BIM", "BCA", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'B',
                OrePrefixes.battery.get(Materials.LV), 'A', GT_ModHandler.getIC2Item("casingiron", 1L), 'I',
                OrePrefixes.circuit.get(Materials.LV), 'M', GT_ModHandler.getIC2Item("elemotor", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("powerunitsmall", 1L),
            bits_no_remove_buffered,
            new Object[] { " CA", "BIM", " CA", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'B',
                OrePrefixes.battery.get(Materials.LV), 'A', GT_ModHandler.getIC2Item("casingiron", 1L), 'I',
                OrePrefixes.circuit.get(Materials.LV), 'M', GT_ModHandler.getIC2Item("elemotor", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("remote", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "TLT", " F ", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'L',
                OrePrefixes.dust.get(Materials.Lapis), 'T', GT_ModHandler.getIC2Item("casingtin", 1L), 'F',
                GT_ModHandler.getIC2Item("frequencyTransmitter", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("odScanner", 1L),
            bits_no_remove_buffered,
            new Object[] { "PGP", "CBC", "WWW", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'G',
                OrePrefixes.dust.get(Materials.Glowstone), 'B', OrePrefixes.battery.get(Materials.HV), 'C',
                OrePrefixes.circuit.get(Materials.HV), 'P', GT_ModHandler.getIC2Item("casinggold", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("ovScanner", 1L),
            bits_no_remove_buffered,
            new Object[] { "PDP", "GCG", "WSW", 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                OrePrefixes.dust.get(Materials.Glowstone), 'D', OrePrefixes.battery.get(Materials.IV), 'C',
                OrePrefixes.circuit.get(Materials.HV), 'P', GT_ModHandler.getIC2Item("casinggold", 1L), 'S',
                GT_ModHandler.getIC2Item("odScanner", 1L) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("staticBoots", 1L),
            bits_no_remove_buffered,
            new Object[] { "I I", "IWI", "CCC", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'I',
                OrePrefixes.ingot.get(Materials.Iron), 'W', new ItemStack(Blocks.wool) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("ecMeter", 1L),
            bits_no_remove_buffered,
            new Object[] { " G ", "CIC", "C C", 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'G',
                OrePrefixes.dust.get(Materials.Glowstone), 'I', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("obscurator", 1L),
            bits_no_remove_buffered,
            new Object[] { "RER", "CAC", "RRR", 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'R',
                OrePrefixes.dust.get(Materials.Redstone), 'E', OrePrefixes.battery.get(Materials.HV), 'A',
                OrePrefixes.circuit.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("overclockerUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "WEW", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'C',
                GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 1), 'E', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("transformerUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "GGG", "WTW", "GEG", 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'T',
                GT_ModHandler.getIC2Item("mvTransformer", 1L), 'E', OrePrefixes.circuit.get(Materials.LV), 'G',
                OrePrefixes.block.get(Materials.Glass) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("energyStorageUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PPP", "WBW", "PEP", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'E',
                OrePrefixes.circuit.get(Materials.LV), 'P', OrePrefixes.plank.get(Materials.Wood), 'B',
                OrePrefixes.battery.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("ejectorUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PHP", "WEW", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'E',
                OrePrefixes.circuit.get(Materials.LV), 'P', new ItemStack(Blocks.piston), 'H',
                new ItemStack(Blocks.hopper) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("suBattery", 1L),
            bits_no_remove_buffered,
            new Object[] { "W", "C", "R", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'C',
                OrePrefixes.dust.get(Materials.HydratedCoal), 'R', OrePrefixes.dust.get(Materials.Redstone) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("pullingUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PHP", "WEW", 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'P',
                new ItemStack(Blocks.sticky_piston), 'R', new ItemStack(Blocks.hopper), 'E',
                OrePrefixes.circuit.get(Materials.LV) });

        if (NotEnoughItems.isModLoaded()) {
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorUraniumSimple", 1L, 1));
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorUraniumDual", 1L, 1));
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorUraniumQuad", 1L, 1));
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorMOXSimple", 1L, 1));
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorMOXDual", 1L, 1));
            codechicken.nei.api.API.hideItem(GT_ModHandler.getIC2Item("reactorMOXQuad", 1L, 1));
        }
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("UranFuel", 1L),
            bits_no_remove_buffered,
            new Object[] { "UUU", "NNN", "UUU", 'U', OrePrefixes.ingot.get(Materials.Uranium), 'N',
                OrePrefixes.nugget.get(Materials.Uranium235) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("MOXFuel", 1L),
            bits_no_remove_buffered,
            new Object[] { "UUU", "NNN", "UUU", 'U', OrePrefixes.ingot.get(Materials.Uranium), 'N',
                OrePrefixes.ingot.get(Materials.Plutonium) });

        GT_ModHandler.removeRecipeByOutputDelayed(Ic2Items.miningLaser.copy());
        GT_ModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', OrePrefixes.plate.get(Materials.Titanium), 'G',
                OrePrefixes.gemExquisite.get(Materials.Diamond), 'E', ItemList.Emitter_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'S', OrePrefixes.screw.get(Materials.Titanium), 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    GT_Values.W) });
        GT_ModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', OrePrefixes.plate.get(Materials.Titanium), 'G',
                OrePrefixes.gemExquisite.get(Materials.Ruby), 'E', ItemList.Emitter_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'S', OrePrefixes.screw.get(Materials.Titanium), 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    GT_Values.W) });
        GT_ModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', OrePrefixes.plate.get(Materials.Titanium), 'G',
                OrePrefixes.gemExquisite.get(Materials.Jasper), 'E', ItemList.Emitter_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'S', OrePrefixes.screw.get(Materials.Titanium), 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    GT_Values.W) });
        GT_ModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', OrePrefixes.plate.get(Materials.Titanium), 'G',
                OrePrefixes.gemExquisite.get(Materials.GarnetRed), 'E', ItemList.Emitter_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'S', OrePrefixes.screw.get(Materials.Titanium), 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    GT_Values.W) });

        GT_ModHandler.removeRecipeDelayed(GT_ModHandler.getIC2Item("miningPipe", 8));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("miningPipe", 1),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", 'P', OrePrefixes.pipeSmall.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("luminator", 16L),
            bits_no_remove_buffered,
            new Object[] { "RTR", "GHG", "GGG", 'H', OrePrefixes.cell.get(Materials.Helium), 'T',
                OrePrefixes.ingot.get(Materials.Tin), 'R', OrePrefixes.ingot.get(Materials.AnyIron), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("luminator", 16L),
            bits_no_remove_buffered,
            new Object[] { "RTR", "GHG", "GGG", 'H', OrePrefixes.cell.get(Materials.Mercury), 'T',
                OrePrefixes.ingot.get(Materials.Tin), 'R', OrePrefixes.ingot.get(Materials.AnyIron), 'G',
                new ItemStack(Blocks.glass, 1) });

        GT_ModHandler.removeRecipeDelayed(
            tStack = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
            tStack,
            tStack,
            tStack,
            new ItemStack(Items.coal, 1, 0),
            tStack,
            tStack,
            tStack,
            tStack);
        GT_ModHandler.removeRecipeDelayed(
            tStack = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
            tStack,
            tStack,
            tStack,
            new ItemStack(Items.coal, 1, 1),
            tStack,
            tStack,
            tStack,
            tStack);
        GT_ModHandler.removeRecipeDelayed(
            null,
            tStack = new ItemStack(Items.coal, 1),
            null,
            tStack,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
            tStack,
            null,
            tStack,
            null);

        GT_ModHandler.removeFurnaceSmelting(new ItemStack(Blocks.hopper));

        GT_Log.out.println("GT_Mod: Applying harder Recipes for several Blocks."); // TODO: Not Buffered

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("reactorReflectorThick", 1L, 1));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorReflectorThick", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { " N ", "NBN", " N ", 'B', OrePrefixes.plateDouble.get(Materials.Beryllium), 'N',
                GT_ModHandler.getIC2Item("reactorReflector", 1L, 1) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorReflectorThick", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { " B ", "NCN", " B ", 'B', OrePrefixes.plate.get(Materials.Beryllium), 'N',
                GT_ModHandler.getIC2Item("reactorReflector", 1L, 1), 'C',
                OrePrefixes.plate.get(Materials.TungstenCarbide) });
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("reactorReflector", 1L, 1));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorReflector", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "TGT", "GSG", "TGT", 'T', OrePrefixes.plate.get(Materials.Tin), 'G',
                OrePrefixes.dust.get(Materials.Graphite), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("reactorReflector", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "TTT", "GSG", "TTT", 'T', OrePrefixes.plate.get(Materials.TinAlloy), 'G',
                OrePrefixes.dust.get(Materials.Graphite), 'S', OrePrefixes.plate.get(Materials.Beryllium) });

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("crophavester", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("crophavester", 1L),
            bits_no_remove_buffered,
            new Object[] { "ACA", "PMS", "WOW", 'M', ItemList.Hull_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'A',
                ItemList.Robot_Arm_LV, 'P', ItemList.Electric_Piston_LV, 'S', ItemList.Sensor_LV, 'W',
                OrePrefixes.toolHeadSense.get(Materials.Aluminium), 'O', ItemList.Conveyor_Module_LV });

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("RTGenerator", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("RTGenerator", 1L),
            bits_no_remove_buffered,
            new Object[] { "III", "IMI", "ICI", 'I', OrePrefixes.itemCasing.get(Materials.Steel), 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'M', ItemList.Hull_IV });

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("RTHeatGenerator", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("RTHeatGenerator", 1L),
            bits_no_remove_buffered,
            new Object[] { "III", "IMB", "ICI", 'I', OrePrefixes.itemCasing.get(Materials.Steel), 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'M', ItemList.Hull_IV, 'B',
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1) });

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("carbonrotor", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("carbonrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GT_ModHandler.getIC2Item("carbonrotorblade", 1), 'S',
                OrePrefixes.screw.get(Materials.Iridium), 'T', GT_ModHandler.getIC2Item("steelshaft", 1) });
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("steelrotor", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("steelrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GT_ModHandler.getIC2Item("steelrotorblade", 1), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel), 'T', GT_ModHandler.getIC2Item("ironshaft", 1) });
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("ironrotor", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("ironrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GT_ModHandler.getIC2Item("ironrotorblade", 1), 'S',
                OrePrefixes.screw.get(Materials.WroughtIron), 'T', GT_ModHandler.getIC2Item("ironshaft", 1) });
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("woodrotor", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("woodrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GT_ModHandler.getIC2Item("woodrotorblade", 1), 'S',
                OrePrefixes.screw.get(Materials.WroughtIron), 'T', OrePrefixes.stickLong.get(Materials.WroughtIron) });

        if (GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Diamond, 1L) != null) {
            tStack = GT_ModHandler.getRecipeOutput(
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Iron, 1L),
                new ItemStack(Items.redstone, 1),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Diamond, 1L),
                new ItemStack(Items.diamond_pickaxe, 1),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Diamond, 1L));
            GT_ModHandler.removeRecipeByOutputDelayed(tStack);
            GT_ModHandler.addCraftingRecipe(
                tStack,
                bits_no_remove_buffered,
                new Object[] { "ICI", "GIG", "DPD", 'C', OrePrefixes.circuit.get(Materials.HV), 'D',
                    OrePrefixes.gear.get(Materials.Diamond), 'G', OrePrefixes.gear.get(Materials.Gold), 'I',
                    OrePrefixes.gear.get(Materials.Steel), 'P', GT_ModHandler.getIC2Item("diamondDrill", 1L, 32767) });
        }

        GT_ModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.paper));
        GT_ModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.sugar));
        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 2),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "SSS", " m ", 'S', new ItemStack(Items.reeds) });
        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "Sm ", 'S', new ItemStack(Items.reeds) });
        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.paper, Materials.Empty, 2),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { " C ", "SSS", " C ", 'S', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1),
                'C', new ItemStack(Blocks.stone_slab) });

        GT_Log.out.println("GT_Mod: Applying Recipes for Tools");
        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("nanoSaber", 1L));
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("nanoSaber", 1L),
            bits_no_remove_buffered,
            new Object[] { "PI ", "PI ", "CLC", 'L', OrePrefixes.battery.get(Materials.EV), 'I',
                OrePrefixes.plateAlloy.get("Iridium"), 'P', OrePrefixes.plate.get(Materials.Platinum), 'C',
                OrePrefixes.circuit.get(Materials.IV) });

        GT_ModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.flint_and_steel, 1));
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(Items.flint_and_steel, 1),
            bits_no_remove_buffered,
            new Object[] { "S ", " F", 'F', new ItemStack(Items.flint, 1), 'S', "nuggetSteel" });

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("diamondDrill", 1L));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("miningDrill", 1L));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("chainsaw", 1L));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("electricHoe", 1L));

        GT_ModHandler.removeRecipeByOutputDelayed(GT_ModHandler.getIC2Item("electricTreetap", 1L));

        if (GraviSuite.isModLoaded()) {
            GT_ModHandler.removeRecipeByOutputDelayed(
                GT_ModHandler.getModItem(GraviSuite.ID, "advNanoChestPlate", 1, GT_Values.W));
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getModItem(GraviSuite.ID, "advNanoChestPlate", 1, GT_Values.W),
                bits_no_remove_buffered,
                new Object[] { "CJC", "TNT", "WPW", 'C', OrePrefixes.plateAlloy.get(Materials.Advanced), 'T',
                    OrePrefixes.plate.get(Materials.TungstenSteel), 'J',
                    GT_ModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, GT_Values.W), 'N',
                    GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemArmorNanoChestplate", 1, GT_Values.W), 'W',
                    OrePrefixes.wireGt12.get(Materials.Platinum), 'P', OrePrefixes.circuit.get(Materials.IV) });

            GT_ModHandler
                .removeRecipeByOutputDelayed(GT_ModHandler.getModItem(GraviSuite.ID, "advLappack", 1, GT_Values.W));
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getModItem(GraviSuite.ID, "advLappack", 1, GT_Values.W),
                bits_no_remove_buffered,
                new Object[] { "CEC", "EJE", "WPW", 'C', OrePrefixes.plateAlloy.get(Materials.Carbon), 'J',
                    GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemArmorEnergypack", 1L, GT_Values.W), 'E',
                    GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemBatCrystal", 1L, GT_Values.W), 'W',
                    OrePrefixes.wireGt04.get(Materials.Platinum), 'P', OrePrefixes.circuit.get(Materials.EV) });

            GT_ModHandler
                .removeRecipeByOutputDelayed(GT_ModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, GT_Values.W));
            GT_ModHandler.addCraftingRecipe(
                GT_ModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, GT_Values.W),
                bits_no_remove_buffered,
                new Object[] { "CJC", "EXE", "YZY", 'C', OrePrefixes.plateAlloy.get(Materials.Carbon), 'J',
                    GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemArmorJetpackElectric", 1, GT_Values.W), 'E',
                    OrePrefixes.plate.get(Materials.Titanium), 'X',
                    GT_ModHandler.getModItem(IndustrialCraft2.ID, "itemArmorAlloyChestplate", 1L), 'Z',
                    OrePrefixes.circuit.get(Materials.EV), 'Y', OrePrefixes.wireGt02.get(Materials.Platinum) });
        }

        GT_ModHandler.addShapelessCraftingRecipe(
            Materials.Fireclay.getDust(2),
            new Object[] { Materials.Brick.getDust(1), Materials.Clay.getDust(1) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Advanced_Rhodium_Palladium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P',
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.plate), 1, 88), 'F',
                OrePrefixes.frameGt.get(Materials.Chrome) });

        if (Forestry.isModLoaded()) {
            ItemStack[] coverIDs = { ItemList.Plank_Larch.get(2L), ItemList.Plank_Teak.get(2L),
                ItemList.Plank_Acacia_Green.get(2L), ItemList.Plank_Lime.get(2L), ItemList.Plank_Chestnut.get(2L),
                ItemList.Plank_Wenge.get(2L), ItemList.Plank_Baobab.get(2L), ItemList.Plank_Sequoia.get(2L),
                ItemList.Plank_Kapok.get(2L), ItemList.Plank_Ebony.get(2L), ItemList.Plank_Mahagony.get(2L),
                ItemList.Plank_Balsa.get(2L), ItemList.Plank_Willow.get(2L), ItemList.Plank_Walnut.get(2L),
                ItemList.Plank_Greenheart.get(2L), ItemList.Plank_Cherry.get(2L), ItemList.Plank_Mahoe.get(2L),
                ItemList.Plank_Poplar.get(2L), ItemList.Plank_Palm.get(2L), ItemList.Plank_Papaya.get(2L),
                ItemList.Plank_Pine.get(2L), ItemList.Plank_Plum.get(2L), ItemList.Plank_Maple.get(2L),
                ItemList.Plank_Citrus.get(2L) };
            for (int i = 0; i < coverIDs.length; i++) {
                ItemStack slabWood = getModItem(Forestry.ID, "slabs", 1, i);
                ItemStack slabWoodFireproof = getModItem(Forestry.ID, "slabsFireproof", 1, i);

                GT_ModHandler.addCraftingRecipe(
                    coverIDs[i],
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                    new Object[] { "s ", " P", 'P', slabWood });

                GT_ModHandler.addCraftingRecipe(
                    coverIDs[i],
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                    new Object[] { "s ", " P", 'P', slabWoodFireproof });
            }
        }
        GT_ModHandler.addCraftingRecipe(
            GT_ModHandler.getIC2Item("electronicCircuit", 1L),
            GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "RIR", "VBV", "CCC", 'R', ItemList.Circuit_Parts_Resistor.get(1), 'C',
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1), 'V',
                ItemList.Circuit_Parts_Vacuum_Tube.get(1), 'B', ItemList.Circuit_Board_Coated_Basic.get(1), 'I',
                ItemList.IC2_Item_Casing_Steel.get(1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_ModHandler.getIC2Item("electronicCircuit", 1L),
            new Object[] { ItemList.Circuit_Integrated.getWildcard(1L) });
    }
}
