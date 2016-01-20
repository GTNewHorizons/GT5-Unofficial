package binnie.extrabees.apiary.machine;

import binnie.core.machines.Machine;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.inventory.ComponentInventorySlots;
import binnie.core.machines.inventory.InventorySlot;
import binnie.core.machines.inventory.SlotValidator;
import binnie.core.machines.inventory.ValidatorIcon;
import binnie.craftgui.minecraft.IMachineInformation;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.apiary.ComponentBeeModifier;
import binnie.extrabees.apiary.ComponentExtraBeeGUI;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.core.ExtraBeeTexture;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;

public class AlvearyMutator
{
  public static int slotMutator = 0;
  
  public static class PackageAlvearyMutator
    extends AlvearyMachine.AlvearyPackage
    implements IMachineInformation
  {
    public PackageAlvearyMutator()
    {
      super(ExtraBeeTexture.AlvearyMutator.getTexture(), false);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentExtraBeeGUI(machine, ExtraBeeGUID.AlvearyMutator);
      
      ComponentInventorySlots inventory = new ComponentInventorySlots(machine);
      
      inventory.addSlot(AlvearyMutator.slotMutator, "mutator");
      inventory.getSlot(AlvearyMutator.slotMutator).setValidator(new AlvearyMutator.SlotValidatorMutator());
      
      new AlvearyMutator.ComponentMutatorModifier(machine);
    }
  }
  
  public static class SlotValidatorMutator
    extends SlotValidator
  {
    public SlotValidatorMutator()
    {
      super();
    }
    
    public boolean isValid(ItemStack itemStack)
    {
      return AlvearyMutator.isMutationItem(itemStack);
    }
    
    public String getTooltip()
    {
      return "Mutagenic Agents";
    }
  }
  
  public static class ComponentMutatorModifier
    extends ComponentBeeModifier
    implements IBeeModifier, IBeeListener
  {
    public ComponentMutatorModifier(Machine machine)
    {
      super();
    }
    
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      if (getUtil().isSlotEmpty(AlvearyMutator.slotMutator)) {
        return 1.0F;
      }
      float mult = AlvearyMutator.getMutationMult(getUtil().getStack(AlvearyMutator.slotMutator));
      return Math.min(mult, 15.0F / currentModifier);
    }
    
    public void onPostQueenDeath(IBee queen)
    {
      getUtil().decreaseStack(AlvearyMutator.slotMutator, 1);
    }
  }
  
  static Map<ItemStack, Float> mutations = new HashMap();
  
  public static boolean isMutationItem(ItemStack item)
  {
    return getMutationMult(item) > 0.0F;
  }
  
  public static float getMutationMult(ItemStack item)
  {
    if (item == null) {
      return 1.0F;
    }
    for (ItemStack comp : mutations.keySet()) {
      if ((ItemStack.areItemStackTagsEqual(item, comp)) && (item.isItemEqual(comp))) {
        return ((Float)mutations.get(comp)).floatValue();
      }
    }
    return 1.0F;
  }
  
  public static void addMutationItem(ItemStack item, float chance)
  {
    if (item == null) {
      return;
    }
    mutations.put(item, Float.valueOf(chance));
  }
  
  public static Collection<ItemStack> getMutagens()
  {
    return mutations.keySet();
  }
}
