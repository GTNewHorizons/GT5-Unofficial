package gregtech.common.powergoggles.gui;

import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.overlay.OverlayHandler;
import com.cleanroommc.modularui.overlay.OverlayManager;
import com.cleanroommc.modularui.screen.CustomModularScreen;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ColorPickerDialog;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public class PowerGogglesGuiOverlay {

    private static String[] settings = { "GT5U.power_goggles_config.settings_general",
        "GT5U.power_goggles_config.settings_color" };
    private static int settingsPage = 0;
    private static IDrawable background = new Rectangle().setColor(Color.argb(0, 0, 0, 100));
    private static String manualGraphMinInput = "0";
    private static String manualGraphMaxInput = "1000";
    private static boolean manualMinInvalid = false;
    private static boolean manualMaxInvalid = false;

    public static void init() {

        OverlayManager.register(
            new OverlayHandler(
                screen -> screen instanceof PowerGogglesGuiHudConfig,
                PowerGogglesGuiOverlay::buildScreen));
    }

    private static CustomModularScreen buildScreen(GuiScreen screen) {
        PowerGogglesGuiHudConfig gui = (PowerGogglesGuiHudConfig) screen;
        manualGraphMinInput = PowerGogglesConfigHandler.manualGraphMin;
        manualGraphMaxInput = PowerGogglesConfigHandler.manualGraphMax;
        validateManualScaleInputs();
        PagedWidget.Controller controller = new PagedWidget.Controller();
        int height = 217;
        ModularPanel overlayPanel = ModularPanel
            .defaultPanel("power_goggles_overlay", gui.displayWidth, gui.displayHeight)
            .size(230 + 2, height)
            .background(IDrawable.EMPTY)
            .leftRel(0.5f)
            .top(10);

        PagedWidget<?> pagedWidget = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(settingsPage);
            }
        }.controller(controller)
            .addPage(makeGeneralSettingsPage(gui))
            .addPage(makeColorSchemePage(overlayPanel));

        return new CustomModularScreen() {

            @Override
            public @NotNull ModularPanel buildUI(ModularGuiContext context) {
                return overlayPanel.child(
                    new Column().sizeRel(1)
                        .child(
                            new SingleChildWidget<>().size(230, 22)
                                .background(background)
                                .height(22)
                                .paddingBottom(4)
                                .child(makePagedWidgetButton(controller)))
                        .child(
                            new SingleChildWidget<>().size(230, height - 22)
                                .child(pagedWidget.sizeRel(1))));
            }
        };
    }

    private static IWidget makePagedWidgetButton(PagedWidget.Controller controller) {
        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> IKey.lang(settings[settingsPage])))
            .onMousePressed(mouseButton -> {
                settingsPage = ++settingsPage % settings.length;
                controller.setPage(settingsPage);
                return true;
            })
            .sizeRel(1)
            .align(Alignment.Center);
    }

    private static IWidget makeGeneralSettingsPage(PowerGogglesGuiHudConfig gui) {
        Flow settings = new Column().coverChildren()
            .background(background);
        settings.child(makeNotationButton(gui))
            .child(makeReadingButton(gui))
            .child(makeGraphScaleButton(gui));
        if (PowerGogglesConfigHandler.manualGraphScale) {
            settings.child(makeManualScaleMinRow())
                .child(makeManualScaleMaxRow());
        }
        return settings.child(makeShowPowerBarButton())
            .child(makeChatHidesHudButton())
            .child(makeResetDefaultsButton(gui))
            .child(
                makeSliderFlow(
                    () -> PowerGogglesConfigHandler.mainTextScaling,
                    val -> PowerGogglesConfigHandler.mainTextScaling = val,
                    "Storage Text Scale",
                    "GT5U.power_goggles_config.main_text_scale"))
            .child(
                makeSliderFlow(
                    () -> PowerGogglesConfigHandler.subTextScaling,
                    val -> PowerGogglesConfigHandler.subTextScaling = val,
                    "Timed Reading Text Scale",
                    "GT5U.power_goggles_config.sub_text_scale"))
            .child(
                makeSliderFlow(
                    () -> PowerGogglesConfigHandler.hudScale,
                    val -> PowerGogglesConfigHandler.hudScale = val,
                    "HUD Scale",
                    "GT5U.power_goggles_config.hud_scale"));
    }

    private static IWidget makeNotationButton(PowerGogglesGuiHudConfig gui) {
        ButtonWidget<?> notationButton = new ButtonWidget<>().overlay(
            IKey.lang(
                "GT5U.power_goggles_config.toggle_notation",
                StatCollector.translateToLocal(gui.formatTypes[PowerGogglesConfigHandler.formatIndex])));
        notationButton.onMousePressed(mouseButton -> {
            PowerGogglesConfigHandler.formatIndex = (PowerGogglesConfigHandler.formatIndex + 1)
                % gui.formatTypes.length;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Format Index")
                .set(PowerGogglesConfigHandler.formatIndex);
            PowerGogglesConfigHandler.config.save();
            notationButton.overlay(
                IKey.lang(
                    "GT5U.power_goggles_config.toggle_notation",
                    StatCollector.translateToLocal(gui.formatTypes[PowerGogglesConfigHandler.formatIndex])));
            return true;
        });
        return notationButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeReadingButton(PowerGogglesGuiHudConfig gui) {
        ButtonWidget<?> readingButton = new ButtonWidget<>().overlay(
            IKey.lang(
                "GT5U.power_goggles_config.toggle_reading",
                StatCollector.translateToLocal(gui.readingTypes[PowerGogglesConfigHandler.readingIndex])));
        readingButton.onMousePressed(mouseButton -> {
            PowerGogglesConfigHandler.readingIndex = (PowerGogglesConfigHandler.readingIndex + 1)
                % gui.readingTypes.length;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Reading Index")
                .set(PowerGogglesConfigHandler.readingIndex);
            PowerGogglesConfigHandler.config.save();
            readingButton.overlay(
                IKey.lang(
                    "GT5U.power_goggles_config.toggle_reading",
                    StatCollector.translateToLocal(gui.readingTypes[PowerGogglesConfigHandler.readingIndex])));
            return true;
        });
        return readingButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeChatHidesHudButton() {
        ButtonWidget<?> chatHidesHudButton = new ButtonWidget<>().overlay(
            IKey.lang(
                "GT5U.power_goggles_config.toggle_hud_with_chat",
                (PowerGogglesConfigHandler.hideWhenChatOpen ? IKey.lang("gui.yes") : IKey.lang("gui.no"))));
        chatHidesHudButton.onMousePressed(mouseButton -> {
            PowerGogglesConfigHandler.hideWhenChatOpen = !PowerGogglesConfigHandler.hideWhenChatOpen;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Hide HUD")
                .set(PowerGogglesConfigHandler.hideWhenChatOpen);
            PowerGogglesConfigHandler.config.save();
            chatHidesHudButton.overlay(
                IKey.lang(
                    "GT5U.power_goggles_config.toggle_hud_with_chat",
                    (PowerGogglesConfigHandler.hideWhenChatOpen ? IKey.lang("gui.yes") : IKey.lang("gui.no"))));
            return true;
        });
        return chatHidesHudButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeGraphScaleButton(PowerGogglesGuiHudConfig gui) {
        ButtonWidget<?> graphScaleButton = new ButtonWidget<>().overlay(
            IKey.lang(
                "GT5U.power_goggles_config.graph_scale",
                IKey.lang(
                    PowerGogglesConfigHandler.manualGraphScale ? "GT5U.power_goggles_config.graph_scale_manual"
                        : "GT5U.power_goggles_config.graph_scale_auto")));
        graphScaleButton.onMousePressed(mouseButton -> {
            validateManualScaleInputs();
            if (PowerGogglesConfigHandler.manualGraphScale && !isManualScaleInputValid()) {
                return true;
            }
            PowerGogglesConfigHandler.manualGraphScale = !PowerGogglesConfigHandler.manualGraphScale;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Manual Graph Scale")
                .set(PowerGogglesConfigHandler.manualGraphScale);
            PowerGogglesConfigHandler.config.save();

            PowerGogglesGuiHudConfig refreshedScreen = new PowerGogglesGuiHudConfig(
                gui.displayWidth,
                gui.displayHeight);
            refreshedScreen.parentScreen = gui.parentScreen;
            Minecraft.getMinecraft()
                .displayGuiScreen(refreshedScreen);
            return true;
        });
        return graphScaleButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeManualScaleMinRow() {
        return new Row().size(230, 18)
            .marginBottom(4)
            .child(
                IKey.lang("GT5U.power_goggles_config.manual_graph_min")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .paddingLeft(3)
                    .width(106)
                    .marginRight(14)
                    .alignment(Alignment.CenterRight))
            .child(
                new TextFieldWidget().size(110, 18)
                    .setTextAlignment(Alignment.Center)
                    .setFormatAsInteger(true)
                    .background(
                        new DynamicDrawable(
                            () -> manualMinInvalid
                                ? new DrawableStack(
                                    GTGuiTextures.BACKGROUND_TEXT_FIELD,
                                    new Rectangle().setColor(Color.argb(200, 40, 40, 110)))
                                : GTGuiTextures.BACKGROUND_TEXT_FIELD))
                    .value(new StringValue.Dynamic(() -> manualGraphMinInput, val -> {
                        manualGraphMinInput = val == null ? "" : val.trim();
                        validateManualScaleInputs();
                        if (isManualScaleInputValid()) {
                            saveManualScaleInputs();
                        }
                    }))
                    .setMaxLength(40)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.power_goggles_config.manual_graph_min_tooltip"))));
    }

    private static IWidget makeManualScaleMaxRow() {
        return new Row().size(230, 18)
            .marginBottom(4)
            .child(
                IKey.lang("GT5U.power_goggles_config.manual_graph_max")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .paddingLeft(3)
                    .width(106)
                    .marginRight(14)
                    .alignment(Alignment.CenterRight))
            .child(
                new TextFieldWidget().size(110, 18)
                    .setTextAlignment(Alignment.Center)
                    .setFormatAsInteger(true)
                    .background(
                        new DynamicDrawable(
                            () -> manualMaxInvalid
                                ? new DrawableStack(
                                    GTGuiTextures.BACKGROUND_TEXT_FIELD,
                                    new Rectangle().setColor(Color.argb(200, 40, 40, 110)))
                                : GTGuiTextures.BACKGROUND_TEXT_FIELD))
                    .value(new StringValue.Dynamic(() -> manualGraphMaxInput, val -> {
                        manualGraphMaxInput = val == null ? "" : val.trim();
                        validateManualScaleInputs();
                        if (isManualScaleInputValid()) {
                            saveManualScaleInputs();
                        }
                    }))
                    .setMaxLength(40)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.power_goggles_config.manual_graph_max_tooltip"))));
    }

    private static void validateManualScaleInputs() {
        BigInteger min = parseNonNegativeBigInteger(manualGraphMinInput);
        BigInteger max = parseNonNegativeBigInteger(manualGraphMaxInput);
        boolean invalidRange = min != null && max != null && min.compareTo(max) >= 0;
        manualMinInvalid = min == null || invalidRange;
        manualMaxInvalid = max == null || invalidRange;
    }

    private static boolean isManualScaleInputValid() {
        return !manualMinInvalid && !manualMaxInvalid;
    }

    private static BigInteger parseNonNegativeBigInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            BigInteger parsed = new BigInteger(value);
            return parsed.compareTo(BigInteger.ZERO) < 0 ? null : parsed;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static void saveManualScaleInputs() {
        PowerGogglesConfigHandler.manualGraphMin = manualGraphMinInput;
        PowerGogglesConfigHandler.manualGraphMax = manualGraphMaxInput;
        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
            .get("Manual Graph Min")
            .set(PowerGogglesConfigHandler.manualGraphMin);
        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
            .get("Manual Graph Max")
            .set(PowerGogglesConfigHandler.manualGraphMax);
        PowerGogglesConfigHandler.config.save();
    }

    private static IWidget makeShowPowerBarButton() {
        ButtonWidget<?> showPowerBarButton = new ButtonWidget<>().overlay(
            IKey.lang(
                "GT5U.power_goggles_config.toggle_power_bar",
                (PowerGogglesConfigHandler.showPowerBar ? IKey.lang("gui.yes") : IKey.lang("gui.no"))));
        showPowerBarButton
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.power_goggles_config.toggle_power_bar_tooltip")));
        showPowerBarButton.onMousePressed(mouseButton -> {
            PowerGogglesConfigHandler.showPowerBar = !PowerGogglesConfigHandler.showPowerBar;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Show Power Bar")
                .set(PowerGogglesConfigHandler.showPowerBar);
            PowerGogglesConfigHandler.config.save();
            showPowerBarButton.overlay(
                IKey.lang(
                    "GT5U.power_goggles_config.toggle_power_bar",
                    (PowerGogglesConfigHandler.showPowerBar ? IKey.lang("gui.yes") : IKey.lang("gui.no"))));
            return true;
        });
        return showPowerBarButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeResetDefaultsButton(PowerGogglesGuiHudConfig gui) {
        ButtonWidget<?> resetDefaultsButton = new ButtonWidget<>()
            .overlay(IKey.lang("GT5U.power_goggles_config.reset_defaults"));
        resetDefaultsButton
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.power_goggles_config.reset_defaults_tooltip")));
        resetDefaultsButton.onMousePressed(mouseButton -> {
            PowerGogglesConfigHandler.resetToDefaults();

            PowerGogglesGuiHudConfig refreshedScreen = new PowerGogglesGuiHudConfig(
                gui.displayWidth,
                gui.displayHeight);
            refreshedScreen.parentScreen = gui.parentScreen;
            Minecraft.getMinecraft()
                .displayGuiScreen(refreshedScreen);
            return true;
        });
        return resetDefaultsButton.size(230, 18)
            .marginBottom(4);
    }

    private static IWidget makeSliderFlow(Supplier<Double> valSupplier, Consumer<Double> setter, String key,
        String textKey) {
        return new Row().size(230, 18)
            .marginBottom(4)
            .child(
                IKey.lang(textKey)
                    .color(Color.WHITE.main)
                    .asWidget()
                    .paddingLeft(3)
                    .width(106)
                    .marginRight(14)
                    .alignment(Alignment.CenterRight))
            .child(
                new SliderWidget().size(110, 18)
                    .background(GuiTextures.MC_BUTTON)
                    .bounds(0, 2)
                    .stopper(0.1)
                    .value(new DoubleValue.Dynamic(valSupplier::get, val -> {
                        setter.accept(val);
                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                            .get(key)
                            .set(val);
                        PowerGogglesConfigHandler.config.save();
                    })));
    }

    private static IWidget makeColorSchemePage(ModularPanel overlayPanel) {
        return new Column().coverChildren()
            .background(background)
            .align(Alignment.TopCenter)
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.textBadColor,
                    val -> { PowerGogglesConfigHandler.textBadColor = val; },
                    "Bad Text",
                    "GT5U.power_goggles_config.text_bad",
                    "GT5U.power_goggles_config.text_bad_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.textOkColor,
                    val -> { PowerGogglesConfigHandler.textOkColor = val; },
                    "Ok Text",
                    "GT5U.power_goggles_config.text_ok",
                    "GT5U.power_goggles_config.text_ok_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.textGoodColor,
                    val -> { PowerGogglesConfigHandler.textGoodColor = val; },
                    "Good Text",
                    "GT5U.power_goggles_config.text_good",
                    "GT5U.power_goggles_config.text_good_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.gradientBadColor,
                    val -> { PowerGogglesConfigHandler.gradientBadColor = val; },
                    "Bad Gradient",
                    "GT5U.power_goggles_config.gradient_bad",
                    "GT5U.power_goggles_config.gradient_bad_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.gradientOkColor,
                    val -> { PowerGogglesConfigHandler.gradientOkColor = val; },
                    "Ok Gradient",
                    "GT5U.power_goggles_config.gradient_ok",
                    "GT5U.power_goggles_config.gradient_ok_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.gradientGoodColor,
                    val -> { PowerGogglesConfigHandler.gradientGoodColor = val; },
                    "Good Gradient",
                    "GT5U.power_goggles_config.gradient_good",
                    "GT5U.power_goggles_config.gradient_good_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.chartBackgroundColor,
                    val -> { PowerGogglesConfigHandler.chartBackgroundColor = val; },
                    "Chart Background Color",
                    "GT5U.power_goggles_config.background_color",
                    "GT5U.power_goggles_config.background_color_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.chartBorderColor,
                    val -> { PowerGogglesConfigHandler.chartBorderColor = val; },
                    "Chart Border Color",
                    "GT5U.power_goggles_config.border_color",
                    "GT5U.power_goggles_config.border_color_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.chartMinTextColor,
                    val -> { PowerGogglesConfigHandler.chartMinTextColor = val; },
                    "Chart Min Text Color",
                    "GT5U.power_goggles_config.chart_min_text_color",
                    "GT5U.power_goggles_config.chart_min_text_color_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.masurementsBackgroundColor,
                    val -> { PowerGogglesConfigHandler.masurementsBackgroundColor = val; },
                    "Measurements Background Color",
                    "GT5U.power_goggles_config.background_lines_color",
                    "GT5U.power_goggles_config.background_lines_color_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.chartMaxTextColor,
                    val -> { PowerGogglesConfigHandler.chartMaxTextColor = val; },
                    "Chart Max Text Color",
                    "GT5U.power_goggles_config.chart_max_text_color",
                    "GT5U.power_goggles_config.chart_max_text_color_tooltip"))
            .child(
                makeColorConfigButton(
                    overlayPanel,
                    () -> PowerGogglesConfigHandler.chartManualScaleIndicatorColor,
                    val -> { PowerGogglesConfigHandler.chartManualScaleIndicatorColor = val; },
                    "Chart Manual Scale Indicator Color",
                    "GT5U.power_goggles_config.chart_manual_scale_indicator_color",
                    "GT5U.power_goggles_config.chart_manual_scale_indicator_color_tooltip"))

            .child(
                new Row().size(228, 18)
                    .marginBottom(4)
                    .child(
                        new DynamicDrawable(
                            () -> new Rectangle().setHorizontalGradient(
                                PowerGogglesConfigHandler.gradientBadColor,
                                PowerGogglesConfigHandler.gradientOkColor)).asWidget()
                                    .size(114, 18))
                    .child(
                        new DynamicDrawable(
                            () -> new Rectangle().setHorizontalGradient(
                                PowerGogglesConfigHandler.gradientOkColor,
                                PowerGogglesConfigHandler.gradientGoodColor)).asWidget()
                                    .size(114, 18)));
    }

    private static IWidget makeColorConfigButton(ModularPanel overlayPanel, Supplier<Integer> colorSupplier,
        Consumer<Integer> setter, String key, String buttonKey, String tooltipKey) {
        IPanelHandler colorPicker = IPanelHandler
            .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog(key, val -> {
                setter.accept(val);
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get(key)
                    .set(val);
                PowerGogglesConfigHandler.config.save();
            }, colorSupplier.get(), true).size(200, 100), true);

        return new Row().size(230, 18)
            .marginBottom(4)
            .child(
                IKey.lang(buttonKey)
                    .color(Color.WHITE.main)
                    .asWidget()
                    .size(106, 18)
                    .marginRight(14)
                    .alignment(Alignment.CenterRight))
            .child(
                new ButtonWidget<>().size(18, 18)
                    .overlay(new DynamicDrawable(() -> new Rectangle().setColor(colorSupplier.get())))
                    .hoverOverlay(new DynamicDrawable(() -> new Rectangle().setColor(colorSupplier.get())))
                    .tooltipBuilder(t -> t.addLine(IKey.lang((tooltipKey))))
                    .onMousePressed(d -> {
                        colorPicker.openPanel();
                        return true;
                    }));
    }
}
