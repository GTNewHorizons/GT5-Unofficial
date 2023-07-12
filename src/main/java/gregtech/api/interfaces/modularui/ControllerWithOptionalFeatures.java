package gregtech.api.interfaces.modularui;

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
 * </ul>
 */
public interface ControllerWithOptionalFeatures extends IVoidable, IRecipeLockable {

    boolean isAllowedToWork();

    void disableWorking();

    void enableWorking();

    // Pos2d getPowerSwitchButtonPos();

    // default ButtonWidget createPowerSwitchButton(IWidgetBuilder<?> builder) {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // if (isAllowedToWork()) {
    // disableWorking();
    // } else {
    // enableWorking();
    // }
    // })
    // .setPlayClickSoundResource(
    // () -> isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
    // : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
    // .setBackground(() -> {
    // if (isAllowedToWork()) {
    // return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
    // GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
    // } else {
    // return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
    // GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
    // }
    // })
    // .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, val -> {
    // if (val) enableWorking();
    // else disableWorking();
    // }), builder)
    // .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_switch"))
    // .setTooltipShowUpDelay(TOOLTIP_DELAY)
    // .setPos(getPowerSwitchButtonPos())
    // .setSize(16, 16);
    // return (ButtonWidget) button;
    // }

    // Pos2d getVoidingModeButtonPos();

    // default ButtonWidget createVoidExcessButton(IWidgetBuilder<?> builder) {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // if (supportsVoidProtection()) {
    // switch (clickData.mouseButton) {
    // case 0 -> setVoidingMode(getVoidingMode().next());
    // case 1 -> setVoidingMode(getVoidingMode().previous());
    // }
    // widget.notifyTooltipChange();
    // }
    // })
    // .setPlayClickSound(supportsVoidProtection())
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // ret.add(getVoidingMode().buttonTexture);
    // ret.add(getVoidingMode().buttonOverlay);
    // if (!supportsVoidProtection()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
    // }
    // return ret.toArray(new IDrawable[0]);
    // })
    // .attachSyncer(
    // new FakeSyncWidget.IntegerSyncer(
    // () -> getVoidingMode().ordinal(),
    // val -> setVoidingMode(VoidingMode.fromOrdinal(val))),
    // builder)
    // .dynamicTooltip(
    // () -> Arrays.asList(
    // StatCollector.translateToLocal("GT5U.gui.button.voiding_mode"),
    // StatCollector.translateToLocal(getVoidingMode().getTransKey())))
    // .setTooltipShowUpDelay(TOOLTIP_DELAY)
    // .setPos(getVoidingModeButtonPos())
    // .setSize(16, 16);
    // if (!supportsVoidProtection()) {
    // button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
    // }
    // return (ButtonWidget) button;
    // }

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

    // Pos2d getInputSeparationButtonPos();

    // default ButtonWidget createInputSeparationButton(IWidgetBuilder<?> builder) {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // if (supportsInputSeparation()) {
    // setInputSeparation(!isInputSeparationEnabled());
    // }
    // })
    // .setPlayClickSound(supportsInputSeparation())
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // if (isInputSeparationEnabled()) {
    // ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
    // if (supportsInputSeparation()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED);
    // }
    // } else {
    // ret.add(GT_UITextures.BUTTON_STANDARD);
    // if (supportsInputSeparation()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED);
    // }
    // }
    // if (!supportsInputSeparation()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
    // }
    // return ret.toArray(new IDrawable[0]);
    // })
    // .attachSyncer(
    // new FakeSyncWidget.BooleanSyncer(this::isInputSeparationEnabled, this::setInputSeparation),
    // builder)
    // .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.input_separation"))
    // .setTooltipShowUpDelay(TOOLTIP_DELAY)
    // .setPos(getInputSeparationButtonPos())
    // .setSize(16, 16);
    // if (!supportsInputSeparation()) {
    // button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
    // }
    // return (ButtonWidget) button;
    // }

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

    // Pos2d getBatchModeButtonPos();

    // default ButtonWidget createBatchModeButton(IWidgetBuilder<?> builder) {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // if (supportsBatchMode()) {
    // setBatchMode(!isBatchModeEnabled());
    // }
    // })
    // .setPlayClickSound(supportsBatchMode())
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // if (isBatchModeEnabled()) {
    // ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
    // if (supportsBatchMode()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
    // }
    // } else {
    // ret.add(GT_UITextures.BUTTON_STANDARD);
    // if (supportsBatchMode()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
    // }
    // }
    // if (!supportsBatchMode()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
    // }
    // return ret.toArray(new IDrawable[0]);
    // })
    // .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isBatchModeEnabled, this::setBatchMode), builder)
    // .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))
    // .setTooltipShowUpDelay(TOOLTIP_DELAY)
    // .setPos(getBatchModeButtonPos())
    // .setSize(16, 16);
    // if (!supportsBatchMode()) {
    // button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
    // }
    // return (ButtonWidget) button;
    // }

    // Pos2d getRecipeLockingButtonPos();

    // default ButtonWidget createLockToSingleRecipeButton(IWidgetBuilder<?> builder) {
    // Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
    // if (supportsSingleRecipeLocking()) {
    // setRecipeLocking(!isRecipeLockingEnabled());
    // }
    // })
    // .setPlayClickSound(supportsSingleRecipeLocking())
    // .setBackground(() -> {
    // List<UITexture> ret = new ArrayList<>();
    // if (isRecipeLockingEnabled()) {
    // ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
    // if (supportsSingleRecipeLocking()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
    // }
    // } else {
    // ret.add(GT_UITextures.BUTTON_STANDARD);
    // if (supportsSingleRecipeLocking()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
    // } else {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED);
    // }
    // }
    // if (!supportsSingleRecipeLocking()) {
    // ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
    // }
    // return ret.toArray(new IDrawable[0]);
    // })
    // .attachSyncer(
    // new FakeSyncWidget.BooleanSyncer(this::isRecipeLockingEnabled, this::setRecipeLocking),
    // builder)
    // .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))
    // .setTooltipShowUpDelay(TOOLTIP_DELAY)
    // .setPos(getRecipeLockingButtonPos())
    // .setSize(16, 16);
    // if (!supportsSingleRecipeLocking()) {
    // button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
    // }
    // return (ButtonWidget) button;
    // }
}
