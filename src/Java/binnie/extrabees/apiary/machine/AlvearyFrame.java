package binnie.extrabees.apiary.machine;

import binnie.Binnie;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.machines.IMachine;
import binnie.core.machines.Machine;
import binnie.core.machines.inventory.ComponentInventorySlots;
import binnie.core.machines.inventory.InventorySlot;
import binnie.core.machines.inventory.SlotValidator;
import binnie.craftgui.minecraft.IMachineInformation;
import binnie.extrabees.apiary.ComponentBeeModifier;
import binnie.extrabees.apiary.ComponentExtraBeeGUI;
import binnie.extrabees.apiary.TileExtraBeeAlveary;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.core.ExtraBeeTexture;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.apiculture.IHiveFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AlvearyFrame
{
  public static int slotFrame = 0;
  
  public static class PackageAlvearyFrame
    extends AlvearyMachine.AlvearyPackage
    implements IMachineInformation
  {
    public PackageAlvearyFrame()
    {
      super(ExtraBeeTexture.AlvearyFrame.getTexture(), false);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentExtraBeeGUI(machine, ExtraBeeGUID.AlvearyFrame);
      
      ComponentInventorySlots inventory = new ComponentInventorySlots(machine);
      
      inventory.addSlot(AlvearyFrame.slotFrame, "frame");
      inventory.getSlot(AlvearyFrame.slotFrame).setValidator(new AlvearyFrame.SlotValidatorFrame());
      
      new AlvearyFrame.ComponentFrameModifier(machine);
    }
  }
  
  public static class SlotValidatorFrame
    extends SlotValidator
  {
    public SlotValidatorFrame()
    {
      super();
    }
    
    public boolean isValid(ItemStack itemStack)
    {
      return (itemStack != null) && ((itemStack.getItem() instanceof IHiveFrame));
    }
    
    public String getTooltip()
    {
      return "Hive Frames";
    }
  }
  
  public static class ComponentFrameModifier
    extends ComponentBeeModifier
    implements IBeeModifier, IBeeListener
  {
    public ComponentFrameModifier(Machine machine)
    {
      super();
    }
    
    public void wearOutEquipment(int amount)
    {
      if (getHiveFrame() == null) {
        return;
      }
      World world = getMachine().getTileEntity().getWorldObj();
      int wear = Math.round(amount * 5 * Binnie.Genetics.getBeeRoot().getBeekeepingMode(world).getWearModifier());
      getInventory().setInventorySlotContents(AlvearyFrame.slotFrame, getHiveFrame().frameUsed((IBeeHousing)((TileExtraBeeAlveary)getMachine().getTileEntity()).getCentralTE(), getInventory().getStackInSlot(AlvearyFrame.slotFrame), null, wear));
    }
    
    public IHiveFrame getHiveFrame()
    {
      if (getInventory().getStackInSlot(AlvearyFrame.slotFrame) != null) {
        return (IHiveFrame)getInventory().getStackInSlot(AlvearyFrame.slotFrame).getItem();
      }
      return null;
    }
    
    public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
    {
      return getHiveFrame() == null ? 1.0F : getHiveFrame().getTerritoryModifier(genome, currentModifier);
    }
    
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return getHiveFrame() == null ? 1.0F : getHiveFrame().getMutationModifier(genome, mate, currentModifier);
    }
    
    public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return getHiveFrame() == null ? 1.0F : getHiveFrame().getLifespanModifier(genome, mate, currentModifier);
    }
    
    public float getProductionModifier(IBeeGenome genome, float currentModifier)
    {
      return getHiveFrame() == null ? 1.0F : getHiveFrame().getProductionModifier(genome, currentModifier);
    }
    
    public float getFloweringModifier(IBeeGenome genome, float currentModifier)
    {
      return getHiveFrame() == null ? 1.0F : getHiveFrame().getFloweringModifier(genome, currentModifier);
    }
  }
}
