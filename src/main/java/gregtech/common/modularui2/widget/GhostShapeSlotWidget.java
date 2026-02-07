package gregtech.common.modularui2.widget;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.UpOrDown;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

/**
 * Slot for selecting extruder shapes on Extrusion Input Hatch.
 */
public class GhostShapeSlotWidget extends PhantomItemSlot {

    private static final String GUI_ID = "shape_selector";

    private final MTEHatchExtrusion hatch;
    private GhostShapeSyncHandler shapeSyncHandler;
    private IPanelHandler selectorPanelHandler;
    private PanelSyncManager syncManager;

    public GhostShapeSlotWidget(MTEHatchExtrusion hatch, PanelSyncManager syncManager) {
        super();
        this.hatch = hatch;
        tooltipBuilder(this::getShapeSlotTooltip);
        this.syncManager = syncManager;
        selectorPanelHandler = buildSelectorPanel();
    }

    @Override
    public IDrawable getCurrentBackground(ITheme theme, WidgetThemeEntry<?> widgetTheme) {
        IDrawable background = super.getCurrentBackground(theme, widgetTheme);
        return new DrawableStack(background, GTGuiTextures.OVERLAY_SLOT_EXTRUDER_SHAPE);
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (!isSelectorPanelOpen()) {
            if (mouseButton == 0 && Interactable.hasShiftDown()) {
                openSelectorPanel();
            } else {
                MouseData mouseData = MouseData.create(mouseButton);
                getSyncHandler().syncToServer(PhantomItemSlotSH.SYNC_CLICK, mouseData::writeToPacket);
            }
        }
        return Result.SUCCESS;
    }

    @Override
    public boolean onMouseScroll(UpOrDown scrollDirection, int amount) {
        if (isSelectorPanelOpen()) return true;
        MouseData mouseData = MouseData.create(scrollDirection.modifier);
        getSyncHandler().syncToServer(PhantomItemSlotSH.SYNC_SCROLL, mouseData::writeToPacket);
        return true;
    }

    @Override
    public PhantomItemSlot slot(ModularSlot slot) {
        shapeSyncHandler = new GhostShapeSyncHandler(slot, hatch);
        setSyncOrValue(shapeSyncHandler);
        return this;
    }

    private void getShapeSlotTooltip(RichTooltip tooltip) {
        tooltip.clearText()
            .addLine(IKey.lang("GT5U.machines.select_shape.tooltip"))
            .spaceLine(2)
            .addLine(IKey.lang("GT5U.machines.select_shape.tooltip.1"))
            .addLine(IKey.lang("GT5U.machines.select_shape.tooltip.2"));
    }

    private boolean isSelectorPanelOpen() {
        return getPanel().getScreen()
            .isPanelOpen(GUI_ID);
    }

    private void openSelectorPanel() {
        if (selectorPanelHandler == null) {
            selectorPanelHandler = buildSelectorPanel();
        }
        selectorPanelHandler.openPanel();
    }

    private IPanelHandler buildSelectorPanel() {

        return syncManager.syncedPanel("shapeSlotPanel", true, (mainPanel, player) -> {
            ModularPanel panel = GTGuis.createPopUpPanel(GUI_ID);
            return new SelectItemGuiBuilder(panel, Arrays.asList(MTEHatchExtrusion.extruderShapes))
                .setHeaderItem(hatch.getStackForm(1))
                .setTitle(IKey.lang("GT5U.machines.select_shape"))
                .setSelectedSyncHandler(shapeSyncHandler.getIndexSync())
                .setOnSelectedClientAction((selected, mouseData) -> {
                    shapeSyncHandler.setSelectedIndex(selected);
                    if (mouseData.shift) {
                        panel.closeIfOpen();
                    }
                })
                .setAllowDeselected(false)
                .setCurrentItemSlotOverlay(GTGuiTextures.OVERLAY_SLOT_EXTRUDER_SHAPE)
                .build();
        });
    }

    @NotNull
    @Override
    public GhostShapeSyncHandler getSyncHandler() {
        return shapeSyncHandler;
    }
}
