package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacket;
import gregtech.api.net.IGT_NetworkHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.GTNetwork;
import gregtech.common.items.matterManipulator.BlockAnalyzer.RequiredItemAnalysis;
import gregtech.common.items.matterManipulator.NBTState.BlockRemoveMode;
import gregtech.common.items.matterManipulator.NBTState.BlockSelectMode;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingAction;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import gregtech.common.items.matterManipulator.NBTState.PlaceMode;
import gregtech.common.items.matterManipulator.NBTState.Shape;
import gregtech.common.tileentities.machines.multi.MTEMMUplink;
import gregtech.common.tileentities.machines.multi.MTEMMUplink.UplinkState;
import io.netty.buffer.ByteBuf;

enum Messages {

    MMBPressed(
        server(simple((player, stack, manipulator, state) -> { manipulator.onMMBPressed(player, stack, state); }))),
    SetRemoveMode(server(enumPacket(BlockRemoveMode.values(), (state, value) -> state.config.removeMode = value))),
    SetPlaceMode(server(enumPacket(PlaceMode.values(), (player, stack, manipulator, state, value) -> {

        int requiredBit = switch (value) {
            case COPYING -> ItemMatterManipulator.ALLOW_COPYING;
            case EXCHANGING -> ItemMatterManipulator.ALLOW_EXCHANGING;
            case GEOMETRY -> 0;
            case MOVING -> ItemMatterManipulator.ALLOW_MOVING;
            case CABLES -> ItemMatterManipulator.ALLOW_CABLES;
        };

        if (manipulator.tier.hasCap(requiredBit)) {
            state.config.placeMode = value;
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
        state.config.action = PendingAction.MOVING_COORDS;
        state.config.coordAOffset = new Vector3i();
        state.config.coordBOffset = null;
        state.config.coordBOffset = null;
    }))),
    MoveB(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MOVING_COORDS;
        state.config.coordAOffset = null;
        state.config.coordBOffset = new Vector3i();
        state.config.coordBOffset = null;
    }))),
    MoveC(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MOVING_COORDS;
        state.config.coordAOffset = null;
        state.config.coordBOffset = null;
        state.config.coordBOffset = new Vector3i();
    }))),
    MoveAll(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MOVING_COORDS;

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

        if (state.config.coordC == null) {
            state.config.coordCOffset = null;
        } else {
            state.config.coordCOffset = state.config.coordC.toVec()
                .sub(lookingAt);
        }

    }))),
    MoveHere(server(simple((player, stack, manipulator, state) -> {
        if (state.config.shape.requiresC()) {
            if (Location.areCompatible(state.config.coordA, state.config.coordB, state.config.coordC)) {
                Vector3i offsetB = state.config.coordB.toVec()
                    .sub(state.config.coordA.toVec());
                Vector3i offsetC = state.config.coordC.toVec()
                    .sub(state.config.coordA.toVec());

                Vector3i newA = MMUtils.getLookingAtLocation(player);
                Vector3i newB = new Vector3i(newA).add(offsetB);
                Vector3i newC = new Vector3i(newA).add(offsetC);

                state.config.coordA = new Location(player.getEntityWorld(), newA);
                state.config.coordB = new Location(player.getEntityWorld(), newB);
                state.config.coordC = new Location(player.getEntityWorld(), newC);
            }
        } else {
            if (Location.areCompatible(state.config.coordA, state.config.coordB)) {
                Vector3i offsetB = state.config.coordB.toVec()
                    .sub(state.config.coordA.toVec());

                Vector3i newA = MMUtils.getLookingAtLocation(player);
                Vector3i newB = new Vector3i(newA).add(offsetB);

                state.config.coordA = new Location(player.getEntityWorld(), newA);
                state.config.coordB = new Location(player.getEntityWorld(), newB);
            }
        }
    }))),
    MarkCopy(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MARK_COPY_A;
        state.config.coordA = null;
        state.config.coordB = null;
    }))),
    MarkCut(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MARK_CUT_A;
        state.config.coordA = null;
        state.config.coordB = null;
    }))),
    MarkPaste(server(simple((player, stack, manipulator, state) -> {
        state.config.action = PendingAction.MARK_PASTE;
        state.config.coordC = null;
    }))),
    GetRequiredItems(server(new ISimplePacketHandler<IntPacket>() {

        @Override
        public void handle(EntityPlayer player, IntPacket packet) {
            ItemStack held = player.inventory.getCurrentItem();

            if (held != null && held.getItem() instanceof ItemMatterManipulator) {
                NBTState state = ItemMatterManipulator.getState(held);

                if (state.config.placeMode != PlaceMode.COPYING) {
                    return;
                }

                if (state.config.coordA == null || state.config.coordB == null || state.config.coordC == null) {
                    return;
                }

                List<PendingBlock> blocks = state.getPendingBlocks(player.getEntityWorld());
                RequiredItemAnalysis itemAnalysis = BlockAnalyzer.getRequiredItemsForBuild(player, blocks);

                if (itemAnalysis.requiredItems.isEmpty()) {
                    var requiredItems = itemAnalysis.requiredItems.entrySet()
                        .stream()
                        .map(
                            e -> String.format(
                                "%s: %d",
                                e.getKey()
                                    .getItemStack()
                                    .getDisplayName(),
                                e.getValue()))
                        .sorted()
                        .collect(Collectors.toList());

                    GTUtility.sendInfoToPlayer(player, "Required items:");

                    for (String item : requiredItems) {
                        GTUtility.sendInfoToPlayer(player, item);
                    }

                    GTUtility.sendInfoToPlayer(player, "All required items are present.");
                    return;
                }

                if (state.connectToUplink()) {
                    List<ItemStack> requiredItems = itemAnalysis.requiredItems.entrySet()
                        .stream()
                        .flatMap(e -> {
                            List<ItemStack> items = new ArrayList<>();

                            long amount = e.getValue() == null ? 0
                                : e.getValue()
                                    .longValue();

                            while (amount > 0) {
                                int toRemove = amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) amount;

                                items.add(
                                    e.getKey()
                                        .getItemStack(toRemove));

                                amount -= toRemove;
                            }

                            return items.stream();
                        })
                        .collect(Collectors.toList());

                    state.uplink.submitPlan(
                        player,
                        state.config.coordA.toString(),
                        requiredItems,
                        (packet.value & PLAN_AUTO_SUBMIT) != 0);
                } else {
                    GTUtility.sendErrorToPlayer(
                        player,
                        "Manipulator not connected to an uplink: cannot create a fake pattern.");

                    if (!itemAnalysis.requiredItems.isEmpty()) {
                        var requiredItems = itemAnalysis.requiredItems.entrySet()
                            .stream()
                            .map(
                                e -> String.format(
                                    "%s: %d",
                                    e.getKey()
                                        .getItemStack()
                                        .getDisplayName(),
                                    e.getValue()))
                            .sorted()
                            .collect(Collectors.toList());

                        GTUtility.sendInfoToPlayer(player, "Required items:");

                        for (String item : requiredItems) {
                            GTUtility.sendInfoToPlayer(player, item);
                        }
                    }
                }

                ItemMatterManipulator.setState(held, state);
            }
        }

        @Override
        public IntPacket getNewPacket(Messages message, @Nullable Object value) {
            IntPacket packet = new IntPacket(message);
            packet.value = value == null ? 0 : (int) (Integer) value;
            return packet;
        }
    })),
    ClearManualPlans(server(simple((player, stack, manipulator, state) -> {
        if (state.connectToUplink()) {
            state.uplink.clearManualPlans(player);
        }
    }))),
    ClearWhitelist(server(simple((player, stack, manipulator, state) -> { state.config.replaceWhitelist = null; }))),
    UpdateUplinkState(client(new ISimplePacketHandler<UplinkPacket>() {

        @Override
        @SideOnly(Side.CLIENT)
        public void handle(EntityPlayer player, UplinkPacket packet) {
            World theWorld = Minecraft.getMinecraft().theWorld;

            if (theWorld.provider.dimensionId == packet.worldId) {
                Location l = packet.getLocation();

                if (theWorld.getTileEntity(l.x, l.y, l.z) instanceof IGregTechTileEntity igte
                    && igte.getMetaTileEntity() instanceof MTEMMUplink uplink) {
                    uplink.setState(packet.getState());
                }
            }
        }

        @Override
        public UplinkPacket getNewPacket(Messages message, @Nullable Object value) {
            UplinkPacket packet = new UplinkPacket(message);

            if (value != null) {
                MTEMMUplink uplink = (MTEMMUplink) value;

                IGregTechTileEntity igte = uplink.getBaseMetaTileEntity();

                NBTState.Location l = new NBTState.Location(
                    igte.getWorld(),
                    igte.getXCoord(),
                    igte.getYCoord(),
                    igte.getZCoord());

                packet.setState(l, uplink.getState());
            }

            return packet;
        }
    })),

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

    public void sendToPlayersAround(Location location) {
        sendToPlayersAround(location, null);
    }

    public void sendToPlayersAround(Location location, Object data) {
        GTMod.GT_FML_LOGGER.info("Sending packet to players around " + location.toString() + ": " + this + "; " + data);
        channel.sendToAllAround(
            getNewPacket(data),
            new TargetPoint(location.worldId, location.x, location.y, location.z, 256d));
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

    public static void init() {
        // does nothing
    }

    private static interface ISimplePacketHandler<T extends SimplePacket> {

        public void handle(EntityPlayer player, T packet);

        public T getNewPacket(Messages message, @Nullable Object data);
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

    private static class UplinkPacket extends SimplePacket {

        public int worldId;
        public long location;
        public byte state;

        public UplinkPacket(Messages message) {
            super(message);
        }

        public void setState(Location location, UplinkState state) {
            this.worldId = location.worldId;
            this.location = CoordinatePacker.pack(location.x, location.y, location.z);
            this.state = (byte) state.ordinal();
        }

        public Location getLocation() {
            return new Location(
                worldId,
                CoordinatePacker.unpackX(location),
                CoordinatePacker.unpackY(location),
                CoordinatePacker.unpackZ(location));
        }

        public UplinkState getState() {
            return UplinkState.values()[state];
        }

        @Override
        public void encode(ByteBuf buffer) {
            buffer.writeInt(worldId);
            buffer.writeLong(location);
            buffer.writeByte(state);
        }

        @Override
        public GTPacket decode(ByteArrayDataInput buffer) {
            UplinkPacket message = new UplinkPacket(super.message);
            message.worldId = buffer.readInt();
            message.location = buffer.readLong();
            message.state = buffer.readByte();
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
            public T getNewPacket(Messages message, @Nullable Object data) {
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

                handleImpl(packet);
            }

            @SideOnly(Side.CLIENT)
            private void handleImpl(T packet) {
                next.handle(Minecraft.getMinecraft().thePlayer, packet);
            }

            @Override
            public T getNewPacket(Messages message, @Nullable Object data) {
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
            public SimplePacket getNewPacket(Messages message, @Nullable Object unused) {
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
            public IntPacket getNewPacket(Messages message, @Nullable Object value) {
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
            public IntPacket getNewPacket(Messages message, @Nullable Object value) {
                IntPacket packet = new IntPacket(message);
                packet.value = value == null ? -1 : ((E) value).ordinal();
                return packet;
            }
        };
    }

    public static final int PLAN_AUTO_SUBMIT = 0b1;
}
