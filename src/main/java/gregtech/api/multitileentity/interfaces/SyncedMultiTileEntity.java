package gregtech.api.multitileentity.interfaces;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;

import gregtech.api.net.GTPacketMultiTileEntity;

public interface SyncedMultiTileEntity {

    public static final int DEFAULT_TIMED_PACKET_PERIOD = 20;

    /**
     * Will send a packet to the client when they open the controller or access a casing.
     * Should only be sent to one player!
     */
    void sendFullPacket(@Nonnull EntityPlayerMP player);

    /**
     * Should always collect all the data that the controller or casing has and should send
     * Called by {@link #sendFullPacket()}
     *
     * @param packet The packet which will be sent
     */
    void getFullPacketData(GTPacketMultiTileEntity packet);

    /**
     * Will send a packet at a certain period of time, defined by {@link #getTimedPacketPeriod()}, to all players around
     * the controller or casing to send important information.
     * Redstone state, color, ect. It shouldn't send data about the internals like inventory and processing time
     */
    void sendTimedPacket();

    /**
     * Collects all the data that should be sent out at a certain period of time defined by
     * {@link #getTimedPacketPeriod()}
     * Called by {@link #sendTimedPacket()}
     *
     * @param packet The packet which will be sent
     */
    void getTimedPacketData(GTPacketMultiTileEntity packet);

    /**
     * Defines the period of time at which a timed packet should be sent out. Default 20 ticks
     */
    default int getTimedPacketPeriod() {
        return DEFAULT_TIMED_PACKET_PERIOD;
    }

    /**
     * Will send a packet, which should only contain data about how the TileEntity should be rendered.
     * !!! Warning !!! This is sent every single tick! Do not put a lot of data here!
     */
    void sendGraphicPacket();

    /**
     * Collects all the data that is needed to be send every single tick
     * Called by {@link #sendGraphicPacket()}
     *
     * @param packet The packet which will be sent
     */
    void getGraphicPacketData(GTPacketMultiTileEntity packet);
}
