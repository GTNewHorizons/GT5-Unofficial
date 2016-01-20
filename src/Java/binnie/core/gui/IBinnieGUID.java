package binnie.core.gui;

import binnie.core.network.IOrdinaled;
import binnie.craftgui.minecraft.Window;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract interface IBinnieGUID
  extends IOrdinaled
{
  public abstract Window getWindow(EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3, Side paramSide);
}
