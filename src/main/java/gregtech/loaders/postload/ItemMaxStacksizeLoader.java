package gregtech.loaders.postload;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLog;

public class ItemMaxStacksizeLoader implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Changing maximum Stacksizes if configured.");

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
        if (OrePrefixes.block.getDefaultStackSize() < 64) {
            Item.getItemFromBlock(Blocks.stone_slab)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.double_stone_slab)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.brick_stairs)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.nether_brick_stairs)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.sandstone_stairs)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.stone_stairs)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.stone_brick_stairs)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.packed_ice)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.ice)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.soul_sand)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.glowstone)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.snow)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.snow)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.iron_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.gold_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.emerald_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.lapis_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.diamond_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.clay)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.redstone_lamp)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.dirt)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.grass)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.mycelium)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.gravel)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.sand)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.brick_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.wool)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.melon_block)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.pumpkin)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.lit_pumpkin)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.dispenser)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.obsidian)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.piston)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.sticky_piston)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.crafting_table)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.glass)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.jukebox)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.anvil)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.chest)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.trapped_chest)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.noteblock)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.mob_spawner)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.bookshelf)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.furnace)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
            Item.getItemFromBlock(Blocks.lit_furnace)
                .setMaxStackSize(OrePrefixes.block.getDefaultStackSize());
        }
    }
}
