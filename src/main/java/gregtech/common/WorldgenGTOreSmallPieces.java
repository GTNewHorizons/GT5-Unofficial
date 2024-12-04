package gregtech.common;

import static gregtech.api.enums.GTValues.debugSmallOres;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.blocks.BlockOres2;

public class WorldgenGTOreSmallPieces extends GTWorldgen {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final Materials mMaterial;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean twilightForest;
    public final String mBiome;
    public static ArrayList<WorldgenGTOreSmallPieces> sList = new ArrayList<>();

    public Class<?>[] mAllowedProviders;
    public String[] blackListedProviders;
    public static Class<?> tfProviderClass;

    static {
        try {
            tfProviderClass = Class.forName("twilightforest.world.WorldProviderTwilightForest");
        } catch (ClassNotFoundException ignored) {}
    }

    public WorldgenGTOreSmallPieces(SmallOreBuilder ore) {
        super(ore.smallOreName, GregTechAPI.sWorldgenList, ore.enabledByDefault);
        this.mOverworld = ore.dimsEnabled.contains(SmallOreBuilder.OW);
        this.mNether = ore.dimsEnabled.contains(SmallOreBuilder.NETHER);
        this.mEnd = ore.dimsEnabled.contains(SmallOreBuilder.THE_END);
        this.twilightForest = ore.dimsEnabled.contains(SmallOreBuilder.TWILIGHT_FOREST);

        this.mMinY = (short) ore.minY;
        this.mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        this.mAmount = (short) Math.max(1, ore.amount);
        this.mMaterial = ore.ore;
        this.mBiome = "None";

        if (this.mEnabled) sList.add(this);

        List<Class<?>> allowedProviders = new ArrayList<>();
        if (this.mNether) {
            allowedProviders.add(WorldProviderHell.class);
        }

        if (this.mOverworld) {
            allowedProviders.add(WorldProviderSurface.class);
            if (!this.twilightForest) {
                blackListedProviders = new String[] { "twilightforest.world.WorldProviderTwilightForest" };
            }
        }

        if (tfProviderClass != null && this.twilightForest) {
            allowedProviders.add(tfProviderClass);
        }

        if (this.mEnd) {
            allowedProviders.add(WorldProviderEnd.class);
        }
        mAllowedProviders = allowedProviders.toArray(new Class[0]);
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

        if (this.mMaterial != null) {
            int j = Math.max(1, this.mAmount / 2 + aRandom.nextInt(this.mAmount) / 2);
            for (int i = 0; i < j; i++) {
                BlockOres2.setOreForWorldGen(
                    aWorld,
                    aChunkX + 8 + aRandom.nextInt(16),
                    this.mMinY + aRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)),
                    aChunkZ + 8 + aRandom.nextInt(16),
                    mMaterial,
                    true);
                count++;
            }
        }
        if (debugSmallOres) {
            GTLog.out.println(
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
