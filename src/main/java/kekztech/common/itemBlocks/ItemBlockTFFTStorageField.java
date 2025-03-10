package kekztech.common.itemBlocks;

import java.text.NumberFormat;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import kekztech.common.tileentities.MTETankTFFT;

public class ItemBlockTFFTStorageField extends ItemBlock {

    private static final int UNIQUE_FLUIDS_PER_CELL = 25;

    public ItemBlockTFFTStorageField(Block block) {
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
                StatCollector.translateToLocalFormatted(
                    "tooltip.kekztech.tfft.capacity",
                    NumberFormat.getNumberInstance()
                        .format(MTETankTFFT.Field.VALUES[meta - 1].getCapacity())));
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "tooltip.kekztech.tfft.capacity.per_fluid",
                    NumberFormat.getNumberInstance()
                        .format(MTETankTFFT.Field.VALUES[meta - 1].getCapacity() / UNIQUE_FLUIDS_PER_CELL)));
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "tooltip.kekztech.tfft.power_draw",
                    MTETankTFFT.Field.VALUES[meta - 1].getCost()));
        }
    }
}
