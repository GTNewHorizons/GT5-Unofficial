package galacticgreg;

import static gregtech.api.enums.GTValues.profileWorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.Enums;
import galacticgreg.api.Enums.TargetBlockPosition;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.dynconfig.DynamicDimensionConfig;
import galacticgreg.dynconfig.DynamicDimensionConfig.AsteroidConfig;
import gregtech.GTMod;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.MurmurHash;
import gregtech.api.objects.XSTR;
import gregtech.common.ores.OreManager;
import gregtech.common.worldgen.IWorldgenLayer;
import gregtech.common.worldgen.WorldgenQuery;

public class WorldGeneratorSpace implements IWorldGenerator {

    public WorldGeneratorSpace() {
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {

        GalacticGreg.Logger.trace("Triggered generate: [Dimension %s]", world.provider.getDimensionName());

        // Don't use the effective dim def here because we want to generate asteroids that clip into end islands
        ModDimensionDef tDimDef = DimensionDef.getDefForWorld(world);

        if (tDimDef == null) {
            GalacticGreg.Logger.trace(
                "Ignoring dimension %s as there is no definition for it in the registry",
                world.provider.getDimensionName());
            return;
        } else {
            GalacticGreg.Logger.trace("Selected DimDef: [%s]", tDimDef.getDimIdentifier());
        }

        if (!tDimDef.generatesAsteroids()) return;

        long pre = profileWorldGen ? System.nanoTime() : 0;

        int seeds = 0;

        for (int offsetZ = -2; offsetZ <= 2; offsetZ++) {
            for (int offsetX = -2; offsetX <= 2; offsetX++) {
                AsteroidGenerator gen = AsteroidGenerator.forChunk(world, chunkX + offsetX, chunkZ + offsetZ);

                if (gen == null) continue;

                if (!gen.affectsChunk(chunkX, chunkZ)) continue;

                gen.generateChunk(world, chunkX, chunkZ);

                seeds++;
            }
        }

        long post = profileWorldGen ? System.nanoTime() : 0;

        if (profileWorldGen) {
            GTMod.GT_FML_LOGGER.info(
                String
                    .format("Generated %d %d in %,d us (%d seeds)", chunkX, chunkZ, (int) ((post - pre) / 1e3), seeds));
        }

        Chunk tChunk = world.getChunkFromBlockCoords(chunkX, chunkZ);

        if (tChunk != null) {
            tChunk.isModified = seeds > 0;
        }
    }

    public static class AsteroidGenerator {

        private List<Ellipsoid> positive, negative;
        private int seedChunkX, seedChunkZ, cX, cY, cZ, radius, size;

        private IStoneType stoneType;
        private transient IWorldgenLayer ore;

        private ModDimensionDef dimensionDef;
        private AsteroidConfig asteroidConfig;

        public static AsteroidGenerator forChunk(World world, int seedChunkX, int seedChunkZ) {
            ModDimensionDef dimensionDef = DimensionDef.getEffectiveDefForChunk(world, seedChunkX, seedChunkZ);
            AsteroidConfig asteroidConfig = DynamicDimensionConfig.getAsteroidConfig(dimensionDef);

            if (asteroidConfig == null || !asteroidConfig.Enabled) return null;

            if (!generatesAsteroid(
                world.getSeed(),
                seedChunkX,
                seedChunkZ,
                world.provider.dimensionId,
                asteroidConfig.Probability)) return null;

            XSTR rng = getRandom(world.getSeed(), seedChunkX, seedChunkZ, world.provider.dimensionId);

            IStoneType asteroidStone = dimensionDef.getRandomAsteroidMaterial(rng.clone());
            if (asteroidStone == null) return null;

            IWorldgenLayer oreLayer;

            if (rng.nextInt(5) == 0) {
                oreLayer = WorldgenQuery.small()
                    .inDimension(dimensionDef)
                    .inStone(asteroidStone.getCategory())
                    .findRandom(rng.clone());
            } else {
                oreLayer = WorldgenQuery.veins()
                    .inDimension(dimensionDef)
                    .inStone(asteroidStone.getCategory())
                    .findRandom(rng.clone());
            }

            if (oreLayer == null) return null;

            GalacticGreg.Logger.debug(
                "Asteroid will be built with: Stone: [%s] Ore: [%s]",
                asteroidStone.getStone(),
                oreLayer.getName());

            int tX = seedChunkX * 16 + rng.nextInt(16);
            int tY = asteroidConfig.AsteroidMinY
                + rng.nextInt(asteroidConfig.AsteroidMaxY - asteroidConfig.AsteroidMinY);
            int tZ = seedChunkZ * 16 + rng.nextInt(16);

            List<Ellipsoid> positive = new ArrayList<>();
            List<Ellipsoid> negative = new ArrayList<>();

            int radius = asteroidConfig.MinSize + rng.nextInt(asteroidConfig.MinSize - asteroidConfig.MaxSize + 1);
            positive.add(new Ellipsoid(0, 0, 0, radius));

            if (asteroidConfig.PositiveEllipsoids > 0) {
                int k = rng.nextInt(2);
                for (int i = 0; i < k; i++) {
                    negative.add(
                        new Ellipsoid(
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 0.25f + 0.6f)));
                }
            }

            if (asteroidConfig.NegativeEllipsoids > 0 && rng.nextInt(4) == 0) {
                int k = rng.nextInt(asteroidConfig.NegativeEllipsoids);
                for (int i = 0; i < k; i++) {
                    positive.add(
                        new Ellipsoid(
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 2 - 1),
                            radius * (rng.nextFloat() * 0.25f + 0.6f)));
                }
            }

            AsteroidGenerator gen = new AsteroidGenerator();

            gen.dimensionDef = dimensionDef;
            gen.asteroidConfig = asteroidConfig;

            gen.positive = positive;
            gen.negative = negative;

            gen.seedChunkX = seedChunkX;
            gen.seedChunkZ = seedChunkZ;

            gen.cX = tX;
            gen.cY = tY;
            gen.cZ = tZ;
            gen.radius = radius;
            gen.size = radius * 2;

            gen.stoneType = asteroidStone;
            gen.ore = oreLayer;

            return gen;
        }

        public boolean affectsChunk(int chunkX, int chunkZ) {
            int minX = (cX - size) >> 4;
            int maxX = (cX + size + 1) >> 4;

            int minZ = (cZ - size) >> 4;
            int maxZ = (cZ + size + 1) >> 4;

            return minX <= chunkX && chunkX <= maxX || minZ <= chunkZ && chunkZ <= maxZ;
        }

        public void generateChunk(World world, int chunkX, int chunkZ) {
            int minX = Math.max(chunkX * 16, cX - size - 1);
            int maxX = Math.min((chunkX + 1) * 16, cX + size + 1);

            int minY = Math.max(0, cY - size - 1);
            int maxY = Math.min(255, cY + size + 1);

            int minZ = Math.max(chunkZ * 16, cZ - size - 1);
            int maxZ = Math.min((chunkZ + 1) * 16, cZ + size + 1);

            MurmurHash hasher = new MurmurHash();
            XSTR rng2 = new XSTR(0);

            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    outer: for (int x = minX; x < maxX; x++) {

                        if (!world.isAirBlock(x, y, z)) {
                            continue;
                        }

                        hasher.reset();

                        hasher.feed(world.getSeed());
                        hasher.feed(world.provider.dimensionId);
                        hasher.feed(x);
                        hasher.feed(y);
                        hasher.feed(z);

                        rng2.setSeed(hasher.finish());

                        for (int i = 0, negativeSize = negative.size(); i < negativeSize; i++) {
                            Ellipsoid e = negative.get(i);

                            if (e.dist2(rng2, x - cX + 0.5f, y - cY + 0.5f, z - cZ + 0.5f) <= 1) {
                                continue outer;
                            }
                        }

                        float dist = 2f;

                        for (int i = 0, positiveSize = positive.size(); i < positiveSize; i++) {
                            Ellipsoid e = positive.get(i);

                            dist = Math.min(dist, e.dist2(rng2, x - cX + 0.5f, y - cY + 0.5f, z - cZ + 0.5f));
                        }

                        if (dist >= 1) continue;

                        boolean placedAnything = false;

                        // try to place big ores, if the ore vein creates big ore
                        if (ore.generatesBigOre()) {
                            if (!placedAnything) {
                                placedAnything = generateOreBlock(
                                    this.asteroidConfig,
                                    rng2,
                                    world,
                                    x,
                                    y,
                                    z,
                                    stoneType,
                                    ore,
                                    radius < 5 ? rng2.nextFloat() : dist,
                                    dist);
                            }
                        }

                        // try to place any special blocks
                        if (!placedAnything) {
                            placedAnything = generateSpecialBlocks(
                                dimensionDef,
                                rng2,
                                world,
                                this.asteroidConfig,
                                x,
                                y,
                                z,
                                dist < 1f / 3f ? TargetBlockPosition.AsteroidInnerCore
                                    : dist < 2f / 3f ? TargetBlockPosition.AsteroidCore
                                        : TargetBlockPosition.AsteroidShell);
                        }

                        // try to place small ores
                        if (!placedAnything) {
                            placedAnything = generateSmallOreBlock(
                                this.asteroidConfig,
                                rng2,
                                world,
                                x,
                                y,
                                z,
                                stoneType,
                                ore,
                                rng2.nextFloat());
                        }

                        // no smallores either? do normal block
                        if (!placedAnything) {
                            world.setBlock(
                                x,
                                y,
                                z,
                                stoneType.getStone()
                                    .getBlock(),
                                stoneType.getStone()
                                    .getBlockMeta(),
                                2);
                        }
                    }
                }
            }

            if (chunkX == seedChunkX && chunkZ == seedChunkZ) {
                generateLootChest(world, this.asteroidConfig);
            }

            for (int z = minZ; z < maxZ; z++) {
                for (int x = minX; x < maxX; x++) {
                    world.markBlocksDirtyVertical(x, z, minY, maxY);
                }
            }
        }

        private void generateLootChest(World world, AsteroidConfig asteroidConfig) {
            XSTR rng = getRandom(world.getSeed(), seedChunkX, seedChunkZ, world.provider.dimensionId);

            if (asteroidConfig.LootChestChance == 0 || size < 6) return;

            GalacticGreg.Logger.trace("Random loot chest enabled, flipping the coin");

            // Loot chest is 1 in 100 (Was: 1:1000 which actually never happened)
            if (asteroidConfig.LootChestChance < rng.nextInt(100)) return;

            GalacticGreg.Logger.debug("We got a match. Preparing to generate the loot chest");

            int x = cX, y = cY, z = cZ;

            // Move it one away from the chunk border to prevent cascading
            if (x % 16 == 0) x++;
            if (x % 16 == 15) x--;

            if (y % 16 == 0) y++;
            if (y % 16 == 15) y--;

            if (z % 16 == 0) z++;
            if (z % 16 == 15) z--;

            // Make sure it's hidden
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (world.isAirBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ)) {
                    return;
                }
            }

            GalacticGreg.Logger.trace("Now generating LootChest and contents");

            // Get amount of items for the loot chests, randomize it (1-num) if enabled
            int tNumLootItems;
            if (asteroidConfig.RandomizeNumLootItems) {
                tNumLootItems = rng.nextInt(asteroidConfig.NumLootItems - 1) + 1;
            } else {
                tNumLootItems = asteroidConfig.NumLootItems;
            }

            GalacticGreg.Logger.debug(String.format("Loot chest random item count will be: %d", tNumLootItems));

            // Get items for the configured loot-table
            WeightedRandomChestContent[] tRandomLoot = ChestGenHooks
                .getItems(DynamicDimensionConfig.getLootChestTable(asteroidConfig), rng);

            // Get chest-block to spawn
            ImmutableBlockMeta tTargetChestType = GalacticGreg.GalacticConfig.CustomLootChest;

            // Place down the chest
            world.setBlock(cX, cY, cZ, tTargetChestType.getBlock(), tTargetChestType.getBlockMeta(), 2);

            // Retrieve the TEs IInventory that should've been created
            IInventory entityChestInventory = (IInventory) world.getTileEntity(cX, cY, cZ);

            // If it's not null...
            if (entityChestInventory != null) {
                // and if we're on the server...
                if (!world.isRemote) {
                    // Fill the chest with stuffz!
                    WeightedRandomChestContent
                        .generateChestContents(rng, tRandomLoot, entityChestInventory, tNumLootItems);
                    GalacticGreg.Logger.trace("Loot chest successfully generated");
                }
            } else {
                // Something made a boo..
                GalacticGreg.Logger
                    .warn("Could not create lootchest at X[%d] Y[%d] Z[%d]. getTileEntity() returned null", cX, cY, cZ);
            }

            // Do some debug logging
            GalacticGreg.Logger.debug("Generated LootChest at X[%d] Y[%d] Z[%d]", cX, cY, cZ);
        }
    }

    /** A random number which gets added to the XSTR because I felt like it */
    private static final long OFFSET = 588283;

    private static XSTR getRandom(long worldSeed, int chunkX, int chunkZ, int dimId) {
        return new XSTR(chunkX * 341873128712L + chunkZ * 132897987541L + dimId + OFFSET + worldSeed);
    }

    public static boolean generatesAsteroid(long worldSeed, int chunkX, int chunkZ, int dimId, int asteroidChance) {
        return getRandom(worldSeed, chunkX, chunkZ, dimId).nextInt(100) <= asteroidChance;
    }

    /**
     * Generate Special Blocks in asteroids if enabled
     */
    private static boolean generateSpecialBlocks(ModDimensionDef dimensionDef, Random rng, World world,
        AsteroidConfig asteroidConfig, int x, int y, int z, TargetBlockPosition blockPosition) {
        // Handler to generate special BlockTypes randomly if activated
        if (asteroidConfig.SpecialBlockChance > 0) {
            if (rng.nextInt(100) < asteroidConfig.SpecialBlockChance) {
                SpecialBlockComb bmc = dimensionDef.getRandomSpecialAsteroidBlock(rng);

                if (bmc != null) {
                    boolean validLocation = switch (bmc.getBlockPosition()) {
                        case AsteroidCore -> blockPosition == Enums.TargetBlockPosition.AsteroidCore;
                        case AsteroidCoreAndShell -> blockPosition == Enums.TargetBlockPosition.AsteroidCore
                            || blockPosition == Enums.TargetBlockPosition.AsteroidShell;
                        case AsteroidShell -> blockPosition == Enums.TargetBlockPosition.AsteroidShell;
                        case AsteroidInnerCore -> blockPosition == Enums.TargetBlockPosition.AsteroidInnerCore;
                    };

                    if (validLocation) {
                        world.setBlock(x, y, z, bmc.getBlock(), bmc.getBlockMeta(), 3);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean generateOreBlock(AsteroidConfig asteroidConfig, Random rng, World pWorld, int pX, int pY,
        int pZ, IStoneType stoneType, IWorldgenLayer oreLayer, float control, float dist) {
        if (rng.nextFloat() <= oreLayer.getDensity() * asteroidConfig.OreDensityMultiplier) {
            IOreMaterial mat = oreLayer.getOre(control);

            if (mat != null) {
                return OreManager.setOreForWorldGen(pWorld, pX, pY, pZ, stoneType, mat, false);
            }
        }

        return false;
    }

    private static boolean generateSmallOreBlock(AsteroidConfig asteroidConfig, Random rng, World pWorld, int pX,
        int pY, int pZ, IStoneType stoneType, IWorldgenLayer oreLayer, float control) {
        if (rng.nextInt(100) < asteroidConfig.SmallOreChance) {
            IOreMaterial mat = oreLayer.getOre(control);

            if (mat != null) {
                return OreManager.setOreForWorldGen(pWorld, pX, pY, pZ, stoneType, mat, true);
            }
        }

        return false;
    }

    private static class Ellipsoid {

        public float x, y, z, r;

        public Ellipsoid(float x, float y, float z, float size) {
            this.x = x;
            this.y = y;
            this.z = z;
            r = 1 / size;
        }

        public float dist2(Random rng, float dx, float dy, float dz) {
            float distX = (dx - x) * r;
            float distY = (dy - y) * r;
            float distZ = (dz - z) * r;

            float dist = distX * distX + distY * distY + distZ * distZ;

            if (dist > 1) return dist;

            if (dist > 0.8) {
                float f = rng.nextFloat();
                dist += f * 0.4;
            }

            return dist;
        }
    }
}
