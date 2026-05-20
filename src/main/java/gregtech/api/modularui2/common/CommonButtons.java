package gregtech.api.modularui2.common;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.function.Consumer;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;

/**
 * A class of commonly used widgets between all forms of MUI2.
 */
public final class CommonButtons {

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
            .size(12)
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
            .size(12)
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
            .size(12)
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
            .size(12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     *
     * @return a button that when clicked, closes the panel its on
     */
    public static ButtonWidget<?> panelCloseButton() {
        return ButtonWidget.panelCloseButton();
    }

    // TODO javadoc
    public static ToggleButton createToggleButton(BooleanSyncValue syncValue, UITexture overlay, String key) {
        return new ToggleButton().value(syncValue)
            .overlay(overlay)
            .addTooltipLine(GTUtility.translate(key))
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    public static ToggleButton createToggleButton(BooleanSyncValue syncValue, UITexture selectedOverlay,
        UITexture unselectedOverlay, String key) {
        return new ToggleButton().value(syncValue)
            .overlay(true, selectedOverlay)
            .overlay(false, unselectedOverlay)
            .addTooltipLine(GTUtility.translate(key))
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    public static ToggleButton createToggleButtonDynamicTooltip(BooleanSyncValue syncValue, UITexture overlay,
        Consumer<RichTooltip> tooltipBuilder) {
        return new ToggleButton().value(syncValue)
            .overlay(overlay)
            .tooltipDynamic(tooltipBuilder)
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    public static ToggleButton createToggleButtonDynamicTooltip(BooleanSyncValue syncValue, UITexture selectedOverlay,
        UITexture unselectedOverlay, Consumer<RichTooltip> tooltipBuilder) {
        return new ToggleButton().value(syncValue)
            .overlay(true, selectedOverlay)
            .overlay(false, unselectedOverlay)
            .tooltipDynamic(tooltipBuilder)
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
