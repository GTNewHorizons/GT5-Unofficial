package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.data.maglev.Tether;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import io.netty.buffer.ByteBuf;

public class GTPacketTether extends GTPacket {

    private Tether tether;

    public GTPacketTether() {
        super();
    }

    public GTPacketTether(MTEMagLevPylon pylon) {
        tether = pylon.machineTether;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TETHER.id;
    }

    @Override
    public void encode(ByteBuf buffer) {

    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        return null;
    }

    @Override
    public void process(IBlockAccess world) {

    }
}
