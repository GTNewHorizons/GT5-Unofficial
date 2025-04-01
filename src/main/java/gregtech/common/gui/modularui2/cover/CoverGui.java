package gregtech.common.gui.modularui2.cover;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;

public class CoverGui<T extends Cover> {

    protected static final int WIDGET_MARGIN = 5;

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
     * GUI,
     * override {@link Cover#buildUI} instead.
     *
     * @param guiData     information about the creation context
     * @param syncManager sync handler where widget sync handlers should be registered
     * @param column      main column to add child widgets
     */
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {}

    /**
     * Creates template panel for cover GUI. Called by {@link Cover#buildUI}.
     * Override this method if you want to implement more customized GUI. Otherwise, implement {@link #addUIWidgets}
     * instead.
     */
    public ModularPanel createBasePanel(CoverGuiData guiData, PanelSyncManager syncManager) {
        syncManager.addCloseListener(player -> {
            if (!NetworkUtils.isClient(player)) {
                guiData.getTileEntity()
                    .markDirty();
            }
        });
        final ModularPanel panel = ModularPanel.defaultPanel(getGuiId(), getGUIWidth(), getGUIHeight());
        if (doesBindPlayerInventory() && !guiData.isAnotherWindow()) {
            panel.bindPlayerInventory();
        }
        final Flow widgetsColumn = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginLeft(WIDGET_MARGIN)
            .marginTop(WIDGET_MARGIN);
        panel.child(widgetsColumn);
        addTitleToUI(guiData, widgetsColumn);
        addUIWidgets(guiData, syncManager, widgetsColumn);
        // if (getUIBuildContext().isAnotherWindow()) {
        // builder.widget(
        // ButtonWidget.closeWindowButton(true)
        // .setPos(getGUIWidth() - 15, 3));
        // }

        final Cover cover = guiData.getCoverable()
            .getCoverAtSide(guiData.getSide());
        if (cover.getMinimumTickRate() > 0 && cover.allowsTickRateAddition()) {
            panel.child(
                new gregtech.common.gui.modularui2.widgets.CoverTickRateButton(cover, syncManager).right(4)
                    .bottom(4));
        }

        return panel;
    }

    protected void addTitleToUI(CoverGuiData guiData, Flow column) {
        ItemStack coverItem = GTUtility.intToStack(guiData.getCoverID());
        if (coverItem == null) return;
        column.child(
            Flow.row()
                .coverChildren()
                .marginBottom(4)
                .child(new com.cleanroommc.modularui.drawable.ItemDrawable(coverItem).asWidget())
                .child(
                    new com.cleanroommc.modularui.widgets.TextWidget(coverItem.getDisplayName()).marginLeft(4)
                        .widgetTheme(GTWidgetThemes.TEXT_TITLE)));
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

    protected T getCover(CoverGuiData guiData) {
        // noinspection unchecked
        return (T) guiData.getCover();
    }
}
