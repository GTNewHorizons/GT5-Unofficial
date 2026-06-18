package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.common.items.behaviors.BehaviourPlungerEssentia;
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
        return SoundResource.GTCEU_OP_PLUNGER.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.GTCEU_OP_PLUNGER.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.GTCEU_OP_PLUNGER.toString();
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "plunger");
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_plunger]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourPlungerItem(getToolDamagePerDropConversion()));
        aItem.addItemBehavior(aID, new BehaviourPlungerFluid(getToolDamagePerDropConversion()));
        if (Mods.Thaumcraft.isModLoaded()) {
            aItem.addItemBehavior(aID, new BehaviourPlungerEssentia(getToolDamagePerDropConversion()));
        }
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
