package gregtech.common.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.common.items.behaviors.BehaviourSense;

public class ToolSense extends GTTool {

    private final ThreadLocal<Object> sIsHarvestingRightNow = new ThreadLocal<>();

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "sense", "scythe")
            || GTToolHarvestHelper.isAppropriateMaterial(aBlock, Material.plants, Material.leaves);
    }

    @Override
    public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World worldObj,
        int aX, int aY, int aZ) {
        // Speed nerf for using AOE tools to break single block
        if (aPlayer != null && aPlayer.isSneaking()) {
            return aDefault / 2;
        }
        return super.getMiningSpeed(aBlock, aMetaData, aDefault, aPlayer, worldObj, aX, aY, aZ);
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rConversions = 0;
        if ((this.sIsHarvestingRightNow.get() == null) && ((aPlayer instanceof EntityPlayerMP))) {
            this.sIsHarvestingRightNow.set(this);

            if (!aPlayer.isSneaking()) {
                for (int i = -2; i < 3; i++) {
                    for (int j = -2; j < 3; j++) {
                        for (int k = -2; k < 3; k++) {
                            if (((i != 0) || (j != 0) || (k != 0)) && (aStack.getItem()
                                .getDigSpeed(
                                    aStack,
                                    aPlayer.worldObj.getBlock(aX + i, aY + j, aZ + k),
                                    aPlayer.worldObj.getBlockMetadata(aX + i, aY + j, aZ + k))
                                > 0.0F)
                                && (((EntityPlayerMP) aPlayer).theItemInWorldManager
                                    .tryHarvestBlock(aX + i, aY + j, aZ + k))) {
                                rConversions++;
                            }
                        }
                    }
                }
            }

            this.sIsHarvestingRightNow.set(null);
        }
        return rConversions;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(
                aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.toolHeadSense.mTextureIndex]
            : MetaGeneratedTool
                .getSecondaryMaterial(aStack).mIconSet.mTextures[gregtech.api.enums.OrePrefixes.stick.mTextureIndex];
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourSense(getToolDamagePerBlockBreak()));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.GREEN + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has taken the Soul of "
                + EnumChatFormatting.RED
                + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }

}
