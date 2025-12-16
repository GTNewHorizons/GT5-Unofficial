package gregtech.common.powergoggles.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

/**
 * Created by brandon3055 on 11/2/2016.
 */
public class PowerGogglesGuiHudConfig extends GuiScreen {

    public String[] formatTypes = new String[] { "GT5U.power_goggles_config.notation_scientific",
        "GT5U.power_goggles_config.notation_engineering", "GT5U.power_goggles_config.notation_si" };

    public String[] readingTypes = new String[] { "GT5U.power_goggles_config.reading_both",
        "GT5U.power_goggles_config.reading_total", "GT5U.power_goggles_config.reading_eut" };
    public GuiScreen parentScreen;
    private boolean draggingHud = false;

    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int dragCenterX;
    private int dragCenterY;
    private int dragWidth;
    private int dragHeight;

    public int displayWidth;
    public int displayHeight;

    public PowerGogglesGuiHudConfig() {}

    public PowerGogglesGuiHudConfig(int displayWidth, int displayHeight) {
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int gapBetweenLines = 2;
        int borderRadius = 3;
        int rectangleHeightToBorderBottom = PowerGogglesConfigHandler.rectangleHeight + gapBetweenLines * 2
            + (int) (fontRenderer.FONT_HEIGHT * 2 * PowerGogglesConfigHandler.subTextScaling)
            + borderRadius;
        dragCenterX = (int) ((PowerGogglesConfigHandler.mainOffsetX
            + (PowerGogglesConfigHandler.rectangleWidth + borderRadius) * PowerGogglesConfigHandler.hudScale
            - 1));

        dragCenterY = (int) (height - PowerGogglesConfigHandler.mainOffsetY);
        dragWidth = (int) (10 * PowerGogglesConfigHandler.hudScale);
        dragHeight = (int) (10 * PowerGogglesConfigHandler.hudScale);

        drawRect(
            dragCenterX - dragWidth / 2,
            dragCenterY - dragHeight / 2,
            dragCenterX + dragWidth / 2,
            dragCenterY + dragHeight / 2,
            Color.rgb(255, 50, 50));
        drawHorizontalLine(
            dragCenterX - dragWidth / 2,
            dragCenterX + dragWidth / 2 - 1,
            dragCenterY,
            Color.rgb(255, 255, 255));
        drawVerticalLine(
            dragCenterX,
            dragCenterY + dragHeight / 2,
            dragCenterY - dragHeight / 2,
            Color.rgb(255, 255, 255));
        super.drawScreen(x, y, partial);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (isOnDragRectangle(x, y)) {
            draggingHud = true;
            dragOffsetX = (x - PowerGogglesConfigHandler.mainOffsetX);
            dragOffsetY = (height - PowerGogglesConfigHandler.mainOffsetY - y);
        }

        super.mouseClicked(x, y, button);
    }

    private boolean isOnDragRectangle(int x, int y) {
        return x <= dragCenterX + dragWidth / 2 && x >= dragCenterX - dragWidth / 2
            && y >= dragCenterY - dragHeight
            && y <= dragCenterY + dragHeight;
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int action) {
        if (draggingHud) {
            PowerGogglesConfigHandler.config.get(Configuration.CATEGORY_GENERAL, "Render Offset X", 10, "")
                .set(PowerGogglesConfigHandler.mainOffsetX);
            PowerGogglesConfigHandler.config.get(Configuration.CATEGORY_GENERAL, "Render Offset Y", 40, "")
                .set(PowerGogglesConfigHandler.mainOffsetY);
            PowerGogglesConfigHandler.config.save();
            draggingHud = false;
        }
        super.mouseMovedOrUp(x, y, action);

    }

    @Override
    protected void mouseClickMove(int x, int y, int action, long time) {
        if (draggingHud) {
            PowerGogglesConfigHandler.mainOffsetX = (x - dragOffsetX);
            PowerGogglesConfigHandler.mainOffsetY = (height - y - dragOffsetY);
        }
        super.mouseClickMove(x, y, action, time);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char keyChar, int keyInt) {
        if (keyInt == 1) {
            Minecraft.getMinecraft()
                .displayGuiScreen(parentScreen); // return to parent???
        }
    }
}
