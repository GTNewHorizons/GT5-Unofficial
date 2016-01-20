package binnie.core.proxy;

import binnie.Binnie;
import binnie.core.liquid.ManagerLiquid;
import binnie.core.resource.BinnieResource;
import binnie.craftgui.resource.minecraft.CraftGUIResourceManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class BinnieProxyClient
  extends BinnieProxy
  implements IBinnieProxy
{
  public void bindTexture(BinnieResource texture)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindTexture(texture.getResourceLocation());
  }
  
  public void bindTexture(ResourceLocation location)
  {
    getMinecraftInstance().getTextureManager().bindTexture(location);
  }
  
  public boolean checkTexture(BinnieResource location)
  {
    SimpleTexture texture = new SimpleTexture(location.getResourceLocation());
    try
    {
      texture.loadTexture(getMinecraftInstance().getResourceManager());
    }
    catch (IOException e)
    {
      return false;
    }
    return true;
  }
  
  public boolean isSimulating(World world)
  {
    return !world.isRemote;
  }
  
  public void registerCustomItemRenderer(Item item, IItemRenderer itemRenderer)
  {
    MinecraftForgeClient.registerItemRenderer(item, itemRenderer);
  }
  
  public World getWorld()
  {
    return getMinecraftInstance().theWorld;
  }
  
  public Minecraft getMinecraftInstance()
  {
    return FMLClientHandler.instance().getClient();
  }
  
  public boolean isClient()
  {
    return true;
  }
  
  public boolean isServer()
  {
    return false;
  }
  
  public File getDirectory()
  {
    return new File(".");
  }
  
  public void registerTileEntity(Class<? extends TileEntity> tile, String id, Object renderer)
  {
    if ((renderer != null) && ((renderer instanceof TileEntitySpecialRenderer))) {
      ClientRegistry.registerTileEntity(tile, id, (TileEntitySpecialRenderer)renderer);
    } else {
      GameRegistry.registerTileEntity(tile, id);
    }
  }
  
  public void registerBlockRenderer(Object renderer)
  {
    if ((renderer != null) && ((renderer instanceof ISimpleBlockRenderingHandler))) {
      RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)renderer);
    }
  }
  
  public void createPipe(Item pipe) {}
  
  public Object createObject(String renderer)
  {
    Object object = null;
    try
    {
      Class<?> rendererClass = Class.forName(renderer);
      if (rendererClass != null) {
        object = rendererClass.newInstance();
      }
    }
    catch (Exception e) {}
    return object;
  }
  
  public IIcon getIcon(IIconRegister register, String mod, String name)
  {
    return register.registerIcon(mod + ":" + name);
  }
  
  public boolean isShiftDown()
  {
    return (Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54));
  }
  
  public EntityPlayer getPlayer()
  {
    return Minecraft.getMinecraft().thePlayer;
  }
  
  public void handlePreTextureRefresh(IIconRegister register, int type)
  {
    if (type == 0) {
      Binnie.Liquid.reloadIcons(register);
    }
  }
  
  public void preInit()
  {
    IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
    if ((manager instanceof IReloadableResourceManager)) {
      ((IReloadableResourceManager)manager).registerReloadListener(new CraftGUIResourceManager());
    }
  }
}
