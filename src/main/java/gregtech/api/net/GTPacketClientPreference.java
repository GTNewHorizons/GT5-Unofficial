package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.GTMod;
import gregtech.api.util.GTClientPreference;
import io.netty.buffer.ByteBuf;

public class GTPacketClientPreference extends GTPacket {

    private GTClientPreference mPreference;
    private EntityPlayerMP mPlayer;

    public GTPacketClientPreference() {
        super();
    }

    public GTPacketClientPreference(GTClientPreference mPreference) {
        super();
        this.mPreference = mPreference;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.CLIENT_PREFERENCE.id;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mPlayer != null) GTMod.proxy.setClientPreference(mPlayer.getUniqueID(), mPreference);
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeBoolean(mPreference.isSingleBlockInitialFilterEnabled());
        aOut.writeBoolean(mPreference.isSingleBlockInitialMultiStackEnabled());
        aOut.writeBoolean(mPreference.isInputBusInitialFilterEnabled());
        aOut.writeBoolean(mPreference.isWailaAverageNSEnabled());
        aOut.writeBoolean(mPreference.isInputHatchInitialFilterEnabled());
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketClientPreference(
            new GTClientPreference(
                aData.readBoolean(),
                aData.readBoolean(),
                aData.readBoolean(),
                aData.readBoolean(),
                aData.readBoolean()));
    }
}
