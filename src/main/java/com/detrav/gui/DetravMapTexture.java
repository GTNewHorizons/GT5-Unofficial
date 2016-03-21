package com.detrav.gui;

import com.detrav.net.DetravProPickPacket01;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravMapTexture extends AbstractTexture {

    private DetravProPickPacket01 packet;

    public DetravMapTexture(DetravProPickPacket01 aPacket)
    {
        packet = aPacket;
    }

    public int width = -1;
    public int height = -1;

    @Override
    public void loadTexture(IResourceManager p_110551_1_){
        this.deleteGlTexture();
        if(packet!=null)
        {
            int tId = getGlTextureId();
            if(tId <0) return;
            BufferedImage bufferedimage = packet.getImage();
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, false);
            width = bufferedimage.getWidth();
            height = bufferedimage.getHeight();
        }
        //GL11.glDrawPixels();
    }

    public int glBindTexture() {
        if (this.glTextureId < 0) return this.glTextureId;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getGlTextureId());
        return this.glTextureId;
    }

}
