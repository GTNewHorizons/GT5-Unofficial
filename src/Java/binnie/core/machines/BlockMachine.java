package binnie.core.machines;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.machines.component.IRender.RandomDisplayTick;
import binnie.core.proxy.BinnieProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

class BlockMachine
  extends BlockContainer
  implements IBlockMachine
{
  private MachineGroup group;
  
  public BlockMachine(MachineGroup group, String blockName)
  {
    super(Material.iron);
    this.group = group;
    setHardness(1.5F);
    setBlockName(blockName);
  }
  
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List itemList)
  {
    for (MachinePackage pack : this.group.getPackages()) {
      if (pack.isActive()) {
        itemList.add(new ItemStack(this, 1, pack.getMetadata().intValue()));
      }
    }
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean renderAsNormalBlock()
  {
    return !this.group.customRenderer;
  }
  
  public int getRenderType()
  {
    return Binnie.Machine.getMachineRenderID();
  }
  
  public TileEntity createTileEntity(World world, int metadata)
  {
    if (this.group.getPackage(metadata) == null) {
      return null;
    }
    return this.group.getPackage(metadata).createTileEntity();
  }
  
  public MachinePackage getPackage(int meta)
  {
    return this.group.getPackage(meta);
  }
  
  public String getMachineName(int meta)
  {
    return getPackage(meta) == null ? "Unnamed Machine" : getPackage(meta).getDisplayName();
  }
  
  public int damageDropped(int par1)
  {
    return par1;
  }
  
  public TileEntity createNewTileEntity(World var1, int meta)
  {
    return createTileEntity(var1, meta);
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
  {
    if (!BinnieCore.proxy.isSimulating(world)) {
      return true;
    }
    if (player.isSneaking()) {
      return true;
    }
    TileEntity entity = world.getTileEntity(x, y, z);
    if ((entity instanceof TileEntityMachine)) {
      ((TileEntityMachine)entity).getMachine().onRightClick(world, player, x, y, z);
    }
    return true;
  }
  
  public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack)
  {
    super.onBlockPlacedBy(world, i, j, k, entityliving, stack);
    if (!BinnieCore.proxy.isSimulating(world)) {
      return;
    }
    IMachine machine = Machine.getMachine(world.getTileEntity(i, j, k));
    if (machine == null) {
      return;
    }
    if ((entityliving instanceof EntityPlayer)) {
      machine.setOwner(((EntityPlayer)entityliving).getGameProfile());
    }
  }
  
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
  {
    TileEntity entity = world.getTileEntity(x, y, z);
    if (((entity instanceof TileEntityMachine)) && (((TileEntityMachine)entity).getMachine().hasInterface(IMachineTexturedFaces.class))) {
      return ((IMachineTexturedFaces)((TileEntityMachine)entity).getMachine().getInterface(IMachineTexturedFaces.class)).getIcon(side);
    }
    return Blocks.dirt.getIcon(0, 0);
  }
  
  public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
  {
    TileEntity tileentity = world.getTileEntity(x, y, z);
    if (!(tileentity instanceof TileEntityMachine)) {
      return;
    }
    TileEntityMachine entity = (TileEntityMachine)tileentity;
    if (entity != null) {
      entity.onBlockDestroy();
    }
    super.breakBlock(world, x, y, z, par5, par6);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register) {}
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random rand)
  {
    IMachine machine = Machine.getMachine(world.getTileEntity(x, y, z));
    if (machine != null) {
      for (IRender.RandomDisplayTick renders : machine.getInterfaces(IRender.RandomDisplayTick.class)) {
        renders.onRandomDisplayTick(world, x, y, z, rand);
      }
    }
  }
  
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
  {
    return new ArrayList();
  }
  
  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
  {
    if ((BinnieCore.proxy.isSimulating(world)) && (canHarvestBlock(player, world.getBlockMetadata(x, y, z))) && 
      (!player.capabilities.isCreativeMode))
    {
      int metadata = world.getBlockMetadata(x, y, z);
      ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(metadata));
      dropBlockAsItem(world, x, y, z, stack);
    }
    return world.setBlockToAir(x, y, z);
  }
  
  public static abstract interface IMachineTexturedFaces
  {
    public abstract IIcon getIcon(int paramInt);
  }
}
