package gtnhintergalactic.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gtnhintergalactic.block.BlockCasingDysonSwarm;

public class ItemCasingDysonSwarm extends ItemBlock {

    public ItemCasingDysonSwarm(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.DysonSwarm"
            + BlockCasingDysonSwarm.names[stack.getItemDamage() % BlockCasingDysonSwarm.names.length];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean f3_h) {
        if (stack.getItemDamage() == 9) {
            tooltip.add("Blast Resistance: 1500");
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
