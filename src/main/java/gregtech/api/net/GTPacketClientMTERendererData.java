package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.render.IMTERenderer;
import io.netty.buffer.ByteBuf;

/**
 * To sync rendering data of MTERenderer with the client
 */
public class GTPacketClientMTERendererData extends GTPacket {

    private int x, y, z;
    private IMTERenderer mteRenderer;
    private ByteArrayDataInput readBuffer;

    public GTPacketClientMTERendererData() {}

    public GTPacketClientMTERendererData(int x, int y, int z, IMTERenderer mteRenderer) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mteRenderer = mteRenderer;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SYNC_TILE_RENDER_DATA_TO_CLIENT.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        mteRenderer.encodeRenderData(buffer);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPacketClientMTERendererData packet = new GTPacketClientMTERendererData();
        packet.x = buffer.readInt();
        packet.y = buffer.readInt();
        packet.z = buffer.readInt();
        packet.readBuffer = buffer;
        return packet;
    }

    @Override
    public void process(IBlockAccess world) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (readBuffer != null && te instanceof IGregTechTileEntity gtTe
            && gtTe.getMetaTileEntity() instanceof IMTERenderer mteRenderer) {
            mteRenderer.decodeRenderData(readBuffer);
        }
    }

}
