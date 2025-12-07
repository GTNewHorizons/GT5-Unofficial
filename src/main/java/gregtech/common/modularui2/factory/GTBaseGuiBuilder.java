package gregtech.common.modularui2.factory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.gui.widgets.CommonWidgets;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.covers.Cover;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.widget.CoverTabButton;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

// spotless:off
/**
 * Builder class for creating GT GUI with standard functionalities like title, cover tabs and logo. Whether to use each
 * feature can be configured.
 * <p>
 * Example usage:
 * <pre>{@code
 *     @Overrride
 *     public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
 *         return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
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
    private final UISettings uiSettings;

    private int width = 176;
    private int height = 166;
    private boolean doesBindPlayerInventory = true;
    private boolean doesAddTitle = true;
    private boolean doesAddCoverTabs = true;
    private boolean doesAddGhostCircuitSlot;
    private boolean doesAddGregTechLogo;
    private int gregtechLogoPosX = 152;
    private int gregtechLogoPosY = 63;

    public GTBaseGuiBuilder(IMetaTileEntity mte, PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        this.mte = mte;
        this.posGuiData = data;
        this.syncManager = syncManager;
        this.uiSettings = uiSettings;
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
     * Sets a new position for the Gregtech Logo
     */
    public GTBaseGuiBuilder moveGregtechLogoPos(int X, int Y) {
        this.gregtechLogoPosX = X;
        this.gregtechLogoPosY = Y;
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
            panel.child(CommonWidgets.createMachineTitle(mte, width));
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
        syncManager.addCloseListener($ -> mte.markDirty());
        return panel;
    }

    private IWidget createCoverTabs() {
        Flow column = Flow.column()
            .coverChildren()
            .leftRel(0f, 2, 1f)
            .top(1)
            .childPadding(2);
        for (int i = 0; i < 6; i++) {
            column.child(getCoverTabButton(mte.getBaseMetaTileEntity(), ForgeDirection.getOrientation(i)));
        }
        uiSettings.getRecipeViewerSettings()
            .addExclusionArea(column);
        return column;
    }

    private @NotNull CoverTabButton getCoverTabButton(ICoverable coverable, ForgeDirection side) {
        return new CoverTabButton(coverable, side, getCoverPanel(coverable, side));
    }

    private IPanelHandler getCoverPanel(ICoverable coverable, ForgeDirection side) {
        String panelKey = "cover_panel_" + side.toString()
            .toLowerCase();
        Cover cover = coverable.getCoverAtSide(side);

        CoverGuiData coverGuiData = new CoverGuiData(
            posGuiData.getPlayer(),
            cover.getCoverID(),
            posGuiData.getX(),
            posGuiData.getY(),
            posGuiData.getZ(),
            side);
        return syncManager.panel(
            panelKey,
            (syncManager, syncHandler) -> coverable.getCoverAtSide(side)
                .buildPopUpUI(coverGuiData, panelKey, syncManager, uiSettings)
                .child(ButtonWidget.panelCloseButton()),
            true);
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
        return new GhostCircuitSlotWidget(mte, syncManager)
            .slot(new ModularSlot(new GhostCircuitItemStackHandler(mte), 0))
            .pos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1);
    }

    private IWidget createGregTechLogo() {
        return new Widget<>().widgetTheme(GTWidgetThemes.PICTURE_LOGO)
            .size(17, 17) // todo: size
            .pos(gregtechLogoPosX, gregtechLogoPosY);
    }
}
