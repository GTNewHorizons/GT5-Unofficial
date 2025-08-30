package gregtech.api.net;

import java.math.BigInteger;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketUpdatePowerGoggles extends GTPacket {

    BigInteger EU;
    long lscCapacity;
    boolean refresh;

    public GTPacketUpdatePowerGoggles() {}

    public GTPacketUpdatePowerGoggles(BigInteger EU) {
        this.EU = EU;
        this.refresh = false;
        this.lscCapacity = 0;
    }

    public GTPacketUpdatePowerGoggles(BigInteger EU, long lscCapacity, boolean refresh) {
        this(EU, refresh);
        this.lscCapacity = lscCapacity;
    }

    public GTPacketUpdatePowerGoggles(BigInteger EU, boolean refresh) {
        this.EU = EU;
        this.refresh = refresh;
        this.lscCapacity = 0;
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
        buffer.writeLong(lscCapacity);
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
        long lscCapacity = buffer.readLong();
        return new GTPacketUpdatePowerGoggles(EU, lscCapacity, refresh);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (this.refresh) {
            PowerGogglesHudHandler.clear();
        }
        PowerGogglesHudHandler.setMeasurement(this.EU, lscCapacity);
        PowerGogglesHudHandler.drawTick();
        PowerGogglesHudHandler.updateClient = true;
    }
}
