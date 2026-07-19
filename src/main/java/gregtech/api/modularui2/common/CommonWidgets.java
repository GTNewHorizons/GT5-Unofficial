package gregtech.api.modularui2.common;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

public final class CommonWidgets {

    /**
     * Returns a ghost circuit slot widget. baseMachine should be an instance of IConfigurationCircuitSupport
     * Otherwise, returns an empty widget
     *
     * @param syncManager - manager
     * @param baseMachine - meta tile entity
     * @return ghost circuit slot widget
     */
    public static Widget<? extends Widget<?>> createCircuitSlot(PanelSyncManager syncManager,
        IMetaTileEntity baseMachine) {
        if (baseMachine instanceof IConfigurationCircuitSupport circuitEnabled && circuitEnabled.allowSelectCircuit()) {
            return new GhostCircuitSlotWidget(baseMachine, syncManager)
                .slot(new ModularSlot(new GhostCircuitItemStackHandler(baseMachine), 0));
        }
        return IDrawable.EMPTY.asWidget()
            .size(18);
    }

    /**
     * Returns a title widget positioned on the top left above the panel. Client only!
     *
     * @param mte        - The machine to make the title for
     * @param panelWidth - The width of the machine's main panel
     * @return machine title widget on the client, null otherwise
     */
    public static Widget<?> createMachineTitle(IMetaTileEntity mte, int panelWidth) {
        if (NetworkUtils.isClient()) {
            String title = mte.getLocalName();

            int borderRadius = 5;

            byte colorization = mte.getBaseMetaTileEntity()
                .getColorization();
            boolean showSwatch = GregTechAPI.sColoredGUI && !GregTechAPI.sMachineMetalGUI
                && colorization != IColoredTileEntity.UNCOLOURED;
            int swatchOuterSize = showSwatch ? 9 : 0;
            int childPadding = 4;
            // Reserve the swatch's width (plus the padding to the text) so the title still wraps to fit within the
            // panel instead of overflowing when the swatch is shown.
            int swatchReservedWidth = showSwatch ? swatchOuterSize + childPadding : 0;

            int maxWidth = panelWidth - borderRadius * 2 - swatchReservedWidth;

            int titleWidth = TextRenderer.getFontRenderer()
                .getStringWidth(title);
            int widgetWidth = Math.min(maxWidth, titleWidth);

            int rows = (int) Math.ceil((double) titleWidth / maxWidth);
            int heightPerRow = TextRenderer.getFontRenderer().FONT_HEIGHT;
            int height = heightPerRow * rows;

            Flow row = Flow.row()
                .coverChildren()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .margin(5, 5, 5, 1)
                .childPadding(childPadding);

            // Whichever of the swatch or the (possibly multi-row) text is taller defines the row's content height;
            // the shorter one gets centered against it.
            int rowContentHeight = Math.max(height, swatchOuterSize);

            if (showSwatch) {
                int swatchSize = 7;
                int swatchMarginTop = (rowContentHeight - swatchOuterSize) / 2;
                // Added before the title text so its position is always the same, regardless of the machine name's
                // length.
                row.child(
                    new ParentWidget<>().size(swatchOuterSize, swatchOuterSize)
                        .marginTop(swatchMarginTop)
                        .widgetTheme(GTWidgetThemes.BACKGROUND_COLOR_SWATCH)
                        .tooltip(
                            tooltip -> tooltip.add(
                                Dyes.get(colorization)
                                    .getLocalizedDyeName()))
                        .child(
                            new Widget<>().size(swatchSize, swatchSize)
                                .pos(1, 1)
                                .background(new Rectangle().color(Color.withAlpha(mte.getGUIColorization(), 255)))));
            }

            int textMarginTop = (rowContentHeight - height) / 2;
            row.child(
                IKey.str(title)
                    .asWidget()
                    .size(widgetWidth, height)
                    .marginTop(textMarginTop)
                    .widgetTheme(GTWidgetThemes.TEXT_TITLE));

            return new SingleChildWidget<>().coverChildren()
                .bottomRel(1)
                .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
                .child(row);
        }
        return null;
    }

    /**
     * Creates a styled parent widget intended to act as a monitor or display area,
     * commonly used for rendering fluid tanks.
     * <p>
     * This widget comes pre-configured with a black screen background texture and
     * standardized internal padding to ensure its contents are framed correctly.
     *
     * @param width  The width of the screen widget.
     * @param height The height of the screen widget.
     * @return A configured {@link ParentWidget}
     */
    public static ParentWidget<?> createFluidScreen(int width, int height) {
        return new ParentWidget<>().size(width, height)
            .padding(3, 2, 3, 2)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK);
    }

    /**
     * Creates a horizontal row widget containing a toggle button and a dynamic text label,
     * typically used for toggling redstone behavior between "Normal" and "Inverted" modes.
     *
     * @param invertedSyncer The synced boolean value that handles the toggle state and updates the label.
     * @return A configured {@link Flow} row containing the toggle button and its label.
     */
    public static Flow createInvertButtonRow(BooleanSyncValue invertedSyncer) {
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16))
            .child(
                IKey.dynamic(
                    () -> invertedSyncer.getValue() ? StatCollector.translateToLocal("gt.interact.desc.inverted")
                        : StatCollector.translateToLocal("gt.interact.desc.normal"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
