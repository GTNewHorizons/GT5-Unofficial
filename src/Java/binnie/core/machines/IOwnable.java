package binnie.core.machines;

import com.mojang.authlib.GameProfile;

abstract interface IOwnable
{
  public abstract GameProfile getOwner();
  
  public abstract void setOwner(GameProfile paramGameProfile);
}
