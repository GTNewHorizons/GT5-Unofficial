package gregtech.common;

import static gregtech.api.enums.GTValues.debugOrevein;
import static gregtech.api.enums.GTValues.debugWorldGen;
import static gregtech.api.enums.GTValues.oreveinAttempts;
import static gregtech.api.enums.GTValues.oreveinMaxPlacementAttempts;
import static gregtech.api.enums.GTValues.profileWorldGen;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.hash.Fnv1a64;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.events.VeinGenerateEvent;
import gregtech.api.net.GTPacketSendOregenPattern;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTLog;
import gregtech.api.world.GTWorldgen;
import gregtech.common.worldgen.WorldgenQuery;

public class GTWorldgenerator implements IWorldGenerator {

    private static final int MAX_VEIN_SIZE = 2; // in chunks

    private static final List<WorldGenContainer> PENDING_TASKS = Collections.synchronizedList(new LinkedList<>());

    // This is probably not going to work. Trying to create a fake orevein to put into hashtable when there will be no
    // ores in a vein.
    public static WorldgenGTOreLayer noOresInVein = new WorldgenGTOreLayer(
        new OreMixBuilder().name("NoOresInVein")
            .disabledByDefault()
            .heightRange(0, 255)
            .weight(0)
            .density(255)
            .size(16)
            .primary(Materials.Aluminium)
            .secondary(Materials.Aluminium)
            .inBetween(Materials.Aluminium)
            .sporadic(Materials.Aluminium));

    public static Hashtable<Long, WorldgenGTOreLayer> validOreveins = new Hashtable<>(1024);
    public boolean mIsGenerating = false;
    public static OregenPattern oregenPattern = OregenPattern.AXISSYMMETRICAL;

    public GTWorldgenerator() {
        // The weight here is irrelevant since the code in GameRegistryMixin forces GTWorldgenerator to the end of the
        // list.
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
        if (debugWorldGen) {
            GTLog.out.println("GTWorldgenerator created");
        }
    }

    @Override
    public void generate(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator,
        IChunkProvider aChunkProvider) {

        ModDimensionDef def = DimensionDef.getEffectiveDefForChunk(aWorld, aX, aZ);

        if (def == null || !def.generatesOre()) {
            return;
        }

        PENDING_TASKS.add(
            new WorldGenContainer(
                new XSTR(Math.abs(aRandom.nextInt()) + 1),
                aX,
                aZ,
                aWorld,
                aChunkGenerator,
                aChunkProvider,
                aWorld.getBiomeGenForCoords(aX * 16 + 8, aZ * 16 + 8).biomeName));
        if (debugWorldGen) GTLog.out.println(
            "ADD WorldSeed:" + aWorld.getSeed()
                + " DimName"
                + aWorld.provider.getDimensionName()
                + " chunk x:"
                + aX
                + " z:"
                + aZ
                + " SIZE: "
                + PENDING_TASKS.size());

        // Hack to prevent cascading worldgen lag
        if (!this.mIsGenerating) {
            this.mIsGenerating = true;

            // Run a maximum of 5 chunks at a time through worldgen. Extra chunks get done later.
            for (int i = 0; i < Math.min(PENDING_TASKS.size(), 5); i++) {
                WorldGenContainer task = PENDING_TASKS.remove(0);

                if (debugWorldGen) GTLog.out.println(
                    "RUN WorldSeed:" + aWorld.getSeed()
                        + " DimId"
                        + aWorld.provider.dimensionId
                        + " chunk x:"
                        + task.mX
                        + " z:"
                        + task.mZ
                        + " SIZE: "
                        + PENDING_TASKS.size()
                        + " i: "
                        + i);

                task.run();
            }
            this.mIsGenerating = false;
        }
    }

    public static boolean isOreChunk(int chunkX, int chunkZ) {
        if (oregenPattern == OregenPattern.EQUAL_SPACING) {
            return Math.floorMod(chunkX, 3) == 1 && Math.floorMod(chunkZ, 3) == 1;
        }
        // add next if statement here or convert to switch when expanding OregenPattern enum

        // AXISSYMMETRICAL
        return Math.abs(chunkX) % 3 == 1 && Math.abs(chunkZ) % 3 == 1;
    }

    public static class OregenPatternSavedData extends WorldSavedData {

        private static final String NAME = "GregTech_OregenPattern";
        private static final String KEY = "oregenPattern";

        public OregenPatternSavedData(String p_i2141_1_) {
            super(p_i2141_1_);
        }

        public static void loadData(World world) {
            if (world.getWorldInfo()
                .getWorldTotalTime() == 0L) {
                // The world has just been created -> use newest pattern
                oregenPattern = OregenPattern.values()[OregenPattern.values().length - 1];
            } else {
                // This is an old world. Use legacy pattern for now, readFromNBT may change this if
                // GregTech_OregenPattern.dat is present
                oregenPattern = OregenPattern.AXISSYMMETRICAL;
            }

            // load OregenPatternSavedData
            WorldSavedData instance = world.mapStorage
                .loadData(OregenPatternSavedData.class, OregenPatternSavedData.NAME);
            if (instance == null) {
                instance = new OregenPatternSavedData(NAME);
                world.mapStorage.setData(OregenPatternSavedData.NAME, instance);
            }
            instance.markDirty();
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load event) {
            final World world = event.world;
            if (!world.isRemote && world.provider.dimensionId == 0) {
                loadData(world);
            }
        }

        @SubscribeEvent
        public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.player instanceof EntityPlayerMP player) {
                GTValues.NW.sendToPlayer(new GTPacketSendOregenPattern(oregenPattern), player);
            }
        }

        @Override
        public void readFromNBT(NBTTagCompound p_76184_1_) {
            if (p_76184_1_.hasKey(KEY)) {
                int ordinal = p_76184_1_.getByte(KEY);
                ordinal = MathHelper.clamp_int(ordinal, 0, OregenPattern.values().length - 1);
                oregenPattern = OregenPattern.values()[ordinal];
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound p_76187_1_) {
            // If we have so many different OregenPatterns that byte isn't good enough something is wrong
            p_76187_1_.setByte(KEY, (byte) oregenPattern.ordinal());
        }

    }

    public enum OregenPattern {
        // The last value is used when creating a new world
        AXISSYMMETRICAL,
        EQUAL_SPACING
    }

    public static class WorldGenContainer implements Runnable {

        public final Random mRandom;
        public final int mX;
        public final int mZ;
        public final World mWorld;
        public final IChunkProvider mChunkGenerator;
        public final IChunkProvider mChunkProvider;
        public final String mBiome;
        // Used for outputting orevein weights and bins
        // static int test=0;

        // aX and aZ are now the by-chunk X and Z for the chunk of interest
        public WorldGenContainer(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator,
            IChunkProvider aChunkProvider, String aBiome) {
            this.mRandom = aRandom;
            this.mX = aX;
            this.mZ = aZ;
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
        // entries to add in a separate tab to calculate %ages.
        //
        // When using the ore weights, discount or remove the high altitude veins since
        // their high weight are offset by their rareness. I usually just use zero for them.
        // Actual spawn rates will vary based upon the average height of the stone layers
        // in the dimension. For example veins that range above and below the average height
        // will be less, and veins that are completely above the average height will be much less.

        public void generateVein(int oreseedX, int oreseedZ) {
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

            String dimensionName = DimensionDef.getDimensionName(this.mWorld);

            if (debugOrevein) GTLog.out.println(
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

            if (validOreveins.containsKey(oreveinSeed)) {
                // Oreseed is located in the previously processed table
                if (debugOrevein) GTLog.out
                    .print(" Valid oreveinSeed=" + oreveinSeed + " validOreveins.size()=" + validOreveins.size() + " ");
                WorldgenGTOreLayer tWorldGen = validOreveins.get(oreveinSeed);

                // Reset RNG to only be based on oreseed X/Z and type of vein
                oreveinRNG.setSeed(oreveinSeed ^ tWorldGen.mPrimary.getId());

                int placementResult = tWorldGen.executeWorldgenChunkified(
                    this.mWorld,
                    oreveinRNG,
                    this.mBiome,
                    this.mX * 16,
                    this.mZ * 16,
                    oreseedX * 16,
                    oreseedZ * 16,
                    this.mChunkGenerator,
                    this.mChunkProvider);

                VeinGenerateEvent event = new VeinGenerateEvent(
                    mWorld,
                    mX,
                    mZ,
                    oreseedX,
                    oreseedZ,
                    tWorldGen,
                    placementResult);
                MinecraftForge.EVENT_BUS.post(event);

                if (placementResult == WorldgenGTOreLayer.NO_OVERLAP && debugOrevein) {
                    GTLog.out.println(" No overlap");
                }

                return;
            }

            ModDimensionDef dimensionDef = DimensionDef.getDefForWorld(mWorld);

            if (oreveinPercentageRoll < dimensionDef.getOreVeinChance()) {
                int placementAttempts = 0;
                boolean oreveinFound = false;
                int i = 0;

                // Used for outputting orevein weights and bins
                /*
                 * if( test==0 ) { test = 1; GTLog.out.println( "sWeight = " + GT_Worldgen_GT_Ore_Layer.sWeight );
                 * for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) { GTLog.out.println( (
                 * tWorldGen).mWorldGenName + " mWeight = " + ( tWorldGen).mWeight + " mSize = " + (tWorldGen).mSize
                 * ); } }
                 */

                XSTR veinRNG = new XSTR(0);

                for (i = 0; i < oreveinAttempts && placementAttempts < oreveinMaxPlacementAttempts
                    && !oreveinFound; i++) {
                    long seed = Fnv1a64.initialState();
                    seed = Fnv1a64.hashStep(seed, oreveinSeed);
                    seed = Fnv1a64.hashStep(seed, i);

                    veinRNG.setSeed(seed);

                    WorldgenGTOreLayer oreLayer = WorldgenQuery.veins()
                        .inDimension(dimensionName)
                        .findRandom(veinRNG);

                    // There aren't any veins in this dimension so there's no point in retrying
                    if (oreLayer == null) break;

                    int placementResult = 0;

                    try {
                        seed = Fnv1a64.hashStep(seed, oreLayer.mPrimary.getId());

                        veinRNG.setSeed(seed);

                        // Adjust the seed so that this layer has a series of unique random numbers.
                        // Otherwise multiple attempts at this same oreseed will get the same offset and X/Z values.
                        // If an orevein failed, any orevein with the same minimum heights would fail as well. This
                        // prevents that, giving each orevein a unique height each pass through here.
                        placementResult = oreLayer.executeWorldgenChunkified(
                            this.mWorld,
                            veinRNG,
                            this.mBiome,
                            this.mX * 16,
                            this.mZ * 16,
                            oreseedX * 16,
                            oreseedZ * 16,
                            this.mChunkGenerator,
                            this.mChunkProvider);

                        VeinGenerateEvent event = new VeinGenerateEvent(
                            mWorld,
                            mX,
                            mZ,
                            oreseedX,
                            oreseedZ,
                            oreLayer,
                            placementResult);
                        MinecraftForge.EVENT_BUS.post(event);
                    } catch (Exception e) {
                        if (debugOrevein) GTLog.out.println(
                            "Exception occurred on oreVein" + oreLayer
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
                        e.printStackTrace(GTLog.err);
                    }

                    switch (placementResult) {
                        case WorldgenGTOreLayer.ORE_PLACED -> {
                            if (debugOrevein) GTLog.out.println(
                                " Added near oreveinSeed=" + oreveinSeed
                                    + " "
                                    + oreLayer.mWorldGenName
                                    + " tries at oremix="
                                    + i
                                    + " placementAttempts="
                                    + placementAttempts
                                    + " dimensionName="
                                    + dimensionName);
                            validOreveins.put(oreveinSeed, oreLayer);
                            oreveinFound = true;
                        }

                        // Should retry in this case until out of chances
                        case WorldgenGTOreLayer.NO_OVERLAP -> {
                            if (debugOrevein) GTLog.out.println(
                                " Added far oreveinSeed=" + oreveinSeed
                                    + " "
                                    + oreLayer.mWorldGenName
                                    + " tries at oremix="
                                    + i
                                    + " placementAttempts="
                                    + placementAttempts
                                    + " dimensionName="
                                    + dimensionName);
                            validOreveins.put(oreveinSeed, oreLayer);
                            oreveinFound = true;
                        }
                        case WorldgenGTOreLayer.NO_OVERLAP_AIR_BLOCK -> {
                            if (debugOrevein) GTLog.out.println(
                                " No overlap and air block in test spot=" + oreveinSeed
                                    + " "
                                    + oreLayer.mWorldGenName
                                    + " tries at oremix="
                                    + i
                                    + " placementAttempts="
                                    + placementAttempts
                                    + " dimensionName="
                                    + dimensionName);
                            // Should retry in this case until out of chances
                            placementAttempts++;
                        }
                    }
                }

                // Only add an empty orevein if unable to place a vein at the oreseed chunk.
                if (!oreveinFound && this.mX == oreseedX && this.mZ == oreseedZ) {
                    if (debugOrevein) GTLog.out.println(
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
                            + dimensionName);
                    validOreveins.put(oreveinSeed, noOresInVein);
                }
            } else if (oreveinPercentageRoll >= dimensionDef.getOreVeinChance()) {
                if (debugOrevein) GTLog.out.println(
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
                        + dimensionDef.getOreVeinChance()
                        + " dimensionName="
                        + dimensionName);
                validOreveins.put(oreveinSeed, noOresInVein);
            }
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();
            Chunk tChunk = this.mWorld.getChunkFromChunkCoords(this.mX, this.mZ);

            // Do GT_Stones and GT_small_ores oregen for this chunk
            try {
                for (GTWorldgen tWorldGen : GregTechAPI.sWorldgenList) {
                    /*
                     * if (debugWorldGen) GTLog.out.println( "tWorldGen.mWorldGenName="+tWorldGen.mWorldGenName );
                     */
                    tWorldGen.executeWorldgen(
                        this.mWorld,
                        this.mRandom,
                        this.mBiome,
                        this.mX * 16,
                        this.mZ * 16,
                        this.mChunkGenerator,
                        this.mChunkProvider);
                }
            } catch (Exception e) {
                e.printStackTrace(GTLog.err);
            }

            long stonegenTime = System.nanoTime();

            int chunkMinX = this.mX - MAX_VEIN_SIZE;
            int chunkMaxX = this.mX + MAX_VEIN_SIZE + 1; // Need to add 1 since it is compared using a <
            int chunkMinZ = this.mZ - MAX_VEIN_SIZE;
            int chunkMaxZ = this.mZ + MAX_VEIN_SIZE + 1;

            // Search for orevein seeds and add to the list;
            for (int x = chunkMinX; x < chunkMaxX; x++) {
                for (int z = chunkMinZ; z < chunkMaxZ; z++) {
                    // Determine if this X/Z is an orevein seed
                    if (isOreChunk(x, z)) {
                        if (debugWorldGen) GTLog.out.println("Processing seed x=" + x + " z=" + z);
                        generateVein(x, z);
                    }
                }
            }

            long oregenTime = System.nanoTime();

            if (tChunk != null) {
                tChunk.isModified = true;
            }

            long endTime = System.nanoTime();

            if (debugWorldGen || profileWorldGen) {
                GTMod.GT_FML_LOGGER.info(
                    " Oregen took " + (oregenTime - stonegenTime) / 1e3
                        + "us Stonegen took "
                        + (stonegenTime - startTime) / 1e3
                        + "us Worldgen took "
                        + (endTime - startTime) / 1e3
                        + "us");
            }
        }
    }
}
