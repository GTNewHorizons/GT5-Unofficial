package gregtech.common;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.debugOrevein;
import static gregtech.api.enums.GTValues.debugWorldGen;
import static gregtech.api.enums.GTValues.oreveinAttempts;
import static gregtech.api.enums.GTValues.oreveinMaxPlacementAttempts;
import static gregtech.api.enums.GTValues.profileWorldGen;

import java.lang.ref.WeakReference;
import java.util.Collections;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.hash.Fnv1a64;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.events.VeinGenerateEvent;
import gregtech.api.net.GTPacketSendOregenPattern;
import gregtech.api.objects.XSTR;
import gregtech.api.world.GTWorldgen;
import gregtech.common.worldgen.WorldgenQuery;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

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

    /**
     * Caches the resolved layer and placement so all chunks of an oreseed use the same vein geometry.
     * A null placement marks a cached empty vein that should fall back to NoOresInVein handling.
     */
    public record CachedOreVein(WorldgenGTOreLayer layer, long placementSeed,
        @Nullable WorldgenGTOreLayer.VeinPlacement placement) {}

    public static Long2ObjectOpenHashMap<CachedOreVein> validOreveins = new Long2ObjectOpenHashMap<>(1024);
    public boolean mIsGenerating = false;

    /**
     * Assumed whenever the world's pattern is unknown. Only AXISSYMMETRICAL worlds carry positive evidence of their
     * pattern, so every other case has to guess, and since 5.09.43.111 almost every world is EQUAL_SPACING.
     */
    public static final OregenPattern DEFAULT_PATTERN = OregenPattern.EQUAL_SPACING;

    private static OregenPattern oregenPattern = DEFAULT_PATTERN;

    /** Returns the oregen pattern used by the current world. */
    public static OregenPattern getOregenPattern() {
        return oregenPattern;
    }

    /** @deprecated Use {@link #getOregenPattern()}. */
    @Deprecated
    public static OregenPattern getClientOregenPattern() {
        return getOregenPattern();
    }

    /** @deprecated Use {@link #getOregenPattern()}. */
    @Deprecated
    public static OregenPattern getServerOregenPattern() {
        return getOregenPattern();
    }

    /** Called when the server syncs its pattern to the client; no-op when a local server is authoritative. */
    public static void setClientOregenPattern(OregenPattern pattern) {
        if (FMLCommonHandler.instance()
            .getMinecraftServerInstance() == null) {
            oregenPattern = pattern;
        }
    }

    public GTWorldgenerator() {
        // The weight here is irrelevant since the code in GameRegistryMixin forces GTWorldgenerator to the end of the
        // list.
        GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
        if (debugWorldGen) {
            GT_FML_LOGGER.debug("GTWorldgenerator created");
        }
    }

    @Override
    public void generate(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator,
        IChunkProvider aChunkProvider) {

        if (!aWorld.isRemote && aWorld.provider.dimensionId == 0) {
            // Spawn search can populate chunks before WorldEvent.Load initializes the saved oregen pattern.
            OregenPatternSavedData.ensureLoaded(aWorld);
        }

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
        if (debugWorldGen) GT_FML_LOGGER.debug(
            "ADD WorldSeed:{} DimName{} chunk x:{} z:{} SIZE: {}",
            aWorld.getSeed(),
            aWorld.provider.getDimensionName(),
            aX,
            aZ,
            PENDING_TASKS.size());

        // Hack to prevent cascading worldgen lag
        if (!this.mIsGenerating) {
            this.mIsGenerating = true;

            // Run a maximum of 5 chunks at a time through worldgen. Extra chunks get done later.
            for (int i = 0; i < Math.min(PENDING_TASKS.size(), 5); i++) {
                WorldGenContainer task = PENDING_TASKS.removeFirst();

                if (debugWorldGen) GT_FML_LOGGER.debug(
                    "RUN WorldSeed:{} DimId{} chunk x:{} z:{} SIZE: {} i: {}",
                    aWorld.getSeed(),
                    aWorld.provider.dimensionId,
                    task.mX,
                    task.mZ,
                    PENDING_TASKS.size(),
                    i);

                task.run();
            }
            this.mIsGenerating = false;
        }
    }

    public static boolean isOreChunk(int chunkX, int chunkZ) {
        if (getOregenPattern() == OregenPattern.EQUAL_SPACING) {
            return Math.floorMod(chunkX, 3) == 1 && Math.floorMod(chunkZ, 3) == 1;
        }
        // add next if statement here or convert to switch when expanding OregenPattern enum

        // AXISSYMMETRICAL
        return Math.abs(chunkX) % 3 == 1 && Math.abs(chunkZ) % 3 == 1;
    }

    public static class OregenPatternSavedData extends WorldSavedData {

        private static final String NAME = "GregTech_OregenPattern";
        private static final String KEY = "oregenPattern";
        private static WeakReference<World> loadedWorld = new WeakReference<>(null);

        /** Kept per instance: MapStorage returns cached instances without running readFromNBT again. */
        private OregenPattern pattern = DEFAULT_PATTERN;

        public OregenPatternSavedData(String p_i2141_1_) {
            super(p_i2141_1_);
        }

        public static void ensureLoaded(World world) {
            if (loadedWorld.get() != world) {
                loadData(world);
            }
        }

        public static void loadData(World world) {
            OregenPatternSavedData instance = (OregenPatternSavedData) world.mapStorage
                .loadData(OregenPatternSavedData.class, OregenPatternSavedData.NAME);

            if (instance == null) {
                instance = new OregenPatternSavedData(NAME);
                world.mapStorage.setData(OregenPatternSavedData.NAME, instance);

                if (world.getWorldInfo()
                    .getWorldTotalTime() == 0L) {
                    // Freshly created world, so the pattern is known and worth persisting
                    instance.pattern = OregenPattern.EQUAL_SPACING;
                    instance.markDirty();
                } else {
                    // Worlds have been stamped with their pattern since 5.09.43.111, so a missing file means the save
                    // lost it rather than predating it. Assume the default but never persist an unverified guess.
                    GT_FML_LOGGER.warn(
                        "{} is missing for an existing world, assuming {}. If this world predates GT 5.09.43.111 its veins use {} instead.",
                        NAME,
                        instance.pattern,
                        OregenPattern.AXISSYMMETRICAL);
                }
            }

            oregenPattern = instance.pattern;
            loadedWorld = new WeakReference<>(world);
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

        @SubscribeEvent
        public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
            // The next server may use another pattern, so do not keep this one until it syncs its own
            setClientOregenPattern(DEFAULT_PATTERN);
        }

        @Override
        public void readFromNBT(NBTTagCompound p_76184_1_) {
            if (p_76184_1_.hasKey(KEY, Constants.NBT.TAG_STRING)) {
                String name = p_76184_1_.getString(KEY);
                try {
                    pattern = OregenPattern.valueOf(name);
                } catch (IllegalArgumentException e) {
                    GT_FML_LOGGER.error("Unknown oregen pattern {}, assuming {}", name, DEFAULT_PATTERN);
                }
            } else if (p_76184_1_.hasKey(KEY, Constants.NBT.TAG_BYTE)) {
                // Written by GT older than this change, mark dirty to rewrite it by name
                int ordinal = MathHelper.clamp_int(p_76184_1_.getByte(KEY), 0, OregenPattern.values().length - 1);
                pattern = OregenPattern.values()[ordinal];
                markDirty();
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound p_76187_1_) {
            p_76187_1_.setString(KEY, pattern.name());
        }

    }

    public enum OregenPattern {
        // Persisted by name, renaming a constant needs a migration in readFromNBT
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

            if (debugOrevein) GT_FML_LOGGER.debug(
                " Finding oreveins for oreveinSeed={} mX={} mZ={} oreseedX={} oreseedZ={} worldSeed={}",
                oreveinSeed,
                this.mX,
                this.mZ,
                oreseedX,
                oreseedZ,
                this.mWorld.getSeed());

            // Search for a valid orevein for this dimension

            if (validOreveins.containsKey(oreveinSeed)) {
                // Oreseed is located in the previously processed table
                if (debugOrevein) GT_FML_LOGGER
                    .debug(" Valid oreveinSeed={} validOreveins.size()={} ", oreveinSeed, validOreveins.size());
                generateCachedVein(oreveinRNG, validOreveins.get(oreveinSeed), oreseedX, oreseedZ);

                return;
            }

            ModDimensionDef dimensionDef = DimensionDef.getDefForWorld(mWorld);

            if (oreveinPercentageRoll < dimensionDef.getOreVeinChance()) {
                int placementAttempts = 0;
                boolean oreveinFound = false;
                int i = 0;
                CachedOreVein cachedOreVein = null;

                // Used for outputting orevein weights and bins
                /*
                 * if( test==0 ) { test = 1; GT_FML_LOGGER.debug( "sWeight = " + GT_Worldgen_GT_Ore_Layer.sWeight );
                 * for (GT_Worldgen_GT_Ore_Layer tWorldGen : GT_Worldgen_GT_Ore_Layer.sList) { GT_FML_LOGGER.debug( (
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

                    // Resolve the exact placement seed using the first processed chunk, then cache it so every chunk
                    // in this vein regenerates the same geometry.
                    long placementSeed = Fnv1a64.hashStep(seed, oreLayer.mPrimary.getId());

                    try {
                        veinRNG.setSeed(placementSeed);
                        WorldgenGTOreLayer.VeinPlacement placement = oreLayer.resolveVeinPlacement(
                            this.mWorld,
                            veinRNG,
                            this.mX * 16,
                            this.mZ * 16,
                            oreseedX * 16,
                            oreseedZ * 16);

                        placementResult = oreLayer.testWorldgenChunkified(
                            this.mWorld,
                            veinRNG,
                            this.mBiome,
                            this.mX * 16,
                            this.mZ * 16,
                            oreseedX * 16,
                            oreseedZ * 16,
                            placement);

                        if (placementResult == WorldgenGTOreLayer.ORE_PLACED
                            || placementResult == WorldgenGTOreLayer.NO_OVERLAP) {
                            cachedOreVein = new CachedOreVein(oreLayer, placementSeed, placement);
                        }
                    } catch (Exception e) {
                        if (debugOrevein) GT_FML_LOGGER.debug(
                            "Exception occurred on oreVein{} oreveinSeed={} mX={} mZ={} oreseedX={} oreseedZ={}",
                            oreLayer,
                            oreveinSeed,
                            this.mX,
                            this.mZ,
                            oreseedX,
                            oreseedZ);
                        GT_FML_LOGGER.error(e);
                    }

                    switch (placementResult) {
                        case WorldgenGTOreLayer.ORE_PLACED -> {
                            if (debugOrevein) GT_FML_LOGGER.debug(
                                " Added near oreveinSeed={} {} tries at oremix={} placementAttempts={} dimensionName={}",
                                oreveinSeed,
                                oreLayer.mWorldGenName,
                                i,
                                placementAttempts,
                                dimensionName);
                            validOreveins.put(oreveinSeed, cachedOreVein);
                            oreveinFound = true;
                        }

                        // Should retry in this case until out of chances
                        case WorldgenGTOreLayer.NO_OVERLAP -> {
                            if (debugOrevein) GT_FML_LOGGER.debug(
                                " Added far oreveinSeed={} {} tries at oremix={} placementAttempts={} dimensionName={}",
                                oreveinSeed,
                                oreLayer.mWorldGenName,
                                i,
                                placementAttempts,
                                dimensionName);
                            validOreveins.put(oreveinSeed, cachedOreVein);
                            oreveinFound = true;
                        }
                        case WorldgenGTOreLayer.NO_OVERLAP_AIR_BLOCK -> {
                            if (debugOrevein) GT_FML_LOGGER.debug(
                                " No overlap and air block in test spot={} {} tries at oremix={} placementAttempts={} dimensionName={}",
                                oreveinSeed,
                                oreLayer.mWorldGenName,
                                i,
                                placementAttempts,
                                dimensionName);
                            // Should retry in this case until out of chances
                            placementAttempts++;
                        }
                    }
                }

                if (oreveinFound) {
                    generateCachedVein(oreveinRNG, cachedOreVein, oreseedX, oreseedZ);
                    return;
                }

                // Only add an empty orevein once placement has failed from the first processed chunk.
                if (debugOrevein) GT_FML_LOGGER.debug(
                    " Empty oreveinSeed={} mX={} mZ={} oreseedX={} oreseedZ={} tries at oremix={} placementAttempts={} dimensionName={}",
                    oreveinSeed,
                    this.mX,
                    this.mZ,
                    oreseedX,
                    oreseedZ,
                    i,
                    placementAttempts,
                    dimensionName);
                validOreveins.put(oreveinSeed, new CachedOreVein(noOresInVein, oreveinSeed, null));
            } else if (oreveinPercentageRoll >= dimensionDef.getOreVeinChance()) {
                if (debugOrevein) GT_FML_LOGGER.debug(
                    " Skipped oreveinSeed={} mX={} mZ={} oreseedX={} oreseedZ={} RNG={} %={} dimensionName={}",
                    oreveinSeed,
                    this.mX,
                    this.mZ,
                    oreseedX,
                    oreseedZ,
                    oreveinPercentageRoll,
                    dimensionDef.getOreVeinChance(),
                    dimensionName);
                validOreveins.put(oreveinSeed, new CachedOreVein(noOresInVein, oreveinSeed, null));
            }
        }

        private void generateCachedVein(XSTR oreveinRNG, CachedOreVein cachedOreVein, int oreseedX, int oreseedZ) {
            WorldgenGTOreLayer tWorldGen = cachedOreVein.layer();
            oreveinRNG.setSeed(cachedOreVein.placementSeed());

            int placementResult = tWorldGen.executeWorldgenChunkified(
                this.mWorld,
                oreveinRNG,
                this.mBiome,
                this.mX * 16,
                this.mZ * 16,
                oreseedX * 16,
                oreseedZ * 16,
                cachedOreVein.placement());

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
                GT_FML_LOGGER.debug(" No overlap");
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
                     * if (debugWorldGen) GT_FML_LOGGER.debug( "tWorldGen.mWorldGenName="+tWorldGen.mWorldGenName );
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
                GT_FML_LOGGER.error(e);
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
                        if (debugWorldGen) GT_FML_LOGGER.debug("Processing seed x={} z={}", x, z);
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
                GT_FML_LOGGER.info(
                    " Oregen took {}us Stonegen took {}us Worldgen took {}us",
                    (oregenTime - stonegenTime) / 1e3,
                    (stonegenTime - startTime) / 1e3,
                    (endTime - startTime) / 1e3);
            }
        }
    }
}
