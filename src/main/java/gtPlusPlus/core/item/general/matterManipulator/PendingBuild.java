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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class PendingBuild {
    public LinkedList<PendingBlock> pendingBlocks;
    public EntityPlayer placingPlayer;
    public NBTState manipulator;
    public Future<LinkedList<PendingBlock>> assembleTask;

    private static final int BLOCKS_PER_PLACE = 256;
    private static final int MAX_PLACE_DISTANCE = 512 * 512;
    private static final double EU_PER_BLOCK = 128.0, TE_PENALTY = 16.0;

    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        if(pendingBlocks == null) {
            if(assembleTask != null && assembleTask.isDone()) {
                try {
                    pendingBlocks = assembleTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "An error occurred while determining which blocks to place: " + e.getClass().getName() + ": " + e.getMessage()));
                    player.setItemInUse(null, 0);
                    return;
                }
            } else {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Determining which blocks to place..."));
                return;
            }
        }

        ArrayList<PendingBlock> list = new ArrayList<>(BLOCKS_PER_PLACE);

        Integer lastChunkX = null, lastChunkZ = null;
        int shuffleCount = 0;

        var world = placingPlayer.worldObj;

        ArrayList<ItemStack> drops = new ArrayList<>();

        while(list.size() < BLOCKS_PER_PLACE && pendingBlocks.size() > 0) {
            var next = pendingBlocks.getFirst();

            int x = next.x, y = next.y, z = next.z;

            if(placingPlayer.getDistanceSq(x, y, z) >= MAX_PLACE_DISTANCE) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if(shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            int chunkX = x >> 4;
            int chunkZ = z >> 4;

            if(!Objects.equal(chunkX, lastChunkX) || !Objects.equal(chunkZ, lastChunkZ)) {
                if(!world.getChunkProvider().chunkExists(chunkX, chunkZ)) {
                    pendingBlocks.removeFirst();
                    continue;
                } else {
                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                }
            }

            if (!list.isEmpty() && !areBlocksBasicallyEqual(next, list.get(0))) {
                break;
            }

            Block block = next.block;
            Block existing = world.getBlock(x, y, z);
            int existingMeta = world.getBlockMetadata(x, y, z);

            if(existing == block && existingMeta == next.metadata) {
                applyNBT(next);

                pendingBlocks.removeFirst();
                continue;
            }

            boolean canPlace = switch(manipulator.config.removeMode) {
                case NONE -> existing.isAir(world, x, y, z);
                case REPLACEABLE -> existing.isReplaceable(world, x, y, z);
                case ALL -> true;
            };

            if (!canPlace) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if(shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            if (!existing.isAir(world, x, y, z)) {
                if (!tryConsumePower(stack, new PendingBlock(world.provider.dimensionId, x, y, z, new ItemStack(existing, existingMeta)))) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
                    break;
                }

                removeBlock(world, x, y, z, existing, existingMeta, drops);
            }

            // shuffle unplaceable blocks to the end of the queue
            if(!block.canPlaceBlockAt(world, next.x, next.y, next.z)) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if(shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            if(!tryConsumePower(stack, next)) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
                break;
            }

            list.add(pendingBlocks.removeFirst());
        }

        if (placingPlayer.capabilities.isCreativeMode) {
            drops.clear();
        }

        ArrayList<ItemStack> splitDrops = new ArrayList<>();

        for(var drop : drops) {
            while(drop.stackSize > 64) {
                splitDrops.add(drop.splitStack(64));
            }

            if(drop.stackSize > 0) {
                splitDrops.add(drop);
            }
        }

        for(var drop : splitDrops) {
            if (!placingPlayer.inventory.addItemStackToInventory(drop)) {
                world.spawnEntityInWorld(new EntityItem(world, placingPlayer.posX, placingPlayer.posY, placingPlayer.posZ, drop));
            }
        }

        if(list.isEmpty()) {
            if(!pendingBlocks.isEmpty()) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not place " + pendingBlocks.size() + " remaining blocks."));
            } else {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Finished placing blocks."));
            }

            player.setItemInUse(null, 0);
            return;
        }

        ItemStack item = new ItemStack(list.get(0).block, list.size());
        item.setTagCompound(list.get(0).nbt);

        if(!tryConsumeItems(item)) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not find item: " + item.getDisplayName()));
            player.setItemInUse(null, 0);
            return;
        }

        for(var pending : list) {
            world.setBlock(pending.x, pending.y, pending.z, pending.block, pending.metadata, 3);
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

    private static boolean areBlocksBasicallyEqual(PendingBlock a, PendingBlock b) {
        return a.block == b.block && a.metadata == b.metadata && Objects.equal(a.nbt, b.nbt);
    }

    private static boolean areStacksBasicallyEqual(ItemStack a, ItemStack b) {
        if(a == null || b == null) {
            return a == null && b == null;
        }

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

        euUsage *= placingPlayer.getDistanceSq(pendingBlock.x, pendingBlock.y, pendingBlock.z) * EU_PER_BLOCK;

        try {
            if((boolean)IS_BLOCK_CONTAINER.invoke(pendingBlock.block)) {
                euUsage *= TE_PENALTY;
            }
        } catch (Throwable e) {
            GT_Mod.GT_FML_LOGGER.error("Could not get Block.isBlockContainer field", e);
        }

        return ElectricItem.manager.use(stack, euUsage, placingPlayer);
    }

    private void removeBlock(World world, int x, int y, int z, Block existing, int existingMeta, ArrayList<ItemStack> pendingDrops) {
        var drops = existing.getDrops(world, x, y, z, existingMeta, 0);

        for(var drop : drops) {
            for(var pending : pendingDrops) {
                if (areStacksBasicallyEqual(drop, pending)) {
                    pending.stackSize += drop.stackSize;
                    drop.stackSize = 0;
                }
            }

            if(drop.stackSize > 0) {
                pendingDrops.add(drop);
            }
        }

        world.setBlock(x, y, z, Blocks.air);
    }

    private void applyNBT(PendingBlock block) {

    }
}