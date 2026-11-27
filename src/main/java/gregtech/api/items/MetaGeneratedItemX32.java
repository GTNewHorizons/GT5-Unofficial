package gregtech.api.items;

import net.minecraft.util.IIcon;

/// A GregTech meta-item whose custom items occupy damage 32000 to 32765. Damage values below 32000 are unused;
/// [gregtech.loaders.postload.PosteaTransformers] converts saved stacks from that range into MaterialLib items.
public abstract class MetaGeneratedItemX32 extends MetaGeneratedItem {

    public MetaGeneratedItemX32(String aUnlocalized) {
        super(aUnlocalized, (short) 32000, (short) 766);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 32000) return null;
        return aMetaData - 32000 < mIconList.length ? mIconList[aMetaData - 32000][0] : null;
    }
}
