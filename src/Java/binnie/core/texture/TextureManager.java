package binnie.core.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;

@SideOnly(Side.CLIENT)
public class TextureManager
{
  static List<Icon> textures = new ArrayList();
  
  public static void init() {}
}
