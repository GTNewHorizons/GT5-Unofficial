package gregtech.api.net;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.util.DimensionalCoord;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;

public class PacketTeleportPlayer extends GTPacket {

    private int dim;

    private int[] coords;
    private boolean teleportPlayer;
    private EntityPlayerMP player;

    public PacketTeleportPlayer() {}

    @Override
    public byte getPacketID() {
        return GTPacketTypes.PLAYER_TELEPORT.id;
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
        return new PacketTeleportPlayer(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            player = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote) return;
        int x = this.coords[0];
        int y = this.coords[1];
        int z = this.coords[2];
        if (this.teleportPlayer) {
            boolean isOp = player.mcServer.getConfigurationManager()
                .func_152596_g(player.getGameProfile());
            if (!isOp && !isChaosLocatorTarget(x, y, z)) return;
            if (x < -30_000_000 || x >= 30_000_000
                || z < -30_000_000
                || z >= 30_000_000
                || y < 0
                || y >= 256
                || !DimensionManager.isDimensionRegistered(this.dim)) return;
            WorldServer destinationWorld = player.mcServer.worldServerForDimension(this.dim);
            if (destinationWorld == null) return;
            if (player.dimension != this.dim) {
                if (!GTUtility.moveEntityToDimensionAtCoords(player, this.dim, x + 0.5, y + 1, z + 0.5)) return;
            } else {
                player.playerNetServerHandler
                    .setPlayerLocation(x + 0.5, y + 1, z + 0.5, player.cameraYaw, player.cameraPitch);
                // try not to tp the player into the hull
            }

        }
        ArrayList<DimensionalCoord> list = new ArrayList<>();
        list.add(new DimensionalCoord(x, y, z, this.dim));
        double deltaX = x + 0.5 - player.posX;
        double deltaY = y - player.posY - 1;
        double deltaZ = z + 0.5 - player.posZ;

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) Math.toDegrees(Math.atan2(-deltaX, deltaZ));
        float pitch = (float) Math.toDegrees(Math.atan2(-deltaY, distanceXZ));
        if (this.dim == player.dimension) {
            player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, yaw, pitch);
        } else {
            player.addChatMessage(new ChatComponentText("Cannot highlight because you're not in the same dimension!"));
        }

    }

    private boolean isChaosLocatorTarget(int x, int y, int z) {
        ItemStack heldItem = player.inventory.getCurrentItem();
        if (!ItemList.ChaosLocator.isStackEqual(heldItem, false, true) || this.dim != 1 || y != 200) return false;
        NBTTagCompound tag = heldItem.getTagCompound();
        // An unconfigured locator uses the GUI default coordinates (0, 0).
        int locatorX = tag == null ? 0 : tag.getInteger("xCoordinate");
        int locatorZ = tag == null ? 0 : tag.getInteger("zCoordinate");
        // Match the locator's server-side coordinates, not an arbitrary destination supplied by the client.
        return locatorX >= -1000 && locatorX <= 1000
            && locatorZ >= -1000
            && locatorZ <= 1000
            && x == locatorX * 10_000
            && z == locatorZ * 10_000;
    }

    public PacketTeleportPlayer(int dim, int x, int y, int z, boolean teleportPlayer) {
        this.dim = dim;
        this.coords = new int[] { x, y, z };
        this.teleportPlayer = teleportPlayer;
    }

}
