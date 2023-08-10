package gregtech.loaders.postload;

import static gregtech.api.util.GT_ModHandler.addToRecyclerBlackList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.glodblock.github.loader.ItemAndBlockHolder;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import team.chisel.init.ChiselBlocks;

public class GT_RecyclerBlacklistLoader implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding Stuff to the Recycler Blacklist.");
        if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "easymobgrinderrecycling", true)) {
            addToRecyclerBlackList(new ItemStack(Items.arrow, 1, 0));
            addToRecyclerBlackList(new ItemStack(Items.bone, 1, 0));
            addToRecyclerBlackList(ItemList.Dye_Bonemeal.get(1L));

            addToRecyclerBlackList(new ItemStack(Items.rotten_flesh, 1, 0));

            addToRecyclerBlackList(new ItemStack(Items.string, 1, 0));

            addToRecyclerBlackList(new ItemStack(Items.egg, 1, 0));
        }
        if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "easystonerecycling", true)) {
            ItemStack tStack = new ItemStack(Blocks.cobblestone, 1, 0);
            while (tStack != null) {
                addToRecyclerBlackList(tStack);
                tStack = GT_ModHandler
                    .getRecipeOutput(tStack, tStack, tStack, tStack, tStack, tStack, tStack, tStack, tStack);
            }
            addToRecyclerBlackList(new ItemStack(Blocks.gravel, 1, 32767));
            addToRecyclerBlackList(new ItemStack(Items.flint, 1, 32767));
            addToRecyclerBlackList(new ItemStack(Blocks.cobblestone_wall, 1, 32767));
            addToRecyclerBlackList(new ItemStack(Blocks.sandstone_stairs, 1, 32767));
            addToRecyclerBlackList(new ItemStack(Blocks.stone_stairs, 1, 32767));
            addToRecyclerBlackList(new ItemStack(Blocks.stone_brick_stairs, 1, 32767));
            addToRecyclerBlackList(GT_ModHandler.getSmeltingOutput(new ItemStack(Blocks.stone, 1, 0), false, null));
            addToRecyclerBlackList(
                GT_ModHandler
                    .getRecipeOutput(new ItemStack(Blocks.glass, 1, 0), null, null, new ItemStack(Blocks.glass, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler
                    .getRecipeOutput(new ItemStack(Blocks.stone, 1, 0), null, null, new ItemStack(Blocks.stone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.cobblestone, 1, 0),
                    null,
                    null,
                    new ItemStack(Blocks.cobblestone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.stone, 1, 0),
                    null,
                    new ItemStack(Blocks.stone, 1, 0),
                    null,
                    new ItemStack(Blocks.stone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.stone, 1, 0),
                    new ItemStack(Blocks.glass, 1, 0),
                    new ItemStack(Blocks.stone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.cobblestone, 1, 0),
                    new ItemStack(Blocks.glass, 1, 0),
                    new ItemStack(Blocks.cobblestone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.glass, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.sand, 1, 0),
                    new ItemStack(Blocks.glass, 1, 0),
                    new ItemStack(Blocks.sand, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0),
                    new ItemStack(Blocks.sandstone, 1, 0)));
            addToRecyclerBlackList(GT_ModHandler.getRecipeOutput(new ItemStack(Blocks.glass, 1, 0)));
            addToRecyclerBlackList(
                GT_ModHandler.getRecipeOutput(new ItemStack(Blocks.glass, 1, 0), new ItemStack(Blocks.glass, 1, 0)));
            if (Mods.Chisel.isModLoaded()) {
                for (int i = 1; i <= 15; i++) {
                    addToRecyclerBlackList(new ItemStack(ChiselBlocks.cobblestone, 1, i));
                }
                for (int i = 0; i <= 15; i++) {
                    addToRecyclerBlackList(new ItemStack(ChiselBlocks.stonebricksmooth, 1, i));
                }
            }
        }
        if (Mods.AE2FluidCraft.isModLoaded()) {
            addToRecyclerBlackList(new ItemStack(ItemAndBlockHolder.DROP));
            addToRecyclerBlackList(new ItemStack(ItemAndBlockHolder.PACKET));
        }
    }
}
