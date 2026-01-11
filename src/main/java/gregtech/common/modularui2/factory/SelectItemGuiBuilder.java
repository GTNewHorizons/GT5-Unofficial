package gregtech.common.modularui2.factory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.sync.SelectItemServerAction;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;

// spotless:off
/**
 * Builder class for GUI where player selects item from given choices.
 * <p>
 * Example usage:
 * <pre>{@code
 *     ModularPanel panel = GTGuis.createPopUpPanel("popup");
 *     return new SelectItemGuiBuilder(panel, choices)
 *         .setTitle("Example Selector Screen")
 *         .setSelected(2)
 *         .build();
 * }</pre>
 *
 * @see GTGuis
 */
// spotless:on
public class SelectItemGuiBuilder {

    public static final int DESELECTED = -1;
    private static final int COLS = 9;
    private static final int GUI_WIDTH = 18 * COLS + 7 * 2;

    private final ModularPanel panel;
    private final List<ItemStack> choices;

    private @Nullable ItemStack headerItem;
    private @Nullable IKey title;
    private int selected;
    private @Nullable IntSyncValue selectedSyncHandler;
    private @Nullable SelectItemServerAction onSelectedServerAction;
    private @Nullable OnSelectedAction onSelectedClientAction;
    private @Nullable IDrawable currentItemSlotOverlay;
    private boolean allowDeselected;

    private boolean built = false;

    /**
     * Entry point of this builder.
     *
     * @param panel   Panel to build widgets on.
     * @param choices Choices of ItemStacks available.
     */
    public SelectItemGuiBuilder(ModularPanel panel, List<ItemStack> choices) {
        this.panel = panel;
        this.choices = choices;
    }

    /**
     * Sets item shown along with title.
     */
    public SelectItemGuiBuilder setHeaderItem(@Nullable ItemStack headerItem) {
        this.headerItem = headerItem;
        return this;
    }

    /**
     * Sets title of the GUI.
     */
    public SelectItemGuiBuilder setTitle(@Nullable IKey title) {
        this.title = title;
        return this;
    }

    /**
     * Sets index of currently selected item. Passing {@link #DESELECTED} to the argument works only when
     * {@link #setAllowDeselected} is set to true. 0 by default.
     */
    public SelectItemGuiBuilder setSelected(int selected) {
        if (selected < DESELECTED || selected >= choices.size()) {
            return this;
        }
        // Before build, allowDeselected might not have been set yet due to method call order,
        // so temporarily allow it and check later.
        // Otherwise, check now.
        if (built && !allowDeselected && selected == DESELECTED) {
            return this;
        }
        this.selected = selected;
        return this;
    }

    /**
     * Sets sync handler for currently selected index. This allows pulling updates caused by outside this GUI.
     */
    public SelectItemGuiBuilder setSelectedSyncHandler(@Nullable IntSyncValue selectedSyncHandler) {
        this.selectedSyncHandler = selectedSyncHandler;
        return this;
    }

    /**
     * Sets action executed on server when item gets selected. The argument {@link SelectItemServerAction} must be
     * registered via {@link com.cleanroommc.modularui.value.sync.PanelSyncManager#syncValue
     * PanelSyncManager.syncValue}.
     */
    public SelectItemGuiBuilder setOnSelectedServerAction(SelectItemServerAction onSelectedServerAction) {
        this.onSelectedServerAction = onSelectedServerAction;
        return this;
    }

    /**
     * Sets action executed on client when item gets selected.
     */
    public SelectItemGuiBuilder setOnSelectedClientAction(@Nullable OnSelectedAction onSelectedClientAction) {
        this.onSelectedClientAction = onSelectedClientAction;
        return this;
    }

    /**
     * Sets slot overlay displayed on the "currently selected" slot.
     */
    public SelectItemGuiBuilder setCurrentItemSlotOverlay(@Nullable IDrawable currentItemSlotOverlay) {
        this.currentItemSlotOverlay = currentItemSlotOverlay;
        return this;
    }

    /**
     * Sets whether to allow the state where no item is selected. False by default.
     */
    public SelectItemGuiBuilder setAllowDeselected(boolean allowDeselected) {
        this.allowDeselected = allowDeselected;
        return this;
    }

    /**
     * Builds panel from the passed template panel with all the elements specified.
     */
    public ModularPanel build() {
        if (choices.isEmpty()) {
            throw new IllegalStateException("Choices must not be empty!");
        }
        int rows = (choices.size() - 1) / COLS + 1;
        panel.size(GUI_WIDTH, 53 + 18 * rows);

        // noinspection DataFlowIssue
        panel.childIf(
            headerItem != null || title != null,
            Flow.row()
                .coverChildren()
                .childPadding(4)
                .pos(5, 5)
                .child(
                    new ItemDrawable(headerItem).asWidget()
                        .size(16))
                .childIf(title != null, title.asWidget()));

        panel.child(
            IKey.lang("GT5U.gui.select.current")
                .asWidget()
                .leftRel(0.5f, -(18 / 2) - 3, 1)
                .height(18)
                .top(22));
        panel.child(
            new SlotLikeButtonWidget(() -> selected > DESELECTED ? choices.get(selected) : null)
                .background(
                    GTGuiTextures.SLOT_ITEM_DARK,
                    new DynamicDrawable(
                        () -> currentItemSlotOverlay != null ? currentItemSlotOverlay : IDrawable.EMPTY))
                .playClickSound(false)
                .onMousePressed(mouseButton -> true)
                .alignX(0.5f)
                .top(22));

        List<List<IWidget>> choiceWidgets = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<IWidget> rowWidgets = new ArrayList<>();
            choiceWidgets.add(rowWidgets);
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                if (index >= choices.size()) break;
                rowWidgets.add(
                    new SlotLikeButtonWidget(choices.get(index)).background(
                        new DynamicDrawable(
                            () -> selected == index ? GTGuiTextures.SLOT_ITEM_DARK : GTGuiTextures.SLOT_ITEM_STANDARD))
                        .size(18)
                        .onMousePressed(mouseButton -> {
                            if (mouseButton == 0) {
                                setSelected(index);
                            } else {
                                setSelected(DESELECTED);
                            }
                            MouseData mouseData = MouseData.create(mouseButton);
                            if (onSelectedServerAction != null) {
                                onSelectedServerAction.send(selected, mouseData);
                            }
                            if (onSelectedClientAction != null) {
                                onSelectedClientAction.accept(selected, mouseData);
                            }
                            return true;
                        }));
            }
        }
        panel.child(
            new Grid().coverChildren()
                .pos(7, 46)
                .matrix(choiceWidgets));

        if (selectedSyncHandler != null) {
            // Correct current selected
            setSelected(selectedSyncHandler.getIntValue());
            // Listen to changes made by other causes such as other players configuring at the same time
            selectedSyncHandler.setChangeListener(() -> setSelected(selectedSyncHandler.getIntValue()));
        }
        // Check skipped during setSelected
        if (!allowDeselected && selected == DESELECTED) {
            setSelected(0);
        }
        built = true;

        return panel;
    }

    @FunctionalInterface
    public interface OnSelectedAction {

        void accept(int selected, MouseData mouseData);
    }
}
