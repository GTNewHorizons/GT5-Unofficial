package com.github.bartimaeusnek.bartworks.client.creativetabs;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class GT2Tab extends CreativeTabs {

    public GT2Tab (String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return ItemRegistry.tab;
    }
}
