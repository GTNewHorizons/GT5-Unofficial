package gregtech.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GTGenericItem;

public class ItemIceCream extends GTGenericItem {

    public static final int FLAVOR_COUNT = 20;

    public ItemIceCream() {
        super("icecream", null, null);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        final String key = getUnlocalizedName(aStack) + ".name";
        return StatCollector.canTranslate(key) ? StatCollector.translateToLocal(key)
            : super.getItemStackDisplayName(aStack);
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        // Meta 0 is "Random Ice Cream", a display-only for the NEI fake recipe
        for (int i = 1; i <= FLAVOR_COUNT; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }

    // TODO:Placeholder
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {}

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        return Items.snowball.getIconFromDamage(0);
    }
}
