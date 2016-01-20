package binnie.core.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeeModifierLogic
{
  private Map<EnumBeeModifier, Float[]> modifiers = new HashMap();
  private List<EnumBeeBooleanModifier> booleanModifiers = new ArrayList();
  
  public float getModifier(EnumBeeModifier modifier, float currentModifier)
  {
    if (!this.modifiers.containsKey(modifier)) {
      return 1.0F;
    }
    float mult = ((Float[])this.modifiers.get(modifier))[0].floatValue();
    float max = ((Float[])this.modifiers.get(modifier))[1].floatValue();
    if (max >= 1.0F)
    {
      if (max <= currentModifier) {
        return 1.0F;
      }
      return Math.min(max / currentModifier, mult);
    }
    if (max >= currentModifier) {
      return 1.0F;
    }
    return Math.max(max / currentModifier, mult);
  }
  
  public boolean getModifier(EnumBeeBooleanModifier modifier)
  {
    return this.booleanModifiers.contains(modifier);
  }
  
  public void setModifier(EnumBeeBooleanModifier modifier)
  {
    this.booleanModifiers.add(modifier);
  }
  
  public void setModifier(EnumBeeModifier modifier, float mult, float max)
  {
    this.modifiers.put(modifier, new Float[] { Float.valueOf(mult), Float.valueOf(max) });
  }
}
