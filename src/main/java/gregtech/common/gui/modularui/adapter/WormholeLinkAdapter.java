package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.common.tileentities.machines.multi.MTEWormholeGenerator;

public class WormholeLinkAdapter implements IByteBufAdapter<MTEWormholeGenerator.WormholeLink> {

    @Override
    public MTEWormholeGenerator.WormholeLink deserialize(PacketBuffer buffer) throws IOException {
        MTEWormholeGenerator.WormholeLink u = MTEWormholeGenerator.WormholeLink.get(buffer.readLong());
        u.mWormholeEnergy = buffer.readDouble();

        return u;
    }

    @Override
    public void serialize(PacketBuffer buffer, MTEWormholeGenerator.WormholeLink u) throws IOException {
        buffer.writeLong(u.mFrequency);
        buffer.writeDouble(u.mWormholeEnergy);
    }

    @Override
    public boolean areEqual(@NotNull MTEWormholeGenerator.WormholeLink t1,
        @NotNull MTEWormholeGenerator.WormholeLink t2) {
        return t1.mFrequency == t2.mFrequency && t1.mWormholeEnergy == t2.mWormholeEnergy;
    }
}
