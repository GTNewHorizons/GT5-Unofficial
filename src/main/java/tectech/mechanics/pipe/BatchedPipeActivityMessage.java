package tectech.mechanics.pipe;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class BatchedPipeActivityMessage implements IMessage {

    public int worldId;
    public boolean isActive;
    public long[] pipes;

    @Override
    public void fromBytes(ByteBuf buf) {
        worldId = buf.readInt();
        isActive = buf.readBoolean();
        pipes = new long[buf.readInt()];
        for (int i = 0; i < pipes.length; i++) pipes[i] = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(worldId);
        buf.writeBoolean(isActive);
        buf.writeInt(pipes.length);
        for (int i = 0; i < pipes.length; i++) buf.writeLong(pipes[i]);
    }
}
