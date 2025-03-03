package gregtech.api.net;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.BlockPosHighlighter;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

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
        int x = this.coords[0];
        int y = this.coords[1];
        int z = this.coords[2];
        System.out.printf("Processing teleport request to %d, %d, %d, dimension %d\n",x,y,z, this.dim);
        ServerConfigurationManager manager = player.mcServer.getConfigurationManager();
        if(manager.func_152596_g(player.getGameProfile())){ //Check if player can /tp
            if(player.dimension != this.dim){
                manager.transferPlayerToDimension(player, this.dim);
            }
            player.playerNetServerHandler.setPlayerLocation(x, y, z, player.cameraYaw, player.cameraPitch);
        } else{
            ArrayList<DimensionalCoord> list = new ArrayList<>();
            list.add(new DimensionalCoord(x,y,z,this.dim));
            double deltaX = x - player.posX;
            double deltaY = y - player.posY;
            double deltaZ = z  - player.posZ;

            double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            float yaw = (float) Math.toDegrees(Math.atan2(-deltaX, deltaZ));
            float pitch = (float) Math.toDegrees(Math.atan2(-deltaY, distanceXZ));
            player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, yaw, pitch);
            BlockPosHighlighter.highlightBlocks(player, list, "yay", "nay");
        }


    }

    public PacketTeleportPlayer(int dim, int x, int y, int z) {
        this.dim = dim;
        this.coords = new int[]{x,y,z};
    }

}
