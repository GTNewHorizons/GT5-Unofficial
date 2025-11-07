package gregtech.common.modularui2.widget;

import net.minecraft.item.ItemStack;

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
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

/**
 * Phantom slot widget for selecting a mold in the Solidifier hatch.
 */
public class GhostMoldSlotWidget extends PhantomItemSlot {

    private static final String GUI_ID = "mold_selector";

    private final MTEHatchSolidifier hatch;
    private final IntSyncValue selectedSyncHandler;
    private IPanelHandler selectorPanelHandler;
    private PanelSyncManager syncManager;

    public GhostMoldSlotWidget(MTEHatchSolidifier hatch, PanelSyncManager syncManager) {
        super();
        this.hatch = hatch;
        this.syncManager = syncManager;
        tooltipBuilder(this::getMoldSlotTooltip);
        this.selectedSyncHandler = syncManager.findSyncHandler("selector_screen_selected", IntSyncValue.class);
        this.selectorPanelHandler = buildSelectorPanel(selectedSyncHandler);
    }

    @Override
    public IDrawable getCurrentBackground(ITheme theme, WidgetThemeEntry<?> widgetTheme) {
        IDrawable background = super.getCurrentBackground(theme, widgetTheme);
        return new DrawableStack(background, GTGuiTextures.OVERLAY_SLOT_MOLD);
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
        ItemSlotSH sh = new PhantomItemSlotSH(slot);
        isValidSyncHandler(sh);
        setSyncHandler(sh);
        return this;
    }

    private void getMoldSlotTooltip(RichTooltip tooltip) {
        ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchSolidifier.moldSlot);
        String moldName = current != null ? GTUtility.translate(current.getDisplayName()) : "None";
        tooltip.clearText()
            .addLine(IKey.lang("GT5U.machines.select_mold.tooltip"))
            .spaceLine(2)
            .addLine(IKey.lang("GT5U.machines.select_mold.tooltip.current", moldName));
    }

    @NotNull
    @Override
    public PhantomItemSlotSH getSyncHandler() {
        return super.getSyncHandler();
    }

    private boolean isSelectorPanelOpen() {
        return getPanel().getScreen()
            .isPanelOpen(GUI_ID);
    }

    private void openSelectorPanel() {
        if (selectorPanelHandler == null) {
            selectorPanelHandler = buildSelectorPanel(selectedSyncHandler);
        }
        selectorPanelHandler.openPanel();
    }

    private IPanelHandler buildSelectorPanel(IntSyncValue selectedSyncHandler) {
        return syncManager.panel(GUI_ID, (mainPanel, player) -> {
            ModularPanel panel = GTGuis.createPopUpPanel(GUI_ID);
            return new SelectItemGuiBuilder(panel, java.util.Arrays.asList(MTEHatchSolidifier.solidifierMolds))
                .setHeaderItem(hatch.getStackForm(0))
                .setTitle(IKey.lang("GT5U.machines.select_mold"))
                .setSelectedSyncHandler(selectedSyncHandler)
                .setOnSelectedClientAction((selected, mouseData) -> {
                    hatch.setMold(MTEHatchSolidifier.solidifierMolds[selected]);
                    if (mouseData.shift) {
                        panel.animateClose();
                    }
                })
                .setCurrentItemSlotOverlay(GTGuiTextures.OVERLAY_SLOT_MOLD)
                .setAllowDeselected(true)
                .build();
        }, true);
    }
}
