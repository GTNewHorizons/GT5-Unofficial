package com.github.technus.tectech.thing.item.gui;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by danie_000 on 17.12.2017.
 */
public class ScanDisplayScreen extends GuiScreen {
    private static final int sizeX=240,sizeY=220;
    private int baseX,baseY;
    private Button up,down,pgUp,pgDown;
    private String[] lines;
    private int firstLine =0;

    private static final ResourceLocation BACKGROUND =new ResourceLocation("tectech:textures/gui/scanDisplayScreen.png");
    private static final ResourceLocation ITEM =new ResourceLocation("tectech:textures/gui/scanDisplayItem.png");

    public ScanDisplayScreen(EntityPlayer player){
        super();
        lines= ElementalDefinitionScanStorage_EM.getLines(player.getHeldItem());
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(ITEM);
        long tick=System.currentTimeMillis()/150;
        int itick=(int)(tick%12);
        drawTexturedModalRect(baseX+99,baseY+189,32*(itick/6),32*(itick%6), 32, 32);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(baseX,baseY,0,0, sizeX, sizeY);
        glDisable(GL_BLEND);
        super.drawScreen(x,y,partialTicks);

        int textBaseX=baseX+20;
        int textBaseY=baseY+20;

        for(int i=firstLine;i<lines.length && i<=firstLine+8;i++){
            TecTech.proxy.renderUnicodeString(lines[i],textBaseX,textBaseY+i*9,200,0xDDEEFF);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        baseX=(width-sizeX)/2;
        baseY=(height-sizeY)/2-12;
        int buttonBaseY=baseY+145;
        this.buttonList.add(up=new Button(0,baseX+77,buttonBaseY,0,220));
        this.buttonList.add(down=new Button(1,baseX+99,buttonBaseY,20,220));
        this.buttonList.add(pgUp=new Button(2,baseX+121,buttonBaseY,40,220));
        this.buttonList.add(pgDown=new Button(3,baseX+143,buttonBaseY,60,220));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (lines.length <= 8) return;
        if (button == pgUp) {
            firstLine -= 8;
        } else if (button == up) {
            firstLine--;
        } else if (button == down) {
            firstLine++;
        } else if (button == pgDown) {
            firstLine += 8;
        }
        if (firstLine > lines.length - 8) firstLine = lines.length - 8;
        if (firstLine < 0) firstLine = 0;
    }

    private class Button extends GuiButton{
        int u,v;
        Button(int id,int x,int y,int u,int v){
            super(id,x,y,20,20,"");
            this.u=u;
            this.v=v;
        }

        @Override
        public void drawButton(Minecraft mc, int xPos, int yPos)
        {
            if (this.visible)
            {
                this.field_146123_n = xPos >= this.xPosition && yPos >= this.yPosition && xPos < this.xPosition + this.width && yPos < this.yPosition + this.height;
                mc.getTextureManager().bindTexture(BACKGROUND);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                mc.getTextureManager().bindTexture(BACKGROUND);
                drawTexturedModalRect(this.xPosition, this.yPosition, u + this.getHoverState(this.field_146123_n) * 80, v, this.width, this.height);
                glDisable(GL_BLEND);
                GL11.glEnable(GL11.GL_BLEND);
            }
        }
    }
}
