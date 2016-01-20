package binnie.core.network;

public abstract interface IPacketProvider
{
  public abstract String getChannel();
  
  public abstract IPacketID[] getPacketIDs();
}
