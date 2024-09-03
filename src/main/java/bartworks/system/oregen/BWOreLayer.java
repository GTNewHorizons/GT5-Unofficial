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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.collect.ArrayListMultimap;

import bartworks.MainMod;
import bartworks.system.material.BWMetaGeneratedOres;
import bartworks.system.material.BWMetaGeneratedSmallOres;
import bartworks.system.material.BWTileEntityMetaGeneratedOre;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.MurmurHash3;
import bartworks.util.Pair;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.world.GTWorldgen;
import gregtech.common.blocks.TileEntityOres;

/**
 * Original GT File Stripped and adjusted to work with this mod
 */
public abstract class BWOreLayer extends GTWorldgen {

    public static final List<BWOreLayer> sList = new ArrayList<>();
    public static final ArrayListMultimap<Short, BWOreLayer> NEIMAP = ArrayListMultimap.create();
    private static final boolean logOregenRoss128 = false;
    public static int sWeight;
    public byte bwOres;
    public int mMinY, mWeight, mDensity, mSize, mMaxY, mPrimaryMeta, mSecondaryMeta, mBetweenMeta, mSporadicMeta;

    public abstract Block getDefaultBlockToReplace();

    public abstract int[] getDefaultDamageToReplace();

    public abstract String getDimName();

    public BWOreLayer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize,
        ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between, ISubTagContainer sprinkled) {
        super(aName, BWOreLayer.sList, aDefault);
        this.mMinY = (short) aMinY;
        this.mMaxY = (short) aMaxY;
        this.mWeight = (short) aWeight;
        this.mDensity = (short) aDensity;
        this.mSize = (short) Math.max(1, aSize);

        if (this.mEnabled) BWOreLayer.sWeight += this.mWeight;

        if (top instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b1000);
        if (bottom instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0100);
        if (between instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0010);
        if (sprinkled instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0001);

        short aPrimary = top instanceof Materials ? (short) ((Materials) top).mMetaItemSubID
            : top instanceof Werkstoff ? ((Werkstoff) top).getmID() : 0;
        short aSecondary = bottom instanceof Materials ? (short) ((Materials) bottom).mMetaItemSubID
            : bottom instanceof Werkstoff ? ((Werkstoff) bottom).getmID() : 0;
        short aBetween = between instanceof Materials ? (short) ((Materials) between).mMetaItemSubID
            : between instanceof Werkstoff ? ((Werkstoff) between).getmID() : 0;
        short aSporadic = sprinkled instanceof Materials ? (short) ((Materials) sprinkled).mMetaItemSubID
            : sprinkled instanceof Werkstoff ? ((Werkstoff) sprinkled).getmID() : 0;
        this.mPrimaryMeta = aPrimary;
        this.mSecondaryMeta = aSecondary;
        this.mBetweenMeta = aBetween;
        this.mSporadicMeta = aSporadic;
        NEIMAP.put((short) this.mPrimaryMeta, this);
        NEIMAP.put((short) this.mSecondaryMeta, this);
        NEIMAP.put((short) this.mBetweenMeta, this);
        NEIMAP.put((short) this.mSporadicMeta, this);
    }

    public List<ItemStack> getStacks() {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(
            (this.bwOres & 0b1000) != 0 ? new ItemStack(WerkstoffLoader.BWOres, 1, this.mPrimaryMeta)
                : new ItemStack(GregTechAPI.sBlockOres1, 1, this.mPrimaryMeta));
        ret.add(
            (this.bwOres & 0b0100) != 0 ? new ItemStack(WerkstoffLoader.BWOres, 1, this.mSecondaryMeta)
                : new ItemStack(GregTechAPI.sBlockOres1, 1, this.mSecondaryMeta));
        ret.add(
            (this.bwOres & 0b0010) != 0 ? new ItemStack(WerkstoffLoader.BWOres, 1, this.mBetweenMeta)
                : new ItemStack(GregTechAPI.sBlockOres1, 1, this.mBetweenMeta));
        ret.add(
            (this.bwOres & 0b0001) != 0 ? new ItemStack(WerkstoffLoader.BWOres, 1, this.mSporadicMeta)
                : new ItemStack(GregTechAPI.sBlockOres1, 1, this.mSporadicMeta));
        return ret;
    }

    public List<Pair<Integer, Boolean>> getStacksRawData() {
        ArrayList<Pair<Integer, Boolean>> ret = new ArrayList<>();
        ret.add(new Pair<>(this.mPrimaryMeta, (this.bwOres & 0b1000) != 0));
        ret.add(new Pair<>(this.mSecondaryMeta, (this.bwOres & 0b0100) != 0));
        ret.add(new Pair<>(this.mBetweenMeta, (this.bwOres & 0b0010) != 0));
        ret.add(new Pair<>(this.mSporadicMeta, (this.bwOres & 0b0001) != 0));
        return ret;
    }

    @Override
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        {
            int tMinY = this.mMinY + aRandom.nextInt(this.mMaxY - this.mMinY - 5);
            int cX = aChunkX - aRandom.nextInt(this.mSize);
            int eX = aChunkX + 16 + aRandom.nextInt(this.mSize);

            boolean wasPlaced = false;

            for (int tX = cX; tX <= eX; ++tX) {
                int cZ = aChunkZ - aRandom.nextInt(this.mSize);
                int eZ = aChunkZ + 16 + aRandom.nextInt(this.mSize);

                for (int tZ = cZ; tZ <= eZ; ++tZ) {
                    int i;
                    if (this.mSecondaryMeta > 0) {
                        for (i = tMinY - 1; i < tMinY + 2; ++i) {
                            if (this.shouldPlace(aRandom, cX, eX, tX, cZ, eZ, tZ)) {
                                wasPlaced = this.setOreBlock(aWorld, tX, i, tZ, this.mSecondaryMeta, false);
                            }
                        }
                    }

                    if (this.mBetweenMeta > 0 && this.shouldPlace(aRandom, cX, eX, tX, cZ, eZ, tZ)) {
                        wasPlaced = this
                            .setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenMeta, false);
                    }

                    if (this.mPrimaryMeta > 0) {
                        for (i = tMinY + 3; i < tMinY + 6; ++i) {
                            if (this.shouldPlace(aRandom, cX, eX, tX, cZ, eZ, tZ)) {
                                wasPlaced = this.setOreBlock(aWorld, tX, i, tZ, this.mPrimaryMeta, false);
                            }
                        }
                    }

                    if (this.mSporadicMeta > 0 && this.shouldPlace(aRandom, cX, eX, tX, cZ, eZ, tZ)) {
                        wasPlaced = this
                            .setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicMeta, false);
                    }
                }
            }

            if (BWOreLayer.logOregenRoss128) {
                MainMod.LOGGER.info("Generated Orevein: " + this.mWorldGenName + " " + aChunkX + " " + aChunkZ);
            }

            return wasPlaced;
        }
    }

    private boolean shouldPlace(Random aRandom, int cX, int eX, int tX, int cZ, int eZ, int tZ) {
        if (aRandom.nextInt(
            Math.max(1, Math.max(MathHelper.abs_int(cZ - tZ), MathHelper.abs_int(eZ - tZ)) / this.mDensity)) == 0
            || aRandom.nextInt(
                Math.max(1, Math.max(MathHelper.abs_int(cX - tX), MathHelper.abs_int(eX - tX)) / this.mDensity)) == 0)
            return true;
        return false;
    }

    public boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre) {
        // security stuff to prevent crashes with 2 TileEntites on the same Spot
        TileEntity te = aWorld.getTileEntity(aX, aY, aZ);
        if (te instanceof BWTileEntityMetaGeneratedOre || te instanceof TileEntityOres) return true;

        if (aMetaData == this.mSporadicMeta && (this.bwOres & 0b0001) != 0
            || aMetaData == this.mBetweenMeta && (this.bwOres & 0b0010) != 0
            || aMetaData == this.mPrimaryMeta && (this.bwOres & 0b1000) != 0
            || aMetaData == this.mSecondaryMeta && (this.bwOres & 0b0100) != 0) {
            return isSmallOre
                ? BWMetaGeneratedSmallOres.setOreBlock(
                    aWorld,
                    aX,
                    aY,
                    aZ,
                    aMetaData,
                    false,
                    this.getDefaultBlockToReplace(),
                    this.getDefaultDamageToReplace())
                : BWMetaGeneratedOres.setOreBlock(
                    aWorld,
                    aX,
                    aY,
                    aZ,
                    aMetaData,
                    false,
                    this.getDefaultBlockToReplace(),
                    this.getDefaultDamageToReplace());
        }

        return this.setGTOreBlockSpace(aWorld, aX, aY, aZ, aMetaData, this.getDefaultBlockToReplace());
    }

    public boolean setGTOreBlockSpace(World aWorld, int aX, int aY, int aZ, int aMetaData, Block block) {
        if (TileEntityOres.setOreBlock(aWorld, aX, aY, aZ, aMetaData, false, false)) return true;
        aY = Math.min(aWorld.getActualHeight(), Math.max(aY, 1));
        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = GregTechAPI.sBlockOres1;
        if (aMetaData < 0 || tBlock == Blocks.air) {
            return false;
        } else {
            if (!tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, block)) {
                return false;
            }
            aMetaData += 5000;
            aWorld.setBlock(aX, aY, aZ, tOreBlock, aMetaData, 0);
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity instanceof TileEntityOres ore) {
                ore.mMetaData = (short) aMetaData;
            }
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BWOreLayer that)) return false;

        if (this.bwOres != that.bwOres || this.mMinY != that.mMinY
            || this.mWeight != that.mWeight
            || this.mDensity != that.mDensity) return false;
        if (this.mSize != that.mSize) return false;
        if (this.mMaxY != that.mMaxY) return false;
        if (this.mPrimaryMeta != that.mPrimaryMeta) return false;
        if (this.mSecondaryMeta != that.mSecondaryMeta) return false;
        if (this.mBetweenMeta != that.mBetweenMeta) return false;
        return this.mSporadicMeta == that.mSporadicMeta;
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
            ByteBuffer.allocate(37)
                .put(this.bwOres)
                .putInt(this.mMinY)
                .putInt(this.mWeight)
                .putInt(this.mDensity)
                .putInt(this.mSize)
                .putInt(this.mMaxY)
                .putInt(this.mPrimaryMeta)
                .putInt(this.mSecondaryMeta)
                .putInt(this.mBetweenMeta)
                .putInt(this.mSporadicMeta)
                .array(),
            0,
            37,
            31);
    }
}
