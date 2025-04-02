package gregtech.common.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import gregtech.common.handlers.PowerGogglesConfigHandler;

/**
 * Created by brandon3055 on 11/2/2016.
 */
public class PowerGogglesGuiHudConfig extends GuiScreen {

    private boolean draggingHud = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private String[] notation = { "SCIENTIFIC", "ENGINEERING", "SI" };

    public PowerGogglesGuiHudConfig() {}

    @Override
    public void initGui() {
        super.initGui();
        int x = width / 2;
        int y = height / 2 - 30;
        buttonList.clear();
        buttonList.add(new GuiButton(0, x, y, "Toggle Notation: " + PowerGogglesConfigHandler.numberFormatting));

    }

    @Override
    public void updateScreen() {

    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        super.drawScreen(x, y, partial);
        // GuiHelper.drawGradientRect(300, 150, height-100, 250, height-20, Color.rgb(255,255,255),
        // Color.rgb(255,255,0));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int action) {
        super.mouseMovedOrUp(x, y, action);

    }

    @Override
    protected void mouseClickMove(int x, int y, int action, long time) {}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char keyChar, int keyInt) {
        if (keyInt == 1) {
            Minecraft.getMinecraft()
                .displayGuiScreen(null); // return to parent???
            return;
        }
    }
}
