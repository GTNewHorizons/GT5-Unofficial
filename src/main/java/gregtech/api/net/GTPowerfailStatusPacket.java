package gregtech.api.net;

import java.util.List;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.client.GTPowerfailRenderer;
import gregtech.common.data.GTPowerfailTracker;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

public class GTPowerfailStatusPacket extends GTPacket {

    public PowerfailAction action;
    /** sanity world id to make sure nothing has gone wrong */
    public int worldId;
    /** {packed x,y,z for each machine with a powerfail} */
    public LongList powerfailedMachines;

    public enum PowerfailAction {
        Set,
        Show,
        Hide
    }

    public GTPowerfailStatusPacket() {}

    public static GTPowerfailStatusPacket show() {
        GTPowerfailStatusPacket packet = new GTPowerfailStatusPacket();

        packet.action = PowerfailAction.Show;

        return packet;
    }

    public static GTPowerfailStatusPacket hide() {
        GTPowerfailStatusPacket packet = new GTPowerfailStatusPacket();

        packet.action = PowerfailAction.Hide;

        return packet;
    }

    public static GTPowerfailStatusPacket set(int worldId, List<GTPowerfailTracker.Powerfail> powerfails) {
        GTPowerfailStatusPacket packet = new GTPowerfailStatusPacket();

        packet.action = PowerfailAction.Set;
        packet.worldId = worldId;
        packet.powerfailedMachines = new LongArrayList(powerfails.size());

        for (GTPowerfailTracker.Powerfail powerfail : powerfails) {
            packet.powerfailedMachines.add(CoordinatePacker.pack(powerfail.x, powerfail.y, powerfail.z));
        }

        return packet;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.POWERFAIL_STATUS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeByte(action.ordinal());

        if (action == PowerfailAction.Set) {
            buffer.writeInt(worldId);
            buffer.writeInt(powerfailedMachines.size());

            for (long l : powerfailedMachines) {
                buffer.writeLong(l);
            }
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPowerfailStatusPacket packet = new GTPowerfailStatusPacket();

        packet.action = PowerfailAction.values()[buffer.readByte()];

        if (packet.action == PowerfailAction.Set) {
            packet.worldId = buffer.readInt();

            int count = buffer.readInt();

            packet.powerfailedMachines = new LongArrayList(count);

            for (int i = 0; i < count; i++) {
                packet.powerfailedMachines.add(buffer.readLong());
            }
        }

        return packet;
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (!(blockAccess instanceof World world)) return;

        switch (action) {
            case Set -> {
                if (this.worldId == world.provider.dimensionId) {
                    GTPowerfailRenderer.POWERFAILS = this.powerfailedMachines;
                }
            }
            case Show -> {
                GTPowerfailRenderer.DO_RENDER = true;
            }
            case Hide -> {
                GTPowerfailRenderer.DO_RENDER = false;
            }
        }
    }
}
