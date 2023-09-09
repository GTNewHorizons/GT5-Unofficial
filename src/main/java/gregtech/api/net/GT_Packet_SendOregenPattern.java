package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GT_Log;
import gregtech.common.GT_Worldgenerator;
import gregtech.common.GT_Worldgenerator.OregenPattern;
import io.netty.buffer.ByteBuf;

public class GT_Packet_SendOregenPattern extends GT_Packet_New {

    protected OregenPattern pattern = OregenPattern.AXISSYMMETRICAL;

    public GT_Packet_SendOregenPattern() {
        super(true);
    }

    public GT_Packet_SendOregenPattern(OregenPattern pattern) {
        super(false);
        this.pattern = pattern;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.pattern.ordinal());
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        int ordinal = aData.readInt();
        // make sure we get valid data:
        if (ordinal >= 0 && ordinal < OregenPattern.values().length) {
            return new GT_Packet_SendOregenPattern(OregenPattern.values()[ordinal]);
        }
        // invalid data, default to AXISSYMMETRICAL:
        GT_Log.err.println(
            String.format(
                "Received invalid data! Received %d but value must be between 0 and %d! Default (0) will be used.",
                ordinal,
                OregenPattern.values().length - 1));
        return new GT_Packet_SendOregenPattern();
    }

    @Override
    public byte getPacketID() {
        return 19;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        GT_Worldgenerator.oregenPattern = this.pattern;
    }

}
