package com.detrav.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.detrav.gui.textures.DetravMapTexture;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravGuiProPick extends GuiScreen {
    public static final int GUI_ID = 20;
    private static DetravMapTexture map = null;
    ListOres ores = null;

    public DetravGuiProPick()
    {

    }

    public static void newMap(DetravMapTexture aMap) {
        if (map != null) {
            map.deleteGlTexture();
            map = null;
        }
        map = aMap;
        map.loadTexture(null);
        //ores = new ListOres(Minecraft.getMinecraft(),)
    }

    int prevW;
    int prevH;


    private static int minHeight = 128;
    private static int minWidth = 128;

    private static final ResourceLocation back = new ResourceLocation("gregtech:textures/gui/propick.png");

    @Override
    public void  drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if(map!=null)
        {
            int currentWidth = Math.max(map.width,minWidth);
            int currentHeight = Math.max(map.height,minHeight);
            int aX = (this.width - currentWidth-100)/2;
            int aY = (this.height - currentHeight)/2;

            if(ores == null)
            {
                ores = new ListOres(this,100,currentHeight,aY,aY+currentHeight,aX+currentWidth,10,map.ores);
                prevW = width;
                prevH = height;
            }
            if(prevW!=width || prevH !=height)
            {
                ores = new ListOres(this,100,currentHeight,aY,aY+currentHeight,aX+currentWidth,10,map.ores);
                prevW = width;
                prevH = height;
            }


            //dradback for ores
            drawRect(aX,aY,aX+currentWidth+100,aY+currentHeight,0xFFC6C6C6);
            map.glBindTexture();
            map.draw(aX,aY);
            ores.drawScreen(x,y,f);
            mc.getTextureManager().bindTexture(back);
            GL11.glColor4f(0xFF, 0xFF, 0xFF, 0xFF);
            //drawcorners
            drawTexturedModalRect(aX-5,aY-5,0,0,5,5);//leftTop
            drawTexturedModalRect(aX+currentWidth+100,aY-5,171,0,5,5);//RightTop
            drawTexturedModalRect(aX-5,aY+currentHeight,0,161,5,5);//leftDown
            drawTexturedModalRect(aX+currentWidth+100,aY+currentHeight,171,161,5,5);//RightDown
            //draw edges
            for(int i = aX;i<aX+currentWidth+100;i+=128) drawTexturedModalRect(i,aY-5,5,0,Math.min(128,aX+currentWidth+100-i),5); //top
            for(int i = aX;i<aX+currentWidth+100;i+=128) drawTexturedModalRect(i,aY+currentHeight,5,161,Math.min(128,aX+currentWidth+100-i),5); //down
            for(int i = aY;i<aY+currentHeight;i+=128) drawTexturedModalRect(aX-5,i,0,5,5,Math.min(128,aY + currentHeight-i)); //left
            for(int i = aY;i<aY+currentHeight;i+=128) drawTexturedModalRect(aX+currentWidth+100,i,171,5,5,Math.min(128,aY+currentHeight-i)); //right
        }
    }

    class ListOres extends GuiScrollingList
    {
        HashMap<String,Integer> ores = null;
        List<String> keys = null;

        public ListOres(GuiScreen parent, int width, int height, int top, int bottom, int left, int entryHeight,HashMap<String,Integer> aOres) {
            super(parent.mc, width, height, top, bottom, left, entryHeight);
            ores = aOres;
            keys = new ArrayList(map.ores.keySet());
            Collections.sort(keys);
        }

        @Override
        protected int getSize() {
            return ores.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {

        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {
            //drawRect(this.left,this.top,this.listWidth,this.listHeight,0xFFFFFFFF);
        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
            //drawString(fontRendererObj,);
            //fontRendererObj.drawString(fontRendererObj.trimStringToWidth())

            fontRendererObj.drawString(fontRendererObj.trimStringToWidth(keys.get(slotIdx), listWidth - 10), this.left + 3 , slotTop + 2, ores.get(keys.get(slotIdx)));

        }
    }
}
