package gregtech.common.tools;

import java.util.ArrayList;
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
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;

public class ToolSaw extends GTTool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public float getBaseDamage() {
        return 1.75F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.GTCEU_OP_SAW.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.GTCEU_OP_SAW.toString();
    }

    @Override
    public boolean isSaw() {
        return true;
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, int aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        if ((aBlock.getMaterial() == Material.leaves) && ((aBlock instanceof IShearable))) {
            aPlayer.worldObj.setBlock(aX, aY, aZ, aBlock, aMetaData, 0);
            if (((IShearable) aBlock).isShearable(aStack, aPlayer.worldObj, aX, aY, aZ)) {
                ArrayList<ItemStack> tDrops = ((IShearable) aBlock)
                    .onSheared(aStack, aPlayer.worldObj, aX, aY, aZ, aFortune);
                aDrops.clear();
                aDrops.addAll(tDrops);
                aEvent.dropChance = 1.0F;
            }
            aPlayer.worldObj.setBlock(aX, aY, aZ, Blocks.air, 0, 0);
        }
        return 0;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "axe", "saw")
            || GTToolHarvestHelper.isAppropriateMaterial(
                aBlock,
                Material.leaves,
                Material.vine,
                Material.wood,
                Material.cactus,
                Material.ice,
                Material.packedIce)
            || GTToolHarvestHelper.isSpecialBlock(aBlock, Blocks.ladder);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadSaw.getTextureIndex()]
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mIconSet.mTextures[MaterialIconRegistry.IconType.HANDLE_SAW
                .ordinal()];
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
                + " was getting cut down by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
