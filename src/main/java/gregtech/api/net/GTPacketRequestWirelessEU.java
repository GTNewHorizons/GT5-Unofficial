package gregtech.api.net;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.misc.WirelessNetworkManager;
import io.netty.buffer.ByteBuf;

public class GTPacketRequestWirelessEU extends GTPacket {

    private UUID uuid;
    private BigInteger EU;
    private EntityPlayerMP player;

    public GTPacketRequestWirelessEU() {}

    public GTPacketRequestWirelessEU(UUID uuid) {
        this.uuid = uuid;
    }

    public GTPacketRequestWirelessEU(UUID uuid, BigInteger EU) {
        this.uuid = uuid;
        this.EU = EU;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.REQUEST_WIRELESS_EU.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        String uuidString = uuid.toString();
        buffer.writeInt(uuidString.length());
        for (int i = 0; i < uuidString.length(); i++) {
            buffer.writeChar(uuidString.charAt(i));
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int length = buffer.readInt();
        char[] uuid = new char[length];
        for (int i = 0; i < length; i++) {
            uuid[i] = buffer.readChar();
        }
        String uuidString = new String(uuid);
        return new GTPacketRequestWirelessEU(UUID.fromString(uuidString));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        System.out.println("REQUESTING WIRELESS EU!");
        NW.sendToPlayer(new GTPacketSendWirelessEU(WirelessNetworkManager.getUserEU(uuid)), player);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer handler) {
            player = handler.playerEntity;
        }
    }
}
