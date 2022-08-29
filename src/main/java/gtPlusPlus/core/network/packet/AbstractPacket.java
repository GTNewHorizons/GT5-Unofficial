package gtPlusPlus.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public interface AbstractPacket extends IMessage {

    public abstract String getPacketName();
}
