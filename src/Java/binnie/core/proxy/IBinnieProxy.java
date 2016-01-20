package binnie.core.proxy;

import binnie.core.AbstractMod;
import binnie.core.resource.BinnieResource;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

public abstract interface IBinnieProxy
  extends IProxyCore
{
  public abstract boolean isClient();
  
  public abstract boolean isServer();
  
  public abstract File getDirectory();
  
  public abstract void bindTexture(BinnieResource paramBinnieResource);
  
  public abstract void bindTexture(ResourceLocation paramResourceLocation);
  
  public abstract int getUniqueRenderID();
  
  public abstract void registerCustomItemRenderer(Item paramItem, IItemRenderer paramIItemRenderer);
  
  public abstract void openGui(AbstractMod paramAbstractMod, int paramInt1, EntityPlayer paramEntityPlayer, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract boolean isSimulating(World paramWorld);
  
  public abstract World getWorld();
  
  public abstract Minecraft getMinecraftInstance();
  
  public abstract boolean needsTagCompoundSynched(Item paramItem);
  
  public abstract Object createObject(String paramString);
  
  public abstract void registerTileEntity(Class<? extends TileEntity> paramClass, String paramString, Object paramObject);
  
  public abstract void createPipe(Item paramItem);
  
  public abstract boolean isDebug();
  
  public abstract void registerBlockRenderer(Object paramObject);
  
  public abstract IIcon getIcon(IIconRegister paramIIconRegister, String paramString1, String paramString2);
}
