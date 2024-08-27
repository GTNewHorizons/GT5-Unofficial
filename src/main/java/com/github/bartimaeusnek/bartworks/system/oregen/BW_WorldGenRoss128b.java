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

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Arsenopyrite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Bismuthinit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Bismutite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Bornite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.ChromoAluminoPovondrait;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.DescloiziteCUVO4;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.DescloiziteZNVO4;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Djurleit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Fayalit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Ferberite;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FluorBuergerit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Forsterit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FuchsitAL;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FuchsitCR;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Hedenbergit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Huebnerit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Loellingit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Olenit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RedZircon;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Roquesit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Thorianit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.VanadioOxyDravit;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Wittichenit;
import static gregtech.api.enums.Materials.Bismuth;
import static gregtech.api.enums.Materials.Coal;
import static gregtech.api.enums.Materials.Diamond;
import static gregtech.api.enums.Materials.Graphite;
import static gregtech.api.enums.Materials.Lepidolite;
import static gregtech.api.enums.Materials.Scheelite;
import static gregtech.api.enums.Materials.Spodumene;
import static gregtech.api.enums.Materials.Stibnite;
import static gregtech.api.enums.Materials.Tetrahedrite;
import static gregtech.api.enums.Materials.Uraninite;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;

import gregtech.api.interfaces.ISubTagContainer;

public class BW_WorldGenRoss128b extends BW_OreLayer {

    @Override
    public Block getDefaultBlockToReplace() {
        return Blocks.stone;
    }

    @Override
    public int[] getDefaultDamageToReplace() {
        return new int[] { 0 };
    }

    @Override
    public String getDimName() {
        return StatCollector.translateToLocal("planet.Ross128b");
    }

    public BW_WorldGenRoss128b(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity,
        int aSize, ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between,
        ISubTagContainer sprinkled) {
        super(aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, top, bottom, between, sprinkled);
    }

    public static void initOres() {
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.Thorianit",
            true,
            30,
            60,
            17,
            1,
            16,
            Thorianit,
            Uraninite,
            Lepidolite,
            Spodumene);
        new BW_WorldGenRoss128b("ore.mix.ross128.carbon", true, 5, 25, 5, 4, 12, Graphite, Diamond, Coal, Graphite);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.bismuth",
            true,
            5,
            80,
            30,
            1,
            16,
            Bismuthinit,
            Stibnite,
            Bismuth,
            Bismutite);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.TurmalinAlkali",
            true,
            5,
            80,
            15,
            4,
            48,
            Olenit,
            FluorBuergerit,
            ChromoAluminoPovondrait,
            VanadioOxyDravit);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.Roquesit",
            true,
            30,
            50,
            3,
            1,
            12,
            Arsenopyrite,
            Ferberite,
            Loellingit,
            Roquesit);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.Tungstate",
            true,
            5,
            40,
            10,
            4,
            14,
            Ferberite,
            Huebnerit,
            Loellingit,
            Scheelite);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.CopperSulfits",
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
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.Forsterit",
            true,
            20,
            90,
            50,
            2,
            32,
            Forsterit,
            Fayalit,
            DescloiziteCUVO4,
            DescloiziteZNVO4);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.Hedenbergit",
            true,
            20,
            90,
            50,
            2,
            32,
            Hedenbergit,
            Fayalit,
            DescloiziteCUVO4,
            DescloiziteZNVO4);
        new BW_WorldGenRoss128b(
            "ore.mix.ross128.RedZircon",
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
    }

    @Override
    public boolean isGenerationAllowed(String aDimName, int aDimensionType, int aAllowedDimensionType) {
        return aDimensionType == ConfigHandler.ross128BID;
    }
}
