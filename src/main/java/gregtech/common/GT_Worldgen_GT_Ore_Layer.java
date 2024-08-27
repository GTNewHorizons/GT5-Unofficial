package gregtech.common;

import static gregtech.api.enums.GT_Values.debugOrevein;
import static gregtech.api.enums.GT_Values.oreveinPlacerOres;
import static gregtech.api.enums.GT_Values.oreveinPlacerOresMultiplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;

import bloodasp.galacticgreg.api.enums.DimensionDef;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;

public class GT_Worldgen_GT_Ore_Layer extends GT_Worldgen {

    public static ArrayList<GT_Worldgen_GT_Ore_Layer> sList = new ArrayList<>();
    public static int sWeight = 0;
    public final short mMinY;
    public final short mMaxY;
    public final short mWeight;
    public final short mDensity;
    public final short mSize;
    public final short mPrimaryMeta;
    public final short mSecondaryMeta;
    public final short mBetweenMeta;
    public final short mSporadicMeta;
    public final String mRestrictBiome;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean mEndAsteroid;
    public final boolean twilightForest;
    public static final int WRONG_BIOME = 0;
    public static final int WRONG_DIMENSION = 1;
    public static final int NO_ORE_IN_BOTTOM_LAYER = 2;
    public static final int NO_OVERLAP = 3;
    public static final int ORE_PLACED = 4;
    public static final int NO_OVERLAP_AIR_BLOCK = 5;
    public final String aTextWorldgen = "worldgen.";

    public Class[] mAllowedProviders;

    public GT_Worldgen_GT_Ore_Layer(OreMixBuilder mix) {
        super(mix.oreMixName, sList, mix.enabledByDefault);
        this.mOverworld = mix.dimsEnabled.getOrDefault(OreMixBuilder.OW, false);
        this.mNether = mix.dimsEnabled.getOrDefault(OreMixBuilder.NETHER, false);
        this.mEnd = mix.dimsEnabled.getOrDefault(OreMixBuilder.THE_END, false);
        this.mEndAsteroid = mix.dimsEnabled
            .getOrDefault(DimensionDef.EndAsteroids.modDimensionDef.getDimensionName(), false);
        this.twilightForest = mix.dimsEnabled.getOrDefault(OreMixBuilder.TWILIGHT_FOREST, false);
        this.mMinY = ((short) mix.minY);
        short mMaxY = ((short) mix.maxY);
        if (mMaxY < (this.mMinY + 9)) {
            GT_Log.out.println("Oremix " + this.mWorldGenName + " has invalid Min/Max heights!");
            mMaxY = (short) (this.mMinY + 9);
        }
        this.mMaxY = mMaxY;
        this.mWeight = (short) mix.weight;
        this.mDensity = (short) mix.density;
        this.mSize = (short) Math.max(1, mix.size);
        this.mPrimaryMeta = (short) mix.primary.mMetaItemSubID;
        this.mSecondaryMeta = (short) mix.secondary.mMetaItemSubID;
        this.mBetweenMeta = (short) mix.between.mMetaItemSubID;
        this.mSporadicMeta = (short) mix.sporadic.mMetaItemSubID;
        this.mRestrictBiome = "None";

        if (this.mEnabled) {
            sWeight += this.mWeight;
        }

        List<Class> allowedProviders = new ArrayList<>();
        if (this.mNether) {
            allowedProviders.add(WorldProviderHell.class);
        }

        if (this.mOverworld) {
            allowedProviders.add(WorldProviderSurface.class);
        }

        if (this.mEnd) {
            allowedProviders.add(WorldProviderEnd.class);
        }
        mAllowedProviders = allowedProviders.toArray(new Class[allowedProviders.size()]);
    }

    @Override
    public int executeWorldgenChunkified(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, int aSeedX, int aSeedZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if (mWorldGenName.equals("NoOresInVein")) {
            if (debugOrevein) GT_Log.out.println(" NoOresInVein");
            // Return a special empty orevein
            return ORE_PLACED;
        }

        if (!isGenerationAllowed(aWorld, mAllowedProviders)) {
            // The following code can be used for debugging, but it spams in logs
            // if (debugOrevein) { GT_Log.out.println( "Wrong dimension" ); }
            return WRONG_DIMENSION;
        }

        if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
            return WRONG_BIOME;
        }
        // For optimal performance, this should be done upstream. Meh
        String tDimensionName = aWorld.provider.getDimensionName();
        boolean isUnderdark = tDimensionName.equals("Underdark");

        int[] placeCount = new int[4];

        int tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);
        // Determine West/East ends of orevein
        int wXVein = aSeedX - aRandom.nextInt(mSize); // West side
        int eXVein = aSeedX + 16 + aRandom.nextInt(mSize);
        // Limit Orevein to only blocks present in current chunk
        int wX = Math.max(wXVein, aChunkX + 2); // Bias placement by 2 blocks to prevent worldgen cascade.
        int eX = Math.min(eXVein, aChunkX + 2 + 16);

        // Get a block at the center of the chunk and the bottom of the orevein.
        Block tBlock = aWorld.getBlock(aChunkX + 7, tMinY, aChunkZ + 9);

        if (wX >= eX) { // No overlap between orevein and this chunk exists in X
            if (tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.stone)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.netherrack)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.end_stone)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, GregTech_API.sBlockGranites)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, GregTech_API.sBlockStones)) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return NO_OVERLAP;
            } else {
                // Didn't reach, but couldn't place in test spot anywys, try for another orevein
                return NO_OVERLAP_AIR_BLOCK;
            }
        }
        // Determine North/Sound ends of orevein
        int nZVein = aSeedZ - aRandom.nextInt(mSize);
        int sZVein = aSeedZ + 16 + aRandom.nextInt(mSize);

        int nZ = Math.max(nZVein, aChunkZ + 2); // Bias placement by 2 blocks to prevent worldgen cascade.
        int sZ = Math.min(sZVein, aChunkZ + 2 + 16);
        if (nZ >= sZ) { // No overlap between orevein and this chunk exists in Z
            if (tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.stone)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.netherrack)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, Blocks.end_stone)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, GregTech_API.sBlockGranites)
                || tBlock.isReplaceableOreGen(aWorld, aChunkX + 7, tMinY, aChunkZ + 9, GregTech_API.sBlockStones)) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return NO_OVERLAP;
            } else {
                // Didn't reach, but couldn't place in test spot anywys, try for another orevein
                return NO_OVERLAP_AIR_BLOCK;
            }
        }

        if (debugOrevein) {
            GT_Log.out.print(
                "Trying Orevein:" + this.mWorldGenName
                    + " Dimension="
                    + tDimensionName
                    + " mX="
                    + aChunkX / 16
                    + " mZ="
                    + aChunkZ / 16
                    + " oreseedX="
                    + aSeedX / 16
                    + " oreseedZ="
                    + aSeedZ / 16
                    + " cY="
                    + tMinY);
        }
        // Adjust the density down the more chunks we are away from the oreseed. The 5 chunks surrounding the seed
        // should always be max density due to truncation of Math.sqrt().
        int localDensity = Math.max(
            1,
            this.mDensity / ((int) Math
                .sqrt(2 + Math.pow(aChunkX / 16 - aSeedX / 16, 2) + Math.pow(aChunkZ / 16 - aSeedZ / 16, 2))));

        // To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll
        // 1 pass through the loop
        // Now we do bottom-level-first oregen, and work our way upwards.
        // Layer -1 Secondary and Sporadic
        int level = tMinY - 1; // Dunno why, but the first layer is actually played one below tMinY. Go figure.
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondaryMeta > 0)) {
                    if (GT_TileEntity_Ores
                        .setOreBlock(aWorld, tX, level, tZ, this.mSecondaryMeta, false, isUnderdark)) {
                        placeCount[1]++;
                    }
                } else
                    if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        if ((placeCount[1] + placeCount[3]) == 0) {
            if (debugOrevein) GT_Log.out.println(" No ore in bottom layer");
            return NO_ORE_IN_BOTTOM_LAYER; // Exit early, didn't place anything in the bottom layer
        }
        // Layers 0 & 1 Secondary and Sporadic
        for (level = tMinY; level < (tMinY + 2); level++) {
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math
                    .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(
                        1,
                        Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                    if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSecondaryMeta > 0)) {
                        if (GT_TileEntity_Ores
                            .setOreBlock(aWorld, tX, level, tZ, this.mSecondaryMeta, false, isUnderdark)) {
                            placeCount[1]++;
                        }
                    } else if ((aRandom.nextInt(7) == 0)
                        && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
                }
            }
        }
        // Layer 2 is Secondary, in-between, and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                    && (this.mBetweenMeta > 0)) { // Between are reduce by 1/2 to compensate
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, isUnderdark)) {
                        placeCount[2]++;
                    }
                } else if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                    && (this.mSecondaryMeta > 0)) {
                        if (GT_TileEntity_Ores
                            .setOreBlock(aWorld, tX, level, tZ, this.mSecondaryMeta, false, isUnderdark)) {
                            placeCount[1]++;
                        }
                    } else
                    if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        level++; // Increment level to next layer
        // Layer 3 is In-between, and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                    && (this.mBetweenMeta > 0)) { // Between are reduce by 1/2 to compensate
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, isUnderdark)) {
                        placeCount[2]++;
                    }
                } else
                    if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        level++; // Increment level to next layer
        // Layer 4 is In-between, Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                    && (this.mBetweenMeta > 0)) { // Between are reduce by 1/2 to compensate
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, isUnderdark)) {
                        placeCount[2]++;
                    }
                } else
                    if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0)) {
                        if (GT_TileEntity_Ores
                            .setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, isUnderdark)) {
                            placeCount[1]++;
                        }
                    } else if ((aRandom.nextInt(7) == 0)
                        && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        level++; // Increment level to next layer
        // Layer 5 is In-between, Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                    && (this.mBetweenMeta > 0)) { // Between are reduce by 1/2 to compensate
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, isUnderdark)) {
                        placeCount[2]++;
                    }
                } else
                    if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0)) {
                        if (GT_TileEntity_Ores
                            .setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, isUnderdark)) {
                            placeCount[1]++;
                        }
                    } else if ((aRandom.nextInt(7) == 0)
                        && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        level++; // Increment level to next layer
        // Layer 6 is Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0)) {
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, isUnderdark)) {
                        placeCount[1]++;
                    }
                } else
                    if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        level++; // Increment level to next layer
        // Layer 7 is Primary and sporadic
        for (int tX = wX; tX < eX; tX++) {
            int placeX = Math
                .max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX)) / localDensity);
            for (int tZ = nZ; tZ < sZ; tZ++) {
                int placeZ = Math
                    .max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ)) / localDensity);
                if (((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0)) {
                    if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, isUnderdark)) {
                        placeCount[1]++;
                    }
                } else
                    if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0))
                        && (this.mSporadicMeta > 0)) { // Sporadics are reduce by 1/7 to compensate
                            if (GT_TileEntity_Ores
                                .setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, isUnderdark))
                                placeCount[3]++;
                        }
            }
        }
        // Place small ores for the vein
        if (oreveinPlacerOres) {
            int nSmallOres = (eX - wX) * (sZ - nZ) * this.mDensity / 10 * oreveinPlacerOresMultiplier;
            // Small ores are placed in the whole chunk in which the vein appears.
            for (int nSmallOresCount = 0; nSmallOresCount < nSmallOres; nSmallOresCount++) {
                int tX = aRandom.nextInt(16) + aChunkX + 2;
                int tZ = aRandom.nextInt(16) + aChunkZ + 2;
                int tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mPrimaryMeta > 0)
                    GT_TileEntity_Ores.setOreBlock(aWorld, tX, tY, tZ, this.mPrimaryMeta, true, isUnderdark);
                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mSecondaryMeta > 0)
                    GT_TileEntity_Ores.setOreBlock(aWorld, tX, tY, tZ, this.mSecondaryMeta, true, isUnderdark);
                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (this.mBetweenMeta > 0)
                    GT_TileEntity_Ores.setOreBlock(aWorld, tX, tY, tZ, this.mBetweenMeta, true, isUnderdark);
                tX = aRandom.nextInt(16) + aChunkX + 2;
                tZ = aRandom.nextInt(16) + aChunkZ + 2;
                tY = aRandom.nextInt(190) + 10; // Y height can vary from 10 to 200 for small ores.
                if (this.mSporadicMeta > 0)
                    GT_TileEntity_Ores.setOreBlock(aWorld, tX, tY, tZ, this.mSporadicMeta, true, isUnderdark);
            }
        }
        if (debugOrevein) {
            GT_Log.out.println(
                " wXVein" + wXVein
                    + " eXVein"
                    + eXVein
                    + " nZVein"
                    + nZVein
                    + " sZVein"
                    + sZVein
                    + " locDen="
                    + localDensity
                    + " Den="
                    + this.mDensity
                    + " Sec="
                    + placeCount[1]
                    + " Spo="
                    + placeCount[3]
                    + " Bet="
                    + placeCount[2]
                    + " Pri="
                    + placeCount[0]);
        }
        // Something (at least the bottom layer must have 1 block) must have been placed, return true
        return ORE_PLACED;
    }
}
