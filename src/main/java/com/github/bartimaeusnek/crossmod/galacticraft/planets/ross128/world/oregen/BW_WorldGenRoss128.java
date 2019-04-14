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

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128.world.oregen;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import static com.github.bartimaeusnek.crossmod.galacticraft.GalacticraftProxy.uo_dimensionList;


public class BW_WorldGenRoss128 extends BW_OreLayer {


    public BW_WorldGenRoss128(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between, ISubTagContainer sprinkled) {
        super(aName, aDefault, aMinY, aMaxY, aWeight, aDensity, aSize, top, bottom, between, sprinkled);
    }

    public static void init_OresRoss128() {
        new BW_WorldGenRoss128("ore.mix.ross128.Thorianit", true, 30, 60, 17, 1, 16, WerkstoffLoader.Thorianit, Materials.Uraninite, Materials.Lepidolite, Materials.Spodumene);
        new BW_WorldGenRoss128("ore.mix.ross128.carbon", true, 5, 25, 5, 4, 12, Materials.Graphite, Materials.Diamond, Materials.Coal, Materials.Graphite);
        new BW_WorldGenRoss128("ore.mix.ross128.bismuth", true, 5, 80, 30, 1, 16, WerkstoffLoader.Bismuthinit, Materials.Stibnite, Materials.Bismuth, WerkstoffLoader.Bismutite);
        new BW_WorldGenRoss128("ore.mix.ross128.TurmalinAlkali", true, 5, 200, 15, 4, 48, WerkstoffLoader.Olenit, WerkstoffLoader.FluorBuergerit, WerkstoffLoader.ChromoAluminoPovondrait, WerkstoffLoader.VanadioOxyDravit);
        new BW_WorldGenRoss128("ore.mix.ross128.Roquesit", true, 5, 250, 3, 1, 12, WerkstoffLoader.Arsenopyrite, WerkstoffLoader.Ferberite, WerkstoffLoader.Loellingit, WerkstoffLoader.Roquesit);
        new BW_WorldGenRoss128("ore.mix.ross128.Tungstate", true, 5, 250, 10, 4, 14, WerkstoffLoader.Ferberite, WerkstoffLoader.Huebnerit, WerkstoffLoader.Loellingit, Materials.Scheelite);
        new BW_WorldGenRoss128("ore.mix.ross128.CopperSulfits", true, 40, 70, 80, 3, 24, WerkstoffLoader.Djurleit, WerkstoffLoader.Bornite, WerkstoffLoader.Wittichenit, Materials.Tetrahedrite);
        new BW_WorldGenRoss128("ore.mix.ross128.Forsterit", true, 20, 180, 50, 2, 32, WerkstoffLoader.Forsterit, WerkstoffLoader.Fayalit, WerkstoffLoader.DescloiziteCUVO4, WerkstoffLoader.DescloiziteZNVO4);
        new BW_WorldGenRoss128("ore.mix.ross128.Hedenbergit", true, 20, 180, 50, 2, 32, WerkstoffLoader.Hedenbergit, WerkstoffLoader.Fayalit, WerkstoffLoader.DescloiziteCUVO4, WerkstoffLoader.DescloiziteZNVO4);
        new BW_WorldGenRoss128("ore.mix.ross128.RedZircon", true, 10, 40, 40, 3, 24, WerkstoffLoader.Fayalit,WerkstoffLoader.FuchsitAL , WerkstoffLoader.RedZircon,WerkstoffLoader.FuchsitCR);
    }

    public static void init_undergroundFluidsRoss128() {
        String ross128b = StatCollector.translateToLocal("planet.Ross128b");
        uo_dimensionList.SetConfigValues(ross128b, ross128b, "veryheavyoil", "liquid_extra_heavy_oil", 0, 625, 40, 5);
        uo_dimensionList.SetConfigValues(ross128b, ross128b, "lava", FluidRegistry.getFluidName(FluidRegistry.LAVA), 0, 32767, 5, 5);
        uo_dimensionList.SetConfigValues(ross128b, ross128b, "gas_natural_gas", "gas_natural_gas", 0, 625, 65, 5);

    }

    @Override
    public boolean isGenerationAllowed(World aWorld, int aDimensionType, int aAllowedDimensionType) {
        return aWorld.provider.dimensionId == ConfigHandler.ross128ID;
    }

}
