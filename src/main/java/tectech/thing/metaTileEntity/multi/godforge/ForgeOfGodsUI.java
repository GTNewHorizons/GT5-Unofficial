package tectech.thing.metaTileEntity.multi.godforge;

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

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SliderWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.TecTech;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;

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
        button.addTooltip("Power Switch")
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    public static ButtonWidget createInputSeparationButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setInputSeparation(!mte.isInputSeparationEnabled());
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
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.input_separation"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getInputSeparationButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public static ButtonWidget createBatchModeButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setBatchMode(!mte.isBatchModeEnabled());
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
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getBatchModeButtonPos())
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public static ButtonWidget createLockToSingleRecipeButton(final IGregTechTileEntity tileEntity,
        final IControllerWithOptionalFeatures mte, IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(tileEntity, "fx_click");
            mte.setRecipeLocking(!mte.isRecipeLockingEnabled());
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
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(mte.getRecipeLockingButtonPos())
            .setSize(16, 16);
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
                TextWidget.dynamicText(() -> inversionInfoText(inversionGetter.get()))
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
}
