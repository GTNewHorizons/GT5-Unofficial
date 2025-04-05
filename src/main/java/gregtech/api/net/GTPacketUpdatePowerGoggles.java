package gregtech.api.net;

import java.math.BigInteger;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.handlers.PowerGogglesHudHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketUpdatePowerGoggles extends GTPacket {

    BigInteger EU;
    boolean refresh;

    public GTPacketUpdatePowerGoggles() {}

    public GTPacketUpdatePowerGoggles(BigInteger EU) {
        this.EU = EU;
        this.refresh = false;
    }

    public GTPacketUpdatePowerGoggles(BigInteger EU, boolean refresh) {
        this.EU = EU;
        this.refresh = refresh;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SEND_WIRELESS_EU.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeBoolean(refresh);
        byte[] EUBytes = EU.toByteArray();
        buffer.writeInt(EUBytes.length);
        buffer.writeBytes(EUBytes);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        boolean refresh = buffer.readBoolean();
        int length = buffer.readInt();
        byte[] eu = new byte[length];
        for (int i = 0; i < length; i++) {
            eu[i] = buffer.readByte();
        }
        BigInteger EU = new BigInteger(eu);
        return new GTPacketUpdatePowerGoggles(EU, refresh);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (this.refresh) {
            PowerGogglesHudHandler.clear();
            PowerGogglesHudHandler.setMeasurement(this.EU);
            PowerGogglesHudHandler.drawTick();
        } else {
            PowerGogglesHudHandler.setMeasurement(this.EU);
            PowerGogglesHudHandler.updateClient = true;
        }

    }
}
