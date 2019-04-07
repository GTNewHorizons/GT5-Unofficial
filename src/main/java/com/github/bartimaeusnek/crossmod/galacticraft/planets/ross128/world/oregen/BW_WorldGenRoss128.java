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

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Ores;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128.world.worldprovider.WorldProviderRoss128b;
import com.github.bartimaeusnek.crossmod.galacticraft.solarsystems.Ross128;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.bartimaeusnek.crossmod.galacticraft.GalacticraftProxy.uo_dimensionList;

/**
 * Original GT File Stripped and adjusted to work with this mod
 */
public class BW_WorldGenRoss128 extends GT_Worldgen {

    private static final boolean logOregenRoss128 = false;

    public static void init_OresRoss128() {
        new BW_WorldGenRoss128("ore.mix.ross128.Thorianit", true, 30, 60, 17, 1, 16, WerkstoffLoader.Thorianit, Materials.Uraninite, Materials.Lepidolite, Materials.Spodumene);
        new BW_WorldGenRoss128("ore.mix.ross128.carbon", true, 5, 25, 5, 4, 12, Materials.Graphite, Materials.Diamond, Materials.Coal, Materials.Graphene);
        new BW_WorldGenRoss128("ore.mix.ross128.bismuth", true, 5, 80, 30, 1, 16, WerkstoffLoader.Bismuthinit, Materials.Stibnite, Materials.Bismuth, WerkstoffLoader.Bismutite);
        new BW_WorldGenRoss128("ore.mix.ross128.TurmalinAlkali", true, 5, 200, 15, 4, 48, WerkstoffLoader.Olenit, WerkstoffLoader.FluorBuergerit, WerkstoffLoader.ChromoAluminoPovondrait, WerkstoffLoader.VanadioOxyDravit);
        new BW_WorldGenRoss128("ore.mix.ross128.Roquesit", true, 5, 250, 3, 1, 12, WerkstoffLoader.Arsenopyrite, WerkstoffLoader.Ferberite, WerkstoffLoader.Loellingit, WerkstoffLoader.Roquesit);
        new BW_WorldGenRoss128("ore.mix.ross128.Tungstate", true, 5, 250, 10, 4, 14, WerkstoffLoader.Ferberite, WerkstoffLoader.Huebnerit, WerkstoffLoader.Loellingit, Materials.Scheelite);
        new BW_WorldGenRoss128("ore.mix.ross128.CopperSulfits", true, 40, 70, 80, 3, 24, WerkstoffLoader.Djurleit, WerkstoffLoader.Bornite, WerkstoffLoader.Wittichenit, Materials.Tetrahedrite);
        new BW_WorldGenRoss128("ore.mix.ross128.magnetite", true, 60, 180, 50, 2, 32, Materials.Magnetite, Materials.Magnetite, Materials.Iron, Materials.VanadiumMagnetite);
        new BW_WorldGenRoss128("ore.mix.ross128.gold", true, 30, 60, 50, 2, 32, Materials.Magnetite, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Gold);
        new BW_WorldGenRoss128("ore.mix.ross128.iron", true, 10, 40, 40, 3, 24, Materials.BrownLimonite, Materials.YellowLimonite, Materials.BandedIron, Materials.Malachite);
    }

    public static void init_undergroundFluidsRoss128(){
        String ross128b=StatCollector.translateToLocal("planet.Ross128b");
        uo_dimensionList.SetConfigValues(ross128b,ross128b,"veryheavyoil","liquid_extra_heavy_oil",0,625,40,5);
        uo_dimensionList.SetConfigValues(ross128b,ross128b,"lava", FluidRegistry.getFluidName(FluidRegistry.LAVA),0,80000,5,5);
        uo_dimensionList.SetConfigValues(ross128b,ross128b,"gas_natural_gas", "gas_natural_gas",0,625,65,5);

    }

    public byte bwOres = 0b0000;
    public int mMinY,mWeight,mDensity,mSize,mMaxY,mPrimaryMeta,mSecondaryMeta,mBetweenMeta,mSporadicMeta;
    public static int sWeight;
    public static final List<GT_Worldgen> sList = new ArrayList<>();

    public BW_WorldGenRoss128(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between, ISubTagContainer sprinkled) {
        super(aName, sList, aDefault);
        this.mMinY = (short) aMinY;
        this.mMaxY = (short) aMaxY;
        this.mWeight = (short)aWeight;
        this.mDensity = (short) aDensity;
        this.mSize = (short) Math.max(1, aSize);

        if (mEnabled)
            sWeight += this.mWeight;

        if (top instanceof Werkstoff)
            bwOres = (byte) (bwOres | 0b1000);
        if (bottom instanceof Werkstoff)
            bwOres = (byte) (bwOres | 0b0100);
        if (between instanceof Werkstoff)
            bwOres = (byte) (bwOres | 0b0010);
        if (sprinkled instanceof Werkstoff)
            bwOres = (byte) (bwOres | 0b0001);

        short aPrimary = top instanceof Materials ?
                (short) ((Materials)top).mMetaItemSubID:
                top instanceof Werkstoff ?
                        (short) ((Werkstoff)top).getmID():
                        0
                ;
        short aSecondary = bottom instanceof Materials ?
                (short) ((Materials)bottom).mMetaItemSubID:
                bottom instanceof Werkstoff ?
                        (short) ((Werkstoff)bottom).getmID():
                        0
                ;
        short aBetween = between instanceof Materials ?
                (short) ((Materials)between).mMetaItemSubID:
                between instanceof Werkstoff ?
                        (short) ((Werkstoff)between).getmID():
                        0
                ;
        short aSporadic = sprinkled instanceof Materials ?
                (short) ((Materials)sprinkled).mMetaItemSubID:
                sprinkled instanceof Werkstoff ?
                        (short) ((Werkstoff)sprinkled).getmID():
                        0
                ;
        this.mPrimaryMeta = (short) aPrimary;
        this.mSecondaryMeta = (short) aSecondary;
        this.mBetweenMeta = (short) aBetween;
        this.mSporadicMeta = (short) aSporadic;

    }

    @Override
    public boolean isGenerationAllowed(World aWorld, int aDimensionType, int aAllowedDimensionType) {
        return aWorld.provider instanceof WorldProviderRoss128b;
    }

    @Override
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
      {
            int tMinY = this.mMinY + aRandom.nextInt(this.mMaxY - this.mMinY - 5);
            int cX = aChunkX - aRandom.nextInt(this.mSize);
            int eX = aChunkX + 16 + aRandom.nextInt(this.mSize);

            for(int tX = cX; tX <= eX; ++tX) {
                int cZ = aChunkZ - aRandom.nextInt(this.mSize);
                int eZ = aChunkZ + 16 + aRandom.nextInt(this.mSize);

                for(int tZ = cZ; tZ <= eZ; ++tZ) {
                    int i;
                    if (this.mSecondaryMeta > 0) {
                        for(i = tMinY - 1; i < tMinY + 2; ++i) {
                            if (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0 || aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0) {
                                setOreBlock(aWorld, tX, i, tZ, this.mSecondaryMeta, false);
                            }
                        }
                    }

                    if (this.mBetweenMeta > 0 && (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0 || aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0)) {
                        setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenMeta, false);
                    }

                    if (this.mPrimaryMeta > 0) {
                        for(i = tMinY + 3; i < tMinY + 6; ++i) {
                            if (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0 || aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0) {
                                setOreBlock(aWorld, tX, i, tZ, this.mPrimaryMeta, false);
                            }
                        }
                    }

                    if (this.mSporadicMeta > 0 && (aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0 || aRandom.nextInt(Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0)) {
                        setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicMeta, false);
                    }
                }
            }

            if (logOregenRoss128) {
                MainMod.LOGGER.info("Generated Orevein: " + this.mWorldGenName + " " + aChunkX + " " + aChunkZ);
            }

            return true;
        }
    }

    public boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre) {
        if ((aMetaData == mSporadicMeta && (bwOres & 0b0001) != 0) || (aMetaData == mBetweenMeta && (bwOres & 0b0010) != 0) || (aMetaData == mPrimaryMeta && (bwOres & 0b1000) != 0) || (aMetaData == mSecondaryMeta && (bwOres & 0b0100) != 0)){
            return BW_MetaGenerated_Ores.setOreBlock(aWorld, aX, aY, aZ, aMetaData, false);
        }
        return GT_TileEntity_Ores.setOreBlock(aWorld, aX, aY, aZ, aMetaData, isSmallOre, false);
    }
    
}
