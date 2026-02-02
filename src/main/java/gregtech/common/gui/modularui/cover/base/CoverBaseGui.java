package gregtech.common.gui.modularui.cover.base;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.modularui2.widget.CoverTickRateButton;

public class CoverBaseGui<T extends Cover> {

    protected final T cover;

    public CoverBaseGui(T cover) {
        this.cover = cover;
    }

    protected static final int WIDGET_MARGIN = 5;
    protected static final int ROW_PADDING = 3;
    protected static final int ROW_ELEMENT_PADDING = 2;

    /**
     * Override this method to provide GUI ID if this cover has GUI. It's used for resource packs to customize stuff.
     * Conventionally it should be {@code cover.snake_case}.
     */
    protected String getGuiId() {
        // throw new RuntimeException("GUI ID must be provided to create GUI!");
        return "hogehoge";
    }

    /**
     * Override this method to implement cover GUI if {@link Cover#hasCoverGUI} is true. If you want highly customized
     * GUI, override {@link Cover#buildUI} instead.
     *
     * @param syncManager sync handler where widget sync handlers should be registered
     * @param column      main column to add child widgets
     */
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {}

    /**
     * Creates a standalone panel holding the UI for this cover. GuiData can be passed in as well.
     * <br>
     * Creates a standalone panel holding the UI for this cover. <br>
     *
     * Since it is standalone, you shouldn't try to have multiple instances of this panel on screen at once, or tied to
     * several widgets. Use {@link CoverBaseGui#createBasePanel} with a unique panel name instead.
     */
    public final ModularPanel createStandalonePanel(PanelSyncManager syncManager, UISettings uiSettings,
        CoverGuiData data) {
        ModularPanel basePanel = createBasePanel("standalone.cover", syncManager, uiSettings, data);
        if (doesBindPlayerInventory()) {
            int slotHeight = 18;
            int inventoryRows = 4;
            int hotbarToInventoryGap = 4;
            int inventoryMargin = 4;
            basePanel.height(getGUIHeight() + slotHeight * inventoryRows + hotbarToInventoryGap + inventoryMargin);
            basePanel.bindPlayerInventory();
        }
        return basePanel;
    }

    /**
     * Creates template panel for cover GUI. Called by {@link Cover#buildUI}. Override this method if you want to
     * implement more customized GUI. Otherwise, implement {@link #addUIWidgets} instead.
     *
     * @param panelName   the unique name of this panel in the context of your UI.
     * @param syncManager sync handler where widget sync handlers should be registered
     * @return UI panel to show
     */
    public ModularPanel createBasePanel(String panelName, PanelSyncManager syncManager, UISettings uiSettings,
        CoverGuiData data) {
        syncManager.addCloseListener(player -> {
            if (!NetworkUtils.isClient(player)) {
                cover.getTile()
                    .markDirty();
            }
        });
        final ModularPanel panel = ModularPanel.defaultPanel(getGuiId(), getGUIWidth(), getGUIHeight());
        final Flow widgetsColumn = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginLeft(WIDGET_MARGIN)
            .marginTop(WIDGET_MARGIN);
        panel.child(widgetsColumn);
        addTitleToUI(widgetsColumn);
        addUIWidgets(syncManager, widgetsColumn, data);

        if (cover.getMinimumTickRate() > 0 && cover.allowsTickRateAddition()) {
            panel.child(
                new CoverTickRateButton(cover, syncManager).right(4)
                    .bottom(4));
        }

        return panel;
    }

    protected void addTitleToUI(Flow column) {
        ItemStack coverItem = GTUtility.intToStack(cover.getCoverID());
        if (coverItem == null) return;
        column.child(
            Flow.row()
                .coverChildren()
                .marginBottom(4)
                .child(new com.cleanroommc.modularui.drawable.ItemDrawable(coverItem).asWidget())
                .child(
                    IKey.str(coverItem.getDisplayName())
                        .asWidget()
                        .marginLeft(4)
                        .widgetTheme(GTWidgetThemes.TEXT_TITLE)));

    }

    /**
     * Creates a layout to which you can add rows positioned with {@link CoverBaseGui#positionRow}.
     */
    protected Flow makeRowLayout() {
        return Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginLeft(WIDGET_MARGIN)
            .childPadding(ROW_PADDING);
    }

    /**
     * Positions a row to render nicely inside the layout returned by {@link CoverBaseGui#makeRowLayout}.
     */
    protected Flow positionRow(Flow row) {
        return row.coverChildren()
            .childPadding(ROW_ELEMENT_PADDING);
    }

    protected Flow makeNamedColumn(IKey name) {
        return Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(name.asWidget());
    }

    protected TextFieldWidget makeNumberField(int width) {
        return new TextFieldWidget().setFormatAsInteger(true)
            .width(width)
            .height(12);
    }

    protected TextFieldWidget makeNumberField() {
        return makeNumberField(80);
    }

    protected int getGUIWidth() {
        return 176;
    }

    protected int getGUIHeight() {
        return 107;
    }

    protected boolean doesBindPlayerInventory() {
        return false;
    }

}
