package gregtech.common.tools;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.common.items.behaviors.BehaviourScrewdriver;

public class ToolScrewdriver extends GTTool {

    public static final List<String> mEffectiveList = Arrays.asList(
        EntityCaveSpider.class.getName(),
        EntitySpider.class.getName(),
        "EntityTFHedgeSpider",
        "EntityTFKingSpider",
        "EntityTFSwarmSpider",
        "EntityTFTowerBroodling");

    @Override
    public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack,
        EntityPlayer aPlayer) {
        String tName = aEntity.getClass()
            .getName();
        tName = tName.substring(tName.lastIndexOf('.') + 1);
        return mEffectiveList.contains(tName) ? aOriginalDamage * 2.0F : aOriginalDamage;
    }

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 400;
    }

    @Override
    public float getBaseDamage() {
        return 1.5F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.GTCEU_OP_SCREWDRIVER.toString();
    }

    @Override
    public boolean canBlock() {
        return true;
    }

    @Override
    public boolean isMiningTool() {
        return false;
    }

    @Override
    public boolean isScrewdriver() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "screwdriver")
            || GTToolHarvestHelper.isAppropriateMaterial(aBlock, Material.circuits);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadScrewdriver
                .getTextureIndex()]
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_handleScrewdriver];
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return !aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourScrewdriver(1, 200));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " is screwed! (by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + ")");
    }
}
