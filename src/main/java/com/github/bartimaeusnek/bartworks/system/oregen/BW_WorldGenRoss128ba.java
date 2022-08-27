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

package com.github.bartimaeusnek.bartworks.system.oregen;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static com.github.bartimaeusnek.crossmod.galacticraft.GalacticraftProxy.uo_dimensionList;
import static gregtech.api.enums.Materials.*;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import gregtech.api.interfaces.ISubTagContainer;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BW_WorldGenRoss128ba extends BW_OreLayer {

    public BW_WorldGenRoss128ba(
            String aName,
            boolean aDefault,
            int aMinY,
            int aMaxY,
            int aWeight,
            int aDensity,
            int aSize,
            ISubTagContainer top,
            ISubTagContainer bottom,
            ISubTagContainer between,
            ISubTagContainer sprinkled) {
        super(aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, top, bottom, between, sprinkled);
    }

    @Override
    public Block getDefaultBlockToReplace() {
        return Block.getBlockFromName("GalacticraftCore:tile.moonBlock");
    }

    @Override
    public int[] getDefaultDamageToReplace() {
        int[] ret = new int[12];
        for (int i = 0; i < 12; i++) {
            if (i != 5 && i != 3) ret[i] = i;
        }
        return ret;
    }

    @Override
    public String getDimName() {
        return StatCollector.translateToLocal("moon.Ross128ba");
    }

    public static void init_Ores() {
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.tib",
                true,
                30,
                60,
                6,
                1,
                16,
                Tiberium,
                Tiberium,
                NaquadahEnriched,
                NaquadahEnriched);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.Tungstate", true, 5, 40, 60, 4, 14, Ferberite, Huebnerit, Loellingit, Scheelite);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.bart",
                true,
                30,
                60,
                1,
                1,
                1,
                BArTiMaEuSNeK,
                BArTiMaEuSNeK,
                BArTiMaEuSNeK,
                BArTiMaEuSNeK);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.TurmalinAlkali",
                true,
                5,
                80,
                60,
                4,
                48,
                Olenit,
                FluorBuergerit,
                ChromoAluminoPovondrait,
                VanadioOxyDravit);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.Amethyst", true, 5, 80, 35, 2, 8, Amethyst, Olivine, Prasiolite, Hedenbergit);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.CopperSulfits",
                true,
                40,
                70,
                80,
                3,
                24,
                Djurleit,
                Bornite,
                Wittichenit,
                Tetrahedrite);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.RedZircon", true, 10, 80, 40, 3, 24, Fayalit, FuchsitAL, RedZircon, FuchsitCR);
        new BW_WorldGenRoss128ba(
                "ore.mix.ross128ba.Fluorspar", true, 10, 80, 35, 4, 8, Galena, Sphalerite, Fluorspar, Barite);
    }

    public static void init_undergroundFluids() {
        String ross128b = StatCollector.translateToLocal("moon.Ross128ba");
        uo_dimensionList.SetConfigValues(
                ross128b,
                ross128b,
                SaltWater.getFluid(1).getFluid().getName(),
                SaltWater.getFluid(1).getFluid().getName(),
                0,
                1250,
                40,
                5);
        uo_dimensionList.SetConfigValues(
                ross128b,
                ross128b,
                Helium_3.getGas(1).getFluid().getName(),
                Helium_3.getGas(1).getFluid().getName(),
                0,
                1250,
                60,
                5);
    }

    @Override
    public boolean isGenerationAllowed(World aWorld, int aDimensionType, int aAllowedDimensionType) {
        return aDimensionType == ConfigHandler.ross128BAID;
    }
}
