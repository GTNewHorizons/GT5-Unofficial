package goodgenerator.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import goodgenerator.loader.Loaders;

public class MyTabs extends CreativeTabs {

    public MyTabs(String name) {
        super(name);
    }

    @Override
    public Item getTabIconItem() {
        return Loaders.radiationProtectionPlate;
    }
}
