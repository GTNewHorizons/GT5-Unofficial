package gregtech.common.gui.modularui.widget;

import static org.lwjgl.opengl.GL11.GL_LINES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;
import com.gtnewhorizons.modularui.api.GlStateManager;

public class LineChartWidget extends Widget<LineChartWidget> {

    private int lineMargin = 11;
    private int lineWidth = 3;
    private String chartUnit = "";
    private int dataPointLimit = 0;

    private GenericListSyncHandler<Double> dataSyncHandler;

    public LineChartWidget lineMargin(int lineMargin) {
        this.lineMargin = lineMargin;
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

        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL_LINES);

        tessellator.setColorRGBA(30, 150, 30, 255);
        double maxValue = data.stream()
            .reduce(Double::max)
            .orElse(1.0);
        double minValue = data.stream()
            .reduce(Double::min)
            .orElse(0.0);
        // Can't exactly have x to x chart and have the line at the top
        if (maxValue == minValue) {
            minValue = 0;
        }

        int startX = 2;
        double lineWidth = (double) (getArea().width - startX) / data.size();
        double lastX = startX;
        double lastY = getPointY(data.get(0), minValue, maxValue);

        for (int i = 1; i < data.size(); i++) {
            tessellator.addVertex(lastX, lastY, 0);

            double currentX = lastX + lineWidth;
            double currentY = getPointY(data.get(i), minValue, maxValue);
            tessellator.addVertex(currentX, currentY, 0);

            lastX = currentX;
            lastY = currentY;
        }

        tessellator.draw();
        TextRenderer renderer = new TextRenderer();
        renderer.setColor(Color.WHITE.main);

        renderer.setPos(0, 0);
        renderer.draw(maxValue + chartUnit);

        renderer.setPos(0, (int) (getArea().height - renderer.getFontHeight()));
        renderer.draw(minValue + chartUnit);

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();

    }

    private double getPointY(double data, double minValue, double maxValue) {
        if (maxValue == 0) {
            return getArea().height - lineMargin;
        }
        double chartHeight = getArea().height - lineMargin * 2;
        return chartHeight * (1 - (data - minValue) / (maxValue - minValue)) + lineMargin;
    }
}
