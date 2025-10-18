package gregtech.common.gui.modularui.multiblock.base;

import static tectech.Reference.MODID;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import tectech.thing.metaTileEntity.multi.base.IParametrized;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class TTMultiblockBaseGui extends MTEMultiBlockBaseGui<TTMultiblockBase> {

    public TTMultiblockBaseGui(TTMultiblockBase multiblock) {
        super(multiblock);
    }

    @Override
    protected void initCustomIcons() {
        this.customIcons.put("power_switch_disabled", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
        this.customIcons.put("power_switch_on", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON);
        this.customIcons.put("power_switch_off", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        UITexture mesh = UITexture.builder()
            .location(MODID, "gui/overlay_slot/mesh")
            .canApplyTheme()
            .build();
        UITexture heatSinkSmall = UITexture.builder()
            .location(MODID, "gui/picture/heat_sink_small")
            .canApplyTheme()
            .build();

        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(createPowerPassButton())
            .child(createEditParametersButton(panel, syncManager))
            .child(createPowerSwitchButton())
            .childIf(
                multiblock.doesBindPlayerInventory(),
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .slotGroup("item_inv"))
                    .background(new DrawableStack(GuiTextures.SLOT_ITEM, mesh))
                    .overlay(
                        heatSinkSmall.asIcon()
                            .size(18, 6)
                            .marginTop(22)));
    }

    protected IWidget createPowerPassButton() {
        return new ToggleButton().value(new BooleanSyncValue(() -> multiblock.ePowerPass, bool -> {
            if (isPowerSwitchDisabled()) return;
            multiblock.ePowerPass = bool;
            if (isPowerSwitchDisabled()) { // TRANSFORMER HACK
                if (multiblock.ePowerPass) {
                    multiblock.getBaseMetaTileEntity()
                        .enableWorking();
                } else {
                    multiblock.getBaseMetaTileEntity()
                        .disableWorking();
                }
            }
        }))
            .tooltip(tooltip -> tooltip.add("Power Pass"))
            .size(18, 18)
            .overlay(
                new DynamicDrawable(
                    () -> isPowerSwitchDisabled() ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_DISABLED
                        : multiblock.ePowerPass ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_ON
                            : GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF));

    }

    protected IWidget createEditParametersButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager
            .panel("info_panel", (p_syncManager, syncHandler) -> getParameterPanel(panel, p_syncManager), true);
        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
            if (multiblock instanceof IParametrized) {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_DISABLED.asIcon()
                    .size(16, 16);
            } else {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_ENABLED.asIcon()
                    .size(16, 16);
            }
        }))
            .tooltipBuilder(t -> t.add("Edit Parameters"))
            .size(18, 18)
            .onMousePressed(mouseData -> {
                if (multiblock instanceof IParametrized) return false;
                if (!infoPanel.isPanelOpen()) {
                    infoPanel.openPanel();
                } else {
                    infoPanel.closePanel();
                }
                return true;
            });
    }

    // Panel implementation will come with first parametrized multiblock port
    private ModularPanel getParameterPanel(ModularPanel parent, PanelSyncManager syncManager) {
        return new ModularPanel("parameters") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(125, 191)
            .relative(parent)
            .rightRel(0, 0, 1)
            .topRel(0);
    }

}
