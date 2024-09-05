package gregtech.common.items.behaviors;

import static gregtech.api.enums.Mods.Railcraft;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class BehaviourCrowbar extends BehaviourNone {

    private final int mVanillaCosts;
    private final int mEUCosts;

    public BehaviourCrowbar(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        if (GTModHandler.getModItem(Railcraft.ID, "fluid.creosote.bucket", 1L) != null) {
            return false;
        }
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) {
            return false;
        }
        byte aMeta = (byte) aWorld.getBlockMetadata(aX, aY, aZ);
        if (aBlock == Blocks.rail) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, (aMeta + 1) % 10, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_BREAK, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        if ((aBlock == Blocks.detector_rail) || (aBlock == Blocks.activator_rail) || (aBlock == Blocks.golden_rail)) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                aWorld.isRemote = true;
                aWorld.setBlock(aX, aY, aZ, aBlock, aMeta / 8 * 8 + (aMeta % 8 + 1) % 6, 0);
                aWorld.isRemote = false;
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.RANDOM_BREAK, 1.0F, -1.0F, aX, aY, aZ);
            }
            return true;
        }
        return false;
    }
}
