package detrav.items.tools;

import net.minecraft.item.ItemStack;

import detrav.enums.Textures01;
import gregtech.api.interfaces.IIconContainer;

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
        if (tier - 6 == 0) return (float) Math.pow(((float) ((tier - 6F) * 2F)), 0.0D);
        else return (float) ((tier - 6F) * 2F);
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {

        if (tier <= 9 && tier >= 6) return Textures01.mTextures[tier - 5];
        else return Textures01.mTextures[1];
    }
}
