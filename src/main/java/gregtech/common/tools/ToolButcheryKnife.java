package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolButcheryKnife extends GTTool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {
        return aOriginalHurtResistance * 2;
    }

    @Override
    public float getSpeedMultiplier() {
        return 0.1F;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMiningTool() {
        return false;
    }

    @Override
    public Enchantment[] getEnchantments(ItemStack aStack) {
        return LOOTING_ENCHANTMENT;
    }

    @Override
    public int[] getEnchantmentLevels(ItemStack aStack) {
        return new int[] { (2 + MetaGeneratedTool.getPrimaryMaterial(aStack).mToolQuality) / 2 };
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_butcheryKnife]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.GREEN + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has butchered "
                + EnumChatFormatting.RED
                + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return false;
    }
}
