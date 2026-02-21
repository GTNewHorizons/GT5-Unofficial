package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTLanguageManager;

public class ItemConcretes extends ItemStonesAbstract {

    private final String mRunFasterToolTip = GTLanguageManager
        .addStringLocalization("gt.runfastertooltip", "You can walk faster on this Block");

    public ItemConcretes(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        aList.add(this.mRunFasterToolTip);
    }
}
