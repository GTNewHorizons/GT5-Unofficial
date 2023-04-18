package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_Plunger_Fluid;
import gregtech.common.items.behaviors.Behaviour_Plunger_Item;

public class GT_Tool_Plunger extends GT_Tool {

    @Override
    public float getBaseDamage() {
        return 1.25F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 0.25F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE.toString();
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "plunger");
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.PLUNGER : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
            : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    @SuppressWarnings("unchecked") // the IItemBehaviour cast cannot be expressed strictly via generics
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new Behaviour_Plunger_Item(getToolDamagePerDropConversion()));
        aItem.addItemBehavior(aID, new Behaviour_Plunger_Fluid(getToolDamagePerDropConversion()));
        try {
            Object tObject = GT_Utility.callConstructor(
                "gregtech.common.items.behaviors.Behaviour_Plunger_Essentia",
                0,
                null,
                false,
                getToolDamagePerDropConversion());
            if ((tObject instanceof IItemBehaviour)) {
                aItem.addItemBehavior(aID, (IItemBehaviour<GT_MetaBase_Item>) tObject);
            }
        } catch (Throwable ignored) {}
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " got stuck trying to escape through a Pipe while fighting "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
