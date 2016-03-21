package com.detrav.net;

import com.detrav.DetravScannerMod;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.gui.DetravMapTexture;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;

/**
 * Created by wital_000 on 20.03.2016.
 */
public class DetravProPickPacket01 extends DetravPacket {
    public int chunkX;
    public int chunkZ;
    public int size;
    HashMap<Byte,Short>[][] map = null;

    @Override
    public int getPacketID() {
        return 0;
    }

    public int level = -1;

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        tOut.writeInt(level);
        tOut.writeInt(chunkX);
        tOut.writeInt(chunkZ);
        tOut.writeInt(size);
        int aSize = (size*2+1)*16;
        int checkOut = 0;
        for(int i =0; i<aSize; i++)
            for(int j =0; j<aSize; j++)
            {
                if(map[i][j]==null)
                    tOut.writeByte(0);
                else
                {
                    tOut.writeByte(map[i][j].keySet().size());
                    for(byte key : map[i][j].keySet())
                    {
                        tOut.writeByte(key);
                        tOut.writeShort(map[i][j].get(key));
                        checkOut++;
                    }
                }
            }
        tOut.writeInt(checkOut);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        DetravProPickPacket01 packet = new DetravProPickPacket01();
        packet.level = aData.readInt();
        packet.chunkX = aData.readInt();
        packet.chunkZ = aData.readInt();
        packet.size = aData.readInt();
        packet.map = new HashMap[(packet.size * 2 + 1) * 16][(packet.size * 2 + 1) * 16];
        int aSize = (packet.size * 2 + 1) * 16;
        int checkOut = 0;
        for (int i = 0; i < aSize; i++)
            for (int j = 0; j < aSize; j++) {
                byte kSize = aData.readByte();
                if(kSize == 0) continue;
                packet.map[i][j] = new HashMap<Byte, Short>();
                for (int k = 0; k < kSize; k++) {
                    packet.map[i][j].put(aData.readByte(),aData.readShort());
                    checkOut++;
                }
            }
        int checkOut2 = aData.readInt();
        if(checkOut != checkOut2) return new DetravProPickPacket01();
        return packet;
    }

    @Override
    public void process() {
        DetravGuiProPick.newMap(new DetravMapTexture(this));
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.openGui(DetravScannerMod.instance, DetravGuiProPick.GUI_ID,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("NetworkTested: " + Integer.toString(level)));
    }

    public void addBlock(int x, int y, int z, short metaData) {
        if(map == null) map = new HashMap[(size*2+1)*16][(size*2+1)*16];
        int aX = x - (chunkX-size)*16;
        int aZ = z - (chunkZ-size)*16;
        if(map[aX][aZ] == null) map[aX][aZ] = new HashMap<Byte, Short>();
        map[aX][aZ].put((byte)y,metaData);
        //String key = String.format(("x_y"))
    }

    public BufferedImage getImage() {
        int wh = (size*2+1)*16;
        BufferedImage image = new BufferedImage(wh,wh,BufferedImage.TYPE_3BYTE_BGR );
        WritableRaster raster = image.getRaster();
        for(int i =0; i<wh; i++)
            for(int j =0; j<wh; j++) {
                if (map[i][j] == null)
                {
                    raster.setSample(i,j,0,255);
                    raster.setSample(i,j,1,255);
                    raster.setSample(i,j,2,255);
                }
                else
                {
                    for(short meta : map[i][j].values()) {
                        //Пока только по одному буду
                        Materials tMaterial = GregTech_API.sGeneratedMaterials[meta% 1000];
                        short[] rgba = tMaterial.getRGBA();
                        raster.setSample(i,j,0,rgba[2]);
                        raster.setSample(i,j,1,rgba[1]);
                        raster.setSample(i,j,2,rgba[0]);
                    }
                }
            }
        return image;


        //image.set
        //return null;
    }
}
