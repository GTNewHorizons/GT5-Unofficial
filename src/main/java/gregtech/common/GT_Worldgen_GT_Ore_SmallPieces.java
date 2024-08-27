package gregtech.common;

import static gregtech.api.enums.GT_Values.debugSmallOres;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;

import gregtech.api.GregTech_API;
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
    public final boolean twilightForest;
    public final String mBiome;
    public static ArrayList<GT_Worldgen_GT_Ore_SmallPieces> sList = new ArrayList<>();

    public Class[] mAllowedProviders;

    public GT_Worldgen_GT_Ore_SmallPieces(SmallOreBuilder ore) {
        super(ore.smallOreName, GregTech_API.sWorldgenList, ore.enabledByDefault);
        this.mOverworld = ore.dimsEnabled.getOrDefault(SmallOreBuilder.OW, false);
        this.mNether = ore.dimsEnabled.getOrDefault(SmallOreBuilder.NETHER, false);
        this.mEnd = ore.dimsEnabled.getOrDefault(SmallOreBuilder.THE_END, false);
        this.twilightForest = ore.dimsEnabled.getOrDefault(SmallOreBuilder.TWILIGHT_FOREST, false);

        this.mMinY = (short) ore.minY;
        this.mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        this.mAmount = (short) Math.max(1, ore.amount);
        this.mMeta = (short) ore.ore.mMetaItemSubID;
        this.mBiome = "None";

        if (this.mEnabled) sList.add(this);

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
    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX,
        int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if (!this.mBiome.equals("None") && !(this.mBiome.equals(aBiome))) {
            return false; // Not the correct biome for ore mix
        }
        if (!isGenerationAllowed(aWorld, mAllowedProviders)) {
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
