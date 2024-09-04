/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.worldgen;

import java.util.Random;

import net.minecraft.block.Block;

import bartworks.common.configs.ConfigHandler;
import gregtech.api.GregTechAPI;

public class BWWorldGenUtil {

    private BWWorldGenUtil() {}

    public static final Block GT_TILES = GregTechAPI.sBlockMachines;

    public static short getGenerator(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[0][tier][rand.nextInt(ConfigHandler.metasForTiers[0][tier].length)];
        return GregTechAPI.METATILEENTITIES[meta] != null ? (short) meta : BWWorldGenUtil.getGenerator(rand, tier);
    }

    public static short getBuffer(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[1][tier][rand.nextInt(ConfigHandler.metasForTiers[1][tier].length)];
        return GregTechAPI.METATILEENTITIES[meta] != null ? (short) meta : BWWorldGenUtil.getBuffer(rand, tier);
    }

    public static short getCable(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[2][tier][rand.nextInt(ConfigHandler.metasForTiers[2][tier].length)];
        return GregTechAPI.METATILEENTITIES[meta] != null ? (short) meta : BWWorldGenUtil.getCable(rand, tier);
    }

    public static short getMachine(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[3][tier][rand.nextInt(ConfigHandler.metasForTiers[3][tier].length)];
        return GregTechAPI.METATILEENTITIES[meta] != null ? (short) meta : BWWorldGenUtil.getMachine(rand, tier);
    }
}
