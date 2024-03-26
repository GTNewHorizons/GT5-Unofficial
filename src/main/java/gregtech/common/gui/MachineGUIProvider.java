package gregtech.common.gui;

import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TabButton;
import com.gtnewhorizons.modularui.common.widget.TabContainer;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.InventoryType;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.GUIHost;
import gregtech.api.gui.GUIProvider;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.tileentity.MachineProgress;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;

/**
 * Default GUI a machine will use to show its information
 */
public class MachineGUIProvider<T extends GUIHost & ProcessingLogicHost<? extends MuTEProcessingLogic<?>> & PowerLogicHost & MachineProgress>
    extends GUIProvider<T> {

    private static final int LOGO_SIZE = 17;
    @Nonnull
    protected static final Pos2d POWER_SWITCH_BUTTON_DEFAULT_POS = new Pos2d(144, 0);
    @Nonnull
    protected static final Pos2d VOIDING_MODE_BUTTON_DEFAULT_POS = new Pos2d(54, 0);
    @Nonnull
    protected static final Pos2d INPUT_SEPARATION_BUTTON_DEFAULT_POS = new Pos2d(36, 0);
    @Nonnull
    protected static final Pos2d BATCH_MODE_BUTTON_DEFAULT_POS = new Pos2d(18, 0);
    @Nonnull
    protected static final Pos2d RECIPE_LOCKING_BUTTON_DEFAULT_POS = new Pos2d(0, 0);

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MachineGUIProvider(@Nonnull T host) {
        super(host);
    }

    @Override
    protected void attachSynchHandlers(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {

    }

    @Override
    protected void addWidgets(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {
        int page = 0;
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        MultiChildWidget mainTab = new MultiChildWidget();
        mainTab.setSize(host.getWidth(), host.getHeight());
        createMainTab(mainTab, builder, uiContext);
        TabContainer tabs = new TabContainer().setButtonSize(20, 24);
        tabs.addTabButton(
            new TabButton(page++)
                .setBackground(
                    false,
                    ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0, 1f, 0.5f),
                    new ItemDrawable(host.getAsItem()).withFixedSize(16, 16)
                        .withOffset(2, 4))
                .setBackground(
                    true,
                    ModularUITextures.VANILLA_TAB_TOP_START.getSubArea(0, 0.5f, 1f, 1f),
                    new ItemDrawable(host.getAsItem()).withFixedSize(16, 16)
                        .withOffset(2, 4))
                .addTooltip(host.getMachineName())
                .setPos(20 * (page - 1), -20))
            .addPage(mainTab);
        if (host.hasItemInput()) {
            MultiChildWidget itemInputTab = new MultiChildWidget();
            itemInputTab.setSize(host.getWidth(), host.getHeight());
            createItemInputTab(itemInputTab, builder, uiContext);
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_ITEM_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_ITEM_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Item Input Inventory")
                    .setPos(20 * (page - 1), -20))
                .addPage(itemInputTab.addChild(getLogo().setPos(147, 86)));
        }

        if (host.hasItemOutput()) {
            MultiChildWidget itemOutputTab = new MultiChildWidget();
            itemOutputTab.setSize(host.getWidth(), host.getHeight());
            createItemOutputTab(itemOutputTab, builder, uiContext);
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_ITEM_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_ITEM_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Item Output Inventory")
                    .setPos(20 * (page - 1), -20))
                .addPage(itemOutputTab.addChild(getLogo().setPos(147, 86)));
        }

        if (host.hasFluidInput()) {
            MultiChildWidget fluidInputTab = new MultiChildWidget();
            fluidInputTab.setSize(host.getWidth(), host.getHeight());
            createFluidInputTab(fluidInputTab, builder, uiContext);
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_FLUID_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_FLUID_IN.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Fluid Input Tanks")
                    .setPos(20 * (page - 1), -20))
                .addPage(fluidInputTab.addChild(getLogo().setPos(147, 86)));
        }

        if (host.hasFluidOutput()) {
            MultiChildWidget fluidOutputTab = new MultiChildWidget();
            fluidOutputTab.setSize(host.getWidth(), host.getHeight());
            createFluidOutputTab(fluidOutputTab, builder, uiContext);
            tabs.addTabButton(
                new TabButton(page++)
                    .setBackground(
                        false,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                        GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .setBackground(
                        true,
                        ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                        GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                            .withOffset(2, 4))
                    .addTooltip("Fluid Output Tanks")
                    .setPos(20 * (page - 1), -20))
                .addPage(fluidOutputTab.addChild(getLogo().setPos(147, 86)));
        }
        MultiChildWidget powerInfoTab = new MultiChildWidget();
        powerInfoTab.setSize(host.getWidth(), host.getHeight());
        createPowerTab(powerInfoTab, builder, uiContext);
        tabs.addTabButton(
            new TabButton(page++)
                .setBackground(
                    false,
                    ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0, 1f, 0.5f),
                    GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                        .withOffset(2, 4))
                .setBackground(
                    true,
                    ModularUITextures.VANILLA_TAB_TOP_MIDDLE.getSubArea(0, 0.5f, 1f, 1f),
                    GT_UITextures.PICTURE_FLUID_OUT.withFixedSize(16, 16)
                        .withOffset(2, 4))
                .addTooltip("Power Information")
                .setPos(20 * (page - 1), -20))
            .addPage(powerInfoTab.addChild(getLogo().setPos(147, 86)));
        builder.widget(tabs);
    }

    protected void createMainTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        MultiChildWidget buttons = new MultiChildWidget();
        buttons.setSize(16, 167)
            .setPos(7, 86);
        buttons.addChild(createPowerSwitchButton(builder))
            .addChild(createVoidExcessButton(builder))
            .addChild(createInputSeparationButton(builder))
            .addChild(createBatchModeButton(builder))
            .addChild(createLockToSingleRecipeButton(builder));
        tab.addChild(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(160, 75))
            .addChild(buttons);
    }

    protected void createItemInputTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        tab.addChild(
            host.getItemLogic(InventoryType.Input, null)
                .getGuiPart()
                .setSize(18 * 4 + 9, 5 * 18)
                .setPos(host.getWidth() / 2 - 2 * 18, 10));
    }

    protected void createItemOutputTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        tab.addChild(
            host.getItemLogic(InventoryType.Output, null)
                .getGuiPart()
                .setSize(18 * 4 + 9, 5 * 18)
                .setPos(host.getWidth() / 2 - 2 * 18, 10));
    }

    protected void createFluidInputTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        tab.addChild(
            host.getFluidLogic(InventoryType.Input, null)
                .getGuiPart()
                .setSize(18 * 4 + 9, 5 * 18)
                .setPos(host.getWidth() / 2 - 2 * 18, 10));
    }

    protected void createFluidOutputTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        tab.addChild(
            host.getFluidLogic(InventoryType.Output, null)
                .getGuiPart()
                .setSize(18 * 4 + 9, 5 * 18)
                .setPos(host.getWidth() / 2 - 2 * 18, 10));
    }

    protected void createPowerTab(@Nonnull MultiChildWidget tab, @Nonnull Builder builder,
        @Nonnull UIBuildContext uiBuildContext) {
        PowerLogic power = host.getPowerLogic();
        tab.addChild(
            new TextWidget()
                .setStringSupplier(
                    () -> numberFormat.format(power.getStoredEnergy()) + "/"
                        + numberFormat.format(power.getCapacity())
                        + " EU")
                .setPos(10, 30))
            .addChild(
                new TextWidget()
                    .setStringSupplier(
                        () -> numberFormat.format(power.getVoltage()) + " EU/t"
                            + "("
                            + numberFormat.format(power.getMaxAmperage())
                            + " A)")
                    .setPos(10, 60));
    }

    /**
     * Should return the logo you want to use that is pasted on each tab. Default is the GT logo.
     */
    @Nonnull
    protected Widget getLogo() {
        DrawableWidget logo = new DrawableWidget();
        logo.setDrawable(GUITextureSet.DEFAULT.getGregTechLogo())
            .setSize(LOGO_SIZE, LOGO_SIZE);
        return logo;
    }

    protected Pos2d getPowerSwitchButtonPos() {
        return POWER_SWITCH_BUTTON_DEFAULT_POS;
    }

    protected ButtonWidget createPowerSwitchButton(IWidgetBuilder<?> builder) {
        ButtonWidget button = new ButtonWidget();
        button.setOnClick((clickData, widget) -> {
            if (host.isAllowedToWork()) {
                host.disableWorking();
            } else {
                host.enableWorking();
            }
        })
            .setPlayClickSoundResource(
                () -> host.isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
                    : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
            .setBackground(() -> {
                if (host.isAllowedToWork()) {
                    return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
                } else {
                    return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
                }
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(host::isAllowedToWork, host::setAllowedToWork), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_switch"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getPowerSwitchButtonPos())
            .setSize(16, 16);
        return button;
    }

    @Nonnull
    protected Pos2d getVoidingModeButtonPos() {
        return VOIDING_MODE_BUTTON_DEFAULT_POS;
    }

    @Nonnull
    protected ButtonWidget createVoidExcessButton(IWidgetBuilder<?> builder) {
        ButtonWidget button = new ButtonWidget();
        button.setOnClick((clickData, widget) -> {
            if (host.supportsVoidProtection()) {
                Set<VoidingMode> allowed = host.getAllowedVoidingModes();
                switch (clickData.mouseButton) {
                    case 0 -> host.setVoidingMode(
                        host.getVoidingMode()
                            .nextInCollection(allowed));
                    case 1 -> host.setVoidingMode(
                        host.getVoidingMode()
                            .previousInCollection(allowed));
                }
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(host.supportsVoidProtection())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(host.getVoidingMode().buttonTexture);
                ret.add(host.getVoidingMode().buttonOverlay);
                if (!host.supportsVoidProtection()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(
                    () -> host.getVoidingMode()
                        .ordinal(),
                    val -> host.setVoidingMode(VoidingMode.fromOrdinal(val))),
                builder)
            .dynamicTooltip(
                () -> Arrays.asList(
                    StatCollector.translateToLocal("GT5U.gui.button.voiding_mode"),
                    StatCollector.translateToLocal(
                        host.getVoidingMode()
                            .getTransKey())))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getVoidingModeButtonPos())
            .setSize(16, 16);
        if (!host.supportsVoidProtection()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return button;
    }

    @Nonnull
    protected Pos2d getInputSeparationButtonPos() {
        return INPUT_SEPARATION_BUTTON_DEFAULT_POS;
    }

    protected ButtonWidget createInputSeparationButton(IWidgetBuilder<?> builder) {
        ButtonWidget button = new ButtonWidget();
        button.setOnClick((clickData, widget) -> {
            if (host.supportsInputSeparation()) {
                host.setInputSeparation(!host.isInputSeparated());
            }
        })
            .setPlayClickSound(host.supportsInputSeparation())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (host.isInputSeparated()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (host.supportsInputSeparation()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (host.supportsInputSeparation()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED);
                    }
                }
                if (!host.supportsInputSeparation()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(host::isInputSeparated, host::setInputSeparation), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.input_separation"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getInputSeparationButtonPos())
            .setSize(16, 16);
        if (!host.supportsInputSeparation()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return button;
    }

    @Nonnull
    protected Pos2d getBatchModeButtonPos() {
        return BATCH_MODE_BUTTON_DEFAULT_POS;
    }

    protected ButtonWidget createBatchModeButton(IWidgetBuilder<?> builder) {
        ButtonWidget button = new ButtonWidget();
        button.setOnClick((clickData, widget) -> {
            if (host.supportsBatchMode()) {
                host.setBatchMode(!host.isBatchModeEnabled());
            }
        })
            .setPlayClickSound(host.supportsBatchMode())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (host.isBatchModeEnabled()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (host.supportsBatchMode()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (host.supportsBatchMode()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                    }
                }
                if (!host.supportsBatchMode()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(host::isBatchModeEnabled, host::setBatchMode), builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getBatchModeButtonPos())
            .setSize(16, 16);
        if (!host.supportsBatchMode()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return button;
    }

    @Nonnull
    protected Pos2d getRecipeLockingButtonPos() {
        return RECIPE_LOCKING_BUTTON_DEFAULT_POS;
    }

    protected ButtonWidget createLockToSingleRecipeButton(IWidgetBuilder<?> builder) {
        ButtonWidget button = new ButtonWidget();
        button.setOnClick((clickData, widget) -> {
            if (host.supportsSingleRecipeLocking()) {
                host.setRecipeLocking(!host.isRecipeLockingEnabled());
            }
        })
            .setPlayClickSound(host.supportsSingleRecipeLocking())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (host.isRecipeLockingEnabled()) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    if (host.supportsSingleRecipeLocking()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    }
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (host.supportsSingleRecipeLocking()) {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
                    } else {
                        ret.add(GT_UITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED);
                    }
                }
                if (!host.supportsSingleRecipeLocking()) {
                    ret.add(GT_UITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(host::isRecipeLockingEnabled, host::setRecipeLocking),
                builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getRecipeLockingButtonPos())
            .setSize(16, 16);
        if (!host.supportsSingleRecipeLocking()) {
            button.addTooltip(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
        }
        return button;
    }
}
