package detrav.items.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.GTMaterialIcons;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravToolElectricProspector extends DetravToolElectricProspectorBase {

    private final int tier;

    public DetravToolElectricProspector(int tier) {
        this.tier = tier;
    }

    @Override
    public int getBaseQuality() {
        return tier - 6;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        if (tier - 6 == 0) return 1.0F;
        else return (tier - 6F) * 2F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {

        if (tier <= 9 && tier >= 6) {
            return aIsToolHead
                ? GTMaterialIcons.item(PROSPECTOR_ICONS[tier - 5], MetaGeneratedTool.getPrimaryMaterialML(aStack))
                : null;
        } else {
            return aIsToolHead
                ? GTMaterialIcons.item(PROSPECTOR_ICONS[1], MetaGeneratedTool.getPrimaryMaterialML(aStack))
                : null;
        }
    }
}
