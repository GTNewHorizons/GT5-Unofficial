package miscutil.xmod.ic2.block.kieticgenerator;

import ic2.core.block.BlockMultiID;
import ic2.core.block.kineticgenerator.tileentity.TileEntityManualKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemKineticGenerator;
import miscutil.core.creative.AddToCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.apache.commons.lang3.mutable.MutableObject;

import cpw.mods.fml.common.registry.GameRegistry;

public class IC2_BlockKineticGenerator
  extends BlockMultiID
{
  public IC2_BlockKineticGenerator(InternalName internalName1)
  {
    super(internalName1, Material.iron, ItemKineticGenerator.class);
    
    setHardness(3.0F);
    setStepSound(Block.soundTypeMetal);
    this.setCreativeTab(AddToCreativeTab.tabMachines);
    
    GameRegistry.registerTileEntity(TileEntityWindKineticGenerator.class, "Advanced Kinetic Wind Generator");
    
  }
  
  @Override
public String getTextureFolder(int id)
  {
    return "kineticgenerator";
  }
  
  @Override
public int damageDropped(int meta)
  {
    return meta;
  }
  
  @Override
public Class<? extends TileEntity> getTeClass(int meta, MutableObject<Class<?>[]> ctorArgTypes, MutableObject<Object[]> ctorArgs)
  {
    try
    {
      switch (meta)
      {
      case 0: 
        return TileEntityWindKineticGenerator.class;     
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    return null;
  }
  
  @Override
public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c)
  {
    if (entityPlayer.isSneaking()) {
      return false;
    }
    TileEntity te = getOwnTe(world, x, y, z);
    if ((te != null) && ((te instanceof TileEntityManualKineticGenerator))) {
      return ((TileEntityManualKineticGenerator)te).playerKlicked(entityPlayer);
    }
    return super.onBlockActivated(world, x, y, z, entityPlayer, side, a, b, c);
  }
}
