package gregtech.common.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.common.handlers.PowerGogglesConfigHandler;

/**
 * Created by brandon3055 on 11/2/2016.
 */
public class PowerGogglesGuiHudConfig extends GuiScreen {

    private GuiScreen parentScreen;
    private boolean draggingHud = false;
    private GuiButton notationToggleButton;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public PowerGogglesGuiHudConfig() {}

    public PowerGogglesGuiHudConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        super.initGui();
        int buttonWidth = fontRenderer.getStringWidth("Toggle Notation: ENGINEERING") + 5;

        int x = width / 2;
        int y = height / 2;
        buttonList.clear();
        notationToggleButton = new GuiButton(
            0,
            x - buttonWidth / 2,
            y - fontRenderer.FONT_HEIGHT - 5,
            "Toggle Notation: " + PowerGogglesConfigHandler.numberFormatting);
        notationToggleButton.width = buttonWidth;
        buttonList.add(notationToggleButton);

    }

    @Override
    public void updateScreen() {
        notationToggleButton.displayString = "Toggle Notation: " + PowerGogglesConfigHandler.numberFormatting;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            String current = PowerGogglesConfigHandler.numberFormatting;
            String next = current.equals("SCIENTIFIC") ? "ENGINEERING"
                : current.equals("ENGINEERING") ? "SI" : "SCIENTIFIC";
            PowerGogglesConfigHandler.numberFormatting = next;
            PowerGogglesConfigHandler.config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Number Formatting",
                    "SCIENTIFIC",
                    "Available options: SI, SCIENTIFIC, ENGINEERING")
                .set(next);
            PowerGogglesConfigHandler.config.save();
        }
    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        GL11.glPushMatrix();
        GL11.glTranslated(width / 2f, height / 2f, 0);
        GL11.glScaled(0.8, 0.8, 1);
        GL11.glTranslated(-width / 2f, -height / 2f, 0);
        super.drawScreen(x, y, partial);
        GL11.glPopMatrix();

        int dragCenterX = PowerGogglesConfigHandler.mainOffsetX;
        int dragCenterY = height - PowerGogglesConfigHandler.mainOffsetY - PowerGogglesConfigHandler.rectangleHeight;
        int dragWidth = 20;
        int dragHeight = 20;
        drawRect(
            dragCenterX - dragWidth / 2,
            dragCenterY - dragHeight / 2,
            dragCenterX + dragWidth / 2,
            dragCenterY + dragHeight / 2,
            Color.argb(255, 255, 255, 255 * 0.85f));

        // GuiHelper.drawGradientRect(300, 150, height-100, 250, height-20, Color.rgb(255,255,255),
        // Color.rgb(255,255,0));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (isOnDragRectangle(x, y)) {
            draggingHud = true;
            dragOffsetX = x - PowerGogglesConfigHandler.mainOffsetX;
            dragOffsetY = height - PowerGogglesConfigHandler.mainOffsetY
                - PowerGogglesConfigHandler.rectangleHeight
                - y;
        }

        super.mouseClicked(x, y, button);
    }

    private boolean isOnDragRectangle(int x, int y) {
        int dragCenterX = PowerGogglesConfigHandler.mainOffsetX;
        int dragCenterY = height - PowerGogglesConfigHandler.mainOffsetY - PowerGogglesConfigHandler.rectangleHeight;
        int dragWidth = 20;
        int dragHeight = 20;
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

            draggingHud = false;
        }
        super.mouseMovedOrUp(x, y, action);

    }

    @Override
    protected void mouseClickMove(int x, int y, int action, long time) {
        if (draggingHud) {
            PowerGogglesConfigHandler.mainOffsetX = x - dragOffsetX;
            PowerGogglesConfigHandler.mainOffsetY = height - y - dragOffsetY;
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
            return;
        }
    }
}
