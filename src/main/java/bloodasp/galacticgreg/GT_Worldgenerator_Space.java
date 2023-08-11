package bloodasp.galacticgreg;

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

import bloodasp.galacticgreg.api.*;
import bloodasp.galacticgreg.api.Enums.DimensionType;
import bloodasp.galacticgreg.api.Enums.SpaceObjectType;
import bloodasp.galacticgreg.api.Enums.TargetBlockPosition;
import bloodasp.galacticgreg.auxiliary.GTOreGroup;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_SmallOre_Space;
import bloodasp.galacticgreg.dynconfig.DynamicDimensionConfig;
import bloodasp.galacticgreg.dynconfig.DynamicDimensionConfig.AsteroidConfig;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.GT_Worldgenerator;

public class GT_Worldgenerator_Space implements IWorldGenerator {

    public static boolean sAsteroids = true;
    private final EventBus eventBus = new EventBus();
    private World worldObj;

    private int chunkX;
    private int chunkZ;
    private int mSize = 100;

    private long mProfilingStart;
    private long mProfilingEnd;

    public GT_Worldgenerator_Space() {
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
    }

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

        if (tDimDef.getDimensionType() != DimensionType.Planet) {
            if (tDimDef.getRandomAsteroidMaterial() == null) GalacticGreg.Logger.error(
                    "Dimension [%s] is set to Asteroids, but no asteroid material is specified! Nothing will generate",
                    tDimDef.getDimensionName());
            else Generate_Asteroids(tDimDef, pRandom, pWorld, pX, pZ);
        } else if (tDimDef.getDimensionType() != DimensionType.Asteroid) {
            Generate_OreVeins(tDimDef, pRandom, pWorld, pX, pZ, "", pChunkGenerator, pChunkProvider);
        }

        Chunk tChunk = pWorld.getChunkFromBlockCoords(pX, pZ);
        if (tChunk != null) {
            tChunk.isModified = true;
        }
    }

    private void Generate_Asteroids(ModDimensionDef pDimensionDef, Random pRandom, World pWorld, int pX, int pZ) {
        GalacticGreg.Logger.trace("Running asteroid-gen in Dim %s", pDimensionDef.getDimIdentifier());

        AsteroidConfig tAConf = DynamicDimensionConfig.getAsteroidConfig(pDimensionDef);
        if (tAConf == null) {
            GalacticGreg.Logger.error(
                    "Dimension %s is set to asteroid, but no config object can be found. Skipping!",
                    pDimensionDef.getDimIdentifier());
            return;
        } else {
            GalacticGreg.Logger.trace("Asteroid probability: %d", tAConf.Probability);
        }

        if ((tAConf.Probability <= 1) || (pRandom.nextInt(tAConf.Probability) == 0)) {
            GalacticGreg.Logger.trace("Generating asteroid NOW");
            // ---------------------------
            if (GalacticGreg.GalacticConfig.ProfileOreGen) mProfilingStart = System.currentTimeMillis();
            // -----------------------------

            // Get Random position
            int tX = pX + pRandom.nextInt(16);
            int tY = 50 + pRandom.nextInt(200 - 50);
            int tZ = pZ + pRandom.nextInt(16);

            // Check if position is free
            if ((pWorld.getBlock(tX, tY, tZ).isAir(pWorld, tX, tY, tZ))) {

                int tCustomAsteroidOffset = -1;
                int tGraniteMeta = 0;

                // Select Random OreGroup and Asteroid Material
                GTOreGroup tOreGroup = GT_Worldgen_GT_Ore_Layer_Space.getRandomOreGroup(pDimensionDef, pRandom, true);
                AsteroidBlockComb tABComb = pDimensionDef.getRandomAsteroidMaterial();
                if (tABComb == null) return;

                // Fill Vars for random Asteroid
                Block tFinalAsteroidBlock = tABComb.getBlock();
                int tFinalAsteroidBlockMeta = tABComb.getMeta();
                int tFinalOreOffset = tABComb.getOreMaterial().getOffset();
                int tFinalUpdateMode = tABComb.getOreMaterial().getUpdateMode();
                GalacticGreg.Logger.debug(
                        "Asteroid will be build with: Block: [%s] OreType: [%s]",
                        Block.blockRegistry.getNameForObject(tABComb.getBlock()),
                        tABComb.getOreMaterial().toString());

                // get random Ore-asteroid generator from the list of registered generators
                ISpaceObjectGenerator aGen = pDimensionDef.getRandomSOGenerator(SpaceObjectType.OreAsteroid);
                if (aGen == null) {
                    GalacticGreg.Logger.ot_error(
                            "GalacticGreg.Generate_Asteroids.NoSOGenFound",
                            "No SpaceObjectGenerator has been registered for type ORE_ASTEROID in Dimension %s. Nothing will generate",
                            pDimensionDef.getDimensionName());
                    return;
                }

                aGen.reset();
                aGen.setCenterPoint(tX, tY, tZ);
                aGen.randomize(tAConf.MinSize, tAConf.MaxSize); // Initialize random values and set size
                aGen.calculate(); // Calculate structure

                // Random loot-chest somewhere in the asteroid
                Vec3 tChestPosition = Vec3.createVectorHelper(0, 0, 0);
                boolean tDoLootChest = false;
                int tNumLootItems = 0;
                if (tAConf.LootChestChance > 0) {
                    GalacticGreg.Logger.trace("Random loot chest enabled, flipping the coin");
                    int tChance = pRandom.nextInt(100); // Loot chest is 1 in 100 (Was: 1:1000 which actually never
                                                        // happend)
                    if (tAConf.LootChestChance >= tChance) {
                        GalacticGreg.Logger.debug("We got a match. Preparing to generate the loot chest");
                        // Get amount of items for the loot chests, randomize it (1-num) if enabled
                        if (tAConf.RandomizeNumLootItems) tNumLootItems = pRandom.nextInt(tAConf.NumLootItems - 1) + 1;
                        else tNumLootItems = tAConf.NumLootItems;

                        GalacticGreg.Logger
                                .debug(String.format("Loot chest random item count will be: %d", tNumLootItems));

                        // try to find any block that is not on the asteroids outer-shell
                        GalacticGreg.Logger.trace("Starting lookup for valid asteroid-block for the chest");
                        for (int x = 0; x < 64; x++) // 64 enough? Should be
                        {
                            int tRndBlock = pRandom.nextInt(aGen.getStructure().size());
                            StructureInformation tChestSI = aGen.getStructure().get(tRndBlock);
                            if (tChestSI.getBlockPosition() != TargetBlockPosition.AsteroidShell) {
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
                                        .getItems(DynamicDimensionConfig.getLootChestTable(tAConf), pRandom);

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
                                GalacticGreg.Logger.debug(
                                        "Generated LootChest at X[%d] Y[%d] Z[%d]",
                                        si.getX(),
                                        si.getY(),
                                        si.getZ());
                                // And skip the rest of this function
                                continue;
                            }
                        }
                        // << Loot-chest generator ===

                        // === Ore generator >>
                        boolean tPlacedOreBlock = false;
                        // If a valid oregroup has been selected (more than 0 ore-veins are enabled for this dim)
                        if (tOreGroup != null) {
                            // GalacticGreg.Logger.trace("tOreGoup is populated, continuing");
                            // Choose a number between 0 and 100
                            int ranOre = pRandom.nextInt(100);
                            int tFinalOreMeta = 0;

                            // If choosen number is below the configured orechance, do random between and sporadic
                            if (ranOre < tAConf.OreChance) {
                                if (pRandom.nextBoolean()) {
                                    // Only take as final value if meta is not zero
                                    if (tOreGroup.SporadicBetweenMeta > 0)
                                        tFinalOreMeta = tOreGroup.SporadicBetweenMeta;
                                } else {
                                    // Only take as final value if meta is not zero
                                    if (tOreGroup.SporadicAroundMeta > 0) tFinalOreMeta = tOreGroup.SporadicAroundMeta;
                                }
                            }
                            // If choosen number is below the configured orechance, do random primary and secondary
                            // We use an offset here, so this part is always higher than the first check.
                            else if (ranOre < tAConf.OreChance + tAConf.OrePrimaryOffset) {
                                if (pRandom.nextBoolean()) {
                                    // Only take as final value if meta is not zero
                                    if (tOreGroup.PrimaryMeta > 0) tFinalOreMeta = tOreGroup.PrimaryMeta;
                                } else {
                                    // Only take as final value if meta is not zero
                                    if (tOreGroup.SecondaryMeta > 0) tFinalOreMeta = tOreGroup.SecondaryMeta;
                                }
                            }

                            // if the final oreMeta has been found...
                            // GalacticGreg.Logger.info("tFinalOreMeta is %d", tFinalOreMeta);
                            if (tFinalOreMeta > 0) {
                                // make sure we obey the configured "HiddenOres" setting (No ores on the shell)
                                if (tAConf.HiddenOres && (si.getBlockPosition() == TargetBlockPosition.AsteroidShell)) {
                                    // Ore would be placed around the shell, which is disabled (hiddenores)
                                    GalacticGreg.Logger.trace(
                                            "Skipping ore-placement event (HiddenOres=true; TargetBlockPosition=AsteroidShell)");
                                } else {
                                    // try to place the ore block. The result is stored in tPlacedOreBlock
                                    tPlacedOreBlock = GT_TileEntity_Ores_Space.setOuterSpaceOreBlock(
                                            pDimensionDef,
                                            pWorld,
                                            si.getX(),
                                            si.getY(),
                                            si.getZ(),
                                            tOreGroup.SecondaryMeta,
                                            true,
                                            tFinalOreOffset);
                                }
                            }
                        }
                        // << Ore generator ===

                        // === Additional special blocks >>
                        // If no ore-block has been placed yet...
                        if (!tPlacedOreBlock) {
                            // try to spawn special blocks
                            boolean tFlag = doGenerateSpecialBlocks(
                                    pDimensionDef,
                                    pRandom,
                                    pWorld,
                                    tAConf,
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    si.getBlockPosition());

                            // No special block placed? Try smallores
                            if (tFlag) tFlag = doGenerateSmallOreBlock(
                                    pDimensionDef,
                                    pRandom,
                                    pWorld,
                                    tAConf,
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    tFinalOreOffset);

                            // no smallores either? do normal block
                            if (tFlag) pWorld.setBlock(
                                    si.getX(),
                                    si.getY(),
                                    si.getZ(),
                                    tFinalAsteroidBlock,
                                    tFinalAsteroidBlockMeta,
                                    tFinalUpdateMode);

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
    private boolean doGenerateSpecialBlocks(ModDimensionDef pDimensionDef, Random pRandom, World pWorld,
            AsteroidConfig tAConf, int eX, int eY, int eZ, TargetBlockPosition pBlockPosition) {

        boolean tFlag = true;
        // Handler to generate special BlockTypes randomly if activated
        if (tAConf.SpecialBlockChance > 0) {
            if (pRandom.nextInt(100) < tAConf.SpecialBlockChance) {
                SpecialBlockComb bmc = pDimensionDef.getRandomSpecialAsteroidBlock();
                if (bmc != null) {
                    boolean tIsAllowed = false;

                    switch (bmc.getBlockPosition()) {
                        case AsteroidCore:
                            if (pBlockPosition == TargetBlockPosition.AsteroidCore) tIsAllowed = true;
                            break;
                        case AsteroidCoreAndShell:
                            if (pBlockPosition == TargetBlockPosition.AsteroidCore
                                    || pBlockPosition == TargetBlockPosition.AsteroidShell)
                                tIsAllowed = true;
                            break;
                        case AsteroidShell:
                            if (pBlockPosition == TargetBlockPosition.AsteroidShell) tIsAllowed = true;
                            break;
                        case AsteroidInnerCore:
                            if (pBlockPosition == TargetBlockPosition.AsteroidInnerCore) tIsAllowed = true;
                            break;
                        default:
                            break;
                    }

                    if (tIsAllowed) {
                        pWorld.setBlock(eX, eY, eZ, bmc.getBlock(), bmc.getMeta(), 2);
                        tFlag = false;
                    }
                }
            }
        }
        return tFlag;
    }

    /**
     * Pick a random small-ore block from the list of enabled small ores for this dim
     *
     * @param pDimDef
     * @param pRandom
     * @return
     */
    private boolean doGenerateSmallOreBlock(ModDimensionDef pDimDef, Random pRandom, World pWorld,
            AsteroidConfig pAConf, int pX, int pY, int pZ, int pTargetBlockOffset) {
        boolean tFlag = true;
        // If smallores are enabled...
        if (pAConf.SmallOreChance > 0) {
            // ... and we hit the random-chance ...
            if (pRandom.nextInt(100) < pAConf.SmallOreChance) {
                // Do small ores.
                int tRandomWeight;
                boolean continueSearch = true;
                int tFoundOreMeta = -1;
                BW_Worldgen_Ore_SmallOre_Space bwOreGen = null;
                // First find a small ore...
                for (int i = 0; (i < 256) && (continueSearch); i++) {
                    tRandomWeight = pRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
                    for (GT_Worldgen tWorldGen : GalacticGreg.smallOreWorldgenList) {
                        if (tWorldGen instanceof BW_Worldgen_Ore_SmallOre_Space) {
                            tRandomWeight = ((BW_Worldgen_Ore_SmallOre_Space) tWorldGen).mDensity;
                            if (tRandomWeight <= 0) {
                                tFoundOreMeta = ((BW_Worldgen_Ore_SmallOre_Space) tWorldGen).mPrimaryMeta;
                                continueSearch = false;
                                bwOreGen = ((BW_Worldgen_Ore_SmallOre_Space) tWorldGen);
                            }
                        } else if (tWorldGen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space) {
                            // That is enabled for *this* dim...
                            if (!((GT_Worldgen_GT_Ore_SmallPieces_Space) tWorldGen).isEnabledForDim(pDimDef)) continue;

                            // And in the correct y-level, of ObeyLimits is true...
                            if (pAConf.ObeyHeightLimits
                                    && !((GT_Worldgen_GT_Ore_SmallPieces_Space) tWorldGen).isAllowedForHeight(pY))
                                continue;

                            // Care about weight
                            tRandomWeight -= ((GT_Worldgen_GT_Ore_SmallPieces_Space) tWorldGen).mAmount;
                            if (tRandomWeight <= 0) {
                                // And return found ore meta
                                tFoundOreMeta = ((GT_Worldgen_GT_Ore_SmallPieces_Space) tWorldGen).mMeta;
                                continueSearch = false;
                            }
                        }
                    }
                }
                if (tFoundOreMeta > -1) {
                    if (bwOreGen == null) {
                        // Make the oreID a small ore with correct type
                        int tCustomOffset = (GTOreTypes.SmallOres.getOffset() + pTargetBlockOffset);

                        // Set the smallOre block
                        GT_TileEntity_Ores_Space
                                .setOuterSpaceOreBlock(pDimDef, pWorld, pX, pY, pZ, tFoundOreMeta, true, tCustomOffset);
                        tFlag = false;
                    } else {
                        bwOreGen.setOreBlock(pWorld, pX, pY, pZ, tFoundOreMeta, true);
                    }
                }
            }
        }
        return tFlag;
    }

    /**
     * Untested! But should work... Comments are todo
     *
     * @param pDimensionDef
     * @param pRandom
     * @param pWorld
     * @param pX
     * @param pZ
     * @param pBiome
     * @param pChunkGenerator
     * @param pChunkProvider
     */
    private void Generate_OreVeins(ModDimensionDef pDimensionDef, Random pRandom, World pWorld, int pX, int pZ,
            String pBiome, IChunkProvider pChunkGenerator, IChunkProvider pChunkProvider) {
        GalacticGreg.Logger.trace("Running orevein-gen in Dim %s", pDimensionDef.getDimIdentifier());

        if (GT_Worldgenerator.isOreChunk(pX / 16, pZ / 16)) {
            if ((GT_Worldgen_GT_Ore_Layer_Space.sWeight > 0) && (GalacticGreg.oreVeinWorldgenList.size() > 0)) {

                boolean temp = true;
                int tRandomWeight;
                for (int i = 0; (i < 256) && (temp); i++) {
                    tRandomWeight = pRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
                    for (GT_Worldgen tWorldGen : GalacticGreg.oreVeinWorldgenList) {
                        if (tWorldGen instanceof GT_Worldgen_GT_Ore_Layer_Space)
                            tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;
                        else if (tWorldGen instanceof BW_Worldgen_Ore_Layer_Space)
                            tRandomWeight -= ((BW_Worldgen_Ore_Layer_Space) tWorldGen).mWeight;
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
                                    temp = false;
                                }
                            } catch (Throwable e) {
                                e.printStackTrace(GT_Log.err);
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
                    for (GT_Worldgen tWorldGen : GalacticGreg.smallOreWorldgenList) {
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
                            e.printStackTrace(GT_Log.err);
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
