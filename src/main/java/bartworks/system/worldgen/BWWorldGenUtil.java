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

import bartworks.common.configs.Configuration;
import gregtech.api.GregTechAPI;

public class BWWorldGenUtil {

    private BWWorldGenUtil() {}

    public static final Block GT_TILES = GregTechAPI.sBlockMachines;

    public static int getGenerator(Random rand, int tier) {
        int meta, randomIndex;
        switch (tier) {
            case 0 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.highPressureSteam.generators.length);
                meta = Configuration.rossRuinMetas.highPressureSteam.generators[randomIndex];
            }
            case 1 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.lv.generators.length);
                meta = Configuration.rossRuinMetas.lv.generators[randomIndex];
            }
            case 2 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.mv.generators.length);
                meta = Configuration.rossRuinMetas.mv.generators[randomIndex];
            }
            case 3 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.hv.generators.length);
                meta = Configuration.rossRuinMetas.hv.generators[randomIndex];
            }
            case 4 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.ev.generators.length);
                meta = Configuration.rossRuinMetas.ev.generators[randomIndex];
            }
            default -> {
                throw new IllegalStateException("tier " + tier + " is not allowed for Ross Ruins.");
            }
        }
        if (GregTechAPI.METATILEENTITIES[meta] == null) {
            throw new IllegalStateException("MetaID " + meta + " is null, please remove it from the Ross Ruin config");
        }

        return meta;
    }

    public static int getBuffer(Random rand, int tier) {
        int meta, randomIndex;
        switch (tier) {
            case 0 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.highPressureSteam.buffers.length);
                meta = Configuration.rossRuinMetas.highPressureSteam.buffers[randomIndex];
            }
            case 1 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.lv.buffers.length);
                meta = Configuration.rossRuinMetas.lv.buffers[randomIndex];
            }
            case 2 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.mv.buffers.length);
                meta = Configuration.rossRuinMetas.mv.buffers[randomIndex];
            }
            case 3 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.hv.buffers.length);
                meta = Configuration.rossRuinMetas.hv.buffers[randomIndex];
            }
            case 4 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.ev.buffers.length);
                meta = Configuration.rossRuinMetas.ev.buffers[randomIndex];
            }
            default -> {
                throw new IllegalStateException("tier " + tier + " is not allowed for Ross Ruins.");
            }
        }
        if (GregTechAPI.METATILEENTITIES[meta] == null) {
            throw new IllegalStateException("MetaID " + meta + " is null, please remove it from the Ross Ruin config");
        }

        return meta;
    }

    public static int getCable(Random rand, int tier) {
        int meta, randomIndex;
        switch (tier) {
            case 0 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.highPressureSteam.cables.length);
                meta = Configuration.rossRuinMetas.highPressureSteam.cables[randomIndex];
            }
            case 1 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.lv.cables.length);
                meta = Configuration.rossRuinMetas.lv.cables[randomIndex];
            }
            case 2 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.mv.cables.length);
                meta = Configuration.rossRuinMetas.mv.cables[randomIndex];
            }
            case 3 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.hv.cables.length);
                meta = Configuration.rossRuinMetas.hv.cables[randomIndex];
            }
            case 4 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.ev.cables.length);
                meta = Configuration.rossRuinMetas.ev.cables[randomIndex];
            }
            default -> {
                throw new IllegalStateException("tier " + tier + " is not allowed for Ross Ruins.");
            }
        }
        if (GregTechAPI.METATILEENTITIES[meta] == null) {
            throw new IllegalStateException("MetaID " + meta + " is null, please remove it from the Ross Ruin config");
        }

        return meta;
    }

    public static int getMachine(Random rand, int tier) {
        int meta, randomIndex;
        switch (tier) {
            case 0 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.highPressureSteam.machines.length);
                meta = Configuration.rossRuinMetas.highPressureSteam.machines[randomIndex];
            }
            case 1 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.lv.machines.length);
                meta = Configuration.rossRuinMetas.lv.machines[randomIndex];
            }
            case 2 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.mv.machines.length);
                meta = Configuration.rossRuinMetas.mv.machines[randomIndex];
            }
            case 3 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.hv.machines.length);
                meta = Configuration.rossRuinMetas.hv.machines[randomIndex];
            }
            case 4 -> {
                randomIndex = rand.nextInt(Configuration.rossRuinMetas.ev.machines.length);
                meta = Configuration.rossRuinMetas.ev.machines[randomIndex];
            }
            default -> {
                throw new IllegalStateException("tier " + tier + " is not allowed for Ross Ruins.");
            }
        }
        if (GregTechAPI.METATILEENTITIES[meta] == null) {
            throw new IllegalStateException("MetaID " + meta + " is null, please remove it from the Ross Ruin config");
        }

        return meta;
    }
}
