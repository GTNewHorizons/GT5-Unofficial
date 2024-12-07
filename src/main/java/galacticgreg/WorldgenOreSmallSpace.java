package galacticgreg;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.world.GTWorldgen;
import gregtech.common.SmallOreBuilder;
import gregtech.common.ores.OreManager;

public class WorldgenOreSmallSpace extends GTWorldgen {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final IMaterial mMaterial;

    private long mProfilingStart;
    private long mProfilingEnd;
    private final Set<String> allowedDims;

    public WorldgenOreSmallSpace(SmallOreBuilder ore) {
        super(ore.smallOreName, GalacticGreg.smallOreWorldgenList, ore.enabledByDefault);

        mMinY = (short) ore.minY;
        mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        mAmount = (short) Math.max(1, ore.amount);
        mMaterial = ore.ore;

        allowedDims = new HashSet<>();
        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            if (!mc.isModLoaded()) continue;

            for (ModDimensionDef mdd : mc.getDimensionList()) {
                String dimId = mdd.getDimIdentifier();

                if (ore.dimsEnabled.contains(dimId)) {
                    allowedDims.add(dimId);
                }
            }
        }

        GalacticGreg.Logger.trace("Initialized new OreLayer: %s", ore.smallOreName);
    }

    /**
     * Check if *this* orelayer is enabled for pDimensionDef
     *
     * @param pDimensionDef the ChunkProvider in question
     * @return
     */
    public boolean isEnabledForDim(ModDimensionDef pDimensionDef) {
        return allowedDims.contains(pDimensionDef.getDimIdentifier());
    }

    @Override
    public boolean executeWorldgen(World pWorld, Random pRandom, String pBiome, int pDimensionType, int pChunkX,
        int pChunkZ, IChunkProvider pChunkGenerator, IChunkProvider pChunkProvider) {
        GalacticGreg.Logger.trace("Entering executeWorldgen for [%s]", mWorldGenName);
        ModDimensionDef tMDD = GalacticGregRegistry.getDimensionTypeByChunkGenerator(pChunkGenerator);
        if (tMDD == null) {
            GalacticGreg.Logger
                .trace("Can't find dimension definition for ChunkProvider %s, skipping", pChunkGenerator.toString());
            return false;
        }

        if (!isEnabledForDim(tMDD)) {
            GalacticGreg.Logger
                .trace("OreGen for %s is disallowed in dimension %s, skipping", mWorldGenName, tMDD.getDimensionName());
            return false;
        }

        if (GalacticGreg.GalacticConfig.ProfileOreGen) mProfilingStart = System.currentTimeMillis();
        // ---------------------------

        if (mMaterial != null) {
            int i = 0;
            for (int j = Math.max(1, this.mAmount / 2 + pRandom.nextInt(this.mAmount) / 2); i < j; i++) {
                OreManager.setOreForWorldGen(
                    pWorld,
                    pChunkX + pRandom.nextInt(16),
                    this.mMinY + pRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)),
                    pChunkZ + pRandom.nextInt(16),
                    null,
                    mMaterial,
                    true);
            }
        }
        // ---------------------------
        if (GalacticGreg.GalacticConfig.ProfileOreGen) {
            try {
                mProfilingEnd = System.currentTimeMillis();
                long tTotalTime = mProfilingEnd - mProfilingStart;
                GalacticGreg.Profiler.AddTimeToList(tMDD, tTotalTime);
                GalacticGreg.Logger.debug(
                    "Done with SmallOre-Worldgen in DimensionType %s. Generation took %d ms",
                    tMDD.getDimensionName(),
                    tTotalTime);
            } catch (Exception ignored) {} // Silently ignore errors
        }

        GalacticGreg.Logger.trace("Leaving executeWorldgen");
        return true;
    }

    public boolean isAllowedForHeight(int pTargetHeight) {
        return (pTargetHeight >= mMinY && pTargetHeight <= mMaxY);
    }
}
