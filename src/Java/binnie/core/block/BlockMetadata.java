package binnie.core.block;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMetadata
  extends BlockContainer
  implements IBlockMetadata
{
  public BlockMetadata(Material material)
  {
    super(material);
  }
  
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int blockMeta, int fortune)
  {
    return getBlockDropped(this, world, x, y, z, blockMeta);
  }
  
  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
  {
    return breakBlock(this, player, world, x, y, z);
  }
  
  public TileEntity createNewTileEntity(World var1, int i)
  {
    return new TileEntityMetadata();
  }
  
  public boolean hasTileEntity(int meta)
  {
    return true;
  }
  
  public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
  {
    super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
    return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
  }
  
  public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
  {
    int metadata = TileEntityMetadata.getTileMetadata(par1IBlockAccess, par2, par3, par4);
    return getIcon(par5, metadata);
  }
  
  public String getBlockName(ItemStack par1ItemStack)
  {
    return getLocalizedName();
  }
  
  public void getBlockTooltip(ItemStack par1ItemStack, List par3List) {}
  
  public int getPlacedMeta(ItemStack item, World world, int x, int y, int z, ForgeDirection clickedBlock)
  {
    int damage = TileEntityMetadata.getItemDamage(item);
    return damage;
  }
  
  public int getDroppedMeta(int tileMeta, int blockMeta)
  {
    return tileMeta;
  }
  
  public static ArrayList<ItemStack> getBlockDropped(IBlockMetadata block, World world, int x, int y, int z, int blockMeta)
  {
    ArrayList<ItemStack> array = new ArrayList();
    TileEntityMetadata tile = TileEntityMetadata.getTile(world, x, y, z);
    if ((tile != null) && (!tile.hasDroppedBlock()))
    {
      int meta = block.getDroppedMeta(world.getBlockMetadata(x, y, z), tile.getTileMetadata());
      array.add(TileEntityMetadata.getItemStack((Block)block, meta));
    }
    return array;
  }
  
  static int temporyMeta = -1;
  
  public static boolean breakBlock(IBlockMetadata block, EntityPlayer player, World world, int i, int j, int k)
  {
    List<ItemStack> drops = new ArrayList();
    
    Block block2 = (Block)block;
    
    TileEntityMetadata tile = TileEntityMetadata.getTile(world, i, j, k);
    if ((tile != null) && (!tile.hasDroppedBlock()))
    {
      int tileMeta = TileEntityMetadata.getTileMetadata(world, i, j, k);
      drops = block2.getDrops(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
    }
    boolean hasBeenBroken = world.setBlockToAir(i, j, k);
    if ((hasBeenBroken) && (BinnieCore.proxy.isSimulating(world)) && (drops.size() > 0) && ((player == null) || (!player.capabilities.isCreativeMode)))
    {
      for (ItemStack drop : drops) {
        block.dropAsStack(world, i, j, k, drop);
      }
      tile.dropBlock();
    }
    return hasBeenBroken;
  }
  
  public void dropAsStack(World world, int x, int y, int z, ItemStack drop)
  {
    dropBlockAsItem(world, x, y, z, drop);
  }
  
  public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
  {
    super.breakBlock(par1World, par2, par3, par4, par5, par6);
    par1World.removeTileEntity(par2, par3, par4);
  }
  
  public static ItemStack getPickBlock(World world, int x, int y, int z)
  {
    List<ItemStack> list = getBlockDropped((IBlockMetadata)world.getBlock(x, y, z), world, x, y, z, world.getBlockMetadata(x, y, z));
    return list.isEmpty() ? null : (ItemStack)list.get(0);
  }
  
  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
  {
    return getPickBlock(world, x, y, z);
  }
}
