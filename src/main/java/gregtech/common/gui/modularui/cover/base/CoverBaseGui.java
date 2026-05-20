package gregtech.common.gui.modularui.cover.base;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import cpw.mods.fml.common.FMLCommonHandler;
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
    protected static final int TICK_RATE_BUTTON_SIZE = 20;

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
        return createBasePanel(syncManager, uiSettings, data).coverChildren();
    }

    public final ModularPanel createPopUpPanel(PanelSyncManager syncManager, UISettings uiSettings, CoverGuiData data,
        IWidget parent) {
        ModularPanel basePanel = createBasePanel(syncManager, uiSettings, data).coverChildren();
        layoutPopUp(basePanel, parent);
        return basePanel;
    }

    /**
     * Creates template panel for cover GUI. Called by {@link Cover#buildUI}. Override this method if you want to
     * implement more customized GUI. Otherwise, implement {@link #addUIWidgets} instead.
     *
     * @param syncManager sync handler where widget sync handlers should be registered
     */
    protected ModularPanel createBasePanel(PanelSyncManager syncManager, UISettings uiSettings, CoverGuiData data) {
        ModularPanel panel = new ModularPanel(getGuiId());
        syncManager.addCloseListener(player -> {
            if (!NetworkUtils.isClient(player)) {
                cover.getTile()
                    .markDirty();
            }
        });
        final Flow mainColumn = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .margin(WIDGET_MARGIN);
        panel.child(mainColumn);

        // Header
        final Flow headerRow = Flow.row()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginBottom(4)
            .marginRight(data.isPopUp() ? 12 : 0) // Filler for close button
            .coverChildren();
        addTitleToUI(headerRow, data);
        mainColumn.child(headerRow);

        // Widgets
        final Flow widgetsColumn = Flow.column()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .coverChildren();
        addUIWidgets(syncManager, widgetsColumn, data);
        mainColumn.child(widgetsColumn);

        // Footer
        if (cover.getMinimumTickRate() > 0 && cover.allowsTickRateAddition()) {
            panel.child(
                new CoverTickRateButton(cover, syncManager).right(4)
                    .bottom(4));
        }
        if (!data.isPopUp() && doesBindPlayerInventory()) {
            mainColumn.child(
                SlotGroupWidget.playerInventory(false)
                    .marginTop(4));
        }
        return panel;
    }

    protected void addTitleToUI(Flow titleRow, CoverGuiData data) {
        ItemStack coverItem = GTUtility.intToStack(cover.getCoverID());
        if (coverItem == null) return;
        titleRow.child(new ItemDrawable(coverItem).asWidget())
            .childIf(
                !data.isPopUp() || shouldIncludeTitleInPopUp(),
                () -> IKey.str(coverItem.getDisplayName())
                    .asWidget()
                    .marginLeft(4)
                    .verticalCenter()
                    .widgetTheme(GTWidgetThemes.TEXT_TITLE));
    }

    private void layoutPopUp(ModularPanel panel, IWidget button) {
        if (positionRelativeToCoverButton()) {
            panel.relative(button)
                .right(24);
        }
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

    protected int getStringMaxWidth(String... strings) {
        // Width doesn't matter on the server and the actual method relies on the client-only FontRenderer class
        if (FMLCommonHandler.instance()
            .getSide()
            .isServer()) return 100;
        return IKey.renderer.getMaxWidth(Arrays.asList(strings));
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

    protected boolean doesBindPlayerInventory() {
        return false;
    }

    protected boolean positionRelativeToCoverButton() {
        return false;
    }

    protected boolean shouldIncludeTitleInPopUp() {
        return true;
    }

}
