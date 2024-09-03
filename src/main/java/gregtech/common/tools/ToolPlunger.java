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
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourPlungerFluid;
import gregtech.common.items.behaviors.BehaviourPlungerItem;

public class ToolPlunger extends GTTool {

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
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "plunger");
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.PLUNGER : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    @SuppressWarnings("unchecked") // the IItemBehaviour cast cannot be expressed strictly via generics
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourPlungerItem(getToolDamagePerDropConversion()));
        aItem.addItemBehavior(aID, new BehaviourPlungerFluid(getToolDamagePerDropConversion()));
        try {
            Object tObject = GTUtility.callConstructor(
                "gregtech.common.items.behaviors.BehaviourPlungerEssentia",
                0,
                null,
                false,
                getToolDamagePerDropConversion());
            if ((tObject instanceof IItemBehaviour)) {
                aItem.addItemBehavior(aID, (IItemBehaviour<MetaBaseItem>) tObject);
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
