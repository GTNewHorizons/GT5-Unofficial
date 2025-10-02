package gregtech.client.renderer.waila;

import java.awt.Dimension;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.StatCollector;

import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaVariableWidthTooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.OverlayConfig;

public class TTRenderGTProgressBar implements IWailaVariableWidthTooltipRenderer {

    int maxStringW;

    @Override
    public Dimension getSize(String[] params, IWailaCommonAccessor accessor) {
        return new Dimension(0, 12);
    }

    @Override
    public void draw(String[] params, IWailaCommonAccessor accessor) {
        drawThickBeveledBox(0, 0, maxStringW, 12, 1, 0xFF505050, 0xFF505050, -1);
        int progresstime = Integer.parseInt(params[0]);
        int maxProgresstime = Integer.parseInt(params[1]);
        int progress = (int) (maxStringW * ((double) progresstime / maxProgresstime));
        for (int xx = 1; xx < progress; xx++) {
            int color = (xx & 1) == 0 ? 0xFFDD0000 : 0xFF900000;
            drawVerticalLine(xx, 1, 12 - 1, color);
        }
        DisplayUtil.drawString(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.machine.in_progress",
                (double) progresstime / 20,
                (double) maxProgresstime / 20,
                (Math.round((double) progresstime / maxProgresstime * 1000) / 10.0)),
            2,
            2,
            OverlayConfig.fontcolor,
            true);
    }

    public static void drawThickBeveledBox(int x1, int y1, int x2, int y2, int thickness, int topleftcolor,
        int botrightcolor, int fillcolor) {
        if (fillcolor != -1) {
            Gui.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillcolor);
        }
        Gui.drawRect(x1, y1, x2 - 1, y1 + thickness, topleftcolor);
        Gui.drawRect(x1, y1, x1 + thickness, y2 - 1, topleftcolor);
        Gui.drawRect(x2 - thickness, y1, x2, y2 - 1, botrightcolor);
        Gui.drawRect(x1, y2 - thickness, x2, y2, botrightcolor);
    }

    public static void drawVerticalLine(int x1, int y1, int y2, int color) {
        Gui.drawRect(x1, y1, x1 + 1, y2, color);
    }

    @Override
    public void setMaxStringW(int width) {
        maxStringW = width + 2;
    }

    @Override
    public int getMaxStringW() {
        return maxStringW;
    }
}
