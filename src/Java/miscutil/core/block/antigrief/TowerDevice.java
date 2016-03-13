package miscutil.core.block.antigrief;

import static miscutil.core.block.ModBlocks.blockGriefSaver;

import java.util.List;
import java.util.Random;

import miscutil.core.creativetabs.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.tileentities.TileEntityReverter;
import miscutil.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TowerDevice extends Block {
  private static IIcon TEX_ANTIBUILDER;
  public static final int META_ANTIBUILDER = 9;
  public TowerDevice()
  {
    super(Material.wood);
    setHardness(10.0F);
    setResistance(35.0F);
    setStepSound(Block.soundTypeWood);
    setCreativeTab(AddToCreativeTab.tabMachines);
  }
  
  public int tickRate()
  {
    return 15;
  }
  
  public IIcon getIcon(int side, int meta)
  {
      return TEX_ANTIBUILDER;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IconRegister)
  {
    TEX_ANTIBUILDER = par1IconRegister.registerIcon(CORE.MODID + ":" + "blockAntiGrief");
  }
  
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
  {
    par3List.add(new ItemStack(par1, 1, 9));
  }
  
  public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
  {
    int meta = par1World.getBlockMetadata(x, y, z);
    return false;
  }
  
  public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
  {
    int meta = world.getBlockMetadata(x, y, z);
    return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
  }
  
  public float getBlockHardness(World world, int x, int y, int z)
  {
    int meta = world.getBlockMetadata(x, y, z);    
    return super.getBlockHardness(world, x, y, z);
  }
  
  public static boolean areNearbyLockBlocks(World world, int x, int y, int z)
  {
    boolean locked = false;
    for (int dx = x - 2; dx <= x + 2; dx++) {
      for (int dy = y - 2; dy <= y + 2; dy++) {
        for (int dz = z - 2; dz <= z + 2; dz++) {
          if ((world.getBlock(dx, dy, dz) == blockGriefSaver) && (world.getBlockMetadata(dx, dy, dz) == 4)) {
            locked = true;
          }
        }
      }
    }
    return locked;
  }
  
  public static void unlockBlock(World par1World, int x, int y, int z)
  {
    Block thereBlockID = par1World.getBlock(x, y, z);
    int thereBlockMeta = par1World.getBlockMetadata(x, y, z);
    if ((thereBlockID == blockGriefSaver) || (thereBlockMeta == 4))
    {
      changeToBlockMeta(par1World, x, y, z, 5);
      par1World.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.click", 0.3F, 0.6F);
    }
  }
  
  private static void changeToBlockMeta(World par1World, int x, int y, int z, int meta)
  {
    Block thereBlockID = par1World.getBlock(x, y, z);
    if ((thereBlockID == blockGriefSaver))
    {
      par1World.setBlock(x, y, z, thereBlockID, meta, 3);
      par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
      par1World.notifyBlocksOfNeighborChange(x, y, z, thereBlockID);
    }
  }
  
  public void onBlockAdded(World par1World, int x, int y, int z)
  {
    int meta = par1World.getBlockMetadata(x, y, z);
    if (!par1World.isRemote) {
    	
    }
  }
  
  public void onNeighborBlockChange(World par1World, int x, int y, int z, Block myBlockID)
  {
    int meta = par1World.getBlockMetadata(x, y, z);
    if (!par1World.isRemote)
    {
      
    }
  }
  
  public void updateTick(World par1World, int x, int y, int z, Random par5Random)
  {
    if (!par1World.isRemote)
    {
      int meta = par1World.getBlockMetadata(x, y, z);
    }
  }
  
  private void letsBuild(World par1World, int x, int y, int z)
  {
	  
  }
  
  private boolean isInactiveTrapCharged(World par1World, int x, int y, int z)
  {
    return false;
  }
  
  private boolean isReactorReady(World world, int x, int y, int z)
  {
    if ((world.getBlock(x, y + 1, z) != Blocks.redstone_block) || 
      (world.getBlock(x, y - 1, z) != Blocks.redstone_block) || 
      (world.getBlock(x + 1, y, z) != Blocks.redstone_block) || 
      (world.getBlock(x - 1, y, z) != Blocks.redstone_block) || 
      (world.getBlock(x, y, z + 1) != Blocks.redstone_block) || 
      (world.getBlock(x, y, z - 1) != Blocks.redstone_block)) {
      return false;
    }
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
  {
    int meta = par1World.getBlockMetadata(x, y, z);
    if ((meta == 3) || (meta == 1) || (meta == 9)) {
      for (int i = 0; i < 1; i++) {
        sparkle(par1World, x, y, z, par5Random);
      }
    }
  }
  
  public void sparkle(World world, int x, int y, int z, Random rand)
  {
    double offset = 0.0625D;
    for (int side = 0; side < 6; side++)
    {
      double rx = x + rand.nextFloat();
      double ry = y + rand.nextFloat();
      double rz = z + rand.nextFloat();
      if ((side == 0) && (!world.getBlock(x, y + 1, z).isOpaqueCube())) {
        ry = y + 1 + offset;
      }
      if ((side == 1) && (!world.getBlock(x, y - 1, z).isOpaqueCube())) {
        ry = y + 0 - offset;
      }
      if ((side == 2) && (!world.getBlock(x, y, z + 1).isOpaqueCube())) {
        rz = z + 1 + offset;
      }
      if ((side == 3) && (!world.getBlock(x, y, z - 1).isOpaqueCube())) {
        rz = z + 0 - offset;
      }
      if ((side == 4) && (!world.getBlock(x + 1, y, z).isOpaqueCube())) {
        rx = x + 1 + offset;
      }
      if ((side == 5) && (!world.getBlock(x - 1, y, z).isOpaqueCube())) {
        rx = x + 0 - offset;
      }
      if ((rx < x) || (rx > x + 1) || (ry < 0.0D) || (ry > y + 1) || (rz < z) || (rz > z + 1)) {
        world.spawnParticle("reddust", rx, ry, rz, 0.0D, 0.0D, 0.0D);
      }
    }
  }
  
  public static void checkAndActivateVanishBlock(World world, int x, int y, int z)
  {
    Block thereID = world.getBlock(x, y, z);
    int thereMeta = world.getBlockMetadata(x, y, z);
  }
  
  public static void changeToActiveVanishBlock(World par1World, int x, int y, int z, int meta)
  {
    changeToBlockMeta(par1World, x, y, z, meta);
    par1World.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.pop", 0.3F, 0.6F);
    
    Block thereBlockID = par1World.getBlock(x, y, z);
    par1World.scheduleBlockUpdate(x, y, z, thereBlockID, getTickRateFor(thereBlockID, meta, par1World.rand));
  }
  
  private static int getTickRateFor(Block thereBlockID, int meta, Random rand)
  {
    return 15;
  }
  
  public int getLightValue(IBlockAccess world, int x, int y, int z)
  {
    Block blockID = world.getBlock(x, y, z);
    int meta = world.getBlockMetadata(x, y, z);
    if (blockID != this) {
      return 0;
    }
      return 10;
  }
  
  public boolean hasTileEntity(int metadata)
  {
    return (metadata == 0);
  }
  
  public TileEntity createTileEntity(World world, int metadata)
  {
    if (metadata == 0) {
    	Utils.LOG_INFO("I have been created. [Antigriefer]"+this.getLocalizedName());
      return new TileEntityReverter();
    }
    return null;
  }
  
  public Item getItemDropped(int meta, Random par2Random, int par3)
  {
    switch (meta)
    {
    case 0: 
      return null;
    }
    return Item.getItemFromBlock(this);
  }
  
  public int damageDropped(int meta)
  {
    return meta;
  }
}
