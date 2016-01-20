package binnie.craftgui.resource;

import java.util.HashMap;
import java.util.Map;

public class StyleSheet
  implements IStyleSheet
{
  protected Map<Object, Texture> textures = new HashMap();
  
  public Texture getTexture(Object key)
  {
    if (!this.textures.containsKey(key)) {
      return StyleSheetManager.getTexture(key);
    }
    return (Texture)this.textures.get(key);
  }
}
