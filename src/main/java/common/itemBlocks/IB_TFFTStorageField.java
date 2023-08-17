package common.itemBlocks;

import java.text.NumberFormat;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import common.tileentities.GTMTE_TFFT;

public class IB_TFFTStorageField extends ItemBlock {

    public IB_TFFTStorageField(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        int meta = stack.getItemDamage();
        if (meta > 0) {
            lines.add(StatCollector.translateToLocal("tile.kekztech_tfftstoragefield_block.desc"));
            lines.add(
                    "Capacity: " + EnumChatFormatting.BLUE
                            + NumberFormat.getNumberInstance().format(GTMTE_TFFT.Field.VALUES[meta - 1].getCapacity())
                            + EnumChatFormatting.GRAY
                            + " L");
            lines.add(
                    "Per Fluid Capacity: " + EnumChatFormatting.BLUE
                            + NumberFormat.getNumberInstance()
                                    .format(GTMTE_TFFT.Field.VALUES[meta - 1].getCapacity() / 25)
                            + EnumChatFormatting.GRAY
                            + " L");
            lines.add(
                    "Power Draw: " + EnumChatFormatting.BLUE
                            + GTMTE_TFFT.Field.VALUES[meta - 1].getCost()
                            + EnumChatFormatting.GRAY
                            + " EU/t");
        }
    }
}
