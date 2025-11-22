package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.behaviors.BehaviorTrowel;

public class ToolTrowel extends GTTool {

    private static final String TYPE_NAME = "trowel";

    @Override
    public float getBaseDamage() {
        return 0;
    }

    @Override
    public boolean isMinableBlock(final Block aBlock, final int aMetaData) {
        return false;
    }

    @Override
    public IIconContainer getIcon(final boolean aIsToolHead, final ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_trowel]
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_handleTrowel];
    }

    @Override
    public short[] getRGBa(final boolean aIsToolHead, final ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public String getToolTypeName() {
        return TYPE_NAME;
    }

    @Override
    public void onStatsAddedToTool(final MetaGeneratedTool aItem, final int aID) {
        aItem.addItemBehavior(aID, new BehaviorTrowel());
    }

    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase aPlayer, final EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " was taken to trowel town by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
