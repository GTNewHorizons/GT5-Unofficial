package com.github.bartimaeusnek.bartworks.client.creativetabs;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class bartworksTab extends CreativeTabs {

    public bartworksTab (String lable) {
        super(lable);
    }

    @Override
    public Item getTabIconItem() {
        return ItemRegistry.RockcutterHV;
    }

}
