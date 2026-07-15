package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import ic2.core.Ic2Items;

public class CraftingRecipeLoader implements Runnable {

    private static final String aTextIron1 = "X X";
    private static final String aTextIron2 = "XXX";
    private static final long bits_no_remove_buffered = GTModHandler.RecipeBits.NOT_REMOVABLE
        | GTModHandler.RecipeBits.BUFFERED;
    private static final long bits = GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;
    private static final String aTextPlateWrench = "PwP";

    @Override
    public void run() {
        GTLog.out.println("GTMod: Adding nerfed Vanilla Recipes.");
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.bucket, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "XhX", " X ", 'X', "plateAnyIron" });
        ItemStack tMat = new ItemStack(Items.iron_ingot);
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, tMat, null, null, null, null, null, null, null))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "ShS", "XZX", "SdS", 'X', "plateAnyIron", 'S', "screwSteel", 'Z', "springSteel" });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, tMat, null, tMat, tMat, null, tMat, tMat, null))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "XX ", "XXh", "XX ", 'X', "plateAnyIron", 'S', "stickWood", 'I', "ingotAnyIron" });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, null, tMat, tMat, null, tMat, tMat, tMat, tMat))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { aTextIron1, "XhX", aTextIron2, 'X', "plateAnyIron", 'S', "stickWood", 'I',
                        "ingotAnyIron" });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler
                .removeRecipe(tMat, null, tMat, tMat, new ItemStack(Blocks.chest, 1, 0), tMat, null, tMat, null))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "XwX", "XCX", " X ", 'X', "plateAnyIron", 'S', "stickWood", 'I', "ingotAnyIron", 'C',
                        "craftingChest" });
            }
        }
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, tMat, tMat, tMat, tMat, tMat, null, null, null))) {
                tStack.stackSize /= 2;
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { " w ", aTextIron2, aTextIron2, 'X', "stickAnyIron", 'S', "stickWood", 'I',
                        "ingotAnyIron" });
            }
        }

        GTModHandler.addCraftingRecipe(
            ItemList.FenceIron.get(6L),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextIron2, aTextIron2, " w ", 'X', OrePrefixes.stick.get(Materials.AnyIron), 'S',
                OrePrefixes.stick.get(Materials.Wood), 'I', OrePrefixes.ingot.get(Materials.AnyIron) });

        tMat = new ItemStack(Items.gold_ingot);
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, tMat, null, null, null, null, null, null, null))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { "ShS", "XZX", "SdS", 'X', "plateGold", 'S', "screwSteel", 'Z', "springSteel" });
            }
        }
        tMat = MaterialLibAPI.getStack(Materials2Materials.Rubber, Materials2Shapes.ingot, 1);
        {
            ItemStack tStack;
            if (null != (tStack = GTModHandler.removeRecipe(tMat, tMat, tMat, tMat, tMat, tMat, null, null, null))) {
                GTModHandler.addCraftingRecipe(
                    tStack,
                    bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_RECIPES,
                    new Object[] { aTextIron2, aTextIron2, 'X', "plateRubber" });
            }
        }
        GTModHandler.removeRecipeByOutputDelayed(ItemList.Bottle_Empty.get(1L));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("reBattery", 1L));
        GTModHandler.removeRecipeByOutputDelayed(new ItemStack(Blocks.tnt));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("dynamite", 1L));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("industrialTnt", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 0));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 1));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 2));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 3));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 4));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 5));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "stamps", 1L, 6));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "engine", 1L, 0));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "engine", 1L, 1));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "engine", 1L, 2));
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(Forestry.ID, "engine", 1L, 4));

        ItemStack tStack = GTModHandler
            .removeRecipe(new ItemStack(Blocks.planks, 1, 0), null, null, new ItemStack(Blocks.planks, 1, 0));
        if (tStack != null) {
            GTModHandler.addCraftingRecipe(
                GTUtility
                    .copyAmount(GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4, tStack),
                bits_no_remove_buffered,
                new Object[] { "s", "P", "P", 'P', "plankWood" });
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize / 2 : tStack.stackSize, tStack),
                bits_no_remove_buffered,
                new Object[] { "P", "P", 'P', "plankWood" });
        }
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stone_button, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "S", "S", 'S', OrePrefixes.stone });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.stone_button, 1, 0),
            bits_no_remove_buffered,
            new Object[] { OrePrefixes.stone });

        GTLog.out.println("GTMod: Adding Vanilla Convenience Recipes.");

        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 3),
            bits_no_remove_buffered,
            new Object[] { "f", "X", 'X', new ItemStack(Blocks.double_stone_slab, 1, 8) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.gravel, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.cobblestone, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.sand, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.gravel, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.cobblestone, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.stone, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 2),
            bits_no_remove_buffered,
            new Object[] { "h", "X", 'X', new ItemStack(Blocks.stonebrick, 1, 0) });

        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 8),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.double_stone_slab, 1, 0) });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 0),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.double_stone_slab, 1, 8) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.cobblestone, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 3) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.brick_block, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 4) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stonebrick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 5) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.nether_brick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 6) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.quartz_block, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 7) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.double_stone_slab, 1, 8),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.stone_slab, 1, 8) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 1),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 1) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 2),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 2) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 3),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 3) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 4),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 4) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 5),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 5) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 6),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 6) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.planks, 1, 7),
            bits_no_remove_buffered,
            new Object[] { "B", "B", 'B', new ItemStack(Blocks.wooden_slab, 1, 7) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', new ItemStack(Blocks.deadbush, 1, 32767) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 2, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', new ItemStack(Blocks.tallgrass, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.stick, 1, 0),
            bits_no_remove_buffered,
            new Object[] { "s", "X", 'X', OrePrefixes.treeSapling });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.comparator, 1, 0),
            bits_no_remove_buffered,
            new Object[] { " T ", "TQT", "SSS", 'Q', OreDictNames.craftingQuartz, 'S', OrePrefixes.stoneSmooth, 'T',
                OreDictNames.craftingRedstoneTorch });

        GTLog.out.println("GTMod: Adding Tool Recipes.");
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.minecart, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { " h ", "PwP", "WPW", 'P', "plateAnyIron", 'W', ItemList.Component_Minecart_Wheels_Iron });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.minecart, 1),
            bits_no_remove_buffered,
            new Object[] { " h ", "PwP", "WPW", 'P', "plateSteel", 'W', ItemList.Component_Minecart_Wheels_Steel });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.chest_minecart, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.furnace_minecart, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', OreDictNames.craftingFurnace });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.hopper_minecart, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X',
                new ItemStack(Blocks.hopper, 1, 32767) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.tnt_minecart, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "X", "C", 'C', new ItemStack(Items.minecart, 1), 'X', new ItemStack(Blocks.tnt, 1, 32767) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_helmet, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RRR", "RhR", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_chestplate, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RhR", "RRR", "RRR", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_leggings, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "RRR", "RhR", "R R", 'R', OrePrefixes.ring.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.chainmail_boots, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "R R", "RhR", 'R', OrePrefixes.ring.get(Materials.Steel) });

        GTModHandler.addCraftingRecipe(
            getModItem(IndustrialCraft2.ID, "itemArmorBronzeBoots", 1, 0),
            bits,
            new Object[] { "R R", "RhR", 'R',
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.plate, 1) });
        GTModHandler.addCraftingRecipe(
            getModItem(IndustrialCraft2.ID, "itemArmorBronzeChestplate", 1, 0),
            bits,
            new Object[] { "RhR", "RRR", "RRR", 'R',
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.plate, 1) });
        GTModHandler.addCraftingRecipe(
            getModItem(IndustrialCraft2.ID, "itemArmorBronzeLegs", 1, 0),
            bits,
            new Object[] { "RRR", "RhR", "R R", 'R',
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.plate, 1) });
        GTModHandler.addCraftingRecipe(
            getModItem(IndustrialCraft2.ID, "itemArmorBronzeHelmet", 1, 0),
            bits,
            new Object[] { "RRR", "RhR", 'R',
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.plate, 1) });

        GTLog.out.println("GTMod: Adding Wool and Color releated Recipes.");
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 1),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeOrange });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 2),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeMagenta });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 3),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLightBlue });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 4),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeYellow });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 5),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLime });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 6),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyePink });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 7),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeGray });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 8),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeLightGray });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 9),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeCyan });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 10),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyePurple });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 11),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBlue });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 12),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBrown });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 13),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeGreen });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 14),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeRed });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Blocks.wool, 1, 15),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.wool, 1, 0), Dyes.dyeBlack });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.stained_glass, 8, 0),
            bits_no_remove_buffered,
            new Object[] { "GGG", "GDG", "GGG", 'G', new ItemStack(Blocks.glass, 1), 'D', Dyes.dyeWhite });

        GTLog.out.println("GTMod: Putting a Potato on a Stick.");
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_PotatoChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foilAluminium", ItemList.Food_PotatoChips });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_ChiliChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foilAluminium", ItemList.Food_ChiliChips });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Packaged_Fries.get(1L),
            bits_no_remove_buffered,
            new Object[] { "plateDoublePaper", ItemList.Food_Fries });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Chum_On_Stick.get(1L),
            bits_no_remove_buffered,
            new Object[] { "stickWood", ItemList.Food_Chum });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Potato_On_Stick.get(1L),
            bits_no_remove_buffered,
            new Object[] { "stickWood", ItemList.Food_Raw_Potato });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Potato_On_Stick_Roasted.get(1L),
            bits_no_remove_buffered,
            new Object[] { "stickWood", ItemList.Food_Baked_Potato });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough.get(1L),
            bits_no_remove_buffered,
            new Object[] { "bucketWater", "dustWheat" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Sugar.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "dustSugar" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Chocolate.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "dustCocoa" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Dough_Chocolate.get(2L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "dustChocolate" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Flat_Dough.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", ToolDictNames.craftingToolRollingPin });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Bun.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Bread.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "foodDough" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Baguette.get(1L),
            bits_no_remove_buffered,
            new Object[] { "foodDough", "foodDough", "foodDough" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Cake.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Dough_Sugar, ItemList.Food_Dough_Sugar, ItemList.Food_Dough_Sugar,
                ItemList.Food_Dough_Sugar });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_ChiliChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_PotatoChips, "dustChili" });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Buns.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Breads.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Baguettes.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Bun.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Bread.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sliced_Baguette.get(2L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, "dustMeatCooked" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Chum.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Buns, ItemList.Food_Chum });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, "dustMeatCooked" });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Burger_Chum.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bun, ItemList.Food_Sliced_Bun, ItemList.Food_Chum });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, new ItemStack(Items.cooked_porkchop, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Breads, new ItemStack(Items.cooked_beef, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread,
                new ItemStack(Items.cooked_porkchop, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Bread, ItemList.Food_Sliced_Bread,
                new ItemStack(Items.cooked_beef, 1) });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, new ItemStack(Items.cooked_porkchop, 1),
                new ItemStack(Items.cooked_porkchop, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguettes, new ItemStack(Items.cooked_beef, 1),
                new ItemStack(Items.cooked_beef, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Cucumber,
                ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Tomato, ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Bacon.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette,
                new ItemStack(Items.cooked_porkchop, 1), new ItemStack(Items.cooked_porkchop, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Large_Sandwich_Steak.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Sliced_Baguette, ItemList.Food_Sliced_Baguette,
                new ItemStack(Items.cooked_beef, 1), new ItemStack(Items.cooked_beef, 1) });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Veggie.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, ItemList.Food_Sliced_Cucumber, ItemList.Food_Sliced_Tomato,
                ItemList.Food_Sliced_Onion });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Cheese.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, ItemList.Food_Sliced_Cheese, ItemList.Food_Sliced_Cheese,
                ItemList.Food_Sliced_Cheese });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Food_Raw_Pizza_Meat.get(1L),
            bits_no_remove_buffered,
            new Object[] { ItemList.Food_Flat_Dough, "dustMeatCooked" });

        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Cheese.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "foodCheese" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Lemon.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropLemon" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Tomato.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropTomato" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Onion.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropOnion" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Cucumber.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropCucumber" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Bun.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Bun });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Bread.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Bread });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Sliced_Baguette.get(2L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Baked_Baguette });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Raw_PotatoChips.get(1L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', "cropPotato" });
        GTModHandler.addCraftingRecipe(
            ItemList.Food_Raw_Cookie.get(4L),
            bits_no_remove_buffered,
            new Object[] { "kX", 'X', ItemList.Food_Dough_Chocolate });

        GTModHandler.addCraftingRecipe(
            ItemList.Food_Raw_Fries.get(1L),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', "cropPotato" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.bowl, 1),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', "plankWood" });
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Rubber, Materials2Shapes.ring, 1),
            bits_no_remove_buffered,
            new Object[] { "k", "X", 'X', "plateRubber" });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.arrow, 1),
            bits_no_remove_buffered | GTModHandler.RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES,
            new Object[] { "  H", " S ", "F  ", 'H', new ItemStack(Items.flint, 1, 32767), 'S', "stickWood", 'F',
                OreDictNames.craftingFeather });

        GTModHandler.removeRecipe(
            new ItemStack(Blocks.planks),
            null,
            new ItemStack(Blocks.planks),
            null,
            new ItemStack(Blocks.planks));
        GTModHandler.removeRecipeByOutputDelayed(ItemList.Food_Baked_Bread.get(1L));
        GTModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.cookie, 1));
        GTModHandler.removeRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, 1),
            MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, 1),
            MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, 1));
        if (null != GTUtility.setStack(
            GTModHandler.getRecipeOutput(
                true,
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, 1),
                null,
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, 1)),
            MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.ingot, 1))) {
            GTLog.out.println("GTMod: Changed Forestrys Bronze Recipe");
        }
        tStack = MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, 1);

        GTModHandler.addCraftingRecipe(
            GTModHandler.getRecipeOutput(
                null,
                new ItemStack(Blocks.sand, 1, 0),
                null,
                null,
                MaterialLibAPI.getStack(Materials2Materials.Apatite, Materials2Shapes.gem, 1),
                null,
                null,
                new ItemStack(Blocks.sand, 1, 0),
                null),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "S", "A", "S", 'A', "dustApatite", 'S', new ItemStack(Blocks.sand, 1, 32767) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getRecipeOutput(
                tStack,
                tStack,
                tStack,
                tStack,
                MaterialLibAPI.getStack(Materials2Materials.Apatite, Materials2Shapes.gem, 1),
                tStack,
                tStack,
                tStack,
                tStack),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SSS", "SAS", "SSS", 'A', "dustApatite", 'S', "dustAsh" });

        GTLog.out.println("GTMod: Adding Mixed Metal Ingot Recipes.");
        GTModHandler.removeRecipeByOutputDelayed(ItemList.IC2_Mixed_Metal_Ingot.get(1L));

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateAnyIron", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(1L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNickel", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateInvar", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(2L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateSteel", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateStainlessSteel", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTitanium", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(3L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(4L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungsten", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBronze", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBronze", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(6L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBronze", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBrass", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(5L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBrass", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(6L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateBrass", 'Z', "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateChrome", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateChrome", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(8L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateChrome", 'Z', "plateAluminium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateStainlessSteel", 'Z', "plateTin" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateStainlessSteel", 'Z', "plateZinc" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(10L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateTungstenSteel", 'Y', "plateStainlessSteel", 'Z',
                "plateAluminium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateChrome", 'Z', "plateAnnealedCopper" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateChrome", 'Z', "plateRoseGold" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(12L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateChrome", 'Z', "plateAstralSilver" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateStainlessSteel", 'Z',
                "plateAnnealedCopper" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateStainlessSteel", 'Z', "plateRoseGold" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(14L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateIridium", 'Y', "plateStainlessSteel", 'Z', "plateAstralSilver" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSG", 'Y', "plateStainlessSteel", 'Z', "plateAnnealedCopper" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSG", 'Y', "plateStainlessSteel", 'Z', "plateRoseGold" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(16L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSG", 'Y', "plateStainlessSteel", 'Z', "plateAstralSilver" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSE", 'Y', "plateChrome", 'Z', "plateAnnealedCopper" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSE", 'Y', "plateChrome", 'Z', "plateRoseGold" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(18L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSE", 'Y', "plateChrome", 'Z', "plateAstralSilver" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSS", 'Y', "plateTungstenSteel", 'Z', "plateAnnealedCopper" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSS", 'Y', "plateTungstenSteel", 'Z', "plateRoseGold" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(20L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateHSSS", 'Y', "plateTungstenSteel", 'Z', "plateAstralSilver" });

        GTModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Fluid.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GPG", "IwI", "GPG", 'G', GTOreDictUnificator.get("gearSteel", 1L), 'P',
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, 1), 'I',
                GTOreDictUnificator.get("pipeHugeSteel", 1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Item.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GPG", "IwI", "GPG", 'G', GTOreDictUnificator.get("gearSteel", 1L), 'P',
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, 1), 'I',
                GTOreDictUnificator.get("pipeHugeTin", 1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(32L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "IwI", "PPP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, 1), 'I',
                GTOreDictUnificator.get("pipeLargeSteel", 1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Long_Distance_Pipeline_Item_Pipe.get(32L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "IwI", "PPP", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, 1), 'I',
                GTOreDictUnificator.get("pipeLargeTin", 1L) });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(22L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadah", 'Y', "plateIridium", 'Z', "plateHSSG" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(24L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadah", 'Y', "plateIridium", 'Z', "plateHSSE" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(26L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadah", 'Y', "plateIridium", 'Z', "plateHSSS" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(28L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadahAlloy", 'Y', "plateOsmiridium", 'Z', "plateHSSE" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(30L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadahAlloy", 'Y', "plateOsmiridium", 'Z', "plateHSSG" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(32L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNaquadahAlloy", 'Y', "plateOsmiridium", 'Z', "plateHSSS" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(34L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNeutronium", 'Y', "plateEnergeticAlloy", 'Z', "plateNaquadah" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(36L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNeutronium", 'Y', "plateEnergeticAlloy", 'Z',
                "plateNaquadahAlloy" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(38L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateNeutronium", 'Y', "plateEnergeticAlloy", 'Z', "plateDraconium" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(40L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateBlackPlutonium", 'Y', "plateSunnarium", 'Z', "plateNaquadah" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(42L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateBlackPlutonium", 'Y', "plateSunnarium", 'Z',
                "plateNaquadahAlloy" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(44L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateBlackPlutonium", 'Y', "plateSunnarium", 'Z', "plateDraconium" });

        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(48L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateDraconiumAwakened", 'Y', "plateNeutronium", 'Z', "plateHSSS" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(52L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateDraconiumAwakened", 'Y', "plateNeutronium", 'Z',
                "plateNaquadah" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(56L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateDraconiumAwakened", 'Y', "plateNeutronium", 'Z',
                "plateNaquadahAlloy" });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_Mixed_Metal_Ingot.get(64L),
            bits_no_remove_buffered,
            new Object[] { "X", "Y", "Z", 'X', "plateDraconiumAwakened", 'Y', "plateNeutronium", 'Z',
                "plateBlackPlutonium" });

        GTLog.out.println("GTMod: Beginning to add regular Crafting Recipes.");
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.stickLong, 2),
            bits_no_remove_buffered,
            new Object[] { "sLf", 'L', "logWood" });

        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("scaffold", 4L),
            bits_no_remove_buffered,
            new Object[] { "WWW", " S ", "S S", 'W', "plankWood", 'S', "stickWood" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.IronMagnetic, Materials2Shapes.stick, 1),
            bits_no_remove_buffered,
            new Object[] { "stickAnyIron", "dustRedstone", "dustRedstone", "dustRedstone", "dustRedstone" });
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.ring, 1),
            bits_no_remove_buffered,
            new Object[] { "PPk", 'P', "platePaper" });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.torch, 2),
            bits_no_remove_buffered,
            new Object[] { "C", "S", 'C', "dustSulfur", 'S', "stickWood" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.torch, 6),
            bits_no_remove_buffered,
            new Object[] { "C", "S", 'C', "dustTricalciumPhosphate", 'S', "stickWood" });

        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', "plankWood", 'C', OrePrefixes.stoneCobble, 'R', "dustRedstone",
                'B', "ingotAnyIron" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', "plankWood", 'C', OrePrefixes.stoneCobble, 'R', "dustRedstone",
                'B', "ingotAnyBronze" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', "plankWood", 'C', OrePrefixes.stoneCobble, 'R', "dustRedstone",
                'B', "ingotAluminium" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', "plankWood", 'C', OrePrefixes.stoneCobble, 'R', "dustRedstone",
                'B', "ingotSteel" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(Blocks.piston, 1),
            bits_no_remove_buffered,
            new Object[] { "WWW", "CBC", "CRC", 'W', "plankWood", 'C', OrePrefixes.stoneCobble, 'R', "dustRedstone",
                'B', "ingotTitanium" });

        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("reactorVent", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "AIA", "I I", "AIA", 'I', new ItemStack(Blocks.iron_bars, 1), 'A', "plateAluminium" });
        GTModHandler.addShapelessCraftingRecipe(
            GTModHandler.getIC2Item("reactorPlatingExplosive", 1L),
            bits_no_remove_buffered,
            new Object[] { GTModHandler.getIC2Item("reactorPlating", 1L), "plateLead" });
        if (!Materials.Steel.mBlastFurnaceRequired) {
            GTModHandler.addShapelessCraftingRecipe(
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 1),
                bits_no_remove_buffered,
                new Object[] { "dustIron", "dustCoal", "dustCoal" });
        }

        GTModHandler
            .removeRecipeByOutputDelayed(MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 1));
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 3),
            bits_no_remove_buffered,
            new Object[] { "dustAnyCopper", "dustAnyCopper", "dustAnyCopper", "dustZinc" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dustSmall, 9),
            bits_no_remove_buffered,
            new Object[] { "dustTetrahedrite", "dustTetrahedrite", "dustTetrahedrite", "dustZinc" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.dust, 3),
            bits_no_remove_buffered,
            new Object[] { "dustAnyCopper", "dustAnyCopper", "dustAnyCopper", "dustTin" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.dustSmall, 9),
            bits_no_remove_buffered,
            new Object[] { "dustTetrahedrite", "dustTetrahedrite", "dustTetrahedrite", "dustTin" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.dustSmall, 9),
            bits_no_remove_buffered,
            new Object[] { "dustIron", "dustIron", "dustNickel" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.dustSmall, 6),
            bits_no_remove_buffered,
            new Object[] { "dustNickel", "dustAnyCopper" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.RoseGold, Materials2Shapes.dust, 4),
            bits_no_remove_buffered,
            new Object[] { "dustGold", "dustGold", "dustGold", "dustGold", "dustAnyCopper" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.SterlingSilver, Materials2Shapes.dust, 4),
            bits_no_remove_buffered,
            new Object[] { "dustSilver", "dustSilver", "dustSilver", "dustSilver", "dustAnyCopper" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.BlackBronze, Materials2Shapes.dust, 4),
            bits_no_remove_buffered,
            new Object[] { "dustGold", "dustSilver", "dustAnyCopper", "dustAnyCopper", "dustAnyCopper" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.BismuthBronze, Materials2Shapes.dust, 4),
            bits_no_remove_buffered,
            new Object[] { "dustBismuth", "dustZinc", "dustAnyCopper", "dustAnyCopper", "dustAnyCopper" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.CobaltBrass, Materials2Shapes.dust, 8),
            bits_no_remove_buffered,
            new Object[] { "dustBrass", "dustBrass", "dustBrass", "dustBrass", "dustBrass", "dustBrass", "dustBrass",
                "dustTin", "dustCobalt" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.DamascusSteel, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustSteel", "dustSmallNickel", "dustSmallNickel", "dustSmallNickel", "dustTinyCoal",
                "dustTinySilicon", "dustTinyManganese", "dustTinyChrome", "dustTinyMolybdenum" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.DamascusSteel, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustSteel", "dustSmallManganese", "dustSmallManganese", "dustSmallChrome",
                "dustSmallChrome", "dustTinyCoal", "dustTinySilicon", "dustTinyVanadium" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.RedstoneAlloy, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustRedstone", "dustSilicon", "dustCoal" });
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get("dustClayCompound", 2L),
            bits_no_remove_buffered,
            new Object[] { "dustClay", "dustFlint", "dustStone" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.ConductiveIron, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustRedstoneAlloy", "dustIron", "dustSilver" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.EnergeticAlloy, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustConductiveIron", "dustGold", "dustBlackSteel" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.EnergeticSilver, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustConductiveIron", "dustSilver", "dustBlackSteel" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.ElectricalSteel, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustSteel", "dustCoal", "dustSilicon" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Soularium, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { new ItemStack(Blocks.soul_sand, 1, 32767), "dustGold", "dustAsh" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.DarkSteel, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustElectricalSteel", "dustCoal", "dustObsidian" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Manyullyn, Materials2Shapes.dust, 3),
            bits_no_remove_buffered,
            new Object[] { "dustArdite", "dustArdite", "dustArdite", "dustArdite", "dustCobalt", "dustCobalt",
                "dustCobalt", "dustCobalt" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.IronWood, Materials2Shapes.dust, 2),
            bits_no_remove_buffered,
            new Object[] { "dustIron", "dustLiveRoot", "dustTinyGold" });

        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { "dustCoal", "dustCoal", "dustCoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter" });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { "dustCharcoal", "dustCharcoal", "dustCharcoal", "dustSulfur", "dustSaltpeter",
                "dustSaltpeter" });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.gunpowder, 6),
            bits_no_remove_buffered,
            new Object[] { "dustCarbon", "dustCarbon", "dustCarbon", "dustSulfur", "dustSaltpeter", "dustSaltpeter" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.IndiumGalliumPhosphide, Materials2Shapes.dust, 3),
            bits_no_remove_buffered,
            new Object[] { "dustIndium", "dustGallium", "dustPhosphorus" });

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.dust, 5),
            bits_no_remove_buffered,
            new Object[] { "dustPotassium", "cellNitrogen", "cellOxygen", "cellOxygen", "cellOxygen" });
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("carbonFiber", 1L));

        ItemStack[] tChestAndTank = new ItemStack[] { ItemList.Super_Chest_EV.get(1), ItemList.Super_Chest_IV.get(1),
            ItemList.Super_Chest_HV.get(1), ItemList.Super_Chest_MV.get(1), ItemList.Super_Chest_LV.get(1),
            ItemList.Quantum_Chest_EV.get(1), ItemList.Quantum_Chest_IV.get(1), ItemList.Quantum_Chest_HV.get(1),
            ItemList.Quantum_Chest_MV.get(1), ItemList.Quantum_Chest_LV.get(1), ItemList.Super_Tank_EV.get(1),
            ItemList.Super_Tank_IV.get(1), ItemList.Super_Tank_HV.get(1), ItemList.Super_Tank_MV.get(1),
            ItemList.Super_Tank_LV.get(1), ItemList.Quantum_Tank_EV.get(1), ItemList.Quantum_Tank_IV.get(1),
            ItemList.Quantum_Tank_HV.get(1), ItemList.Quantum_Tank_MV.get(1), ItemList.Quantum_Tank_LV.get(1) };
        for (ItemStack tItem : tChestAndTank) {
            GTModHandler.addShapelessCraftingRecipe(tItem, new Object[] { tItem });
        }

        List<ItemStack> iToRemoveAndHide = Arrays
            .stream(
                new String[] { "copperCableItem", "insulatedCopperCableItem", "goldCableItem", "insulatedGoldCableItem",
                    "insulatedIronCableItem", "glassFiberCableItem", "tinCableItem", "ironCableItem",
                    "insulatedTinCableItem", "detectorCableItem", "splitterCableItem", "electrolyzer", "cutter" })
            .map(x -> GTModHandler.getIC2Item(x, 1L))
            .collect(Collectors.toList());

        if (NotEnoughItems.isModLoaded()) {
            iToRemoveAndHide.forEach(item -> {
                codechicken.nei.api.API.hideItem(item);
                GTModHandler.removeRecipeByOutputDelayed(item);
            });
        }

        Arrays
            .stream(
                new String[] { "batBox", "mfeUnit", "lvTransformer", "mvTransformer", "hvTransformer", "evTransformer",
                    "cesuUnit", "luminator", "teleporter", "energyOMat", "advBattery", "boatElectric", "cropnalyzer",
                    "coil", "powerunit", "powerunitsmall", "remote", "odScanner", "ovScanner", "solarHelmet",
                    "staticBoots", "ecMeter", "obscurator", "overclockerUpgrade", "transformerUpgrade",
                    "energyStorageUpgrade", "ejectorUpgrade", "suBattery", "frequencyTransmitter", "pullingUpgrade" })
            .map(x -> GTModHandler.getIC2Item(x, 1L))
            .forEach(GTModHandler::removeRecipeByOutputDelayed);

        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("batBox", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "BBB", "PPP", 'C', "cableGt01Tin", 'P', "plankWood", 'B', "batteryBasic" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("mfeUnit", 1L),
            bits_no_remove_buffered,
            new Object[] { "CEC", "EME", "CEC", 'C', "cableGt01Gold", 'E', "batteryElite", 'M',
                GTModHandler.getIC2Item("machine", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("lvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "POP", "PCP", 'C', "cableGt01Tin", 'O', GTModHandler.getIC2Item("coil", 1L), 'P',
                "plankWood" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("mvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { "CMC", 'C', "cableGt01Copper", 'M', GTModHandler.getIC2Item("machine", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("hvTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "IMB", " C ", 'C', "cableGt01Gold", 'M', GTModHandler.getIC2Item("mvTransformer", 1L),
                'I', "circuitBasic", 'B', "batteryAdvanced" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("evTransformer", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "IMB", " C ", 'C', "cableGt01Aluminium", 'M',
                GTModHandler.getIC2Item("hvTransformer", 1L), 'I', "circuitAdvanced", 'B', "batteryMaster" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("cesuUnit", 1L),
            bits_no_remove_buffered,
            new Object[] { "PCP", "BBB", "PPP", 'C', "cableGt01Copper", 'P', "plateBronze", 'B', "batteryAdvanced" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("teleporter", 1L),
            bits_no_remove_buffered,
            new Object[] { "GFG", "CMC", "GDG", 'C', "cableGt01Platinum", 'G', "circuitAdvanced", 'D', "gemDiamond",
                'M', GTModHandler.getIC2Item("machine", 1L), 'F',
                GTModHandler.getIC2Item("frequencyTransmitter", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("energyOMat", 1L),
            bits_no_remove_buffered,
            new Object[] { "RBR", "CMC", 'C', "cableGt01Copper", 'R', "dustRedstone", 'B', "batteryBasic", 'M',
                GTModHandler.getIC2Item("machine", 1L) });

        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("boatElectric", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "XWX", aTextIron2, 'C', "cableGt01Copper", 'X', "plateIron", 'W',
                GTModHandler.getIC2Item("waterMill", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("coil", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "CXC", "CCC", 'C', "wireGt01Copper", 'X', "ingotAnyIron" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("powerunit", 1L),
            bits_no_remove_buffered,
            new Object[] { "BCA", "BIM", "BCA", 'C', "cableGt01Copper", 'B', "batteryBasic", 'A',
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.itemCasing, 1), 'I', "circuitBasic",
                'M', GTModHandler.getIC2Item("elemotor", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("powerunitsmall", 1L),
            bits_no_remove_buffered,
            new Object[] { " CA", "BIM", " CA", 'C', "cableGt01Copper", 'B', "batteryBasic", 'A',
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.itemCasing, 1), 'I', "circuitBasic",
                'M', GTModHandler.getIC2Item("elemotor", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("remote", 1L),
            bits_no_remove_buffered,
            new Object[] { " C ", "TLT", " F ", 'C', "cableGt01Copper", 'L', "dustLapis", 'T',
                GTModHandler.getIC2Item("casingtin", 1L), 'F', GTModHandler.getIC2Item("frequencyTransmitter", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("odScanner", 1L),
            bits_no_remove_buffered,
            new Object[] { "PGP", "CBC", "WWW", 'W', "cableGt01Copper", 'G', "dustGlowstone", 'B', "batteryAdvanced",
                'C', "circuitAdvanced", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.itemCasing, 1) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("ovScanner", 1L),
            bits_no_remove_buffered,
            new Object[] { "PDP", "GCG", "WSW", 'W', "cableGt01Gold", 'G', "dustGlowstone", 'D', "batteryElite", 'C',
                "circuitAdvanced", 'P',
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.itemCasing, 1), 'S',
                GTModHandler.getIC2Item("odScanner", 1L) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("staticBoots", 1L),
            bits_no_remove_buffered,
            new Object[] { "I I", "IWI", "CCC", 'C', "cableGt01Copper", 'I', "ingotIron", 'W',
                new ItemStack(Blocks.wool) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("ecMeter", 1L),
            bits_no_remove_buffered,
            new Object[] { " G ", "CIC", "C C", 'C', "cableGt01Copper", 'G', "dustGlowstone", 'I', "circuitBasic" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("obscurator", 1L),
            bits_no_remove_buffered,
            new Object[] { "RER", "CAC", "RRR", 'C', "cableGt01Gold", 'R', "dustRedstone", 'E', "batteryAdvanced", 'A',
                "circuitAdvanced" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("overclockerUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "CCC", "WEW", 'W', "cableGt01Copper", 'C',
                GTModHandler.getIC2Item("reactorCoolantSimple", 1L, 1), 'E', "circuitBasic" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("transformerUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "GGG", "WTW", "GEG", 'W', "cableGt01Gold", 'T', GTModHandler.getIC2Item("mvTransformer", 1L),
                'E', "circuitBasic", 'G', "blockGlass" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("energyStorageUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PPP", "WBW", "PEP", 'W', "cableGt01Copper", 'E', "circuitBasic", 'P', "plankWood", 'B',
                "batteryBasic" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("ejectorUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PHP", "WEW", 'W', "cableGt01Copper", 'E', "circuitBasic", 'P', new ItemStack(Blocks.piston),
                'H', new ItemStack(Blocks.hopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.IC2_SuBattery.get(1L),
            bits_no_remove_buffered,
            new Object[] { "W", "C", "R", 'W', "cableGt01Copper", 'C', "dustHydratedCoal", 'R', "dustRedstone" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("pullingUpgrade", 1L),
            bits_no_remove_buffered,
            new Object[] { "PHP", "WEW", 'W', "cableGt01Copper", 'P', new ItemStack(Blocks.sticky_piston), 'R',
                new ItemStack(Blocks.hopper), 'E', "circuitBasic" });

        if (NotEnoughItems.isModLoaded()) {
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorUraniumSimple", 1L, 1));
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorUraniumDual", 1L, 1));
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorUraniumQuad", 1L, 1));
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorMOXSimple", 1L, 1));
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorMOXDual", 1L, 1));
            codechicken.nei.api.API.hideItem(GTModHandler.getIC2Item("reactorMOXQuad", 1L, 1));
        }

        GTModHandler.removeRecipeByOutputDelayed(Ic2Items.miningLaser.copy());
        GTModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', "plateTitanium", 'G', "gemExquisiteDiamond", 'E',
                ItemList.Emitter_HV, 'C', "circuitElite", 'S', "screwTitanium", 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    WILDCARD) });
        GTModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', "plateTitanium", 'G', "gemExquisiteRuby", 'E', ItemList.Emitter_HV,
                'C', "circuitElite", 'S', "screwTitanium", 'B', new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    WILDCARD) });
        GTModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', "plateTitanium", 'G', "gemExquisiteJasper", 'E',
                ItemList.Emitter_HV, 'C', "circuitElite", 'S', "screwTitanium", 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    WILDCARD) });
        GTModHandler.addCraftingRecipe(
            Ic2Items.miningLaser.copy(),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "GEC", "SBd", 'P', "plateTitanium", 'G', "gemExquisiteGarnetRed", 'E',
                ItemList.Emitter_HV, 'C', "circuitElite", 'S', "screwTitanium", 'B',
                new ItemStack(
                    Ic2Items.chargingEnergyCrystal.copy()
                        .getItem(),
                    1,
                    WILDCARD) });

        GTModHandler.removeRecipeDelayed(GTModHandler.getIC2Item("miningPipe", 8));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("miningPipe", 1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", 'P', "pipeSmallSteel" });

        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("luminator", 16L),
            bits_no_remove_buffered,
            new Object[] { "RTR", "GHG", "GGG", 'H', "cellHelium", 'T', "ingotTin", 'R', "ingotAnyIron", 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("luminator", 16L),
            bits_no_remove_buffered,
            new Object[] { "RTR", "GHG", "GGG", 'H', "cellMercury", 'T', "ingotTin", 'R', "ingotAnyIron", 'G',
                new ItemStack(Blocks.glass, 1) });

        GTModHandler.removeRecipeDelayed(
            tStack = MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
            tStack,
            tStack,
            tStack,
            new ItemStack(Items.coal, 1, 0),
            tStack,
            tStack,
            tStack,
            tStack);
        GTModHandler.removeRecipeDelayed(
            tStack = MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
            tStack,
            tStack,
            tStack,
            new ItemStack(Items.coal, 1, 1),
            tStack,
            tStack,
            tStack,
            tStack);
        GTModHandler.removeRecipeDelayed(
            null,
            tStack = new ItemStack(Items.coal, 1),
            null,
            tStack,
            GTOreDictUnificator.get("ingotIron", 1L),
            tStack,
            null,
            tStack,
            null);

        GTModHandler.removeFurnaceSmelting(new ItemStack(Blocks.hopper));

        GTLog.out.println("GTMod: Applying harder Recipes for several Blocks."); // TODO: Not Buffered

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("reactorReflectorThick", 1L, 1));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("reactorReflectorThick", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { " N ", "NBN", " N ", 'B', "plateDoubleBeryllium", 'N',
                GTModHandler.getIC2Item("reactorReflector", 1L, 1) });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("reactorReflectorThick", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { " B ", "NCN", " B ", 'B', "plateBeryllium", 'N',
                GTModHandler.getIC2Item("reactorReflector", 1L, 1), 'C', "plateTungstenCarbide" });
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("reactorReflector", 1L, 1));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("reactorReflector", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "TGT", "GSG", "TGT", 'T', "plateTin", 'G', "dustGraphite", 'S', "plateDoubleSteel" });
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("reactorReflector", 1L, 1),
            bits_no_remove_buffered,
            new Object[] { "TTT", "GSG", "TTT", 'T', "plateTinAlloy", 'G', "dustGraphite", 'S', "plateBeryllium" });

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("crophavester", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("RTGenerator", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("RTGenerator", 1L),
            bits_no_remove_buffered,
            new Object[] { "III", "IMI", "ICI", 'I', "itemCasingSteel", 'C', "circuitMaster", 'M', ItemList.Hull_IV });

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("RTHeatGenerator", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("RTHeatGenerator", 1L),
            bits_no_remove_buffered,
            new Object[] { "III", "IMB", "ICI", 'I', "itemCasingSteel", 'C', "circuitMaster", 'M', ItemList.Hull_IV,
                'B', GTOreDictUnificator.get("blockCopper", 1) });

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("carbonrotor", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("carbonrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GTModHandler.getIC2Item("carbonrotorblade", 1), 'S',
                "screwIridium", 'T', ItemList.IC2_ShaftSteel.get(1L) });
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("steelrotor", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("steelrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GTModHandler.getIC2Item("steelrotorblade", 1), 'S',
                "screwStainlessSteel", 'T', ItemList.IC2_ShaftIron.get(1L) });
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("ironrotor", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("ironrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GTModHandler.getIC2Item("ironrotorblade", 1), 'S', "screwCastIron",
                'T', ItemList.IC2_ShaftIron.get(1) });
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("woodrotor", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("woodrotor", 1L),
            bits_no_remove_buffered,
            new Object[] { "dBS", "BTB", "SBw", 'B', GTModHandler.getIC2Item("woodrotorblade", 1), 'S', "screwCastIron",
                'T', "stickLongCastIron" });

        if (GTOreDictUnificator.get("gearDiamond", 1L) != null) {
            tStack = GTModHandler.getRecipeOutput(
                GTOreDictUnificator.get("gearIron", 1L),
                new ItemStack(Items.redstone, 1),
                GTOreDictUnificator.get("gearIron", 1L),
                GTOreDictUnificator.get("gearGold", 1L),
                GTOreDictUnificator.get("gearIron", 1L),
                GTOreDictUnificator.get("gearGold", 1L),
                GTOreDictUnificator.get("gearDiamond", 1L),
                new ItemStack(Items.diamond_pickaxe, 1),
                GTOreDictUnificator.get("gearDiamond", 1L));
            GTModHandler.removeRecipeByOutputDelayed(tStack);
            GTModHandler.addCraftingRecipe(
                tStack,
                bits_no_remove_buffered,
                new Object[] { "ICI", "GIG", "DPD", 'C', "circuitAdvanced", 'D', "gearDiamond", 'G', "gearGold", 'I',
                    "gearSteel", 'P', GTModHandler.getIC2Item("diamondDrill", 1L, 32767) });
        }

        GTModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.paper));
        GTModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.sugar));
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.dust, 2),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SSS", " m ", 'S', new ItemStack(Items.reeds) });
        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get("dustSugar", 1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "Sm ", 'S', new ItemStack(Items.reeds) });
        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get("paperEmpty", 2),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { " C ", "SSS", " C ", 'S',
                MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.dust, 1), 'C',
                new ItemStack(Blocks.stone_slab) });

        GTLog.out.println("GTMod: Applying Recipes for Tools");
        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("nanoSaber", 1L));
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("nanoSaber", 1L),
            bits_no_remove_buffered,
            new Object[] { "PI ", "PI ", "CLC", 'L', "batteryData", 'I', OrePrefixes.plateAlloy.get("Iridium"), 'P',
                "platePlatinum", 'C', "circuitElite" });

        GTModHandler.removeRecipeByOutputDelayed(new ItemStack(Items.flint_and_steel, 1));
        GTModHandler.addCraftingRecipe(
            new ItemStack(Items.flint_and_steel, 1),
            bits_no_remove_buffered,
            new Object[] { "S ", " F", 'F', new ItemStack(Items.flint, 1), 'S', "nuggetSteel" });

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("diamondDrill", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("miningDrill", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("chainsaw", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("electricHoe", 1L));

        GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getIC2Item("electricTreetap", 1L));

        if (GraviSuite.isModLoaded()) {
            GTModHandler
                .removeRecipeByOutputDelayed(GTModHandler.getModItem(GraviSuite.ID, "advNanoChestPlate", 1, WILDCARD));
            GTModHandler.addCraftingRecipe(
                GTModHandler.getModItem(GraviSuite.ID, "advNanoChestPlate", 1, WILDCARD),
                bits_no_remove_buffered,
                new Object[] { "CJC", "TNT", "WPW", 'C', "plateAlloyAdvanced", 'T', "plateTungstenSteel", 'J',
                    GTModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, WILDCARD), 'N',
                    GTModHandler.getModItem(IndustrialCraft2.ID, "itemArmorNanoChestplate", 1, WILDCARD), 'W',
                    "wireGt12Platinum", 'P', "circuitElite" });

            GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(GraviSuite.ID, "advLappack", 1, WILDCARD));
            GTModHandler.addCraftingRecipe(
                GTModHandler.getModItem(GraviSuite.ID, "advLappack", 1, WILDCARD),
                bits_no_remove_buffered,
                new Object[] { "CEC", "EJE", "WPW", 'C', "plateAlloyCarbon", 'J',
                    GTModHandler.getModItem(IndustrialCraft2.ID, "itemArmorEnergypack", 1L, WILDCARD), 'E',
                    GTModHandler.getModItem(IndustrialCraft2.ID, "itemBatCrystal", 1L, WILDCARD), 'W',
                    "wireGt04Platinum", 'P', "circuitData" });

            GTModHandler.removeRecipeByOutputDelayed(GTModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, WILDCARD));
            GTModHandler.addCraftingRecipe(
                GTModHandler.getModItem(GraviSuite.ID, "advJetpack", 1, WILDCARD),
                bits_no_remove_buffered,
                new Object[] { "CJC", "EXE", "YZY", 'C', "plateAlloyCarbon", 'J',
                    GTModHandler.getModItem(IndustrialCraft2.ID, "itemArmorJetpackElectric", 1, WILDCARD), 'E',
                    "plateTitanium", 'X', GTModHandler.getModItem(IndustrialCraft2.ID, "itemArmorAlloyChestplate", 1L),
                    'Z', "circuitData", 'Y', "wireGt02Platinum" });
        }

        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(Materials2Materials.Fireclay, Materials2Shapes.dust, 2),
            new Object[] { MaterialLibAPI.getStack(Materials2Materials.Brick, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.dust, 1) });

        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Advanced_Rhodium_Palladium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P',
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.plate), 1, 88), 'F',
                OrePrefixes.frameGt.get(Materials.Chrome) });

        if (Forestry.isModLoaded()) {

            for (int i = 0; i < ItemList.FORESTRY_DECORATIVE_PLANKS.size(); i++) {
                ItemStack slabWood = getModItem(Forestry.ID, "slabs", 1, i);
                ItemStack slabWoodFireproof = getModItem(Forestry.ID, "slabsFireproof", 1, i);
                final ItemList plank = ItemList.FORESTRY_DECORATIVE_PLANKS.get(i);

                GTModHandler.addCraftingRecipe(
                    plank.get(2),
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
                    new Object[] { "s ", " P", 'P', slabWood });

                GTModHandler.addCraftingRecipe(
                    plank.get(2),
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
                    new Object[] { "s ", " P", 'P', slabWoodFireproof });
            }
        }
        GTModHandler.addCraftingRecipe(
            GTModHandler.getIC2Item("electronicCircuit", 1L),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "RIR", "VBV", "CCC", 'R', ItemList.Circuit_Parts_Resistor.get(1), 'C',
                GTOreDictUnificator.get("cableGt01RedAlloy", 1), 'V', ItemList.Circuit_Parts_Vacuum_Tube.get(1), 'B',
                ItemList.Circuit_Board_Coated_Basic.get(1), 'I',
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.itemCasing, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            GTModHandler.getIC2Item("electronicCircuit", 1L),
            new Object[] { ItemList.Circuit_Integrated.getWildcard(1L) });

        if (Thaumcraft.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                ItemList.MagLevHarness.get(1),
                bits_no_remove_buffered,
                new Object[] { "RAR", "SBS", "RSR", 'R', "stickThaumium", 'A', "plateInfusedAir", 'S',
                    "plateSteelMagnetic", 'B', GTModHandler.getModItem(Thaumcraft.ID, "ItemBaubleBlanks", 1, 2), });
        }

        if (TwilightForest.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                GTModHandler.getModItem(TwilightForest.ID, "tile.TFThorns", 1, 0),
                bits_no_remove_buffered,
                new Object[] { "TWT", "LWL", "TWT", 'T',
                    GTModHandler.getModItem(TwilightForest.ID, "tile.TFThornRose", 1, 0), 'L',
                    ItemList.TF_LiveRoot.get(1), 'W',
                    GTModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 0), });
        }

        if (EtFuturumRequiem.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                ItemList.Plank_Cherry_EFR.get(2L),
                GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "s ", " P", 'P', GTModHandler.getModItem(EtFuturumRequiem.ID, "wood_slab", 1, 3), 's',
                    "craftingToolSaw" });
        }
    }
}
