package gregtech.common;

import static gregtech.api.enums.GTValues.debugSmallOres;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.ores.OreManager;

public class WorldgenGTOreSmallPieces extends GTWorldgen {

    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public final IMaterial mMaterial;
    public final String mBiome;
    public final Set<String> mAllowedDimensions;
    public static ArrayList<WorldgenGTOreSmallPieces> sList = new ArrayList<>();

    public WorldgenGTOreSmallPieces(SmallOreBuilder ore) {
        super(ore.smallOreName, GregTechAPI.sWorldgenList, ore.enabledByDefault);

        this.mMinY = (short) ore.minY;
        this.mMaxY = (short) Math.max(this.mMinY + 1, ore.maxY);
        this.mAmount = (short) Math.max(1, ore.amount);
        this.mMaterial = ore.ore;
        this.mBiome = "None";
        this.mAllowedDimensions = new HashSet<>(ore.dimsEnabled);

        if (this.mEnabled) sList.add(this);
    }

    @Override
    public boolean executeWorldgen(World world, Random random, String biome, int dimId, int chunkX,
        int chunkZ, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
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
