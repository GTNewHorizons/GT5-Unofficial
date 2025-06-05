package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;

import gregtech.common.data.maglev.TetherManager;
import io.netty.buffer.ByteBuf;

public class GTPacketTether extends GTPacket {

    private int x;
    private int y;
    private int z;

    public GTPacketTether() {}

    public GTPacketTether(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TETHER.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        return new GTPacketTether(x, y, z);
    }

    @Override
    public void process(IBlockAccess world) {
        if (world != null) {
            TetherManager.PLAYER_RENDER_LINES.put(new BlockPos(x, y, z), 0);
        }
    }
}
