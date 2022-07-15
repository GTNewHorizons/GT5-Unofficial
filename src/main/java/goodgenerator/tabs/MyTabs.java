package goodgenerator.tabs;

import goodgenerator.loader.Loaders;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MyTabs extends CreativeTabs {
    public MyTabs(String name) {
        super(name);
    }

    @Override
    public Item getTabIconItem() {
        return Loaders.radiationProtectionPlate;
    }
}
