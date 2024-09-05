package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTLanguageManager;

public class ItemBlockLaser extends ItemBlock {

    public ItemBlockLaser(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean f3_h) {
        tooltip.add(
            GTLanguageManager
                .addStringLocalization("gt.laserplatingtooltip", "Engineered to withstand extreme temperatures"));
    }
}
