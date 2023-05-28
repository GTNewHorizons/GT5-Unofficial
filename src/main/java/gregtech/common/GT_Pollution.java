package gregtech.common;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.common.GT_Proxy.dimensionWisePollution;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IMachineCallback;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_Pollution;
import gregtech.api.util.GT_ChunkAssociatedData;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_PollutionRenderer;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_Cleanroom;

public class GT_Pollution {

    private static final Storage STORAGE = new Storage();
    /**
     * Pollution dispersion until effects start: Calculation: ((Limit * 0.01) + 2000) * (4 <- spreading rate)
     * <p>
     * SMOG(500k) 466.7 pollution/sec Poison(750k) 633,3 pollution/sec Dying Plants(1mio) 800 pollution/sec Sour
     * Rain(1.5mio) 1133.3 pollution/sec
     * <p>
     * Pollution producers (pollution/sec) Bronze Boiler(20) Lava Boiler(20) High Pressure Boiler(20) Bronze Blast
     * Furnace(50) Diesel Generator(40/80/160) Gas Turbine(20/40/80) Charcoal Pile(100)
     * <p>
     * Large Diesel Engine(320) Electric Blast Furnace(100) Implosion Compressor(2000) Large Boiler(240) Large Gas
     * Turbine(160) Multi Smelter(100) Pyrolyse Oven(400)
     * <p>
     * Machine Explosion(100,000)
     * <p>
     * Other Random Shit: lots and lots
     * <p>
     * Muffler Hatch Pollution reduction: ** inaccurate ** LV (0%), MV (30%), HV (52%), EV (66%), IV (76%), LuV (84%),
     * ZPM (89%), UV (92%), MAX (95%)
     */
    private List<ChunkCoordIntPair> pollutionList = new ArrayList<>(); // chunks left to process in this cycle

    private final Set<ChunkCoordIntPair> pollutedChunks = new HashSet<>(); // a global list of all chunks with positive
                                                                           // pollution
    private int operationsPerTick = 0; // how much chunks should be processed in each cycle
    private static final short cycleLen = 1200;
    private final World world;
    private boolean blank = true;
    public static int mPlayerPollution;

    private static final int POLLUTIONPACKET_MINVALUE = 1000;

    private static GT_PollutionEventHandler EVENT_HANDLER;

    public GT_Pollution(World world) {
        this.world = world;

        if (EVENT_HANDLER == null) {
            EVENT_HANDLER = new GT_PollutionEventHandler();
            MinecraftForge.EVENT_BUS.register(EVENT_HANDLER);
        }
    }

    public static void onWorldTick(TickEvent.WorldTickEvent aEvent) { // called from proxy
        // return if pollution disabled
        if (!GT_Mod.gregtechproxy.mPollution) return;
        if (aEvent.phase == TickEvent.Phase.START) return;
        final GT_Pollution pollutionInstance = dimensionWisePollution.get(aEvent.world.provider.dimensionId);
        if (pollutionInstance == null) return;
        pollutionInstance.tickPollutionInWorld((int) (aEvent.world.getTotalWorldTime() % cycleLen));
    }

    private void tickPollutionInWorld(int aTickID) { // called from method above
        // gen data set
        if (aTickID == 0 || blank) {
            // make a snapshot of what to work on
            pollutionList = new ArrayList<>(pollutedChunks);
            // set operations per tick
            if (pollutionList.size() > 0) operationsPerTick = Math.max(1, pollutionList.size() / cycleLen);
            else operationsPerTick = 0; // SANity
            blank = false;
        }

        for (int chunksProcessed = 0; chunksProcessed < operationsPerTick; chunksProcessed++) {
            if (pollutionList.size() == 0) break; // no more stuff to do
            ChunkCoordIntPair actualPos = pollutionList.remove(pollutionList.size() - 1); // faster
            // get pollution
            ChunkData currentData = STORAGE.get(world, actualPos);
            int tPollution = currentData.getAmount();
            // remove some
            tPollution = (int) (0.9945f * tPollution);

            if (tPollution > 400000) { // Spread Pollution

                ChunkCoordIntPair[] tNeighbors = new ChunkCoordIntPair[4]; // array is faster
                tNeighbors[0] = (new ChunkCoordIntPair(actualPos.chunkXPos + 1, actualPos.chunkZPos));
                tNeighbors[1] = (new ChunkCoordIntPair(actualPos.chunkXPos - 1, actualPos.chunkZPos));
                tNeighbors[2] = (new ChunkCoordIntPair(actualPos.chunkXPos, actualPos.chunkZPos + 1));
                tNeighbors[3] = (new ChunkCoordIntPair(actualPos.chunkXPos, actualPos.chunkZPos - 1));
                for (ChunkCoordIntPair neighborPosition : tNeighbors) {
                    ChunkData neighbor = STORAGE.get(world, neighborPosition);
                    int neighborPollution = neighbor.getAmount();
                    if (neighborPollution * 6 < tPollution * 5) { // MATHEMATICS...
                        int tDiff = tPollution - neighborPollution;
                        tDiff = tDiff / 20;
                        neighborPollution = GT_Utility.safeInt((long) neighborPollution + tDiff); // tNPol += tDiff;
                        tPollution -= tDiff;
                        setChunkPollution(neighborPosition, neighborPollution);
                    }
                }

                // Create Pollution effects
                // Smog filter TODO
                if (tPollution > GT_Mod.gregtechproxy.mPollutionSmogLimit) {
                    AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(
                        actualPos.chunkXPos << 4,
                        0,
                        actualPos.chunkZPos << 4,
                        (actualPos.chunkXPos << 4) + 16,
                        256,
                        (actualPos.chunkZPos << 4) + 16);
                    List<EntityLivingBase> tEntitys = world.getEntitiesWithinAABB(EntityLivingBase.class, chunk);
                    for (EntityLivingBase tEnt : tEntitys) {
                        if (tEnt instanceof EntityPlayerMP && ((EntityPlayerMP) tEnt).capabilities.isCreativeMode)
                            continue;
                        if (!(GT_Utility.isWearingFullGasHazmat(tEnt))) {
                            switch (XSTR_INSTANCE.nextInt(3)) {
                                default:
                                    tEnt.addPotionEffect(
                                        new PotionEffect(
                                            Potion.digSlowdown.id,
                                            Math.min(tPollution / 1000, 1000),
                                            tPollution / 400000));
                                case 1:
                                    tEnt.addPotionEffect(
                                        new PotionEffect(
                                            Potion.weakness.id,
                                            Math.min(tPollution / 1000, 1000),
                                            tPollution / 400000));
                                case 2:
                                    tEnt.addPotionEffect(
                                        new PotionEffect(
                                            Potion.moveSlowdown.id,
                                            Math.min(tPollution / 1000, 1000),
                                            tPollution / 400000));
                            }
                        }
                    }

                    // Poison effects
                    if (tPollution > GT_Mod.gregtechproxy.mPollutionPoisonLimit) {
                        // AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(tPos.chunkPosX*16, 0, tPos.chunkPosZ*16,
                        // tPos.chunkPosX*16+16, 256, tPos.chunkPosZ*16+16);
                        // List<EntityLiving> tEntitys = aWorld.getEntitiesWithinAABB(EntityLiving.class, chunk);
                        for (EntityLivingBase tEnt : tEntitys) {
                            if (tEnt instanceof EntityPlayerMP && ((EntityPlayerMP) tEnt).capabilities.isCreativeMode)
                                continue;
                            if (!GT_Utility.isWearingFullGasHazmat(tEnt)) {
                                switch (XSTR_INSTANCE.nextInt(4)) {
                                    default:
                                        tEnt.addPotionEffect(new PotionEffect(Potion.hunger.id, tPollution / 500000));
                                    case 1:
                                        tEnt.addPotionEffect(
                                            new PotionEffect(
                                                Potion.confusion.id,
                                                Math.min(tPollution / 2000, 1000),
                                                1));
                                    case 2:
                                        tEnt.addPotionEffect(
                                            new PotionEffect(
                                                Potion.poison.id,
                                                Math.min(tPollution / 4000, 1000),
                                                tPollution / 500000));
                                    case 3:
                                        tEnt.addPotionEffect(
                                            new PotionEffect(
                                                Potion.blindness.id,
                                                Math.min(tPollution / 2000, 1000),
                                                1));
                                }
                            }
                        }

                        // killing plants
                        if (tPollution > GT_Mod.gregtechproxy.mPollutionVegetationLimit) {
                            int f = 20;
                            for (; f < (tPollution / 25000); f++) {
                                int x = (actualPos.chunkXPos << 4) + XSTR_INSTANCE.nextInt(16);
                                int y = 60 + (-f + XSTR_INSTANCE.nextInt(f * 2 + 1));
                                int z = (actualPos.chunkZPos << 4) + XSTR_INSTANCE.nextInt(16);
                                damageBlock(world, x, y, z, tPollution > GT_Mod.gregtechproxy.mPollutionSourRainLimit);
                            }
                        }
                    }
                }
            }
            // Write new pollution to Hashmap !!!
            setChunkPollution(actualPos, tPollution);

            // Send new value to players nearby
            if (tPollution > POLLUTIONPACKET_MINVALUE) {
                NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
                    world.provider.dimensionId,
                    (actualPos.chunkXPos << 4),
                    64,
                    (actualPos.chunkZPos << 4),
                    256);
                GT_Values.NW.sendToAllAround(new GT_Packet_Pollution(actualPos, tPollution), point);
            }
        }
    }

    private void setChunkPollution(ChunkCoordIntPair coord, int pollution) {
        mutatePollution(world, coord.chunkXPos, coord.chunkZPos, c -> c.setAmount(pollution), pollutedChunks);
    }

    private static void damageBlock(World world, int x, int y, int z, boolean sourRain) {
        if (world.isRemote) return;
        Block tBlock = world.getBlock(x, y, z);
        int tMeta = world.getBlockMetadata(x, y, z);
        if (tBlock == Blocks.air || tBlock == Blocks.stone || tBlock == Blocks.sand || tBlock == Blocks.deadbush)
            return;

        if (tBlock == Blocks.leaves || tBlock == Blocks.leaves2 || tBlock.getMaterial() == Material.leaves)
            world.setBlockToAir(x, y, z);
        if (tBlock == Blocks.reeds) {
            tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
            world.setBlockToAir(x, y, z);
        }
        if (tBlock == Blocks.tallgrass) world.setBlock(x, y, z, Blocks.deadbush);
        if (tBlock == Blocks.vine) {
            tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
            world.setBlockToAir(x, y, z);
        }
        if (tBlock == Blocks.waterlily || tBlock == Blocks.wheat
            || tBlock == Blocks.cactus
            || tBlock.getMaterial() == Material.cactus
            || tBlock == Blocks.melon_block
            || tBlock == Blocks.melon_stem) {
            tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
            world.setBlockToAir(x, y, z);
        }
        if (tBlock == Blocks.red_flower || tBlock == Blocks.yellow_flower
            || tBlock == Blocks.carrots
            || tBlock == Blocks.potatoes
            || tBlock == Blocks.pumpkin
            || tBlock == Blocks.pumpkin_stem) {
            tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
            world.setBlockToAir(x, y, z);
        }
        if (tBlock == Blocks.sapling || tBlock.getMaterial() == Material.plants)
            world.setBlock(x, y, z, Blocks.deadbush);
        if (tBlock == Blocks.cocoa) {
            tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
            world.setBlockToAir(x, y, z);
        }
        if (tBlock == Blocks.mossy_cobblestone) world.setBlock(x, y, z, Blocks.cobblestone);
        if (tBlock == Blocks.grass || tBlock.getMaterial() == Material.grass) world.setBlock(x, y, z, Blocks.dirt);
        if (tBlock == Blocks.farmland || tBlock == Blocks.dirt) {
            world.setBlock(x, y, z, Blocks.sand);
        }

        if (sourRain && world.isRaining()
            && (tBlock == Blocks.stone || tBlock == Blocks.gravel || tBlock == Blocks.cobblestone)
            && world.getBlock(x, y + 1, z) == Blocks.air
            && world.canBlockSeeTheSky(x, y, z)) {
            if (tBlock == Blocks.stone) {
                world.setBlock(x, y, z, Blocks.cobblestone);
            } else if (tBlock == Blocks.cobblestone) {
                world.setBlock(x, y, z, Blocks.gravel);
            } else if (tBlock == Blocks.gravel) {
                world.setBlock(x, y, z, Blocks.sand);
            }
        }
    }

    private static GT_Pollution getPollutionManager(World world) {
        return dimensionWisePollution.computeIfAbsent(world.provider.dimensionId, i -> new GT_Pollution(world));
    }

    /** @see #addPollution(World, int, int, int) */
    @SuppressWarnings("rawtypes")
    public static void addPollution(IGregTechTileEntity te, int aPollution) {
        if (!GT_Mod.gregtechproxy.mPollution || aPollution == 0 || te.isClientSide()) return;
        IMetaTileEntity iMetaTileEntity = te.getMetaTileEntity();

        if (iMetaTileEntity instanceof IMachineCallback) {
            if (((IMachineCallback) iMetaTileEntity).getCallbackBase() instanceof GT_MetaTileEntity_Cleanroom) {
                if (aPollution > 0) {
                    ((GT_MetaTileEntity_Cleanroom) ((IMachineCallback) iMetaTileEntity).getCallbackBase())
                        .doMaintenanceIssue();
                }
            }
        }

        mutatePollution(te.getWorld(), te.getXCoord() >> 4, te.getZCoord() >> 4, d -> d.changeAmount(aPollution), null);
    }

    /** @see #addPollution(World, int, int, int) */
    public static void addPollution(Chunk ch, int aPollution) {
        if (!GT_Mod.gregtechproxy.mPollution || aPollution == 0 || ch.worldObj.isRemote) return;
        mutatePollution(ch.worldObj, ch.xPosition, ch.zPosition, d -> d.changeAmount(aPollution), null);
    }

    /**
     * Add some pollution to given chunk. Can pass in negative to remove pollution. Will clamp the final pollution
     * number to 0 if it would be changed into negative.
     *
     * @param w          world to modify. do nothing if it's a client world
     * @param chunkX     chunk coordinate X, i.e. blockX >> 4
     * @param chunkZ     chunk coordinate Z, i.e. blockZ >> 4
     * @param aPollution desired delta. Positive means the pollution in chunk would go higher.
     */
    public static void addPollution(World w, int chunkX, int chunkZ, int aPollution) {
        if (!GT_Mod.gregtechproxy.mPollution || aPollution == 0 || w.isRemote) return;
        mutatePollution(w, chunkX, chunkZ, d -> d.changeAmount(aPollution), null);
    }

    private static void mutatePollution(World world, int x, int z, Consumer<ChunkData> mutator,
        @Nullable Set<ChunkCoordIntPair> chunks) {
        ChunkData data = STORAGE.get(world, x, z);
        boolean hadPollution = data.getAmount() > 0;
        mutator.accept(data);
        boolean hasPollution = data.getAmount() > 0;
        if (hasPollution != hadPollution) {
            if (chunks == null) chunks = getPollutionManager(world).pollutedChunks;
            if (hasPollution) chunks.add(new ChunkCoordIntPair(x, z));
            else chunks.remove(new ChunkCoordIntPair(x, z));
        }
    }

    /** @see #getPollution(World, int, int) */
    public static int getPollution(IGregTechTileEntity te) {
        return getPollution(te.getWorld(), te.getXCoord() >> 4, te.getZCoord() >> 4);
    }

    /** @see #getPollution(World, int, int) */
    public static int getPollution(Chunk ch) {
        return getPollution(ch.worldObj, ch.xPosition, ch.zPosition);
    }

    /**
     * Get the pollution in specified chunk
     * 
     * @param w      world to look in. can be a client world, but that limits the knowledge to what server side send us
     * @param chunkX chunk coordinate X, i.e. blockX >> 4
     * @param chunkZ chunk coordinate Z, i.e. blockZ >> 4
     * @return pollution amount. may be 0 if pollution is disabled, or if it's a client world and server did not send us
     *         info about this chunk
     */
    public static int getPollution(World w, int chunkX, int chunkZ) {
        if (!GT_Mod.gregtechproxy.mPollution) return 0;
        if (w.isRemote)
            // it really should be querying the client side stuff instead
            return GT_PollutionRenderer.getKnownPollution(chunkX << 4, chunkZ << 4);
        return STORAGE.get(w, chunkX, chunkZ)
            .getAmount();
    }

    @Deprecated
    public static int getPollution(ChunkCoordIntPair aCh, int aDim) {
        return getPollution(DimensionManager.getWorld(aDim), aCh.chunkXPos, aCh.chunkZPos);
    }

    public static boolean hasPollution(Chunk ch) {
        if (!GT_Mod.gregtechproxy.mPollution) return false;
        return STORAGE.isCreated(ch.worldObj, ch.getChunkCoordIntPair()) && STORAGE.get(ch)
            .getAmount() > 0;
    }

    // Add compatibility with old code
    @Deprecated /* Don't use it... too weird way of passing position */
    public static void addPollution(World aWorld, ChunkPosition aPos, int aPollution) {
        // The abuse of ChunkPosition to store block position and dim...
        // is just bad especially when that is both used to store ChunkPos and BlockPos depending on context
        addPollution(aWorld.getChunkFromBlockCoords(aPos.chunkPosX, aPos.chunkPosZ), aPollution);
    }

    static void migrate(ChunkDataEvent.Load e) {
        addPollution(
            e.getChunk(),
            e.getData()
                .getInteger("GTPOLLUTION"));
    }

    public static class GT_PollutionEventHandler {

        @SubscribeEvent
        public void chunkWatch(ChunkWatchEvent.Watch event) {
            if (!GT_Mod.gregtechproxy.mPollution) return;
            World world = event.player.worldObj;
            if (STORAGE.isCreated(world, event.chunk)) {
                int pollution = STORAGE.get(world, event.chunk)
                    .getAmount();
                if (pollution > POLLUTIONPACKET_MINVALUE)
                    GT_Values.NW.sendToPlayer(new GT_Packet_Pollution(event.chunk, pollution), event.player);
            }
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load e) {
            // super class loads everything lazily. We force it to load them all.
            if (!e.world.isRemote) STORAGE.loadAll(e.world);
        }
    }

    @ParametersAreNonnullByDefault
    private static final class Storage extends GT_ChunkAssociatedData<ChunkData> {

        private Storage() {
            super("Pollution", ChunkData.class, 64, (byte) 0, false);
        }

        @Override
        protected void writeElement(DataOutput output, ChunkData element, World world, int chunkX, int chunkZ)
            throws IOException {
            output.writeInt(element.getAmount());
        }

        @Override
        protected ChunkData readElement(DataInput input, int version, World world, int chunkX, int chunkZ)
            throws IOException {
            if (version != 0) throw new IOException("Region file corrupted");
            ChunkData data = new ChunkData(input.readInt());
            if (data.getAmount() > 0)
                getPollutionManager(world).pollutedChunks.add(new ChunkCoordIntPair(chunkX, chunkZ));
            return data;
        }

        @Override
        protected ChunkData createElement(World world, int chunkX, int chunkZ) {
            return new ChunkData();
        }

        @Override
        public void loadAll(World w) {
            super.loadAll(w);
        }

        public boolean isCreated(World world, ChunkCoordIntPair coord) {
            return isCreated(world.provider.dimensionId, coord.chunkXPos, coord.chunkZPos);
        }
    }

    private static final class ChunkData implements GT_ChunkAssociatedData.IData {

        public int amount;

        private ChunkData() {
            this(0);
        }

        private ChunkData(int amount) {
            this.amount = Math.max(0, amount);
        }

        /**
         * Current pollution amount.
         */
        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = Math.max(amount, 0);
        }

        public void changeAmount(int delta) {
            this.amount = Math.max(GT_Utility.safeInt(amount + (long) delta, 0), 0);
        }

        @Override
        public boolean isSameAsDefault() {
            return amount == 0;
        }
    }
}
