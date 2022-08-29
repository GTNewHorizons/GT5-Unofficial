package gtPlusPlus.xmod.bop.creative;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MiscUtilsBOPTab extends CreativeTabs {

    public MiscUtilsBOPTab(final String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Rainforest)
                .getItem();
    }
}
