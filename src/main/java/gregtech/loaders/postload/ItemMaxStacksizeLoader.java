package gregtech.loaders.postload;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;

public class ItemMaxStacksizeLoader implements Runnable {

    @Override
    public void run() {
        GT_FML_LOGGER.debug("GTMod: Changing maximum Stacksizes if configured.");

        ItemList.Upgrade_Overclocker.getItem()
            .setMaxStackSize(GTMod.proxy.mUpgradeCount);
        Items.cake.setMaxStackSize(64);
        Items.wooden_door.setMaxStackSize(8);
        Items.iron_door.setMaxStackSize(8);
        Items.ender_pearl.setMaxStackSize(64);
        Items.egg.setMaxStackSize(64);
        Items.snowball.setMaxStackSize(64);
        Items.mushroom_stew.setMaxStackSize(64);
        if (OrePrefixes.plank.getDefaultStackSize() < 64) {
            Item.getItemFromBlock(Blocks.wooden_slab)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.double_wooden_slab)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.oak_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.jungle_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.birch_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.spruce_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.acacia_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.dark_oak_stairs)
                .setMaxStackSize(OrePrefixes.plank.getDefaultStackSize());
        }
    }
}
