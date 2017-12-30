package com.detrav.net;

import com.detrav.DetravScannerMod;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.gui.textures.DetravMapTexture;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;


/**
 * Created by wital_000 on 20.03.2016.
 */
public class DetravProPickPacket00 extends DetravPacket {
    public int chunkX;
    public int chunkZ;
    public int size;
    public int ptype;
    HashMap<Byte,Short>[][] map = null;

    @Override
    public int getPacketID() {
        return 0;
    }

    public int level = -1;

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        tOut.writeInt(ptype);
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
        DetravProPickPacket00 packet = new DetravProPickPacket00();
        packet.ptype = aData.readInt();
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
        if(checkOut != checkOut2) return new DetravProPickPacket00();
        return packet;
    }


    @Override
    public void process() {
        DetravGuiProPick.newMap(new DetravMapTexture(this));
        DetravScannerMod.proxy.openProPickGui();
    }

    public void addBlock(int x, int y, int z, short metaData) {
        if(map == null) map = new HashMap[(size*2+1)*16][(size*2+1)*16];
        int aX = x - (chunkX-size)*16;
        int aZ = z - (chunkZ-size)*16;
        if(map[aX][aZ] == null) map[aX][aZ] = new HashMap<Byte, Short>();
        map[aX][aZ].put((byte)y,metaData);
        //String key = String.format(("x_y"))
    }

    private HashMap<String,Integer> ores = null;

    public BufferedImage getImage(int posX,int posZ) {
        int wh = (size*2+1)*16;
        //int aWh = 1024;
        //while (aWh<wh) aWh*=2;
        BufferedImage image = new BufferedImage(wh,wh,BufferedImage.TYPE_INT_ARGB );
        WritableRaster raster = image.getRaster();

        int playerI = posX - (chunkX-size)*16;
        int playerJ = posZ - (chunkZ-size)*16;

        if(ores == null) ores = new HashMap<String, Integer>();
        int exception = 0;
        switch (ptype) {
            case 0:
            case 1:
            for (int i = 0; i < wh; i++)
                for (int j = 0; j < wh; j++) {
                    if (map[i][j] == null) {
                        raster.setSample(i, j, 0, 255);
                        raster.setSample(i, j, 1, 255);
                        raster.setSample(i, j, 2, 255);
                        raster.setSample(i, j, 3, 255);
                    } else {
                        for (short meta : map[i][j].values()) {
                            String name;
                            short[] rgba;
                            Materials tMaterial = null;
                            try {
                                tMaterial = GregTech_API.sGeneratedMaterials[meta % 1000];
                            } catch (Exception e) {
                                tMaterial = null;
                            }
                            if (tMaterial == null) {
                                exception++;
                                continue;
                            }
                            rgba = tMaterial.getRGBA();
                            //ores.put(GT_Ore)
                            name = tMaterial.getLocalizedNameForItem(GT_LanguageManager.getTranslation("gt.blockores." + meta + ".name"));

                            raster.setSample(i, j, 0, rgba[0]);
                            raster.setSample(i, j, 1, rgba[1]);
                            raster.setSample(i, j, 2, rgba[2]);
                            raster.setSample(i, j, 3, 255);
                            if (!ores.containsKey(name))
                                ores.put(name, (0xFF << 24) + ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF)));
                        }
                    }
                    if (playerI == i || playerJ == j) {
                        raster.setSample(i, j, 0, (raster.getSample(i, j, 0) + 255) / 2);
                        raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                        raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                    }
                    if ((i - 15) % 16 == 0 || (j - 15) % 16 == 0) {
                        raster.setSample(i, j, 0, raster.getSample(i, j, 0) / 2);
                        raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                        raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                    }
                }
                break;
            case 2:
                int gasId = Materials.NatruralGas.mGas.getID();
                short[] gas_rgba = new short[]{ 0,0xFF,0xFF};
                int oilLightId =  Materials.OilLight.mFluid.getID();
                short[] oilLight_rgba = new short[]{ 0xFF,0xFF,0};
                int oilMediumId = Materials.OilMedium.mFluid.getID();
                short[] oilMedium_rgba = new short[]{ 0,0xFF,0};
                int oilHeavyId = Materials.OilHeavy.mFluid.getID();
                short[] oilHeavy_rgba = new short[]{ 0xFF,0,0xFF};
                int oilId = Materials.Oil.mFluid.getID();
                short[] oil_rgba = new short[]{ 0,0,0};
                short[] metas = new short[2];
                for (int i = 0; i < wh; i++)
                    for (int j = 0; j < wh; j++) {
                        if (map[i][j] == null) {
                            raster.setSample(i, j, 0, 255);
                            raster.setSample(i, j, 1, 255);
                            raster.setSample(i, j, 2, 255);
                            raster.setSample(i, j, 3, 255);
                        } else {
                            metas[0] = map[i][j].get(Byte.valueOf((byte)1));
                            metas[1] = map[i][j].get(Byte.valueOf((byte)2));
                            metas[1] = (short)(metas[1]/2 + 20);
                            if(metas[1] >0xFF) metas[1] = 0xFF;
                            String name = null;
                            short[] rgba = null;
                            if (metas[0] == gasId) {
                                name = "NatruralGas";
                                rgba = gas_rgba.clone();
                            } else if (metas[0] == oilLightId) {
                                name = "OilLight";
                                rgba = oilLight_rgba.clone();
                            } else if (metas[0] == oilMediumId) {
                                name = "OilMedium";
                                rgba = oilMedium_rgba.clone();
                            } else if (metas[0] == oilHeavyId) {
                                name = "OilHeavy";
                                rgba = oilHeavy_rgba.clone();
                            } else if (metas[0] == oilId) {
                                name = "Oil";
                                rgba = oil_rgba.clone();
                            }
                            if (rgba == null) continue;
                            if (name == null) continue;

                            if (!ores.containsKey(name))
                                ores.put(name, (0xFF << 24) + ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF)));

                            for( int z = 0; z<3; z++) {
                                rgba[z] = (short) (rgba[z] + (0xFF - metas[1]));
                                if(rgba[z]>0xFF) rgba[z] = 0xFF;
                            }

                            raster.setSample(i, j, 0, rgba[0]);
                            raster.setSample(i, j, 1, rgba[1]);
                            raster.setSample(i, j, 2, rgba[2]);
                            raster.setSample(i, j, 3, 255);


                        }
                        if (playerI == i || playerJ == j) {
                            raster.setSample(i, j, 0, (raster.getSample(i, j, 0) + 255) / 2);
                            raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                            raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                        }
                        if ((i - 15) % 16 == 0 || (j - 15) % 16 == 0) {
                            raster.setSample(i, j, 0, raster.getSample(i, j, 0) / 2);
                            raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                            raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                        }
                    }
                break;
            case 3:
                    ores.put("Pollution", (0xFF << 24) + ((0 & 0xFF) << 16) + ((0 & 0xFF) << 8) + ((0 & 0xFF)));
                for (int i = 0; i < wh; i++)
                    for (int j = 0; j < wh; j++) {
                        if (map[i][j] == null) {
                            raster.setSample(i, j, 0, 255);
                            raster.setSample(i, j, 1, 255);
                            raster.setSample(i, j, 2, 255);
                            raster.setSample(i, j, 3, 255);
                        } else {
                            for (short meta : map[i][j].values()) {
                                raster.setSample(i, j, 0, meta);
                                raster.setSample(i, j, 1, meta);
                                raster.setSample(i, j, 2, meta);
                                raster.setSample(i, j, 3, 255);
                            }
                        }
                        if (playerI == i || playerJ == j) {
                            raster.setSample(i, j, 0, (raster.getSample(i, j, 0) + 255) / 2);
                            raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                            raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                        }
                        if ((i - 15) % 16 == 0 || (j - 15) % 16 == 0) {
                            raster.setSample(i, j, 0, raster.getSample(i, j, 0) / 2);
                            raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                            raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                        }
                    }
                break;
            default:
                DetravScannerMod.proxy.sendPlayerExeption("Not been realized YET!");
                break;
        }
        if(exception > 0)
            DetravScannerMod.proxy.sendPlayerExeption("null matertial exception: " + exception);
        /*try {
            File outputfile = new File("saved.png");
            ImageIO.write(image, "png", outputfile);
        }
        catch (Exception e) {}*/

        return image;


        //image.set
        //return null;
    }

    public HashMap<String,Integer> getOres()
    {
        if(ores == null) return new HashMap<String, Integer>();
        return ores;
    }


    public int getSize() {
        return (size*2+1)*16;
    }
}
