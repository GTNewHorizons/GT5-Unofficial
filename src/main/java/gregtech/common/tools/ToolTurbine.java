package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public abstract class ToolTurbine extends GTTool {

    @Override
    public abstract float getBaseDamage();

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return false;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? getTurbineIcon() : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.GREEN + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " put "
                + EnumChatFormatting.RED
                + aEntity.getCommandSenderName()
                + "s"
                + EnumChatFormatting.WHITE
                + " head into a turbine");
    }

    public abstract IIconContainer getTurbineIcon();

    @Override
    public abstract float getSpeedMultiplier();

    @Override
    public abstract float getMaxDurabilityMultiplier();

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }
}
