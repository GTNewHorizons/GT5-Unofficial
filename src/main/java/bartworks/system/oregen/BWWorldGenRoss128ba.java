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

package bartworks.system.oregen;

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

import bartworks.common.configs.ConfigHandler;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.interfaces.ISubTagContainer;

public class BWWorldGenRoss128ba extends BWOreLayer {

    public BWWorldGenRoss128ba(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity,
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
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.tib",
            true,
            30,
            60,
            6,
            1,
            16,
            WerkstoffLoader.Tiberium,
            WerkstoffLoader.Tiberium,
            NaquadahEnriched,
            NaquadahEnriched);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.Tungstate",
            true,
            5,
            40,
            60,
            4,
            14,
            WerkstoffLoader.Ferberite,
            WerkstoffLoader.Huebnerit,
            WerkstoffLoader.Loellingit,
            Scheelite);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.bart",
            true,
            30,
            60,
            1,
            1,
            1,
            WerkstoffLoader.BArTiMaEuSNeK,
            WerkstoffLoader.BArTiMaEuSNeK,
            WerkstoffLoader.BArTiMaEuSNeK,
            WerkstoffLoader.BArTiMaEuSNeK);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.TurmalinAlkali",
            true,
            5,
            80,
            60,
            4,
            48,
            WerkstoffLoader.Olenit,
            WerkstoffLoader.FluorBuergerit,
            WerkstoffLoader.ChromoAluminoPovondrait,
            WerkstoffLoader.VanadioOxyDravit);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.Amethyst",
            true,
            5,
            80,
            35,
            2,
            8,
            Amethyst,
            Olivine,
            WerkstoffLoader.Prasiolite,
            WerkstoffLoader.Hedenbergit);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.CopperSulfits",
            true,
            40,
            70,
            80,
            3,
            24,
            WerkstoffLoader.Djurleit,
            WerkstoffLoader.Bornite,
            WerkstoffLoader.Wittichenit,
            Tetrahedrite);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.RedZircon",
            true,
            10,
            80,
            40,
            3,
            24,
            WerkstoffLoader.Fayalit,
            WerkstoffLoader.FuchsitAL,
            WerkstoffLoader.RedZircon,
            WerkstoffLoader.FuchsitCR);
        new BWWorldGenRoss128ba(
            "ore.mix.ross128ba.Fluorspar",
            true,
            10,
            80,
            35,
            4,
            8,
            Galena,
            Sphalerite,
            WerkstoffLoader.Fluorspar,
            Barite);
    }

    @Override
    public boolean isGenerationAllowed(String aDimName, int aDimensionType, int aAllowedDimensionType) {
        return aDimensionType == ConfigHandler.ross128BAID;
    }
}
