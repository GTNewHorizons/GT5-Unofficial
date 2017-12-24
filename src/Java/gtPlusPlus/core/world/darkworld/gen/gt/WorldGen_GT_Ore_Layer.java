package gtPlusPlus.core.world.darkworld.gen.gt;

import static gtPlusPlus.core.world.darkworld.gen.gt.WorldGen_GT_Base.debugWorldGen;

import java.util.ArrayList;
import java.util.Random;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.loaders.misc.GT_Achievements;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGen_GT_Ore_Layer
        extends GT_Worldgen {
    public static ArrayList<WorldGen_GT_Ore_Layer> sList = new ArrayList<WorldGen_GT_Ore_Layer>();
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
    //public final String mBiome;
    public final String mRestrictBiome;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public static final int WRONG_BIOME=0;
    public static final int WRONG_DIMENSION=1;
    public static final int NO_ORE_IN_BOTTOM_LAYER=2;
    public static final int NO_OVERLAP=3;
    public static final int ORE_PLACED=4;

    
    //public final boolean mMoon;
    //public final boolean mMars;
    //public final boolean mAsteroid;
    public final String aTextWorldgen = "worldgen.";

    public WorldGen_GT_Ore_Layer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, boolean aOverworld, boolean aNether, boolean aEnd, boolean GC_UNUSED1, boolean GC_UNUSED2, boolean GC_UNUSED3,  Materials aPrimary, Materials aSecondary, Materials aBetween, Materials aSporadic) {
        super(aName, sList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "TheEnd", aEnd);
        //this.mMoon = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Moon", aMoon);
        //this.mMars = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Mars", aMars);
        //this.mAsteroid = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Asteroid", aAsteroid);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
        short mMaxY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY));
        if (mMaxY < (this.mMinY + 7))    {
            GT_Log.out.println(
                    "Oremix " + this.mWorldGenName +
                    " has invalid Min/Max heights!"
                );
            mMaxY = (short) (this.mMinY + 7);
        }
        this.mMaxY = mMaxY;
        this.mWeight = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RandomWeight", aWeight));
        this.mDensity = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Density", aDensity));
        this.mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Size", aSize)));
        this.mPrimaryMeta = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OrePrimaryLayer", aPrimary.mMetaItemSubID));
        this.mSecondaryMeta = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSecondaryLayer", aSecondary.mMetaItemSubID));
        this.mBetweenMeta = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporadiclyInbetween", aBetween.mMetaItemSubID));
        this.mSporadicMeta = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "OreSporaticlyAround", aSporadic.mMetaItemSubID));
        this.mRestrictBiome = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "RestrictToBiomeName", "None");

        //if (mPrimaryMeta != -1 && GregTech_API.sGeneratedMaterials[(mPrimaryMeta % 1000)] == null) throw new IllegalArgumentException("A Material for the supplied ID " + mPrimaryMeta + " for " + mWorldGenName + " does not exist");
        //if (mSecondaryMeta != -1 && GregTech_API.sGeneratedMaterials[(mSecondaryMeta % 1000)] == null) throw new IllegalArgumentException("A Material for the supplied ID " + mSecondaryMeta + " for " + mWorldGenName + " does not exist");
        //if (mBetweenMeta != -1 && GregTech_API.sGeneratedMaterials[(mBetweenMeta % 1000)] == null) throw new IllegalArgumentException("A Material for the supplied ID " + mBetweenMeta + " for " + mWorldGenName + " does not exist");
        //if (mPrimaryMeta != -1 && GregTech_API.sGeneratedMaterials[(mSporadicMeta % 1000)] == null) throw new IllegalArgumentException("A Material for the supplied ID " + mSporadicMeta + " for " + mWorldGenName + " does not exist");

        if (this.mEnabled) {
            GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mPrimaryMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSecondaryMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mBetweenMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSporadicMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            sWeight += this.mWeight;            
        }
    }

    public int executeWorldgenChunkified(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, int aSeedX, int aSeedZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if( mWorldGenName.equals("NoOresInVein") ) {
            if (debugWorldGen) GT_Log.out.println(
                            " NoOresInVein"
            );
            // This is a special empty orevein
            return ORE_PLACED;
        }
        if (!isGenerationAllowed(aWorld, aDimensionType, ((aDimensionType == -1) && (this.mNether)) || ((aDimensionType == 0) && (this.mOverworld)) || ((aDimensionType == 1) && (this.mEnd)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
            /* // Debug code, but spams log
            if (debugWorldGen) {
                GT_Log.out.println(
                    "Wrong dimension"
                );
            }
            */
            return WRONG_DIMENSION;
        }
        if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
            return WRONG_BIOME;
        }
        int[] placeCount=new int[4];

        int tMinY = mMinY + aRandom.nextInt(mMaxY - mMinY - 5);
        // Determine West/East ends of orevein
        int wXVein = aSeedX - aRandom.nextInt(mSize);        // West side
        int eXVein = aSeedX + 16 + aRandom.nextInt(mSize);
        // Limit Orevein to only blocks present in current chunk
        int wX = Math.max( wXVein, aChunkX + 2);  // Bias placement by 2 blocks to prevent worldgen cascade.
        int eX = Math.min( eXVein, aChunkX + 2 + 16);
        if (wX >= eX) {  //No overlap between orevein and this chunk exists in X
            /*
            if (debugWorldGen) {
                GT_Log.out.println(
                    "No X overlap"
                );
            }
            */
            return NO_OVERLAP;
        }
        // Determine North/Sound ends of orevein
        int nZVein = aSeedZ - aRandom.nextInt(mSize);
        int sZVein = aSeedZ + 16 + aRandom.nextInt(mSize);
        
        int nZ = Math.max(nZVein, aChunkZ + 2);  // Bias placement by 2 blocks to prevent worldgen cascade.
        int sZ = Math.min(sZVein, aChunkZ + 2 + 16);
        if (nZ >= sZ) { //No overlap between orevein and this chunk exists in Z
            /*
            if (debugWorldGen) {
                GT_Log.out.println(
                    "No Z overlap"
                );
            }
            */
            return NO_OVERLAP;
        }
        // Adjust the density down the more chunks we are away from the oreseed.  The 5 chunks surrounding the seed should always be max density due to truncation of Math.sqrt().
        int localDensity = Math.max(1, this.mDensity / ((int)Math.sqrt(2 + Math.pow(aChunkX/16 - aSeedX/16, 2) + Math.pow(aChunkZ/16 - aSeedZ/16, 2))) );

         // To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll 1 pass through the loop
        // Now we do bottom-level-first oregen, and work our way upwards.
        int level = tMinY - 1; //Dunno why, but the first layer is actually played one below tMinY.  Go figure.
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondaryMeta > 0) ) {
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSecondaryMeta, false, false)) {
                            placeCount[1]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, false))
                            placeCount[3]++;
                    }
                }
            }
        if ((placeCount[1]+placeCount[3])==0) {
            if (debugWorldGen) GT_Log.out.println(
                " No ore in bottom layer"
            );
            return NO_ORE_IN_BOTTOM_LAYER;  // Exit early, didn't place anything in the bottom layer
        }
        for (level = tMinY; level < (tMinY-1+3); level++) {
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSecondaryMeta > 0) ) {
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSecondaryMeta, false, false)) {
                            placeCount[1]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, false))
                            placeCount[3]++;
                    }
                }
            }
        }
        // Low Middle layer is between + sporadic
        // level should be = tMinY-1+3 from end of for loop
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, false)) {
                            placeCount[2]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, false))
                            placeCount[3]++;
                    }
                }
            }
        // High Middle layer is between + primary + sporadic
        level++; // Increment level to next layer
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if ((aRandom.nextInt(2) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mBetweenMeta > 0) ) {  // Between are only 1 per vertical column, reduce by 1/2 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mBetweenMeta, false, false)) {
                            placeCount[2]++;
                        }
                    }
                    else if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0) ) {
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, false)) {
                            placeCount[0]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, false))
                            placeCount[3]++;
                    }
                }
            }
        // Top two layers are primary + sporadic
        level++; // Increment level to next layer
        for( ; level < (tMinY + 6); level++){ // should do two layers
            for (int tX = wX; tX < eX; tX++) {
                int placeX = Math.max(1, Math.max(MathHelper.abs_int(wXVein - tX), MathHelper.abs_int(eXVein - tX))/localDensity);
                for (int tZ = nZ; tZ < sZ; tZ++) {
                    int placeZ = Math.max(1, Math.max(MathHelper.abs_int(sZVein - tZ), MathHelper.abs_int(nZVein - tZ))/localDensity);
                    if ( ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mPrimaryMeta > 0) ) {
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mPrimaryMeta, false, false)) {
                            placeCount[0]++;
                        }
                    }
                    else if ((aRandom.nextInt(7) == 0) && ((aRandom.nextInt(placeZ) == 0) || (aRandom.nextInt(placeX) == 0)) && (this.mSporadicMeta > 0) ) {  // Sporadics are only 1 per vertical column normally, reduce by 1/7 to compensate
                        if (GT_TileEntity_Ores.setOreBlock(aWorld, tX, level, tZ, this.mSporadicMeta, false, false))
                            placeCount[3]++;
                    }
                }
            }
        }    
        if (debugWorldGen) {
            String tDimensionName = aWorld.provider.getDimensionName();
            GT_Log.out.println(
                            "Generated Orevein:" + this.mWorldGenName +
                            " Dimension=" + tDimensionName +
                            " mX="+aChunkX/16+
                            " mZ="+aChunkZ/16+
                            " oreseedX="+ aSeedX/16 +
                            " oreseedZ="+ aSeedZ/16 +
                            " cY="+tMinY+
                            " wXVein" + wXVein +
                            " eXVein" + eXVein +
                            " nZVein" + nZVein +
                            " sZVein" + sZVein +
                            " locDen=" + localDensity +
                            " Den=" + this.mDensity +
                            " Sec="+placeCount[1]+
                            " Spo="+placeCount[3]+
                            " Bet="+placeCount[2]+
                            " Pri="+placeCount[0]
            );
        }
        // Something (at least the bottom layer must have 1 block) must have been placed, return true
        return ORE_PLACED;
    }
}