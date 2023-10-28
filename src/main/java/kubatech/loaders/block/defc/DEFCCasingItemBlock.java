package kubatech.loaders.block.defc;

import static kubatech.kubatech.KT;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.common.blocks.GT_Item_Casings_Abstract;

public class DEFCCasingItemBlock extends GT_Item_Casings_Abstract {

    public DEFCCasingItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(KT);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if (getDamage(aStack) > 0)
            aList.add(StatCollector.translateToLocalFormatted("defc.casing.tip", getDamage(aStack)));
        super.addInformation(aStack, aPlayer, aList, aF3_H);
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.field_150939_a.getIcon(0, p_77617_1_);
    }
}
