package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class BehaviourSoftHammer extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.softhammer", "Activates and Deactivates Machines");

    public BehaviourSoftHammer(int aCosts) {
        this.mCosts = aCosts;
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
        byte aMeta = (byte) aWorld.getBlockMetadata(aX, aY, aZ);
        if (aBlock == Blocks.lit_redstone_lamp) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, Blocks.redstone_lamp, 0, 0);
                aWorld.isRemote = false;
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        if (aBlock == Blocks.redstone_lamp) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, Blocks.lit_redstone_lamp, 0, 0);
                aWorld.isRemote = false;
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        if (aBlock == Blocks.golden_rail) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, (aMeta + 8) % 16, 0);
                aWorld.isRemote = false;
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        if (aBlock == Blocks.activator_rail) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, (aMeta + 8) % 16, 0);
                aWorld.isRemote = false;
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
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
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
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
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        if (aBlock == Blocks.hopper) {
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 1) % 6 != 1 ? (aMeta + 1) % 6 : 2, 3);
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
