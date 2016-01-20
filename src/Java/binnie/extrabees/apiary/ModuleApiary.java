package binnie.extrabees.apiary;

import binnie.core.BinnieCore;
import binnie.core.IInitializable;
import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.circuits.BinnieCircuitLayout;
import binnie.core.machines.MachineGroup;
import binnie.core.proxy.BinnieProxy;
import binnie.extrabees.ExtraBees;
import binnie.extrabees.apiary.machine.AlvearyMachine;
import binnie.extrabees.apiary.machine.AlvearyMutator;
import binnie.extrabees.apiary.machine.AlvearyStimulator.CircuitType;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.core.Tabs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModuleApiary
  implements IInitializable
{
  public static Block blockComponent;
  BinnieCircuitLayout stimulatorLayout;
  
  public void preInit()
  {
    MachineGroup machineGroup = new MachineGroup(ExtraBees.instance, "alveay", "alveary", AlvearyMachine.values());
    machineGroup.setCreativeTab(Tabs.tabApiculture);
    
    BinnieCore.proxy.registerTileEntity(TileExtraBeeAlveary.class, "extrabees.tile.alveary", BinnieCore.proxy.createObject("binnie.core.machines.RendererMachine"));
    




    blockComponent = machineGroup.getBlock();
    
    AlvearyMutator.addMutationItem(new ItemStack(Blocks.soul_sand), 1.5F);
    
    AlvearyMutator.addMutationItem(Mods.IC2.stack("UranFuel"), 4.0F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("MOXFuel"), 10.0F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("Plutonium"), 8.0F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("smallPlutonium"), 5.0F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("Uran235"), 4.0F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("smallUran235"), 2.5F);
    AlvearyMutator.addMutationItem(Mods.IC2.stack("Uran238"), 2.0F);
    
    AlvearyMutator.addMutationItem(new ItemStack(Items.ender_pearl), 2.0F);
    AlvearyMutator.addMutationItem(new ItemStack(Items.ender_eye), 4.0F);
    for (EnumHiveFrame frame : EnumHiveFrame.values())
    {
      frame.item = new ItemHiveFrame(frame);
      GameRegistry.registerItem(frame.item, "hiveFrame." + frame.name().toLowerCase());
    }
  }
  
  public void postInit()
  {
    EnumHiveFrame.init();
    
    GameRegistry.addRecipe(AlvearyMachine.Mutator.get(1), new Object[] { "g g", " a ", "t t", Character.valueOf('g'), Items.gold_ingot, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 5) });
    


    GameRegistry.addRecipe(AlvearyMachine.Frame.get(1), new Object[] { "iii", "tat", " t ", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 4) });
    


    GameRegistry.addRecipe(AlvearyMachine.RainShield.get(1), new Object[] { " b ", "bab", "t t", Character.valueOf('b'), Items.brick, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 4) });
    



    GameRegistry.addRecipe(AlvearyMachine.Lighting.get(1), new Object[] { "iii", "iai", " t ", Character.valueOf('i'), Items.glowstone_dust, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 4) });
    

    GameRegistry.addRecipe(AlvearyMachine.Stimulator.get(1), new Object[] { "kik", "iai", " t ", Character.valueOf('i'), Items.gold_nugget, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 4), Character.valueOf('k'), new ItemStack(Mods.Forestry.item("chipsets"), 1, 2) });
    

    GameRegistry.addRecipe(AlvearyMachine.Hatchery.get(1), new Object[] { "i i", " a ", "iti", Character.valueOf('i'), Blocks.glass_pane, Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, 5) });
    

    GameRegistry.addRecipe(new ShapedOreRecipe(AlvearyMachine.Transmission.get(1), new Object[] { " t ", "tat", " t ", Character.valueOf('a'), Mods.Forestry.block("alveary"), Character.valueOf('t'), "gearTin" }));
    for (AlvearyStimulator.CircuitType type : AlvearyStimulator.CircuitType.values()) {
      type.createCircuit(this.stimulatorLayout);
    }
  }
  
  public void init()
  {
    this.stimulatorLayout = new BinnieCircuitLayout(ExtraBees.instance, "Stimulator");
  }
}
