package gregtech.api.interfaces.modularui;

import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;

/**
 * Machines implementing this interface can have logic and GUI buttons
 * to configure various behaviors regarding multiblock.
 * <ul>
 * <li>Power switch</li>
 * <li>Void protection</li>
 * <li>Separated input buses</li>
 * <li>Batch mode</li>
 * <li>Recipe locking</li>
 * <li>Multiple machine modes</li>
 * </ul>
 */
public interface ControllerWithOptionalFeatures extends IVoidable, IRecipeLockable {

    boolean isAllowedToWork();

    void disableWorking();

    void enableWorking();

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
                    return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
                } else {
                    return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
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
                ret.add(getVoidingMode().buttonTexture);
                ret.add(getVoidingMode().buttonOverlay);
                if (!supportsVoidProtection()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
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
            if (supportsMachineModeSwitch()) {
                onMachineModeSwitchClick();
                setMachineMode(nextMachineMode());
            }
        })
            .setPlayClickSound(supportsMachineModeSwitch())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (supportsMachineModeSwitch()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    ret.add(getMachineModeIcon(getMachineMode()));
                } else return null;
                return ret.toArray(new IDrawable[0]);
            })
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
            }
        })
            .setPlayClickSound(supportsInputSeparation())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isInputSeparationEnabled()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsInputSeparation()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (supportsInputSeparation()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED);
                    }
                }
                if (!supportsInputSeparation()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(this::isInputSeparationEnabled, this::setInputSeparation),
                builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.input_separation"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getInputSeparationButtonPos())
            .setSize(16, 16);
        if (!supportsInputSeparation()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
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
        return false;
    }

    Pos2d getBatchModeButtonPos();

    default ButtonWidget createBatchModeButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsBatchMode()) {
                setBatchMode(!isBatchModeEnabled());
            }
        })
            .setPlayClickSound(supportsBatchMode())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isBatchModeEnabled()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsBatchMode()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (supportsBatchMode()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                    }
                }
                if (!supportsBatchMode()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isBatchModeEnabled, this::setBatchMode), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getBatchModeButtonPos())
            .setSize(16, 16);
        if (!supportsBatchMode()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return (ButtonWidget) button;
    }

    Pos2d getRecipeLockingButtonPos();

    default ButtonWidget createLockToSingleRecipeButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (supportsSingleRecipeLocking()) {
                setRecipeLocking(!isRecipeLockingEnabled());
            }
        })
            .setPlayClickSound(supportsSingleRecipeLocking())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isRecipeLockingEnabled()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (supportsSingleRecipeLocking()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (supportsSingleRecipeLocking()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED);
                    }
                }
                if (!supportsSingleRecipeLocking()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(this::isRecipeLockingEnabled, this::setRecipeLocking),
                builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getRecipeLockingButtonPos())
            .setSize(16, 16);
        if (!supportsSingleRecipeLocking()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return (ButtonWidget) button;
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
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                }
                ret.add(GT_UITextures.OVERLAY_BUTTON_STRUCTURE_UPDATE);
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
