package gregtech.common.items.behaviors;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.implementations.tiles.IColorableTile;
import appeng.api.util.AEColor;
import appeng.block.networking.BlockCableBus;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.SoundResource;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class Behaviour_Spray_Color extends Behaviour_None {

    private final ItemStack mEmpty;
    private final ItemStack mUsed;
    private final ItemStack mFull;
    private final long mUses;
    private final byte mColor;
    private final Collection<Block> mAllowedVanillaBlocks = Arrays.asList(
        Blocks.glass,
        Blocks.glass_pane,
        Blocks.stained_glass,
        Blocks.stained_glass_pane,
        Blocks.carpet,
        Blocks.hardened_clay);
    private final String mTooltip;
    private final String mTooltipUses = GT_LanguageManager
        .addStringLocalization("gt.behaviour.paintspray.uses", "Remaining Uses:");
    private final String mTooltipUnstackable = GT_LanguageManager
        .addStringLocalization("gt.behaviour.unstackable", "Not usable when stacked!");
    private final String mTooltipChain = GT_LanguageManager.addStringLocalization(
        "gt.behaviour.paintspray.chain",
        "If used while sneaking it will paint a chain of blocks");

    private final String mTooltipChainAmount = GT_LanguageManager.addStringLocalization(
        "gt.behaviour.paintspray.chain",
        "Paints up to %d blocks, in the direction you're looking at");

    public Behaviour_Spray_Color(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses, int aColor) {
        this.mEmpty = aEmpty;
        this.mUsed = aUsed;
        this.mFull = aFull;
        this.mUses = aUses;
        this.mColor = ((byte) aColor);
        this.mTooltip = GT_LanguageManager.addStringLocalization(
            "gt.behaviour.paintspray." + this.mColor + ".tooltip",
            "Can Color things in " + Dyes.get(this.mColor).mName);
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }
        boolean rOutput = false;
        if (!aPlayer.canPlayerEdit(aX, aY, aZ, side.ordinal(), aStack)) {
            return false;
        }
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        long tUses = tNBT.getLong("GT.RemainingPaint");
        if (GT_Utility.areStacksEqual(aStack, this.mFull, true)) {
            aStack.func_150996_a(this.mUsed.getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(this.mUsed));
            tUses = this.mUses;
        }
        int painted = 0;
        int maxPainted = GregTech_API.sSpecialFile.get(ConfigCategories.general, "SprayCanChainRange", 256);
        ForgeDirection lookSide;
        Vec3 look = aPlayer.getLookVec();
        double absX = Math.abs(look.xCoord);
        double absY = Math.abs(look.yCoord);
        double absZ = Math.abs(look.zCoord);
        if (absX > absY && absX > absZ) {
            lookSide = look.xCoord > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
        } else if (absY > absX && absY > absZ) {
            lookSide = look.yCoord > 0 ? ForgeDirection.UP : ForgeDirection.DOWN;
        } else {
            lookSide = look.zCoord > 0 ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
        }
        while ((GT_Utility.areStacksEqual(aStack, this.mUsed, true)) && (colorize(aWorld, aX, aY, aZ, side, aPlayer))) {
            GT_Utility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, 1.0F, aX, aY, aZ);
            if (!aPlayer.capabilities.isCreativeMode) {
                tUses -= 1L;
            }
            rOutput = true;
            painted++;
            if (painted >= maxPainted && maxPainted != -1) break;
            if (!aPlayer.isSneaking() || tUses <= 0) break;
            switch (lookSide) {
                case UP -> aY += 1;
                case DOWN -> aY -= 1;
                case NORTH -> aZ -= 1;
                case SOUTH -> aZ += 1;
                case WEST -> aX -= 1;
                case EAST -> aX += 1;
                default -> throw new IllegalArgumentException("Unexpected value: " + lookSide);
            }
        }
        tNBT.removeTag("GT.RemainingPaint");
        if (tUses > 0L) {
            tNBT.setLong("GT.RemainingPaint", tUses);
        }
        if (tNBT.hasNoTags()) {
            aStack.setTagCompound(null);
        } else {
            aStack.setTagCompound(tNBT);
        }
        if (tUses <= 0L) {
            if (this.mEmpty == null) {
                aStack.stackSize -= 1;
            } else {
                aStack.func_150996_a(this.mEmpty.getItem());
                Items.feather.setDamage(aStack, Items.feather.getDamage(this.mEmpty));
            }
        }
        return rOutput;
    }

    private boolean colorize(World aWorld, int aX, int aY, int aZ, ForgeDirection side, EntityPlayer player) {
        final Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock != Blocks.air) {
            if (this.mAllowedVanillaBlocks.contains(aBlock) || aBlock instanceof BlockColored) {
                if (aBlock == Blocks.hardened_clay) {
                    aWorld.setBlock(aX, aY, aZ, Blocks.stained_hardened_clay, (~this.mColor) & 0xF, 3);
                    return true;
                }
                if (aBlock == Blocks.glass_pane) {
                    aWorld.setBlock(aX, aY, aZ, Blocks.stained_glass_pane, (~this.mColor) & 0xF, 3);
                    return true;
                }
                if (aBlock == Blocks.glass) {
                    aWorld.setBlock(aX, aY, aZ, Blocks.stained_glass, (~this.mColor) & 0xF, 3);
                    return true;
                }
                if (aWorld.getBlockMetadata(aX, aY, aZ) == ((~this.mColor) & 0xF)) {
                    return false;
                }
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (~this.mColor) & 0xF, 3);
                return true;
            }

            if (aBlock instanceof IColorableTile) {
                return ((IColorableTile) aBlock).recolourBlock(side, AEColor.values()[(~this.mColor) & 0xF], player);
            }

            if (aBlock instanceof BlockCableBus) {
                return ((BlockCableBus) aBlock).recolourBlock(aWorld, aX, aY, aZ, side, (~this.mColor) & 0xF, player);
            }
        }
        return aBlock.recolourBlock(aWorld, aX, aY, aZ, side, (~this.mColor) & 0xF);
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        aList.add(this.mTooltipChain);
        aList.add(
            String.format(
                this.mTooltipChainAmount,
                GregTech_API.sSpecialFile.get(ConfigCategories.general, "SprayCanChainRange", 256)));
        NBTTagCompound tNBT = aStack.getTagCompound();
        long tRemainingPaint = tNBT == null ? this.mUses
            : GT_Utility.areStacksEqual(aStack, this.mFull, true) ? this.mUses : tNBT.getLong("GT.RemainingPaint");
        aList.add(this.mTooltipUses + " " + tRemainingPaint);
        aList.add(this.mTooltipUnstackable);
        return aList;
    }
}
