package gregtech.common.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by brandon3055 on 11/2/2016.
 */
public class PowerGogglesGuiHudConfig extends GuiScreen {

    private boolean draggingHud = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public PowerGogglesGuiHudConfig() {}

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();


    }

    @Override
    public void updateScreen() {

    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void drawScreen(int x, int y, float partial) {

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
    protected void mouseClickMove(int x, int y, int action, long time) {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char keyChar, int keyInt) {
        if (keyInt == 1) {
            Minecraft.getMinecraft().displayGuiScreen(null); //return to parent???
            return;
        }
    }
}
