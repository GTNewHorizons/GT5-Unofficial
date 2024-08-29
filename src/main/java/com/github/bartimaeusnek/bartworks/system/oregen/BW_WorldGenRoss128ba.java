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

package com.github.bartimaeusnek.bartworks.system.oregen;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.BArTiMaEuSNeK;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Bornite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.ChromoAluminoPovondrait;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Djurleit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Fayalit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Ferberite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FluorBuergerit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Fluorspar;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FuchsitAL;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FuchsitCR;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Hedenbergit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Huebnerit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Loellingit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Olenit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Prasiolite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RedZircon;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Tiberium;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.VanadioOxyDravit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Wittichenit;
import static gregtech.api.enums.Materials.Amethyst;
import static gregtech.api.enums.Materials.Barite;
import static gregtech.api.enums.Materials.Galena;
import static gregtech.api.enums.Materials.NaquadahEnriched;
import static gregtech.api.enums.Materials.Olivine;
import static gregtech.api.enums.Materials.Scheelite;
import static gregtech.api.enums.Materials.Sphalerite;
import static gregtech.api.enums.Materials.Tetrahedrite;

import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;

import gregtech.api.interfaces.ISubTagContainer;

public class BW_WorldGenRoss128ba extends BW_OreLayer {

    public BW_WorldGenRoss128ba(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity,
        int aSize, ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between,
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
            "ore.mix.ross128ba.Tungstate",
            true,
            5,
            40,
            60,
            4,
            14,
            Ferberite,
            Huebnerit,
            Loellingit,
            Scheelite);
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
            "ore.mix.ross128ba.Amethyst",
            true,
            5,
            80,
            35,
            2,
            8,
            Amethyst,
            Olivine,
            Prasiolite,
            Hedenbergit);
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
            "ore.mix.ross128ba.RedZircon",
            true,
            10,
            80,
            40,
            3,
            24,
            Fayalit,
            FuchsitAL,
            RedZircon,
            FuchsitCR);
        new BW_WorldGenRoss128ba(
            "ore.mix.ross128ba.Fluorspar",
            true,
            10,
            80,
            35,
            4,
            8,
            Galena,
            Sphalerite,
            Fluorspar,
            Barite);
    }

    @Override
    public boolean isGenerationAllowed(String aDimName, int aDimensionType, int aAllowedDimensionType) {
        return aDimensionType == ConfigHandler.ross128BAID;
    }
}
