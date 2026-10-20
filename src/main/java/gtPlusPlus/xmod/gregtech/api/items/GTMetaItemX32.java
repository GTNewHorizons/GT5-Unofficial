package gtPlusPlus.xmod.gregtech.api.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/// A GT++ meta-item whose custom items occupy damage 32000 to 32765.
public abstract class GTMetaItemX32 extends GTMetaItem {

    public GTMetaItemX32(final String aUnlocalized) {
        super(aUnlocalized, (short) 32000, (short) 766);
    }

    @Override
    public ItemStack getContainerItem(final ItemStack aStack) {
        return null;
    }

    @Override
    public final IIcon getIconFromDamage(final int aMetaData) {
        if (aMetaData < 32000) {
            return null;
        }
        return (aMetaData - 32000) < this.mIconList.length ? this.mIconList[aMetaData - 32000][0] : null;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
        return 0xFFFFFF;
    }
}
