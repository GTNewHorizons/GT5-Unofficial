package binnie.core.liquid;

import binnie.core.BinnieCore;
import binnie.core.Mods;
import binnie.core.Mods.Mod;
import binnie.core.proxy.BinnieProxy;
import binnie.genetics.item.GeneticsItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

public enum FluidContainer
{
  Bucket,  Capsule,  Refractory,  Can,  Glass,  Cylinder;
  
  IIcon bottle;
  IIcon contents;
  ItemFluidContainer item;
  
  private FluidContainer() {}
  
  public int getMaxStackSize()
  {
    return this == Bucket ? 1 : 16;
  }
  
  @SideOnly(Side.CLIENT)
  public void updateIcons(IIconRegister register)
  {
    this.bottle = BinnieCore.proxy.getIcon(register, this == Cylinder ? "binniecore" : "forestry", "liquids/" + toString().toLowerCase() + ".bottle");
    
    this.contents = BinnieCore.proxy.getIcon(register, this == Cylinder ? "binniecore" : "forestry", "liquids/" + toString().toLowerCase() + ".contents");
  }
  
  public IIcon getBottleIcon()
  {
    return this.bottle;
  }
  
  public IIcon getContentsIcon()
  {
    return this.contents;
  }
  
  public String getName()
  {
    return BinnieCore.proxy.localise("item.container." + name().toLowerCase());
  }
  
  public boolean isActive()
  {
    return getEmpty() != null;
  }
  
  public ItemStack getEmpty()
  {
    switch (1.$SwitchMap$binnie$core$liquid$FluidContainer[ordinal()])
    {
    case 1: 
      return new ItemStack(Items.bucket, 1, 0);
    case 2: 
      return Mods.Forestry.stack("canEmpty");
    case 3: 
      return Mods.Forestry.stack("waxCapsule");
    case 4: 
      return new ItemStack(Items.glass_bottle, 1, 0);
    case 5: 
      return Mods.Forestry.stack("refractoryEmpty");
    case 6: 
      return GeneticsItems.Cylinder.get(1);
    }
    return null;
  }
  
  public void registerContainerData(IFluidType fluid)
  {
    if (!isActive()) {
      return;
    }
    ItemStack filled = this.item.getContainer(fluid);
    
    ItemStack empty = getEmpty();
    if ((filled == null) || (empty == null) || (fluid.get(1000) == null)) {
      return;
    }
    FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(fluid.get(1000), filled, empty);
    
    FluidContainerRegistry.registerFluidContainer(data);
  }
}
