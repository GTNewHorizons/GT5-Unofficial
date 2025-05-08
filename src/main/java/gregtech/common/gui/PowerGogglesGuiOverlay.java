package gregtech.common.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.overlay.OverlayHandler;
import com.cleanroommc.modularui.overlay.OverlayManager;
import com.cleanroommc.modularui.screen.CustomModularScreen;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ColorPickerDialog;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.common.gui.modularui.NoSyncDouble;
import gregtech.common.handlers.PowerGogglesConfigHandler;

public class PowerGogglesGuiOverlay {

    private static String[] settings = { "GT5U.power_goggles_config.settings_general",
        "GT5U.power_goggles_config.settings_color" };
    private static int settingsPage = 0;

    public static void init() {
        ModularPanel hackPanel = ModularPanel.defaultPanel("overlay")
            .size(0)
            .pos(-100, 0);

        OverlayManager.register(new OverlayHandler(screen -> screen instanceof PowerGogglesGuiHudConfig, screen -> {
            PowerGogglesGuiHudConfig gui = (PowerGogglesGuiHudConfig) screen;
            PagedWidget.Controller controller = new PagedWidget.Controller();
            int height = 217;
            IDrawable background = new Rectangle().setColor(Color.argb(0, 0, 0, 100));
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
            }.controller(controller);

            ButtonWidget<?> pagedWidgetButton = new ButtonWidget<>().overlay(IKey.lang(settings[settingsPage]));
            pagedWidgetButton.onMousePressed(mouseButton -> {
                settingsPage = ++settingsPage % settings.length;
                controller.setPage(settingsPage);
                pagedWidgetButton.overlay(IKey.lang(settings[settingsPage]));
                return true;
            });

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
                    IKey.str(
                        "Toggle Notation: "
                            + StatCollector.translateToLocal(gui.formatTypes[PowerGogglesConfigHandler.formatIndex])));
                return true;
            });

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

            ButtonWidget<?> mainTextScaleUpButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.main_text_scale") + "-"));
            mainTextScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.mainTextScaling -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(PowerGogglesConfigHandler.mainTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> mainTextScaleDownButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.main_text_scale") + "+"));
            mainTextScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.mainTextScaling += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(PowerGogglesConfigHandler.mainTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            ButtonWidget<?> subTextScaleUpButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.sub_text_scale") + "-"));
            subTextScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.subTextScaling -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(PowerGogglesConfigHandler.subTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> subTextScaleDownButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.sub_text_scale") + "+"));
            subTextScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.subTextScaling += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(PowerGogglesConfigHandler.subTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            ButtonWidget<?> hudScaleUpButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.hud_scale") + "-"));
            hudScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.hudScale -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(PowerGogglesConfigHandler.hudScale);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> hudScaleDownButton = new ButtonWidget<>()
                .overlay(IKey.str(IKey.lang("GT5U.power_goggles_config.hud_scale") + "+"));
            hudScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.hudScale += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(PowerGogglesConfigHandler.hudScale);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            pagedWidget.addPage(
                new Column().coverChildren()
                    .background(background)
                    .child(
                        notationButton.size(230, 18)
                            .marginBottom(4))
                    .child(
                        readingButton.size(230, 18)
                            .marginBottom(4))
                    .child(
                        chatHidesHudButton.size(230, 18)
                            .marginBottom(4))
                    .child(
                        new Row().size(230, 18)
                            .marginBottom(4)
                            .child(
                                IKey.lang("GT5U.power_goggles_config.main_text_scale")
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
                                    .value(new NoSyncDouble(() -> PowerGogglesConfigHandler.mainTextScaling, val -> {
                                        PowerGogglesConfigHandler.mainTextScaling = val;
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Storage Text Scale")
                                            .set(PowerGogglesConfigHandler.mainTextScaling);
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new Row().size(230, 18)
                            .marginBottom(4)
                            .child(
                                IKey.lang("GT5U.power_goggles_config.sub_text_scale")
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
                                    .value(new NoSyncDouble(() -> PowerGogglesConfigHandler.subTextScaling, val -> {
                                        PowerGogglesConfigHandler.subTextScaling = val;
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Timed Reading Text Scale")
                                            .set(PowerGogglesConfigHandler.subTextScaling);
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new Row().size(230, 18)
                            .marginBottom(4)
                            .child(
                                IKey.lang("GT5U.power_goggles_config.hud_scale")
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
                                    .value(new NoSyncDouble(() -> PowerGogglesConfigHandler.hudScale, val -> {
                                        PowerGogglesConfigHandler.hudScale = val;
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("HUD Scale")
                                            .set(PowerGogglesConfigHandler.hudScale);
                                        PowerGogglesConfigHandler.config.save();
                                    })))));
            IPanelHandler colorPickerBad = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("badG", val -> {
                    PowerGogglesConfigHandler.gradientBadColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Bad Gradient")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.gradientBadColor, true).size(200, 100), true);

            IPanelHandler colorPickerOk = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("okG", val -> {
                    PowerGogglesConfigHandler.gradientOkColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Ok Gradient")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.gradientOkColor, true).size(200, 100), true);

            IPanelHandler colorPickerGood = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("goodG", val -> {
                    PowerGogglesConfigHandler.gradientGoodColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Good Gradient")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.gradientGoodColor, true).size(200, 100), true);

            IPanelHandler colorPickerBadText = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("badG", val -> {
                    PowerGogglesConfigHandler.textBadColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Bad Text")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.textBadColor, true).size(200, 100), true);

            IPanelHandler colorPickerOkText = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("okG", val -> {
                    PowerGogglesConfigHandler.textOkColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Ok Text")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.textOkColor, true).size(200, 100), true);

            IPanelHandler colorPickerGoodText = IPanelHandler
                .simple(overlayPanel, (bla, blab) -> new ColorPickerDialog("goodG", val -> {
                    PowerGogglesConfigHandler.textGoodColor = val;
                    PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                        .get("Good Text")
                        .set(val);
                    PowerGogglesConfigHandler.config.save();
                }, PowerGogglesConfigHandler.textGoodColor, true).size(200, 100), true);

            pagedWidget.addPage(
                new Column().coverChildren()
                    .background(background)
                    .align(Alignment.TopCenter)
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> IKey.str("Bad Gradient Color")
                                        .color(PowerGogglesConfigHandler.gradientBadColor)))
                            .marginBottom(4)
                            .tooltipBuilder(
                                t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.gradient_bad_tooltip"))))
                            .onMousePressed(d -> {
                                colorPickerBad.openPanel();
                                return true;
                            }))
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> IKey.str("Ok Gradient Color")
                                        .color(PowerGogglesConfigHandler.gradientOkColor)))
                            .tooltipBuilder(
                                t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.gradient_ok_tooltip"))))
                            .marginBottom(4)
                            .onMousePressed(d -> {
                                colorPickerOk.openPanel();
                                return true;
                            }))
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> IKey.str("Good Gradient Color")
                                        .color(PowerGogglesConfigHandler.gradientGoodColor)))
                            .tooltipBuilder(
                                t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.gradient_good_tooltip"))))
                            .marginBottom(4)
                            .onMousePressed(d -> {
                                colorPickerGood.openPanel();
                                return true;
                            }))
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
                                            .size(114, 18)))
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> IKey.str("Bad Text Color")
                                        .color(PowerGogglesConfigHandler.textBadColor)))
                            .tooltipBuilder(t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.text_bad_tooltip"))))
                            .marginBottom(4)
                            .onMousePressed(d -> {
                                colorPickerBadText.openPanel();
                                return true;
                            }))
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> IKey.str("Ok Text Color")
                                        .color(PowerGogglesConfigHandler.textOkColor)))
                            .tooltipBuilder(t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.text_ok_tooltip"))))
                            .marginBottom(4)
                            .onMousePressed(d -> {
                                colorPickerOkText.openPanel();
                                return true;
                            }))
                    .child(
                        new ButtonWidget<>().size(230, 18)
                            .overlay(
                                new DynamicDrawable(
                                    () -> new DynamicDrawable(
                                        () -> IKey.str("Good Text Color")
                                            .color(PowerGogglesConfigHandler.textGoodColor))))
                            .tooltipBuilder(t -> t.addLine(IKey.lang(("GT5U.power_goggles_config.text_good_tooltip"))))
                            .onMousePressed(d -> {
                                colorPickerGoodText.openPanel();
                                return true;
                            })));

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
                                    .child(
                                        pagedWidgetButton.sizeRel(1)
                                            .align(Alignment.Center)))
                            .child(
                                new SingleChildWidget<>().size(230, height - 22)
                                    .child(pagedWidget.sizeRel(1))));
                }
            };
        }));
        // Render an overlay in the main menu to prevent a ~1s delay when opening the config gui for the first time
        // This is a known bug with low priority
        OverlayManager.register(
            new OverlayHandler(screen -> screen instanceof GuiMainMenu, screen -> new ModularScreen(hackPanel)));
    }
}
