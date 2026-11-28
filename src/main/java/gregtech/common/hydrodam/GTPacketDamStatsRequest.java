package gregtech.common.hydrodam;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.ServerThreadUtil;

import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import io.netty.buffer.ByteBuf;

/// Asks the server for the litres and net change of the volume owning the dam water at a position, in the sender's
/// dimension. Answered with {@link GTPacketDamStats}, unless the position fails validation.
public class GTPacketDamStatsRequest extends GTPacket {

    /// How far from the sender the position may be, in blocks. Well beyond WAILA's range, which follows the player's
    /// reach.
    private static final double MAX_DISTANCE = 16;

    private int x, y, z;
    private EntityPlayerMP player;

    public GTPacketDamStatsRequest() {}

    public GTPacketDamStatsRequest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.DAM_STATS_REQUEST.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        return new GTPacketDamStatsRequest(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void setINetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer serverHandler) player = serverHandler.playerEntity;
    }

    /// Packets may arrive on a network thread, so the volumes are read on the server thread.
    @Override
    public void process(IBlockAccess world) {
        EntityPlayerMP sender = player;

        if (sender == null) return;

        int requestX = x;
        int requestY = y;
        int requestZ = z;

        ServerThreadUtil.addScheduledTask(() -> reply(sender, requestX, requestY, requestZ));
    }

    /// Answers only for dam water near the sender that belongs to a volume; anything else is ignored.
    private static void reply(EntityPlayerMP player, int x, int y, int z) {
        if (player.getDistanceSq(x + 0.5, y + 0.5, z + 0.5) > MAX_DISTANCE * MAX_DISTANCE) return;

        World world = player.worldObj;
        DamVolumes volumes = DamVolumes.get(world);
        WaterVolume volume = volumes.getVolumeAt(x, y, z);

        if (volume == null) return;

        GTValues.NW.sendToPlayer(
            new GTPacketDamStats(
                world.provider.dimensionId,
                volume.getId(),
                volume.getLitres(),
                volumes.getNetLitresPerTick(volume)),
            player);
    }
}
