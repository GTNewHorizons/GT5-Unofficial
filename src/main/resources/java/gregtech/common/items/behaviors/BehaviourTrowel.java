package gregtech.common.items.behaviors;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;

public class BehaviourTrowel extends BehaviourNone {

    @Override
    public List<String> getAdditionalToolTips(final MetaBaseItem aItem, final List<String> aList,
        final ItemStack aStack) {
        aList.add(StatCollector.translateToLocal("gt.behaviour.trowel.tooltip1"));
        aList.add(StatCollector.translateToLocal("gt.behaviour.trowel.tooltip2"));
        return aList;
    }

    @Override
    public boolean onItemUse(final MetaBaseItem aItem, final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX, final int aY, final int aZ, final int ordinalSide, final float hitX, final float hitY, final float hitZ) {
        if (null == aPlayer) {
            return false;
        }
        if (!(aItem instanceof MetaGeneratedTool)) {
            return false;
        }

        final ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (int i = 0; i < 9; i++) {
            if (i == aPlayer.inventory.currentItem) {
                continue;
            }

            final ItemStack candidate = aPlayer.inventory.getStackInSlot(i);
            if (isValidBlock(candidate)) {
                builder.add(i);
            }
        }
        final ImmutableList<Integer> candidates = builder.build();
        if (candidates.isEmpty()) {
            return false;
        }

        final int count = candidates.size();
        int chosenSlot;
        if (count == 1) {
            chosenSlot = candidates.get(0);
        } else {
            chosenSlot = candidates.get(ThreadLocalRandom.current().nextInt(count));
        }

        final ItemStack itemToPlace = aPlayer.inventory.getStackInSlot(chosenSlot);

        if (itemToPlace.getItem() instanceof final ItemBlock blockItem) {
            final Block blockToPlace = blockItem.field_150939_a;
            if (blockToPlace != null && blockToPlace.getMaterial().blocksMovement()) {
                final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
                final int newX = aX + side.offsetX;
                final int newY = aY + side.offsetY;
                final int newZ = aZ + side.offsetZ;
                final AxisAlignedBB blockBounds = AxisAlignedBB.getBoundingBox(newX, newY, newZ, newX + 1, newY + 1, newZ + 1);
                if (aPlayer.boundingBox.intersectsWith(blockBounds)) {
                    return false;
                }
            }
        }

        final int damage = itemToPlace.getItemDamage();
        final int stackSize = itemToPlace.stackSize;

        if (aWorld.isRemote) {
            // Do swing on client side
            return true;
        }

        if (aPlayer.capabilities.isCreativeMode || ((MetaGeneratedTool) aItem).doDamage(aStack, 100)) {
            // We can guarantee getItem() is non-null here because of the isValidBlock check done previously.
            //noinspection DataFlowIssue
            final boolean success = itemToPlace.getItem().onItemUse(itemToPlace, aPlayer, aWorld, aX, aY, aZ, ordinalSide, hitX, hitY, hitZ);

            if (aPlayer.capabilities.isCreativeMode) {
                itemToPlace.setItemDamage(damage);
                itemToPlace.stackSize = stackSize;
            }

            if (itemToPlace.stackSize < 1) {
                aPlayer.inventory.setInventorySlotContents(chosenSlot, null);
            }

            return success;
        }

        return false;
    }

    protected boolean isValidBlock(ItemStack aStack) {
        return aStack != null && aStack.getItem() instanceof ItemBlock && aStack.stackSize > 0;
    }
}
