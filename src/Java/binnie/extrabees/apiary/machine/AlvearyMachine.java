package binnie.extrabees.apiary.machine;

import binnie.core.machines.IMachineType;
import binnie.core.machines.Machine;
import binnie.core.machines.MachinePackage;
import binnie.core.machines.MachineRendererBlock;
import binnie.core.resource.BinnieResource;
import binnie.extrabees.apiary.ModuleApiary;
import binnie.extrabees.apiary.TileExtraBeeAlveary;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public enum AlvearyMachine
  implements IMachineType
{
  Mutator(AlvearyMutator.PackageAlvearyMutator.class),  Frame(AlvearyFrame.PackageAlvearyFrame.class),  RainShield(AlvearyRainShield.PackageAlvearyRainShield.class),  Lighting(AlvearyLighting.PackageAlvearyLighting.class),  Stimulator(AlvearyStimulator.PackageAlvearyStimulator.class),  Hatchery(AlvearyHatchery.PackageAlvearyHatchery.class),  Transmission(AlvearyTransmission.PackageAlvearyTransmission.class);
  
  Class<? extends MachinePackage> clss;
  
  private AlvearyMachine(Class<? extends MachinePackage> clss)
  {
    this.clss = clss;
  }
  
  public Class<? extends MachinePackage> getPackageClass()
  {
    return this.clss;
  }
  
  public ItemStack get(int size)
  {
    return new ItemStack(ModuleApiary.blockComponent, size, ordinal());
  }
  
  public static abstract class AlvearyPackage
    extends MachinePackage
  {
    BinnieResource machineTexture;
    
    public AlvearyPackage(String id, BinnieResource machineTexture, boolean powered)
    {
      super(powered);
      this.machineTexture = machineTexture;
    }
    
    public void createMachine(Machine machine) {}
    
    public TileEntity createTileEntity()
    {
      return new TileExtraBeeAlveary(this);
    }
    
    public void register() {}
    
    public void renderMachine(Machine machine, double x, double y, double z, float var8, RenderBlocks renderer)
    {
      MachineRendererBlock.instance.renderMachine(this.machineTexture, x, y, z, var8);
    }
  }
  
  public boolean isActive()
  {
    return true;
  }
}
