package detrav.utils;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import detrav.items.DetravMetaGeneratedTool01;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravCreativeTab extends CreativeTabs {

    public DetravCreativeTab() {
        super("Detrav Scanner");
    }

    @Override
    public Item getTabIconItem() {
        return Items.stick;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List p_78018_1_) {
        for (Object o : Item.itemRegistry) {
            Item item = (Item) o;

            if (item == null) {
                continue;
            }

            for (CreativeTabs tab : item.getCreativeTabs()) {
                if (tab == this) {
                    item.getSubItems(item, this, p_78018_1_);
                    if (item instanceof DetravMetaGeneratedTool01) {
                        ((DetravMetaGeneratedTool01) item).getDetravSubItems(item, this, p_78018_1_);
                    }
                }
            }
        }

        if (this.func_111225_m() != null) {
            this.addEnchantmentBooksToList(p_78018_1_, this.func_111225_m());
        }
    }
}
