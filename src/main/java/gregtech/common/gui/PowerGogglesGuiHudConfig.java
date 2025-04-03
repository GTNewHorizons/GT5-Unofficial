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

    private GuiButton mainScaleUpButton;
    private GuiButton mainScaleDownButton;
    private GuiButton subScaleUpButton;
    private GuiButton subScaleDownButton;

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
        int formatButtonWidth = fontRenderer.getStringWidth("Main Text Scale+") * 2 + 10;

        int x = width / 2;
        int y = (height / 2);
        buttonList.clear();
        notationToggleButton = new GuiButton(
            0,
            x - formatButtonWidth / 2,
            y - 50,
            "Toggle Notation: " + formatTypes[PowerGogglesConfigHandler.formatIndex]);
        notationToggleButton.width = formatButtonWidth;

        readingToggleButton = new GuiButton(
            1,
            x - formatButtonWidth / 2,
            notationToggleButton.yPosition + notationToggleButton.height,
            "Toggle Reading Type: " + readingTypes[PowerGogglesConfigHandler.readingIndex]);
        readingToggleButton.width = formatButtonWidth;

        gradientToggleButton = new GuiButton(
            2,
            x - formatButtonWidth / 2,
            readingToggleButton.yPosition + readingToggleButton.height,
            "Toggle Gradient: " + gradientTypes[PowerGogglesConfigHandler.gradientIndex]);
        gradientToggleButton.width = formatButtonWidth;

        mainScaleDownButton = new GuiButton(
            3,
            x - formatButtonWidth / 2,
            gradientToggleButton.yPosition + gradientToggleButton.height,
            "Main Text Scale-");
        mainScaleDownButton.width = formatButtonWidth / 2;

        mainScaleUpButton = new GuiButton(
            4,
            x,
            gradientToggleButton.yPosition + gradientToggleButton.height,
            "Main Text Scale+");
        mainScaleUpButton.width = formatButtonWidth / 2;

        subScaleDownButton = new GuiButton(
            5,
            x - formatButtonWidth / 2,
            gradientToggleButton.yPosition + gradientToggleButton.height + mainScaleDownButton.height,
            "Sub Text Scale-");
        subScaleDownButton.width = formatButtonWidth / 2;

        subScaleUpButton = new GuiButton(
            6,
            x,
            gradientToggleButton.yPosition + gradientToggleButton.height + mainScaleUpButton.height,
            "Sub Text Scale+");
        subScaleUpButton.width = formatButtonWidth / 2;

        buttonList.add(notationToggleButton);
        buttonList.add(readingToggleButton);
        buttonList.add(mainScaleDownButton);
        buttonList.add(mainScaleUpButton);
        buttonList.add(subScaleDownButton);
        buttonList.add(subScaleUpButton);
        buttonList.add(gradientToggleButton);

    }

    @Override
    public void updateScreen() {
        notationToggleButton.displayString = "Toggle Notation: " + formatTypes[PowerGogglesConfigHandler.formatIndex];
        readingToggleButton.displayString = "Toggle Reading Type: "
            + readingTypes[PowerGogglesConfigHandler.readingIndex];
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
                PowerGogglesConfigHandler.config
                    .get(
                        Configuration.CATEGORY_GENERAL,
                        "Format Index",
                        0,
                        "Available options: SI, SCIENTIFIC, ENGINEERING")
                    .set(PowerGogglesConfigHandler.formatIndex);
                PowerGogglesConfigHandler.config.save();
                return;
            case 1:
                PowerGogglesConfigHandler.readingIndex = (PowerGogglesConfigHandler.readingIndex + 1)
                    % readingTypes.length;
                PowerGogglesConfigHandler.config
                    .get(Configuration.CATEGORY_GENERAL, "Reading Index", 0, "Available options: TOTAL, EUT, BOTH")
                    .set(PowerGogglesConfigHandler.formatIndex);
                PowerGogglesConfigHandler.config.save();
                return;
            case 2:
                PowerGogglesConfigHandler.gradientIndex = (PowerGogglesConfigHandler.gradientIndex + 1)
                    % gradientTypes.length;
                PowerGogglesConfigHandler.config
                    .get(Configuration.CATEGORY_GENERAL, "Gradient Index", 0, "Available options: NORMAL, DEUTERANOPIA")
                    .set(PowerGogglesConfigHandler.gradientIndex);
                PowerGogglesConfigHandler.config.save();
                return;
            case 3:
                scale = PowerGogglesConfigHandler.mainTextScaling - 0.1;
                PowerGogglesConfigHandler.mainTextScaling = scale;
                PowerGogglesConfigHandler.config
                    .get(Configuration.CATEGORY_GENERAL, "Storage Text Scale", 1, "Text size of the storage EU reading")
                    .set(scale);
                PowerGogglesConfigHandler.config.save();
                return;
            case 4:
                scale = PowerGogglesConfigHandler.mainTextScaling + 0.1;
                PowerGogglesConfigHandler.mainTextScaling = scale;
                PowerGogglesConfigHandler.config
                    .get(Configuration.CATEGORY_GENERAL, "Storage Text Scale", 1, "Text size of the storage EU reading")
                    .set(scale);
                PowerGogglesConfigHandler.config.save();
                return;
            case 5:
                scale = PowerGogglesConfigHandler.subTextScaling - 0.1;
                PowerGogglesConfigHandler.subTextScaling = scale;
                PowerGogglesConfigHandler.config
                    .get(
                        Configuration.CATEGORY_GENERAL,
                        "Timed Reading Text Scale",
                        0.75,
                        "Text size of the 5m and 1h readings")
                    .set(scale);
                PowerGogglesConfigHandler.config.save();
                return;
            case 6:
                scale = PowerGogglesConfigHandler.subTextScaling + 0.1;
                PowerGogglesConfigHandler.subTextScaling = scale;
                PowerGogglesConfigHandler.config
                    .get(
                        Configuration.CATEGORY_GENERAL,
                        "Timed Reading Text Scale",
                        0.75,
                        "Text size of the 5m and 1h readings")
                    .set(scale);
                PowerGogglesConfigHandler.config.save();

        }
    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        GL11.glPushMatrix();
        super.drawScreen(x, y, partial);
        GL11.glPopMatrix();

        dragCenterX = PowerGogglesConfigHandler.mainOffsetX + PowerGogglesConfigHandler.rectangleWidth;
        dragCenterY = height - PowerGogglesConfigHandler.mainOffsetY - PowerGogglesConfigHandler.rectangleHeight;
        dragWidth = 10;
        dragHeight = 10;
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
            dragOffsetX = x - PowerGogglesConfigHandler.mainOffsetX;
            dragOffsetY = height - PowerGogglesConfigHandler.mainOffsetY
                - PowerGogglesConfigHandler.rectangleHeight
                - y;
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
