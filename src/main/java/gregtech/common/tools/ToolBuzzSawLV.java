package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolBuzzSawLV extends ToolSaw {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 300;
    }

    @Override
    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_ONE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_TWO.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_ONE.toString();
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return false;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadBuzzSaw
                .getTextureIndex()]
            : Textures.ItemIcons.HANDLE_BUZZSAW;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Steel.mRGBa;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " got buzzed by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
