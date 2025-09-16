package gregtech.api.metatileentity.implementations;

import static gregtech.api.util.GTUtility.isStackValid;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.util.ProxiedItemHandlerModifiable;

public class MTEHatchInputBusCompressed extends MTEHatchInputBus implements IMEMonitor<IAEItemStack> {

    public final int slotCount;
    public final int stackCapacity;

    private final IItemHandlerModifiable proxiedHandler;

    private final Map<IMEMonitorHandlerReceiver<IAEItemStack>, Object> listeners = new HashMap<>();

    public MTEHatchInputBusCompressed(int id, String name, String nameRegional, int tier, int slots,
        int stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            0,
            ArrayExt.of(
                "Item Input for Multiblocks",
                "Shift + right click with screwdriver to turn Sort mode on/off",
                "Capacity: " + slots + " slots, " + GTUtility.formatNumbers(stackCapacity) + " stacks each",
                "Stores more than 1 stack per slot",
                "Items cannot be extracted or inserted via the GUI"));

        this.slotCount = slots;
        this.stackCapacity = stackCapacity;
        this.proxiedHandler = null;
    }

    protected MTEHatchInputBusCompressed(MTEHatchInputBusCompressed prototype) {
        super(
            prototype.mName,
            prototype.mTier,
            prototype.slotCount + 1,
            prototype.mDescriptionArray,
            prototype.mTextures);
        this.slotCount = prototype.slotCount;
        this.stackCapacity = prototype.stackCapacity;
        this.proxiedHandler = new ProxiedItemHandlerModifiable(inventoryHandler) {

            @Override
            public int getSlotLimit(int slot) {
                return 64 * stackCapacity;
            }
        };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBusCompressed(this);
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return proxiedHandler;
    }

    @Override
    public int getCircuitSlot() {
        return slotCount;
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return GTUtility.longToInt((stack == null ? 64 : stack.getMaxStackSize()) * (long) stackCapacity);
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        ItemList list = new ItemList();

        for (int i = 0; i < mInventory.length - 1; i++) {
            ItemStack stack = mInventory[i];
            if (isStackValid(stack)) list.add(AEItemStack.create(stack));
        }

        return list;
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> l, Object verificationToken) {
        listeners.put(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> l) {
        listeners.remove(l);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEItemStack input) {
        return false;
    }

    @Override
    public boolean canAccept(IAEItemStack input) {
        ItemStack stack = input.getItemStack();

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        for (int i = 0; i < slotCount; i++) {
            if (allowPutStack(igte, i, ForgeDirection.UNKNOWN, stack)) return true;
        }

        return false;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return i == 2;
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource src) {
        IAEItemStack inserted = null;

        if (disableLimited) {
            // First pass: try to find slots that have the item already
            for (int i = 0; i < slotCount && input.getStackSize() > 0; i++) {
                ItemStack slot = mInventory[i];

                if (!isStackValid(slot)) continue;

                if (input.isSameType(slot)) {
                    int maxStack = getStackSizeLimit(i, slot);

                    int remaining = maxStack - slot.stackSize;
                    int toTransfer = Math.min(remaining, GTUtility.longToInt(input.getStackSize()));

                    if (toTransfer > 0) {
                        input.decStackSize(toTransfer);
                        if (mode == Actionable.MODULATE) slot.stackSize += toTransfer;

                        if (inserted == null) inserted = input.copy()
                            .setStackSize(0);

                        inserted.incStackSize(toTransfer);
                    }
                }
            }

            // Second pass: try to find any empty slot
            for (int i = 0; i < slotCount && input.getStackSize() > 0; i++) {
                ItemStack slot = mInventory[i];

                if (isStackValid(slot)) continue;

                ItemStack stack = input.getItemStack();
                stack.stackSize = 0;
                slot = stack;
                if (mode == Actionable.MODULATE) mInventory[i] = stack;

                int maxStack = getStackSizeLimit(i, slot);

                int remaining = maxStack - slot.stackSize;
                int toTransfer = Math.min(remaining, GTUtility.longToInt(input.getStackSize()));

                if (toTransfer > 0) {
                    input.decStackSize(toTransfer);
                    if (mode == Actionable.MODULATE) slot.stackSize += toTransfer;

                    if (inserted == null) inserted = input.copy()
                        .setStackSize(0);

                    inserted.incStackSize(toTransfer);
                }
            }
        } else {
            int slotToInsertInto = -1;

            // First pass: try to find a matching slot
            for (int i = 0; i < slotCount; i++) {
                ItemStack slot = mInventory[i];

                if (isStackValid(slot) && input.isSameType(slot)) {
                    slotToInsertInto = i;
                    break;
                }
            }

            if (slotToInsertInto == -1) {
                // Second pass: try to find an empty slot
                for (int i = 0; i < slotCount; i++) {
                    ItemStack slot = mInventory[i];

                    if (!isStackValid(slot)) {
                        slotToInsertInto = i;
                        break;
                    }
                }
            }

            if (slotToInsertInto != -1) {
                // Found a slot to insert into

                ItemStack slot = mInventory[slotToInsertInto];

                if (isStackValid(slot)) {
                    ItemStack stack = input.getItemStack();
                    stack.stackSize = 0;
                    slot = stack;
                    if (mode == Actionable.MODULATE) mInventory[slotToInsertInto] = stack;
                }

                int maxStack = getStackSizeLimit(slotToInsertInto, slot);

                int remaining = maxStack - slot.stackSize;
                int toTransfer = Math.min(remaining, GTUtility.longToInt(input.getStackSize()));

                if (toTransfer > 0) {
                    input.decStackSize(toTransfer);
                    if (mode == Actionable.MODULATE) slot.stackSize += toTransfer;

                    inserted = input.copy()
                        .setStackSize(toTransfer);
                }
            }
        }

        if (inserted != null && mode == Actionable.MODULATE) {
            Iterable<IAEItemStack> changes = GTDataUtils.singletonIterable(inserted);

            listeners.forEach((handler, o) -> {
                if (handler.isValid(o)) {
                    handler.postChange(this, changes, src);
                }
            });
        }

        return inserted;
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        IAEItemStack extracted = null;

        for (int i = 0; i < slotCount; i++) {
            ItemStack slot = mInventory[i];

            if (GTUtility.isStackInvalid(slot) || !request.isSameType(slot)) continue;

            int toConsume = (int) Math.min(Integer.MAX_VALUE, Math.min(slot.stackSize, request.getStackSize()));

            if (mode == Actionable.MODULATE) {
                slot.stackSize -= toConsume;

                if (slot.stackSize <= 0) {
                    mInventory[i] = null;
                }
            }

            if (extracted == null) {
                extracted = AEItemStack.create(slot)
                    .setStackSize(0);
            }

            extracted.incStackSize(toConsume);
        }

        if (extracted != null && mode == Actionable.MODULATE) {
            Iterable<IAEItemStack> changes = GTDataUtils.singletonIterable(
                extracted.copy()
                    .setStackSize(-extracted.getStackSize()));

            listeners.forEach((handler, o) -> {
                if (handler.isValid(o)) {
                    handler.postChange(this, changes, src);
                }
            });
        }

        return extracted;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addInputBusUIWidgets(builder, buildContext);

        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(slotCount - 1)
                .background(getGUITextureSet().getItemSlot())
                .canInsert(false)
                .canTake(false)
                .build()
                .setPos(52, 7));
    }
}
