package gtPlusPlus.core.item.general.matterManipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.spongepowered.include.com.google.common.base.Objects;

import appeng.api.config.Actionable;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.GTMod;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PendingBlock;
import ic2.api.item.ElectricItem;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

public class PendingBuild {
    public LinkedList<PendingBlock> pendingBlocks;
    public EntityPlayer placingPlayer;
    public NBTState manipulator;
    public Future<LinkedList<PendingBlock>> assembleTask;

    private boolean printedProtectedBlockWarning = false;

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

        World world = placingPlayer.worldObj;

        ArrayList<ItemStack> drops = new ArrayList<>();
        ArrayList<FluidStack> fluidDrops = new ArrayList<>();

        while(list.size() < BLOCKS_PER_PLACE && pendingBlocks.size() > 0) {
            PendingBlock next = pendingBlocks.getFirst();

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

            if (!world.canMineBlock(player, x, y, z) || MinecraftServer.getServer().isBlockProtected(world, x, y, z, player)) {
                if (!printedProtectedBlockWarning) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Tried to break/place a block in a protected area!"));
                    printedProtectedBlockWarning = true;
                }

                pendingBlocks.removeFirst();
                continue;
            }

            if (!list.isEmpty() && !areBlocksBasicallyEqual(next, list.get(0))) {
                break;
            }

            @SuppressWarnings("null")
            Block block = next.block == null ? Blocks.air : next.block.field_150939_a;
            Block existing = world.getBlock(x, y, z);
            int existingMeta = world.getBlockMetadata(x, y, z);

            if(existing == block && existingMeta == next.metadata) {
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

                removeBlock(world, x, y, z, existing, existingMeta, drops, fluidDrops);
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

        ArrayList<ItemStack> splitDrops = new ArrayList<>();

        for(ItemStack drop : drops) {
            while(drop.stackSize > 64) {
                splitDrops.add(drop.splitStack(64));
            }

            if(drop.stackSize > 0) {
                splitDrops.add(drop);
            }
        }

        givePlayerStuff(splitDrops.toArray(new ItemStack[0]), fluidDrops.toArray(new FluidStack[0]));

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
        if (item.getItem() != null) {
            item.setTagCompound(list.get(0).nbt);
    
            if(!tryConsumeItems(item)) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not find item, it will be skipped. (" + item.stackSize + " x " + item.getDisplayName() + ")"));
                player.setItemInUse(null, 0);
                return;
            }
        }

        for(PendingBlock pending : list) {
            if (pending.block != null) {
                pending.block.placeBlockAt(
                    item,
                    player, player.worldObj,
                    pending.x, pending.y, pending.z,
                    -1, 0, 0, 0,
                    pending.metadata);
            }
        }

        if (list.size() > 0) {
            GTUtility.sendSoundToPlayers(
                world,
                SoundResource.MOB_ENDERMEN_PORTAL,
                1.0F,
                -1,
                (int) (player.posX + 0.5),
                (int) (player.posY + 0.5),
                (int) (player.posZ + 0.5));
        }
    }

    public boolean tryConsumeItems(ItemStack... items) {
        if(placingPlayer.capabilities.isCreativeMode) {
            return true;
        } else {
            if(consumeItemsFromPlayer(items, true)) {
                consumeItemsFromPlayer(items, false);

                return true;
            }

            if(consumeItemsFromAE(items, true)) {
                consumeItemsFromAE(items, false);

                return true;
            }

            return false;
        }
    }

    public void givePlayerStuff(ItemStack[] items, FluidStack[] fluids) {
        if (manipulator.encKey != null && !manipulator.hasMEConnection()) {
            manipulator.connectToMESystem();
        }

        boolean hasME = manipulator.hasMEConnection() && manipulator.canInteractWithAE(placingPlayer);

        if (items != null) {
            for (ItemStack item : items) {
                if (hasME) {
                    IAEItemStack result = manipulator.storageGrid.getItemInventory().injectItems(
                        AEItemStack.create(item),
                        Actionable.MODULATE,
                        new PlayerSource(placingPlayer, manipulator.securityTerminal));
        
                    if (result != null) {
                        item.stackSize = (int) result.getStackSize();
                    } else {
                        item.stackSize = 0;
                    }
                }
        
                if (item.stackSize <= 0) {
                    continue;
                }
    
                placingPlayer.inventory.addItemStackToInventory(item);
    
                if (item.stackSize <= 0) {
                    continue;
                }
    
                placingPlayer.worldObj.spawnEntityInWorld(new EntityItem(placingPlayer.worldObj, placingPlayer.posX, placingPlayer.posY, placingPlayer.posZ, item));
            }
        }

        if (fluids != null) {
            for (FluidStack fluid : fluids) {
                if (hasME) {
                    IAEFluidStack result = manipulator.storageGrid.getFluidInventory().injectItems(
                        AEFluidStack.create(fluid),
                        Actionable.MODULATE,
                        new PlayerSource(placingPlayer, manipulator.securityTerminal));
        
                    if (result != null) {
                        fluid.amount = (int) result.getStackSize();
                    } else {
                        fluid.amount = 0;
                    }
                }
    
                if (fluid.amount <= 0) {
                    continue;
                }
    
                ItemStack idealCell = inventoryStream(placingPlayer.inventory)
                    .filter(x -> (
                        x != null &&
                        x.getItem() instanceof IFluidContainerItem container &&
                        container.fill(x, fluid, false) == fluid.amount
                    ))
                    .findFirst()
                    .orElse(null);
    
                if (idealCell != null) {
                    fluid.amount -= ((IFluidContainerItem) idealCell.getItem()).fill(idealCell, fluid, true);
                    placingPlayer.inventory.markDirty();
                }
    
                if (fluid.amount <= 0) {
                    continue;
                }

                List<ItemStack> validCells = inventoryStream(placingPlayer.inventory)
                    .filter(x -> (
                        x != null &&
                        x.getItem() instanceof IFluidContainerItem container &&
                        container.fill(x, fluid, false) > 0
                    ))
                    .collect(Collectors.toList());
    
                for (ItemStack cell : validCells) {
                    fluid.amount -= ((IFluidContainerItem) cell.getItem()).fill(idealCell, fluid, true);
                    placingPlayer.inventory.markDirty();

                    if (fluid.amount <= 0) {
                        break;
                    }
                }

                if (fluid.amount > 0) {
                    placingPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Could not find a container for fluid (it was voided): " + fluid.amount + "L of " + fluid.getLocalizedName()));
                }
            }
        }
    }

    private Stream<ItemStack> inventoryStream(IInventory inv) {
        return IntStream.range(0, inv.getSizeInventory()).mapToObj(inv::getStackInSlot);
    }

    private ItemStack findFluidCell(int minCapacity) {
        for (int i = 0; i < placingPlayer.inventory.getSizeInventory(); i++) {
            ItemStack stack = placingPlayer.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof IFluidContainerItem fluidContainer && fluidContainer.getCapacity(stack) >= minCapacity) {
                return stack;
            }
        }

        return null;
    }

    private boolean consumeItemsFromPlayer(ItemStack[] items, boolean simulate) {
        for(int i = 0; i < items.length; i++) {
            items[i] = items[i].copy();
        }

        ItemStack[] inv = placingPlayer.inventory.mainInventory;

        if(simulate) {
            ItemStack[] temp = new ItemStack[inv.length];

            for(int i = 0; i < temp.length; i++) {
                temp[i] = ItemStack.copyItemStack(inv[i]);
            }

            inv = temp;
        }

        for(ItemStack item : items) {
            for(int i = 0; i < inv.length; i++) {
                ItemStack slot = inv[i];

                if (slot != null && areStacksBasicallyEqual(item, slot)) {
                    int toRemove = Math.min(slot.stackSize, item.stackSize);

                    slot.stackSize -= toRemove;
                    item.stackSize -= toRemove;

                    if(slot.stackSize == 0) {
                        inv[i] = null;
                    }
                }

                if(item.stackSize == 0) {
                    break;
                }
            }

            if(item.stackSize > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean consumeItemsFromAE(ItemStack[] items, boolean simulate) {
        if (manipulator.encKey == null) {
            return false;
        }

        if (!manipulator.hasMEConnection()) {
            if (!manipulator.connectToMESystem()) {
                return false;
            }
        }

        if (!manipulator.canInteractWithAE(placingPlayer)) {
            return false;
        }

        for (ItemStack item : items) {
            IAEItemStack result = manipulator.storageGrid.getItemInventory().extractItems(
                AEItemStack.create(item),
                simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                new PlayerSource(placingPlayer, manipulator.securityTerminal));

            if (result == null || result.getStackSize() == 0) {
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
            Field isBlockContainer = ReflectionHelper.findField(Block.class, "field_149758_A", "isBlockContainer");
            isBlockContainer.setAccessible(true);
            java.util.Objects.requireNonNull(isBlockContainer);
            IS_BLOCK_CONTAINER = MethodHandles.lookup().unreflectGetter(isBlockContainer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not find field Block.isBlockContainer", e);
        }
    }

    public boolean tryConsumePower(ItemStack stack, PendingBlock pendingBlock) {
        double euUsage = EU_PER_BLOCK;

        try {
            ItemBlock block = pendingBlock.block;
            if(block != null && (boolean)IS_BLOCK_CONTAINER.invoke(block.field_150939_a)) {
                euUsage *= TE_PENALTY;
            }
        } catch (Throwable e) {
            GTMod.GT_FML_LOGGER.error("Could not get Block.isBlockContainer field", e);
        }

        return tryConsumePower(stack, pendingBlock.x, pendingBlock.y, pendingBlock.z, euUsage);
    }

    public boolean tryConsumePower(ItemStack stack, double x, double y, double z, double euUsage) {
        euUsage *= placingPlayer.getDistanceSq(x, y, z);

        return ElectricItem.manager.use(stack, euUsage, placingPlayer);
    }

    private void removeBlock(World world, int x, int y, int z, Block existing, int existingMeta, ArrayList<ItemStack> pendingDrops, ArrayList<FluidStack> pendingFluidDrops) {
        TileEntity te = world.getTileEntity(x, y, z);

        List<ItemStack> drops = new ArrayList<>();

        emptyInventory(te, drops);
        emptyTank(te, pendingFluidDrops);
        removeCovers(te, drops);

        drops.addAll(existing.getDrops(world, x, y, z, existingMeta, 0));

        mergeStacks(pendingDrops, drops);

        world.setBlock(x, y, z, Blocks.air);
    }

    private void mergeStacks(List<ItemStack> dest, List<ItemStack> src) {
        for(ItemStack output : dest) {
            for(ItemStack input : src) {
                if (areStacksBasicallyEqual(input, output)) {
                    input.stackSize += output.stackSize;
                    output.stackSize = 0;
                }
            }
        }

        for (ItemStack input : src) {
            if (input.stackSize > 0) {
                dest.add(input);
            }
        }
    }

    private void emptyInventory(TileEntity te, List<ItemStack> drops) {
        if (te instanceof IInventory inv) {
            int size = inv.getSizeInventory();

            for (int i = 0; i < size; i++) {
                ItemStack stack = inv.getStackInSlot(i);

                if (stack != null && stack.getItem() != null) {
                    inv.setInventorySlotContents(i, null);

                    drops.add(stack);
                }
            }

            inv.markDirty();
        }
    }

    private void emptyTank(TileEntity te, List<FluidStack> drops) {
        if (te instanceof IFluidHandler handler) {
            FluidStack fluid = null;
            while ((fluid = handler.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true)) != null && fluid.getFluid() != null && fluid.amount > 0) {
                drops.add(fluid);
            }
        }
    }

    private void removeCovers(TileEntity te, List<ItemStack> drops) {
        if (te instanceof ICoverable coverable) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (coverable.getCoverIDAtSide(side) != 0) {
                    ItemStack cover = coverable.removeCoverAtSide(side, true);
    
                    if (cover != null) {
                        drops.add(cover);
                    }
                }
            }
        }
    }
}