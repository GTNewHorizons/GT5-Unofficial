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
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;

public class ToolChainsawLV extends ToolSaw {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 800;
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
    public String getCraftingSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_ONE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_TWO.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.IC2_TOOLS_CHAINSAW_CHAINSAW_USE_ONE.toString();
    }

    @Override
    public boolean isChainsaw() {
        return true;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "brrrr");
            GTMod.achievements.issueAchievement(aPlayer, "buildChainsaw");
        } catch (Exception ignored) {}
    }

    @Override
    public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, int aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        int rAmount = 0;
        if ((aBlock.getMaterial() == Material.leaves) && ((aBlock instanceof IShearable))) {
            aPlayer.worldObj.setBlock(aX, aY, aZ, aBlock, aMetaData, 0);
            if (((IShearable) aBlock).isShearable(aStack, aPlayer.worldObj, aX, aY, aZ)) {
                ((IShearable) aBlock).onSheared(aStack, aPlayer.worldObj, aX, aY, aZ, aFortune);
                aDrops.clear();
            }
            aPlayer.worldObj.setBlock(aX, aY, aZ, Blocks.air, 0, 0);
        } else if (((aBlock.getMaterial() == Material.ice) || (aBlock.getMaterial() == Material.packedIce))
            && (aDrops.isEmpty())) {
                aDrops.add(new ItemStack(aBlock, 1, aMetaData));
                aPlayer.worldObj.setBlockToAir(aX, aY, aZ);
                aEvent.dropChance = 1.0F;
                return 1;
            }
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
    public float getMiningSpeed(Block aBlock, int aMetaData, float aDefault, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ) {
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
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Steel.mRGBa;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadChainsaw
                .getTextureIndex()]
            : Textures.ItemIcons.POWER_UNIT_LV;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " was massacred by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }
}
