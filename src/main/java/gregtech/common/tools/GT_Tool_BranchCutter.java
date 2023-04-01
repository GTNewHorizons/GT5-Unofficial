package gregtech.common.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.api.util.GT_Utility;

public class GT_Tool_BranchCutter extends GT_Tool {

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 0.25F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 0.25F;
    }

    @Override
    public boolean isGrafter() {
        return true;
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
            int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        if (aBlock.getMaterial() == Material.leaves) {
            aEvent.dropChance = Math.min(
                    1.0F,
                    Math.max(
                            aEvent.dropChance,
                            (aStack.getItem()
                                   .getHarvestLevel(aStack, "") + 1) * 0.2F));
            if (aBlock == Blocks.leaves) {
                aDrops.clear();
                if (((aMetaData & 0x3) == 0) && (aPlayer.worldObj.rand.nextInt(9) <= aFortune * 2)) {
                    aDrops.add(new ItemStack(Items.apple, 1, 0));
                } else {
                    aDrops.add(new ItemStack(Blocks.sapling, 1, aMetaData & 0x3));
                }
            } else if (aBlock == Blocks.leaves2) {
                aDrops.clear();
                aDrops.add(new ItemStack(Blocks.sapling, 1, (aMetaData & 0x3) + 4));
            } else if (aBlock == GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("rubberLeaves", 1L))) {
                aDrops.clear();
                aDrops.add(GT_ModHandler.getIC2Item("rubberSapling", 1L));
            }
        }
        return 0;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "grafter")
                || GT_ToolHarvestHelper.isAppropriateMaterial(aBlock, Material.leaves);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.GRAFTER : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
                : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
                EnumChatFormatting.RED + aEntity.getCommandSenderName()
                        + EnumChatFormatting.WHITE
                        + " has been trimmed by "
                        + EnumChatFormatting.GREEN
                        + aPlayer.getCommandSenderName()
                        + EnumChatFormatting.WHITE);
    }
}
