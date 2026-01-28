package gregtech.common.render;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketClientMTERendererData;
import io.netty.buffer.ByteBuf;

public interface IMTERenderer {

    void renderTESR(double x, double y, double z, float timeSinceLastTick);

    default AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    default double getMaxRenderDistanceSquared() {
        return 4096.0D;
    }

    /**
     * Encore render data used by the client (Server side)
     * 
     * @param buffer
     */
    default void encodeRenderData(ByteBuf buffer) {};

    /**
     * Decodes render data used by the client (Client side)
     * 
     * @param buffer
     */
    default void decodeRenderData(ByteArrayDataInput buffer) {};

    /**
     * Send render data to the client
     * 
     * @param mte
     */
    default void sendRenderDataToClient(IMetaTileEntity mte) {
        IGregTechTileEntity tile = mte.getBaseMetaTileEntity();
        if (tile.isClientSide()) return;

        double maxDistSq = getMaxRenderDistanceSquared();
        int x = tile.getXCoord();
        int y = tile.getYCoord();
        int z = tile.getZCoord();

        tile.getWorld().playerEntities.stream()
            .filter(player -> player.getDistanceSq(x + 0.5, y + 0.5, z + 0.5) <= maxDistSq)
            .forEach(
                player -> GTValues.NW
                    .sendToPlayer(new GTPacketClientMTERendererData(x, y, z, this), (EntityPlayerMP) player));
    }
}
