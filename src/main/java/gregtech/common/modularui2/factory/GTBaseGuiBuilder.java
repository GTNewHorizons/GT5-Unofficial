package gregtech.common.modularui2.factory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
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
    private boolean doesAddMufflerButton = true;
    private int mufflerPosFromTop = 0;
    private int mufflerPosFromRightOutwards = 13;

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

    public GTBaseGuiBuilder doesAddMufflerButton(boolean doesAddMufflerButton) {
        this.doesAddMufflerButton = doesAddMufflerButton;
        return this;
    }

    public GTBaseGuiBuilder setMufflerPosFromTop(int mufflerPosFromTop) {
        this.mufflerPosFromTop = mufflerPosFromTop;
        return this;
    }

    public GTBaseGuiBuilder setMufflerPosFromRightOutwards(int mufflerPosFromRightOutwards) {
        this.mufflerPosFromRightOutwards = mufflerPosFromRightOutwards;
        return this;
    }

    public GTBaseGuiBuilder setMufflerPos(int mufflerPosFromTop, int mufflerPosFromRightOutwards) {
        return setMufflerPosFromTop(mufflerPosFromTop).setMufflerPosFromRightOutwards(mufflerPosFromRightOutwards);
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
        if (doesAddMufflerButton && mte instanceof MTEMultiBlockBase) {
            panel.child(createMufflerButton());
        }
        syncManager.addCloseListener($ -> mte.markDirty());
        return panel;
    }

    private IWidget createMufflerButton() {
        return new ToggleButton().syncHandler("mufflerSyncer")
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.machines.muffled")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_OFF)
            .size(12, 12)
            .top(mufflerPosFromTop)
            .right(-mufflerPosFromRightOutwards)
            .excludeAreaInRecipeViewer(true);
    }

    private IWidget createTitle() {
        String title = mte.getLocalName();

        int borderRadius = 5;
        int maxWidth = width - borderRadius * 2;

        int titleWidth = TextRenderer.getFontRenderer()
            .getStringWidth(title);
        int widgetWidth = Math.min(maxWidth, titleWidth);

        int rows = (int) Math.ceil((double) titleWidth / maxWidth);
        int heightPerRow = (int) (IKey.renderer.getFontHeight());
        int height = heightPerRow * rows;

        return new SingleChildWidget<>().coverChildren()
            .topRelAnchor(0, 1)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
            .child(
                IKey.str(title)
                    .asWidget()
                    .size(widgetWidth, height)
                    .widgetTheme(GTWidgetThemes.TEXT_TITLE)
                    .marginLeft(5)
                    .marginRight(5)
                    .marginTop(5)
                    .marginBottom(1));
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
            .addRecipeViewerExclusionArea(column);
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
        return new GhostCircuitSlotWidget(mte, selectedSyncHandler)
            .slot(new ModularSlot(new GhostCircuitItemStackHandler(mte), 0))
            .pos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1);
    }

    private IWidget createGregTechLogo() {
        return new Widget<>().widgetTheme(GTWidgetThemes.PICTURE_LOGO)
            .size(17, 17) // todo: size
            .pos(152, 63);
    }
}
