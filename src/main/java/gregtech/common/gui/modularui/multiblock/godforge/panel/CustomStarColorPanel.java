package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.HueBar;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.FloatValue;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.ColorData;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.StarColors;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;

public class CustomStarColorPanel {

    private static final int SIZE = 200;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.CUSTOM_STAR_COLOR);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Title
        panel.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal("fog.cosmetics.starcolor"))
                .alignment(Alignment.CENTER)
                .asWidget()
                .align(Alignment.TopCenter)
                .marginTop(9)); // todo check

        Flow mainColumn = new Column().coverChildren()
            .marginTop(23)
            .alignX(0.5f);

        // Color rows
        ColorData colorData = new ColorData();

        PagedWidget.Controller controller = new PagedWidget.Controller();

        mainColumn.child(
            new PagedWidget<>().coverChildren()
                .expanded()
                .controller(controller)
                .addPage(createStarColorRGBPage(colorData))
                .addPage(createStarColorHSVPage(colorData)));

        // Color preview
        mainColumn.child(createColorPreviewRow(controller, colorData));

        panel.child(mainColumn);
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {

    }

    private static ForgeOfGodsStarColor getClickedStarColor(SyncHypervisor hypervisor) {
        IntSyncValue starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        return hypervisor.getData()
            .getStarColors()
            .getByIndex(starColorClickedSyncer.getIntValue());
    }

    private static Flow createStarColorRGBPage(ColorData colorData) {
        return new Column().coverChildren()
            .child(createStarColorRGBRow(StarColors.RGB.RED, colorData))
            .child(createStarColorRGBRow(StarColors.RGB.GREEN, colorData))
            .child(createStarColorRGBRow(StarColors.RGB.BLUE, colorData))
            .child(createStarColorGammaRow(colorData));
    }

    private static Flow createStarColorRGBRow(StarColors.RGB color, ColorData colorData) {
        Flow row = new Row().size(SIZE - 16, 16)
            .marginBottom(3);

        // Title
        row.child(
            IKey.str(color.getTitle())
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(32, 16));

        // Slider
        row.child(
            new SliderWidget().size(118, 8)
                .background(new DynamicDrawable(() -> {
                    int colorHex = colorData.getColor();
                    int start = 0, end = 0;
                    switch (color) {
                        case RED -> {
                            start = Color.withRed(colorHex, 0);
                            end = Color.withRed(colorHex, 0xFF);
                        }
                        case GREEN -> {
                            start = Color.withGreen(colorHex, 0);
                            end = Color.withGreen(colorHex, 0xFF);
                        }
                        case BLUE -> {
                            start = Color.withBlue(colorHex, 0);
                            end = Color.withBlue(colorHex, 0xFF);
                        }
                    }
                    return new Rectangle().setHorizontalGradient(start, end);
                }))
                .bounds(0, 255)
                .sliderTexture(new Rectangle().setColor(Color.WHITE.main))
                .sliderSize(2, 8)
                // spotless:off
                .value(new DoubleValue.Dynamic(
                    () ->
                        switch (color) {
                            case RED -> colorData.getR();
                            case GREEN -> colorData.getG();
                            case BLUE -> colorData.getB();
                    },
                    val -> {
                        switch (color) {
                            case RED -> colorData.setR((int) val);
                            case GREEN -> colorData.setG((int) val);
                            case BLUE -> colorData.setB((int) val);
                        }
                    }))
                .tooltipDynamic(t -> {
                    float value = switch (color) {
                        case RED -> colorData.getR();
                        case GREEN -> colorData.getG();
                        case BLUE -> colorData.getB();
                    };
                    t.addLine(color.getTooltip(value));
                })
                // spotless:on
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .tooltipAutoUpdate(true));

        // Text field
        row.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(0, 255)
                // spotless:off
                .value(new IntValue.Dynamic(
                    () ->
                        switch (color) {
                            case RED -> colorData.getR();
                            case GREEN -> colorData.getG();
                            case BLUE -> colorData.getB();
                        },
                    val -> {
                        switch (color) {
                            case RED -> colorData.setR(val);
                            case GREEN -> colorData.setG(val);
                            case BLUE -> colorData.setB(val);
                        }
                    }))
                // spotless:on
                .size(32, 16)
                .marginLeft(2)
                .setTextAlignment(Alignment.CENTER)
                .setTextColor(color.getHexColor())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.onlyintegers")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static Flow createStarColorHSVPage(ColorData colorData) {
        return new Column().coverChildren()
            .child(createStarColorHSVRow(StarColors.HSV.HUE, colorData))
            .child(createStarColorHSVRow(StarColors.HSV.SATURATION, colorData))
            .child(createStarColorHSVRow(StarColors.HSV.VALUE, colorData))
            .child(createStarColorGammaRow(colorData));
    }

    private static Flow createStarColorHSVRow(StarColors.HSV color, ColorData colorData) {
        int maxValue = color == StarColors.HSV.HUE ? 360 : 1;

        Flow row = new Row().size(SIZE - 16, 16)
            .marginBottom(3);

        // Title
        row.child(
            IKey.str(color.getTitle())
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(32, 16));

        // Slider
        IDrawable background = switch (color) {
            case HUE -> new HueBar(GuiAxis.X);
            case SATURATION -> new DynamicDrawable(() -> {
                int start = Color.withHSVSaturation(colorData.getColor(), 0.0f);
                int end = Color.withHSVSaturation(colorData.getColor(), 1.0f);
                return new Rectangle().setHorizontalGradient(start, end);
            });
            case VALUE -> new DynamicDrawable(() -> {
                int start = Color.withValue(colorData.getColor(), 0.0f);
                int end = Color.withValue(colorData.getColor(), 1.0f);
                return new Rectangle().setHorizontalGradient(start, end);
            });
        };

        row.child(
            new SliderWidget().size(118, 8)
                .background(background)
                .bounds(0, maxValue)
                .sliderTexture(new Rectangle().setColor(Color.WHITE.main))
                .sliderSize(2, 8)
                // spotless:off
                .value(new DoubleValue.Dynamic(
                    () ->
                        switch (color) {
                            case HUE -> colorData.getH();
                            case SATURATION -> colorData.getS();
                            case VALUE -> colorData.getV();
                        },
                    val -> {
                        switch (color) {
                            case HUE -> colorData.setH((float) val);
                            case SATURATION -> colorData.setS((float) val);
                            case VALUE -> colorData.setV((float) val);
                        }
                    }))
                .tooltipDynamic(t -> {
                    float value = switch (color) {
                        case HUE -> colorData.getH();
                        case SATURATION -> colorData.getS();
                        case VALUE -> colorData.getV();
                    };
                    t.addLine(color.getTooltip(value));
                })
                // spotless:on
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .tooltipAutoUpdate(true));

        // Text field
        row.child(
            new TextFieldWidget().setNumbersDouble(raw -> MathHelper.clamp_double(raw, 0, maxValue))
                // spotless:off
                .value(new FloatValue.Dynamic(
                    () ->
                        switch (color) {
                            case HUE -> colorData.getH();
                            case SATURATION -> colorData.getS();
                            case VALUE -> colorData.getV();
                        },
                    val -> {
                        switch (color) {
                            case HUE -> colorData.setH(val);
                            case SATURATION -> colorData.setS(val);
                            case VALUE -> colorData.setV(val);
                        }
                    }))
                // spotless:on
                .size(32, 16)
                .marginLeft(2)
                .setTextAlignment(Alignment.CENTER)
                .setTextColor(color.getHexColor())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.onlydecimals")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static Flow createStarColorGammaRow(ColorData colorData) {
        StarColors.Extra gamma = StarColors.Extra.GAMMA;
        Flow row = new Row().size(SIZE - 16, 16);

        // Title
        row.child(
            IKey.str(gamma.getTitle())
                .alignment(Alignment.CENTER)
                .asWidget()
                .align(Alignment.CenterLeft)
                .size(32, 16));

        // Slider
        // todo

        // Text field
        row.child(
            new TextFieldWidget().setNumbersDouble(raw -> MathHelper.clamp_double(raw, 0, 100))
                .value(new FloatValue.Dynamic(colorData::getGamma, colorData::setGamma))
                .size(32, 16)
                .setTextAlignment(Alignment.CENTER)
                .setTextColor(gamma.getHexColor())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.onlydecimals")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static Flow createColorPreviewRow(PagedWidget.Controller pageController, ColorData colorData) {
        Flow row = new Row().coverChildren();

        // RGB/HSV switchers
        row.child(
            new PageButton(0, pageController).size(24, 16)
                .marginRight(3)
                .overlay(IKey.lang("fog.cosmetics.color.rgb")));
        row.child(
            new PageButton(1, pageController).size(24, 16)
                .marginRight(5)
                .overlay(IKey.lang("fog.cosmetics.color.hsv")));

        // RGB text field header
        row.child(
            IKey.lang("fog.cosmetics.color.hex")
                .asWidget()
                .height(15)
                .marginRight(3));

        // RGB text field
        row.child(
            new TextFieldWidget()
                // spotless:off
            .setValidator(raw -> {
                if (!raw.startsWith("#")) {
                    if (raw.startsWith("0x") || raw.startsWith("0X")) {
                        raw = raw.substring(2);
                    }
                    return "#" + raw;
                }
                return raw;
            })
            .value(new StringValue.Dynamic(
                () -> "#" + Color.toFullHexString(colorData.getR(), colorData.getG(), colorData.getB()),
                val -> {
                    try {
                        colorData.updateFrom((int) (long) Long.decode(val));
                    } catch (NumberFormatException ignored) {}
                }))
            // spotless:on
                .size(50, 15)
                .marginRight(5));

        // Color preview
        row.child(
            new DynamicDrawable(() -> new Rectangle().setColor(colorData.getColor())).asWidget()
                .size(30, 15)
                .alignX(0.5f)
                .marginTop(4));

        return row;
    }
}
