package binnie.core.machines.storage;

import binnie.core.gui.BinnieCoreGUI;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.texture.BinnieCoreTexture;
import net.minecraft.tileentity.TileEntity;

class StandardCompartment
{
  public static class PackageCompartment
    extends Compartment.PackageCompartment
  {
    public PackageCompartment()
    {
      super(BinnieCoreTexture.Compartment);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 4, 25);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
  
  public static class PackageCompartmentCopper
    extends Compartment.PackageCompartment
  {
    public PackageCompartmentCopper()
    {
      super(BinnieCoreTexture.CompartmentCopper);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 6, 25);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
  
  public static class PackageCompartmentBronze
    extends Compartment.PackageCompartment
  {
    public PackageCompartmentBronze()
    {
      super(BinnieCoreTexture.CompartmentBronze);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 8, 25);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
  
  public static class PackageCompartmentIron
    extends Compartment.PackageCompartment
  {
    public PackageCompartmentIron()
    {
      super(BinnieCoreTexture.CompartmentIron);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 4, 50);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
  
  public static class PackageCompartmentGold
    extends Compartment.PackageCompartment
  {
    public PackageCompartmentGold()
    {
      super(BinnieCoreTexture.CompartmentGold);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 6, 50);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
  
  public static class PackageCompartmentDiamond
    extends Compartment.PackageCompartment
  {
    public PackageCompartmentDiamond()
    {
      super(BinnieCoreTexture.CompartmentDiamond);
    }
    
    public void createMachine(Machine machine)
    {
      new ComponentBinnieCoreGUI(machine, BinnieCoreGUI.Compartment);
      new ComponentCompartmentInventory(machine, 8, 50);
    }
    
    public TileEntity createTileEntity()
    {
      return new TileEntityMachine(this);
    }
    
    public void register() {}
  }
}
