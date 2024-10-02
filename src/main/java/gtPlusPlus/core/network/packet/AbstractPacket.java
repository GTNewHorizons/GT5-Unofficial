package gtPlusPlus.core.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public interface AbstractPacket extends IMessage {

    String getPacketName();
}
