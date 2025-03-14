package gregtech.common.modularui2.factory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ContainerCustomizer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

// spotless:off
/**
 * Builder class for creating GT GUI with standard functionalities like title, cover tabs and logo. Whether to use each
 * feature can be configured.
 * <p>
 * Example usage:
 * <pre>{@code
 *     @Overrride
 *     public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
 *         return GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
 *             .setHeight(200)
 *             .doesBindPlayerInventory(false)
 *             .build()
 *             .child(...);
 *     }
 * }</pre>
 *
 * @see GTGuis
 */
// spotless:on
public final class GTBaseGuiBuilder {

    private final IMetaTileEntity mte;
    private final PosGuiData posGuiData;
    private final PanelSyncManager syncManager;

    private int width = 176;
    private int height = 166;
    private boolean doesBindPlayerInventory = true;
    private boolean doesAddTitle = true;
    private boolean doesAddCoverTabs = true;
    private boolean doesAddGhostCircuitSlot;
    private boolean doesAddGregTechLogo;

    public GTBaseGuiBuilder(IMetaTileEntity mte, PosGuiData data, PanelSyncManager syncManager) {
        this.mte = mte;
        this.posGuiData = data;
        this.syncManager = syncManager;
        this.doesAddGhostCircuitSlot = mte instanceof IConfigurationCircuitSupport ccs && ccs.allowSelectCircuit();
        this.doesAddGregTechLogo = !this.doesAddGhostCircuitSlot;
    }

    /**
     * Sets width of the GUI. 176 by default.
     */
    public GTBaseGuiBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets height of the GUI. 166 by default.
     */
    public GTBaseGuiBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * Sets whether to add player inventory to the GUI. Enabled by default.
     */
    public GTBaseGuiBuilder doesBindPlayerInventory(boolean doesBindPlayerInventory) {
        this.doesBindPlayerInventory = doesBindPlayerInventory;
        return this;
    }

    /**
     * Sets whether to add title tab to the GUI. Enabled by default.
     */
    public GTBaseGuiBuilder doesAddTitle(boolean doesAddTitle) {
        this.doesAddTitle = doesAddTitle;
        return this;
    }

    /**
     * Sets whether to add cover tabs to the GUI. Enabled by default.
     */
    public GTBaseGuiBuilder doesAddCoverTabs(boolean doesAddCoverTabs) {
        this.doesAddCoverTabs = doesAddCoverTabs;
        return this;
    }

    /**
     * Sets whether to add ghost circuit slot to the GUI. You probably don't need to call this since this builder
     * automatically enables/disables the feature by checking {@link IConfigurationCircuitSupport#allowSelectCircuit}.
     */
    public GTBaseGuiBuilder doesAddGhostCircuitSlot(boolean doesAddGhostCircuitSlot) {
        this.doesAddGhostCircuitSlot = doesAddGhostCircuitSlot;
        return this;
    }

    /**
     * Sets whether to add ghost circuit slot to the GUI. Enabled by default when machine is not an instance of
     * {@link IConfigurationCircuitSupport}.
     */
    public GTBaseGuiBuilder doesAddGregTechLogo(boolean doesAddGregTechLogo) {
        this.doesAddGregTechLogo = doesAddGregTechLogo;
        return this;
    }

    /**
     * Builds the resulting panel. Call after calling all the necessary feature switch methods.
     */
    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel(mte.getGuiId(), width, height);
        if (doesBindPlayerInventory) {
            panel.bindPlayerInventory();
        }
        if (doesAddTitle && NetworkUtils.isClient()) {
            panel.child(createTitle());
        }
        if (doesAddCoverTabs) {
            panel.child(createCoverTabs());
        }
        if (doesAddGhostCircuitSlot) {
            panel.child(createGhostCircuitSlot());
        }
        if (doesAddGregTechLogo) {
            panel.child(createGregTechLogo());
        }
        syncManager.setContainerCustomizer(new ContainerCustomizer());
        syncManager.getContainerCustomizer()
            .setCanInteractWith($ -> {
                IGregTechTileEntity gtTE = mte.getBaseMetaTileEntity();
                return gtTE != null && gtTE.canAccessData();
            });
        syncManager.addCloseListener($ -> mte.markDirty());
        return panel;
    }

    private IWidget createTitle() {
        String title = mte.getLocalName();
        return new ParentWidget<>().coverChildren()
            .topRelAnchor(0, 1)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
            .child(
                IKey.str(title)
                    .asWidget()
                    .widgetTheme(GTWidgetThemes.TEXT_TITLE)
                    .marginLeft(5)
                    .marginRight(5)
                    .marginTop(5)
                    .marginBottom(1));
    }

    private IWidget createCoverTabs() {
        Flow column = Flow.column()
            .coverChildren()
            .leftRel(0, 2, 1)
            .top(1)
            .childPadding(2);
        for (int i = 0; i < 6; i++) {
            ForgeDirection side = ForgeDirection.getOrientation(i);
            String panelKey = "cover_panel_" + side.toString()
                .toLowerCase();
            IPanelHandler panel = syncManager.panel(panelKey, coverPanelBuilder(panelKey, side), true);
            column.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                panel.openPanel();
                return true;
            })
                .overlay(
                    new ItemDrawable(ItemList.Electric_Pump_LV.get(1)).asIcon()
                        .marginLeft(2)
                        .marginTop(1))
                // todo: change background
                .widgetTheme(GTWidgetThemes.BACKGROUND_COVER_TAB_NORMAL)
                .size(18, 20));
        }
        posGuiData.getNEISettings()
            .addNEIExclusionArea(column);
        return column;
    }

    private PanelSyncHandler.IPanelBuilder coverPanelBuilder(String name, ForgeDirection side) {
        // todo: actual cover panel
        return (syncManager, syncHandler) -> GTGuis.createPopUpPanel(name)
            .size(176, 107)
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(4)
                    .center()
                    .child(
                        IKey.str(
                            "Cover Panel " + side.toString()
                                .toLowerCase())
                            .asWidget())
                    .child(
                        new ItemDrawable(ItemList.GigaChad.get(1)).asIcon()
                            .asWidget()));
    }

    private IWidget createGhostCircuitSlot() {
        if (!(mte instanceof IConfigurationCircuitSupport ccs) || !ccs.allowSelectCircuit()) {
            throw new IllegalStateException("Tried to add configuration circuit slot to an unsupported machine!");
        }

        IntSyncValue selectedSyncHandler = new IntSyncValue(() -> {
            ItemStack selectedItem = mte.getStackInSlot(ccs.getCircuitSlot());
            if (selectedItem != null && selectedItem.getItem() instanceof ItemIntegratedCircuit) {
                // selected index 0 == config 1
                return selectedItem.getItemDamage() - 1;
            }
            return -1;
        });
        syncManager.syncValue("selector_screen_selected", selectedSyncHandler);
        return new GhostCircuitSlotWidget(mte, selectedSyncHandler)
            .slot(new ModularSlot(new GhostCircuitItemStackHandler(mte), 0, true))
            .pos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1);
    }

    private IWidget createGregTechLogo() {
        return new Widget<>().widgetTheme(GTWidgetThemes.PICTURE_LOGO)
            .size(17, 17) // todo: size
            .pos(152, 63);
    }
}
