package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.common.GT_Pollution;
import net.minecraft.world.IBlockAccess;

public class GT_Packet_Pollution extends GT_Packet {
    private int mPollution;

    public GT_Packet_Pollution() {
        super(true);
    }

    public GT_Packet_Pollution(int aPollution) {
        super(false);
        mPollution = aPollution;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(4);
        tOut.writeInt(mPollution);
        return tOut.toByteArray();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new GT_Packet_Pollution(aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
    	GT_Pollution.mPlayerPollution = mPollution;
    }

    @Override
    public byte getPacketID() {
        return 4;
    }
}