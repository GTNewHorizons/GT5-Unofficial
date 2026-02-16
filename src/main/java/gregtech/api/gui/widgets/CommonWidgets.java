package gregtech.api.gui.widgets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

/**
 * A class of commonly used widgets between all forms of MUI2.
 */
public class CommonWidgets {

    /**
     * Returns a muffle button with the provided syncHandler
     * For unsynced sync handlers. Use {@link #createMuffleButton(String)} with a key for synced handlers
     *
     * @param syncValue - sync handler, can not be synced to manager or will crash
     * @return a muffle button with the attached sync value
     */
    public static ToggleButton createMuffleButton(BooleanSyncValue syncValue) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.machines.muffled")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a muffle toggle button with the provided syncKey
     *
     * @param syncKey - key of synced BooleanSyncValue
     * @return a muffle button with the attached sync value
     */
    public static ToggleButton createMuffleButton(String syncKey) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.machines.muffled")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a power toggle button with the syncValue
     *
     * @param syncValue             - value of unsynced Sync value
     * @param isPowerSwitchDisabled - is power switch disabled
     * @param baseMetaTileEntity    - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createPowerSwitchButton(BooleanSyncValue syncValue, boolean isPowerSwitchDisabled,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(StatCollector.translateToLocal("GT5U.gui.button.power_switch")))
            .size(18, 18)
            .overlay(new DynamicDrawable(() -> {
                if (isPowerSwitchDisabled) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
                if (baseMetaTileEntity.isAllowedToWork()) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON;
                return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
            }));
    }

    /**
     * Returns a power toggle button with the syncKey
     *
     * @param syncKey               - key of synced BooleanSyncValue
     * @param isPowerSwitchDisabled - is power switch disabled
     * @param baseMetaTileEntity    - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createPowerSwitchButton(String syncKey, boolean isPowerSwitchDisabled,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(StatCollector.translateToLocal("GT5U.gui.button.power_switch")))
            .size(18, 18)
            .overlay(new DynamicDrawable(() -> {
                if (isPowerSwitchDisabled) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
                if (baseMetaTileEntity.isAllowedToWork()) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON;
                return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
            }));
    }

    /**
     * Returns a small, 12x12 power toggle button with the syncKey
     *
     * @param syncValue          - unsynced BooleanSyncValue
     * @param baseMetaTileEntity - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createSmallPowerSwitchButton(BooleanSyncValue syncValue,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.gui.button.power_switch")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a small, 12x12 power toggle button with the syncKey
     *
     * @param syncKey            - key of synced value
     * @param baseMetaTileEntity - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createSmallPowerSwitchButton(String syncKey, IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.gui.button.power_switch")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     *
     * @return a button that when clicked, closes the panel its on
     */
    public static ButtonWidget<?> panelCloseButton() {
        return ButtonWidget.panelCloseButton();
    }

    /**
     * Returns a ghost circuit slot widget. baseMachine should be an instance of IConfigurationCircuitSupport
     * Otherwise, returns an empty widget
     *
     * @param syncManager - manager
     * @param baseMachine - meta tile entity
     * @return ghost circuit slot widget
     */
    public static Widget<? extends Widget<?>> createCircuitSlot(PanelSyncManager syncManager,
        IMetaTileEntity baseMachine) {
        if (baseMachine instanceof IConfigurationCircuitSupport circuitEnabled && circuitEnabled.allowSelectCircuit()) {
            IntSyncValue selectedSyncHandler = new IntSyncValue(() -> {
                ItemStack selectedItem = baseMachine.getInventoryHandler()
                    .getStackInSlot(circuitEnabled.getCircuitSlot());
                if (selectedItem != null && selectedItem.getItem() instanceof ItemIntegratedCircuit) {
                    // selected index 0 == config 1
                    return selectedItem.getItemDamage() - 1;
                }
                return -1;
            }, index -> {
                if (index != -1) {
                    baseMachine.setInventorySlotContents(
                        circuitEnabled.getCircuitSlot(),
                        GTUtility.getAllIntegratedCircuits()
                            .get(index)
                            .copy());
                } else {
                    baseMachine.setInventorySlotContents(circuitEnabled.getCircuitSlot(), null);
                }
            });
            syncManager.syncValue("selector_screen_selected", selectedSyncHandler);
            return new GhostCircuitSlotWidget(baseMachine, syncManager)
                .slot(new ModularSlot(new GhostCircuitItemStackHandler(baseMachine), 0));
        }
        return IDrawable.EMPTY.asWidget()
            .size(18);
    }

    /**
     * Returns a title widget positioned on the top left above the panel. Client only!
     *
     * @param mte        - The machine to make the title for
     * @param panelWidth - The width of the machine's main panel
     * @return machine title widget on the client, null otherwise
     */
    public static Widget<?> createMachineTitle(IMetaTileEntity mte, int panelWidth) {
        if (NetworkUtils.isClient()) {
            String title = mte.getLocalName();

            int borderRadius = 5;
            int maxWidth = panelWidth - borderRadius * 2;

            int titleWidth = TextRenderer.getFontRenderer()
                .getStringWidth(title);
            int widgetWidth = Math.min(maxWidth, titleWidth);

            int rows = (int) Math.ceil((double) titleWidth / maxWidth);
            int heightPerRow = TextRenderer.getFontRenderer().FONT_HEIGHT;
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
        return null;
    }
}
