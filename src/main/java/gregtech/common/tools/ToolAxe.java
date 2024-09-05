package gregtech.common.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;

public class ToolAxe extends GTTool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 100;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
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
    public float getSpeedMultiplier() {
        return 2.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public String getCraftingSound() {
        return null;
    }

    @Override
    public String getEntityHitSound() {
        return null;
    }

    @Override
    public String getMiningSound() {
        return null;
    }

    @Override
    public boolean canBlock() {
        return false;
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
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "axe")
            || GTToolHarvestHelper.isAppropriateMaterial(aBlock, Material.wood)
            || GTToolHarvestHelper.isSpecialBlock(aBlock, Blocks.ladder);
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rAmount = 0;
        if ((GregTechAPI.sTimber) && (!aPlayer.isSneaking())
            && (OrePrefixes.log.contains(new ItemStack(aBlock, 1, aMetaData)))) {
            int tY = aY + 1;
            for (int tH = aPlayer.worldObj.getHeight(); tY < tH; tY++) {
                if ((aPlayer.worldObj.getBlock(aX, tY, aZ) != aBlock)
                    || (!aPlayer.worldObj.func_147480_a(aX, tY, aZ, true))) {
                    break;
                }
                rAmount++;
            }
        }
        return rAmount;
    }

    @Override
    public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World aWorld,
        int aX, int aY, int aZ) {

        if (aBlock.isWood(aPlayer.worldObj, aX, aY, aZ)
            && OrePrefixes.log.contains(new ItemStack(aBlock, 1, aMetaData))) {
            float rAmount = 1.0F;
            float tIncrement = 1.0F;
            if ((GregTechAPI.sTimber) && !aPlayer.isSneaking()) {
                int tY = aY + 1;
                for (int tH = aPlayer.worldObj.getHeight(); (tY < tH)
                    && (aPlayer.worldObj.getBlock(aX, tY, aZ) == aBlock); tY++) {
                    tIncrement += 0.1F;
                    rAmount += tIncrement;
                }
            }
            return 2.0F * aDefault / rAmount;
        }

        return (aBlock.getMaterial() == Material.leaves) || (aBlock.getMaterial() == Material.vine)
            || (aBlock.getMaterial() == Material.plants)
            || (aBlock.getMaterial() == Material.gourd) ? aDefault / 4.0F : aDefault;
    }

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadAxe.mTextureIndex]
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.stick.mTextureIndex];
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {}

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has been chopped by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
