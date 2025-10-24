package gregtech.common.gui.modularui.widget;

import static org.lwjgl.opengl.GL11.GL_LINES;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;
import com.gtnewhorizons.modularui.api.GlStateManager;

public class ChartWidget extends Widget<ChartWidget> {

    private GenericListSyncHandler<Double> dataSyncHandler;

    @Override
    public boolean isValidSyncHandler(SyncHandler syncHandler) {
        return syncHandler instanceof GenericListSyncHandler<?>;
    }

    public ChartWidget syncHandler(GenericListSyncHandler<Double> dataSyncHandler) {
        this.dataSyncHandler = dataSyncHandler;
        setSyncHandler(dataSyncHandler);
        return this;
    }

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        if (dataSyncHandler == null) {
            return;
        }
        List<Double> data = dataSyncHandler.getValue();
        if (data.isEmpty()) {
            return;
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
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL_LINES);

        tessellator.setColorRGBA(30, 150, 30, 255);
        double maxValue = getMaxValue(data);

        double lineWidth = (double) getArea().width / data.size();
        double lastX = 0;
        double lastY = getPointY(data.get(0), maxValue);

        for (int i = 1; i < data.size(); i++) {
            tessellator.addVertex(lastX, lastY, 0);

            double currentX = lastX + lineWidth;
            double currentY = getPointY(data.get(i), maxValue);
            tessellator.addVertex(currentX, currentY, 0);

            lastX = currentX;
            lastY = currentY;
        }

        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();

    }

    private double getPointY(double data, double maxValue) {
        if (maxValue == 0) {
            return getArea().height;
        }
        return getArea().height * (1 - data / maxValue);
    }

    private double getMaxValue(List<Double> data) {
        double max = data.get(0);
        for (double value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
