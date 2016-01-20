package binnie.core.network;

import binnie.core.network.packet.PacketPayload;

public abstract interface INetworkedEntity
{
  public abstract void writeToPacket(PacketPayload paramPacketPayload);
  
  public abstract void readFromPacket(PacketPayload paramPacketPayload);
}
