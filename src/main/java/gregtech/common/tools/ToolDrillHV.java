package gregtech.common.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolDrillHV extends ToolDrillLV {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 800;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 1600;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 12800;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 3200;
    }

    @Override
    public int getBaseQuality() {
        return 1;
    }

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 9.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "highpowerdrill");
            GTMod.achievements.issueAchievement(aPlayer, "buildDDrill");
        } catch (Exception ignored) {}
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.StainlessSteel.mRGBa;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadDrill
            .getTextureIndex()] : Textures.ItemIcons.POWER_UNIT_HV;
    }
}
