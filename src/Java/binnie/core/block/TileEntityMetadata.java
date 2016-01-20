package binnie.core.block;

import binnie.core.BinnieCore;
import binnie.core.network.packet.MessageMetadata;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityMetadata
  extends TileEntity
{
  private int meta;
  
  public boolean receiveClientEvent(int par1, int par2)
  {
    if (par1 == 42)
    {
      this.meta = par2;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    return true;
  }
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    super.readFromNBT(nbt);
    this.meta = nbt.getInteger("meta");
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    super.writeToNBT(nbt);
    nbt.setInteger("meta", this.meta);
  }
  
  public boolean canUpdate()
  {
    return false;
  }
  
  public int getTileMetadata()
  {
    return this.meta;
  }
  
  public void setTileMetadata(int meta, boolean notify)
  {
    if (this.meta != meta)
    {
      this.meta = meta;
      if (notify) {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
    }
  }
  
  public Packet getDescriptionPacket()
  {
    return BinnieCore.instance.getNetworkWrapper().getPacketFrom(new MessageMetadata(this.xCoord, this.yCoord, this.zCoord, this.meta).GetMessage());
  }
  
  public static TileEntityMetadata getTile(IBlockAccess world, int x, int y, int z)
  {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (!(tile instanceof TileEntityMetadata)) {
      return null;
    }
    return (TileEntityMetadata)tile;
  }
  
  public static ItemStack getItemStack(Block block, int damage)
  {
    ItemStack item = new ItemStack(block, 1, 0);
    setItemDamage(item, damage);
    return item;
  }
  
  public static void setItemDamage(ItemStack item, int i)
  {
    item.setItemDamage(i < 16387 ? i : 16387);
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("meta", i);
    item.setTagCompound(tag);
  }
  
  public static int getItemDamage(ItemStack item)
  {
    if ((item.hasTagCompound()) && (item.getTagCompound().hasKey("meta"))) {
      return item.getTagCompound().getInteger("meta");
    }
    return item.getItemDamage();
  }
  
  public static int getTileMetadata(IBlockAccess world, int x, int y, int z)
  {
    TileEntityMetadata tile = getTile(world, x, y, z);
    return tile == null ? 0 : tile.getTileMetadata();
  }
  
  private boolean droppedBlock = false;
  
  public boolean hasDroppedBlock()
  {
    return this.droppedBlock;
  }
  
  public void dropBlock()
  {
    this.droppedBlock = true;
  }
}
