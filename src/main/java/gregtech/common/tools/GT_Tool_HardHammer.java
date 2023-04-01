package gregtech.common.tools;

import static gregtech.GT_Mod.MAX_IC2;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.GT_Mod;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_Prospecting;

public class GT_Tool_HardHammer extends GT_Tool {

    public static final List<String> mEffectiveList = Arrays.asList(
            EntityIronGolem.class.getName(),
            "EntityTowerGuardian");

    @Override
    public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack,
            EntityPlayer aPlayer) {
        String tName = aEntity.getClass()
                              .getName();
        tName = tName.substring(tName.lastIndexOf('.') + 1);
        return (mEffectiveList.contains(tName)) || (tName.contains("Golem")) ? aOriginalDamage * 2.0F : aOriginalDamage;
    }

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 200;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 400;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 200;
    }

    @Override
    public int getBaseQuality() {
        return 0;
    }

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {
        return aOriginalHurtResistance * 2;
    }

    @Override
    public float getSpeedMultiplier() {
        return 0.75F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.RANDOM_ANVIL_USE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return null;
    }

    @Override
    public String getBreakingSound() {
        return SoundResource.RANDOM_ANVIL_BREAK.toString();
    }

    @Override
    public String getMiningSound() {
        return null;
    }

    @Override
    public boolean canBlock() {
        return true;
    }

    @Override
    public boolean isCrowbar() {
        return false;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "hammer", "pickaxe")
                || GT_ToolHarvestHelper.isAppropriateMaterial(
                        aBlock,
                        Material.rock,
                        Material.glass,
                        Material.ice,
                        Material.packedIce)
                || GT_Recipe.GT_Recipe_Map.sHammerRecipes.containsInput(new ItemStack(aBlock, 1, aMetaData));
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
            int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rConversions = 0;
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(
                null,
                true,
                MAX_IC2,
                null,
                new ItemStack(aBlock, 1, aMetaData));
        if ((tRecipe == null) || (aBlock.hasTileEntity(aMetaData))) {
            for (ItemStack tDrop : aDrops) {
                tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(
                        null,
                        true,
                        MAX_IC2,
                        null,
                        GT_Utility.copyAmount(1L, tDrop));
                if (tRecipe != null) {
                    ItemStack tHammeringOutput = tRecipe.getOutput(0);
                    if (tHammeringOutput != null) {
                        rConversions += tDrop.stackSize;
                        tDrop.stackSize *= tHammeringOutput.stackSize;
                        tHammeringOutput.stackSize = tDrop.stackSize;
                        GT_Utility.setStack(tDrop, tHammeringOutput);
                    }
                }
            }
        } else {
            aDrops.clear();
            aDrops.add(tRecipe.getOutput(0));
            rConversions++;
        }
        return rConversions;
    }

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
                ? GT_MetaGenerated_Tool.getPrimaryMaterial(
                        aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadHammer.mTextureIndex]
                : GT_MetaGenerated_Tool.getSecondaryMaterial(
                        aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.stick.mTextureIndex];
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
                : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new Behaviour_Prospecting(1, 1000));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
                EnumChatFormatting.RED + aEntity.getCommandSenderName()
                        + EnumChatFormatting.WHITE
                        + " was squashed by "
                        + EnumChatFormatting.GREEN
                        + aPlayer.getCommandSenderName()
                        + EnumChatFormatting.WHITE);
    }

    @Override
    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        try {
            GT_Mod.achievements.issueAchievement(aPlayer, "tools");
        } catch (Exception ignored) {}
    }
}
