package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import org.joml.Vector3i;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import gregtech.api.net.GTPacket;
import gregtech.api.net.IGT_NetworkHandler;
import gregtech.common.GTNetwork;
import gregtech.common.items.matterManipulator.NBTState.BlockRemoveMode;
import gregtech.common.items.matterManipulator.NBTState.BlockSelectMode;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingAction;
import gregtech.common.items.matterManipulator.NBTState.PlaceMode;
import gregtech.common.items.matterManipulator.NBTState.Shape;
import io.netty.buffer.ByteBuf;

enum Messages {

    MMBPressed(
        server(simple((player, stack, manipulator, state) -> { manipulator.onMMBPressed((EntityPlayerMP) player); }))),
    SetRemoveMode(server(enumPacket(BlockRemoveMode.values(), (state, value) -> state.config.removeMode = value))),
    SetPlaceMode(server(enumPacket(PlaceMode.values(), (player, stack, manipulator, state, value) -> {
        state.config.placeMode = value;
        if (!manipulator.tier.mAllowCopying
            && (state.config.placeMode == PlaceMode.COPYING || state.config.placeMode == PlaceMode.MOVING)) {
            state.config.placeMode = PlaceMode.GEOMETRY;
        }
    }))),
    SetBlockSelectMode(
        server(enumPacket(BlockSelectMode.values(), (state, value) -> state.config.blockSelectMode = value))),
    SetPendingAction(server(enumPacket(PendingAction.values(), (state, value) -> state.config.action = value))),
    ClearBlocks(server(simple((player, stack, manipulator, state) -> {
        state.config.setCorners(null);
        state.config.setEdges(null);
        state.config.setFaces(null);
        state.config.setVolumes(null);
        state.config.action = null;
    }))),
    SetShape(server(enumPacket(Shape.values(), (state, value) -> state.config.shape = value))),
    MoveA(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.GEOM_MOVING_COORDS;
        state.config.coordAOffset = new Vector3i();
        state.config.coordBOffset = null;
    }))),
    MoveB(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.GEOM_MOVING_COORDS;
        state.config.coordAOffset = null;
        state.config.coordBOffset = new Vector3i();
    }))),
    MoveBoth(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.GEOM_MOVING_COORDS;

        Vector3i lookingAt = MMUtils.getLookingAtLocation(player);

        if (state.config.coordA == null) {
            state.config.coordAOffset = null;
        } else {
            state.config.coordAOffset = state.config.coordA.toVec()
                .sub(lookingAt);
        }

        if (state.config.coordB == null) {
            state.config.coordBOffset = null;
        } else {
            state.config.coordBOffset = state.config.coordB.toVec()
                .sub(lookingAt);
        }
    }))),
    MoveHere(server(simple((player, stack, manipulator, state) -> {
        if (Location.areCompatible(state.config.coordA, state.config.coordB)) {
            Vector3i deltas = MMUtils.getRegionDeltas(state.config.coordA, state.config.coordB);

            Vector3i newA = MMUtils.getLookingAtLocation(player);
            Vector3i newB = new Vector3i(newA).add(deltas);

            state.config.coordA = new Location(player.getEntityWorld(), newA);
            state.config.coordB = new Location(player.getEntityWorld(), newB);
        }
    }))),
    MarkCopy(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MARK_COPY_A;
        state.config.coordA = null;
        state.config.coordB = null;
    }))),
    MarkPaste(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MARK_PASTE;
        state.config.coordC = null;
    }))),

    ;

    public static final IGT_NetworkHandler channel = createNetwork();

    private ISimplePacketHandler<? extends SimplePacket> handler;

    private <T extends SimplePacket> Messages(ISimplePacketHandler<T> handler) {
        this.handler = handler;
    }

    public SimplePacket getNewPacket() {
        return handler.getNewPacket(this, null);
    }

    public SimplePacket getNewPacket(Object data) {
        return handler.getNewPacket(this, data);
    }

    public void sendToServer() {
        sendToServer(null);
    }

    public void sendToServer(Object data) {
        GTMod.GT_FML_LOGGER.info("Sending packet to server: " + this + "; " + data);
        channel.sendToServer(getNewPacket(data));
    }

    public void sendToPlayer(EntityPlayerMP player) {
        sendToPlayer(player, null);
    }

    public void sendToPlayer(EntityPlayerMP player, Object data) {
        GTMod.GT_FML_LOGGER.info("Sending packet to player: " + this + "; " + data + "; " + player);
        channel.sendToPlayer(getNewPacket(data), player);
    }

    @SuppressWarnings("unchecked")
    public void handle(EntityPlayer player, SimplePacket packet) {
        GTMod.GT_FML_LOGGER
            .info("Handling packet: " + this + "; " + packet + "; " + player + "; " + NetworkUtils.isClient());
        ((ISimplePacketHandler<SimplePacket>) handler).handle(player, packet);
    }

    public static GTNetwork createNetwork() {
        ArrayList<GTPacket> packets = new ArrayList<>();

        for (Messages message : values()) {
            packets.add(message.getNewPacket());
        }

        return new GTNetwork("MatterManipulator", packets.toArray(new GTPacket[0]));
    }

    private static interface ISimplePacketHandler<T extends SimplePacket> {

        public void handle(EntityPlayer player, T packet);

        public T getNewPacket(Messages message, Object data);
    }

    private static class SimplePacket extends GTPacket {

        public final Messages message;

        public SimplePacket(Messages message) {
            this.message = message;
        }

        @Override
        public byte getPacketID() {
            return (byte) message.ordinal();
        }

        @Override
        public GTPacket decode(ByteArrayDataInput buffer) {
            return message.getNewPacket();
        }

        @Override
        public void encode(ByteBuf buffer) {

        }

        private EntityPlayerMP playerMP;

        @Override
        public void setINetHandler(INetHandler handler) {
            playerMP = handler instanceof NetHandlerPlayServer server ? server.playerEntity : null;
        }

        @Override
        public void process(IBlockAccess world) {
            message.handle(playerMP, this);
        }
    }

    private static class IntPacket extends SimplePacket {

        public int value;

        public IntPacket(Messages message) {
            super(message);
        }

        @Override
        public void encode(ByteBuf buffer) {
            buffer.writeInt(value);
        }

        @Override
        public GTPacket decode(ByteArrayDataInput buffer) {
            IntPacket message = new IntPacket(super.message);
            message.value = buffer.readInt();
            return message;
        }
    }

    private static <T extends SimplePacket> ISimplePacketHandler<T> server(ISimplePacketHandler<T> next) {
        return new ISimplePacketHandler<T>() {

            @Override
            public void handle(EntityPlayer player, T packet) {
                if (player == null) {
                    GTMod.GT_FML_LOGGER.error(
                        "Player was null when trying to process " + packet.message.name()
                            + " packet: it will be ignored");
                    return;
                }

                next.handle(player, packet);
            }

            @Override
            public T getNewPacket(Messages message, Object data) {
                return next.getNewPacket(message, data);
            }
        };
    }

    private static <T extends SimplePacket> ISimplePacketHandler<T> client(ISimplePacketHandler<T> next) {
        return new ISimplePacketHandler<T>() {

            @Override
            public void handle(EntityPlayer player, T packet) {
                if (player != null) {
                    GTMod.GT_FML_LOGGER.error(
                        "Player wasn't null when trying to process " + packet.message.name()
                            + " packet: it will be ignored");
                    return;
                }

                next.handle(Minecraft.getMinecraft().thePlayer, packet);
            }

            @Override
            public T getNewPacket(Messages message, Object data) {
                return next.getNewPacket(message, data);
            }
        };
    }

    private static interface ISimpleHandler {

        public void handle(EntityPlayer player, ItemStack stack, ItemMatterManipulator manipulator, NBTState state);
    }

    private static ISimplePacketHandler<SimplePacket> simple(ISimpleHandler handler) {
        return new ISimplePacketHandler<Messages.SimplePacket>() {

            @Override
            public void handle(EntityPlayer player, SimplePacket packet) {
                ItemStack held = player.inventory.getCurrentItem();

                if (held != null && held.getItem() instanceof ItemMatterManipulator manipulator) {
                    NBTState state = ItemMatterManipulator.getState(held);

                    handler.handle(player, held, manipulator, state);

                    ItemMatterManipulator.setState(held, state);
                }
            }

            @Override
            public SimplePacket getNewPacket(Messages message, Object unused) {
                return new SimplePacket(message);
            }
        };
    }

    private static <E extends Enum<E>> ISimplePacketHandler<IntPacket> enumPacket(E[] values,
        BiConsumer<NBTState, E> setter) {
        return new ISimplePacketHandler<IntPacket>() {

            @Override
            public void handle(EntityPlayer player, IntPacket packet) {
                E value = packet.value < 0 || packet.value >= values.length ? null : values[packet.value];

                ItemStack held = player.inventory.getCurrentItem();

                if (held != null && held.getItem() instanceof ItemMatterManipulator) {
                    NBTState state = ItemMatterManipulator.getState(held);

                    setter.accept(state, value);

                    ItemMatterManipulator.setState(held, state);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public IntPacket getNewPacket(Messages message, Object value) {
                IntPacket packet = new IntPacket(message);
                packet.value = value == null ? -1 : ((E) value).ordinal();
                return packet;
            }
        };
    }

    private static interface IEnumSetter<E extends Enum<E>> {

        public void set(EntityPlayer player, ItemStack stack, ItemMatterManipulator manipulator, NBTState state,
            E value);
    }

    private static <E extends Enum<E>> ISimplePacketHandler<IntPacket> enumPacket(E[] values, IEnumSetter<E> setter) {
        return new ISimplePacketHandler<IntPacket>() {

            @Override
            public void handle(EntityPlayer player, IntPacket packet) {
                E value = packet.value < 0 || packet.value >= values.length ? null : values[packet.value];

                ItemStack held = player.inventory.getCurrentItem();

                if (held != null && held.getItem() instanceof ItemMatterManipulator manipulator) {
                    NBTState state = ItemMatterManipulator.getState(held);

                    setter.set(player, held, manipulator, state, value);

                    ItemMatterManipulator.setState(held, state);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public IntPacket getNewPacket(Messages message, Object value) {
                IntPacket packet = new IntPacket(message);
                packet.value = value == null ? -1 : ((E) value).ordinal();
                return packet;
            }
        };
    }

    private static class InteractPacket extends SimplePacket {

        public PlayerInteractEvent.Action action;
        public int x;
        public int y;
        public int z;
        public int face;
        public World world;

        public InteractPacket(Messages message) {
            super(message);
        }

        public InteractPacket(Messages message, PlayerInteractEvent event) {
            super(message);
            this.action = event.action;
            this.x = event.x;
            this.y = event.y;
            this.z = event.z;
            this.face = event.face;
            this.world = event.world;
        }

        @Override
        public void encode(ByteBuf buffer) {
            buffer.writeByte(action.ordinal());
            buffer.writeInt(x);
            buffer.writeInt(y);
            buffer.writeInt(z);
            buffer.writeByte(face);
            buffer.writeInt(world.provider.dimensionId);
        }

        @Override
        public GTPacket decode(ByteArrayDataInput buffer) {
            InteractPacket packet = new InteractPacket(message);

            action = PlayerInteractEvent.Action.values()[buffer.readByte()];
            x = buffer.readInt();
            y = buffer.readInt();
            z = buffer.readInt();
            face = buffer.readByte();

            int worldId = buffer.readInt();

            for (WorldServer world : MinecraftServer.getServer().worldServers) {
                if (world.provider.dimensionId == worldId) {
                    packet.world = world;
                    break;
                }
            }

            return packet;
        }
    }
}
