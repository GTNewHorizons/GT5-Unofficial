/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import gregtech.api.GregTech_API;
import java.util.Random;
import net.minecraft.block.Block;

public class GT_WorldgenUtil {

    private GT_WorldgenUtil() {}

    public static final Block GT_TILES = GregTech_API.sBlockMachines;

    public static short getGenerator(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[0][tier][rand.nextInt(ConfigHandler.metasForTiers[0][tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? (short) meta : GT_WorldgenUtil.getGenerator(rand, tier);
    }

    public static short getBuffer(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[1][tier][rand.nextInt(ConfigHandler.metasForTiers[1][tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? (short) meta : GT_WorldgenUtil.getBuffer(rand, tier);
    }

    public static short getCable(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[2][tier][rand.nextInt(ConfigHandler.metasForTiers[2][tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? (short) meta : GT_WorldgenUtil.getCable(rand, tier);
    }

    public static short getMachine(Random rand, int tier) {
        int meta = ConfigHandler.metasForTiers[3][tier][rand.nextInt(ConfigHandler.metasForTiers[3][tier].length)];
        return GregTech_API.METATILEENTITIES[meta] != null ? (short) meta : GT_WorldgenUtil.getMachine(rand, tier);
    }
}
