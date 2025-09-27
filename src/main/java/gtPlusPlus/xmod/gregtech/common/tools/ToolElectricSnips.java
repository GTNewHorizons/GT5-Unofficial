package gtPlusPlus.xmod.gregtech.common.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.tools.ToolWireCutter;

public class ToolElectricSnips extends ToolWireCutter {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 100;
    }

    @Override
    public float getBaseDamage() {
        return 4.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.4F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_electricSnips]
            : ItemIcons.POWER_UNIT_MV;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.TungstenSteel.mRGBa;
    }

    @Override
    public void onToolCrafted(final ItemStack aStack, final EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        aPlayer.triggerAchievement(AchievementList.buildSword);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "tools");
        } catch (final Exception e) {}
    }

    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase aPlayer, final EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has been Snipped out of existence by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
