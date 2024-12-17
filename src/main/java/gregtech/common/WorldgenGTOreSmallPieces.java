package gregtech.common;

import static gregtech.api.enums.GTValues.debugSmallOres;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.collect.ImmutableList;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.world.GTWorldgen;
import gregtech.common.ores.OreManager;
import gregtech.common.worldgen.IWorldgenLayer;

public class WorldgenGTOreSmallPieces extends GTWorldgen implements IWorldgenLayer {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final IMaterial mMaterial;
    public final String mBiome;
    public final Set<String> mAllowedDimensions;
    public final Set<IStoneCategory> mAllowedStone;
    public static ArrayList<WorldgenGTOreSmallPieces> sList = new ArrayList<>();

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
    public float getSize() {
        return mAmount / 2;
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
    public boolean canGenerateIn(IStoneType stoneType) {
        return mAllowedStone != null && mAllowedStone.contains(stoneType.getCategory());
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
    public boolean contains(IMaterial material) {
        return material == mMaterial;
    }

    @Override
    public ImmutableList<IMaterial> getOres() {
        return mMaterial == null ? ImmutableList.of() : ImmutableList.of(mMaterial);
    }

    @Override
    public IMaterial getOre(float k) {
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

    @Override
    public boolean executeWorldgen(World world, Random random, String biome, int dimId, int chunkX, int chunkZ,
        IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (!this.mBiome.equals("None") && !(this.mBiome.equals(biome))) {
            return false; // Not the correct biome for ore mix
        }

        if (!mAllowedDimensions.contains(world.provider.getDimensionName())) {
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
                    + " @ dim="
                    + dimId
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
