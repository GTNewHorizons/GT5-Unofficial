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

import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.api.util.GTUtility;

public class ToolBranchCutter extends GTTool {

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
        int aY, int aZ, int aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
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
            } else if (aBlock == GTUtility.getBlockFromStack(GTModHandler.getIC2Item("rubberLeaves", 1L))) {
                aDrops.clear();
                aDrops.add(GTModHandler.getIC2Item("rubberSapling", 1L));
            }
        }
        return 0;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "grafter")
            || GTToolHarvestHelper.isAppropriateMaterial(aBlock, Material.leaves);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_branchCutter]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
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
