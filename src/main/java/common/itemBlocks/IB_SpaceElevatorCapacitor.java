package common.itemBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class IB_SpaceElevatorCapacitor extends ItemBlock {

    public IB_SpaceElevatorCapacitor(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        lines.add(StatCollector.translateToLocal("tile.kekztech_spaceelevatorcapacitor_block.desc"));
    }
}
