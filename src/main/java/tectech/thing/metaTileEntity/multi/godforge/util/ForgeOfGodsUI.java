package tectech.thing.metaTileEntity.multi.godforge.util;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SliderWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.TecTech;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;

/**
 * Holds UI element builders and other conveniences shared between the primary Forge of the Gods and its modules.
 */
public class ForgeOfGodsUI {

    // ARGB representations of the 4 colors used in the color selector (red, green, blue, gold)
    public static final int RED_ARGB = 0xFFFF5555;
    public static final int GREEN_ARGB = 0xFF55FF55;
    public static final int BLUE_ARGB = 0xFF0000AA;
    public static final int GOLD_ARGB = 0xFFFFAA00;

    public static ButtonWidget createPowerSwitchButton(final IGregTechTileEntity tileEntity) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            if (tileEntity.isAllowedToWork()) {
                tileEntity.disableWorking();
            } else {
                tileEntity.enableWorking();
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (tileEntity.isAllowedToWork()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, 148)
            .setSize(16, 16);
        button.addTooltip(StatCollector.translateToLocal("tt.gui.tooltip.power_switch"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    public static ButtonWidget createInputSeparationButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setInputSeparation(!mte.isInputSeparationEnabled());
            widget.notifyTooltipChange();
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (mte.isInputSeparationEnabled()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_INPUT_SEPARATION);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(mte::isInputSeparationEnabled, mte::setInputSeparation),
                builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getInputSeparationButtonPos())
            .setSize(16, 16);

        mte.addDynamicTooltipOfFeatureToButton(
            button,
            mte::supportsInputSeparation,
            mte::isInputSeparationEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_on"),
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_off"));
        return (ButtonWidget) button;
    }

    public static ButtonWidget createBatchModeButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setBatchMode(!mte.isBatchModeEnabled());
            widget.notifyTooltipChange();
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (mte.isBatchModeEnabled()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_BATCH_MODE);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(mte::isBatchModeEnabled, mte::setBatchMode), builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getBatchModeButtonPos())
            .setSize(16, 16);

        mte.addDynamicTooltipOfFeatureToButton(
            button,
            mte::supportsBatchMode,
            mte::isBatchModeEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_on"),
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_off"));

        return (ButtonWidget) button;
    }

    public static ButtonWidget createLockToSingleRecipeButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setRecipeLocking(!mte.isRecipeLockingEnabled());
            widget.notifyTooltipChange();
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (mte.isRecipeLockingEnabled()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_RECIPE_LOCKED);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(mte::isRecipeLockingEnabled, mte::setRecipeLocking), builder)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getRecipeLockingButtonPos())
            .setSize(16, 16);

        mte.addDynamicTooltipOfFeatureToButton(
            button,
            mte::supportsSingleRecipeLocking,
            mte::isRecipeLockingEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_on"),
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_off"));

        return (ButtonWidget) button;
    }

    public static ButtonWidget createStructureUpdateButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            if (mte.getStructureUpdateTime() <= -20) {
                mte.setStructureUpdateTime(1);
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (mte.getStructureUpdateTime() > -20) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_STRUCTURE_CHECK);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_STRUCTURE_CHECK_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(mte::getStructureUpdateTime, mte::setStructureUpdateTime),
                builder)
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.structure_update"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getStructureUpdateButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public static ButtonWidget createVoidExcessButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            Set<VoidingMode> allowed = mte.getAllowedVoidingModes();
            switch (clickData.mouseButton) {
                case 0 -> mte.setVoidingMode(
                    mte.getVoidingMode()
                        .nextInCollection(allowed));
                case 1 -> mte.setVoidingMode(
                    mte.getVoidingMode()
                        .previousInCollection(allowed));
            }
            widget.notifyTooltipChange();
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                switch (mte.getVoidingMode()) {
                    case VOID_NONE -> ret.add(TecTechUITextures.OVERLAY_BUTTON_VOIDING_OFF);
                    case VOID_ITEM -> ret.add(TecTechUITextures.OVERLAY_BUTTON_VOIDING_ITEMS);
                    case VOID_FLUID -> ret.add(TecTechUITextures.OVERLAY_BUTTON_VOIDING_FLUIDS);
                    case VOID_ALL -> ret.add(TecTechUITextures.OVERLAY_BUTTON_VOIDING_BOTH);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(
                    () -> mte.getVoidingMode()
                        .ordinal(),
                    val -> mte.setVoidingMode(VoidingMode.fromOrdinal(val))),
                builder)
            .dynamicTooltip(
                () -> Arrays.asList(
                    StatCollector.translateToLocal("GT5U.gui.button.voiding_mode"),
                    StatCollector.translateToLocal(
                        mte.getVoidingMode()
                            .getTransKey())))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getVoidingModeButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public static ModularWindow createGeneralInfoWindow(Supplier<Boolean> inversionGetter,
        Consumer<Boolean> inversionSetter) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        final int WIDTH = 300;
        final int HEIGHT = 300;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);

        builder.setDraggable(true);
        scrollable.widget(
            new TextWidget(EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.introduction"))
                .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                .setTextAlignment(Alignment.TopCenter)
                .setPos(7, 13)
                .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.introductioninfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 30)
                    .setSize(280, 50))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.tableofcontents"))
                        .setDefaultColor(EnumChatFormatting.AQUA)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 80)
                        .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(150))
                    .setBackground(
                        new Text(EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.fuel"))
                            .alignment(Alignment.CenterLeft)
                            .color(0x55ffff))
                    .setPos(7, 95)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(434))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.modules"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 110)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(1088))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.upgrades"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 125)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(1412))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.milestones"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 140)
                    .setSize(150, 15))
            .widget(
                TextWidget.dynamicText(() -> inversionHeaderText(inversionGetter.get()))
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 155)
                    .setSize(150, 15))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (inversionGetter.get()) {
                    scrollable.setVerticalScrollOffset(1766);
                }
            })
                .setPlayClickSound(inversionGetter.get())
                .setPos(7, 155)
                .setSize(150, 15)
                .attachSyncer(new FakeSyncWidget.BooleanSyncer(inversionGetter, inversionSetter), scrollable))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.fuel"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(127, 160)
                        .setSize(40, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 177)
                    .setSize(280, 250))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.modules"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 440)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.moduleinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 461)
                    .setSize(280, 620))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.upgrades"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 1098)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.upgradeinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1115)
                    .setSize(280, 290))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.milestones"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 1422)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.milestoneinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1439)
                    .setSize(280, 320))
            .widget(
                TextWidget.dynamicText(() -> inversionHeaderText(inversionGetter.get()))
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.TopCenter)
                    .setPos(7, 1776)
                    .setSize(280, 15))
            .widget(
                TextWidget.dynamicText(() -> inversionInfoText(inversionGetter.get()))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1793)
                    .setSize(280, 160))
            .widget(
                new TextWidget("").setPos(7, 1965)
                    .setSize(10, 10));

        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_GLOW_WHITE)
                .setPos(0, 0)
                .setSize(300, 300))
            .widget(
                scrollable.setSize(292, 292)
                    .setPos(4, 4))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(284, 4));

        return builder.build();
    }

    private static Text inversionHeaderText(boolean inversion) {
        return inversion
            ? new Text(
                EnumChatFormatting.BOLD + "§k2"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.WHITE
                    + EnumChatFormatting.BOLD
                    + translateToLocal("gt.blockmachines.multimachine.FOG.inversion")
                    + EnumChatFormatting.BOLD
                    + "§k2")
            : new Text("");
    }

    private static Text inversionInfoText(boolean inversion) {
        return inversion ? new Text(translateToLocal("gt.blockmachines.multimachine.FOG.inversioninfotext"))
            : new Text("");
    }

    public static void reopenWindow(Widget widget, int windowId) {
        if (!widget.isClient()) {
            ModularUIContext ctx = widget.getContext();
            if (ctx.isWindowOpen(windowId)) {
                ctx.closeWindow(windowId);
            }
            ctx.openSyncedWindow(windowId);
        }
    }

    public static void closeWindow(Widget widget, int windowId) {
        if (!widget.isClient()) {
            ModularUIContext ctx = widget.getContext();
            if (ctx.isWindowOpen(windowId)) {
                ctx.closeWindow(windowId);
            }
        }
    }

    public enum StarColorRGBM {

        RED(EnumChatFormatting.RED, RED_ARGB, 0, 255, ForgeOfGodsStarColor.DEFAULT_RED),
        GREEN(EnumChatFormatting.GREEN, GREEN_ARGB, 0, 255, ForgeOfGodsStarColor.DEFAULT_GREEN),
        BLUE(EnumChatFormatting.DARK_BLUE, BLUE_ARGB, 0, 255, ForgeOfGodsStarColor.DEFAULT_BLUE),
        GAMMA(EnumChatFormatting.GOLD, GOLD_ARGB, 0, 100, ForgeOfGodsStarColor.DEFAULT_GAMMA);

        private final String title;
        private final EnumChatFormatting mcColor;
        private final int muiColor;
        private final float lowerBound, upperBound;
        private final float defaultValue;

        StarColorRGBM(EnumChatFormatting mcColor, int muiColor, float lower, float upper, float defaultVal) {
            this.title = "fog.cosmetics.color." + name().toLowerCase();
            this.mcColor = mcColor;
            this.muiColor = muiColor;
            this.lowerBound = lower;
            this.upperBound = upper;
            this.defaultValue = defaultVal;
        }

        public String tooltip(float value) {
            if (this == GAMMA) {
                return String.format("%s%s: %.1f", mcColor, translateToLocal(title), value);
            }
            return String.format("%s%s: %d", mcColor, translateToLocal(title), (int) value);
        }
    }

    public static Widget createStarColorRGBMGroup(StarColorRGBM color, DoubleConsumer setter, DoubleSupplier getter) {
        MultiChildWidget widget = new MultiChildWidget();
        widget.setSize(184, 16);

        // Title
        widget.addChild(
            new TextWidget(translateToLocal(color.title)).setDefaultColor(color.mcColor)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(0, 0)
                .setSize(32, 16));

        // Color slider
        widget.addChild(new SliderWidget().setSetter(val -> {
            int aux = (int) (val * 10);
            setter.accept(aux / 10d);
        })
            .setGetter(() -> (float) getter.getAsDouble())
            .setBounds(color.lowerBound, color.upperBound)
            .setHandleSize(new Size(4, 0))
            .dynamicTooltip(() -> {
                List<String> ret = new ArrayList<>();
                ret.add(color.tooltip((float) getter.getAsDouble()));
                return ret;
            })
            .setUpdateTooltipEveryTick(true)
            .setSize(118, 8)
            .setPos(32, 4));

        // Color manual text box
        Widget numberEntry = new NumericWidget().setSetter(setter)
            .setGetter(getter)
            .setBounds(color.lowerBound, color.upperBound)
            .setDefaultValue(color.defaultValue)
            .setTextAlignment(Alignment.Center)
            .setTextColor(color.muiColor)
            .setSize(32, 16)
            .setPos(152, 0)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);

        if (color == StarColorRGBM.GAMMA) {
            numberEntry.addTooltip(translateToLocal("fog.cosmetics.onlydecimals"));
            ((NumericWidget) numberEntry).setIntegerOnly(false);
        } else {
            numberEntry.addTooltip(translateToLocal("fog.cosmetics.onlyintegers"));
        }

        return widget.addChild(numberEntry);
    }

    public static Widget createStarColorButton(String text, String tooltip,
        BiConsumer<Widget.ClickData, Widget> onClick) {
        MultiChildWidget widget = new MultiChildWidget();
        widget.setSize(35, 15);

        widget.addChild(
            new ButtonWidget().setOnClick(onClick)
                .setSize(35, 15)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .addTooltip(translateToLocal(tooltip))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(0, 0));

        widget.addChild(
            TextWidget.localised(text)
                .setTextAlignment(Alignment.Center)
                .setPos(0, 0)
                .setSize(35, 15));

        return widget;
    }

    public static Widget createStarColorButton(Supplier<String> text, Supplier<String> tooltip,
        BiConsumer<Widget.ClickData, Widget> onClick) {
        MultiChildWidget widget = new MultiChildWidget();
        widget.setSize(35, 15);

        widget.addChild(
            new ButtonWidget().setOnClick(onClick)
                .setSize(35, 15)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .dynamicTooltip(() -> {
                    List<String> ret = new ArrayList<>();
                    ret.add(translateToLocal(tooltip.get()));
                    return ret;
                })
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(0, 0));

        widget.addChild(
            new DynamicTextWidget(() -> new Text(translateToLocal(text.get()))).setTextAlignment(Alignment.Center)
                .setPos(0, 0)
                .setSize(35, 15));

        return widget;
    }

    public static Widget getIndividualUpgradeGroup(ForgeOfGodsUpgrade upgrade, Supplier<Integer> shardGetter,
        Runnable complete, Runnable respec, Supplier<Boolean> check, Supplier<MilestoneFormatter> formatGetter) {
        MultiChildWidget widget = new MultiChildWidget();
        widget.setSize(upgrade.getWindowSize());

        Size windowSize = upgrade.getWindowSize();
        int w = windowSize.width;
        int h = windowSize.height;

        // Close window button
        widget.addChild(
            ButtonWidget.closeWindowButton(true)
                .setPos(w - 15, 3));

        // Background symbol
        widget.addChild(
            new DrawableWidget().setDrawable(upgrade.getSymbol())
                .setPos((int) ((1 - upgrade.getSymbolWidthRatio() / 2) * w / 2), h / 4)
                .setSize((int) (w / 2 * upgrade.getSymbolWidthRatio()), h / 2));

        // Background overlay
        widget.addChild(
            new DrawableWidget().setDrawable(upgrade.getOverlay())
                .setPos(w / 4, h / 4)
                .setSize(w / 2, h / 2));

        // Upgrade name title
        widget.addChild(
            new TextWidget(upgrade.getNameText()).setTextAlignment(Alignment.Center)
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setSize(w - 15, 30)
                .setPos(9, 5));

        // Upgrade body text
        widget.addChild(
            new TextWidget(upgrade.getBodyText()).setTextAlignment(Alignment.Center)
                .setDefaultColor(EnumChatFormatting.WHITE)
                .setSize(w - 15, upgrade.getLoreYPos() - 30)
                .setPos(9, 30));

        // Lore Text
        widget.addChild(
            new TextWidget(EnumChatFormatting.ITALIC + upgrade.getLoreText()).setTextAlignment(Alignment.Center)
                .setDefaultColor(0xbbbdbd)
                .setSize(w - 15, (int) (h * 0.9) - upgrade.getLoreYPos())
                .setPos(9, upgrade.getLoreYPos()));

        // Shard cost text
        String costStr = " " + EnumChatFormatting.BLUE + upgrade.getShardCost();
        widget.addChild(
            new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.shardcost") + costStr)
                .setTextAlignment(Alignment.Center)
                .setScale(0.7f)
                .setMaxWidth(70)
                .setDefaultColor(0x9c9c9c)
                .setPos(11, h - 25));

        // Available shards text
        widget.addChild(
            new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.availableshards"))
                .setTextAlignment(Alignment.Center)
                .setScale(0.7f)
                .setMaxWidth(90)
                .setDefaultColor(0x9c9c9c)
                .setPos(w - 87, h - 25));

        // Available shards amount
        widget.addChild(
            TextWidget.dynamicText(() -> getAvailableShardsText(upgrade, shardGetter, formatGetter))
                .setTextAlignment(Alignment.Center)
                .setScale(0.7f)
                .setMaxWidth(90)
                .setDefaultColor(0x9c9c9c)
                .setPos(w - 27, h - 18));

        // Complete button group
        MultiChildWidget completeGroup = new MultiChildWidget();
        completeGroup.setPos(w / 2 - 21, (int) (h * 0.9));

        // Complete button
        completeGroup.addChild(new ButtonWidget().setOnClick(($, $$) -> {
            if (check.get()) {
                respec.run();
            } else {
                complete.run();
            }
        })
            .setSize(40, 15)
            .setBackground(
                () -> new IDrawable[] {
                    check.get() ? GTUITextures.BUTTON_STANDARD_PRESSED : GTUITextures.BUTTON_STANDARD })
            .dynamicTooltip(() -> constructionStatusString(check))
            .setTooltipShowUpDelay(TOOLTIP_DELAY));

        // Complete text overlay
        completeGroup.addChild(
            TextWidget.dynamicText(() -> constructionStatusText(check))
                .setTextAlignment(Alignment.Center)
                .setScale(0.7f)
                .setMaxWidth(36)
                .setPos(3, 5));

        widget.addChild(completeGroup);

        return widget;
    }

    public static Widget createMaterialInputButton(ForgeOfGodsUpgrade upgrade, Supplier<Boolean> check,
        BiConsumer<Widget.ClickData, Widget> clickAction) {
        Size windowSize = upgrade.getWindowSize();
        int w = windowSize.width;
        int h = windowSize.height;

        return new ButtonWidget().setOnClick(clickAction)
            .setPlayClickSound(true)
            .setBackground(
                () -> new IDrawable[] { check.get() ? TecTechUITextures.BUTTON_BOXED_CHECKMARK_18x18
                    : TecTechUITextures.BUTTON_BOXED_EXCLAMATION_POINT_18x18 })
            .setPos(w / 2 - 40, (int) (h * 0.9))
            .setSize(15, 15)
            .dynamicTooltip(() -> upgradeMaterialRequirements(check))
            .addTooltip(
                EnumChatFormatting.GRAY + translateToLocal("fog.button.materialrequirements.tooltip.clickhere"));
    }

    private static Text getAvailableShardsText(ForgeOfGodsUpgrade upgrade, Supplier<Integer> shardGetter,
        Supplier<MilestoneFormatter> formatGetter) {
        EnumChatFormatting enoughShards = EnumChatFormatting.RED;
        if (shardGetter.get() >= upgrade.getShardCost()) {
            enoughShards = EnumChatFormatting.GREEN;
        }
        return new Text(
            enoughShards + formatGetter.get()
                .format(shardGetter.get()));
    }

    private static List<String> constructionStatusString(Supplier<Boolean> check) {
        if (check.get()) {
            return ImmutableList.of(translateToLocal("fog.upgrade.respec"));
        }
        return ImmutableList.of(translateToLocal("fog.upgrade.confirm"));
    }

    private static Text constructionStatusText(Supplier<Boolean> check) {
        if (check.get()) {
            return new Text(translateToLocal("fog.upgrade.respec"));
        }
        return new Text(translateToLocal("fog.upgrade.confirm"));
    }

    private static List<String> upgradeMaterialRequirements(Supplier<Boolean> check) {
        if (check.get()) {
            return ImmutableList.of(translateToLocal("fog.button.materialrequirementsmet.tooltip"));
        }
        return ImmutableList.of(translateToLocal("fog.button.materialrequirements.tooltip"));
    }

    public static Widget createExtraCostWidget(final ItemStack costStack, Supplier<Short> paidAmount) {
        MultiChildWidget widget = new MultiChildWidget();
        widget.setSize(36, 18);

        if (costStack == null) {
            // Nothing to pay, so just create a simple disabled slot drawable
            widget.addChild(
                new DrawableWidget().setDrawable(GTUITextures.BUTTON_STANDARD_DISABLED)
                    .setSize(18, 18));
            return widget;
        }

        // Item slot
        ItemStackHandler handler = new ItemStackHandler(1);
        ItemStack handlerStack = costStack.copy();
        handlerStack.stackSize = Math.max(1, handlerStack.stackSize - paidAmount.get());
        handler.setStackInSlot(0, handlerStack);
        widget.addChild(
            new SlotWidget(handler, 0).setAccess(false, false)
                .setRenderStackSize(false)
                .disableInteraction()
                .setBackground(GTUITextures.BUTTON_STANDARD_PRESSED))
            .addChild(new ButtonWidget().setOnClick((clickData, w) -> {
                if (widget.isClient()) {
                    if (clickData.mouseButton == 0) {
                        GuiCraftingRecipe.openRecipeGui("item", handlerStack.copy());
                    } else if (clickData.mouseButton == 1) {
                        GuiUsageRecipe.openRecipeGui("item", handlerStack.copy());
                    }
                }
            })
                .setSize(16, 16)
                .setPos(1, 1));

        // Progress text
        widget.addChild(new DynamicTextWidget(() -> {
            short paid = paidAmount.get();
            EnumChatFormatting color = EnumChatFormatting.YELLOW;
            if (paid == 0) color = EnumChatFormatting.RED;
            else if (paid == costStack.stackSize) color = EnumChatFormatting.GREEN;
            return new Text(color + "x" + (costStack.stackSize - paid));
        }).setTextAlignment(Alignment.Center)
            .setScale(0.8f)
            .setPos(18, 5)
            .setSize(18, 9)
            .setEnabled(w -> paidAmount.get() < costStack.stackSize));

        // Completed checkmark
        widget.addChild(
            new DrawableWidget().setDrawable(TecTechUITextures.GREEN_CHECKMARK_11x9)
                .setPos(21, 5)
                .setSize(11, 9)
                .setEnabled(w -> paidAmount.get() >= costStack.stackSize));

        return widget;
    }

    public static ModularWindow createSpecialThanksWindow() {
        final int WIDTH = 200;
        final int HEIGHT = 200;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);

        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_RAINBOW);
        builder.setDraggable(true);
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(184, 4))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_THANKS)
                    .setPos(50, 50)
                    .setSize(100, 100))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.contributors"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setScale(1f)
                    .setPos(0, 5)
                    .setSize(200, 15))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.lead"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 30)
                        .setSize(60, 10))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.cloud")).setScale(0.8f)
                    .setDefaultColor(EnumChatFormatting.AQUA)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 40)
                    .setSize(60, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.programming"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 55)
                        .setSize(60, 10))
            .widget(
                new TextWidget(
                    translateToLocal("gt.blockmachines.multimachine.FOG.serenibyss") + " "
                        + EnumChatFormatting.DARK_AQUA
                        + translateToLocal("gt.blockmachines.multimachine.FOG.teg")).setScale(0.8f)
                            .setTextAlignment(Alignment.CenterLeft)
                            .setPos(7, 67)
                            .setSize(60, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.textures"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 85)
                        .setSize(100, 10))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.ant")).setScale(0.8f)
                    .setDefaultColor(EnumChatFormatting.GREEN)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 95)
                    .setSize(60, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.rendering"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 110)
                        .setSize(100, 10))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.bucket")).setScale(0.8f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 120)
                    .setSize(60, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.lore"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 135)
                        .setSize(100, 10))
            .widget(
                delenoName().setSpace(-1)
                    .setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setPos(7, 145)
                    .setSize(60, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.UNDERLINE + translateToLocal("gt.blockmachines.multimachine.FOG.playtesting"))
                        .setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 160)
                        .setSize(100, 10))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.misi")).setScale(0.8f)
                    .setDefaultColor(0xffc26f)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 170)
                    .setSize(60, 10))
            .widget(
                new TextWidget(EnumChatFormatting.ITALIC + translateToLocal("gt.blockmachines.multimachine.FOG.thanks"))
                    .setScale(0.8f)
                    .setDefaultColor(0xbbbdbd)
                    .setTextAlignment(Alignment.Center)
                    .setPos(90, 140)
                    .setSize(100, 60));
        return builder.build();
    }

    private static DynamicPositionedRow delenoName() {
        DynamicPositionedRow nameRow = new DynamicPositionedRow();
        String deleno = translateToLocal("gt.blockmachines.multimachine.FOG.deleno");
        int[] colors = new int[] { 0xffffff, 0xf6fff5, 0xecffec, 0xe3ffe2, 0xd9ffd9, 0xd0ffcf };

        for (int i = 0; i < deleno.length(); i++) {
            nameRow.addChild(
                new TextWidget(Character.toString(deleno.charAt(i))).setDefaultColor(colors[i])
                    .setScale(0.8f)
                    .setTextAlignment(Alignment.CenterLeft));
        }
        return nameRow;
    }
}
