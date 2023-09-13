package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GT_Mod;
import gregtech.api.util.GT_ClientPreference;
import io.netty.buffer.ByteBuf;

public class GT_Packet_ClientPreference extends GT_Packet_New {

    private GT_ClientPreference mPreference;
    private EntityPlayerMP mPlayer;

    public GT_Packet_ClientPreference() {
        super(true);
    }

    public GT_Packet_ClientPreference(GT_ClientPreference mPreference) {
        super(false);
        this.mPreference = mPreference;
    }

    @Override
    public byte getPacketID() {
        return 9;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mPlayer != null) GT_Mod.gregtechproxy.setClientPreference(mPlayer.getUniqueID(), mPreference);
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeBoolean(mPreference.isSingleBlockInitialFilterEnabled());
        aOut.writeBoolean(mPreference.isSingleBlockInitialMultiStackEnabled());
        aOut.writeBoolean(mPreference.isInputBusInitialFilterEnabled());
        aOut.writeBoolean(mPreference.isWailaAverageNSEnabled());
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_ClientPreference(
            new GT_ClientPreference(
                aData.readBoolean(),
                aData.readBoolean(),
                aData.readBoolean(),
                aData.readBoolean()));
    }
}
