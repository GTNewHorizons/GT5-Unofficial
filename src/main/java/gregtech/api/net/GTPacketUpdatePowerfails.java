package gregtech.api.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.enums.Mods;
import gregtech.common.data.GTPowerfailTracker.Powerfail;
import gregtech.crossmod.navigator.PowerfailLayerManager;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class GTPacketUpdatePowerfails extends GTPacket {

    /** sanity world id to make sure nothing has gone wrong */
    private int worldId;
    private List<Powerfail> powerfails;

    GTPacketUpdatePowerfails() {}

    public GTPacketUpdatePowerfails(int worldId, List<Powerfail> powerfails) {
        this.worldId = worldId;
        this.powerfails = powerfails;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.UPDATE_POWERFAILS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(worldId);
        buffer.writeInt(powerfails.size());

        for (Powerfail p : powerfails) {
            buffer.writeLong(p.getCoord());
            buffer.writeInt(p.mteId);
            buffer.writeInt(p.count);
            buffer.writeLong(p.lastOccurrence.getTime());
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPacketUpdatePowerfails packet = new GTPacketUpdatePowerfails();

        packet.worldId = buffer.readInt();

        int count = buffer.readInt();

        packet.powerfails = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Powerfail p = new Powerfail();

            p.dim = packet.worldId;

            p.setCoord(buffer.readLong());

            p.mteId = buffer.readInt();
            p.count = buffer.readInt();
            p.lastOccurrence = new Date(buffer.readLong());

            packet.powerfails.add(p);
        }

        return packet;
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (!(blockAccess instanceof World world)) return;
        if (this.worldId != world.provider.dimensionId) return;

        Long2ObjectOpenHashMap<Powerfail> powerfails = GTMod.clientProxy().powerfailRenderer.powerfails;
        powerfails.clear();

        for (Powerfail p : this.powerfails) {
            powerfails.put(p.getCoord(), p);
        }

        if (Mods.Navigator.isModLoaded()) {
            PowerfailLayerManager.INSTANCE.clearCurrentCache();
        }
    }
}
