package gregtech.common.gui.modularui.widget;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;
import com.gtnewhorizons.angelica.glsm.GLStateManager;
import com.gtnewhorizons.modularui.api.GlStateManager;

public class LineChartWidget extends Widget<LineChartWidget> {

    private int lineMarginLeft = 2;
    private int lineMarginTop = 11;
    private int lineMarginRight = 0;
    private int lineMarginBottom = 11;

    private int lineWidth = 3;
    private String chartUnit = "";
    private int dataPointLimit = 0;

    private boolean renderMinMaxText = true;

    private boolean renderTextureWithAlpha = false;
    private float alpha = 1f;

    private boolean lowerBoundAlwaysZero = false;

    private GenericListSyncHandler<Double> dataSyncHandler;

    public LineChartWidget lineMarginLeft(int lineMarginLeft) {
        this.lineMarginLeft = lineMarginLeft;
        return this;
    }

    public LineChartWidget lineMarginTop(int lineMarginTop) {
        this.lineMarginTop = lineMarginTop;
        return this;
    }

    public LineChartWidget lineMarginRight(int lineMarginRight) {
        this.lineMarginRight = lineMarginRight;
        return this;
    }

    public LineChartWidget lineMarginBottom(int lineMarginBottom) {
        this.lineMarginBottom = lineMarginBottom;
        return this;
    }

    public LineChartWidget lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public LineChartWidget chartUnit(String chartUnit) {
        this.chartUnit = chartUnit;
        return this;
    }

    public LineChartWidget dataPointLimit(int dataPointLimit) {
        this.dataPointLimit = dataPointLimit;
        return this;
    }

    public LineChartWidget renderMinMaxText(boolean renderMinMaxText) {
        this.renderMinMaxText = renderMinMaxText;
        return this;
    }

    public LineChartWidget renderTextureWithAlpha() {
        this.renderTextureWithAlpha = true;
        return this;
    }

    public LineChartWidget renderTextureWithAlpha(boolean renderTextureWithAlpha) {
        this.renderTextureWithAlpha = renderTextureWithAlpha;
        return this;
    }

    public LineChartWidget alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public LineChartWidget lowerBoundAlwaysZero() {
        this.renderTextureWithAlpha = true;
        return this;
    }

    public LineChartWidget lowerBoundAlwaysZero(boolean lowerBoundAlwaysZero) {
        this.lowerBoundAlwaysZero = lowerBoundAlwaysZero;
        return this;
    }

    @Override
    public boolean isValidSyncHandler(SyncHandler syncHandler) {
        return syncHandler instanceof GenericListSyncHandler<?>;
    }

    public LineChartWidget syncHandler(GenericListSyncHandler<Double> dataSyncHandler) {
        this.dataSyncHandler = dataSyncHandler;
        setSyncHandler(dataSyncHandler);
        return this;
    }

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        if (dataSyncHandler == null) {
            return;
        }
        List<Double> data = new ArrayList<>(dataSyncHandler.getValue());
        if (data.isEmpty()) {
            return;
        }

        // Obtain last dataPointLimit entries if specified
        if (dataPointLimit > 0) {
            Collections.reverse(data);
            data = data.stream()
                .limit(dataPointLimit)
                .collect(Collectors.toList());
            Collections.reverse(data);
        }

        double maxValue = data.stream()
            .reduce(Double::max)
            .orElse(1.0);
        double minValue;
        if (lowerBoundAlwaysZero) {
            minValue = 0;
        } else {
            minValue = data.stream()
                .reduce(Double::min)
                .orElse(0.0);
        }

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.glLineWidth(lineWidth);

        drawChartLines(data, widgetTheme, minValue, maxValue);

        if (renderMinMaxText) {
            drawChartText(widgetTheme, minValue, maxValue);
        }

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();

    }

    private void drawChartLines(List<Double> data, WidgetThemeEntry<?> widgetTheme, double minValue, double maxValue) {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL_LINE_STRIP);

        WidgetTheme theme = widgetTheme.getTheme();
        int lineColor = theme.getColor();
        tessellator.setColorRGBA(
            Color.getRed(lineColor),
            Color.getGreen(lineColor),
            Color.getBlue(lineColor),
            Color.getAlpha(lineColor));

        // Can't exactly have x to x chart and have the line at the top
        if (maxValue == minValue) {
            minValue = 0;
        }

        double lineWidth = (double) (getArea().width - (lineMarginLeft + lineMarginRight)) / data.size();
        // First vertex drawn twice (once here and once in the loop) so it's, well, a line
        double x = lineMarginLeft;
        double y = getPointY(data.get(0), minValue, maxValue);
        tessellator.addVertex(x, y, 0);

        for (int i = 0; i < data.size(); i++) {

            x = lineMarginLeft + lineWidth * (i + 1);
            y = getPointY(data.get(i), minValue, maxValue);
            tessellator.addVertex(x, y, 0);
        }

        tessellator.draw();
    }

    private void drawChartText(WidgetThemeEntry<?> widgetTheme, double minValue, double maxValue) {
        TextRenderer renderer = TextRenderer.SHARED;
        renderer.setAlignment(Alignment.CenterLeft, getArea().width);
        renderer.setColor(
            widgetTheme.getTheme()
                .getTextColor());
        renderer.setScale(1.0f);
        renderer.setShadow(true);
        renderer.setSimulate(false);

        renderer.setPos(0, 0);
        renderer.draw(maxValue + chartUnit);

        renderer.setPos(0, (int) (getArea().height - renderer.getFontHeight()));
        renderer.draw(minValue + chartUnit);
    }

    // Normally UITextures are rendered without blend which eliminates opacity
    @Override
    public void drawBackground(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        if (!renderTextureWithAlpha) {
            super.drawBackground(context, widgetTheme);
            return;
        }
        IDrawable bg = getCurrentBackground(context.getTheme(), widgetTheme);
        if (bg instanceof DrawableStack stack) {
            for (IDrawable drawable : stack.getDrawables()) {
                if (drawable instanceof UITexture texture) {
                    renderTexture(texture);
                } else {
                    drawable.drawAtZero(context, getArea(), widgetTheme.getTheme());
                }
            }
            return;
        }

        if (bg instanceof UITexture texture) {
            renderTexture(texture);
            return;
        }

        super.drawBackground(context, widgetTheme);
    }

    private void renderTexture(UITexture texture) {
        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GLStateManager.enableBlend();
        GLStateManager.glColor4f(1, 1, 1, alpha);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture.location);
        GuiDraw.drawTexture(0, 0, getArea().width, getArea().height, texture.u0, texture.v0, texture.u1, texture.v1, 0);

        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GLStateManager.disableBlend();
        GLStateManager.glColor4f(1, 1, 1, 1f);
    }

    private double getPointY(double data, double minValue, double maxValue) {
        if (maxValue == 0) {
            return getArea().height - (lineMarginTop);
        }
        double chartHeight = getArea().height - (lineMarginTop + lineMarginBottom);
        return chartHeight * (1 - (data - minValue) / (maxValue - minValue)) + lineMarginBottom;
    }
}
