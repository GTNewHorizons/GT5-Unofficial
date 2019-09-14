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

package com.github.bartimaeusnek.bartworks.system.oregen;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static com.github.bartimaeusnek.crossmod.galacticraft.GalacticraftProxy.uo_dimensionList;
import static gregtech.api.enums.Materials.*;

public class BW_WorldGenRoss128ba extends BW_OreLayer  {

    public BW_WorldGenRoss128ba(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between, ISubTagContainer sprinkled) {
        super(aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, top, bottom, between, sprinkled);
    }

    @Override
    public Block getDefaultBlockToReplace() {
        return Block.getBlockFromItem(GT_ModHandler.getModItem("galacticraftCore","tile.moonBlock",1).getItem());
    }

    public static void init_OresRoss128ba() {
        new BW_WorldGenRoss128ba("ore.mix.ross128ba.tib", true, 30, 60, 6, 1, 16, Tiberium, Tiberium, NaquadahEnriched, NaquadahEnriched);
        new BW_WorldGenRoss128ba("ore.mix.ross128.Tungstate", true, 5, 40, 10, 4, 14, Ferberite, Huebnerit, Loellingit, Scheelite);
        new BW_WorldGenRoss128ba("ore.mix.ross128ba.bart", true, 30, 60, 1, 1, 3, BArTiMaEuSNeK, BArTiMaEuSNeK, BArTiMaEuSNeK, BArTiMaEuSNeK);

    }

    public static void init_undergroundFluidsRoss128ba() {
        String ross128b = StatCollector.translateToLocal("planet.Ross128ba");
        uo_dimensionList.SetConfigValues(ross128b, ross128b, Materials.SaltWater.getFluid(1).getUnlocalizedName(), Materials.SaltWater.getFluid(1).getUnlocalizedName(), 0, 625, 40, 5);
        uo_dimensionList.SetConfigValues(ross128b, ross128b, Materials.Helium_3.getGas(1).getUnlocalizedName(), Materials.Helium_3.getGas(1).getUnlocalizedName(), 0, 625, 60, 5);

    }
}
