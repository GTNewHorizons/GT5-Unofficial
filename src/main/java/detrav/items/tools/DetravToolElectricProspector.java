package detrav.items.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravToolElectricProspector extends DetravToolElectricProspectorBase {

    private final int tier;

    public DetravToolElectricProspector(int tier) {
        this.tier = tier;
    }

    public int getBaseQuality() {
        return tier - 6;
    }

    public float getMaxDurabilityMultiplier() {
        if (tier - 6 == 0) return 1.0F;
        else return (tier - 6F) * 2F;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {

        if (tier <= 9 && tier >= 6) {
            return aIsToolHead
                ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[mProspectorTextures[tier - 5]]
                : null;
        } else {
            return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[mProspectorTextures[1]]
                : null;
        }
    }
}
