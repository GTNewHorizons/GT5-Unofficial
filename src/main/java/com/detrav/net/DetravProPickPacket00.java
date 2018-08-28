package com.detrav.net;

import com.detrav.DetravScannerMod;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.gui.textures.DetravMapTexture;
import com.detrav.utils.GTppHelper;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;

import net.minecraftforge.fluids.*;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.util.HashMap;

/*
//DEBUG CLASSES
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
*/

/**
 * Created by wital_000 on 20.03.2016.
 */
public class DetravProPickPacket00 extends DetravPacket {
    public int chunkX;
    public int chunkZ;
    public int size;
    public int ptype;
    HashMap<Byte,Short>[][] map = null;
    static HashMap<Integer, short[]> fluidColors = null;
    
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
        if(map == null)
        	map = new HashMap[(size*2+1)*16][(size*2+1)*16];
        int aX = x - (chunkX-size)*16;
        int aZ = z - (chunkZ-size)*16;
        if(map[aX][aZ] == null)
        	map[aX][aZ] = new HashMap<Byte, Short>();
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

        int playerI = posX - (chunkX-size)*16 - 1; // Correct player offset
        int playerJ = posZ - (chunkZ-size)*16 - 1;

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
                            gtPlusPlus.core.material.Material pMaterial = null;
                            try {
                            	if (meta<7000||meta>7500) {
                            		tMaterial = GregTech_API.sGeneratedMaterials[meta % 1000];
                            	} else {
                            		Short l = (short) (meta-7000);
                            		pMaterial = GTppHelper.decodeoresGTpp.get(l);
                            	}
                            } catch (Exception e) {
                                tMaterial = null;
                            }
                            if (meta<7000||meta>7500) {
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
                            else{
                                if (pMaterial == null) {
                                    exception++;
                                    continue;
                                }
                                rgba = pMaterial.getRGBA();
                                //ores.put(GT_Ore)
                                name = pMaterial.getLocalizedName() + " Ore";
                                
                                raster.setSample(i, j, 0, rgba[0]);
                                raster.setSample(i, j, 1, rgba[1]);
                                raster.setSample(i, j, 2, rgba[2]);
                                raster.setSample(i, j, 3, 255);
                                if (!ores.containsKey(name))
                                    ores.put(name, (0xFF << 24) + ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF)));
                                }
                            	
                        }
                    }
                    if (playerI == i || playerJ == j) {
                        raster.setSample(i, j, 0, (raster.getSample(i, j, 0) + 255) / 2);
                        raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                        raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                    }
                    if ((i) % 16 == 0 || (j) % 16 == 0) {  // Draw grid on screen
                        raster.setSample(i, j, 0, raster.getSample(i, j, 0) / 2);
                        raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                        raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                    }
                }
                break;
            case 2:
                if( fluidColors == null ) {  // Should probably be put somewhere else, but I suck at Java
                    fluidColors = new HashMap<Integer, short[]>();

                    fluidColors.put( Materials.NatruralGas.mGas.getID(),                         new short[]{0x00,0xff,0xff} );
                    fluidColors.put( Materials.OilLight.mFluid.getID(),                          new short[]{0xff,0xff,0x00} );
                    fluidColors.put( Materials.OilMedium.mFluid.getID(),                         new short[]{0x00,0xFF,0x00} );
                    fluidColors.put( Materials.OilHeavy.mFluid.getID(),                          new short[]{0xFF,0x00,0xFF} );
                    fluidColors.put( Materials.Oil.mFluid.getID(),                               new short[]{0x00,0x00,0x00} );
                    fluidColors.put( Materials.Helium_3.mGas.getID(),                            new short[]{0x80,0x20,0xe0} );
                    fluidColors.put( Materials.SaltWater.mFluid.getID(),                         new short[]{0x80,0xff,0x80} );
                    fluidColors.put( Materials.Naquadah.getMolten(0).getFluid().getID(),         new short[]{0x20,0x20,0x20} );
                    fluidColors.put( Materials.NaquadahEnriched.getMolten(0).getFluid().getID(), new short[]{0x60,0x60,0x60} );
                    fluidColors.put( Materials.Lead.getMolten(0).getFluid().getID(),             new short[]{0xd0,0xd0,0xd0} );
                    fluidColors.put( Materials.Chlorobenzene.mFluid.getID(),                     new short[]{0x40,0x80,0x40} );
                    fluidColors.put( FluidRegistry.getFluid("liquid_extra_heavy_oil").getID(),   new short[]{0x00,0x00,0x50} );
                    fluidColors.put( Materials.Oxygen.mGas.getID(),                              new short[]{0x40,0x40,0xA0} );
                    fluidColors.put( Materials.Nitrogen.mGas.getID(),                            new short[]{0x00,0x80,0xd0} );
                    fluidColors.put( Materials.Methane.mGas.getID(),                             new short[]{0x80,0x20,0x20} );
                    fluidColors.put( Materials.Ethane.mGas.getID(),                              new short[]{0x40,0x80,0x20} );
                    fluidColors.put( Materials.Ethylene.mGas.getID(),                            new short[]{0xd0,0xd0,0xd0} );
                    fluidColors.put( Materials.LiquidAir.mFluid.getID(),                         new short[]{0x40,0x80,0x40} );

/*
                   Set set = fluidColors.entrySet();
                   Iterator iterator = set.iterator();
                   System.out.println( "DETRAV SCANNER DEBUG" );
                   while(iterator.hasNext()) {
                       Map.Entry mentry = (Map.Entry) iterator.next();
                       System.out.println( "key is: "+ (Integer)mentry.getKey() + " & Value is: " +
                                           ((short[])mentry.getValue())[0] + " " + 
                                           ((short[])mentry.getValue())[1] + " " + 
                                           ((short[])mentry.getValue())[2] );
                   }
*/
               }

                short[] metas = new short[2];
                for (int i = 0; i < wh; i++)
                    for (int j = 0; j < wh; j++) {
                        if (map[i][j] == null) {
                            raster.setSample(i, j, 0, 255);
                            raster.setSample(i, j, 1, 255);
                            raster.setSample(i, j, 2, 255);
                            raster.setSample(i, j, 3, 255);
                        } else {
                            metas[0] = map[i][j].get(Byte.valueOf((byte)1));   //  fluidID
                            metas[1] = map[i][j].get(Byte.valueOf((byte)2));   //  Size of the field
                            String name = null;
                            short[] rgba = null;

                            rgba = (short[])fluidColors.get( (Integer) ((int) metas[0] ) );
                            if (rgba == null) {
                                DetravScannerMod.proxy.sendPlayerExeption( "Unknown fluid ID = " + metas[0] + " Please add to DetravProPickPacket00.java!" );
                                rgba = new short[]{ 0x00, 0xff, 0x00 };
                            }

                            name = FluidRegistry.getFluid( metas[0] ).getLocalizedName(new FluidStack( FluidRegistry.getFluid( metas[0] ), 0) );
                            if (name == null) {
                                name = "Unknown Fluid name!";
                            }

                            if (!ores.containsKey(name))
                                ores.put(name, (0xFF << 24) + ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF)));

                            int k = (i % 16); // Variables used to locate within a chunk.
                            int l = (j % 16);

                            if( ( (k + l*16) * 3) < (metas[1]+48) ) { // draw an indicator in the chunk about how large the field is at this chunk.
                                raster.setSample(i, j, 0, rgba[0]);
                                raster.setSample(i, j, 1, rgba[1]);
                                raster.setSample(i, j, 2, rgba[2]);
                                raster.setSample(i, j, 3, 255);
                            } else {
                                raster.setSample(i, j, 0, 255);
                                raster.setSample(i, j, 1, 255);
                                raster.setSample(i, j, 2, 255);
                                raster.setSample(i, j, 3, 255);
                            }

                        }
                        if (playerI == i || playerJ == j) {  // Draw red line on screen
                            raster.setSample(i, j, 0, (raster.getSample(i, j, 0) + 255) / 2);
                            raster.setSample(i, j, 1, raster.getSample(i, j, 1) / 2);
                            raster.setSample(i, j, 2, raster.getSample(i, j, 2) / 2);
                        }
                        if ((i) % 16 == 0 || (j) % 16 == 0) {  // Draw grid on screen
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
                        if ((i) % 16 == 0 || (j) % 16 == 0) {  // Draw grid on screen
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
