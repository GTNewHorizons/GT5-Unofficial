/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.worldgen;

import gregtech.api.GregTech_API;
import net.minecraft.block.Block;

import java.util.Random;

public class GT_WorldgenUtil {

    private GT_WorldgenUtil() {}

    public static final Block GT_TILES = GregTech_API.sBlockMachines;

    public static final short[][] METAFORTIERS_ENERGY = {
            {100,101,102,105}, //ULV=HPSteam
            {1110,1115,1120,1127},
            {1111,12726,1116,1121,1128},
            {1112,12727,1117,1122,1129},
            {12728,1190,1130,12685},
            {1191,1174,695,12686},
    };
    public static final short[][] METAFORTIERS_BUFFER = {
            {5133,5123}, //ULV=HPSteam
            {161,171,181,191},
            {162,172,182,192},
            {163,173,183,193},
            {164,174,184,194},
            {165,175,185,195},
    };
    //ULV=HPSteam
    public static final short[][] METAFORTIERS_CABLE = {
            {5133,5123}, //ULV=HPSteam
            {1210,1230,1250,1270,1290},
            {1310,1330,1350,1370,1390},
            {1410,1430,1450,1470,1490},
            {1510,1530,1550,1570,1590},
            {1650,1670,1690},
    };
    public static final short[][] METAFORTIERS_MACHINE = {
            {103,104,106,107,109,110,112,113,115,116,118,119}, //ULV=HPSteam
            {201,211,221,231,241,251,261,271,281,291,301,311,321,331,341,351,361,371,381,391,401,411,421,431,441,451,461,471,481,491,501,511,521,531,541,551,561,571,581,591,601,611,621,631,641,651,661,671},
            {},
            {},
            {},
            {},
    };

    private static boolean initialisized;

    private static void init(){

        for (int j = 1; j < 5; j++) {
            GT_WorldgenUtil.METAFORTIERS_MACHINE[j+1]=new short[GT_WorldgenUtil.METAFORTIERS_MACHINE[1].length];
            for (int i = 0; i < GT_WorldgenUtil.METAFORTIERS_MACHINE[1].length; i++) {
                GT_WorldgenUtil.METAFORTIERS_MACHINE[j+1][i]= (short) (GT_WorldgenUtil.METAFORTIERS_MACHINE[1][i]+j);
            }
        }
        GT_WorldgenUtil.initialisized =true;
    }

    public static short getGenerator(Random rand,int tier){
        short meta = GT_WorldgenUtil.METAFORTIERS_ENERGY[tier][rand.nextInt(GT_WorldgenUtil.METAFORTIERS_ENERGY[tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? meta : GT_WorldgenUtil.getGenerator(rand,tier);
    }

    public static short getBuffer(Random rand,int tier){
        short meta = GT_WorldgenUtil.METAFORTIERS_BUFFER[tier][rand.nextInt(GT_WorldgenUtil.METAFORTIERS_BUFFER[tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? meta : GT_WorldgenUtil.getBuffer(rand,tier);
    }

    public static short getCable(Random rand,int tier){
        short meta = GT_WorldgenUtil.METAFORTIERS_CABLE[tier][rand.nextInt(GT_WorldgenUtil.METAFORTIERS_CABLE[tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? meta : GT_WorldgenUtil.getCable(rand,tier);
    }

    public static short getMachine(Random rand,int tier){
        if (!GT_WorldgenUtil.initialisized)
            GT_WorldgenUtil.init();
        short meta = GT_WorldgenUtil.METAFORTIERS_MACHINE[tier][rand.nextInt(GT_WorldgenUtil.METAFORTIERS_MACHINE[tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? meta : GT_WorldgenUtil.getMachine(rand,tier);
    }


}
