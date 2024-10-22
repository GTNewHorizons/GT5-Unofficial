package gregtech.common.items.matterManipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IRedstoneEmitter;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.entities.EntityItemLarge;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import gregtech.common.tileentities.machines.multi.MTEMMUplink;
import gregtech.common.tileentities.machines.multi.MTEMMUplink.UplinkStatus;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import ic2.api.item.ElectricItem;
import it.unimi.dsi.fastutil.Pair;

public abstract class AbstractBuildable implements IPseudoInventory, IBuildable {

    public EntityPlayer placingPlayer;
    public NBTState state;
    public ManipulatorTier tier;

    public final HashMap<ItemId, Long> pendingItems = new HashMap<>();
    public final HashMap<FluidId, Long> pendingFluids = new HashMap<>();

    private boolean printedUplinkWarning = false;

    protected static final double EU_PER_BLOCK = 128.0, TE_PENALTY = 16.0, EU_DISTANCE_EXP = 1.25;

    @Override
    public boolean tryConsumeItems(ItemStack... items) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return true;
        } else {
            ItemStackMap<Long> itemMap = GTUtility.getItemStackHistogram(Arrays.asList(items));

            consumeItemsFromPending(itemMap, true);
            consumeItemsFromPlayer(itemMap, true);
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
                consumeItemsFromAE(itemMap, true);
            }
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
                consumeItemsFromUplink(itemMap, true);
            }

            if (itemMap.values()
                .stream()
                .anyMatch(a -> a > 0)) return false;

            itemMap = GTUtility.getItemStackHistogram(Arrays.asList(items));

            consumeItemsFromPending(itemMap, false);
            consumeItemsFromPlayer(itemMap, false);
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
                consumeItemsFromAE(itemMap, false);
            }
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
                consumeItemsFromUplink(itemMap, false);
            }

            return true;
        }
    }

    public List<ItemStack> tryConsumeItemsAllowPartial(List<ItemStack> items) {
        if (placingPlayer.capabilities.isCreativeMode) {
            return new ArrayList<>();
        } else {
            ItemStackMap<Long> itemMap = GTUtility.getItemStackHistogram(items);

            consumeItemsFromPending(itemMap, false);
            consumeItemsFromPlayer(itemMap, false);
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
                consumeItemsFromAE(itemMap, false);
            }
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
                consumeItemsFromUplink(itemMap, false);
            }

            return GTUtility.getStacksOfSize(itemMap, Integer.MAX_VALUE);
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

        if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
            if (state.encKey != null && !state.hasMEConnection()) {
                state.connectToMESystem();
            }
        }

        boolean hasME = state.hasMEConnection() && state.canInteractWithAE(placingPlayer);

        if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
            if (state.uplinkAddress != null && !state.hasUplinkConnection()) {
                state.connectToUplink();
            }
        }

        MTEMMUplink uplink = state.uplink;

        pendingItems.forEach((item, amount) -> {
            if (hasME) {
                AEItemStack stack = AEItemStack.create(item.getItemStack());
                Objects.requireNonNull(stack);
                stack.setStackSize(amount);

                IAEItemStack result = state.storageGrid.getItemInventory()
                    .injectItems(stack, Actionable.MODULATE, new PlayerSource(placingPlayer, state.securityTerminal));

                if (result != null) {
                    amount = result.getStackSize();
                } else {
                    return;
                }
            }

            if (uplink != null) {
                IAEItemStack stack = AEItemStack.create(item.getItemStack());
                Objects.requireNonNull(stack);
                stack.setStackSize(amount);

                UplinkStatus status = uplink.tryGivePlayerItems(Arrays.asList(stack));

                if (status != UplinkStatus.OK && !printedUplinkWarning) {
                    printedUplinkWarning = true;
                    GTUtility.sendErrorToPlayer(placingPlayer, "Could not push items to uplink: " + status.toString());
                }

                amount = stack.getStackSize();
            }

            while (amount > 0) {
                ItemStack stack = item.getItemStack();

                int toRemove = (int) Math.min(amount, stack.getMaxStackSize());

                stack.stackSize = toRemove;
                amount -= toRemove;

                if (!placingPlayer.inventory.addItemStackToInventory(stack)) {
                    amount += toRemove;
                    break;
                } else {
                    amount += stack.stackSize;
                }
            }

            if (!placingPlayer.capabilities.isCreativeMode) {
                while (amount > 0) {
                    int toRemove = (int) Math.min(amount, Integer.MAX_VALUE);
                    amount -= toRemove;
                    placingPlayer.worldObj.spawnEntityInWorld(
                        new EntityItemLarge(
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

                IAEFluidStack result = state.storageGrid.getFluidInventory()
                    .injectItems(stack, Actionable.MODULATE, new PlayerSource(placingPlayer, state.securityTerminal));

                if (result != null) {
                    amount = result.getStackSize();
                } else {
                    return;
                }
            }

            if (uplink != null) {
                AEFluidStack stack = AEFluidStack.create(id.getFluidStack());
                stack.setStackSize(amount);

                UplinkStatus status = uplink.tryGivePlayerFluids(Arrays.asList(stack));

                if (status != UplinkStatus.OK && !printedUplinkWarning) {
                    printedUplinkWarning = true;
                    GTUtility.sendErrorToPlayer(placingPlayer, "Could not push fluids to uplink: " + status.toString());
                }

                amount += stack.getStackSize();
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
            if (amount == null || amount == 0) return 0l;

            Long available = pendingItems.get(ItemId.create(item));

            if (available == null || available == 0) return amount;

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
            if (amountLong == null || amountLong == 0) return 0l;

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
        if (state.encKey == null) {
            return;
        }

        if (!state.hasMEConnection()) {
            if (!state.connectToMESystem()) {
                return;
            }
        }

        if (!state.canInteractWithAE(placingPlayer)) {
            return;
        }

        requestedItems.replaceAll((item, amount) -> {
            if (amount == null || amount == 0) return 0l;

            IAEItemStack result = state.storageGrid.getItemInventory()
                .extractItems(
                    Objects.requireNonNull(AEItemStack.create(item))
                        .setStackSize(amount),
                    simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                    new PlayerSource(placingPlayer, state.securityTerminal));

            return result == null ? amount : (amount - result.getStackSize());
        });
    }

    private void consumeItemsFromUplink(ItemStackMap<Long> requestedItems, boolean simulate) {
        if (state.uplinkAddress == null) return;

        state.connectToUplink();

        if (!state.hasUplinkConnection()) return;

        MTEMMUplink uplink = state.uplink;

        UplinkStatus status = uplink.tryConsumeItems(requestedItems, simulate);

        if (status != UplinkStatus.OK && !printedUplinkWarning) {
            printedUplinkWarning = true;
            GTUtility.sendErrorToPlayer(placingPlayer, "Could not request items from uplink: " + status.toString());
        }
    }

    protected static boolean areBlocksBasicallyEqual(PendingBlock a, PendingBlock b) {
        return a.getBlock() == b.getBlock() && a.metadata == b.metadata;
    }

    protected static boolean areStacksBasicallyEqual(ItemStack a, ItemStack b) {
        if (a == null || b == null) {
            return a == null && b == null;
        }

        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage()
            && ItemStack.areItemStackTagsEqual(a, b);
    }

    protected static final MethodHandle IS_BLOCK_CONTAINER;

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
        double euUsage = EU_PER_BLOCK * pendingBlock.getBlock()
            .getBlockHardness(pendingBlock.getWorld(), pendingBlock.x, pendingBlock.y, pendingBlock.z);

        try {
            ItemBlock block = pendingBlock.getItem();
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

    protected void removeBlock(World world, int x, int y, int z, Block existing, int existingMeta) {
        TileEntity te = world.getTileEntity(x, y, z);

        emptySuperchest(te);
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

        world.setBlockToAir(x, y, z);
    }

    protected void emptySuperchest(TileEntity te) {
        if (te instanceof IGregTechTileEntity igte && igte.getMetaTileEntity() instanceof MTEDigitalChestBase dchest) {
            for (IAEItemStack stack : dchest.getStorageList()) {
                stack = dchest.extractItems(stack, Actionable.MODULATE, null);

                while (stack.getStackSize() > 0) {
                    ItemStack is = stack.getItemStack();
                    stack.setStackSize(stack.getStackSize() - is.stackSize);
                    givePlayerItems(is);
                }
            }
        }
    }

    protected void emptyTileInventory(TileEntity te) {
        if (te instanceof IInventory inv) {
            MMUtils.emptyInventory(this, inv);
        }
    }

    protected void emptyTank(TileEntity te) {
        if (te instanceof IFluidHandler handler) {
            FluidStack fluid = null;
            while ((fluid = handler.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, true)) != null
                && fluid.getFluid() != null
                && fluid.amount > 0) {
                givePlayerFluids(fluid);
            }
        }
    }

    protected void removeCovers(TileEntity te) {
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

    protected void resetAEMachine(Object machine) {
        if (machine instanceof ISegmentedInventory segmentedInventory) {
            IInventory upgrades = segmentedInventory.getInventoryByName("upgrades");

            if (upgrades != null) {
                MMUtils.emptyInventory(this, upgrades);
            }

            IInventory cells = segmentedInventory.getInventoryByName("cells");

            if (cells != null) {
                MMUtils.emptyInventory(this, cells);
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

    protected void resetKeptSettings(TileEntity te) {
        if (te instanceof IRedstoneEmitter emitter) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                emitter.setRedstoneOutputStrength(side, false);
            }
        }
    }

    private static class SoundInfo {

        private int eventCount;
        private double sumX, sumY, sumZ;
    }

    private final HashMap<Pair<SoundResource, World>, SoundInfo> pendingSounds = new HashMap<>();

    private static final Function<Pair<SoundResource, World>, SoundInfo> SOUND_INFO_CTOR = ignored -> new SoundInfo();

    protected void playSound(World world, int x, int y, int z, SoundResource sound) {
        Pair<SoundResource, World> pair = Pair.of(sound, world);

        SoundInfo info = pendingSounds.computeIfAbsent(pair, SOUND_INFO_CTOR);

        info.eventCount++;
        info.sumX += x;
        info.sumY += y;
        info.sumZ += z;
    }

    protected void playSounds() {
        pendingSounds.forEach((pair, info) -> {
            if (info.eventCount > 0) {
                GTUtility.sendSoundToPlayers(
                    pair.right(),
                    pair.left(),
                    5.0F,
                    -1,
                    (int) (info.sumX / info.eventCount),
                    (int) (info.sumY / info.eventCount),
                    (int) (info.sumZ / info.eventCount));
            }
        });
    }
}
