package binnie.core.proxy;

import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.network.BinnieCorePacketID;
import binnie.core.network.INetworkedEntity;
import binnie.core.network.packet.MessageUpdate;
import binnie.core.resource.BinnieResource;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

public class BinnieProxy
  extends BinnieModProxy
  implements IBinnieProxy
{
  public BinnieProxy()
  {
    super(BinnieCore.instance);
  }
  
  public void preInit() {}
  
  public void init() {}
  
  public void postInit() {}
  
  public void bindTexture(BinnieResource texture) {}
  
  public boolean checkTexture(BinnieResource location)
  {
    return false;
  }
  
  public int getUniqueRenderID()
  {
    return RenderingRegistry.getNextAvailableRenderId();
  }
  
  public void openGui(AbstractMod mod, int id, EntityPlayer player, int x, int y, int z)
  {
    player.openGui(mod, id, player.worldObj, x, y, z);
  }
  
  public boolean isSimulating(World world)
  {
    return true;
  }
  
  public void registerCustomItemRenderer(Item item, IItemRenderer itemRenderer) {}
  
  public boolean needsTagCompoundSynched(Item item)
  {
    return item.getShareTag();
  }
  
  public World getWorld()
  {
    return null;
  }
  
  public void throwException(String message, Throwable e)
  {
    FMLCommonHandler.instance().raiseException(e, message, true);
  }
  
  public Minecraft getMinecraftInstance()
  {
    return null;
  }
  
  public boolean isClient()
  {
    return false;
  }
  
  public boolean isServer()
  {
    return true;
  }
  
  public File getDirectory()
  {
    return new File("./");
  }
  
  public void registerTileEntity(Class<? extends TileEntity> tile, String id, Object renderer)
  {
    GameRegistry.registerTileEntity(tile, id);
  }
  
  public void createPipe(Item pipe) {}
  
  public boolean isDebug()
  {
    return System.getenv().containsKey("BINNIE_DEBUG");
  }
  
  public void registerBlockRenderer(Object renderer) {}
  
  public Object createObject(String renderer)
  {
    return null;
  }
  
  public void sendNetworkEntityPacket(INetworkedEntity entity)
  {
    MessageUpdate packet = new MessageUpdate(BinnieCorePacketID.NetworkEntityUpdate.ordinal(), entity);
    sendToAll(packet);
  }
  
  public IIcon getIcon(IIconRegister register, String mod, String name)
  {
    return null;
  }
  
  private short uniqueTextureUID = 1200;
  
  public void handleTextureRefresh(IIconRegister register, int type) {}
  
  public void handlePostTextureRefresh(IIconRegister register, int type) {}
  
  public short getUniqueTextureUID()
  {
    return this.uniqueTextureUID++;
  }
  
  public void bindTexture(ResourceLocation location) {}
  
  public boolean isShiftDown()
  {
    return false;
  }
  
  public EntityPlayer getPlayer()
  {
    return null;
  }
  
  public MinecraftServer getServer()
  {
    return MinecraftServer.getServer();
  }
}
