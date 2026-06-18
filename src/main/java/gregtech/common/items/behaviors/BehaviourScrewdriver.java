package gregtech.common.items.behaviors;

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

public class BehaviourScrewdriver extends BehaviourNone {

    private final int mVanillaCosts;
    private final int mEUCosts;

    public BehaviourScrewdriver(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
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
        int metadata = aMeta / 4 * 4 + (aMeta % 4 + 1) % 4;
        if ((aBlock == Blocks.unpowered_repeater) || (aBlock == Blocks.powered_repeater)) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, metadata, 3);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SCREWDRIVER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        if ((aBlock == Blocks.unpowered_comparator) || (aBlock == Blocks.powered_comparator)) {
            if (GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                aWorld.setBlockMetadataWithNotify(aX, aY, aZ, metadata, 3);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SCREWDRIVER, 1.0F, 1.0F, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }
}
