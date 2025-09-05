package gregtech.client.hud.elements;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class ColumnElement extends WidgetElement<ColumnElement> implements Configurable {

    private final Supplier<List<Double>> dataSupplier;
    private double minValue, maxValue;
    private int cellWidth = HUDManager.UIConstants.DEFAULT_CELL_WIDTH;
    private int cellHeight = HUDManager.UIConstants.DEFAULT_CELL_HEIGHT;
    private int spacing = HUDManager.UIConstants.GRID_SPACING;

    public ColumnElement(Supplier<List<Double>> dataSupplier, double minValue, double maxValue) {
        this.dataSupplier = dataSupplier;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.width = HUDManager.UIConstants.DEFAULT_CELL_WIDTH;
        this.height = HUDManager.UIConstants.DEFAULT_CELL_HEIGHT * 10; // Default for 10 cells
        this.hoverHighlight = false;
    }

    public ColumnElement setCellSize(int cellWidth, int cellHeight) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        return this;
    }

    public ColumnElement setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.TILE_BG_R,
            HUDManager.UIConstants.TILE_BG_G,
            HUDManager.UIConstants.TILE_BG_B,
            HUDManager.UIConstants.TILE_ALPHA,
            HUDManager.UIConstants.TILE_HOVER_R,
            HUDManager.UIConstants.TILE_HOVER_G,
            HUDManager.UIConstants.TILE_HOVER_B,
            HUDManager.UIConstants.TILE_ALPHA);

        List<Double> data = dataSupplier.get();
        if (data.isEmpty()) return;

        double range = maxValue - minValue;
        if (range == 0) range = 1.0;

        int left = absLeft(baseX);
        int bottom = absBottom(baseY);

        for (int i = 0; i < data.size() && i * (cellHeight + spacing) < height; i++) {
            double value = data.get(i);
            double normalized = (value - minValue) / range;
            int barHeight = (int) (normalized * cellHeight);
            int y = bottom - (i + 1) * (cellHeight + spacing);

            HUDManager.HUDUtils.drawHudRect(
                left,
                y,
                left + cellWidth,
                y + barHeight,
                HUDManager.UIConstants.PROGRESS_FILL_R,
                HUDManager.UIConstants.PROGRESS_FILL_G,
                HUDManager.UIConstants.PROGRESS_FILL_B,
                HUDManager.UIConstants.PROGRESS_FILL_ALPHA);
        }
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Cell Width:", this.cellWidth, val -> { this.cellWidth = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Cell Height:", this.cellHeight, val -> { this.cellHeight = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Spacing:", this.spacing, val -> { this.spacing = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createDoubleConfig(gui, "Min Value:", this.minValue, val -> { this.minValue = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createDoubleConfig(gui, "Max Value:", this.maxValue, val -> { this.maxValue = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", this.offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", this.offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }

    @Override
    public int getConfigSpacingCount() {
        return 8;
    }
}
