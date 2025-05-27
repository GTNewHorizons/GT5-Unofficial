package gregtech.api.net;

import net.minecraft.client.Minecraft;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;
import io.netty.buffer.ByteBuf;

public class GTPacketTether extends GTPacket {

    private Tether tether;

    public GTPacketTether() {
        super();
    }

    public GTPacketTether(Tether tether) {
        super();
        this.tether = tether;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TETHER.id;
    }

    @Override
    public void encode(ByteBuf buffer) {

        buffer.writeBoolean(tether == null); // hasTether
        if (tether == null) return;
        buffer.writeByte(0x1); // hasTether
        buffer.writeInt(tether.sourceX());
        buffer.writeInt(tether.sourceY());
        buffer.writeInt(tether.sourceZ());
        buffer.writeInt(tether.dimID());
        buffer.writeInt(tether.range());
        buffer.writeByte(tether.tier());

    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        if (buffer.readBoolean()) {
            return new GTPacketTether(null);
        }
        Tether newTether = new Tether(
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readByte());
        return new GTPacketTether(newTether);
    }

    @Override
    public void process(IBlockAccess world) {
        TetherManager.PLAYER_TETHERS.replace(Minecraft.getMinecraft().thePlayer, tether);
    }
}
