package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolDrillMV extends ToolDrillLV {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 400;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 800;
    }

    @Override
    public int getBaseQuality() {
        return 1;
    }

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 6.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 2.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(
                aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadDrill.mTextureIndex]
            : Textures.ItemIcons.POWER_UNIT_MV;
    }
}
