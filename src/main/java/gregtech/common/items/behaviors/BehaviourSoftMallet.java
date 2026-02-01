package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTUtility;

public class BehaviourSoftMallet extends BehaviourNone {

    public static final String NBT_SOFT_MALLET_MODE = "gt.soft_mallet_mode";
    public static final int SOFT_MALLET_MODE_TOGGLE = 0;
    public static final int SOFT_MALLET_MODE_ALWAYS_ON = 1;
    public static final int SOFT_MALLET_MODE_ALWAYS_OFF = 2;
    private final int mCosts;

    public BehaviourSoftMallet(int aCosts) {
        this.mCosts = aCosts;
    }

    public static int getMode(ItemStack stack) {
        if (stack == null) return SOFT_MALLET_MODE_TOGGLE;
        if (!stack.hasTagCompound()) return SOFT_MALLET_MODE_TOGGLE;
        int mode = stack.getTagCompound()
            .getInteger(NBT_SOFT_MALLET_MODE);
        return (mode >= SOFT_MALLET_MODE_TOGGLE && mode <= SOFT_MALLET_MODE_ALWAYS_OFF) ? mode
            : SOFT_MALLET_MODE_TOGGLE;
    }

    public static void setMode(ItemStack stack, int mode) {
        if (stack == null) return;
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound()
            .setInteger(NBT_SOFT_MALLET_MODE, mode);
    }

    public static int cycleMode(int current) {
        return (current + 1) % 3;
    }

    public static String getModeText(int mode) {
        return switch (mode) {
            case SOFT_MALLET_MODE_ALWAYS_ON -> "gt.softmallet.mode.1";
            case SOFT_MALLET_MODE_ALWAYS_OFF -> "gt.softmallet.mode.2";
            default -> "gt.softmallet.mode.0";
        };
    }

    private static void cycleModeWithFeedback(EntityPlayer player, World world, ItemStack stack, double sx, double sy,
        double sz) {
        int next = cycleMode(getMode(stack));
        setMode(stack, next);
        GTUtility.sendChatTrans(player, getModeText(next));
        GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, sx, sy, sz);
    }

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (!aWorld.isRemote && aPlayer.isSneaking()) {
            cycleModeWithFeedback(aPlayer, aWorld, aStack, aPlayer.posX, aPlayer.posY, aPlayer.posZ);
        }
        return aStack;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) {
            return false;
        }
        int aMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        if (aBlock == Blocks.lit_redstone_lamp) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, Blocks.redstone_lamp, 0, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if (aBlock == Blocks.redstone_lamp) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, Blocks.lit_redstone_lamp, 0, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if (aBlock == Blocks.golden_rail) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, (aMeta + 8) % 16, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if (aBlock == Blocks.activator_rail) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, (aMeta + 8) % 16, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if ((aBlock == Blocks.log) || (aBlock == Blocks.log2) || (aBlock == Blocks.hay_block)) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 4) % 12, 3);
            }
            return true;
        }
        if ((aBlock == Blocks.piston) || (aBlock == Blocks.sticky_piston)
            || (aBlock == Blocks.dispenser)
            || (aBlock == Blocks.dropper)) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 1) % 6, 3);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if ((aBlock == Blocks.pumpkin) || (aBlock == Blocks.lit_pumpkin)
            || (aBlock == Blocks.furnace)
            || (aBlock == Blocks.lit_furnace)
            || (aBlock == Blocks.chest)
            || (aBlock == Blocks.trapped_chest)) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta - 1) % 4 + 2, 3);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if (aBlock == Blocks.hopper) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 1) % 6 != 1 ? (aMeta + 1) % 6 : 2, 3);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(StatCollector.translateToLocal("gt.softmallet.tooltip"));
        aList.add(StatCollector.translateToLocal("gt.softmallet.tooltip.mode"));
        return aList;
    }
}
