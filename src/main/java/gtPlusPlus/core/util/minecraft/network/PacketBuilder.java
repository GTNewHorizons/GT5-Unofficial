package gtPlusPlus.core.util.minecraft.network;

import net.minecraft.world.WorldServer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class PacketBuilder {

    private static PacketBuilder instance;

    public static PacketBuilder instance() {
        if (instance == null) {
            instance = new PacketBuilder();
        }
        return instance;
    }

    public void sendTileEntityPacket(IGregTechTileEntity tile) {
        if (tile.getWorld() instanceof WorldServer) {
            WorldServer world = (WorldServer) tile.getWorld();
            PacketTileEntity pkt = new PacketTileEntity(tile);
            PacketDispatcher.sendToWatchers(pkt, world, tile.getXCoord(), tile.getZCoord());
        }
    }
}
