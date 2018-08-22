package gregtech.common.tools;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import net.minecraft.item.ItemStack;

public class GT_Tool_Wrench_IV
        extends GT_Tool_Wrench_LV {
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
        return 4.5F;
    }

    public float getSpeedMultiplier() {
        return 6.0F;
    }

    public float getMaxDurabilityMultiplier() {
        return 16.0F;
    }

    public boolean canBlock() {
        return false;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadWrench.mTextureIndex] : Textures.ItemIcons.POWER_UNIT_IV;
    }

    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }
}