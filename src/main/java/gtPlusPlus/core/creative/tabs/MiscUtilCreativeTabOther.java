package gtPlusPlus.core.creative.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class MiscUtilCreativeTabOther extends CreativeTabs {

    public MiscUtilCreativeTabOther(final String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return Items.repeater;
    }
}
