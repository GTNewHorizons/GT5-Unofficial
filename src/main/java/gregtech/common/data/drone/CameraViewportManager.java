package gregtech.common.data.drone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.ItemStackGuiData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.modularui2.MetaTileEntityGuiHandler;
import gregtech.api.net.PacketObserveMachine;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.util.GTLog;
import gregtech.common.entity.EntityDrone;
import gregtech.common.items.ItemDroneRemoteInterface;

public class CameraViewportManager {

    public static final Map<UUID, ObservationSession> sessions = new ConcurrentHashMap<>();
    private static final List<Runnable> pendingServerActions = new ArrayList<>();

    public void setObservedMachineStatus(NBTTagCompound tag) {}

    public boolean isObservingActive() {
        return false;
    }

    public boolean isObservingActive(EntityPlayerMP player) {
        return CameraViewportManager.sessions.containsKey(player.getUniqueID());
    }

    public static void clearPlayerSession(EntityPlayerMP player) {
        ObservationSession session = sessions.remove(player.getUniqueID());
        if (session != null) {
            session.cleanup(player);
        }
    }

    public void resetStatus() {
        for (ObservationSession session : sessions.values()) {
            if (session.cameraEntity != null) {
                session.cameraEntity.setDead();
                session.cameraEntity = null;
            }
            session.active = false;
        }
        sessions.clear();
        synchronized (pendingServerActions) {
            pendingServerActions.clear();
        }
    }

    public boolean isSwitchingToRemoteGui() {
        return false;
    }

    public void setSwitchingToRemoteGui(boolean val) {}

    public void stopObserving() {}

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            clearPlayerSession(player);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            clearPlayerSession(player);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        List<Runnable> actions = null;
        synchronized (pendingServerActions) {
            if (!pendingServerActions.isEmpty()) {
                actions = new ArrayList<>(pendingServerActions);
                pendingServerActions.clear();
            }
        }
        if (actions != null) {
            for (Runnable action : actions) {
                action.run();
            }
        }
    }

    public static class ObservationSession {

        private final int dim;
        private final int centreX, centreY, centreZ;
        private final int machineX, machineY, machineZ;
        private final int targetChunkX, targetChunkZ;
        private boolean active = false;

        public boolean openedFromItem = false;
        public int hoveredX = -1;
        public int hoveredY = -1;
        public int hoveredZ = -1;

        EntityDrone cameraEntity;

        public int getDim() {
            return dim;
        }

        private static java.lang.reflect.Method getOrCreateChunkWatcherMethod;
        private static java.lang.reflect.Method addPlayerMethod;
        private static java.lang.reflect.Method removePlayerMethod;

        static {
            try {
                try {
                    getOrCreateChunkWatcherMethod = PlayerManager.class
                        .getDeclaredMethod("getOrCreateChunkWatcher", int.class, int.class, boolean.class);
                } catch (NoSuchMethodException e) {
                    getOrCreateChunkWatcherMethod = PlayerManager.class
                        .getDeclaredMethod("func_72690_a", int.class, int.class, boolean.class);
                }
                getOrCreateChunkWatcherMethod.setAccessible(true);

                Class<?> playerInstanceClass = Class
                    .forName("net.minecraft.server.management.PlayerManager$PlayerInstance");
                try {
                    addPlayerMethod = playerInstanceClass.getDeclaredMethod("addPlayer", EntityPlayerMP.class);
                } catch (NoSuchMethodException e) {
                    addPlayerMethod = playerInstanceClass.getDeclaredMethod("func_73255_a", EntityPlayerMP.class);
                }
                addPlayerMethod.setAccessible(true);

                try {
                    removePlayerMethod = playerInstanceClass.getDeclaredMethod("removePlayer", EntityPlayerMP.class);
                } catch (NoSuchMethodException e) {
                    removePlayerMethod = playerInstanceClass.getDeclaredMethod("func_73252_b", EntityPlayerMP.class);
                }
                removePlayerMethod.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace(GTLog.err);
            }
        }

        private static void addPlayerToWatcher(PlayerManager pm, int cx, int cz, EntityPlayerMP player) {
            if (getOrCreateChunkWatcherMethod == null || addPlayerMethod == null) return;
            try {
                Object playerInstance = getOrCreateChunkWatcherMethod.invoke(pm, cx, cz, true);
                if (playerInstance != null) {
                    addPlayerMethod.invoke(playerInstance, player);
                }
            } catch (Exception e) {
                e.printStackTrace(GTLog.err);
            }
        }

        private static void removePlayerFromWatcher(PlayerManager pm, int cx, int cz, EntityPlayerMP player) {
            if (getOrCreateChunkWatcherMethod == null || removePlayerMethod == null) return;
            try {
                Object playerInstance = getOrCreateChunkWatcherMethod.invoke(pm, cx, cz, false);
                if (playerInstance != null) {
                    removePlayerMethod.invoke(playerInstance, player);
                }
            } catch (Exception e) {
                e.printStackTrace(GTLog.err);
            }
        }

        private static int getViewRadius(PlayerManager pm) {
            try {
                java.lang.reflect.Field field;
                try {
                    field = PlayerManager.class.getDeclaredField("playerViewRadius");
                } catch (NoSuchFieldException e) {
                    try {
                        field = PlayerManager.class.getDeclaredField("field_72698_e");
                    } catch (NoSuchFieldException e2) {
                        return 10;
                    }
                }
                field.setAccessible(true);
                return field.getInt(pm);
            } catch (Exception e) {
                return 10;
            }
        }

        public ObservationSession(int dim, int centreX, int centreY, int centreZ, int machineX, int machineY,
            int machineZ) {
            this.dim = dim;
            this.centreX = centreX;
            this.centreY = centreY;
            this.centreZ = centreZ;
            this.machineX = machineX;
            this.machineY = machineY;
            this.machineZ = machineZ;
            this.targetChunkX = machineX >> 4;
            this.targetChunkZ = machineZ >> 4;
        }

        public void init(EntityPlayerMP player) {
            if (player.dimension != dim) {
                return;
            }
            WorldServer world = player.getServerForPlayer();
            PlayerManager pm = world.getPlayerManager();
            TileEntity centreTe = world.getTileEntity(centreX, centreY, centreZ);

            if (centreTe == null) {
                return;
            }

            for (int cx = targetChunkX - 2; cx <= targetChunkX + 2; cx++) {
                for (int cz = targetChunkZ - 2; cz <= targetChunkZ + 2; cz++) {
                    GTChunkManager.requestChunkLoad(centreTe, new ChunkCoordIntPair(cx, cz));
                    addPlayerToWatcher(pm, cx, cz, player);
                }
            }
            active = true;
            cameraEntity = new EntityDrone(world);
            cameraEntity.setPosition(machineX + 0.5, machineY + 1.5, machineZ + 0.5);
            world.spawnEntityInWorld(cameraEntity);
        }

        public void update(EntityPlayerMP player, double x, double y, double z, float yaw, int hX, int hY, int hZ) {
            if (!active) return;
            this.hoveredX = hX;
            this.hoveredY = hY;
            this.hoveredZ = hZ;

            if (cameraEntity != null) {
                cameraEntity.setPosition(x, y, z);
                cameraEntity.rotationYaw = yaw;
                cameraEntity.prevRotationYaw = yaw;
            }

            int chunkX = (int) Math.floor(x) >> 4;
            int chunkZ = (int) Math.floor(z) >> 4;

            if (Math.abs(chunkX - targetChunkX) > 2 || Math.abs(chunkZ - targetChunkZ) > 2) {
                cleanup(player);
                sessions.remove(player.getUniqueID());
                return;
            }

            WorldServer world = player.getServerForPlayer();
            int finalX = machineX;
            int finalY = machineY;
            int finalZ = machineZ;

            if (hX != -1 && hY != -1 && hZ != -1) {
                finalX = hX;
                finalY = hY;
                finalZ = hZ;
            }

            TileEntity te = world.getTileEntity(finalX, finalY, finalZ);
            if (te instanceof BaseMetaTileEntity gte) {
                NBTTagCompound statusTag = new NBTTagCompound();
                gte.getWailaNBTData(player, gte, statusTag, world, finalX, finalY, finalZ);
                statusTag.setInteger("observeX", finalX);
                statusTag.setInteger("observeY", finalY);
                statusTag.setInteger("observeZ", finalZ);
                PacketObserveMachine reply = new PacketObserveMachine(
                    dim,
                    centreX,
                    centreY,
                    centreZ,
                    machineX,
                    machineY,
                    machineZ,
                    true,
                    x,
                    y,
                    z,
                    yaw);
                reply.hoveredX = hX;
                reply.hoveredY = hY;
                reply.hoveredZ = hZ;
                reply.statusTag = statusTag;
                GTValues.NW.sendToPlayer(reply, player);
            }
        }

        public void cleanup(EntityPlayerMP player) {
            if (!active) return;
            try {
                WorldServer world = player.getServerForPlayer();
                PlayerManager pm = world.getPlayerManager();
                TileEntity centreTe = world.getTileEntity(centreX, centreY, centreZ);

                int r = getViewRadius(pm);
                int playerChunkX = ((int) Math.floor(player.posX)) >> 4;
                int playerChunkZ = ((int) Math.floor(player.posZ)) >> 4;

                for (int cx = targetChunkX - 2; cx <= targetChunkX + 2; cx++) {
                    for (int cz = targetChunkZ - 2; cz <= targetChunkZ + 2; cz++) {
                        boolean isWithinPlayerView = Math.abs(cx - playerChunkX) <= r
                            && Math.abs(cz - playerChunkZ) <= r;
                        if (!isWithinPlayerView) {
                            removePlayerFromWatcher(pm, cx, cz, player);
                        }
                        if (centreTe != null) {
                            GTChunkManager.releaseChunk(centreTe, new ChunkCoordIntPair(cx, cz));
                        }
                    }
                }
                if (centreTe != null) {
                    GTChunkManager.releaseTicket(centreTe);
                }
            } finally {
                if (cameraEntity != null) {
                    cameraEntity.setDead();
                    cameraEntity = null;
                }
                active = false;
            }
        }
    }

    public static void reopenDroneGuiOnServer(EntityPlayerMP player) {
        synchronized (pendingServerActions) {
            pendingServerActions.add(() -> reopenDroneGuiOnServerImpl(player));
        }
    }

    private static void reopenDroneGuiOnServerImpl(EntityPlayerMP player) {
        ObservationSession session = sessions.get(player.getUniqueID());
        if (session != null) {
            if (session.openedFromItem) {
                ItemStack stack = null;
                for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                    ItemStack s = player.inventory.mainInventory[i];
                    if (s != null && s.getItem() instanceof ItemDroneRemoteInterface) {
                        stack = s;
                        break;
                    }
                }
                if (stack != null) {
                    ItemDroneRemoteInterface item = (ItemDroneRemoteInterface) stack.getItem();
                    GuiManager.open(item.factory, new ItemStackGuiData(player, stack), player);
                }
            } else {
                WorldServer world = player.getServerForPlayer();
                if (world == null) return;
                TileEntity te = world.getTileEntity(session.centreX, session.centreY, session.centreZ);
                if (te instanceof IGregTechTileEntity gte) {
                    IMetaTileEntity mte = gte.getMetaTileEntity();
                    if (mte != null) {
                        MetaTileEntityGuiHandler.open(player, mte);
                    }
                }
            }
        }
    }
}
