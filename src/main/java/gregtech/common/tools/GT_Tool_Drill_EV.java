package gregtech.common.tools;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_Tool_Drill_EV
        extends GT_Tool_Drill_LV {
    public int getToolDamagePerBlockBreak() {
        return GT_Mod.gregtechproxy.mHardRock ? 1600 : 3200;
    }

    public int getToolDamagePerDropConversion() {
        return 6400;
    }

    public int getToolDamagePerContainerCraft() {
        return 51200;
    }

    public int getToolDamagePerEntityAttack() {
        return 12800;
    }

    public int getBaseQuality() {
        return 1;
    }

    public float getBaseDamage() {
        return 3.5F;
    }

    public float getSpeedMultiplier() {
        return 12.0F;
    }

    public float getMaxDurabilityMultiplier() {
        return 8.0F;
    }

    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        try {
            GT_Mod.instance.achievements.issueAchievement(aPlayer, "highpowerdrill");
            GT_Mod.instance.achievements.issueAchievement(aPlayer, "buildDDrill");
        } catch (Exception e) {
        }
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? gregtech.api.items.GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadDrill.mTextureIndex] : Textures.ItemIcons.POWER_UNIT_EV;
    }
}