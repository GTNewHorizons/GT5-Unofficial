package binnie.core.genetics;

import com.mojang.authlib.GameProfile;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IErrorState;
import forestry.api.genetics.IHousing;
import forestry.core.EnumErrorCode;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

class VirtualHousing
  implements IHousing
{
  private EntityPlayer player;
  
  public VirtualHousing(EntityPlayer player)
  {
    this.player = player;
  }
  
  public int getXCoord()
  {
    return (int)this.player.posX;
  }
  
  public int getYCoord()
  {
    return (int)this.player.posY;
  }
  
  public int getZCoord()
  {
    return (int)this.player.posZ;
  }
  
  public int getBiomeId()
  {
    return this.player.worldObj.getBiomeGenForCoords(getXCoord(), getYCoord()).biomeID;
  }
  
  public EnumTemperature getTemperature()
  {
    return EnumTemperature.getFromValue(getBiome().temperature);
  }
  
  public EnumHumidity getHumidity()
  {
    return EnumHumidity.getFromValue(getBiome().rainfall);
  }
  
  public World getWorld()
  {
    return this.player.worldObj;
  }
  
  public void setErrorState(int state) {}
  
  public int getErrorOrdinal()
  {
    return 0;
  }
  
  public boolean addProduct(ItemStack product, boolean all)
  {
    return false;
  }
  
  public GameProfile getOwnerName()
  {
    return this.player.getGameProfile();
  }
  
  public BiomeGenBase getBiome()
  {
    return this.player.worldObj.getBiomeGenForCoords(getXCoord(), getZCoord());
  }
  
  public EnumErrorCode getErrorState()
  {
    return null;
  }
  
  public void setErrorState(IErrorState state) {}
  
  public boolean setErrorCondition(boolean condition, IErrorState errorState)
  {
    return false;
  }
  
  public Set<IErrorState> getErrorStates()
  {
    return null;
  }
}
