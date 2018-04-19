package com.detrav.gui.textures;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.detrav.net.DetravProPickPacket00;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravMapTexture extends AbstractTexture {

    private DetravProPickPacket00 packet;

    public DetravMapTexture(DetravProPickPacket00 aPacket)
    {
        packet = aPacket;
    }

    public int width = -1;
    public int height = -1;
    public HashMap<String,Integer> ores = null;

    @Override
    public void loadTexture(IResourceManager p_110551_1_){
        this.deleteGlTexture();
        if(packet!=null)
        {
            int tId = getGlTextureId();
            if(tId <0) return;
            BufferedImage bufferedimage = packet.getImage((int)Minecraft.getMinecraft().thePlayer.posX,(int)Minecraft.getMinecraft().thePlayer.posZ);
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, false);
            width = packet.getSize();
            height = packet.getSize();
            ores = packet.getOres();
        }
        //GL11.glDrawPixels();
    }

    public int glBindTexture() {
        if (this.glTextureId < 0) return this.glTextureId;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getGlTextureId());
        return this.glTextureId;
    }

    public void draw(int x, int y)
    {
        float f = 1F / (float)width;
        float f1 = 1F / (float)height;
        int u = 0;
        int v = 0;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x), (double)(y + height), 0, (double)((float)(u) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), 0, (double)((float)(u + width) * f), (double)((float)(v + height) * f1));
        tessellator.addVertexWithUV((double)(x + width), (double)(y), 0, (double)((float)(u + width) * f), (double)((float)(v) * f1));
        tessellator.addVertexWithUV((double)(x), (double)(y), 0, (double)((float)(u) * f), (double)((float)(v) * f1));
        tessellator.draw();
    }

}
