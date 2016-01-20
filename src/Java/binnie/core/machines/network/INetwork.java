package binnie.core.machines.network;

import cpw.mods.fml.relauncher.Side;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract interface INetwork
{
  public static abstract interface SendGuiNBT
  {
    public abstract void sendGuiNBT(Map<String, NBTTagCompound> paramMap);
  }
  
  public static abstract interface TilePacketSync
  {
    public abstract void syncToNBT(NBTTagCompound paramNBTTagCompound);
    
    public abstract void syncFromNBT(NBTTagCompound paramNBTTagCompound);
  }
  
  public static abstract interface RecieveGuiNBT
  {
    public abstract void recieveGuiNBT(Side paramSide, EntityPlayer paramEntityPlayer, String paramString, NBTTagCompound paramNBTTagCompound);
  }
  
  public static abstract interface GuiNBT
    extends INetwork.RecieveGuiNBT, INetwork.SendGuiNBT
  {}
}
