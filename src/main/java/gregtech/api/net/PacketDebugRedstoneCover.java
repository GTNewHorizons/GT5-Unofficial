package gregtech.api.net;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.BlockPosHighlighter;
import io.netty.buffer.ByteBuf;

public class PacketDebugRedstoneCover extends GTPacket {

    private int dim;
    private int[] coords;
    private boolean teleportPlayer;
    private EntityPlayerMP player;

    public PacketDebugRedstoneCover() {}

    @Override
    public byte getPacketID() {
        return GTPacketTypes.DEBUG_COVER.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(dim);
        for (int i = 0; i < 3; i++) {
            buf.writeInt(coords[i]);
        }
        buf.writeBoolean(teleportPlayer);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        return new PacketDebugRedstoneCover(
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readBoolean());
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
        System.out.printf("Processing teleport request to %d, %d, %d, dimension %d\n", x, y, z, this.dim);
        ServerConfigurationManager manager = player.mcServer.getConfigurationManager();
        if (this.teleportPlayer) {
            if (player.dimension != this.dim) {
                manager.transferPlayerToDimension(player, this.dim);
            }
            player.playerNetServerHandler.setPlayerLocation(x, y + 1, z, player.cameraYaw, player.cameraPitch); // try
                                                                                                                // not
                                                                                                                // to
                                                                                                                // tp
                                                                                                                // the
                                                                                                                // player
                                                                                                                // into
                                                                                                                // the
                                                                                                                // hull
        }
        ArrayList<DimensionalCoord> list = new ArrayList<>();
        list.add(new DimensionalCoord(x, y, z, this.dim));
        double deltaX = x - player.posX;
        double deltaY = y - player.posY - 1;
        double deltaZ = z - player.posZ;

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) Math.toDegrees(Math.atan2(-deltaX, deltaZ));
        float pitch = (float) Math.toDegrees(Math.atan2(-deltaY, distanceXZ));
        if (this.dim == player.dimension) {
            player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, yaw, pitch);
            String foundMsg = String
                .format("Highlighting cover at %d,%d,%d", this.coords[0], this.coords[1], this.coords[2]);
            BlockPosHighlighter
                .highlightBlocks(player, list, foundMsg, "Cannot highlight because you're not in the same dimension!");
        } else {
            player.addChatMessage(new ChatComponentText("Cannot highlight because you're not in the same dimension!"));
        }

    }

    public PacketDebugRedstoneCover(int dim, int x, int y, int z, boolean teleportPlayer) {
        this.dim = dim;
        this.coords = new int[] { x, y, z };
        this.teleportPlayer = teleportPlayer;
    }

}
