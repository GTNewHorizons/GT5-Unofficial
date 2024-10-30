package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEFluidStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.entities.EntityItemLarge;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.tileentities.machines.multi.MTEMMUplink;
import gregtech.common.tileentities.machines.multi.MTEMMUplink.UplinkStatus;
import it.unimi.dsi.fastutil.Pair;

public class MMInventory implements IPseudoInventory {

    public EntityPlayer player;
    public NBTState state;
    public ManipulatorTier tier;

    public final HashMap<ItemId, Long> pendingItems = new HashMap<>();
    public final HashMap<FluidId, Long> pendingFluids = new HashMap<>();

    private boolean printedUplinkWarning = false;

    public MMInventory(EntityPlayer player, NBTState state, ManipulatorTier tier) {
        this.player = player;
        this.state = state;
        this.tier = tier;
    }

    @Override
    public Pair<Boolean, List<IAEItemStack>> tryConsumeItems(List<IAEItemStack> items, int flags) {
        if ((flags & CONSUME_IGNORE_CREATIVE) == 0 && player.capabilities.isCreativeMode) {
            return Pair.of(true, items);
        } else {
            List<IAEItemStack> simulated = GTUtility.mapToList(items, IAEItemStack::copy);
            List<IAEItemStack> extracted = new ArrayList<>();

            consumeItemsFromPending(simulated, extracted, flags | CONSUME_SIMULATED);
            consumeItemsFromPlayer(simulated, extracted, flags | CONSUME_SIMULATED);
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
                consumeItemsFromAE(simulated, extracted, flags | CONSUME_SIMULATED);
            }
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
                consumeItemsFromUplink(simulated, extracted, flags | CONSUME_SIMULATED);
            }

            if ((flags & CONSUME_PARTIAL) == 0) {
                if (simulated.stream()
                    .anyMatch(s -> s.getStackSize() > 0)) {
                    return Pair.of(false, null);
                }
            }

            simulated = GTUtility.mapToList(items, IAEItemStack::copy);
            extracted = new ArrayList<>();

            consumeItemsFromPending(simulated, extracted, flags);
            consumeItemsFromPlayer(simulated, extracted, flags);
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
                consumeItemsFromAE(simulated, extracted, flags);
            }
            if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_UPLINK)) {
                consumeItemsFromUplink(simulated, extracted, flags);
            }

            ItemStackMap<IAEItemStack> out = new ItemStackMap<>(true);

            for (IAEItemStack ex : extracted) {
                ItemStack stack = ex.getItemStack();

                IAEItemStack merged = out.get(stack);

                if (merged == null) {
                    merged = Objects.requireNonNull(AEItemStack.create(stack));
                    merged.setStackSize(0);
                    out.put(stack, merged);
                }

                merged.incStackSize(ex.getStackSize());
            }

            return Pair.of(true, new ArrayList<>(out.values()));
        }
    }

    public void givePlayerItems(ItemStack... items) {
        if (player.capabilities.isCreativeMode) {
            return;
        }

        for (ItemStack item : items) {
            if (item != null && item.getItem() != null) {
                pendingItems.merge(ItemId.create(item), (long) item.stackSize, (Long a, Long b) -> a + b);
            }
        }
    }

    public void givePlayerFluids(FluidStack... fluids) {
        if (player.capabilities.isCreativeMode) {
            return;
        }

        for (FluidStack fluid : fluids) {
            if (fluid != null) {
                pendingFluids.merge(FluidId.create(fluid), (long) fluid.amount, (Long a, Long b) -> a + b);
            }
        }
    }

    public void actuallyGivePlayerStuff() {
        if (player.capabilities.isCreativeMode) {
            pendingItems.clear();
            pendingFluids.clear();
            return;
        }

        if (tier.hasCap(ItemMatterManipulator.CONNECTS_TO_AE)) {
            if (state.encKey != null && !state.hasMEConnection()) {
                state.connectToMESystem();
            }
        }

        boolean hasME = state.hasMEConnection() && state.canInteractWithAE(player);

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
                    .injectItems(stack, Actionable.MODULATE, new PlayerSource(player, state.securityTerminal));

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
                    GTUtility.sendErrorToPlayer(player, "Could not push items to uplink: " + status.toString());
                }

                amount = stack.getStackSize();
            }

            while (amount > 0) {
                ItemStack stack = item.getItemStack();

                int toRemove = (int) Math.min(amount, stack.getMaxStackSize());

                stack.stackSize = toRemove;
                amount -= toRemove;

                if (!player.inventory.addItemStackToInventory(stack)) {
                    amount += toRemove;
                    break;
                } else {
                    amount += stack.stackSize;
                }
            }

            if (!player.capabilities.isCreativeMode) {
                while (amount > 0) {
                    int toRemove = (int) Math.min(amount, Integer.MAX_VALUE);
                    amount -= toRemove;
                    player.worldObj.spawnEntityInWorld(
                        new EntityItemLarge(
                            player.worldObj,
                            player.posX,
                            player.posY,
                            player.posZ,
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
                    .injectItems(stack, Actionable.MODULATE, new PlayerSource(player, state.securityTerminal));

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
                    GTUtility.sendErrorToPlayer(player, "Could not push fluids to uplink: " + status.toString());
                }

                amount += stack.getStackSize();
            }

            // this is final because of the lambdas, but its amount field is updated several times
            final FluidStack fluid = id
                .getFluidStack(amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount.intValue());

            // spotless:off
            ItemStack idealCell = GTUtility.streamInventory(player.inventory)
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
            List<ItemStack> validCells = GTUtility.streamInventory(player.inventory)
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

            if (amount > 0 && !player.capabilities.isCreativeMode) {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.GOLD + "Could not find a container for fluid (it was voided): "
                            + amount
                            + "L of "
                            + fluid.getLocalizedName()));
            }
        });

        pendingFluids.clear();
    }

    private void consumeItemsFromPending(List<IAEItemStack> requestedItems, List<IAEItemStack> extractedItems,
        int flags) {
        boolean simulate = (flags & CONSUME_SIMULATED) != 0;
        boolean fuzzy = (flags & CONSUME_FUZZY) != 0;

        for (IAEItemStack req : requestedItems) {
            if (req.getStackSize() == 0) {
                continue;
            }

            if (!fuzzy) {
                ItemId id = ItemId.create(req.getItemStack());

                Long amtInPending = pendingItems.get(id);

                if (amtInPending == null || amtInPending == 0) {
                    continue;
                }

                long toRemove = Math.min(amtInPending, req.getStackSize());

                extractedItems.add(
                    req.copy()
                        .setStackSize(toRemove));
                amtInPending -= toRemove;
                req.decStackSize(toRemove);

                if (!simulate) {
                    if (amtInPending == 0) {
                        pendingItems.remove(id);
                    } else {
                        pendingItems.put(id, amtInPending);
                    }
                }
            } else {
                var iter = pendingItems.entrySet()
                    .iterator();

                while (iter.hasNext()) {
                    var e = iter.next();

                    if (e.getValue() == null || e.getValue() == 0) {
                        continue;
                    }

                    ItemStack stack = e.getKey()
                        .getItemStack();

                    if (stack.getItem() != req.getItem()) {
                        continue;
                    }

                    if (stack.getHasSubtypes() && Items.feather.getDamage(stack) != req.getItemDamage()) {
                        continue;
                    }

                    long amtInPending = e.getValue();
                    long toRemove = Math.min(amtInPending, req.getStackSize());

                    extractedItems.add(
                        req.copy()
                            .setStackSize(toRemove));
                    amtInPending -= toRemove;
                    req.decStackSize(toRemove);

                    if (!simulate) {
                        if (amtInPending == 0) {
                            iter.remove();
                        } else {
                            e.setValue(amtInPending);
                        }
                    }
                }
            }
        }
    }

    private void consumeItemsFromPlayer(List<IAEItemStack> requestedItems, List<IAEItemStack> extractedItems,
        int flags) {
        boolean simulate = (flags & CONSUME_SIMULATED) != 0;
        boolean fuzzy = (flags & CONSUME_FUZZY) != 0;

        ItemStack[] inv = player.inventory.mainInventory;

        for (IAEItemStack req : requestedItems) {
            if (req.getStackSize() == 0) {
                continue;
            }

            for (int i = 0; i < inv.length; i++) {
                ItemStack slot = inv[i];

                if (req.getStackSize() == 0) {
                    break;
                }

                if (slot == null || slot.getItem() == null || slot.stackSize == 0) {
                    continue;
                }

                ItemStack reqStack = req.getItemStack();

                if (fuzzy ? slot.isItemEqual(reqStack) : MMUtils.areStacksBasicallyEqual(slot, reqStack)) {
                    if (slot.stackSize == 111) {
                        extractedItems.add(
                            Objects.requireNonNull(AEItemStack.create(slot))
                                .setStackSize(req.getStackSize()));
                        req.setStackSize(0);
                    } else {
                        int toRemove = Math.min(slot.stackSize, reqStack.stackSize);

                        req.decStackSize(toRemove);
                        extractedItems.add(
                            Objects.requireNonNull(AEItemStack.create(slot))
                                .setStackSize(toRemove));

                        if (!simulate) {
                            slot.stackSize -= toRemove;
                            if (slot.stackSize == 0) {
                                inv[i] = null;
                            }
                            player.inventory.markDirty();
                        }
                    }
                }
            }
        }
    }

    private void consumeItemsFromAE(List<IAEItemStack> requestedItems, List<IAEItemStack> extractedItems, int flags) {
        boolean simulate = (flags & CONSUME_SIMULATED) != 0;
        boolean fuzzy = (flags & CONSUME_FUZZY) != 0;

        if (state.encKey == null) {
            return;
        }

        if (!state.hasMEConnection()) {
            if (!state.connectToMESystem()) {
                return;
            }
        }

        if (!state.canInteractWithAE(player)) {
            return;
        }

        for (IAEItemStack req : requestedItems) {
            if (req.getStackSize() == 0) {
                continue;
            }

            // spotless:off
            List<IAEItemStack> matches = fuzzy ?
                ImmutableList.copyOf(state.itemStorage.getStorageList().findFuzzy(req, FuzzyMode.IGNORE_ALL)) :
                Arrays.asList(state.itemStorage.getStorageList().findPrecise(req));
            // spotless:on

            for (IAEItemStack match : matches) {
                if (req.getStackSize() == 0) {
                    break;
                }

                if (match == null) {
                    continue;
                }

                match = match.copy()
                    .setStackSize(req.getStackSize());

                IAEItemStack result = state.itemStorage.extractItems(
                    match,
                    simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                    new PlayerSource(player, state.securityTerminal));

                if (result != null) {
                    extractedItems.add(result);
                    req.decStackSize(result.getStackSize());
                }
            }
        }
    }

    private void consumeItemsFromUplink(List<IAEItemStack> requestedItems, List<IAEItemStack> extractedItems,
        int flags) {
        boolean simulate = (flags & CONSUME_SIMULATED) != 0;
        boolean fuzzy = (flags & CONSUME_FUZZY) != 0;

        if (state.uplinkAddress == null) return;

        state.connectToUplink();

        if (!state.hasUplinkConnection()) return;

        MTEMMUplink uplink = state.uplink;

        var result = uplink.tryConsumeItems(requestedItems, simulate, fuzzy);

        if (result.left() != UplinkStatus.OK && !printedUplinkWarning) {
            printedUplinkWarning = true;
            GTUtility.sendErrorToPlayer(
                player,
                "Could not request items from uplink: " + result.left()
                    .toString());
        }

        if (result.right() != null) extractedItems.addAll(result.right());
    }

}
