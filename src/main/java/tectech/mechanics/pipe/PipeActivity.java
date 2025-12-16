package tectech.mechanics.pipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import it.unimi.dsi.fastutil.ints.IntBooleanPair;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import tectech.loader.NetworkDispatcher;

public class PipeActivity {

    public static final PipeActivity INSTANCE = new PipeActivity();

    private static final Map<IntBooleanPair, LongArrayList> updates = new HashMap<>();

    private static final Function<IntBooleanPair, LongArrayList> CTOR = x -> new LongArrayList();

    private PipeActivity() {

    }

    public synchronized static <TPipe extends IMetaTileEntity & IActivePipe> void enqueueUpdate(TPipe pipe) {
        IGregTechTileEntity base = pipe.getBaseMetaTileEntity();

        if (base != null && !base.isDead()) {
            enqueueUpdate(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), pipe.getActive());
        }
    }

    public synchronized static void enqueueUpdate(World world, int x, int y, int z, boolean isActive) {
        LongArrayList coords = updates.computeIfAbsent(IntBooleanPair.of(world.provider.dimensionId, isActive), CTOR);

        coords.add(CoordinatePacker.pack(x, y, z));
    }

    private static final int MAX_UPDATES_PER_PACKET = 30_000 / 8;

    public synchronized static void sendUpdates() {
        var iter = updates.entrySet()
            .iterator();

        while (iter.hasNext()) {
            var e = iter.next();

            int worldId = e.getKey()
                .leftInt();
            boolean isActive = e.getKey()
                .rightBoolean();
            LongArrayList coords = e.getValue();

            for (int i = 0; i < coords.size(); i += MAX_UPDATES_PER_PACKET) {
                LongList forPacket = coords.subList(i, Math.min(coords.size(), i + MAX_UPDATES_PER_PACKET));

                BatchedPipeActivityMessage message = new BatchedPipeActivityMessage();
                message.worldId = worldId;
                message.isActive = isActive;
                message.pipes = forPacket.toLongArray();

                NetworkDispatcher.INSTANCE.sendToDimension(message, worldId);
            }

            iter.remove();
        }
    }

    public static void init() {
        FMLCommonHandler.instance()
            .bus()
            .register(INSTANCE);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent tickEvent) {
        if (tickEvent.side == Side.SERVER && tickEvent.type == Type.SERVER && tickEvent.phase == Phase.END) {
            sendUpdates();
        }
    }

    public static class Handler implements IMessageHandler<BatchedPipeActivityMessage, IMessage> {

        @Override
        public IMessage onMessage(BatchedPipeActivityMessage message, MessageContext ctx) {
            var world = Minecraft.getMinecraft().theWorld;

            if (message.worldId != world.provider.dimensionId) return null;

            Integer lastChunkX = null, lastChunkZ = null;

            for (long coord : message.pipes) {
                int x = CoordinatePacker.unpackX(coord);
                int y = CoordinatePacker.unpackY(coord);
                int z = CoordinatePacker.unpackZ(coord);

                int chunkX = x >> 4;
                int chunkZ = z >> 4;

                // if this pipe's chunk isn't loaded, ignore it completely
                if (!Objects.equals(chunkX, lastChunkX) || !Objects.equals(chunkZ, lastChunkZ)) {
                    if (!world.getChunkProvider()
                        .chunkExists(chunkX, chunkZ)) {
                        continue;
                    } else {
                        lastChunkX = chunkX;
                        lastChunkZ = chunkZ;
                    }
                }

                if (world.getTileEntity(x, y, z) instanceof IGregTechTileEntity igte) {
                    if (igte.getMetaTileEntity() instanceof IActivePipe pipe) {
                        pipe.setActive(message.isActive);
                    }
                }
            }

            return null;
        }
    }
}
