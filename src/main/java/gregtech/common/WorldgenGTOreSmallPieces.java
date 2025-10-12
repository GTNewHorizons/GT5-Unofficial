package gregtech.common;

import static gregtech.api.enums.GTValues.debugSmallOres;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.world.GTWorldgen;
import gregtech.common.ores.OreManager;
import gregtech.common.worldgen.IWorldgenLayer;

public class WorldgenGTOreSmallPieces extends GTWorldgen implements IWorldgenLayer {

    public static final List<WorldgenGTOreSmallPieces> sList = new ArrayList<>();
    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    private final IOreMaterial mMaterial;

    public final String mBiome;
    private final Set<String> mAllowedDimensions;
    private final Set<IStoneCategory> mAllowedStone;

    public WorldgenGTOreSmallPieces(SmallOreBuilder ore) {
        super(ore.smallOreName, GregTechAPI.sWorldgenList, ore.enabledByDefault);

        this.mMinY = (short) ore.minY;
        this.mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        this.mAmount = (short) Math.max(1, ore.amount);
        this.mMaterial = ore.ore;
        this.mBiome = "None";
        this.mAllowedDimensions = new HashSet<>(ore.dimsEnabled);
        this.mAllowedStone = ore.stoneCategories == null ? null : new HashSet<>(ore.stoneCategories);

        if (this.mEnabled) sList.add(this);
    }

    @Override
    public int getMinY() {
        return mMinY;
    }

    @Override
    public int getMaxY() {
        return mMaxY;
    }

    @Override
    public int getWeight() {
        return mAmount;
    }

    @Override
    public float getDensity() {
        return GTUtility.clamp(mAmount / 64.0f, 0f, 1f);
    }

    @Override
    public boolean canGenerateIn(String dimName) {
        return mAllowedDimensions.contains(dimName);
    }

    @Override
    public boolean canGenerateIn(IStoneCategory stoneType) {
        return mAllowedStone != null && mAllowedStone.contains(stoneType);
    }

    @Override
    public boolean isStoneSpecific() {
        return mAllowedStone != null;
    }

    @Override
    public boolean contains(IOreMaterial material) {
        return material == mMaterial;
    }

    @Override
    public IOreMaterial getOre(float k) {
        return mMaterial;
    }

    @Override
    public String getName() {
        return mWorldGenName;
    }

    @Override
    public boolean generatesBigOre() {
        return false;
    }

    public Set<String> getAllowedDimensions() {
        return mAllowedDimensions;
    }

    public IOreMaterial getMaterial() {
        return mMaterial;
    }

    @Override
    public boolean executeWorldgen(World world, Random random, String biome, int chunkX, int chunkZ,
        IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (!this.mBiome.equals("None") && !(this.mBiome.equals(biome))) {
            return false; // Not the correct biome for ore mix
        }

        if (!mAllowedDimensions.contains(DimensionDef.getDimensionName(world))) {
            return false;
        }

        int count = 0;

        if (this.mMaterial != null) {
            int smallOresToGenerate = Math.max(1, this.mAmount / 2 + random.nextInt(this.mAmount) / 2);

            for (int i = 0; i < smallOresToGenerate; i++) {
                OreManager.setOreForWorldGen(
                    world,
                    chunkX + 8 + random.nextInt(16),
                    this.mMinY + random.nextInt(Math.max(1, this.mMaxY - this.mMinY)),
                    chunkZ + 8 + random.nextInt(16),
                    null,
                    mMaterial,
                    true);
                count++;
            }
        }
        if (debugSmallOres) {
            GTLog.out.println(
                "Small Ore:" + this.mWorldGenName
                    + " @ DimName="
                    + world.provider.getDimensionName()
                    + " mX="
                    + chunkX / 16
                    + " mZ="
                    + chunkZ / 16
                    + " ore="
                    + count);
        }
        return true;
    }
}
