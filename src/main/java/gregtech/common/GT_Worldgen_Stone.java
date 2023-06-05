package gregtech.common;

import static gregtech.api.enums.GT_Values.debugStones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import gregtech.api.GregTech_API;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen_Ore;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;

public class GT_Worldgen_Stone extends GT_Worldgen_Ore {

    static final double[] sizeConversion = { 1, 1, 1.333333, 1.333333, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 }; // Bias
                                                                                                                         // the
                                                                                                                         // sizes
                                                                                                                         // towards
                                                                                                                         // skinnier
                                                                                                                         // boulders,
                                                                                                                         // ie
                                                                                                                         // more
                                                                                                                         // "shafts"
                                                                                                                         // than
                                                                                                                         // dikes
                                                                                                                         // or
                                                                                                                         // sills.

    public Hashtable<Long, StoneSeeds> validStoneSeeds = new Hashtable<>(1024);

    static class StoneSeeds {

        public boolean mExists;

        StoneSeeds(boolean exists) {
            mExists = exists;
        }
    }

    static class ValidSeeds {

        public int mX;
        public int mZ;

        ValidSeeds(int x, int z) {
            this.mX = x;
            this.mZ = z;
        }
    }

    public GT_Worldgen_Stone(String aName, boolean aDefault, Block aBlock, int aBlockMeta, int aDimensionType,
        int aAmount, int aSize, int aProbability, int aMinY, int aMaxY, Collection<String> aBiomeList,
        boolean aAllowToGenerateinVoid) {
        super(
            aName,
            aDefault,
            aBlock,
            aBlockMeta,
            aDimensionType,
            aAmount,
            aSize,
            aProbability,
            aMinY,
            aMaxY,
            aBiomeList,
            aAllowToGenerateinVoid);
    }

    @Override
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        XSTR stoneRNG = new XSTR();
        ArrayList<ValidSeeds> stones = new ArrayList<>();

        if (!isGenerationAllowed(aWorld, aDimensionType, this.mDimensionType)) {
            return false;
        }
        if (!(this.mBiomeList.isEmpty() || this.mBiomeList.contains(aBiome))) {
            return false;
        }
        // I think the real size of the balls is mSize/8, but the original code was difficult to understand.
        // Overall there will be less GT stones since they aren't spheres any more. /16 since this code uses it as a
        // radius.
        double realSize = mSize / 16;
        int windowWidth = ((int) realSize) / 16 + 1; // Width of chunks to check for a potential stoneseed
        // Check stone seeds to see if they have been added
        for (int x = aChunkX / 16 - windowWidth; x < (aChunkX / 16 + windowWidth + 1); x++) {
            for (int z = aChunkZ / 16 - windowWidth; z < (aChunkZ / 16 + windowWidth + 1); z++) {
                long hash = (((aWorld.provider.dimensionId & 0xffL) << 56) | (((long) x & 0x000000000fffffffL) << 28)
                    | ((long) z & 0x000000000fffffffL));
                if (!validStoneSeeds.containsKey(hash)) {
                    // Determine if RNG says to add stone at this chunk
                    stoneRNG.setSeed(
                        aWorld.getSeed() ^ hash + Math.abs(mBlockMeta)
                            + Math.abs(mSize)
                            + ((GregTech_API.sBlockGranites == mBlock) ? (32768) : (0))); // Don't judge me.
                                                                                          // Want different
                                                                                          // values for
                                                                                          // different block
                                                                                          // types
                    if ((this.mProbability <= 1) || (stoneRNG.nextInt(this.mProbability) == 0)) {
                        // Add stone at this chunk
                        validStoneSeeds.put(hash, new StoneSeeds(true));
                        // Add to generation list
                        stones.add(new ValidSeeds(x, z));
                        if (debugStones) GT_Log.out.println(
                            "New stoneseed=" + mWorldGenName + " x=" + x + " z=" + z + " realSize=" + realSize);
                    } else {
                        validStoneSeeds.put(hash, new StoneSeeds(false));
                    }
                } else {
                    // This chunk has already been checked, check to see if a boulder exists here
                    if (validStoneSeeds.get(hash).mExists) {
                        // Add to generation list
                        stones.add(new ValidSeeds(x, z));
                    }
                }
            }
        }

        boolean result = stones.size() != 0;
        // Now process each oreseed vs this requested chunk
        for (; stones.size() != 0; stones.remove(0)) {
            int x = stones.get(0).mX * 16;
            int z = stones.get(0).mZ * 16;

            stoneRNG.setSeed(
                aWorld.getSeed()
                    ^ (((aWorld.provider.dimensionId & 0xffL) << 56) | (((long) x & 0x000000000fffffffL) << 28)
                        | ((long) z & 0x000000000fffffffL)) + Math.abs(mBlockMeta)
                        + Math.abs(mSize)
                        + ((GregTech_API.sBlockGranites == mBlock) ? (32768) : (0))); // Don't judge me
            for (int i = 0; i < this.mAmount; i++) { // Not sure why you would want more than one in a chunk! Left alone
                                                     // though.
                // Locate the stoneseed XYZ. Original code would request an isAir at the seed location, causing a chunk
                // generation request.
                // To reduce potential worldgen cascade, we just always try to place a ball and use the check inside the
                // for loop to prevent
                // placement instead.
                int tX = x + stoneRNG.nextInt(16);
                int tY = mMinY + stoneRNG.nextInt(mMaxY - mMinY);
                int tZ = z + stoneRNG.nextInt(16);

                // Determine the XYZ sizes of the stoneseed
                double xSize = sizeConversion[stoneRNG.nextInt(sizeConversion.length)];
                double ySize = sizeConversion[stoneRNG.nextInt(sizeConversion.length) / 2]; // Skew the ySize towards
                                                                                            // the larger sizes, more
                                                                                            // long skinny pipes
                double zSize = sizeConversion[stoneRNG.nextInt(sizeConversion.length)];

                // Equation for an ellipsoid centered around 0,0,0
                // Sx, Sy, and Sz are size controls (size = 1/S_)
                // 1 = full size, 1.333 = 75%, 2 = 50%, 4 = 25%
                // (x * Sx)^2 + (y * Sy)^2 + (z * sZ)^2 <= (mSize)^2

                // So, we setup the intial boundaries to be the size of the boulder plus a block in each direction
                int tMinX = tX - (int) (realSize / xSize - 1.0);
                int tMaxX = tX + (int) (realSize / xSize + 2.0);
                int tMinY = tY - (int) (realSize / ySize - 1.0);
                int tMaxY = tY + (int) (realSize / ySize + 2.0);
                int tMinZ = tZ - (int) (realSize / zSize - 1.0);
                int tMaxZ = tZ + (int) (realSize / zSize + 2.0);

                // If the (tY-ySize) of the stoneseed is air in the current chunk, mark the seed empty and move on.
                if (aWorld.getBlock(aChunkX + 8, tMinY, aChunkZ + 8)
                    .isAir(aWorld, aChunkX + 8, tMinY, aChunkZ + 8)) {
                    if (debugStones) GT_Log.out.println(
                        mWorldGenName + " tX="
                            + tX
                            + " tY="
                            + tY
                            + " tZ="
                            + tZ
                            + " realSize="
                            + realSize
                            + " xSize="
                            + realSize / xSize
                            + " ySize="
                            + realSize / ySize
                            + " zSize="
                            + realSize / zSize
                            + " tMinY="
                            + tMinY
                            + " tMaxY="
                            + tMaxY
                            + " - Skipped because first requesting chunk would not contain this stone");
                    long hash = (((aWorld.provider.dimensionId & 0xffL) << 56)
                        | (((long) x & 0x000000000fffffffL) << 28)
                        | ((long) z & 0x000000000fffffffL));
                    validStoneSeeds.remove(hash);
                    validStoneSeeds.put(hash, new StoneSeeds(false));
                }

                // Chop the boundaries by the parts that intersect with the current chunk
                int wX = Math.max(tMinX, aChunkX + 8);
                int eX = Math.min(tMaxX, aChunkX + 8 + 16);

                int sZ = Math.max(tMinZ, aChunkZ + 8);
                int nZ = Math.min(tMaxZ, aChunkZ + 8 + 16);

                if (debugStones) GT_Log.out.println(
                    mWorldGenName + " tX="
                        + tX
                        + " tY="
                        + tY
                        + " tZ="
                        + tZ
                        + " realSize="
                        + realSize
                        + " xSize="
                        + realSize / xSize
                        + " ySize="
                        + realSize / ySize
                        + " zSize="
                        + realSize / zSize
                        + " wX="
                        + wX
                        + " eX="
                        + eX
                        + " tMinY="
                        + tMinY
                        + " tMaxY="
                        + tMaxY
                        + " sZ="
                        + sZ
                        + " nZ="
                        + nZ);

                double rightHandSide = realSize * realSize + 1; // Precalc the right hand side
                for (int iY = tMinY; iY < tMaxY; iY++) { // Do placement from the bottom up layer up. Maybe better on
                                                         // cache usage?
                    double yCalc = ((double) (iY - tY) * ySize);
                    yCalc = yCalc * yCalc; // (y*Sy)^2
                    double leftHandSize = yCalc;
                    if (leftHandSize > rightHandSide) {
                        continue; // If Y alone is larger than the RHS, skip the rest of the loops
                    }
                    for (int iX = wX; iX < eX; iX++) {
                        double xCalc = ((double) (iX - tX) * xSize);
                        xCalc = xCalc * xCalc;
                        leftHandSize = yCalc + xCalc;
                        if (leftHandSize > rightHandSide) { // Again, if X and Y is larger than the RHS, skip to the
                                                            // next value
                            continue;
                        }
                        for (int iZ = sZ; iZ < nZ; iZ++) {
                            double zCalc = ((double) (iZ - tZ) * zSize);
                            zCalc = zCalc * zCalc;
                            leftHandSize = zCalc + xCalc + yCalc;
                            if (leftHandSize <= rightHandSide) {
                                // Yay! We can actually place a block now. (this part copied from original code)
                                Block tTargetedBlock = aWorld.getBlock(iX, iY, iZ);
                                if (tTargetedBlock instanceof GT_Block_Ores_Abstract) {
                                    TileEntity tTileEntity = aWorld.getTileEntity(iX, iY, iZ);
                                    if ((tTileEntity instanceof GT_TileEntity_Ores)) {
                                        if (tTargetedBlock != GregTech_API.sBlockOres1) {
                                            ((GT_TileEntity_Ores) tTileEntity).convertOreBlock(aWorld, iX, iY, iZ);
                                        }
                                        ((GT_TileEntity_Ores) tTileEntity)
                                            .overrideOreBlockMaterial(this.mBlock, (byte) this.mBlockMeta);
                                    }
                                } else if (((this.mAllowToGenerateinVoid) && (aWorld.getBlock(iX, iY, iZ)
                                    .isAir(aWorld, iX, iY, iZ)))
                                    || ((tTargetedBlock != null) && ((tTargetedBlock
                                        .isReplaceableOreGen(aWorld, iX, iY, iZ, Blocks.stone))
                                        || (tTargetedBlock
                                            .isReplaceableOreGen(aWorld, iX, iY, iZ, Blocks.stained_hardened_clay))
                                        || (tTargetedBlock.isReplaceableOreGen(aWorld, iX, iY, iZ, Blocks.cobblestone))
                                        || (tTargetedBlock.isReplaceableOreGen(aWorld, iX, iY, iZ, Blocks.end_stone))
                                        || (tTargetedBlock.isReplaceableOreGen(aWorld, iX, iY, iZ, Blocks.netherrack))
                                        || (tTargetedBlock
                                            .isReplaceableOreGen(aWorld, iX, iY, iZ, GregTech_API.sBlockGranites))
                                        || (tTargetedBlock
                                            .isReplaceableOreGen(aWorld, iX, iY, iZ, GregTech_API.sBlockStones))))) {
                                                aWorld.setBlock(iX, iY, iZ, this.mBlock, this.mBlockMeta, 0);
                                            }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
