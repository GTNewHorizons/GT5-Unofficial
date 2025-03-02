package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

public class PacketTeleportPlayer extends GTPacket{
    private int dim;
    private int[] coords;
    private EntityPlayerMP player;
    public PacketTeleportPlayer() { }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TELEPORT_PLAYER.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(dim);
        for(int i = 0; i < 3; i++) {
            buf.writeInt(coords[i]);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        return new PacketTeleportPlayer(
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        );
    }
    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            player = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }
    @Override
    public void process(IBlockAccess world) {
        System.out.printf("Processing teleport request to %d, %d, %d, dimension %d\n",this.coords[0],this.coords[1],this.coords[2], this.dim);
        System.out.println(player.getDisplayName());
        player.mcServer.getConfigurationManager().transferPlayerToDimension(player, this.dim);
        player.dimension = this.dim;
        player.setPositionAndUpdate(this.coords[0], this.coords[1], this.coords[2]);

    }

    public PacketTeleportPlayer(int dim, int x, int y, int z) {
        this.dim = dim;
        this.coords = new int[]{x,y,z};
    }

}
