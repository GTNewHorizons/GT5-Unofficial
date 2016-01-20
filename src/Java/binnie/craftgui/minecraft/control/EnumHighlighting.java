package binnie.craftgui.minecraft.control;

import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.minecraft.MinecraftTooltip;
import binnie.craftgui.minecraft.MinecraftTooltip.Type;

public enum EnumHighlighting
{
  Error,  Warning,  Help,  ShiftClick;
  
  private EnumHighlighting() {}
  
  int getColour()
  {
    switch (1.$SwitchMap$binnie$craftgui$minecraft$control$EnumHighlighting[ordinal()])
    {
    case 1: 
      return MinecraftTooltip.getOutline(MinecraftTooltip.Type.Error);
    case 2: 
      return MinecraftTooltip.getOutline(Tooltip.Type.Help);
    case 3: 
      return 16776960;
    case 4: 
      return MinecraftTooltip.getOutline(MinecraftTooltip.Type.Warning);
    }
    return 0;
  }
}
