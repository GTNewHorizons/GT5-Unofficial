package gtPlusPlus.xmod.bop.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.pine.LeavesPineTree;
import gtPlusPlus.xmod.bop.blocks.pine.LogPineTree;
import gtPlusPlus.xmod.bop.blocks.pine.SaplingPineTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LeavesRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LogRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.SaplingRainforestTree;

public class BOPBlockRegistrator {

    public static Block log_Rainforest;
    public static Block leaves_Rainforest;
    public static Block sapling_Rainforest;
    public static Block log_Pine;
    public static Block leaves_Pine;
    public static Block sapling_Pine;

    // Runs Each tree Type separately
    public static void run() {
        registerTree_Rainforest();
        registerTree_Pine();
    }

    private static void registerTree_Rainforest() {
        log_Rainforest = new LogRainforestTree();
        leaves_Rainforest = new LeavesRainforestTree();
        sapling_Rainforest = new SaplingRainforestTree();
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(log_Rainforest), "logWood", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(leaves_Rainforest), "treeLeaves", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(sapling_Rainforest), "treeSapling", true);
    }

    private static void registerTree_Pine() {
        log_Pine = new LogPineTree();
        leaves_Pine = new LeavesPineTree();
        sapling_Pine = new SaplingPineTree();
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(log_Pine), "logWood", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(leaves_Pine), "treeLeaves", true);
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(sapling_Pine), "treeSapling", true);
    }

    public static void recipes() {
        // Rainforest Oak
        addRecipeLogsToPlanks(ItemUtils.getSimpleStack(log_Rainforest));
        // Pine
        addRecipeLogsToPlanks(ItemUtils.getSimpleStack(log_Pine));
    }

    public static void addRecipeLogsToPlanks(ItemStack aStack) {
        // add normal recipes like vanilla, so oredict recipe handlers will add recipes from logs to planks.
        // other recipes are also processed by oredict recipe handlers.
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 4), aStack);
    }

}
