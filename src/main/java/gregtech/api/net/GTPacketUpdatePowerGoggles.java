package gregtech.api.net;

import java.math.BigInteger;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.powergoggles.PowerGogglesMeasurement;
import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketUpdatePowerGoggles extends GTPacket {

    BigInteger EU;
    long lscCapacity;

    public GTPacketUpdatePowerGoggles() {}

    public GTPacketUpdatePowerGoggles(BigInteger EU, long lscCapacity) {
        this.EU = EU;
        this.lscCapacity = lscCapacity;
    }

    public GTPacketUpdatePowerGoggles(BigInteger EU) {
        this.EU = EU;
        this.lscCapacity = 0;
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
        buffer.writeLong(lscCapacity);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int length = buffer.readInt();
        byte[] eu = new byte[length];
        for (int i = 0; i < length; i++) {
            eu[i] = buffer.readByte();
        }
        BigInteger EU = new BigInteger(eu);
        long lscCapacity = buffer.readLong();
        return new GTPacketUpdatePowerGoggles(EU, lscCapacity);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        PowerGogglesHudHandler hudHandler = PowerGogglesHudHandler.getInstance();
        if (lscCapacity == 0) {
            hudHandler.getRenderer()
                .processMeasurement(new PowerGogglesMeasurement(true, this.EU));
        } else {
            hudHandler.getRenderer()
                .processMeasurement(new PowerGogglesMeasurement(false, this.EU, lscCapacity));
        }
    }
}
