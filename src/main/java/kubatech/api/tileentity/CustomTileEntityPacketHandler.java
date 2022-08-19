package kubatech.api.tileentity;

import kubatech.api.network.CustomTileEntityPacket;

public interface CustomTileEntityPacketHandler {
    void HandleCustomPacket(CustomTileEntityPacket customdata);
}
