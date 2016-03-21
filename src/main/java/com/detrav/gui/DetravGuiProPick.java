package com.detrav.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureUtil;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravGuiProPick extends GuiScreen {
    public static final int GUI_ID = 20;
    private static DetravMapTexture map = null;
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
    }



    @Override
    public void  drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if(map!=null)
        {
            int aX = (this.width - map.width)/2;
            int aY = (this.height - map.height)/2;
            map.glBindTexture();
            map.draw(aX,aY);
            //this.drawTexturedModalRect(aX,aY,0,0,map.width,map.height);
        }
        //TextureUtil.glGenTextures()
        //this.mc.renderEngine.bindTexture();
        //this.drawTexturedModelRectFromIcon();
        //this.drawTexturedModalRect();
    }
}
