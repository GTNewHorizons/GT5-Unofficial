package gregtech.api.net;

import java.math.BigInteger;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.handlers.PowerGogglesHudHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketSendWirelessEU extends GTPacket {

    BigInteger EU;

    public GTPacketSendWirelessEU() {}

    public GTPacketSendWirelessEU(BigInteger EU) {
        this.EU = EU;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SEND_WIRELESS_EU.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        byte[] EUBytes = EU.toByteArray();
        buffer.writeInt(EUBytes.length);
        buffer.writeBytes(EUBytes);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int length = buffer.readInt();
        byte[] eu = new byte[length];
        for (int i = 0; i < length; i++) {
            eu[i] = buffer.readByte();
        }
        BigInteger EU = new BigInteger(eu);
        return new GTPacketSendWirelessEU(EU);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        System.out.println("SENDING WIRELESS EU!");
        PowerGogglesHudHandler.setMeasurement(this.EU);
        PowerGogglesHudHandler.drawTick();
    }
}
