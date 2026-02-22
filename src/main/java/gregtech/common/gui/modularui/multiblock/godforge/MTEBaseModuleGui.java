package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.gui.widgets.CommonWidgets;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;

public abstract class MTEBaseModuleGui<T extends MTEBaseModule> extends TTMultiblockBaseGui<T> {

    protected final SyncHypervisor hypervisor;

    public MTEBaseModuleGui(T multiblock) {
        super(multiblock);
        this.hypervisor = new SyncHypervisor(getModuleType(), getMainPanel());

        hypervisor.setModule(getModuleType(), multiblock);
    }

    public abstract Modules<T> getModuleType();

    public Panels getMainPanel() {
        return getModuleType().getMainPanel();
    }

    @Override
    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = super.getBasePanel(guiData, syncManager, uiSettings);
        hypervisor.setModularPanel(getMainPanel(), panel);
        return panel;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        hypervisor.setSyncManager(getMainPanel(), syncManager);

        SyncValues.CONNECTION_STATUS.registerFor(getModuleType(), getMainPanel(), hypervisor);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -3, 1)
            .childPadding(2)
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .slotGroup("item_inv"))
                    .background(GuiTextures.SLOT_ITEM, GTGuiTextures.TT_OVERLAY_SLOT_MESH)
                    .overlay(
                        GTGuiTextures.TT_CONTROLLER_SLOT_HEAT_SINK.asIcon()
                            .size(18, 6)
                            .marginTop(22))
                    .marginTop(2))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager))
            .child(createVoltageConfigButton());
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createGeneralInfoPanelButton());
    }

    @Override
    protected Flow createTerminalLeftCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalLeftCornerColumn(panel, syncManager)
            .childIf(usesTerminalLeftButton(), this::createTerminalLeftButton);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).childPadding(2)
            .marginLeft(1)
            .child(createConnectionStatus());
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).marginRight(2)
            .childIf(usesExtraButton(), this::createExtraButton);
    }

    @Override
    protected int getTextBoxToInventoryGap() {
        return 20;
    }

    @Override
    protected ToggleButton createMuffleButton() {
        return CommonWidgets.createMuffleButton("mufflerSyncer")
            .size(7)
            .background(IDrawable.EMPTY)
            .overlay(true, GTGuiTextures.GODFORGE_SOUND_OFF)
            .overlay(false, GTGuiTextures.GODFORGE_SOUND_ON)
            .top(8)
            .right(8)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected ToggleButton createPowerSwitchButton() {
        return super.createPowerSwitchButton().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .selectedBackground(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.isAllowedToWork()) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON;
                }
                // Disabled instead of off because it has
                // better contrast with the background
                return GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
            }))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        return ((ToggleButton) super.createStructureUpdateButton(syncManager)).size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .selectedBackground(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.getStructureUpdateTime() > -20) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_STRUCTURE_CHECK;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_STRUCTURE_CHECK_OFF;
            }))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    protected ButtonWidget<?> createVoltageConfigButton() {
        IPanelHandler voltageConfigPanel = Panels.VOLTAGE_CONFIG.getFrom(getModuleType(), getMainPanel(), hypervisor);
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_POWER_PANEL)
            .onMousePressed(d -> {
                if (!voltageConfigPanel.isPanelOpen()) {
                    voltageConfigPanel.openPanel();
                } else {
                    voltageConfigPanel.closePanel();
                }
                return true;
            })
            .tooltip(t -> t.addLine(translateToLocal("GT5U.gui.button.power_panel")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createVoidExcessButton(PanelSyncManager syncManager) {
        return ((ButtonWidget<?>) super.createVoidExcessButton(syncManager)).size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            // spotless:off
            .overlay(new DynamicDrawable(() ->
                switch (multiblock.getVoidingMode()) {
                    case VOID_NONE -> GTGuiTextures.TT_OVERLAY_BUTTON_VOIDING_OFF;
                    case VOID_ITEM -> GTGuiTextures.TT_OVERLAY_BUTTON_VOIDING_ITEMS;
                    case VOID_FLUID -> GTGuiTextures.TT_OVERLAY_BUTTON_VOIDING_FLUIDS;
                    case VOID_ALL -> GTGuiTextures.TT_OVERLAY_BUTTON_VOIDING_BOTH;
                }))
            // spotless:on
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createInputSeparationButton(PanelSyncManager syncManager) {
        return ((ToggleButton) super.createInputSeparationButton(syncManager)).size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .selectedBackground(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.isInputSeparationEnabled()) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_INPUT_SEPARATION;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_INPUT_SEPARATION_OFF;
            }))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createBatchModeButton(PanelSyncManager syncManager) {
        return ((ToggleButton) super.createBatchModeButton(syncManager)).size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .selectedBackground(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.isBatchModeEnabled()) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_BATCH_MODE;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_BATCH_MODE_OFF;
            }))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createLockToSingleRecipeButton(PanelSyncManager syncManager) {
        if (!usesLockToSingleRecipeButton()) {
            return new Widget<>();
        }

        return ((ToggleButton) super.createLockToSingleRecipeButton(syncManager)).size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .selectedBackground(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.isRecipeLockingEnabled()) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_RECIPE_LOCKED;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_RECIPE_UNLOCKED;
            }))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    protected IWidget createConnectionStatus() {
        BooleanSyncValue connectionSyncer = SyncValues.CONNECTION_STATUS
            .lookupFrom(getModuleType(), getMainPanel(), hypervisor);
        return IKey.dynamic(() -> {
            EnumChatFormatting color;
            String status;
            if (connectionSyncer.getBoolValue()) {
                color = EnumChatFormatting.GREEN;
                status = translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus.true");
            } else {
                color = EnumChatFormatting.RED;
                status = translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus.false");
            }
            return translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus") + " " + color + status;
        })
            .style(EnumChatFormatting.BLACK)
            .alignment(Alignment.CENTER)
            .asWidget()
            .size(86, 16);
    }

    protected ButtonWidget<?> createGeneralInfoPanelButton() {
        IPanelHandler generalInfoPanel = Panels.GENERAL_INFO.getFrom(getModuleType(), getMainPanel(), hypervisor);
        return new ButtonWidget<>().size(18)
            .overlay(IDrawable.EMPTY)
            .background(GTGuiTextures.PICTURE_GODFORGE_LOGO)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!generalInfoPanel.isPanelOpen()) {
                    generalInfoPanel.openPanel();
                } else {
                    generalInfoPanel.closePanel();
                }
                return true;
            })
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.clickhere")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createExtraButton() {
        return new Widget<>();
    }

    protected IWidget createTerminalLeftButton() {
        return new Widget<>();
    }

    protected boolean usesExtraButton() {
        return false;
    }

    protected boolean usesTerminalLeftButton() {
        return false;
    }

    protected boolean usesLockToSingleRecipeButton() {
        return true;
    }
}
