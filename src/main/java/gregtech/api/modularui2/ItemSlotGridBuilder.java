package gregtech.api.modularui2;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class ItemSlotGridBuilder {

    @NotNull
    private final IItemHandler itemHandler;
    @NotNull
    private final PanelSyncManager syncManager;
    private int width;
    private int height;
    private String slotGroupKey = "item_inv";
    private boolean hasSlotGroup = true;
    private boolean isSingletonSlotGroup = false;
    private boolean canPut = true;
    private boolean canTake = true;
    private int indexOffset = 0;
    @NotNull
    private Predicate<ItemStack> filter = $ -> true;
    @NotNull
    private UnaryOperator<ItemSlot> slotModifier = UnaryOperator.identity();
    private Class<? extends ItemSlot> itemSlotOverride;
    private Object[] overrideParameters;

    public ItemSlotGridBuilder(@NotNull IItemHandler itemHandler, @NotNull PanelSyncManager syncManager) {
        this.itemHandler = itemHandler;
        this.syncManager = syncManager;
    }

    public ItemSlotGridBuilder size(int size) {
        return size(size, size);
    }

    public ItemSlotGridBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ItemSlotGridBuilder slotGroupKey(String slotGroupKey) {
        this.slotGroupKey = slotGroupKey;
        return this;
    }

    public ItemSlotGridBuilder hasSlotGroup(boolean hasSlotGroup) {
        this.hasSlotGroup = hasSlotGroup;
        return this;
    }

    // disables slot group when enabled
    public ItemSlotGridBuilder singletonSlotGroup(boolean isSingletonSlotGroup) {
        this.isSingletonSlotGroup = isSingletonSlotGroup;
        if (isSingletonSlotGroup) hasSlotGroup = false;
        return this;
    }

    public ItemSlotGridBuilder canPut(boolean canPut) {
        this.canPut = canPut;
        return this;
    }

    public ItemSlotGridBuilder canTake(boolean canTake) {
        this.canTake = canTake;
        return this;
    }

    public ItemSlotGridBuilder accessibility(boolean canPut, boolean canTake) {
        return canPut(canPut).canTake(canTake);
    }

    public ItemSlotGridBuilder indexOffset(int indexOffset) {
        this.indexOffset = indexOffset;
        return this;
    }

    public ItemSlotGridBuilder filter(@NotNull Predicate<ItemStack> filter) {
        this.filter = filter;
        return this;
    }

    public ItemSlotGridBuilder slotModifier(@NotNull UnaryOperator<ItemSlot> slotModifier) {
        this.slotModifier = slotModifier;
        return this;
    }

    public ItemSlotGridBuilder overrideItemSlot(Class<? extends ItemSlot> itemSlotOverride,
        Object... overrideParameters) {
        this.itemSlotOverride = itemSlotOverride;
        this.overrideParameters = overrideParameters;
        return this;
    }

    public Grid build() {
        return new Grid().coverChildren()
            .gridOfWidthHeight(width, height, ($x, $y, index) -> {
                ModularSlot modularSlot = new ModularSlot(itemHandler, index + indexOffset)
                    .accessibility(canPut, canTake)
                    .filter(filter);

                if (hasSlotGroup) {
                    syncManager.registerSlotGroup(slotGroupKey, height);
                    modularSlot.slotGroup(slotGroupKey);
                } else if (isSingletonSlotGroup) modularSlot.singletonSlotGroup();

                ItemSlot itemSlot = new ItemSlot();
                if (itemSlotOverride != null) {
                    try {
                        itemSlot = itemSlotOverride.getConstructor(
                            Arrays.stream(overrideParameters)
                                .map(Object::getClass)
                                .toArray(Class[]::new))
                            .newInstance(overrideParameters);
                    } catch (Exception e) {
                        throw new RuntimeException("An error occured: " + e);
                    }
                }

                return slotModifier.apply(itemSlot.slot(modularSlot));
            });
    }
}
