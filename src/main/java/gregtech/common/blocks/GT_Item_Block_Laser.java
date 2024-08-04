package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_LanguageManager;

public class GT_Item_Block_Laser extends ItemBlock {

    public GT_Item_Block_Laser(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean f3_h) {
        tooltip.add(
            GT_LanguageManager
                .addStringLocalization("gt.laserplatingtooltip", "Engineered to withstand extreme temperatures"));
    }
}
