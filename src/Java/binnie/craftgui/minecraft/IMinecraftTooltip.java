package binnie.craftgui.minecraft;

import binnie.craftgui.core.ITooltip;

public abstract interface IMinecraftTooltip
  extends ITooltip
{
  public abstract void getTooltip(MinecraftTooltip paramMinecraftTooltip);
}
