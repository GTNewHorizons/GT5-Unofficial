package gregtech.common;

import static gregtech.api.enums.GT_Values.debugOrevein;
import static gregtech.api.enums.GT_Values.debugWorldGen;
import static gregtech.api.enums.GT_Values.oreveinAttempts;
import static gregtech.api.enums.GT_Values.oreveinMaxPlacementAttempts;
import static gregtech.api.enums.GT_Values.oreveinPercentage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;

public class GT_Worldgenerator implements IWorldGenerator {

    private static int mEndAsteroidProbability = 300;
    private static int mSize = 100;
    private static int endMinSize = 50;
    private static int endMaxSize = 200;
    private static boolean endAsteroids = true;
    public static List<Runnable> mList = new ArrayList<>();
    public static HashSet<Long> ProcChunks = new HashSet<>();
    // This is probably not going to work. Trying to create a fake orevein to put into hashtable when there will be no
    // ores in a vein.
    public static GT_Worldgen_GT_Ore_Layer noOresInVein = new GT_Worldgen_GT_Ore_Layer(
        "NoOresInVein",
        false,
        0,
        255,
        0,
        255,
        16,
        false,
        false,
        false,
        Materials.Aluminium,
        Materials.Aluminium,
        Materials.Aluminium,
        Materials.Aluminium);
    public static Hashtable<Long, GT_Worldgen_GT_Ore_Layer> validOreveins = new Hashtable<>(1024);
    public boolean mIsGenerating = false;
    public static final Object listLock = new Object();

    public GT_Worldgenerator() {
        endAsteroids = GregTech_API.sWorldgenFile.get("endasteroids", "GenerateAsteroids", true);
        endMinSize = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidMinSize", 50);
        endMaxSize = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidMaxSize", 200);
        mEndAsteroidProbability = GregTech_API.sWorldgenFile.get("endasteroids", "AsteroidProbability", 300);
        GameRegistry.registerWorldGenerator(this, 1073741823);
        if (debugWorldGen) {
            GT_Log.out.println("GT_Worldgenerator created");
        }
    }

    @Override
    public void generate(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator,
        IChunkProvider aChunkProvider) {
        synchronized (listLock) {
            mList.add(
                new WorldGenContainer(
                    new XSTR(Math.abs(aRandom.nextInt()) + 1),
                    aX,
                    aZ,
                    aWorld.provider.dimensionId,
                    aWorld,
                    aChunkGenerator,
                    aChunkProvider,
                    aWorld.getBiomeGenForCoords(aX * 16 + 8, aZ * 16 + 8).biomeName));
            if (debugWorldGen) GT_Log.out.println(
                "ADD WorldSeed:" + aWorld.getSeed()
                    + " DimId"
                    + aWorld.provider.dimensionId
                    + " chunk x:"
                    + aX
                    + " z:"
                    + aZ
                    + " SIZE: "
                    + mList.size());
        }

        if (!this.mIsGenerating) {
            this.mIsGenerating = true;
            int mList_sS = mList.size();
            mList_sS = Math.min(mList_sS, 5); // Run a maximum of 5 chunks at a time through worldgen. Extra chunks get
                                              // done later.
            for (int i = 0; i < mList_sS; i++) {
                WorldGenContainer toRun = (WorldGenContainer) mList.get(0);
                if (debugWorldGen) GT_Log.out.println(
                    "RUN WorldSeed:" + aWorld.getSeed()
                        + " DimId"
                        + aWorld.provider.dimensionId
                        + " chunk x:"
                        + toRun.mX
                        + " z:"
                        + toRun.mZ
                        + " SIZE: "
                        + mList.size()
                        + " i: "
                        + i);
                synchronized (listLock) {
                    mList.remove(0);
                }
                toRun.run();
            }
            this.mIsGenerating = false;
        }
    }

    public static class WorldGenContainer implements Runnable {

        public final Random mRandom;
        public final int mX;
        public final int mZ;
        public final int mDimensionType;
        public final World mWorld;
        public final IChunkProvider mChunkGenerator;
        public final IChunkProvider mChunkProvider;
        public final String mBiome;
        // Used for outputting orevein weights and bins
        // static int test=0;

        // Local class to track which orevein seeds must be checked when doing chunkified worldgen
        static class NearbySeeds {

            public int mX;
            public int mZ;

            NearbySeeds(int x, int z) {
                this.mX = x;
                this.mZ = z;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof NearbySeeds that)) return false;
                if (this.mX != that.mX) return false;
                return this.mZ == that.mZ;
            }

            @Override
            public int hashCode() {
                int result = this.mX;
                result = 31 * result + this.mZ;
                return result;
            }
        }

        public static ArrayList<GT_Worldgenerator.WorldGenContainer.NearbySeeds> seedList = new ArrayList<>();

        // aX and aZ are now the by-chunk X and Z for the chunk of interest
        public WorldGenContainer(Random aRandom, int aX, int aZ, int aDimensionType, World aWorld,
            IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider, String aBiome) {
            this.mRandom = aRandom;
            this.mX = aX;
            this.mZ = aZ;
            this.mDimensionType = aDimensionType;
            this.mWorld = aWorld;
            this.mChunkGenerator = aChunkGenerator;
            this.mChunkProvider = aChunkProvider;
            this.mBiome = aBiome;
        }

        // How to evaluate oregen distribution
        // - Enable debugOreveins
        // - Fly around for a while, or teleport jumping ~320 blocks at a time, with
        // a 15-30s pause for worldgen to catch up
        // - Do this across a large area, at least 2000x2000 blocks for good numbers
        // - Open logs\gregtech.log
        // - Using notepad++, do a Search | Find - enter "Added" for the search term
        // - Select Find All In Current Document
        // - In the Search window, right-click and Select All
        // - Copy and paste to a new file
        // - Delete extraneous stuff at top, and blank line at bottom. Line count is
        // # of total oreveins
        // - For simple spot checks, use Find All in Current Document for specific
        // oremixes, ie ore.mix.diamond, to check how many appear in the list.
        // - For more complex work, import file into Excel, and sort based on oremix
        // column. Drag select the oremix names, in the bottom right will be how many
        // entries to add in a seperate tab to calculate %ages.
        //
        // When using the ore weights, discount or remove the high altitude veins since
        // their high weight are offset by their rareness. I usually just use zero for them.
        // Actual spawn rates will vary based upon the average height of the stone layers
        // in the dimension. For example veins that range above and below the average height
        // will be less, and veins that are completely above the average height will be much less.

        public void worldGenFindVein(int oreseedX, int oreseedZ) {
            // Explanation of oreveinseed implementation.
            // (long)this.mWorld.getSeed()<<16) Deep Dark does two oregen passes, one with getSeed set to +1 the
            // original world seed. This pushes that +1 off the low bits of oreseedZ, so that the hashes are far apart
            // for the two passes.
            // ((this.mWorld.provider.dimensionId & 0xffL)<<56) Puts the dimension in the top bits of the hash, to
            // make sure to get unique hashes per dimension
            // ((long)oreseedX & 0x000000000fffffffL) << 28) Puts the chunk X in the bits 29-55. Cuts off the top few
            // bits of the chunk so we have bits for dimension.
            // ( (long)oreseedZ & 0x000000000fffffffL )) Puts the chunk Z in the bits 0-27. Cuts off the top few bits
            // of the chunk so we have bits for dimension.
            long oreveinSeed = (this.mWorld.getSeed() << 16)
                ^ (((this.mWorld.provider.dimensionId & 0xffL) << 56) | (((long) oreseedX & 0x000000000fffffffL) << 28)
                    | ((long) oreseedZ & 0x000000000fffffffL)); // Use an RNG that is identical every time it is
                                                                // called for
            // this oreseed.
            XSTR oreveinRNG = new XSTR(oreveinSeed);
            int oreveinPercentageRoll = oreveinRNG.nextInt(100); // Roll the dice, see if we get an orevein here at all
            int noOrePlacedCount = 0;
            String tDimensionName = "";
            if (debugOrevein) {
                tDimensionName = this.mWorld.provider.getDimensionName();
            }

            if (debugOrevein) GT_Log.out.println(
                " Finding oreveins for oreveinSeed=" + oreveinSeed
                    + " mX="
                    + this.mX
                    + " mZ="
                    + this.mZ
                    + " oreseedX="
                    + oreseedX
                    + " oreseedZ="
                    + oreseedZ
                    + " worldSeed="
                    + this.mWorld.getSeed());

            // Search for a valid orevein for this dimension
            if (!validOreveins.containsKey(oreveinSeed)) {
                if ((oreveinPercentageRoll < oreveinPercentage) && (GT_Worldgen_GT_Ore_Layer.sWeight > 0)
                    && (GT_Worldgen_GT_Ore_Layer.sList.size() > 0)) {
                    int placementAttempts = 0;
                    boolean oreveinFound = false;
                    int i;

                    // Used for outputting orevein weights and bins
                    /*
                     * if( test==0 ) { test = 1; GT_Log.out.println( "sWeight = " + GT_Worldgen_GT_Ore_Layer.sWeight );
                     * for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) { GT_Log.out.println( (
                     * tWorldGen).mWorldGenName + " mWeight = " + ( tWorldGen).mWeight + " mSize = " + (tWorldGen).mSize
                     * ); } }
                     */
                    for (i = 0; (i < oreveinAttempts) && (!oreveinFound)
                        && (placementAttempts < oreveinMaxPlacementAttempts); i++) {
                        int tRandomWeight = oreveinRNG.nextInt(GT_Worldgen_GT_Ore_Layer.sWeight);
                        for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) {
                            tRandomWeight -= (tWorldGen).mWeight;
                            if (tRandomWeight <= 0) {
                                try {
                                    // Adjust the seed so that this layer has a series of unique random numbers.
                                    // Otherwise multiple attempts at this same oreseed will get the same offset and X/Z
                                    // values. If an orevein failed, any orevein with the
                                    // same minimum heights would fail as well. This prevents that, giving each orevein
                                    // a unique height each pass through here.
                                    int placementResult = tWorldGen.executeWorldgenChunkified(
                                        this.mWorld,
                                        new XSTR(oreveinSeed ^ (tWorldGen.mPrimaryMeta)),
                                        this.mBiome,
                                        this.mDimensionType,
                                        this.mX * 16,
                                        this.mZ * 16,
                                        oreseedX * 16,
                                        oreseedZ * 16,
                                        this.mChunkGenerator,
                                        this.mChunkProvider);
                                    switch (placementResult) {
                                        case GT_Worldgen_GT_Ore_Layer.ORE_PLACED -> {
                                            if (debugOrevein) GT_Log.out.println(
                                                " Added near oreveinSeed=" + oreveinSeed
                                                    + " "
                                                    + (tWorldGen).mWorldGenName
                                                    + " tries at oremix="
                                                    + i
                                                    + " placementAttempts="
                                                    + placementAttempts
                                                    + " dimensionName="
                                                    + tDimensionName);
                                            validOreveins.put(oreveinSeed, tWorldGen);
                                            oreveinFound = true;
                                        }
                                        case GT_Worldgen_GT_Ore_Layer.NO_ORE_IN_BOTTOM_LAYER -> placementAttempts++;

                                        // SHould do retry in this case until out of chances
                                        case GT_Worldgen_GT_Ore_Layer.NO_OVERLAP -> {
                                            if (debugOrevein) GT_Log.out.println(
                                                " Added far oreveinSeed=" + oreveinSeed
                                                    + " "
                                                    + (tWorldGen).mWorldGenName
                                                    + " tries at oremix="
                                                    + i
                                                    + " placementAttempts="
                                                    + placementAttempts
                                                    + " dimensionName="
                                                    + tDimensionName);
                                            validOreveins.put(oreveinSeed, tWorldGen);
                                            oreveinFound = true;
                                        }
                                        case GT_Worldgen_GT_Ore_Layer.NO_OVERLAP_AIR_BLOCK -> {
                                            if (debugOrevein) GT_Log.out.println(
                                                " No overlap and air block in test spot=" + oreveinSeed
                                                    + " "
                                                    + (tWorldGen).mWorldGenName
                                                    + " tries at oremix="
                                                    + i
                                                    + " placementAttempts="
                                                    + placementAttempts
                                                    + " dimensionName="
                                                    + tDimensionName);
                                            // SHould do retry in this case until out of chances
                                            placementAttempts++;
                                        }
                                    }
                                    break; // Try the next orevein
                                } catch (Throwable e) {
                                    if (debugOrevein) GT_Log.out.println(
                                        "Exception occurred on oreVein" + tWorldGen
                                            + " oreveinSeed="
                                            + oreveinSeed
                                            + " mX="
                                            + this.mX
                                            + " mZ="
                                            + this.mZ
                                            + " oreseedX="
                                            + oreseedX
                                            + " oreseedZ="
                                            + oreseedZ);
                                    e.printStackTrace(GT_Log.err);
                                }
                            }
                        }
                    }
                    // Only add an empty orevein if unable to place a vein at the oreseed chunk.
                    if ((!oreveinFound) && (this.mX == oreseedX) && (this.mZ == oreseedZ)) {
                        if (debugOrevein) GT_Log.out.println(
                            " Empty oreveinSeed=" + oreveinSeed
                                + " mX="
                                + this.mX
                                + " mZ="
                                + this.mZ
                                + " oreseedX="
                                + oreseedX
                                + " oreseedZ="
                                + oreseedZ
                                + " tries at oremix="
                                + i
                                + " placementAttempts="
                                + placementAttempts
                                + " dimensionName="
                                + tDimensionName);
                        validOreveins.put(oreveinSeed, noOresInVein);
                    }
                } else if (oreveinPercentageRoll >= oreveinPercentage) {
                    if (debugOrevein) GT_Log.out.println(
                        " Skipped oreveinSeed=" + oreveinSeed
                            + " mX="
                            + this.mX
                            + " mZ="
                            + this.mZ
                            + " oreseedX="
                            + oreseedX
                            + " oreseedZ="
                            + oreseedZ
                            + " RNG="
                            + oreveinPercentageRoll
                            + " %="
                            + oreveinPercentage
                            + " dimensionName="
                            + tDimensionName);
                    validOreveins.put(oreveinSeed, noOresInVein);
                }
            } else {
                // oreseed is located in the previously processed table
                if (debugOrevein) GT_Log.out
                    .print(" Valid oreveinSeed=" + oreveinSeed + " validOreveins.size()=" + validOreveins.size() + " ");
                GT_Worldgen_GT_Ore_Layer tWorldGen = validOreveins.get(oreveinSeed);
                oreveinRNG.setSeed(oreveinSeed ^ (tWorldGen.mPrimaryMeta)); // Reset RNG to only be based on oreseed X/Z
                                                                            // and type of vein
                int placementResult = tWorldGen.executeWorldgenChunkified(
                    this.mWorld,
                    oreveinRNG,
                    this.mBiome,
                    this.mDimensionType,
                    this.mX * 16,
                    this.mZ * 16,
                    oreseedX * 16,
                    oreseedZ * 16,
                    this.mChunkGenerator,
                    this.mChunkProvider);
                switch (placementResult) {
                    case GT_Worldgen_GT_Ore_Layer.NO_ORE_IN_BOTTOM_LAYER -> {
                        if (debugOrevein) GT_Log.out.println(" No ore in bottom layer");
                    }
                    case GT_Worldgen_GT_Ore_Layer.NO_OVERLAP -> {
                        if (debugOrevein) GT_Log.out.println(" No overlap");
                    }
                }
            }
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();
            int oreveinMaxSize;

            // Do GT_Stones and GT_small_ores oregen for this chunk
            try {
                for (GT_Worldgen tWorldGen : GregTech_API.sWorldgenList) {
                    /*
                     * if (debugWorldGen) GT_Log.out.println( "tWorldGen.mWorldGenName="+tWorldGen.mWorldGenName );
                     */
                    tWorldGen.executeWorldgen(
                        this.mWorld,
                        this.mRandom,
                        this.mBiome,
                        this.mDimensionType,
                        this.mX * 16,
                        this.mZ * 16,
                        this.mChunkGenerator,
                        this.mChunkProvider);
                }
            } catch (Throwable e) {
                e.printStackTrace(GT_Log.err);
            }
            long leftOverTime = System.nanoTime();

            // Determine bounding box on how far out to check for oreveins affecting this chunk
            // For now, manually reducing oreveinMaxSize when not in the Underdark for performance
            if (this.mWorld.provider.getDimensionName()
                .equals("Underdark")) {
                oreveinMaxSize = 32; // Leave Deep Dark/Underdark max oregen at 32, instead of 64
            } else {
                oreveinMaxSize = 32;
            }

            int wXbox = this.mX - (oreveinMaxSize / 16);
            int eXbox = this.mX + (oreveinMaxSize / 16 + 1); // Need to add 1 since it is compared using a <
            int nZbox = this.mZ - (oreveinMaxSize / 16);
            int sZbox = this.mZ + (oreveinMaxSize / 16 + 1);

            // Search for orevein seeds and add to the list;
            for (int x = wXbox; x < eXbox; x++) {
                for (int z = nZbox; z < sZbox; z++) {
                    // Determine if this X/Z is an orevein seed
                    if (((Math.abs(x) % 3) == 1) && ((Math.abs(z) % 3) == 1)) {
                        if (debugWorldGen) GT_Log.out.println("Adding seed x=" + x + " z=" + z);
                        seedList.add(new NearbySeeds(x, z));
                    }
                }
            }

            // Now process each oreseed vs this requested chunk
            for (; seedList.size() != 0; seedList.remove(0)) {
                if (debugWorldGen)
                    GT_Log.out.println("Processing seed x=" + seedList.get(0).mX + " z=" + seedList.get(0).mZ);
                worldGenFindVein(seedList.get(0).mX, seedList.get(0).mZ);
            }

            long oregenTime = System.nanoTime();

            // Asteroid Worldgen
            int tDimensionType = this.mWorld.provider.dimensionId;
            // String tDimensionName = this.mWorld.provider.getDimensionName();
            Random aRandom = new Random();
            // if (((tDimensionType == 1) && endAsteroids && ((mEndAsteroidProbability <= 1) ||
            // (aRandom.nextInt(mEndAsteroidProbability) == 0))) || ((tDimensionName.equals("Asteroids")) && gcAsteroids
            // && ((mGCAsteroidProbability <= 1) || (aRandom.nextInt(mGCAsteroidProbability) == 0)))) {
            if (((tDimensionType == 1) && endAsteroids
                && ((mEndAsteroidProbability <= 1) || (aRandom.nextInt(mEndAsteroidProbability) == 0)))) {
                short primaryMeta = 0;
                short secondaryMeta = 0;
                short betweenMeta = 0;
                short sporadicMeta = 0;
                if ((GT_Worldgen_GT_Ore_Layer.sWeight > 0) && (GT_Worldgen_GT_Ore_Layer.sList.size() > 0)) {
                    boolean temp = true;
                    int tRandomWeight;
                    for (int i = 0; (i < oreveinAttempts) && (temp); i++) {
                        tRandomWeight = aRandom.nextInt(GT_Worldgen_GT_Ore_Layer.sWeight);
                        for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) {
                            tRandomWeight -= tWorldGen.mWeight;
                            if (tRandomWeight <= 0) {
                                try {
                                    if (tWorldGen.mEndAsteroid) {
                                        primaryMeta = tWorldGen.mPrimaryMeta;
                                        secondaryMeta = tWorldGen.mSecondaryMeta;
                                        betweenMeta = tWorldGen.mBetweenMeta;
                                        sporadicMeta = tWorldGen.mSporadicMeta;
                                        temp = false;
                                        break;
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace(GT_Log.err);
                                }
                            }
                        }
                    }
                }
                // if(GT_Values.D1)GT_FML_LOGGER.info("do asteroid gen: "+this.mX+" "+this.mZ);
                int tX = mX * 16 + aRandom.nextInt(16);
                int tY = 50 + aRandom.nextInt(200 - 50);
                int tZ = mZ * 16 + aRandom.nextInt(16);
                mSize = aRandom.nextInt(endMaxSize - endMinSize);
                if ((mWorld.getBlock(tX, tY, tZ)
                    .isAir(mWorld, tX, tY, tZ))) {
                    float randomRadian = aRandom.nextFloat() * (float) Math.PI;
                    double xBase = tX + 8 + MathHelper.sin(randomRadian) * mSize / 8.0F;
                    double xFactor = tX + 8 - MathHelper.sin(randomRadian) * mSize / 8.0F;
                    double zBase = tZ + 8 + MathHelper.cos(randomRadian) * mSize / 8.0F;
                    double zFactor = tZ + 8 - MathHelper.cos(randomRadian) * mSize / 8.0F;
                    double yBase = tY + aRandom.nextInt(3) - 2;
                    double yFactor = tY + aRandom.nextInt(3) - 2;
                    for (int i = 0; i <= mSize; i++) {
                        double xCenter = xBase + (xFactor - xBase) * i / mSize;
                        double yCenter = yBase + (yFactor - yBase) * i / mSize;
                        double zCenter = zBase + (zFactor - zBase) * i / mSize;
                        double randomDistance = aRandom.nextDouble() * mSize / 16.0D;
                        double halfLength = (MathHelper.sin(i * (float) Math.PI / mSize) + 1.0F) * randomDistance
                            + 1.0D;
                        double halfHeight = (MathHelper.sin(i * (float) Math.PI / mSize) + 1.0F) * randomDistance
                            + 1.0D;
                        int tMinX = MathHelper.floor_double(xCenter - halfLength / 2.0D);
                        int tMinY = MathHelper.floor_double(yCenter - halfHeight / 2.0D);
                        int tMinZ = MathHelper.floor_double(zCenter - halfLength / 2.0D);
                        int tMaxX = MathHelper.floor_double(xCenter + halfLength / 2.0D);
                        int tMaxY = MathHelper.floor_double(yCenter + halfHeight / 2.0D);
                        int tMaxZ = MathHelper.floor_double(zCenter + halfLength / 2.0D);
                        for (int eX = tMinX; eX <= tMaxX; eX++) {
                            double xChance = (eX + 0.5D - xCenter) / (halfLength / 2.0D);
                            if (xChance * xChance < 1.0D) {
                                for (int eY = tMinY; eY <= tMaxY; eY++) {
                                    double yChance = (eY + 0.5D - yCenter) / (halfHeight / 2.0D);
                                    if (xChance * xChance + yChance * yChance < 1.0D) {
                                        for (int eZ = tMinZ; eZ <= tMaxZ; eZ++) {
                                            double zChance = (eZ + 0.5D - zCenter) / (halfLength / 2.0D);
                                            if ((xChance * xChance + yChance * yChance + zChance * zChance < 1.0D)
                                                && (mWorld.getBlock(tX, tY, tZ)
                                                    .isAir(mWorld, tX, tY, tZ))) {
                                                int ranOre = aRandom.nextInt(50);
                                                if (ranOre < 3) {
                                                    GT_TileEntity_Ores
                                                        .setOreBlock(mWorld, eX, eY, eZ, primaryMeta, false);
                                                } else if (ranOre < 6) {
                                                    GT_TileEntity_Ores
                                                        .setOreBlock(mWorld, eX, eY, eZ, secondaryMeta, false);
                                                } else if (ranOre < 8) {
                                                    GT_TileEntity_Ores
                                                        .setOreBlock(mWorld, eX, eY, eZ, betweenMeta, false);
                                                } else if (ranOre < 10) {
                                                    GT_TileEntity_Ores
                                                        .setOreBlock(mWorld, eX, eY, eZ, sporadicMeta, false);
                                                } else {
                                                    mWorld.setBlock(eX, eY, eZ, Blocks.end_stone, 0, 0);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Chunk tChunk = this.mWorld.getChunkFromBlockCoords(this.mX, this.mZ);
            if (tChunk != null) {
                tChunk.isModified = true;
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            if (debugWorldGen) {
                GT_Log.out.println(
                    " Oregen took " + (oregenTime - leftOverTime)
                        + " Leftover gen took "
                        + (leftOverTime - startTime)
                        + " Worldgen took "
                        + duration
                        + " nanoseconds");
            }
        }
    }
}
