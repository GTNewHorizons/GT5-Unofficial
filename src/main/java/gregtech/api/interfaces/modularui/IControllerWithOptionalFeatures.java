package gregtech.api.interfaces.modularui;

import static gregtech.api.gui.modularui.GTUITextures.OVERLAY_BUTTON_POWER_PANEL;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.screen.RichTooltip;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.common.config.Gregtech;

/**
 * Machines implementing this interface can have logic and GUI buttons to configure various behaviors regarding
 * multiblock.
 * <ul>
 * <li>Power switch</li>
 * <li>Void protection</li>
 * <li>Separated input buses</li>
 * <li>Batch mode</li>
 * <li>Recipe locking</li>
 * <li>Multiple machine modes</li>
 * </ul>
 */
public interface IControllerWithOptionalFeatures extends IVoidable, IRecipeLockable {

    int POWER_PANEL_WINDOW_ID = 8;

    boolean isAllowedToWork();

    void disableWorking();

    void enableWorking();

    void setMuffled(boolean value);

    boolean isMuffled();

    Pos2d getPowerSwitchButtonPos();

    default ButtonWidget createPowerSwitchButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isAllowedToWork()) {
                disableWorking();
            } else {
                enableWorking();
            }
        })
            .setPlayClickSoundResource(
                () -> isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
                    : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
            .setBackground(() -> {
                if (isAllowedToWork()) {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                        GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
                } else {
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                        GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
                }
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, val -> {
                if (val) enableWorking();
                else disableWorking();
            }), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_switch"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getPowerSwitchButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    default ButtonWidget createMuffleButton(IWidgetBuilder<?> builder, boolean canBeMuffled) {
        return (ButtonWidget) new ButtonWidget().setOnClick((clickData, widget) -> { setMuffled(!isMuffled()); })
            .setPlayClickSound(true)
            .setEnabled(canBeMuffled)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isMuffled()) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    ret.add(GTUITextures.OVERLAY_BUTTON_MUFFLE_ON);
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_MUFFLE_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isMuffled, this::setMuffled), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.machines.muffled"))
            .setPos(200, 0)
            .setSize(12, 12);
    }

    Pos2d getVoidingModeButtonPos();

    default ButtonWidget createVoidExcessButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsVoidProtection()) {
                Set<VoidingMode> allowed = getAllowedVoidingModes();
                switch (clickData.mouseButton) {
                    case 0 -> setVoidingMode(getVoidingMode().nextInCollection(allowed));
                    case 1 -> setVoidingMode(getVoidingMode().previousInCollection(allowed));
                }
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(supportsVoidProtection())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(getVoidingMode().buttonTextureLegacy);
                ret.add(getVoidingMode().buttonOverlayLegacy);
                if (!supportsVoidProtection()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(
                    () -> getVoidingMode().ordinal(),
                    val -> setVoidingMode(VoidingMode.fromOrdinal(val))),
                builder)
            .dynamicTooltip(
                () -> Arrays.asList(
                    StatCollector.translateToLocal("GT5U.gui.button.voiding_mode"),
                    StatCollector.translateToLocal(getVoidingMode().getTransKey())))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getVoidingModeButtonPos())
            .setSize(16, 16);
        if (!supportsVoidProtection()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return (ButtonWidget) button;
    }

    /**
     * @return if the multi has more than 1 mode
     */
    default boolean supportsMachineModeSwitch() {
        return false;
    }

    /**
     * @return the current mode number. This is a getter is used for displaying the icon in the GUI
     */
    default int getMachineMode() {
        return 0;
    }

    /**
     * @return name for the current machine mode on this machine. Defaults "Unknown Mode"
     */
    default String getMachineModeName() {
        return "Unknown Mode";
    }

    /**
     * @param index Index of machineModeIcons to pull from
     * @return UITexture associated with that machineMode
     */
    default UITexture getMachineModeIcon(int index) {
        return null;
    }

    /**
     * @param index Number to set machineMode to
     */
    default void setMachineMode(int index) {}

    /**
     * @return Returns the next machineMode number in the sequence
     */
    default int nextMachineMode() {
        return 0;
    }

    /**
     * @return Returns whether machine supports mode switch by default
     */
    default boolean getDefaultModeSwitch() {
        return supportsMachineModeSwitch();
    }

    Pos2d getMachineModeSwitchButtonPos();

    /**
     * Called when the mode switch button is clicked
     */
    default void onMachineModeSwitchClick() {}

    default ButtonWidget createModeSwitchButton(IWidgetBuilder<?> builder) {
        if (!supportsMachineModeSwitch()) return null;
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            onMachineModeSwitchClick();
            setMachineMode(nextMachineMode());
        })
            .setPlayClickSound(true)
            .setBackground(() -> new IDrawable[] { GTUITextures.BUTTON_STANDARD, getMachineModeIcon(getMachineMode()) })
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(this::getMachineMode, this::setMachineMode), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.mode_switch"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getMachineModeSwitchButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    /**
     * @return if the multi supports input separation.
     */
    boolean supportsInputSeparation();

    /**
     * @return true if input separation is enabled, else false. This is getter is used for displaying the icon in the
     *         GUI
     */
    boolean isInputSeparationEnabled();

    void setInputSeparation(boolean enabled);

    default boolean getDefaultInputSeparationMode() {
        return supportsInputSeparation();
    }

    Pos2d getInputSeparationButtonPos();

    default ButtonWidget createInputSeparationButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsInputSeparation()) {
                setInputSeparation(!isInputSeparationEnabled());
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(supportsInputSeparation())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isInputSeparationEnabled()) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsInputSeparation()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED);
                    }
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    if (supportsInputSeparation()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED);
                    }
                }
                if (!supportsInputSeparation()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(this::isInputSeparationEnabled, this::setInputSeparation),
                builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getInputSeparationButtonPos())
            .setSize(16, 16);

        addDynamicTooltipOfFeatureToButton(
            button,
            this::supportsInputSeparation,
            this::isInputSeparationEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_on"),
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_off"));

        return (ButtonWidget) button;
    }

    /**
     * @return if the multi supports precise power management.
     */
    boolean supportsPowerPanel();

    Pos2d getPowerPanelButtonPos();

    ModularWindow createPowerPanel(final EntityPlayer player);

    default ButtonWidget createPowerPanelButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsPowerPanel()) {
                if (!widget.isClient()) widget.getContext()
                    .openSyncedWindow(POWER_PANEL_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                ret.add(OVERLAY_BUTTON_POWER_PANEL);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_panel"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getPowerPanelButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    /**
     * @return if the multi supports batch mode.
     */
    boolean supportsBatchMode();

    /**
     * @return true if batch mode is enabled, else false. This is getter is used for displaying the icon in the GUI
     */
    boolean isBatchModeEnabled();

    void setBatchMode(boolean enabled);

    default boolean getDefaultBatchMode() {
        return Gregtech.general.batchModeInitialValue;
    }

    Pos2d getBatchModeButtonPos();

    default ButtonWidget createBatchModeButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsBatchMode()) {
                setBatchMode(!isBatchModeEnabled());
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(supportsBatchMode())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isBatchModeEnabled()) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsBatchMode()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    }
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    if (supportsBatchMode()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                    }
                }
                if (!supportsBatchMode()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isBatchModeEnabled, this::setBatchMode), builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getBatchModeButtonPos())
            .setSize(16, 16);

        addDynamicTooltipOfFeatureToButton(
            button,
            this::supportsBatchMode,
            this::isBatchModeEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_on"),
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_off"));

        return (ButtonWidget) button;
    }

    Pos2d getRecipeLockingButtonPos();

    default ButtonWidget createLockToSingleRecipeButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsSingleRecipeLocking()) {
                setRecipeLocking(!isRecipeLockingEnabled());
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(supportsSingleRecipeLocking())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isRecipeLockingEnabled()) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsSingleRecipeLocking()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    }
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    if (supportsSingleRecipeLocking()) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED);
                    }
                }
                if (!supportsSingleRecipeLocking()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(this::isRecipeLockingEnabled, this::setRecipeLocking),
                builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getRecipeLockingButtonPos())
            .setSize(16, 16);

        addDynamicTooltipOfFeatureToButton(
            button,
            this::supportsSingleRecipeLocking,
            this::isRecipeLockingEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_on"),
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_off"));

        return (ButtonWidget) button;
    }

    /**
     * Adds a dynamic tooltip to a widget button that displays the status of a multi-block feature.
     * <p>
     * The tooltip behavior depends on feature support:
     * <ul>
     * <li>If the feature is supported: Shows a dynamic tooltip that updates based on the feature's enabled state</li>
     * <li>If the feature is not supported: Shows a static tooltip with the current state plus a "forbidden"
     * message</li>
     * </ul>
     *
     * <p>
     * <strong>Important:</strong> When implementing this method, ensure that any action that changes the feature's
     * enabled state calls {@code widget.notifyTooltipChange()} to refresh the tooltip display.
     *
     * @param widget                 the widget button to add the tooltip to
     * @param supportsFeature        supplier that returns {@code true} if the multi-block feature is supported
     * @param isFeatureEnabled       supplier that returns {@code true} if the feature is currently enabled
     * @param tooltipFeatureEnabled  tooltip text to display when the feature is enabled
     * @param tooltipFeatureDisabled tooltip text to display when the feature is disabled
     *
     *
     * @see gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui#addDynamicTooltipOfFeatureToButton(RichTooltip,
     *      Supplier, Supplier, String, String) For equivalent method but made for non-ModularUI
     */
    default void addDynamicTooltipOfFeatureToButton(Widget widget, Supplier<Boolean> supportsFeature,
        Supplier<Boolean> isFeatureEnabled, String tooltipFeatureEnabled, String tooltipFeatureDisabled) {

        if (supportsFeature.get()) {
            widget.dynamicTooltip(() -> {
                if (isFeatureEnabled.get()) {
                    return Collections.singletonList(tooltipFeatureEnabled);
                } else {
                    return Collections.singletonList(tooltipFeatureDisabled);
                }
            });
        } else {
            if (isFeatureEnabled.get()) {
                widget.addTooltip(tooltipFeatureEnabled);
            } else {
                widget.addTooltip(tooltipFeatureDisabled);
            }

            widget.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
    }

    Pos2d getStructureUpdateButtonPos();

    int getStructureUpdateTime();

    void setStructureUpdateTime(int time);

    default ButtonWidget createStructureUpdateButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget()
            .setOnClick((clickData, widget) -> { if (getStructureUpdateTime() <= -20) setStructureUpdateTime(1); })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (getStructureUpdateTime() > -20) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                }
                ret.add(GTUITextures.OVERLAY_BUTTON_STRUCTURE_UPDATE);
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(this::getStructureUpdateTime, this::setStructureUpdateTime),
                builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.structure_update"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getStructureUpdateButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }
}
