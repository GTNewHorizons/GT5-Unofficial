package gregtech.common.modularui2.widget.builder;

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

/**
 * A builder class for creating and configuring a {@link Grid} of {@link ItemSlot} widgets.
 * <p>
 * It allows for easy configuration of grid dimensions, slot accessibility, item filtering, custom
 * slot instantiations, and slot groups.
 * <p>
 * Will register a slot group with the name {@code slotGroupKey} and rowSize of {@code height}
 * unless {@code hasSlotGroup} is set to false.
 * {@code slotGroupKey} is set to {@code "item_inv"} by default.
 */
public class ItemSlotGridBuilder {

    @NotNull
    private final IItemHandler itemHandler;
    @NotNull
    private final PanelSyncManager syncManager;
    private int width;
    private int height;
    private String slotGroupKey = "item_inv";
    private boolean hasSlotGroup = true;
    private boolean canPut = true;
    private boolean canTake = true;
    private int indexOffset = 0;
    @NotNull
    private Predicate<ItemStack> filter = $ -> true;
    @NotNull
    private UnaryOperator<ItemSlot> slotModifier = UnaryOperator.identity();
    private Class<? extends ItemSlot> itemSlotOverride;
    private Object[] overrideParameters;

    /**
     * Constructs a new ItemSlotGridBuilder.
     *
     * @param itemHandler The inventory item handler backing the slots.
     * @param syncManager The synchronization manager for slot group registration.
     */
    public ItemSlotGridBuilder(@NotNull IItemHandler itemHandler, @NotNull PanelSyncManager syncManager) {
        this.itemHandler = itemHandler;
        this.syncManager = syncManager;
    }

    /**
     * Sets the dimensions of the grid to a square.
     *
     * @param size The width and height of the grid in slots.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder size(int size) {
        return size(size, size);
    }

    /**
     * Sets the dimensions of the grid.
     *
     * @param width  The number of slots horizontally.
     * @param height The number of slots vertically.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Sets the identifier key / name for the slot group.
     *
     * @param slotGroupKey The string key used to group these slots. Defaults to "item_inv".
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder slotGroupKey(String slotGroupKey) {
        this.slotGroupKey = slotGroupKey;
        return this;
    }

    /**
     * Determines whether the generated slots should be registered as part of a unified slot group.
     *
     * @param hasSlotGroup True to group slots under the configured {@code slotGroupKey}, false otherwise. Defaults to
     *                     true.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder hasSlotGroup(boolean hasSlotGroup) {
        this.hasSlotGroup = hasSlotGroup;
        return this;
    }

    /**
     * Sets whether items can be inserted into the slots by the player/system.
     *
     * @param canPut True to allow item insertion, false to deny. Defaults to true.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder canPut(boolean canPut) {
        this.canPut = canPut;
        return this;
    }

    /**
     * Sets whether items can be extracted from the slots by the player/system.
     *
     * @param canTake True to allow item extraction, false to deny. Defaults to true.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder canTake(boolean canTake) {
        this.canTake = canTake;
        return this;
    }

    /**
     * Sets the accessibility rules for both inserting and extracting items at once.
     *
     * @param canPut  True to allow item insertion.
     * @param canTake True to allow item extraction.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder accessibility(boolean canPut, boolean canTake) {
        return canPut(canPut).canTake(canTake);
    }

    /**
     * Sets the offset for the starting index within the {@link IItemHandler}.
     * This is highly useful when mapping a grid to a specific sub-section of a larger inventory.
     *
     * @param indexOffset The starting index. Defaults to 0.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder indexOffset(int indexOffset) {
        this.indexOffset = indexOffset;
        return this;
    }

    /**
     * Sets a filter to restrict which items can be placed in these grid slots.
     *
     * @param filter A predicate testing an {@link ItemStack}. Defaults to allowing all items.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder filter(@NotNull Predicate<ItemStack> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Applies a custom modifier function to every {@link ItemSlot} after it has been instantiated
     * and configured, but right before it is added to the layout grid.
     * <p>
     * Example: Change the background overlay of the slot
     *
     * <pre>
     *     {@code slotModifier(itemSlot -> itemSlot.backgroundOverlay(<texture>))}
     * </pre>
     *
     * @param slotModifier A unary operator to modify or wrap the slot. Defaults to {@code UnaryOperator.identity()}
     *                     which does not modify the item slot.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder slotModifier(@NotNull UnaryOperator<ItemSlot> slotModifier) {
        this.slotModifier = slotModifier;
        return this;
    }

    /**
     * Overrides the default {@link ItemSlot} implementation with a custom subclass.
     * The custom slot class will be instantiated dynamically via reflection during the build process.
     *
     * @param itemSlotOverride   The {@link Class} of the custom item slot.
     * @param overrideParameters The arguments to pass directly to the custom slot's constructor.
     * @return This builder instance for method chaining.
     */
    public ItemSlotGridBuilder overrideItemSlot(Class<? extends ItemSlot> itemSlotOverride,
        Object... overrideParameters) {
        this.itemSlotOverride = itemSlotOverride;
        this.overrideParameters = overrideParameters;
        return this;
    }

    /**
     * Finalizes the configuration and constructs the {@link Grid} of item slots.
     *
     * @return A fully populated and configured {@link Grid} widget.
     * @throws RuntimeException If dynamically instantiating a custom overridden item slot via reflection fails.
     */
    public Grid build() {
        return new Grid().coverChildren()
            .gridOfWidthHeight(width, height, ($x, $y, index) -> {
                ModularSlot modularSlot = new ModularSlot(itemHandler, index + indexOffset)
                    .accessibility(canPut, canTake)
                    .filter(filter);

                if (hasSlotGroup) {
                    syncManager.registerSlotGroup(slotGroupKey, height);
                    modularSlot.slotGroup(slotGroupKey);
                }

                ItemSlot itemSlot = new ItemSlot();
                if (itemSlotOverride != null) {
                    try {
                        itemSlot = itemSlotOverride.getConstructor(
                            Arrays.stream(overrideParameters)
                                .map(Object::getClass)
                                .toArray(Class[]::new))
                            .newInstance(overrideParameters);
                    } catch (Exception e) {
                        throw new RuntimeException("An error occurred: " + e);
                    }
                }

                return slotModifier.apply(itemSlot.slot(modularSlot));
            });
    }
}
