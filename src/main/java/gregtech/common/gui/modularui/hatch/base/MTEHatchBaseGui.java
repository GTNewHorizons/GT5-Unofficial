package gregtech.common.gui.modularui.hatch.base;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

/**
 * A base class for common hatch implementations. Has configurable corner panels and makes building ui's easier.
 * The main overriding is done in
 * {@link #createContentSection(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
 *
 * For heavily custom UI's, override
 * {@link #build(com.cleanroommc.modularui.factory.PosGuiData, com.cleanroommc.modularui.value.sync.PanelSyncManager, com.cleanroommc.modularui.screen.UISettings)}
 * instead.
 */
public class MTEHatchBaseGui<T extends MTEHatch> {

    protected final T hatch;
    protected final IGregTechTileEntity baseMetaTileEntity;

    public MTEHatchBaseGui(T hatch) {
        this.hatch = hatch;
        this.baseMetaTileEntity = hatch.getBaseMetaTileEntity();
    }

    private final int borderRadius = 4;

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);

        return panel.child(
            Flow.column()
                .sizeRel(1)
                .padding(borderRadius)
                .child(createContentHolderRow(panel, syncManager))
                .child(createInventoryRow(panel, syncManager)));
    }

    // override for cross-panel syncing
    public void registerSyncValues(PanelSyncManager syncManager) {

    }

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(hatch, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            .doesAddGhostCircuitSlot(false)
            // Has to be replaced with inventory row for alignment reasons
            .doesBindPlayerInventory(false)
            .build();
    }

    protected int getBasePanelWidth() {
        return 176;
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
            .childIf(this.supportsLeftCornerFlow(), createLeftCornerFlow(panel, syncManager))
            .childIf(this.supportsRightCornerFlow(), createRightCornerFlow(panel, syncManager));
    }

    protected int getContentRowWidth() {
        return getBasePanelWidth() - 2 * borderRadius;
    }

    protected int getContentRowHeight() {
        return (getBasePanelHeight() - borderRadius * 2) - (hatch.doesBindPlayerInventory() ? 80 : 0);
    }

    // Row by default, going left to right
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = Flow.row()
            .coverChildren()
            .align(Alignment.BottomLeft)
            .paddingBottom(4)
            .paddingLeft(3);
        return cornerFlow;
    }

    protected boolean supportsLeftCornerFlow() {
        return false;
    }

    // Column by defualt, going bottom to up (reversed child order)
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = Flow.column()
            .coverChildren()
            .reverseLayout(true)
            .align(Alignment.BottomRight)
            .paddingBottom(2)
            .paddingRight(5);
        cornerFlow.childIf(this.doesAddGregTechLogo(), createLogo())
            .childIf(this.doesAddCircuitSlot(), createCircuitSlot(syncManager));
        return cornerFlow;
    }

    protected boolean supportsRightCornerFlow() {
        return true;
    }

    protected boolean doesAddGregTechLogo() {
        return true;
    }

    protected UITexture getLogoTexture() {
        return GTGuiTextures.OVERLAY_GREGTECH_LOGO;
    }

    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(getLogoTexture()).size(18)
            .marginTop(2);
    }

    protected boolean doesAddCircuitSlot() {
        return hatch instanceof IConfigurationCircuitSupport circuitSupport && circuitSupport.allowSelectCircuit();
    }

    protected IWidget createCircuitSlot(PanelSyncManager syncManager) {
        if (!(hatch instanceof IConfigurationCircuitSupport)) return IDrawable.EMPTY.asWidget()
            .size(18);

        IntSyncValue selectedSyncHandler = new IntSyncValue(() -> {
            ItemStack selectedItem = hatch.getStackInSlot(hatch.getCircuitSlot());
            if (selectedItem != null && selectedItem.getItem() instanceof ItemIntegratedCircuit) {
                // selected index 0 == config 1
                return selectedItem.getItemDamage() - 1;
            }
            return -1;
        });
        syncManager.syncValue("selector_screen_selected", selectedSyncHandler);
        return new GhostCircuitSlotWidget(hatch, syncManager)
            .slot(new ModularSlot(new GhostCircuitItemStackHandler(hatch), 0));
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .height(76)
            .alignX(0)
            .childIf(
                hatch.doesBindPlayerInventory(),
                SlotGroupWidget.playerInventory(false)
                    .align(Alignment.CENTER));
    }

}
