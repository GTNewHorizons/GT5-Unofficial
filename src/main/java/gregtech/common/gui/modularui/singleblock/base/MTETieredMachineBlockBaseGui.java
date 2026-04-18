package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.gui.widgets.CommonWidgets;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;

// For singleblock MUI2 guis.
public class MTETieredMachineBlockBaseGui<T extends MTETieredMachineBlock> {

    protected final T machine;
    protected final IGregTechTileEntity baseMetaTileEntity;

    public MTETieredMachineBlockBaseGui(T machine) {
        this.machine = machine;
        this.baseMetaTileEntity = machine.getBaseMetaTileEntity();
    }

    private final int borderRadius = 4;
    private final int buttonSize = 18;

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        ModularPanel panel = this.getBasePanel(guiData, syncManager, uiSettings);

        return panel.child(
            Flow.column()
                .child(createTopRightCornerColumn())
                .sizeRel(1)
                .padding(borderRadius)
                .child(createContentHolderRow(panel, syncManager))
                .childIf(machine.supportsInventoryRow(), () -> createInventoryRow(panel, syncManager)));
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {

        syncManager.registerSlotGroup("item_inv", 1, false);

        BooleanSyncValue powerSwitchSyncer = new BooleanSyncValue(baseMetaTileEntity::isAllowedToWork, bool -> {
            if (bool) baseMetaTileEntity.enableWorking();
            else baseMetaTileEntity.disableWorking();
        });
        syncManager.syncValue("powerSwitch", powerSwitchSyncer);

        BooleanSyncValue mufflerSyncer = new BooleanSyncValue(
            baseMetaTileEntity::isMuffled,
            baseMetaTileEntity::setMuffler);
        syncManager.syncValue("mufflerSyncer", mufflerSyncer);

    }

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(machine, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            .doesAddGhostCircuitSlot(false)
            // Has to be replaced with inventory row for alignment reasons
            .doesBindPlayerInventory(false)
            .build();
    }

    protected int getBasePanelWidth() {
        return 178;
    }

    protected int getBasePanelHeight() {
        return 166;
    }

    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow contentFlow = Flow.row()
            .size(getContentRowWidth(), getContentRowHeight());
        contentFlow.child(createContentSection(panel, syncManager));
        return contentFlow;
    }

    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().sizeRel(1)
            .childIf(this.supportsLeftCornerFlow(), () -> createLeftCornerFlow(panel, syncManager))
            .childIf(this.supportsRightCornerFlow(), () -> createRightCornerFlow(panel, syncManager));
    }

    protected int getContentRowWidth() {
        return getBasePanelWidth() - 2 * borderRadius;
    }

    protected int getContentRowHeight() {
        return (getBasePanelHeight() - borderRadius * 2) - (machine.doesBindPlayerInventory() ? 80 : 0);
    }

    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    // Row by default, going left to right
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = Flow.row()
            .coverChildren()
            .align(Alignment.BottomLeft)
            .paddingLeft(4);
        return cornerFlow;
    }

    protected boolean supportsRightCornerFlow() {
        return true;
    }

    // Row by default, going left to right
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .coverChildren()
            .align(Alignment.BottomRight)
            .paddingBottom(2)
            .paddingRight(4);

        cornerFlow.childIf(this.doesAddGregTechLogo(), this::createLogo)
            .childIf(this.doesAddCircuitSlot(), () -> this.createCircuitSlot(syncManager));

        return cornerFlow;
    }

    protected boolean doesAddGregTechLogo() {
        return true;
    }

    protected UITexture getLogoTexture() {
        return GTGuiTextures.OVERLAY_GREGTECH_LOGO;
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .height(76)
            .alignX(0)
            .childIf(
                machine.doesBindPlayerInventory(),
                () -> SlotGroupWidget.playerInventory(false)
                    .marginLeft(4));
    }

    protected boolean doesAddSpecialSlot() {
        return true;
    }

    // by default, adds an empty widget, things can override this to add anything in the bottom right corner
    // typically, this is used for the 'special slot' on singleblocks
    protected Widget<? extends Widget<?>> createSpecialSlot() {
        return IDrawable.EMPTY.asWidget()
            .size(18)
            .marginTop(4);
    }

    protected boolean supportsPowerSwitch() {
        return true;
    }

    protected boolean supportsMuffler() {
        return true;
    }

    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(getLogoTexture()).size(18)
            .marginLeft(2);
    }

    // will add if the machine is an instance of IConfigurationCircuitSupport
    protected boolean doesAddCircuitSlot() {
        return machine instanceof IConfigurationCircuitSupport cc && cc.allowSelectCircuit();
    }

    protected Widget<? extends Widget<?>> createCircuitSlot(PanelSyncManager syncManager) {
        return CommonWidgets.createCircuitSlot(syncManager, machine)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected Flow createTopRightCornerColumn() {
        return Flow.column()
            .coverChildren()
            .align(Alignment.TopRight)
            .childIf(supportsMuffler(), this::createMufflerButton)
            .childIf(supportsPowerSwitch(), this::createPowerSwitchButton);
    }

    protected ToggleButton createMufflerButton() {
        return CommonWidgets.createMuffleButton("mufflerSyncer")
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true);
    }

    protected ToggleButton createPowerSwitchButton() {
        return CommonWidgets.createSmallPowerSwitchButton("powerSwitch", machine.getBaseMetaTileEntity())
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

}
