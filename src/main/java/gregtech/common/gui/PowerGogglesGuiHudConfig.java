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

    private String[] formatTypes = new String[] { "SCIENTIFIC", "ENGINEERING", "SI" };
    private String[] readingTypes = new String[] { "BOTH", "TOTAL", "EU/T" };
    private String[] gradientTypes = new String[] { "NORMAL", "DEUTERANOPIA" };
    private GuiScreen parentScreen;
    private boolean draggingHud = false;

    private GuiButton notationToggleButton;
    private GuiButton readingToggleButton;
    private GuiButton gradientToggleButton;
    private GuiButton showHudOnChatButton;

    private GuiButton mainScaleUpButton;
    private GuiButton mainScaleDownButton;
    private GuiButton subScaleUpButton;
    private GuiButton subScaleDownButton;
    private GuiButton hudScaleUpButton;
    private GuiButton hudScaleDownButton;

    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int dragCenterX;
    private int dragCenterY;
    private int dragWidth;
    private int dragHeight;

    public PowerGogglesGuiHudConfig() {}

    public PowerGogglesGuiHudConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        super.initGui();
        int buttonWidth = fontRenderer.getStringWidth("Main Text Scale+") * 2 + 10;

        int x = width / 2;
        int y = height / 4;
        buttonList.clear();
        notationToggleButton = new GuiButton(
            0,
            x - buttonWidth / 2,
            y,
            "Toggle Notation: " + formatTypes[PowerGogglesConfigHandler.formatIndex]);
        notationToggleButton.width = buttonWidth;

        readingToggleButton = new GuiButton(
            1,
            x - buttonWidth / 2,
            notationToggleButton.yPosition + notationToggleButton.height,
            "Toggle Reading Type: " + readingTypes[PowerGogglesConfigHandler.readingIndex]);
        readingToggleButton.width = buttonWidth;

        showHudOnChatButton = new GuiButton(
            2,
            x - buttonWidth / 2,
            readingToggleButton.yPosition + readingToggleButton.height,
            "HUD with chat open: " + !PowerGogglesConfigHandler.hideWhenChatOpen);
        showHudOnChatButton.width = buttonWidth;

        gradientToggleButton = new GuiButton(
            3,
            x - buttonWidth / 2,
            showHudOnChatButton.yPosition + showHudOnChatButton.height,
            "Toggle Gradient: " + gradientTypes[PowerGogglesConfigHandler.gradientIndex]);
        gradientToggleButton.width = buttonWidth;

        mainScaleDownButton = new GuiButton(
            4,
            x - buttonWidth / 2,
            gradientToggleButton.yPosition + gradientToggleButton.height,
            "Main Text Scale-");
        mainScaleDownButton.width = buttonWidth / 2;

        mainScaleUpButton = new GuiButton(
            5,
            x,
            gradientToggleButton.yPosition + gradientToggleButton.height,
            "Main Text Scale+");
        mainScaleUpButton.width = buttonWidth / 2;

        subScaleDownButton = new GuiButton(
            6,
            x - buttonWidth / 2,
            mainScaleDownButton.yPosition + mainScaleDownButton.height,
            "Sub Text Scale-");
        subScaleDownButton.width = buttonWidth / 2;
        subScaleUpButton = new GuiButton(
            7,
            x,
            mainScaleUpButton.yPosition + mainScaleUpButton.height,
            "Sub Text Scale+");
        subScaleUpButton.width = buttonWidth / 2;

        hudScaleDownButton = new GuiButton(
            8,
            x - buttonWidth / 2,
            subScaleDownButton.yPosition + subScaleDownButton.height,
            "HUD Scale-");
        hudScaleDownButton.width = buttonWidth / 2;
        hudScaleUpButton = new GuiButton(9, x, subScaleUpButton.yPosition + subScaleUpButton.height, "HUD Scale+");
        hudScaleUpButton.width = buttonWidth / 2;

        buttonList.add(notationToggleButton);
        buttonList.add(readingToggleButton);
        buttonList.add(showHudOnChatButton);
        buttonList.add(gradientToggleButton);
        buttonList.add(mainScaleDownButton);
        buttonList.add(mainScaleUpButton);
        buttonList.add(subScaleDownButton);
        buttonList.add(subScaleUpButton);
        buttonList.add(hudScaleDownButton);
        buttonList.add(hudScaleUpButton);

    }

    @Override
    public void updateScreen() {
        notationToggleButton.displayString = "Toggle Notation: " + formatTypes[PowerGogglesConfigHandler.formatIndex];
        readingToggleButton.displayString = "Toggle Reading Type: "
            + readingTypes[PowerGogglesConfigHandler.readingIndex];
        showHudOnChatButton.displayString = "HUD with chat open: " + !PowerGogglesConfigHandler.hideWhenChatOpen;
        gradientToggleButton.displayString = "Toggle Gradient: "
            + gradientTypes[PowerGogglesConfigHandler.gradientIndex];
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        double scale;
        switch (button.id) {
            case 0:
                PowerGogglesConfigHandler.formatIndex = (PowerGogglesConfigHandler.formatIndex + 1)
                    % formatTypes.length;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Format Index")
                    .set(PowerGogglesConfigHandler.formatIndex);
                break;
            case 1:
                PowerGogglesConfigHandler.readingIndex = (PowerGogglesConfigHandler.readingIndex + 1)
                    % readingTypes.length;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Reading Index")
                    .set(PowerGogglesConfigHandler.readingIndex);
                break;
            case 2:
                PowerGogglesConfigHandler.hideWhenChatOpen = !PowerGogglesConfigHandler.hideWhenChatOpen;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Hide HUD")
                    .set(PowerGogglesConfigHandler.hideWhenChatOpen);
                break;
            case 3:
                PowerGogglesConfigHandler.gradientIndex = (PowerGogglesConfigHandler.gradientIndex + 1)
                    % gradientTypes.length;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Gradient Index")
                    .set(PowerGogglesConfigHandler.gradientIndex);
                break;
            case 4:
                scale = PowerGogglesConfigHandler.mainTextScaling - 0.1;
                PowerGogglesConfigHandler.mainTextScaling = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(scale);
                break;
            case 5:
                scale = PowerGogglesConfigHandler.mainTextScaling + 0.1;
                PowerGogglesConfigHandler.mainTextScaling = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Storage Text Scale")
                    .set(scale);
                break;
            case 6:
                scale = PowerGogglesConfigHandler.subTextScaling - 0.1;
                PowerGogglesConfigHandler.subTextScaling = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(scale);
                break;
            case 7:
                scale = PowerGogglesConfigHandler.subTextScaling + 0.1;
                PowerGogglesConfigHandler.subTextScaling = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("Timed Reading Text Scale")
                    .set(scale);
                break;
            case 8:
                scale = PowerGogglesConfigHandler.hudScale - 0.1;
                PowerGogglesConfigHandler.hudScale = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(scale);
                break;
            case 9:
                scale = PowerGogglesConfigHandler.hudScale + 0.1;
                PowerGogglesConfigHandler.hudScale = scale;
                PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                    .get("HUD Scale")
                    .set(scale);
                break;
        }
        PowerGogglesConfigHandler.config.save();

    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        GL11.glPushMatrix();
        super.drawScreen(x, y, partial);
        GL11.glPopMatrix();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int gapBetweenLines = 2;
        int borderRadius = 3;
        int rectangleHeightToBorderBottom = PowerGogglesConfigHandler.rectangleHeight + gapBetweenLines * 2
            + (int) (fontRenderer.FONT_HEIGHT * 2 * PowerGogglesConfigHandler.subTextScaling)
            + borderRadius;
        dragCenterX = (int) ((PowerGogglesConfigHandler.mainOffsetX
            + PowerGogglesConfigHandler.rectangleWidth * PowerGogglesConfigHandler.hudScale
            - 1));
        dragCenterY = (int) ((height - PowerGogglesConfigHandler.mainOffsetY
            - PowerGogglesConfigHandler.rectangleHeight)
            + (rectangleHeightToBorderBottom) * (1 - PowerGogglesConfigHandler.hudScale));
        dragWidth = (int) (10 * PowerGogglesConfigHandler.hudScale);
        dragHeight = (int) (10 * PowerGogglesConfigHandler.hudScale);

        drawRect(
            dragCenterX - dragWidth / 2,
            dragCenterY - dragHeight / 2,
            dragCenterX + dragWidth / 2,
            dragCenterY + dragHeight / 2,
            Color.argb(40, 24, 163, 255 * 0.75f));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (isOnDragRectangle(x, y)) {
            draggingHud = true;
            dragOffsetX = (int) ((x - PowerGogglesConfigHandler.mainOffsetX));
            dragOffsetY = (int) ((height - PowerGogglesConfigHandler.mainOffsetY - y));
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
            PowerGogglesConfigHandler.mainOffsetX = (int) ((x - dragOffsetX));
            PowerGogglesConfigHandler.mainOffsetY = (int) ((height - y - dragOffsetY));
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
