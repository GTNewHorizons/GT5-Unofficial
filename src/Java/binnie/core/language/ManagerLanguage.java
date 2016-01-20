package binnie.core.language;

import binnie.core.AbstractMod;
import binnie.core.ManagerBase;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.StatCollector;

public class ManagerLanguage
  extends ManagerBase
{
  private Map<Object, String> objNames = new HashMap();
  
  public void addObjectName(Object obj, String name)
  {
    this.objNames.put(obj, name);
  }
  
  public String unlocalised(AbstractMod mod, String id)
  {
    return mod.getModID() + "." + id;
  }
  
  public String localise(Object key)
  {
    String loc = StatCollector.translateToLocal(key.toString());
    if (loc.equals(key.toString())) {
      return this.objNames.containsKey(key) ? localise(this.objNames.get(key)) : key.toString();
    }
    return loc;
  }
  
  public String localise(AbstractMod mod, String id)
  {
    return localise(unlocalised(mod, id));
  }
  
  public String localiseOrBlank(AbstractMod mod, String id)
  {
    return localiseOrBlank(unlocalised(mod, id));
  }
  
  public String localise(AbstractMod mod, String id, Object... objs)
  {
    return String.format(localise(mod, id), objs);
  }
  
  public String localiseOrBlank(Object key)
  {
    String trans = localise(key);
    return trans.equals(key) ? "" : trans;
  }
  
  public boolean canLocalise(Object key)
  {
    String trans = localise(key);
    return !trans.equals(key);
  }
}
