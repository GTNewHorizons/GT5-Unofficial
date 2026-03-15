package detrav.utils;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTCreativeTab;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravCreativeTab extends GTCreativeTab {

    public DetravCreativeTab() {
        super("GT_DETRAV_TAB", "Detrav Scanner");
    }

    @Override
    public ItemStack getIconItemStack() {
        return ItemList.Tool_Scanner.get(1, new ItemStack(Items.stick, 1));
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
