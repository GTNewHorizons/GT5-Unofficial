package galacticgreg;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.AsteroidBlockComb;
import galacticgreg.api.BlockMetaComb;
import galacticgreg.api.Enums;
import galacticgreg.api.ISpaceObjectGenerator;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.StructureInformation;
import galacticgreg.auxiliary.GTOreGroup;
import galacticgreg.dynconfig.DynamicDimensionConfig;
import galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.GTWorldgenerator;
import gregtech.common.ores.OreManager;

public class WorldGeneratorSpace implements IWorldGenerator {

    private long mProfilingStart;
    private long mProfilingEnd;

    public WorldGeneratorSpace() {
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
    }

    @Override
    public void generate(Random pRandom, int pX, int pZ, World pWorld, IChunkProvider pChunkGenerator,
        IChunkProvider pChunkProvider) {
        pX *= 16;
        pZ *= 16;

        String tBiome = pWorld.getBiomeGenForCoords(pX + 8, pZ + 8).biomeName;
        pRandom = new Random(pRandom.nextInt());
        if (tBiome == null) {
            tBiome = BiomeGenBase.plains.biomeName;
        }
        GalacticGreg.Logger
            .trace("Triggered generate: [ChunkGenerator %s] [Biome %s]", pChunkGenerator.toString(), tBiome);

        ModDimensionDef tDimDef = GalacticGregRegistry.getDimensionTypeByChunkGenerator(pChunkGenerator);

        if (tDimDef == null) {
            GalacticGreg.Logger.trace(
                "Ignoring ChunkGenerator type %s as there is no definition for it in the registry",
                pChunkGenerator.toString());
            return;
        } else {
            GalacticGreg.Logger.trace("Selected DimDef: [%s]", tDimDef.getDimIdentifier());
        }

        /*
         * In some later addons maybe, not for now. Ignoring Biome-based worldgen String tBiome =
         * pWorld.getBiomeGenForCoords(pX + 8, pZ + 8).biomeName; pRandom = new Random(pRandom.nextInt()); if (tBiome ==
         * null) { tBiome = BiomeGenBase.plains.biomeName; }
         */

        if (tDimDef.getDimensionType() != Enums.DimensionType.Planet) {
            if (tDimDef.getRandomAsteroidMaterial() == null) GalacticGreg.Logger.error(
                "Dimension [%s] is set to Asteroids, but no asteroid material is specified! Nothing will generate",
                tDimDef.getDimensionName());
            else generateAsteroids(tDimDef, pRandom, pWorld, pX, pZ);
        } else if (tDimDef.getDimensionType() != Enums.DimensionType.Asteroid) {
            generateOreVeins(tDimDef, pRandom, pWorld, pX, pZ, "", pChunkGenerator, pChunkProvider);
        }

        Chunk tChunk = pWorld.getChunkFromBlockCoords(pX, pZ);
        if (tChunk != null) {
            tChunk.isModified = true;
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
            if (GalacticGreg.GalacticConfig.ProfileOreGen) mProfilingStart = System.currentTimeMillis();
            // -----------------------------

            // Get Random position
            int tX = pX + pRandom.nextInt(16);
            int tY = 50 + pRandom.nextInt(200 - 50);
            int tZ = pZ + pRandom.nextInt(16);

            // Check if position is free
            if (pWorld.isAirBlock(tX, tY, tZ)) {

                // Select Random OreGroup and Asteroid Material
                GTOreGroup oreGroup = WorldgenOreLayerSpace.getRandomOreGroup(pDimensionDef, pRandom, true);

                AsteroidBlockComb asteroidStone = pDimensionDef.getRandomAsteroidMaterial();
                if (asteroidStone == null) return;

                GalacticGreg.Logger.debug(
                    "Asteroid will be build with: Block: [%s] OreType: [%s]",
                    Block.blockRegistry.getNameForObject(asteroidStone.getBlock()),
                    asteroidStone.getStone()
                        .toString());

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
                aGen.randomize(dimAsteroidConfig.MinSize, dimAsteroidConfig.MaxSize); // Initialize random values and set size
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

                        // === Ore generator >>
                        boolean tPlacedAnything = false;
                        // If a valid oregroup has been selected (more than 0 ore-veins are enabled for this dim)
                        if (oreGroup != null) {
                            // GalacticGreg.Logger.trace("tOreGroup is populated, continuing");
                            // Choose a number between 0 and 100
                            int ranOre = pRandom.nextInt(100);
                            IMaterial ore = null;

                            // If chosen number is below the configured orechance, do random between and sporadic
                            if (ranOre < dimAsteroidConfig.OreChance) {
                                if (pRandom.nextBoolean()) {
                                    // Only take as final value if meta is not zero
                                    if (oreGroup.SporadicBetween != null)
                                        ore = oreGroup.SporadicBetween;
                                } else {
                                    // Only take as final value if meta is not zero
                                    if (oreGroup.SporadicAround != null) ore = oreGroup.SporadicAround;
                                }
                            }
                            // If chosen number is below the configured orechance, do random primary and secondary
                            // We use an offset here, so this part is always higher than the first check.
                            else if (ranOre < dimAsteroidConfig.OreChance + dimAsteroidConfig.OrePrimaryOffset) {
                                if (pRandom.nextBoolean()) {
                                    // Only take as final value if meta is not zero
                                    if (oreGroup.Primary != null) ore = oreGroup.Primary;
                                } else {
                                    // Only take as final value if meta is not zero
                                    if (oreGroup.Secondary != null) ore = oreGroup.Secondary;
                                }
                            }

                            // if the final oreMeta has been found...
                            // GalacticGreg.Logger.info("tFinalOreMeta is %d", tFinalOreMeta);
                            if (ore != null) {
                                // make sure we obey the configured "HiddenOres" setting (No ores on the shell)
                                if (dimAsteroidConfig.HiddenOres && (si.getBlockPosition() == Enums.TargetBlockPosition.AsteroidShell)) {
                                    // Ore would be placed around the shell, which is disabled (hiddenores)
                                    GalacticGreg.Logger.trace(
                                        "Skipping ore-placement event (HiddenOres=true; TargetBlockPosition=AsteroidShell)");
                                } else {
                                    // try to place the ore block
                                    tPlacedAnything = OreManager.setOreForWorldGen(
                                        pWorld,
                                        si.getX(),
                                        si.getY(),
                                        si.getZ(),
                                        asteroidStone.getStone(),
                                        oreGroup.Secondary,
                                        false);
                                }
                            }
                        }
                        // << Ore generator ===

                        // === Additional special blocks >>

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


                        // No special block placed? Try smallores
                        if (!tPlacedAnything) {
                            tPlacedAnything = generateSmallOreBlock(
                                pDimensionDef,
                                pRandom,
                                pWorld,
                                dimAsteroidConfig,
                                si.getX(),
                                si.getY(),
                                si.getZ(),
                                asteroidStone);
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
            if (GalacticGreg.GalacticConfig.ProfileOreGen) {
                try {
                    mProfilingEnd = System.currentTimeMillis();
                    long tTotalTime = mProfilingEnd - mProfilingStart;
                    GalacticGreg.Profiler.AddTimeToList(pDimensionDef, tTotalTime);
                    GalacticGreg.Logger.debug(
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

    /**
     * Pick a random small-ore block from the list of enabled small ores for this dim
     *
     * @param pDimDef
     * @param pRandom
     * @return
     */
    private boolean generateSmallOreBlock(ModDimensionDef pDimDef, Random pRandom, World pWorld,
        DynamicDimensionConfig.AsteroidConfig pAConf, int pX, int pY, int pZ, AsteroidBlockComb asteroidStone) {
        // If smallores are enabled...
        if (pAConf.SmallOreChance > 0) {
            // ... and we hit the random-chance ...
            if (pRandom.nextInt(100) < pAConf.SmallOreChance) {

                // First find a small ore...
                for (int i = 0; (i < 256); i++) {
                    int tRandomWeight = pRandom.nextInt(WorldgenOreLayerSpace.sWeight);
                    for (GTWorldgen tWorldGen : GalacticGreg.smallOreWorldgenList) {

                        if (!(tWorldGen instanceof WorldgenOreSmallSpace oreSmallSpace)) continue;

                        // That is enabled for *this* dim...
                        if (!oreSmallSpace.isEnabledForDim(pDimDef)) continue;

                        // And in the correct y-level, of ObeyLimits is true...
                        if (pAConf.ObeyHeightLimits && oreSmallSpace.isAllowedForHeight(pY))
                            continue;

                        // Care about weight
                        tRandomWeight -= oreSmallSpace.mAmount;
                        if (tRandomWeight <= 0) {
                            OreManager.setOreForWorldGen(pWorld, pX, pY, pZ, asteroidStone.getStone(), oreSmallSpace.mMaterial, true);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void generateOreVeins(ModDimensionDef pDimensionDef, Random pRandom, World pWorld, int pX, int pZ,
        String pBiome, IChunkProvider pChunkGenerator, IChunkProvider pChunkProvider) {
        GalacticGreg.Logger.trace("Running orevein-gen in Dim %s", pDimensionDef.getDimIdentifier());

        if (GTWorldgenerator.isOreChunk(pX / 16, pZ / 16)) {
            if (WorldgenOreLayerSpace.sWeight > 0 && !GalacticGreg.oreVeinWorldgenList.isEmpty()) {

                outer: for (int i = 0; i < 256; i++) {
                    int tRandomWeight = pRandom.nextInt(WorldgenOreLayerSpace.sWeight);

                    for (GTWorldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) {
                        if (tWorldGen instanceof WorldgenOreLayerSpace)
                            tRandomWeight -= ((WorldgenOreLayerSpace) tWorldGen).mWeight;

                        if (tRandomWeight <= 0) {
                            try {
                                if (tWorldGen.executeWorldgen(
                                    pWorld,
                                    pRandom,
                                    pBiome,
                                    Integer.MIN_VALUE,
                                    pX,
                                    pZ,
                                    pChunkGenerator,
                                    pChunkProvider)) {
                                    break outer;
                                }
                            } catch (Throwable e) {
                                e.printStackTrace(GTLog.err);
                            }
                            break;
                        }
                    }
                }
            }
            // Generate Small Ores

            int i = 0;
            for (int tX = pX - 16; i < 3; tX += 16) {
                int j = 0;
                for (int tZ = pZ - 16; j < 3; tZ += 16) {
                    for (GTWorldgen tWorldGen : GalacticGreg.smallOreWorldgenList) {
                        try {
                            tWorldGen.executeWorldgen(
                                pWorld,
                                pRandom,
                                "",
                                Integer.MIN_VALUE,
                                tX,
                                tZ,
                                pChunkGenerator,
                                pChunkProvider);
                        } catch (Throwable e) {
                            e.printStackTrace(GTLog.err);
                        }
                    }
                    j++;
                }
                i++;
            }
        }
        GalacticGreg.Logger.trace("Leaving orevein-gen for Dim %s", pDimensionDef.getDimIdentifier());
    }
}
