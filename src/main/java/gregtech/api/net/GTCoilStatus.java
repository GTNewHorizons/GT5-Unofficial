package gregtech.api.net;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.interfaces.IBlockWithActiveOffset;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;

public class GTCoilStatus extends GTPacket {

    public int worldId;
    public boolean isActive;
    public LongCollection coils;

    public GTCoilStatus() {}

    public GTCoilStatus(int worldId, boolean isActive, LongCollection coils) {
        this.worldId = worldId;
        this.isActive = isActive;
        this.coils = coils;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.COIL_STATUS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(worldId);
        buffer.writeBoolean(isActive);

        buffer.writeInt(coils.size());

        for (long l : coils) {
            buffer.writeLong(l);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTCoilStatus packet = new GTCoilStatus();

        packet.worldId = buffer.readInt();
        packet.isActive = buffer.readBoolean();

        int count = buffer.readInt();

        packet.coils = new LongArrayList(count);

        for (int i = 0; i < count; i++) {
            packet.coils.add(buffer.readLong());
        }

        return packet;
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (!(blockAccess instanceof World world)) return;
        if (world.provider.dimensionId != worldId) return;

        for (long coil : coils) {
            int x = CoordinatePacker.unpackX(coil);
            int y = CoordinatePacker.unpackY(coil);
            int z = CoordinatePacker.unpackZ(coil);

            if (world.getBlock(x, y, z) instanceof IBlockWithActiveOffset offset) {
                int meta = world.getBlockMetadata(x, y, z);

                meta %= offset.getActiveOffset();
                if (isActive) meta += offset.getActiveOffset();

                world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            }
        }
    }
}
