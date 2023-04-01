package gregtech.common;

import static gregtech.api.enums.GT_Values.debugSmallOres;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;

public class GT_Worldgen_GT_Ore_SmallPieces extends GT_Worldgen {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final short mMeta;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean mMoon = false, mMars = false, mAsteroid = false;
    public final String mBiome;
    public final String aTextWorldgen = "worldgen.";
    public static ArrayList<GT_Worldgen_GT_Ore_SmallPieces> sList = new ArrayList<GT_Worldgen_GT_Ore_SmallPieces>();

    // TODO CHECK IF INSTANTIATION IS CORRECT
    public GT_Worldgen_GT_Ore_SmallPieces(String aName, boolean aDefault, int aMinY, int aMaxY, int aAmount,
            boolean aOverworld, boolean aNether, boolean aEnd, Materials aPrimary) {
        super(aName, GregTech_API.sWorldgenList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "TheEnd", aEnd);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
        this.mMaxY = ((short) Math.max(
                this.mMinY + 1,
                GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY)));
        this.mAmount = ((short) Math.max(
                1,
                GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Amount", aAmount)));
        this.mMeta = ((short) GregTech_API.sWorldgenFile.get(
                aTextWorldgen + this.mWorldGenName,
                "Ore",
                aPrimary.mMetaItemSubID));
        this.mBiome = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "BiomeName", "None");
        sList.add(this);
    }

    public GT_Worldgen_GT_Ore_SmallPieces(String aName, boolean aDefault, int aMinY, int aMaxY, int aAmount,
            boolean aOverworld, boolean aNether, boolean aEnd, boolean GC_UNUSED1, boolean GC_UNUSED2,
            boolean GC_UNUSED3, Materials aPrimary) {
        super(aName, GregTech_API.sWorldgenList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "TheEnd", aEnd);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MinHeight", aMinY));
        this.mMaxY = ((short) Math.max(
                this.mMinY + 1,
                GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "MaxHeight", aMaxY)));
        this.mAmount = ((short) Math.max(
                1,
                GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "Amount", aAmount)));
        this.mMeta = ((short) GregTech_API.sWorldgenFile.get(
                aTextWorldgen + this.mWorldGenName,
                "Ore",
                aPrimary.mMetaItemSubID));
        this.mBiome = GregTech_API.sWorldgenFile.get(aTextWorldgen + this.mWorldGenName, "BiomeName", "None");
        sList.add(this);
    }

    @Override
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
            int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if (!this.mBiome.equals("None") && !(this.mBiome.equals(aBiome))) {
            return false; // Not the correct biome for ore mix
        }
        if (!isGenerationAllowed(
                aWorld,
                aDimensionType,
                ((aDimensionType == -1) && (this.mNether)) || ((aDimensionType == 0) && (this.mOverworld))
                        || ((aDimensionType == 1) && (this.mEnd)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
            return false;
        }
        int count = 0;
        // For optimal performance, this should be done upstream. Meh
        String tDimensionName = aWorld.provider.getDimensionName();
        boolean isUnderdark = tDimensionName.equals("Underdark");

        if (this.mMeta > 0) {
            int j = Math.max(1, this.mAmount / 2 + aRandom.nextInt(this.mAmount) / 2);
            for (int i = 0; i < j; i++) {
                GT_TileEntity_Ores.setOreBlock(
                        aWorld,
                        aChunkX + 8 + aRandom.nextInt(16),
                        this.mMinY + aRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)),
                        aChunkZ + 8 + aRandom.nextInt(16),
                        this.mMeta,
                        true,
                        isUnderdark);
                count++;
            }
        }
        if (debugSmallOres) {
            GT_Log.out.println(
                    "Small Ore:" + this.mWorldGenName
                            + " @ dim="
                            + aDimensionType
                            + " mX="
                            + aChunkX / 16
                            + " mZ="
                            + aChunkZ / 16
                            + " ore="
                            + count);
        }
        return true;
    }
}
