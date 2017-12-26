package com.github.technus.tectech.thing.item.gui;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by danie_000 on 17.12.2017.
 */
public class ScanDisplayScreen extends GuiScreen {
    private static final int sizeX=240,sizeY=220, renderedLines =10;
    private int baseX,baseY;
    private Button up,down,pgUp,pgDown;
    private final String[] lines;
    private int firstLine =0;

    private static final ResourceLocation[] BACKGROUNDS =new ResourceLocation[]{new ResourceLocation("tectech:textures/gui/scanDisplayScreen1.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen2.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen3.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen4.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen5.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen6.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen7.png"),
                                                                                new ResourceLocation("tectech:textures/gui/scanDisplayScreen8.png")};
    private static final ResourceLocation ITEM =new ResourceLocation("tectech:textures/gui/scanDisplayItem.png");

    public ScanDisplayScreen(EntityPlayer player){
        lines=ElementalDefinitionScanStorage_EM.getLines(player.getHeldItem());
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(ITEM);
        long tick=System.currentTimeMillis()/150;
        int itick=(int)(tick%12);
        drawTexturedModalRect(baseX+99,baseY+189,32*(itick/6),32*(itick%6), 32, 32);
        mc.getTextureManager().bindTexture(BACKGROUNDS[(int)(tick%8)]);
        drawTexturedModalRect(baseX,baseY,0,0, sizeX, sizeY);
        glDisable(GL_BLEND);
        super.drawScreen(x,y,partialTicks);

        itick=(TecTech.Rnd.nextInt(0x66)<<16)+0x77EEFF;
        int textBaseX=baseX+20;
        int textBaseXX=baseX+95;
        int textBaseY=baseY+28;
        for(int i=firstLine-1, j=8;i>=0 && j!=0;i--,j/=2){
            int equalPos=lines[i].indexOf('=');
            if(equalPos>=0){
                TecTech.proxy.renderUnicodeString(lines[i].substring(0,equalPos), textBaseX, textBaseY - 8 + j, 200, itick);
                TecTech.proxy.renderUnicodeString(lines[i].substring(equalPos), textBaseXX, textBaseY - 8 + j, 200, itick);
            }else {
                TecTech.proxy.renderUnicodeString(lines[i], textBaseX, textBaseY - 8 + j, 200, itick);
            }
        }
        for(int i = firstLine, j = 0; i<lines.length && j< renderedLines; i++,j++){
            textBaseY += 9;
            int equalPos=lines[i].indexOf('=');
            if(equalPos>=0){
                TecTech.proxy.renderUnicodeString(lines[i].substring(0,equalPos), textBaseX, textBaseY, 200, itick);
                TecTech.proxy.renderUnicodeString(lines[i].substring(equalPos), textBaseXX, textBaseY, 200, itick);
            }else {
                TecTech.proxy.renderUnicodeString(lines[i], textBaseX, textBaseY, 200, itick);
            }
        }
        for(int i = firstLine+ renderedLines, j = 8; i<lines.length && j!=0; i++,j/=2){
            int equalPos=lines[i].indexOf('=');
            if(equalPos>=0){
                TecTech.proxy.renderUnicodeString(lines[i].substring(0,equalPos), textBaseX, textBaseY + 17 - j, 200, itick);
                TecTech.proxy.renderUnicodeString(lines[i].substring(equalPos), textBaseXX, textBaseY + 17 - j, 200, itick);
            }else {
                TecTech.proxy.renderUnicodeString(lines[i], textBaseX, textBaseY + 17 - j, 200, itick);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        baseX=(width- sizeX)/2;
        baseY=(height- sizeY)/2-12;
        int buttonBaseY=baseY+145;
        buttonList.add(pgUp= new Button(0, baseX + 77, buttonBaseY, 0, 220));
        buttonList.add(up= new Button(1, baseX + 99, buttonBaseY, 20, 220));
        buttonList.add(down= new Button(2, baseX + 121, buttonBaseY, 40, 220));
        buttonList.add(pgDown= new Button(3, baseX + 143, buttonBaseY, 60, 220));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (lines.length <= renderedLines) {
            return;
        }
        if (button == pgUp) {
            firstLine -= renderedLines;
        } else if (button == up) {
            firstLine--;
        } else if (button == down) {
            firstLine++;
        } else if (button == pgDown) {
            firstLine += renderedLines;
        }
        if (firstLine > lines.length - renderedLines) {
            firstLine = lines.length - renderedLines;
        }
        if (firstLine < 0) {
            firstLine = 0;
        }
    }

    private static class Button extends GuiButton{
        int u,v;
        Button(int id,int x,int y,int u,int v){
            super(id,x,y,20,20,"");
            this.u=u;
            this.v=v;
        }

        @Override
        public void drawButton(Minecraft mc, int xPos, int yPos)
        {
            if (visible)
            {
                field_146123_n = xPos >= xPosition && yPos >= yPosition && xPos < xPosition + width && yPos < yPosition + height;
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                mc.getTextureManager().bindTexture(BACKGROUNDS[0]);
                drawTexturedModalRect(xPosition, yPosition, u + getHoverState(field_146123_n) * 80, v, width, height);
                glDisable(GL_BLEND);
                glEnable(GL_BLEND);
            }
        }
    }
}
