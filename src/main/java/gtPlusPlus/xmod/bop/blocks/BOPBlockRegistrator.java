package gtPlusPlus.xmod.bop.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTRecipeBuilder;
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
        OreDictionary.registerOre("logWood", new ItemStack(log_Rainforest, 1, GTRecipeBuilder.WILDCARD));
        OreDictionary.registerOre("treeLeaves", new ItemStack(leaves_Rainforest, 1, GTRecipeBuilder.WILDCARD));
        OreDictionary.registerOre("treeSapling", new ItemStack(sapling_Rainforest, 1, GTRecipeBuilder.WILDCARD));
    }

    private static void registerTree_Pine() {
        log_Pine = new LogPineTree();
        leaves_Pine = new LeavesPineTree();
        sapling_Pine = new SaplingPineTree();
        OreDictionary.registerOre("logWood", new ItemStack(log_Pine, 1, GTRecipeBuilder.WILDCARD));
        OreDictionary.registerOre("treeLeaves", new ItemStack(leaves_Pine, 1, GTRecipeBuilder.WILDCARD));
        OreDictionary.registerOre("treeSapling", new ItemStack(sapling_Pine, 1, GTRecipeBuilder.WILDCARD));
    }

    public static void recipes() {
        // Rainforest Oak
        addRecipeLogsToPlanks(new ItemStack(log_Rainforest));
        // Pine
        addRecipeLogsToPlanks(new ItemStack(log_Pine));
    }

    public static void addRecipeLogsToPlanks(ItemStack aStack) {
        // add normal recipes like vanilla, so oredict recipe handlers will add recipes from logs to planks.
        // other recipes are also processed by oredict recipe handlers.
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 4), aStack);
    }

}
