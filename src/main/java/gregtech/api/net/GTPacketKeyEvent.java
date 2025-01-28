package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IKeyHandlerTile;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;

public class GTPacketKeyEvent extends GTPacket {

    private int mX, mZ;
    private short mY;
    private byte mKey;
    private byte mAction;
    private String mPlayerName; // Add player name

    public GTPacketKeyEvent() {
        super();
    }

    public GTPacketKeyEvent(int aX, short aY, int aZ, byte aKey, byte aAction, String aPlayerName) {
        super();
        mX = aX;
        mY = aY;
        mZ = aZ;
        mKey = aKey;
        mAction = aAction;
        mPlayerName = aPlayerName;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.KEY_EVENT.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        aOut.writeByte(mKey);
        aOut.writeByte(mAction);
        writeString(aOut, mPlayerName);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketKeyEvent(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            aData.readByte(),
            aData.readByte(),
            readString(aData));
    }

    private void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    private String readString(ByteArrayDataInput data) {
        int length = data.readInt();
        byte[] bytes = new byte[length];
        data.readFully(bytes);
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (!(aWorld instanceof World) || ((World) aWorld).isRemote) return;

        World world = (World) aWorld;
        TileEntity tTileEntity = world.getTileEntity(mX, mY, mZ);

        // Find the player
        EntityPlayer player = null;
        for (Object obj : world.playerEntities) {
            if (obj instanceof EntityPlayer && ((EntityPlayer) obj).getCommandSenderName()
                .equals(mPlayerName)) {
                player = (EntityPlayer) obj;
                break;
            }
        }

        if (player == null) {
            // Can't send chat if we don't have a player
            return;
        }

        GTUtility.sendChatToPlayer(player, "Processing packet at: " + mX + ", " + mY + ", " + mZ);

        if (tTileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity mte = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            if (mte instanceof IKeyHandlerTile) {
                ((IKeyHandlerTile) mte).onKeyInteraction(mKey, mAction == 1, player);
            } else {
                GTUtility.sendChatToPlayer(player, "Error: TileEntity does not handle key events");
            }
        } else {
            GTUtility.sendChatToPlayer(player, "Error: Invalid TileEntity type");
        }
    }
}
