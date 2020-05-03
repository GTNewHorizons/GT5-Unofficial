package gregtech.api.gui;

import gregtech.api.enums.Dyes;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltipManager;
import gregtech.api.gui.widgets.GT_GuiTooltipManager.GT_IToolTipRenderer;
import gregtech.api.interfaces.IGuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public abstract class GT_GUICover extends GuiScreen implements GT_IToolTipRenderer, IGuiScreen {

    protected GT_GuiTooltipManager ttManager = new GT_GuiTooltipManager();

    protected int gui_width = 176;
    protected int gui_height = 107;
    protected int guiTop, guiLeft;
    protected boolean drawButtons = true;
    private GuiButton selectedButton;

    protected List<IGuiElement> elements = new ArrayList<>();

    public GT_GUICover() {
    }

    public GT_GUICover(int width, int height) {
        this.gui_width = width;
        this.gui_height = height;
    }

    @Override
    public void initGui() {
        guiLeft = (this.width - this.gui_width) / 2;
        guiTop = (this.height - this.gui_height) / 2;

        for (IGuiElement element : elements) {
            element.onInit();

            if (element instanceof GuiButton)
                buttonList.add(element);
        }

        onInitGui(guiLeft, guiTop, gui_width, gui_height);
    }

    protected abstract void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height);

    @Override
    public void drawScreen(int mouseX, int mouseY, float parTicks) {
        drawDefaultBackground();

        drawBackground(mouseX, mouseY, parTicks);

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        if (drawButtons) {
            RenderHelper.enableGUIStandardItemLighting();
            for (IGuiElement e : elements)
                e.draw(mouseX, mouseY, parTicks);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL11.GL_LIGHTING);
        drawForegroundLayer(mouseX, mouseY, parTicks);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    public void drawForegroundLayer(int mouseX, int mouseY, float parTicks) {
        drawExtras(mouseX, mouseY, parTicks);
        ttManager.onTick(this, mouseX, mouseY);
    }

    public void drawBackground(int mouseX, int mouseY, float parTicks) {
        short[] color = Dyes.MACHINE_METAL.getRGBA();
        GL11.glColor3ub((byte) color[0], (byte) color[1], (byte) color[2]);
        this.mc.renderEngine.bindTexture(new ResourceLocation("gregtech:textures/gui/GuiCover.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0,0, gui_width, gui_height);
    }

    public void drawExtras(int mouseX, int mouseY, float parTicks) {}

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void keyTyped(char p_73869_1_, int p_73869_2_) {
        if (p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
            return;
        }
        super.keyTyped(p_73869_1_, p_73869_2_);
    }

    @Override
    public void addElement(IGuiElement element) {
        if (elements.contains(element))
            return;
        elements.add(element);
    }
    @Override
    public boolean removeElement(IGuiElement element) {
        return elements.remove(element);
    }

    public void actionPerformed(GuiButton button) {
        selectedButton = button;
    }

    public void clearSelectedButton() {
        selectedButton = null;
    }
    public GuiButton getSelectedButton(){return selectedButton;}

    public void buttonClicked(GuiButton button) {

    }

    public RenderItem getItemRenderer() {
        return itemRender;
    }

    /**
     * GT_IToolTipRenderer
     */
    @Override
    public void drawHoveringText(List par1List, int par2, int par3, FontRenderer render) {
        super.drawHoveringText(par1List, par2, par3, render);
    }
    @Override
    public FontRenderer getFontRenderer() {
        return super.fontRendererObj;
    }
    @Override
    public void addToolTip(GT_GuiTooltip toolTip) {
        ttManager.addToolTip(toolTip);
    }
    @Override
    public boolean removeToolTip(GT_GuiTooltip toolTip) {
        return ttManager.removeToolTip(toolTip);
    }

    /**
     * Junk
     */
    @Override
    public int getGuiTop() {
        return guiTop;
    }
    @Override
    public int getGuiLeft() {
        return guiLeft;
    }
    @Override
    public int getXSize() {
        return gui_width;
    }
    @Override
    public int getYSize() {
        return gui_height;
    }
}
