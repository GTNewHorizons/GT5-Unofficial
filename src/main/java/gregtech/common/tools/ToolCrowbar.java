package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.google.common.base.Strings;

import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolCrowbar extends GTTool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    @Override
    public float getBaseDamage() {
        return 2.0F;
    }

    @Override
    public boolean canBlock() {
        return true;
    }

    @Override
    public boolean isCrowbar() {
        return true;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        if (aBlock.getMaterial() == Material.circuits) {
            return true;
        }
        String tTool = aBlock.getHarvestTool(aMetaData);
        // A block that names no harvest tool is fair game. This used to scan MetaGeneratedTool01's stats for another
        // ToolCrowbar that disagreed, but the same instance was registered under both of the crowbar's metadata ids,
        // so the "i != this" test could never pass -- and the crowbar has its own item now regardless.
        if (Strings.isNullOrEmpty(tTool)) return true;
        return tTool.equals("crowbar");
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[MaterialIconRegistry.IconType.TOOL_CROWBAR
                .ordinal()]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " was removed by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
