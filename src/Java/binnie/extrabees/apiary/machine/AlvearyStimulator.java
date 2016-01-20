package binnie.extrabees.apiary.machine;

import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.circuits.BinnieCircuit;
import binnie.core.genetics.BeeModifierLogic;
import binnie.core.genetics.EnumBeeBooleanModifier;
import binnie.core.genetics.EnumBeeModifier;
import binnie.core.machines.Machine;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.inventory.ComponentInventorySlots;
import binnie.core.machines.inventory.InventorySlot;
import binnie.core.machines.inventory.SlotValidator;
import binnie.core.machines.power.ComponentPowerReceptor;
import binnie.craftgui.minecraft.IMachineInformation;
import binnie.extrabees.apiary.ComponentBeeModifier;
import binnie.extrabees.apiary.ComponentExtraBeeGUI;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.core.ExtraBeeTexture;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.circuits.ChipsetManager;
import forestry.api.circuits.ICircuit;
import forestry.api.circuits.ICircuitBoard;
import forestry.api.circuits.ICircuitLayout;
import forestry.api.circuits.ICircuitRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class AlvearyStimulator
{
  public static int slotCircuit = 0;
  
  public static class PackageAlvearyStimulator
    extends AlvearyMachine.AlvearyPackage
    implements IMachineInformation
  {
    public PackageAlvearyStimulator()
    {
      super(ExtraBeeTexture.AlvearyStimulator.getTexture(), true);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentExtraBeeGUI(machine, ExtraBeeGUID.AlvearyStimulator);
      
      ComponentInventorySlots inventory = new ComponentInventorySlots(machine);
      
      inventory.addSlot(AlvearyStimulator.slotCircuit, "circuit");
      inventory.getSlot(AlvearyStimulator.slotCircuit).setValidator(new AlvearyStimulator.SlotValidatorCircuit());
      
      ComponentPowerReceptor power = new ComponentPowerReceptor(machine);
      

      new AlvearyStimulator.ComponentStimulatorModifier(machine);
    }
  }
  
  public static class SlotValidatorCircuit
    extends SlotValidator
  {
    public SlotValidatorCircuit()
    {
      super();
    }
    
    public boolean isValid(ItemStack itemStack)
    {
      return (itemStack != null) && (ChipsetManager.circuitRegistry.isChipset(itemStack));
    }
    
    public String getTooltip()
    {
      return "Forestry Circuits";
    }
  }
  
  public static class ComponentStimulatorModifier
    extends ComponentBeeModifier
    implements IBeeModifier, IBeeListener
  {
    public ComponentStimulatorModifier(Machine machine)
    {
      super();
    }
    
    float powerUsage = 0.0F;
    boolean powered = false;
    
    public void onUpdate()
    {
      super.onUpdate();
      this.modifiers = getCircuits();
      this.powerUsage = 0.0F;
      for (AlvearyStimulator.StimulatorCircuit beeMod : this.modifiers) {
        this.powerUsage += beeMod.getPowerUsage();
      }
      this.powered = getUtil().hasEnergyMJ(this.powerUsage);
    }
    
    AlvearyStimulator.StimulatorCircuit[] modifiers = new AlvearyStimulator.StimulatorCircuit[0];
    
    public ICircuitBoard getHiveFrame()
    {
      if (!getUtil().isSlotEmpty(AlvearyStimulator.slotCircuit)) {
        return ChipsetManager.circuitRegistry.getCircuitboard(getUtil().getStack(AlvearyStimulator.slotCircuit));
      }
      return null;
    }
    
    public AlvearyStimulator.StimulatorCircuit[] getCircuits()
    {
      ICircuitBoard board = getHiveFrame();
      if (board == null) {
        return new AlvearyStimulator.StimulatorCircuit[0];
      }
      ICircuit[] circuits = board.getCircuits();
      List<IBeeModifier> mod = new ArrayList();
      for (ICircuit circuit : circuits) {
        if ((circuit instanceof AlvearyStimulator.StimulatorCircuit)) {
          mod.add((AlvearyStimulator.StimulatorCircuit)circuit);
        }
      }
      return (AlvearyStimulator.StimulatorCircuit[])mod.toArray(new AlvearyStimulator.StimulatorCircuit[0]);
    }
    
    public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getTerritoryModifier(genome, mod);
      }
      return mod;
    }
    
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getMutationModifier(genome, mate, mod);
      }
      return mod;
    }
    
    public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getLifespanModifier(genome, mate, mod);
      }
      return mod;
    }
    
    public float getProductionModifier(IBeeGenome genome, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getProductionModifier(genome, mod);
      }
      return mod;
    }
    
    public float getFloweringModifier(IBeeGenome genome, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getFloweringModifier(genome, mod);
      }
      return mod;
    }
    
    public float getGeneticDecay(IBeeGenome genome, float currentModifier)
    {
      float mod = 1.0F;
      if (!this.powered) {
        return mod;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        mod *= beeMod.getGeneticDecay(genome, mod);
      }
      return mod;
    }
    
    public boolean isSealed()
    {
      if (!this.powered) {
        return false;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        if (beeMod.isSealed()) {
          return true;
        }
      }
      return false;
    }
    
    public boolean isSelfLighted()
    {
      if (!this.powered) {
        return false;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        if (beeMod.isSelfLighted()) {
          return true;
        }
      }
      return false;
    }
    
    public boolean isSunlightSimulated()
    {
      if (!this.powered) {
        return false;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        if (beeMod.isSunlightSimulated()) {
          return true;
        }
      }
      return false;
    }
    
    public boolean isHellish()
    {
      if (!this.powered) {
        return false;
      }
      for (IBeeModifier beeMod : this.modifiers) {
        if (beeMod.isHellish()) {
          return true;
        }
      }
      return false;
    }
    
    public void wearOutEquipment(int amount)
    {
      getUtil().useEnergyMJ(this.powerUsage);
    }
  }
  
  public static class StimulatorCircuit
    extends BinnieCircuit
    implements IBeeModifier
  {
    AlvearyStimulator.CircuitType type;
    
    public StimulatorCircuit(AlvearyStimulator.CircuitType type, ICircuitLayout layout)
    {
      super(4, layout, Mods.Forestry.item("thermionicTubes"), type.recipe);
      this.type = type;
    }
    
    public int getPowerUsage()
    {
      return this.type.power;
    }
    
    public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
    {
      return this.type.getTerritoryModifier(genome, currentModifier);
    }
    
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return this.type.getMutationModifier(genome, mate, currentModifier);
    }
    
    public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return this.type.getLifespanModifier(genome, mate, currentModifier);
    }
    
    public float getProductionModifier(IBeeGenome genome, float currentModifier)
    {
      return this.type.getProductionModifier(genome, currentModifier);
    }
    
    public float getFloweringModifier(IBeeGenome genome, float currentModifier)
    {
      return this.type.getFloweringModifier(genome, currentModifier);
    }
    
    public boolean isSealed()
    {
      return this.type.isSealed();
    }
    
    public boolean isSelfLighted()
    {
      return this.type.isSelfLighted();
    }
    
    public boolean isSunlightSimulated()
    {
      return this.type.isSunlightSimulated();
    }
    
    public boolean isHellish()
    {
      return this.type.isHellish();
    }
    
    public float getGeneticDecay(IBeeGenome genome, float currentModifier)
    {
      return this.type.getGeneticDecay(genome, currentModifier);
    }
  }
  
  public static enum CircuitType
    implements IBeeModifier
  {
    LowVoltage(3, 10),  HighVoltage(5, 20),  Plant(10, 10),  Death(6, 10),  Life(11, 10),  Nether(7, 15),  Mutation(4, 15),  Inhibitor(1, 10),  Territory(2, 10);
    
    public int recipe;
    public int power;
    BeeModifierLogic logic = new BeeModifierLogic();
    
    static
    {
      LowVoltage.logic.setModifier(EnumBeeModifier.Production, 1.5F, 5.0F);
      HighVoltage.logic.setModifier(EnumBeeModifier.Production, 2.5F, 10.0F);
      Plant.logic.setModifier(EnumBeeModifier.Flowering, 1.5F, 5.0F);
      Death.logic.setModifier(EnumBeeModifier.Lifespan, 0.8F, 0.2F);
      Life.logic.setModifier(EnumBeeModifier.Lifespan, 1.5F, 5.0F);
      Nether.logic.setModifier(EnumBeeBooleanModifier.Hellish);
      Mutation.logic.setModifier(EnumBeeModifier.Mutation, 1.5F, 5.0F);
      Inhibitor.logic.setModifier(EnumBeeModifier.Territory, 0.4F, 0.1F);
      Inhibitor.logic.setModifier(EnumBeeModifier.Production, 0.9F, 0.5F);
      Territory.logic.setModifier(EnumBeeModifier.Territory, 1.5F, 5.0F);
      for (CircuitType type : values()) {
        type.logic.setModifier(EnumBeeModifier.GeneticDecay, 1.5F, 10.0F);
      }
    }
    
    private CircuitType(int recipe, int power)
    {
      this.recipe = recipe;
      this.power = power;
    }
    
    public void createCircuit(ICircuitLayout layout)
    {
      AlvearyStimulator.StimulatorCircuit circuit = new AlvearyStimulator.StimulatorCircuit(this, layout);
      for (EnumBeeModifier modifier : EnumBeeModifier.values())
      {
        float mod = this.logic.getModifier(modifier, 1.0F);
        if (mod != 1.0F) {
          if (mod > 1.0F)
          {
            int increase = (int)((mod - 1.0F) * 100.0F);
            circuit.addTooltipString("Increases " + modifier.getName() + " by " + increase + "%");
          }
          else
          {
            int decrease = (int)((1.0F - mod) * 100.0F);
            circuit.addTooltipString("Decreases " + modifier.getName() + " by " + decrease + "%");
          }
        }
      }
    }
    
    public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.Territory, currentModifier);
    }
    
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.Mutation, currentModifier);
    }
    
    public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.Lifespan, currentModifier);
    }
    
    public float getProductionModifier(IBeeGenome genome, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.Production, currentModifier);
    }
    
    public float getFloweringModifier(IBeeGenome genome, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.Flowering, currentModifier);
    }
    
    public float getGeneticDecay(IBeeGenome genome, float currentModifier)
    {
      return this.logic.getModifier(EnumBeeModifier.GeneticDecay, currentModifier);
    }
    
    public boolean isSealed()
    {
      return this.logic.getModifier(EnumBeeBooleanModifier.Sealed);
    }
    
    public boolean isSelfLighted()
    {
      return this.logic.getModifier(EnumBeeBooleanModifier.SelfLighted);
    }
    
    public boolean isSunlightSimulated()
    {
      return this.logic.getModifier(EnumBeeBooleanModifier.SunlightStimulated);
    }
    
    public boolean isHellish()
    {
      return this.logic.getModifier(EnumBeeBooleanModifier.Hellish);
    }
  }
}
