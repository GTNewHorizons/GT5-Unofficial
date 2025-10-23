package gregtech.common;

import static gregtech.api.enums.GTValues.debugOrevein;
import static gregtech.api.enums.GTValues.oreveinPlacerOres;
import static gregtech.api.enums.GTValues.oreveinPlacerOresMultiplier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.ores.OreManager;
import gregtech.common.worldgen.IWorldgenLayer;

public class WorldgenGTOreLayer extends GTWorldgen implements IWorldgenLayer {

    public static final ArrayList<WorldgenGTOreLayer> sList = new ArrayList<>();
    public final short mMinY;
    public final short mMaxY;
    public final short mWeight;
    public final short mDensity;
    public final short mSize;
    public final IOreMaterial mPrimary;
    public final IOreMaterial mSecondary;
    public final IOreMaterial mBetween;
    public final IOreMaterial mSporadic;
    public final String mRestrictBiome;
    /** {full dim name} */
    private final Set<String> mAllowedDimensions;
    private final Set<IStoneCategory> mAllowedStone;

    public WorldgenGTOreLayer(OreMixBuilder mix) {
        super(mix.oreMixName, sList, mix.enabledByDefault);
        this.mAllowedDimensions = new HashSet<>(mix.dimsEnabled);
        this.mMinY = ((short) mix.minY);
        short mMaxY = ((short) mix.maxY);
        if (mMaxY < (this.mMinY + 9)) {
            GTLog.out.println("Oremix " + this.mWorldGenName + " has invalid Min/Max heights!");
            mMaxY = (short) (this.mMinY + 9);
        }
        this.mMaxY = mMaxY;
        this.mWeight = (short) mix.weight;
        this.mDensity = (short) mix.density;
        this.mSize = (short) Math.max(1, mix.size);
        this.mPrimary = mix.primary;
        this.mSecondary = mix.secondary;
        this.mBetween = mix.between;
        this.mSporadic = mix.sporadic;
        this.mAllowedStone = mix.stoneCategories == null ? null : new HashSet<>(mix.stoneCategories);
        this.mRestrictBiome = "None";
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
        return mWeight;
    }

    @Override
    public float getDensity() {
        return 1f / (float) mDensity;
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
        return mPrimary == material || mBetween == material || mSecondary == material || mSporadic == material;
    }

    @Override
    public IOreMaterial getOre(float k) {
        if (k < 1.0 / 7.0) {
            return mSporadic;
        }

        if (k < 3.0 / 7.0) {
            return mBetween;
        }

        if (k < 5.0 / 7.0) {
            return mSecondary;
        }

        return mPrimary;
    }

    @Override
    public String getName() {
        return mWorldGenName;
    }

    @Override
    public boolean generatesBigOre() {
        return true;
    }

    public Set<String> getAllowedDimensions() {
        return mAllowedDimensions;
    }

    @Override
    public int executeWorldgenChunkified(World world, Random rng, String biome, int chunkX, int chunkZ, int seedX,
        int seedZ, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (mWorldGenName.equals("NoOresInVein")) {
            if (debugOrevein) GTLog.out.println(" NoOresInVein");
            // Return a special empty orevein
            return ORE_PLACED;
        }

        if (!mAllowedDimensions.contains(DimensionDef.getDimensionName(world))) {
            // The following code can be used for debugging, but it spams in logs
            // if (debugOrevein) { GTLog.out.println( "Wrong dimension" ); }
            return WRONG_DIMENSION;
        }

        if (!this.mRestrictBiome.equals("None") && !this.mRestrictBiome.equals(biome)) {
            return WRONG_BIOME;
        }

        int[] placeCount = new int[4];

        int veinMinY = mMinY + rng.nextInt(mMaxY - mMinY - 5);
        // Determine West/East ends of orevein
        int veinWestX = seedX - rng.nextInt(mSize); // West side
        int veinEastX = seedX + 16 + rng.nextInt(mSize);
        // Limit Orevein to only blocks present in current chunk
        int limitWestX = Math.max(veinWestX, chunkX + 2); // Bias placement by 2 blocks to prevent worldgen cascade.
        int limitEastX = Math.min(veinEastX, chunkX + 2 + 16);

        ModDimensionDef dimensionDef = DimensionDef.getDefForWorld(world);

        if (!dimensionDef.respectsOreVeinHeights()) {
            // Offset the chunkX/Z by the magic numbers used below to get the 'centre' (I have no idea where they come
            // from).
            int x = chunkX + 7;
            int z = chunkZ + 9;

            Chunk chunk = world.getChunkFromBlockCoords(x, z);

            int minY = world.getActualHeight();
            int maxY = 0;

            // Most EBS's will be empty in the end, so instead of doing a naive 0-256 scan we can check each one
            // separately.
            // This is also faster than World.getBlock since there are fewer lookups needed to get a block.
            for (ExtendedBlockStorage ebs : chunk.getBlockStorageArray()) {
                if (ebs == null) continue;

                for (int y = 0; y < 16; y++) {
                    Block block = ebs.getBlockByExtId(7, y, 9);

                    int realY = y + ebs.getYLocation();

                    if (block.isBlockSolid(world, x, realY, z, ForgeDirection.UP.ordinal())) {
                        minY = Math.min(minY, realY);
                        maxY = Math.max(maxY, realY);
                    }
                }
            }

            veinMinY = minY + rng.nextInt(maxY - minY + 1);
        }

        if (limitWestX >= limitEastX) { // No overlap between orevein and this chunk exists in X
            int hits = 0;

            // Check for stone at the center of the chunk and the bottom of the orevein.
            for (int i = 0; i < 9; i++) {
                if (StoneType.findStoneType(world, chunkX + 7, veinMinY + i, chunkZ + 9) != null) {
                    hits++;
                }
            }

            if (hits >= 5) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return NO_OVERLAP;
            } else {
                // Didn't reach, but couldn't place in test spot anyways, try for another orevein
                return NO_OVERLAP_AIR_BLOCK;
            }
        }

        // Determine North/Sound ends of orevein
        int veinNorthZ = seedZ - rng.nextInt(mSize);
        int veinSouthZ = seedZ + 16 + rng.nextInt(mSize);

        int limitNorthZ = Math.max(veinNorthZ, chunkZ + 2); // Bias placement by 2 blocks to prevent worldgen cascade.
        int limitSouthZ = Math.min(veinSouthZ, chunkZ + 2 + 16);

        if (limitNorthZ >= limitSouthZ) { // No overlap between orevein and this chunk exists in Z
            int hits = 0;

            // Check for stone at the center of the chunk and the bottom of the orevein.
            for (int i = 0; i < 9; i++) {
                if (StoneType.findStoneType(world, chunkX + 7, veinMinY + i, chunkZ + 9) != null) {
                    hits++;
                }
            }

            if (hits >= 5) {
                // Didn't reach, but could have placed. Save orevein for future use.
                return NO_OVERLAP;
            } else {
                // Didn't reach, but couldn't place in test spot anyways, try for another orevein
                return NO_OVERLAP_AIR_BLOCK;
            }
        }

        if (debugOrevein) {
            GTLog.out.print(
                "Trying Orevein:" + this.mWorldGenName
                    + " Dimension="
                    + world.provider.getDimensionName()
                    + " mX="
                    + chunkX / 16
                    + " mZ="
                    + chunkZ / 16
                    + " oreseedX="
                    + seedX / 16
                    + " oreseedZ="
                    + seedZ / 16
                    + " cY="
                    + veinMinY);
        }
        double dx = chunkX / 16 - seedX / 16;
        double dz = chunkZ / 16 - seedZ / 16;

        // Adjust the density down the more chunks we are away from the oreseed. The 5 chunks surrounding the seed
        // should always be max density due to truncation of Math.sqrt().
        int localDensity = Math.max(1, this.mDensity / (int) Math.sqrt(2 + dx * dx + dz * dz));

        LayerGenerator generator = new LayerGenerator();

        generator.world = world;
        generator.rng = rng;
        generator.limitWestX = limitWestX;
        generator.limitEastX = limitEastX;
        generator.limitSouthZ = limitSouthZ;
        generator.limitNorthZ = limitNorthZ;
        generator.veinWestX = veinWestX;
        generator.veinEastX = veinEastX;
        generator.veinSouthZ = veinSouthZ;
        generator.veinNorthZ = veinNorthZ;
        generator.localDensity = localDensity;
        // Dunno why, but the first layer is actually played one below tMinY. Go figure.
        generator.level = veinMinY - 1;
        generator.placeCount = placeCount;

        // To allow for early exit due to no ore placed in the bottom layer (probably because we are in the sky), unroll
        // 1 pass through the loop
        // Now we do bottom-level-first oregen, and work our way upwards.

        generator.generateLayer(true, false, false); // layer -1
        generator.generateLayer(true, false, false); // layer 0
        generator.generateLayer(true, false, false); // layer 1
        generator.generateLayer(true, true, false); // layer 2
        generator.generateLayer(false, true, false); // layer 3
        generator.generateLayer(false, true, true); // layer 4
        generator.generateLayer(false, true, true); // layer 5
        generator.generateLayer(false, false, true); // layer 6
        generator.generateLayer(false, false, true); // layer 7

        // Place small ores for the vein
        if (oreveinPlacerOres) {
            int smallOresToGenerate = (limitEastX - limitWestX) * (limitSouthZ - limitNorthZ)
                * this.mDensity
                / 10
                * oreveinPlacerOresMultiplier;
            // Small ores are placed in the whole chunk in which the vein appears.

            for (int i = 0; i < smallOresToGenerate; i++) {
                int tX = rng.nextInt(16) + chunkX + 2;
                int tZ = rng.nextInt(16) + chunkZ + 2;
                int tY = rng.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (mPrimary != null) {
                    OreManager.setOreForWorldGen(world, tX, tY, tZ, null, mPrimary, true);
                }

                tX = rng.nextInt(16) + chunkX + 2;
                tZ = rng.nextInt(16) + chunkZ + 2;
                tY = rng.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (mSecondary != null) {
                    OreManager.setOreForWorldGen(world, tX, tY, tZ, null, mSecondary, true);
                }

                tX = rng.nextInt(16) + chunkX + 2;
                tZ = rng.nextInt(16) + chunkZ + 2;
                tY = rng.nextInt(160) + 10; // Y height can vary from 10 to 170 for small ores.
                if (mBetween != null) {
                    OreManager.setOreForWorldGen(world, tX, tY, tZ, null, mBetween, true);
                }

                tX = rng.nextInt(16) + chunkX + 2;
                tZ = rng.nextInt(16) + chunkZ + 2;
                tY = rng.nextInt(190) + 10; // Y height can vary from 10 to 200 for small ores.
                if (mSporadic != null) {
                    OreManager.setOreForWorldGen(world, tX, tY, tZ, null, mSporadic, true);
                }
            }
        }
        if (debugOrevein) {
            GTLog.out.println(
                " wXVein" + veinWestX
                    + " eXVein"
                    + veinEastX
                    + " nZVein"
                    + veinNorthZ
                    + " sZVein"
                    + veinSouthZ
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

    private class LayerGenerator {

        World world;
        Random rng;
        int limitWestX, limitEastX, limitSouthZ, limitNorthZ;
        int veinWestX, veinEastX, veinSouthZ, veinNorthZ;
        int localDensity, level;
        int[] placeCount;

        private void generateLayer(boolean secondary, boolean between, boolean primary) {
            for (int tX = limitWestX; tX < limitEastX; tX++) {
                int chanceX = Math.max(1, Math.max(Math.abs(veinWestX - tX), Math.abs(veinEastX - tX)) / localDensity);

                for (int tZ = limitNorthZ; tZ < limitSouthZ; tZ++) {
                    int chanceZ = Math
                        .max(1, Math.max(Math.abs(veinSouthZ - tZ), Math.abs(veinNorthZ - tZ)) / localDensity);

                    if (primary) {
                        if ((rng.nextInt(chanceZ) == 0 || rng.nextInt(chanceX) == 0) && mPrimary != null) {
                            if (OreManager.setOreForWorldGen(world, tX, level, tZ, null, mPrimary, false)) {
                                placeCount[0]++;
                            }
                            continue;
                        }
                    }

                    if (between) {
                        if ((rng.nextInt(chanceZ) == 0 || rng.nextInt(chanceX) == 0) && mBetween != null) {
                            if (OreManager.setOreForWorldGen(world, tX, level, tZ, null, mBetween, false)) {
                                placeCount[2]++;
                            }
                            continue;
                        }
                    }

                    if (secondary) {
                        if ((rng.nextInt(chanceZ) == 0 || rng.nextInt(chanceX) == 0) && mSecondary != null) {
                            if (OreManager.setOreForWorldGen(world, tX, level, tZ, null, mSecondary, false)) {
                                placeCount[1]++;
                            }
                            continue;
                        }
                    }

                    if (rng.nextInt(7) == 0 && (rng.nextInt(chanceZ) == 0 || rng.nextInt(chanceX) == 0)
                        && mSporadic != null) { // Sporadics are reduced by 1/7 to compensate
                        if (OreManager.setOreForWorldGen(world, tX, level, tZ, null, mSporadic, false)) {
                            placeCount[3]++;
                        }
                        continue;
                    }
                }
            }

            level++;
        }
    }
}
