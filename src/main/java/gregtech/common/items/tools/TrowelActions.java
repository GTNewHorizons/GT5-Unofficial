package gregtech.common.items.tools;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;

/**
 * The "right click to place a random hotbar block" half of a decorator's trowel, moved out of
 * {@code BehaviourTrowel}.
 */
public final class TrowelActions {

    private TrowelActions() {}

    /**
     * Places a random block from elsewhere on the player's hotbar, so a wall comes out speckled rather than uniform.
     *
     * @param pay charges the trowel for one placement, and reports whether it could be paid for.
     * @return whether the click was consumed.
     */
    public static boolean place(final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX,
        final int aY, final int aZ, final int ordinalSide, final float hitX, final float hitY, final float hitZ,
        final BooleanSupplier pay) {
        if (null == aPlayer) {
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

        if (pay.getAsBoolean()) {
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

    private static boolean isValidBlock(ItemStack aStack) {
        return aStack != null && aStack.getItem() instanceof ItemBlock && aStack.stackSize > 0;
    }
}
