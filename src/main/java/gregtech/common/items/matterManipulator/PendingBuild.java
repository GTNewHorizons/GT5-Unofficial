package gregtech.common.items.matterManipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.api.config.Actionable;
import appeng.api.implementations.tiles.ISegmentedInventory;
import appeng.api.networking.security.PlayerSource;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.helpers.ICustomNameObject;
import appeng.parts.AEBasePart;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.GTMod;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IRedstoneEmitter;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import ic2.api.item.ElectricItem;

public class PendingBuild {

    public LinkedList<PendingBlock> pendingBlocks;
    public EntityPlayer placingPlayer;
    public NBTState manipulator;
    public Future<LinkedList<PendingBlock>> assembleTask;

    public final HashMap<ItemId, Long> pendingItems = new HashMap<>();
    public final HashMap<FluidId, Long> pendingFluids = new HashMap<>();

    private boolean printedProtectedBlockWarning = false;

    private static final int BLOCKS_PER_PLACE = 256;
    private static final int MAX_PLACE_DISTANCE = 512 * 512;
    private static final double EU_PER_BLOCK = 128.0, TE_PENALTY = 16.0, EU_DISTANCE_EXP = 1.25;

    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        if (pendingBlocks == null) {
            if (assembleTask != null && assembleTask.isDone()) {
                try {
                    pendingBlocks = assembleTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    player.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "An error occurred while determining which blocks to place: "
                                + e.getClass()
                                    .getName()
                                + ": "
                                + e.getMessage()));
                    player.setItemInUse(null, 0);
                    return;
                }
            } else {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY
                            + "Determining which blocks to place..."));
                return;
            }
        }

        ArrayList<PendingBlock> list = new ArrayList<>(BLOCKS_PER_PLACE);

        Integer lastChunkX = null, lastChunkZ = null;
        int shuffleCount = 0;

        World world = placingPlayer.worldObj;

        while (list.size() < BLOCKS_PER_PLACE && pendingBlocks.size() > 0) {
            PendingBlock next = pendingBlocks.getFirst();

            int x = next.x, y = next.y, z = next.z;

            if (placingPlayer.getDistanceSq(x, y, z) >= MAX_PLACE_DISTANCE) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            int chunkX = x >> 4;
            int chunkZ = z >> 4;

            if (!Objects.equals(chunkX, lastChunkX) || !Objects.equals(chunkZ, lastChunkZ)) {
                if (!world.getChunkProvider()
                    .chunkExists(chunkX, chunkZ)) {
                    pendingBlocks.removeFirst();
                    continue;
                } else {
                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                }
            }

            // spotless:off
            if (!world.canMineBlock(player, x, y, z) || MinecraftServer.getServer().isBlockProtected(world, x, y, z, player)) {
                // spotless:on
                if (!printedProtectedBlockWarning) {
                    player.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.GOLD + "Tried to break/place a block in a protected area!"));
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

            if (existing == block && existingMeta == next.metadata) {
                pendingBlocks.removeFirst();
                continue;
            }

            boolean canPlace = switch (manipulator.config.removeMode) {
                case NONE -> existing.isAir(world, x, y, z);
                case REPLACEABLE -> existing.isReplaceable(world, x, y, z);
                case ALL -> true;
            };

            if (!canPlace) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            if (!existing.isAir(world, x, y, z)) {
                PendingBlock toRemove = new PendingBlock(
                    world.provider.dimensionId,
                    x,
                    y,
                    z,
                    new ItemStack(existing, existingMeta));
                if (!tryConsumePower(stack, toRemove)) {
                    player.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
                    break;
                }

                removeBlock(world, x, y, z, existing, existingMeta);
            }

            if (!block.canPlaceBlockAt(world, next.x, next.y, next.z)) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            if (!tryConsumePower(stack, next)) {
                player.addChatMessage(
                    new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
                break;
            }

            list.add(pendingBlocks.removeFirst());
        }

        actuallyGivePlayerStuff();

        if (list.isEmpty()) {
            if (!pendingBlocks.isEmpty()) {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.RED + "Could not place " + pendingBlocks.size() + " remaining blocks."));
            } else {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Finished placing blocks."));
            }

            player.setItemInUse(null, 0);
            return;
        }

        ItemStack item = new ItemStack(list.get(0).block, list.size());
        if (item.getItem() != null) {
            item.setTagCompound(list.get(0).nbt);

            if (!tryConsumeItems(item)) {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.RED + "Could not find item, it will be skipped. ("
                            + item.stackSize
                            + " x "
                            + item.getDisplayName()
                            + ")"));
                player.setItemInUse(null, 0);
                return;
            }
        }

        double avgX = 0, avgY = 0, avgZ = 0;

        int n = list.size();

        for (PendingBlock pending : list) {
            avgX += pending.x / (double) n;
            avgY += pending.y / (double) n;
            avgZ += pending.z / (double) n;

            ItemBlock block = pending.block;
            if (block != null) {
                block.placeBlockAt(
                    item,
                    player,
                    player.worldObj,
                    pending.x,
                    pending.y,
                    pending.z,
                    0,
                    0,
                    0,
                    0,
                    pending.metadata);
            }
        }

        if (n > 0) {
            GTUtility.sendSoundToPlayers(
                world,
                SoundResource.MOB_ENDERMEN_PORTAL,
                5.0F,
                -1,
                (int) avgX,
                (int) avgY,
                (int) avgZ);
        }
    }

    public boolean tryConsumeItems(ItemStack... items) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return true;
        } else {
            ItemStackMap<Long> itemMap = GTUtility.getItemStackHistogram(items);

            consumeItemsFromPending(itemMap, true);
            consumeItemsFromPlayer(itemMap, true);
            consumeItemsFromAE(itemMap, true);

            if (itemMap.values()
                .stream()
                .anyMatch(a -> a > 0)) return false;

            itemMap = GTUtility.getItemStackHistogram(items);

            consumeItemsFromPending(itemMap, false);
            consumeItemsFromPlayer(itemMap, false);
            consumeItemsFromAE(itemMap, false);

            return true;
        }
    }

    public void givePlayerItems(ItemStack... items) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return;
        }

        for (ItemStack item : items) {
            if (item != null) {
                pendingItems.merge(ItemId.create(item), (long) item.stackSize, (Long a, Long b) -> a + b);
            }
        }
    }

    public void givePlayerFluids(FluidStack... fluids) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return;
        }

        for (FluidStack fluid : fluids) {
            if (fluid != null) {
                pendingFluids.merge(FluidId.create(fluid), (long) fluid.amount, (Long a, Long b) -> a + b);
            }
        }
    }

    public void actuallyGivePlayerStuff() {
        if (placingPlayer.capabilities.isCreativeMode) {
            pendingItems.clear();
            pendingFluids.clear();
            return;
        }

        if (manipulator.encKey != null && !manipulator.hasMEConnection()) {
            manipulator.connectToMESystem();
        }

        boolean hasME = manipulator.hasMEConnection() && manipulator.canInteractWithAE(placingPlayer);

        pendingItems.forEach((item, amount) -> {
            if (hasME) {
                AEItemStack stack = AEItemStack.create(item.getItemStack());
                Objects.requireNonNull(stack);
                stack.setStackSize(amount);

                IAEItemStack result = manipulator.storageGrid.getItemInventory()
                    .injectItems(
                        stack,
                        Actionable.MODULATE,
                        new PlayerSource(placingPlayer, manipulator.securityTerminal));

                if (result != null) {
                    amount = result.getStackSize();
                } else {
                    return;
                }
            }

            while (amount > 0) {
                ItemStack stack = item.getItemStack();

                int toRemove = (int) Math.min(amount, stack.getMaxStackSize());

                stack.stackSize = toRemove;
                amount -= toRemove;

                if (!placingPlayer.inventory.addItemStackToInventory(stack)) {
                    break;
                } else {
                    amount += stack.stackSize;
                }
            }

            if (!placingPlayer.capabilities.isCreativeMode) {
                while (amount > 0) {
                    int toRemove = amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount.intValue();
                    amount -= toRemove;
                    placingPlayer.worldObj.spawnEntityInWorld(
                        new EntityItem(
                            placingPlayer.worldObj,
                            placingPlayer.posX,
                            placingPlayer.posY,
                            placingPlayer.posZ,
                            item.getItemStack(toRemove)));
                }
            }
        });

        pendingItems.clear();

        pendingFluids.forEach((id, amount) -> {
            if (hasME) {
                AEFluidStack stack = AEFluidStack.create(id.getFluidStack());
                stack.setStackSize(amount);

                IAEFluidStack result = manipulator.storageGrid.getFluidInventory()
                    .injectItems(
                        stack,
                        Actionable.MODULATE,
                        new PlayerSource(placingPlayer, manipulator.securityTerminal));

                if (result != null) {
                    amount = result.getStackSize();
                } else {
                    return;
                }
            }

            // this is final because of the lambdas, but its amount field is updated several times
            final FluidStack fluid = id
                .getFluidStack(amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount.intValue());

            // spotless:off
            ItemStack idealCell = GTUtility.streamInventory(placingPlayer.inventory)
                .sorted(Comparator.comparingInt((ItemStack x) -> (
                    x != null && x.getItem() instanceof IFluidContainerItem container ? container.getCapacity(x) : 0
                )))
                .filter(x -> (
                    x != null &&
                    x.getItem() instanceof IFluidContainerItem container &&
                    container.fill(x, fluid, false) == fluid.amount
                ))
                .findFirst()
                .orElse(null);
            // spotless:on

            if (idealCell != null) {
                amount -= ((IFluidContainerItem) idealCell.getItem()).fill(idealCell, fluid.copy(), true);
            }

            if (amount <= 0) {
                return;
            }

            fluid.amount = amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount.intValue();

            // spotless:off
            List<ItemStack> validCells = GTUtility.streamInventory(placingPlayer.inventory)
                .filter(x -> (
                    x != null &&
                    x.getItem() instanceof IFluidContainerItem container &&
                    container.fill(x, fluid, false) > 0
                ))
                .collect(Collectors.toList());
            // spotless:on

            for (ItemStack cell : validCells) {
                fluid.amount = amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount.intValue();
                amount -= ((IFluidContainerItem) cell.getItem()).fill(idealCell, fluid.copy(), true);

                if (amount <= 0) {
                    return;
                }
            }

            if (amount > 0 && !placingPlayer.capabilities.isCreativeMode) {
                placingPlayer.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.GOLD + "Could not find a container for fluid (it was voided): "
                            + amount
                            + "L of "
                            + fluid.getLocalizedName()));
            }
        });

        pendingFluids.clear();
    }

    private void consumeItemsFromPending(ItemStackMap<Long> requestedItems, boolean simulate) {
        requestedItems.replaceAll((item, amount) -> {
            if (amount == 0) return 0l;

            long available = pendingItems.get(ItemId.create(item));

            long toRemove = Math.min(available, amount);

            available -= toRemove;
            amount -= toRemove;

            if (!simulate) {
                if (available == 0) {
                    pendingItems.remove(ItemId.create(item));
                } else {
                    pendingItems.put(ItemId.create(item), available);
                }
            }

            return amount;
        });
    }

    private void consumeItemsFromPlayer(ItemStackMap<Long> requestedItems, boolean simulate) {
        ItemStack[] inv = placingPlayer.inventory.mainInventory;

        requestedItems.replaceAll((item, amountLong) -> {
            if (amountLong == 0) return 0l;

            int remaining = amountLong > Integer.MAX_VALUE ? Integer.MAX_VALUE : amountLong.intValue();
            int initial = remaining;

            for (int i = 0; i < inv.length; i++) {
                ItemStack slot = inv[i];

                if (slot != null && areStacksBasicallyEqual(item, slot)) {
                    int toRemove = Math.min(slot.stackSize, remaining);

                    remaining -= toRemove;

                    if (!simulate) {
                        slot.stackSize -= toRemove;
                        if (slot.stackSize == 0) {
                            inv[i] = null;
                        }
                    }
                }
            }

            int provided = initial - remaining;
            return amountLong - provided;
        });
    }

    private void consumeItemsFromAE(ItemStackMap<Long> requestedItems, boolean simulate) {
        if (manipulator.encKey == null) {
            return;
        }

        if (!manipulator.hasMEConnection()) {
            if (!manipulator.connectToMESystem()) {
                return;
            }
        }

        if (!manipulator.canInteractWithAE(placingPlayer)) {
            return;
        }

        requestedItems.replaceAll((item, amount) -> {
            if (amount == 0) return 0l;

            IAEItemStack result = manipulator.storageGrid.getItemInventory()
                .extractItems(
                    Objects.requireNonNull(AEItemStack.create(item))
                        .setStackSize(amount),
                    simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                    new PlayerSource(placingPlayer, manipulator.securityTerminal));

            return result == null ? amount : (amount - result.getStackSize());
        });
    }

    private static boolean areBlocksBasicallyEqual(PendingBlock a, PendingBlock b) {
        return a.block == b.block && a.metadata == b.metadata && Objects.equals(a.nbt, b.nbt);
    }

    private static boolean areStacksBasicallyEqual(ItemStack a, ItemStack b) {
        if (a == null || b == null) {
            return a == null && b == null;
        }

        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage()
            && ItemStack.areItemStackTagsEqual(a, b);
    }

    private static final MethodHandle IS_BLOCK_CONTAINER;

    static {
        try {
            Field isBlockContainer = ReflectionHelper.findField(Block.class, "field_149758_A", "isBlockContainer");
            isBlockContainer.setAccessible(true);
            Objects.requireNonNull(isBlockContainer);
            IS_BLOCK_CONTAINER = MethodHandles.lookup()
                .unreflectGetter(isBlockContainer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not find field Block.isBlockContainer", e);
        }
    }

    public boolean tryConsumePower(ItemStack stack, PendingBlock pendingBlock) {
        double euUsage = EU_PER_BLOCK;

        try {
            ItemBlock block = pendingBlock.block;
            if (block != null && (boolean) IS_BLOCK_CONTAINER.invoke(block.field_150939_a)) {
                euUsage *= TE_PENALTY;
            }
        } catch (Throwable e) {
            GTMod.GT_FML_LOGGER.error("Could not get Block.isBlockContainer field", e);
        }

        return tryConsumePower(stack, pendingBlock.x, pendingBlock.y, pendingBlock.z, euUsage);
    }

    public boolean tryConsumePower(ItemStack stack, double x, double y, double z, double euUsage) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return true;
        }

        euUsage *= Math.pow(placingPlayer.getDistance(x, y, z), EU_DISTANCE_EXP);

        return ElectricItem.manager.use(stack, euUsage, placingPlayer);
    }

    private void removeBlock(World world, int x, int y, int z, Block existing, int existingMeta) {
        TileEntity te = world.getTileEntity(x, y, z);

        emptyTileInventory(te);
        emptyTank(te);
        removeCovers(te);
        resetAEMachine(te);
        resetKeptSettings(te);

        if (existing instanceof IFluidBlock fluidBlock && fluidBlock.canDrain(world, x, y, z)) {
            givePlayerFluids(fluidBlock.drain(world, x, y, z, true));
        } else if (existing == Blocks.water || existing == Blocks.lava) {
            givePlayerFluids(new FluidStack(existing == Blocks.water ? FluidRegistry.WATER : FluidRegistry.LAVA, 1000));
        } else {
            givePlayerItems(
                existing.getDrops(world, x, y, z, existingMeta, 0)
                    .toArray(new ItemStack[0]));
        }

        world.setBlock(x, y, z, Blocks.air);
    }

    private void emptyTileInventory(TileEntity te) {
        if (te instanceof IInventory inv) {
            emptyInventory(inv);
        }
    }

    private void emptyInventory(IInventory inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                inv.setInventorySlotContents(i, null);

                givePlayerItems(stack);
            }
        }

        inv.markDirty();
    }

    private void emptyTank(TileEntity te) {
        if (te instanceof IFluidHandler handler) {
            FluidStack fluid = null;
            while ((fluid = handler.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true)) != null
                && fluid.getFluid() != null
                && fluid.amount > 0) {
                givePlayerFluids(fluid);
            }
        }
    }

    private void removeCovers(TileEntity te) {
        if (te instanceof ICoverable coverable) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (coverable.getCoverIDAtSide(side) != 0) {
                    ItemStack cover = coverable.removeCoverAtSide(side, true);

                    if (cover != null && cover.getItem() != null) {
                        givePlayerItems(cover);
                    }
                }
            }
        }
    }

    private void resetAEMachine(Object machine) {
        if (machine instanceof ISegmentedInventory segmentedInventory) {
            IInventory upgrades = segmentedInventory.getInventoryByName("upgrades");

            if (upgrades != null) {
                emptyInventory(upgrades);
            }

            IInventory cells = segmentedInventory.getInventoryByName("cells");

            if (cells != null) {
                emptyInventory(cells);
            }
        }

        if (machine instanceof ICustomNameObject customName) {
            if (customName.hasCustomName()) {
                try {
                    customName.setCustomName(null);
                } catch (IllegalArgumentException e) {
                    // hack because AEBasePart's default setCustomName impl throws an IAE when the name is null
                    if (machine instanceof AEBasePart basePart) {
                        NBTTagCompound tag = basePart.getItemStack()
                            .getTagCompound();

                        if (tag != null) {
                            tag.removeTag("display");

                            if (tag.hasNoTags()) {
                                basePart.getItemStack()
                                    .setTagCompound(null);
                            }
                        }
                    }
                }
            }
        }

        if (machine instanceof IPartHost host) {
            // intentionally includes UNKNOWN to remove any cables
            for (ForgeDirection dir : ForgeDirection.values()) {
                IPart part = host.getPart(dir);

                if (part != null) {
                    resetAEMachine(part);

                    host.removePart(dir, false);

                    givePlayerItems(part.getItemStack(PartItemStack.Break));

                    ArrayList<ItemStack> drops = new ArrayList<>();
                    part.getDrops(drops, false);

                    if (!drops.isEmpty()) {
                        givePlayerItems(drops.toArray(new ItemStack[drops.size()]));
                    }
                }
            }
        }
    }

    private void resetKeptSettings(TileEntity te) {
        if (te instanceof IRedstoneEmitter emitter) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                emitter.setRedstoneOutputStrength(side, false);
            }
        }
    }
}
