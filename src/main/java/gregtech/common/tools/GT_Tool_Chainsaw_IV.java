package gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.item.ItemStack;

public class GT_Tool_Chainsaw_IV
        extends GT_Tool_Chainsaw_LV {
    public int getToolDamagePerBlockBreak() {
        return 12800;
    }

    public int getToolDamagePerDropConversion() {
        return 25600;
    }

    public int getToolDamagePerContainerCraft() {
        return 204800;
    }

    public int getToolDamagePerEntityAttack() {
        return 51200;
    }

    public int getBaseQuality() {
        return 1;
    }

    public float getBaseDamage() {
        return 5.0F;
    }

    public float getSpeedMultiplier() {
        return 6.0F;
    }

    public float getMaxDurabilityMultiplier() {
        return 16.0F;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? gregtech.api.items.GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadChainsaw.mTextureIndex] : Textures.ItemIcons.POWER_UNIT_IV;
    }
}