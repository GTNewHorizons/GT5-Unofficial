package gregtech.common.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.overlay.OverlayHandler;
import com.cleanroommc.modularui.overlay.OverlayManager;
import com.cleanroommc.modularui.screen.CustomModularScreen;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.handlers.PowerGogglesConfigHandler;
import gregtech.common.handlers.PowerGogglesHudHandler;

public class PowerGogglesGuiOverlay {

    private static String[] settings = { "General settings", "Scale settings", "Color Scheme: Gradient",
        "Color Scheme: Text" };
    private static int settingsPage = 0;

    public static void init() {
        OverlayManager.register(new OverlayHandler(screen -> screen instanceof PowerGogglesGuiHudConfig, screen -> {
            PowerGogglesGuiHudConfig gui = (PowerGogglesGuiHudConfig) screen;
            PagedWidget.Controller controller = new PagedWidget.Controller();
            PagedWidget<?> pagedWidget = new PagedWidget() {

                @Override
                public void afterInit() {
                    setPage(settingsPage);
                }
            }.controller(controller);

            ButtonWidget<?> pagedWidgetButton = new ButtonWidget<>().overlay(IKey.str(settings[settingsPage]));
            pagedWidgetButton.onMousePressed(mouseButton -> {
                settingsPage = ++settingsPage % settings.length;
                controller.setPage(settingsPage);
                pagedWidgetButton.overlay(IKey.str(settings[settingsPage]));
                return true;
            });

            ButtonWidget<?> notationButton = new ButtonWidget<>()
                .overlay(IKey.str("Toggle Notation: " + gui.formatTypes[PowerGogglesConfigHandler.formatIndex]));
            notationButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.formatIndex = (PowerGogglesConfigHandler.formatIndex + 1)
                    % gui.formatTypes.length;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Format Index")
                    .set(PowerGogglesConfigHandler.formatIndex);
                PowerGogglesConfigHandler.config.save();
                notationButton
                    .overlay(IKey.str("Toggle Notation: " + gui.formatTypes[PowerGogglesConfigHandler.formatIndex]));
                return true;
            });

            ButtonWidget<?> readingButton = new ButtonWidget<>()
                .overlay(IKey.str("Toggle Reading Type: " + gui.readingTypes[PowerGogglesConfigHandler.readingIndex]));
            readingButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.readingIndex = (PowerGogglesConfigHandler.readingIndex + 1)
                    % gui.formatTypes.length;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Reading Index")
                    .set(PowerGogglesConfigHandler.readingIndex);
                PowerGogglesConfigHandler.config.save();
                readingButton.overlay(
                    IKey.str("Toggle Reading Type: " + gui.readingTypes[PowerGogglesConfigHandler.readingIndex]));
                return true;
            });

            ButtonWidget<?> chatHidesHudButton = new ButtonWidget<>().overlay(
                IKey.str(
                    "Hide HUD with chat open: "
                        + (PowerGogglesConfigHandler.hideWhenChatOpen ? IKey.lang("gui.yes") : IKey.lang("gui.no"))));
            chatHidesHudButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.hideWhenChatOpen = !PowerGogglesConfigHandler.hideWhenChatOpen;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Hide HUD")
                    .set(PowerGogglesConfigHandler.hideWhenChatOpen);
                PowerGogglesConfigHandler.config.save();
                chatHidesHudButton.overlay(
                    IKey.str(
                        "Hide HUD with chat open: " + (PowerGogglesConfigHandler.hideWhenChatOpen ? IKey.lang("gui.yes")
                            : IKey.lang("gui.no"))));
                return true;
            });

            pagedWidget.addPage(
                new Column().sizeRel(1f)
                    .child(new Row().sizeRel(1, 0.15f)

                    )
                    .child(
                        notationButton.sizeRel(0.8f, 0.15f)
                            .posRel(0.5f, 0f))
                    .child(
                        readingButton.sizeRel(0.8f, 0.15f)
                            .posRel(0.5f, 0.2f))
                    .child(
                        chatHidesHudButton.sizeRel(0.8f, 0.15f)
                            .posRel(0.5f, 0.4f)));

            ButtonWidget<?> mainTextScaleUpButton = new ButtonWidget<>().overlay(IKey.str("Storage Text Scale-"));
            mainTextScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.mainTextScaling -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(PowerGogglesConfigHandler.mainTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> mainTextScaleDownButton = new ButtonWidget<>().overlay(IKey.str("Storage Text Scale+"));
            mainTextScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.mainTextScaling += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(PowerGogglesConfigHandler.mainTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            ButtonWidget<?> subTextScaleUpButton = new ButtonWidget<>().overlay(IKey.str("Sub Text Scale-"));
            subTextScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.subTextScaling -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(PowerGogglesConfigHandler.subTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> subTextScaleDownButton = new ButtonWidget<>().overlay(IKey.str("Sub Text Scale+"));
            subTextScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.subTextScaling += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(PowerGogglesConfigHandler.subTextScaling);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            ButtonWidget<?> hudScaleUpButton = new ButtonWidget<>().overlay(IKey.str("HUD Scale-"));
            hudScaleUpButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.hudScale -= 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(PowerGogglesConfigHandler.hudScale);
                PowerGogglesConfigHandler.config.save();
                return true;
            });
            ButtonWidget<?> hudScaleDownButton = new ButtonWidget<>().overlay(IKey.str("HUD Scale+"));
            hudScaleDownButton.onMousePressed(mouseButton -> {
                PowerGogglesConfigHandler.hudScale += 0.1;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(PowerGogglesConfigHandler.hudScale);
                PowerGogglesConfigHandler.config.save();
                return true;
            });

            pagedWidget.addPage(
                new Column().sizeRel(1f)
                    .child(
                        new Row().sizeRel(1, 0.2f)
                            .child(
                                mainTextScaleUpButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterLeft))
                            .child(
                                mainTextScaleDownButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterRight)))
                    .child(
                        new Row().sizeRel(1, 0.2f)
                            .child(
                                subTextScaleUpButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterLeft))
                            .child(
                                subTextScaleDownButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterRight)))
                    .child(
                        new Row().sizeRel(1, 0.2f)
                            .child(
                                hudScaleUpButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterLeft))
                            .child(
                                hudScaleDownButton.sizeRel(0.45f, 0.75f)
                                    .align(Alignment.CenterRight))));

            pagedWidget.addPage(
                new Column().sizeRel(1)
                    .child(createBadRgbConfig())
                    .child(createOkRgbConfig())
                    .child(createGoodRgbConfig())

            );
            pagedWidget.addPage(
                new Column().sizeRel(1)
                    .child(createBadTextConfig())
                    .child(createOkTextConfig())
                    .child(createGoodTextConfig()));

            return new CustomModularScreen() {

                @Override
                public @NotNull ModularPanel buildUI(ModularGuiContext context) {
                    return ModularPanel.defaultPanel("power_goggles_overlay", gui.displayWidth, gui.displayHeight)
                        .background(IDrawable.EMPTY)
                        .sizeRel(0.4f, 0.75f)
                        .posRel(0.5f, 0.25f)
                        .child(
                            new Column().sizeRel(1)
                                .child(
                                    new SingleChildWidget<>().sizeRel(1, 0.2f)
                                        .child(pagedWidgetButton.sizeRel(1, 0.5f)))
                                .child(
                                    new SingleChildWidget<>().sizeRel(1, 0.8f)
                                        .child(pagedWidget.sizeRel(1))));
                }
            };
        }));
        // Render an overlay in the main menu to prevent a ~1s delay when opening the config gui for the first time
        // This is a known bug with low priority
        OverlayManager.register(
            new OverlayHandler(
                screen -> screen instanceof GuiMainMenu,
                screen -> new ModularScreen(
                    ModularPanel.defaultPanel("overlay")
                        .size(0)
                        .pos(-100, 0))));
    }

    private static IWidget createBadRgbConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("Bad Gradient RGB")
                    .asWidget()
                    .tooltip(
                        t -> t.add(
                            "Affects what color the power rectangle's gradient approaches as EU change goes lower"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.gradientBadColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.gradientBadColor = Color
                                            .withRed(PowerGogglesConfigHandler.gradientBadColor, Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Gradient Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.gradientBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.gradientBadColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.gradientBadColor = Color.withGreen(
                                            PowerGogglesConfigHandler.gradientBadColor,
                                            Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Gradient Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.gradientBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.gradientBadColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.gradientBadColor = Color.withBlue(
                                            PowerGogglesConfigHandler.gradientBadColor,
                                            Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Gradient Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.gradientBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                    })))));
    }

    private static IWidget createOkRgbConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("OK Gradient RGB")
                    .asWidget()
                    .tooltip(
                        t -> t.add(
                            "Affects what color the power rectangle's gradient approaches as EU change approaches 0"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.gradientOkColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.gradientOkColor = Color
                                            .withRed(PowerGogglesConfigHandler.gradientOkColor, Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Gradient Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.gradientOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.gradientOkColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.gradientOkColor = Color.withGreen(
                                            PowerGogglesConfigHandler.gradientOkColor,
                                            Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Gradient Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.gradientOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.gradientOkColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.gradientOkColor = Color.withBlue(
                                            PowerGogglesConfigHandler.gradientOkColor,
                                            Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Gradient Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.gradientOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                    })))));
    }

    private static IWidget createGoodRgbConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("Good Gradient RGB")
                    .asWidget()
                    .tooltip(
                        t -> t.add(
                            "Affects what color the power rectangle's gradient approaches as EU change goes higher"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.gradientGoodColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.gradientGoodColor = Color.withRed(
                                            PowerGogglesConfigHandler.gradientGoodColor,
                                            Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Gradient Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.gradientGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.gradientGoodColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.gradientGoodColor = Color.withGreen(
                                            PowerGogglesConfigHandler.gradientGoodColor,
                                            Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Gradient Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.gradientGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.gradientGoodColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.gradientGoodColor = Color.withBlue(
                                            PowerGogglesConfigHandler.gradientGoodColor,
                                            Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Gradient Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.gradientGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                    })))));
    }

    private static IWidget createBadTextConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("Bad Text Color RGB")
                    .asWidget()
                    .tooltip(t -> t.add("Affects Text color when EU change is negative"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.textBadColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.textBadColor = Color
                                            .withRed(PowerGogglesConfigHandler.textBadColor, Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Text Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.textBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();
                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.textBadColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.textBadColor = Color
                                            .withGreen(PowerGogglesConfigHandler.textBadColor, Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Text Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.textBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.textBadColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.textBadColor = Color
                                            .withBlue(PowerGogglesConfigHandler.textBadColor, Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Bad Text Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.textBadColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    })))));
    }

    private static IWidget createOkTextConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("OK Text RGB")
                    .asWidget()
                    .tooltip(t -> t.add("Affects Text color when EU change is 0"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.textOkColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.textOkColor = Color
                                            .withRed(PowerGogglesConfigHandler.textOkColor, Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Text Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.textOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.textOkColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.textOkColor = Color
                                            .withGreen(PowerGogglesConfigHandler.textOkColor, Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Color Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.textOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.textOkColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.textOkColor = Color
                                            .withBlue(PowerGogglesConfigHandler.textOkColor, Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("OK Color Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.textOkColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    })))));
    }

    private static IWidget createGoodTextConfig() {
        return new Column().sizeRel(1, 0.3f)
            .child(
                IKey.str("Good Text RGB")
                    .asWidget()
                    .tooltip(t -> t.add("Affects Text color when EU change is positive"))
                    .color(Color.WHITE.main)
                    .background(new Rectangle().setColor(Color.argb(11, 22, 145, (int) (255 * 0.65f))))
                    .sizeRel(1, 0.5f)
                    .alignment(Alignment.Center))
            .child(
                new Row().sizeRel(1, 0.5f)
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getRed(PowerGogglesConfigHandler.textGoodColor)),
                                    (red -> {
                                        PowerGogglesConfigHandler.textGoodColor = Color
                                            .withRed(PowerGogglesConfigHandler.textGoodColor, Integer.parseInt(red));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Text Red")
                                            .set(Color.getRed(PowerGogglesConfigHandler.textGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getGreen(PowerGogglesConfigHandler.textGoodColor)),
                                    (green -> {
                                        PowerGogglesConfigHandler.textGoodColor = Color.withGreen(
                                            PowerGogglesConfigHandler.textGoodColor,
                                            Integer.parseInt(green));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Text Green")
                                            .set(Color.getGreen(PowerGogglesConfigHandler.textGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    }))))
                    .child(
                        new TextFieldWidget().sizeRel(0.33f, 0.5f)
                            .setNumbers(0, 255)
                            .value(
                                new StringValue.Dynamic(
                                    () -> String.valueOf(Color.getBlue(PowerGogglesConfigHandler.textGoodColor)),
                                    (blue -> {
                                        PowerGogglesConfigHandler.textGoodColor = Color
                                            .withBlue(PowerGogglesConfigHandler.textGoodColor, Integer.parseInt(blue));
                                        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                                            .get("Good Text Blue")
                                            .set(Color.getBlue(PowerGogglesConfigHandler.textGoodColor));
                                        PowerGogglesConfigHandler.config.save();
                                        PowerGogglesHudHandler.updateColors();

                                    })))));
    }

}
