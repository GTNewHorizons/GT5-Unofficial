package gtPlusPlus.core.item.general.matterManipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.spongepowered.include.com.google.common.base.Objects;

import gregtech.GT_Mod;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PendingBlock;
import ic2.api.item.ElectricItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class PendingBuild {
    public LinkedList<PendingBlock> pendingBlocks;
    public EntityPlayer placingPlayer;
    public NBTState manipulator;
    public Future<LinkedList<PendingBlock>> assembleTask;

    private static final int BLOCKS_PER_PLACE = 10;
    private static final int MAX_PLACE_DISTANCE = 512 * 512;
    private static final double EU_PER_BLOCK = 512.0, TE_PENALTY = 16.0;

    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        if(pendingBlocks == null) {
            if(assembleTask != null && assembleTask.isDone()) {
                try {
                    pendingBlocks = assembleTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    player.addChatMessage(new ChatComponentText("Error assembling blocks to place: " + e.getClass().getName() + ": " + e.getMessage()));
                    player.setItemInUse(null, 0);
                    return;
                }
            } else {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Determining which blocks to place..."));
                return;
            }
        }

        if(pendingBlocks.isEmpty()) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Finished placing blocks."));
            player.setItemInUse(null, 0);
            return;
        }

        ArrayList<PendingBlock> list = new ArrayList<>(BLOCKS_PER_PLACE);

        Integer lastChunkX = null, lastChunkZ = null;
        boolean anyUnplaceable = false;

        while(list.size() < BLOCKS_PER_PLACE && pendingBlocks.size() > 0) {
            var next = pendingBlocks.getFirst();

            if(placingPlayer.getDistanceSq(next.location.x, next.location.y, next.location.z) >= MAX_PLACE_DISTANCE) {
                pendingBlocks.removeFirst();
                continue;
            }

            int cx = next.location.x >> 4;
            int cz = next.location.z >> 4;

            if(!Objects.equal(cx, lastChunkX) || !Objects.equal(cz, lastChunkZ)) {
                if(!placingPlayer.worldObj.getChunkProvider().chunkExists(cx, cz)) {
                    pendingBlocks.removeFirst();
                    continue;
                } else {
                    lastChunkX = cx;
                    lastChunkZ = cz;
                }
            }

            if (list.get(0) != null && !areStacksBasicallyEqual(next.block, list.get(0).block)) {
                break;
            }

            // shuffle unplaceable blocks to the end of the queue
            if(!((ItemBlock)next.block.getItem()).field_150939_a.canPlaceBlockAt(placingPlayer.worldObj, next.location.x, next.location.y, next.location.z)) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                anyUnplaceable = true;
                continue;
            }

            if(!tryConsumePower(stack, next)) {
                break;
            }

            list.add(pendingBlocks.removeFirst());
        }

        if(list.isEmpty() && anyUnplaceable) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not place remaining blocks."));
            player.setItemInUse(null, 0);
            return;
        }

        ItemStack item = list.get(0).block.copy();
        item.stackSize = list.size();

        if(!tryConsumeItems(item)) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not find item: " + item.getDisplayName()));
            player.setItemInUse(null, 0);
            return;
        }

        for(var pendingBlock : list) {
            var block = ((ItemBlock)pendingBlock.block.getItem()).field_150939_a;

            int x = pendingBlock.location.x, y = pendingBlock.location.y, z = pendingBlock.location.z;

            placingPlayer.worldObj.setBlock(x, y, z, block, pendingBlock.block.getItemDamage(), 3);
        }
    }

    private boolean tryConsumeItems(ItemStack... items) {
        if(placingPlayer.capabilities.isCreativeMode) {
            return true;
        } else {
            if(!consumeItemsFromPlayer(items, true)) {
                return false;
            }

            return consumeItemsFromPlayer(items, false);
        }
    }

    private boolean consumeItemsFromPlayer(ItemStack[] items, boolean simulate) {
        for(int i = 0; i < items.length; i++) {
            items[i] = items[i].copy();
        }

        var inv = placingPlayer.inventory.mainInventory;

        if(!simulate) {
            var temp = new ItemStack[inv.length];

            for(int i = 0; i < temp.length; i++) {
                temp[i] = inv[i].copy();
            }
        }

        for(var item : items) {
            for(int i = 0; i < inv.length; i++) {
                var slot = inv[i];

                if(areStacksBasicallyEqual(item, slot)) {
                    int toRemove = Math.min(slot.stackSize, item.stackSize);

                    slot.stackSize -= toRemove;
                    item.stackSize -= toRemove;

                    if(slot.stackSize == 0) {
                        inv[i] = null;
                    }

                    if(item.stackSize == 0) {
                        break;
                    }
                }
            }

            if(item.stackSize > 0) {
                return false;
            }
        }

        return true;
    }

    private static boolean areStacksBasicallyEqual(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && ItemStack.areItemStackTagsEqual(a, b);
    }


    private static final MethodHandle IS_BLOCK_CONTAINER;

    static {
        try {
            Field isBlockContainer = Block.class.getDeclaredField("isBlockContainer");
            isBlockContainer.setAccessible(true);
            IS_BLOCK_CONTAINER = MethodHandles.lookup().unreflectGetter(isBlockContainer);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new RuntimeException("Could not find field Block.isBlockContainer", e);
        }
    }

    private boolean tryConsumePower(ItemStack stack, PendingBlock pendingBlock) {
        double euUsage = 1.0;

        euUsage *= placingPlayer.getDistanceSq(pendingBlock.location.x, pendingBlock.location.y, pendingBlock.location.z) * EU_PER_BLOCK;

        try {
            if((boolean)IS_BLOCK_CONTAINER.invoke(((ItemBlock)pendingBlock.block.getItem()).field_150939_a)) {
                euUsage *= TE_PENALTY;
            }
        } catch (Throwable e) {
            GT_Mod.GT_FML_LOGGER.error("Could not get Block.isBlockContainer field", e);
        }

        return ElectricItem.manager.use(stack, euUsage, placingPlayer);
    }
}