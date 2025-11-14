package gregtech.common.powergoggles.gui;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
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
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ColorPickerDialog;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public class PowerGogglesGuiOverlay {

    private static String[] settings = { "GT5U.power_goggles_config.settings_general",
        "GT5U.power_goggles_config.settings_color" };
    private static int settingsPage = 0;
    private static IDrawable background = new Rectangle().setColor(Color.argb(0, 0, 0, 100));

    public static void init() {

        OverlayManager.register(
            new OverlayHandler(
                screen -> screen instanceof PowerGogglesGuiHudConfig,
                PowerGogglesGuiOverlay::buildScreen));
    }

    private static CustomModularScreen buildScreen(GuiScreen screen) {
        PowerGogglesGuiHudConfig gui = (PowerGogglesGuiHudConfig) screen;
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
        return new Column().coverChildren()
            .background(background)
            .child(makeNotationButton(gui))
            .child(makeReadingButton(gui))
            .child(makeChatHidesHudButton())
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
