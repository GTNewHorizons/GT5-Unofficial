package gregtech.common.modularui2.widget;

import static gregtech.api.util.item.GhostCircuitItemStackHandler.NO_CONFIG;
import static gregtech.common.modularui2.factory.SelectItemGuiBuilder.DESELECTED;
import static gregtech.common.modularui2.sync.GhostCircuitSyncHandler.SYNC_CIRCUIT_CONFIG;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DrawableArray;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;
import gregtech.common.modularui2.sync.GhostCircuitSyncHandler;

/**
 * Item slot that appears on machines implementing {@link IConfigurationCircuitSupport}.
 */
public class GhostCircuitSlotWidget extends ItemSlot {

    private static final String GUI_ID = "circuit_selector";

    private final IMetaTileEntity mte;
    private final IntSyncValue selectedSyncHandler;
    private IPanelHandler selectorPanelHandler;

    public GhostCircuitSlotWidget(IMetaTileEntity mte, IntSyncValue selectedSyncHandler) {
        super();
        this.mte = mte;
        this.selectedSyncHandler = selectedSyncHandler;
        tooltipBuilder(this::getCircuitSlotTooltip);
    }

    @Override
    public IDrawable getCurrentBackground(ITheme theme, WidgetTheme widgetTheme) {
        IDrawable background = super.getCurrentBackground(theme, widgetTheme);
        return new DrawableArray(background, GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT);
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (!isSelectorPanelOpen()) {
            if (mouseButton == 0 && Interactable.hasShiftDown()) {
                openSelectorPanel();
            } else {
                MouseData mouseData = MouseData.create(mouseButton);
                getSyncHandler().syncToServer(2, mouseData::writeToPacket);
            }
        }
        return Result.SUCCESS;
    }

    @Override
    public boolean onMouseRelease(int mouseButton) {
        return true;
    }

    @Override
    public boolean onMouseScroll(ModularScreen.UpOrDown scrollDirection, int amount) {
        if (isSelectorPanelOpen()) return true;
        MouseData mouseData = MouseData.create(scrollDirection.modifier);
        getSyncHandler().syncToServer(3, mouseData::writeToPacket);
        return true;
    }

    @Override
    public void onMouseDrag(int mouseButton, long timeSinceClick) {}

    @Override
    public ItemSlot slot(ModularSlot slot) {
        ItemSlotSH sh = new GhostCircuitSyncHandler(slot);
        isValidSyncHandler(sh);
        setSyncHandler(sh);
        return this;
    }

    private void getCircuitSlotTooltip(RichTooltip tooltip) {
        int config = getSyncHandler().getGhostCircuitHandler()
            .getCircuitConfig();
        String configString;
        if (config == NO_CONFIG) {
            configString = IKey.lang("GT5U.machines.select_circuit.tooltip.configuration.none")
                .get();
        } else {
            configString = String.valueOf(config);
        }
        tooltip.clearText()
            .addLine(IKey.lang("GT5U.machines.select_circuit.tooltip"))
            .spaceLine(2)
            .addLine(IKey.lang("GT5U.machines.select_circuit.tooltip.configuration", configString))
            .addLine(IKey.lang("GT5U.machines.select_circuit.tooltip.1"))
            .addLine(IKey.lang("GT5U.machines.select_circuit.tooltip.2"))
            .addLine(IKey.lang("GT5U.machines.select_circuit.tooltip.3"));
    }

    @NotNull
    @Override
    public GhostCircuitSyncHandler getSyncHandler() {
        return (GhostCircuitSyncHandler) super.getSyncHandler();
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
        return IPanelHandler.simple(getPanel(), (mainPanel, player) -> {
            ModularPanel panel = GTGuis.createPopUpPanel(GUI_ID);
            return new SelectItemGuiBuilder(panel, GTUtility.getAllIntegratedCircuits()) //
                .setHeaderItem(mte.getStackForm(1))
                .setTitle(IKey.lang("GT5U.machines.select_circuit"))
                .setSelectedSyncHandler(selectedSyncHandler)
                .setOnSelectedClientAction((selected, mouseData) -> {
                    getSyncHandler().syncToServer(SYNC_CIRCUIT_CONFIG, buffer -> {
                        // selected index 0 == config 1
                        int circuitConfig = selected == DESELECTED ? -1 : selected + 1;
                        buffer.writeShort(circuitConfig);
                    });
                    if (mouseData.shift) {
                        panel.animateClose();
                    }
                })
                .setCurrentItemSlotOverlay(GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT)
                .setAllowDeselected(true)
                .build();
        }, true);
    }
}
