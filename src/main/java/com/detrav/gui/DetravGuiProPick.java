package com.detrav.gui;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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


    @Override
    public void  drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if(map!=null)
        {
            List<String> keys = new ArrayList(map.ores.keySet());
            Collections.sort(keys);
            int w = 0;
            for(String item : keys)
            {
                w = Math.max(fontRendererObj.getStringWidth(item),w);
            }
            w+=10;

            int aX = (this.width - map.width-100)/2;
            int aY = (this.height - map.height)/2;

            if(ores == null)
            {
                ores = new ListOres(this,100,map.height,aY,aY+map.height,aX+map.width,10,map.ores);
                prevW = width;
                prevH = height;
            }
            if(prevW!=width || prevH !=height)
            {
                ores = new ListOres(this,100,map.height,aY,aY+map.height,aX+map.width,10,map.ores);
                prevW = width;
                prevH = height;
            }

            map.glBindTexture();
            map.draw(aX,aY);
            ores.drawScreen(x,y,f);
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
