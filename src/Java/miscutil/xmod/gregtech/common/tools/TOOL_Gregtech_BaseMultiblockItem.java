package miscutil.xmod.gregtech.common.tools;

import gregtech.api.interfaces.IIconContainer;
import miscutil.xmod.gregtech.api.items.Gregtech_MetaTool;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class TOOL_Gregtech_BaseMultiblockItem extends TOOL_Gregtech_Base {
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
        return aIsToolHead ? Gregtech_MetaTool.getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
	public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE + " put " + EnumChatFormatting.RED +
                aEntity.getCommandSenderName() + "s" + EnumChatFormatting.WHITE + " head into a turbine");
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
