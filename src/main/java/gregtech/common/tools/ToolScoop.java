package gregtech.common.tools;

import static gregtech.api.enums.Mods.Forestry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.common.items.behaviors.BehaviourNone;
import gregtech.common.items.behaviors.BehaviourScoop;

public class ToolScoop extends GTTool {

    public static Material sBeeHiveMaterial;

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "scoop")
            || GTToolHarvestHelper.isAppropriateMaterial(aBlock, sBeeHiveMaterial);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_scoop]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        if (Forestry.isModLoaded()) {
            aItem.addItemBehavior(aID, new BehaviourScoop(200));
        } else {
            aItem.addItemBehavior(aID, new BehaviourNone());
        }
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " got scooped up by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
