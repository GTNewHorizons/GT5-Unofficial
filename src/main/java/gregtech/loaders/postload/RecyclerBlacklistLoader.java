package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.Chisel;
import static gregtech.api.util.GTModHandler.addToRecyclerBlackList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.glodblock.github.loader.ItemAndBlockHolder;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;

public class RecyclerBlacklistLoader implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Adding Stuff to the Recycler Blacklist.");
        addToRecyclerBlackList(new ItemStack(Items.arrow, 1, 0));
        addToRecyclerBlackList(new ItemStack(Items.bone, 1, 0));
        addToRecyclerBlackList(ItemList.Dye_Bonemeal.get(1L));

        addToRecyclerBlackList(new ItemStack(Items.rotten_flesh, 1, 0));

        addToRecyclerBlackList(new ItemStack(Items.string, 1, 0));

        addToRecyclerBlackList(new ItemStack(Items.egg, 1, 0));
        ItemStack tStack = new ItemStack(Blocks.cobblestone, 1, 0);
        while (tStack != null) {
            addToRecyclerBlackList(tStack);
            tStack = GTModHandler
                .getRecipeOutput(tStack, tStack, tStack, tStack, tStack, tStack, tStack, tStack, tStack);
        }
        addToRecyclerBlackList(new ItemStack(Blocks.gravel, 1, 32767));
        addToRecyclerBlackList(new ItemStack(Items.flint, 1, 32767));
        addToRecyclerBlackList(new ItemStack(Blocks.cobblestone_wall, 1, 32767));
        addToRecyclerBlackList(new ItemStack(Blocks.sandstone_stairs, 1, 32767));
        addToRecyclerBlackList(new ItemStack(Blocks.stone_stairs, 1, 32767));
        addToRecyclerBlackList(new ItemStack(Blocks.stone_brick_stairs, 1, 32767));
        addToRecyclerBlackList(GTModHandler.getSmeltingOutput(new ItemStack(Blocks.stone, 1, 0), false, null));
        addToRecyclerBlackList(
            GTModHandler
                .getRecipeOutput(new ItemStack(Blocks.glass, 1, 0), null, null, new ItemStack(Blocks.glass, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler
                .getRecipeOutput(new ItemStack(Blocks.stone, 1, 0), null, null, new ItemStack(Blocks.stone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.cobblestone, 1, 0),
                null,
                null,
                new ItemStack(Blocks.cobblestone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.stone, 1, 0),
                null,
                new ItemStack(Blocks.stone, 1, 0),
                null,
                new ItemStack(Blocks.stone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.stone, 1, 0),
                new ItemStack(Blocks.glass, 1, 0),
                new ItemStack(Blocks.stone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.cobblestone, 1, 0),
                new ItemStack(Blocks.glass, 1, 0),
                new ItemStack(Blocks.cobblestone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.glass, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.sand, 1, 0),
                new ItemStack(Blocks.glass, 1, 0),
                new ItemStack(Blocks.sand, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0),
                new ItemStack(Blocks.sandstone, 1, 0)));
        addToRecyclerBlackList(GTModHandler.getRecipeOutput(new ItemStack(Blocks.glass, 1, 0)));
        addToRecyclerBlackList(
            GTModHandler.getRecipeOutput(new ItemStack(Blocks.glass, 1, 0), new ItemStack(Blocks.glass, 1, 0)));
        if (Chisel.isModLoaded()) {
            for (int i = 1; i <= 15; i++) {
                addToRecyclerBlackList(GTModHandler.getModItem(Chisel.ID, "cobblestone", 1, i));
            }
            for (int i = 0; i <= 15; i++) {
                addToRecyclerBlackList(GTModHandler.getModItem(Chisel.ID, "stonebricksmooth", 1, i));
            }
        }
        if (Mods.AE2FluidCraft.isModLoaded()) {
            addToRecyclerBlackList(new ItemStack(ItemAndBlockHolder.DROP));
            addToRecyclerBlackList(new ItemStack(ItemAndBlockHolder.PACKET));
        }
    }
}
