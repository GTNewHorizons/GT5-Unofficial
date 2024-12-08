package galacticgreg;

import static gregtech.api.enums.GTValues.profileWorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.AsteroidBlockComb;
import galacticgreg.api.BlockMetaComb;
import galacticgreg.api.Enums;
import galacticgreg.api.ISpaceObjectGenerator;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.StructureInformation;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.DimensionDef.DimNames;
import galacticgreg.dynconfig.DynamicDimensionConfig;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.objects.XSTR;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.config.Worldgen;
import gregtech.common.ores.OreManager;
import gregtech.common.worldgen.IWorldgenLayer;
import gregtech.common.worldgen.WorldgenQuery;

public class WorldGeneratorSpace implements IWorldGenerator {

    private long mProfilingStart;
    private long mProfilingEnd;

    private static boolean generateEndAsteroids = true;
    private static int mEndAsteroidProbability;
    private static int mEndAsteroidMinSize;
    private static int mEndAsteroidMaxSize;

    public WorldGeneratorSpace() {
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);

        generateEndAsteroids = Worldgen.endAsteroids.generateEndAsteroids;
        mEndAsteroidProbability = Worldgen.endAsteroids.EndAsteroidProbability;
        mEndAsteroidMinSize = Worldgen.endAsteroids.EndAsteroidMinSize;
        mEndAsteroidMaxSize = Worldgen.endAsteroids.EndAsteroidMaxSize;
    }

    private static final int END_ASTEROID_DISTANCE = 16;

    @Override
    public void generate(Random pRandom, int pX, int pZ, World pWorld, IChunkProvider pChunkGenerator,
        IChunkProvider pChunkProvider) {

        int cX = pX;
        int cZ = pZ;
        pX *= 16;
        pZ *= 16;

        pRandom = new Random(pRandom.nextInt());

        GalacticGreg.Logger
            .trace("Triggered generate: [ChunkGenerator %s]", pChunkGenerator.toString());

        ModDimensionDef tDimDef = DimensionDef.getDefForWorld(pWorld);

        if (tDimDef.getDimensionName().equals(DimNames.THE_END)) {
            if (pX * pX + pZ * pZ > END_ASTEROID_DISTANCE * END_ASTEROID_DISTANCE) {
                tDimDef = DimensionDef.EndAsteroids.modDimensionDef;
            }
        }

        if (tDimDef == null) {
            GalacticGreg.Logger.trace(
                "Ignoring dimension %s as there is no definition for it in the registry",
                pWorld.provider.getDimensionName());
            return;
        } else {
            GalacticGreg.Logger.trace("Selected DimDef: [%s]", tDimDef.getDimIdentifier());
        }

        if (tDimDef.getDimensionType() == Enums.DimensionType.Asteroid) {
            if (tDimDef.getDimensionName().equals(DimNames.ENDASTEROIDS) && generateEndAsteroids) {
                XSTR random = new XSTR(
                    pWorld.getSeed() + cX * cX * 91777L + cZ * cZ * 137413L + cX * cZ * 1853L + cX * 3L + cZ * 17L);

                if (mEndAsteroidProbability <= 1 || random.nextInt(mEndAsteroidProbability) == 0) {
                    long pre = System.nanoTime();
                    generateEndAsteroid(tDimDef, pWorld, random, cX, cZ);
                    long post = System.nanoTime();

                    if (GTValues.profileWorldGen) {
                        GTMod.GT_FML_LOGGER.info("Generated asteroid in " + (post - pre)/1e6 + "ms");
                    }
                }
            } else if (tDimDef.getRandomAsteroidMaterial() == null) {
                GalacticGreg.Logger.error(
                    "Dimension [%s] is set to Asteroids, but no asteroid material is specified! Nothing will generate",
                    tDimDef.getDimensionName());
            } else {
                generateAsteroids(tDimDef, pRandom, pWorld, pX, pZ);
            }

            Chunk tChunk = pWorld.getChunkFromBlockCoords(pX, pZ);
            if (tChunk != null) {
                tChunk.isModified = true;
            }
        }
    }

    private void generateAsteroids(ModDimensionDef pDimensionDef, Random pRandom, World pWorld, int pX, int pZ) {
        GalacticGreg.Logger.trace("Running asteroid-gen in Dim %s", pDimensionDef.getDimIdentifier());

        DynamicDimensionConfig.AsteroidConfig dimAsteroidConfig = DynamicDimensionConfig.getAsteroidConfig(pDimensionDef);
        
        if (dimAsteroidConfig == null) {
            GalacticGreg.Logger.error(
                "Dimension %s is set to asteroid, but no config object can be found. Skipping!",
                pDimensionDef.getDimIdentifier());
            return;
        } else {
            GalacticGreg.Logger.trace("Asteroid probability: %d", dimAsteroidConfig.Probability);
        }

        if (dimAsteroidConfig.Probability <= 1 || pRandom.nextInt(dimAsteroidConfig.Probability) == 0) {
            GalacticGreg.Logger.trace("Generating asteroid NOW");
            // ---------------------------
            if (GalacticGreg.GalacticConfig.ProfileOreGen || profileWorldGen) mProfilingStart = System.currentTimeMillis();
            // -----------------------------

            AsteroidBlockComb asteroidStone = pDimensionDef.getRandomAsteroidMaterial();
            if (asteroidStone == null) return;

            // Select Random OreGroup and Asteroid Material
            IWorldgenLayer oreLayer;

            int minY, maxY;

            if (pRandom.nextInt(5) == 0) {
                oreLayer = WorldgenQuery.small()
                    .inDimension(pDimensionDef)
                    .inStone(asteroidStone.getStone().getCategory())
                    .find(pRandom);

                minY = oreLayer == null ? 50 : oreLayer.getMinY();
                maxY = oreLayer == null ? 200 : oreLayer.getMaxY();
            } else {
                oreLayer = WorldgenQuery.veins()
                    .inDimension(pDimensionDef)
                    .inStone(asteroidStone.getStone().getCategory())
                    .find(pRandom);
                
                minY = oreLayer == null ? 50 : oreLayer.getMinY();
                maxY = oreLayer == null ? 200 : oreLayer.getMaxY();
            }

            if (oreLayer == null) return;
            
            // Get Random position
            int tX = pX + pRandom.nextInt(16);
            int tY = minY + pRandom.nextInt(maxY - minY);
            int tZ = pZ + pRandom.nextInt(16);

            GalacticGreg.Logger.debug(
                "Asteroid will be build with: Stone: [%s] Ore: [%s]",
                    asteroidStone.getStone(),
                    oreLayer.getName());

            // Check if position is free
            if (pWorld.isAirBlock(tX, tY, tZ)) {

                // get random Ore-asteroid generator from the list of registered generators
                ISpaceObjectGenerator aGen = pDimensionDef.getRandomSOGenerator(Enums.SpaceObjectType.OreAsteroid);
                if (aGen == null) {
                    GalacticGreg.Logger.ot_error(
                        "GalacticGreg.Generate_Asteroids.NoSOGenFound",
                        "No SpaceObjectGenerator has been registered for type ORE_ASTEROID in Dimension %s. Nothing will generate",
                        pDimensionDef.getDimensionName());
                    return;
                }

                aGen.reset();
                aGen.setCenterPoint(tX, tY, tZ);
                aGen.randomize((int) oreLayer.getSize(), (int) oreLayer.getSize() * 2); // Initialize random values and set size
                aGen.calculate(); // Calculate structure

                // Random loot-chest somewhere in the asteroid
                Vec3 tChestPosition = Vec3.createVectorHelper(0, 0, 0);
                boolean tDoLootChest = false;
                int tNumLootItems = 0;
                if (dimAsteroidConfig.LootChestChance > 0) {
                    GalacticGreg.Logger.trace("Random loot chest enabled, flipping the coin");
                    int tChance = pRandom.nextInt(100); // Loot chest is 1 in 100 (Was: 1:1000 which actually never
                                                        // happened)
                    if (dimAsteroidConfig.LootChestChance >= tChance) {
                        GalacticGreg.Logger.debug("We got a match. Preparing to generate the loot chest");
                        // Get amount of items for the loot chests, randomize it (1-num) if enabled
                        if (dimAsteroidConfig.RandomizeNumLootItems) tNumLootItems = pRandom.nextInt(dimAsteroidConfig.NumLootItems - 1) + 1;
                        else tNumLootItems = dimAsteroidConfig.NumLootItems;

                        GalacticGreg.Logger
                            .debug(String.format("Loot chest random item count will be: %d", tNumLootItems));

                        // try to find any block that is not on the asteroids outer-shell
                        GalacticGreg.Logger.trace("Starting lookup for valid asteroid-block for the chest");
                        for (int x = 0; x < 64; x++) // 64 enough? Should be
                        {
                            int tRndBlock = pRandom.nextInt(
                                aGen.getStructure()
                                    .size());
                            StructureInformation tChestSI = aGen.getStructure()
                                .get(tRndBlock);
                            if (tChestSI.getBlockPosition() != Enums.TargetBlockPosition.AsteroidShell) {
                                GalacticGreg.Logger.debug(
                                    String.format(
                                        "Chest position found [x:%d y:%d z:%d]",
                                        tChestSI.getX(),
                                        tChestSI.getY(),
                                        tChestSI.getZ()));
                                // Found valid position "Somewhere" in the asteroid, set position...
                                tChestPosition = Vec3
                                    .createVectorHelper(tChestSI.getX(), tChestSI.getY(), tChestSI.getZ());
                                // .. and set CreateFlag to true
                                tDoLootChest = true;
                                break;
                            }
                        }
                    }
                }

                // Now build the structure
                GalacticGreg.Logger.trace("Now generating Space-Structure");
                for (StructureInformation si : aGen.getStructure()) {
                    // Only replace airblocks
                    if (pWorld.isAirBlock(si.getX(), si.getY(), si.getZ())) {
                        // === Loot-chest generator >>
                        if (tDoLootChest) // If gen-lootchest enabled...
                        {
                            // Check if current x/y/z is the location where the chest shall be created
                            if ((int) tChestPosition.xCoord == si.getX() && (int) tChestPosition.yCoord == si.getY()
                                && (int) tChestPosition.zCoord == si.getZ()) {
                                GalacticGreg.Logger.trace("Now generating LootChest and contents");
                                // Get items for the configured loot-table
                                WeightedRandomChestContent[] tRandomLoot = ChestGenHooks
                                    .getItems(DynamicDimensionConfig.getLootChestTable(dimAsteroidConfig), pRandom);

                                // Get chest-block to spawn
                                BlockMetaComb tTargetChestType = GalacticGreg.GalacticConfig.CustomLootChest;

                                // Place down the chest
                                if (tTargetChestType.getMeta() > 0) pWorld.setBlock(
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    tTargetChestType.getBlock(),
                                    tTargetChestType.getMeta(),
                                    2);
                                else pWorld.setBlock(si.getX(), si.getY(), si.getZ(), tTargetChestType.getBlock());

                                // Retrieve the TEs IInventory that should've been created
                                IInventory entityChestInventory = (IInventory) pWorld
                                    .getTileEntity(si.getX(), si.getY(), si.getZ());
                                // If it's not null...
                                if (entityChestInventory != null) {
                                    // and if we're on the server...
                                    if (!pWorld.isRemote) {
                                        // Fill the chest with stuffz!
                                        WeightedRandomChestContent.generateChestContents(
                                            pRandom,
                                            tRandomLoot,
                                            entityChestInventory,
                                            tNumLootItems);
                                        GalacticGreg.Logger.trace("Loot chest successfully generated");
                                    }
                                } else {
                                    // Something made a boo..
                                    GalacticGreg.Logger.warn(
                                        "Could not create lootchest at X[%d] Y[%d] Z[%d]. getTileEntity() returned null",
                                        si.getX(),
                                        si.getY(),
                                        si.getZ());
                                }
                                // Make sure we never compare coordinates again (for this asteroid/Structure)
                                tDoLootChest = false;
                                // Do some debug logging
                                GalacticGreg.Logger
                                    .debug("Generated LootChest at X[%d] Y[%d] Z[%d]", si.getX(), si.getY(), si.getZ());
                                // And skip the rest of this function
                                continue;
                            }
                        }
                        // << Loot-chest generator ===

                        boolean tPlacedAnything = false;

                        boolean canPlaceOre = !dimAsteroidConfig.HiddenOres || si.getBlockPosition() == Enums.TargetBlockPosition.AsteroidShell;

                        // try to place big ores, if the ore vein creates big ore
                        if (canPlaceOre && oreLayer.generatesBigOre()) {
                            if (!tPlacedAnything) {
                                tPlacedAnything = generateOreBlock(
                                    pRandom,
                                    pWorld,
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    asteroidStone,
                                    oreLayer,
                                    false);
                            }
                        }

                        // try to place any special blocks
                        if (!tPlacedAnything) {
                            tPlacedAnything = generateSpecialBlocks(
                                pDimensionDef,
                                pRandom,
                                pWorld,
                                dimAsteroidConfig,
                                si.getX(),
                                si.getY(),
                                si.getZ(),
                                si.getBlockPosition());
                        }

                        // try to place small ores
                        if (canPlaceOre) {
                            if (!tPlacedAnything) {
                                tPlacedAnything = generateOreBlock(
                                    pRandom,
                                    pWorld,
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    asteroidStone,
                                    oreLayer,
                                    true);
                            }
                        }

                        // no smallores either? do normal block
                        if (!tPlacedAnything) {
                            pWorld.setBlock(
                                si.getX(),
                                si.getY(),
                                si.getZ(),
                                asteroidStone.getBlock(),
                                asteroidStone.getMeta(),
                                3);
                        }

                        // << Additional special blocks ===
                    }
                }
            }
            // ---------------------------
            // OreGen profiler stuff
            if (GalacticGreg.GalacticConfig.ProfileOreGen || profileWorldGen) {
                try {
                    mProfilingEnd = System.currentTimeMillis();
                    long tTotalTime = mProfilingEnd - mProfilingStart;
                    GalacticGreg.Profiler.AddTimeToList(pDimensionDef, tTotalTime);
                    GalacticGreg.Logger.info(
                        "Done with Asteroid-Worldgen in DimensionType %s. Generation took %d ms",
                        pDimensionDef.getDimensionName(),
                        tTotalTime);
                } catch (Exception ignored) {} // Silently ignore errors
            }
            // ---------------------------
        }
        GalacticGreg.Logger.trace("Leaving asteroid-gen for Dim %s", pDimensionDef.getDimIdentifier());
    }

    /**
     * Generate Special Blocks in asteroids if enabled
     *
     * @param pDimensionDef
     * @param pRandom
     * @param pWorld
     * @param tAConf
     * @param eX
     * @param eY
     * @param eZ
     * @return
     */
    private boolean generateSpecialBlocks(ModDimensionDef pDimensionDef, Random pRandom, World pWorld,
        DynamicDimensionConfig.AsteroidConfig tAConf, int eX, int eY, int eZ,
        Enums.TargetBlockPosition pBlockPosition) {

        // Handler to generate special BlockTypes randomly if activated
        if (tAConf.SpecialBlockChance > 0) {
            if (pRandom.nextInt(100) < tAConf.SpecialBlockChance) {
                SpecialBlockComb bmc = pDimensionDef.getRandomSpecialAsteroidBlock();
                if (bmc != null) {
                    boolean tIsAllowed = false;

                    switch (bmc.getBlockPosition()) {
                        case AsteroidCore:
                            if (pBlockPosition == Enums.TargetBlockPosition.AsteroidCore) tIsAllowed = true;
                            break;
                        case AsteroidCoreAndShell:
                            if (pBlockPosition == Enums.TargetBlockPosition.AsteroidCore
                                || pBlockPosition == Enums.TargetBlockPosition.AsteroidShell) tIsAllowed = true;
                            break;
                        case AsteroidShell:
                            if (pBlockPosition == Enums.TargetBlockPosition.AsteroidShell) tIsAllowed = true;
                            break;
                        case AsteroidInnerCore:
                            if (pBlockPosition == Enums.TargetBlockPosition.AsteroidInnerCore) tIsAllowed = true;
                            break;
                        default:
                            break;
                    }

                    if (tIsAllowed) {
                        pWorld.setBlock(eX, eY, eZ, bmc.getBlock(), bmc.getMeta(), 2);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean generateOreBlock(Random pRandom, World pWorld, int pX, int pY, int pZ, AsteroidBlockComb asteroidStone, IWorldgenLayer oreLayer, boolean small) {
        if (pRandom.nextFloat() <= oreLayer.getDensity()) {
            IMaterial mat = oreLayer.getOre(pRandom.nextFloat());

            if (mat != null) {
                OreManager.setOreForWorldGen(pWorld, pX, pY, pZ, asteroidStone.getStone(), mat, small);
                return true;
            }
        }

        return false;
    }

    private void generateEndAsteroid(ModDimensionDef tDimDef, World world, Random random2, int chunkX, int chunkZ) {
        XSTR random = new XSTR(random2.nextLong());

        int tX = chunkX * 16 + random.nextInt(16);
        int tY = 50 + random.nextInt(100 - 50);
        int tZ = chunkZ * 16 + random.nextInt(16);
        int asteroidSize = (mEndAsteroidMinSize + random.nextInt(mEndAsteroidMaxSize - mEndAsteroidMinSize + 1)) / 2;

        WorldgenGTOreLayer selectedOreLayer = WorldgenQuery.veins()
            .inDimension(tDimDef)
            .find(random);
        
        // should never happen, but let's be safe
        if (selectedOreLayer == null) return;

        if (world.isAirBlock(tX, tY, tZ)) {

            List<Ellipsoid> positive = new ArrayList<>();
            List<Ellipsoid> negative = new ArrayList<>();

            {
                int radius = random.nextInt(asteroidSize);
                positive.add(new Ellipsoid(0, 0, 0, radius));

                int k = random.nextInt(2);
                for (int i = 0; i < k; i++) {
                    negative.add(new Ellipsoid(
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 0.25f + 0.6f)));
                }

                k = random.nextInt(2);
                for (int i = 0; i < k; i++) {
                    positive.add(new Ellipsoid(
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 2 - 1),
                        radius * (random.nextFloat() * 0.25f + 0.6f)));
                }
            }

            for (int x = -asteroidSize * 2; x < asteroidSize * 2; x++) {
                for (int z = -asteroidSize * 2; z < asteroidSize * 2; z++) {
                    y: for (int y = -asteroidSize * 2; y < asteroidSize * 2; y++) {

                        if (y + tY < 0) continue;
                        if (y + tY >= 255) continue;

                        for (Ellipsoid e : negative) {
                            if (e.contains(random, x, y, z)) {
                                continue y;
                            }
                        }

                        boolean place = false;

                        for (Ellipsoid e : positive) {
                            if (e.contains(random, x, y, z)) {
                                place = true;
                                break;
                            }
                        }

                        if (!place) continue;

                        if (!world.isAirBlock(tX + x, tY + y, tZ + z)) {
                            continue;
                        }

                        int ranOre = random.nextInt(50);
                        if (ranOre < 3) {
                            OreManager.setOreForWorldGen(
                                world,
                                tX + x,
                                tY + y,
                                tZ + z,
                                StoneType.Endstone,
                                selectedOreLayer.mPrimary,
                                false);
                        } else if (ranOre < 6) {
                            OreManager.setOreForWorldGen(
                                world,
                                tX + x,
                                tY + y,
                                tZ + z,
                                StoneType.Endstone,
                                selectedOreLayer.mSecondary,
                                false);
                        } else if (ranOre < 8) {
                            OreManager.setOreForWorldGen(
                                world,
                                tX + x,
                                tY + y,
                                tZ + z,
                                StoneType.Endstone,
                                selectedOreLayer.mBetween,
                                false);
                        } else if (ranOre < 10) {
                            OreManager.setOreForWorldGen(
                                world,
                                tX + x,
                                tY + y,
                                tZ + z,
                                StoneType.Endstone,
                                selectedOreLayer.mSporadic,
                                false);
                        } else {
                            world.setBlock(tX + x, tY + y, tZ + z, Blocks.end_stone, 0, 3);
                        }
                    }
                }
            }
        }
    }

    private static class Ellipsoid {
        public float x, y, z, r;

        public Ellipsoid(float x, float y, float z, float size) {
            this.x = x;
            this.y = y;
            this.z = z;
            r = 1 / size;
        }

        public boolean contains(Random rng, float dx, float dy, float dz) {
            float distX = (dx - x) * r;
            float distY = (dy - y) * r;
            float distZ = (dz - z) * r;

            float dist = distX * distX + distY * distY + distZ * distZ;

            if (dist > 1) return false;

            if (dist > 0.8) {
                float f = rng.nextFloat();
                dist += f * 0.4;
            }

            return dist < 1;
        }
    }
}
