package binnie.core.mod.parser;

import binnie.core.AbstractMod;
import cpw.mods.fml.common.registry.GameRegistry;
import java.lang.reflect.Field;
import net.minecraft.item.Item;

public class ItemParser
  extends FieldParser
{
  public boolean isHandled(Field field, AbstractMod mod)
  {
    return Item.class.isAssignableFrom(field.getType());
  }
  
  public void preInit(Field field, AbstractMod mod)
    throws IllegalArgumentException, IllegalAccessException
  {
    Item item = (Item)field.get(null);
    if (item != null) {
      GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
    }
  }
}
