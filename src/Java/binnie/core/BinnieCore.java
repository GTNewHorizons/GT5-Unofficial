package binnie.core;

import binnie.Binnie;
import binnie.core.block.MultipassBlockRenderer;
import binnie.core.block.TileEntityMetadata;
import binnie.core.gui.BinnieCoreGUI;
import binnie.core.gui.BinnieGUIHandler;
import binnie.core.gui.IBinnieGUID;
import binnie.core.item.ItemFieldKit;
import binnie.core.item.ItemGenesis;
import binnie.core.item.ModuleItems;
import binnie.core.liquid.FluidContainer;
import binnie.core.liquid.ItemFluidContainer;
import binnie.core.liquid.ManagerLiquid;
import binnie.core.machines.MachineGroup;
import binnie.core.machines.storage.ModuleStorage;
import binnie.core.mod.config.ConfigurationMain;
import binnie.core.mod.config.ConfigurationMods;
import binnie.core.mod.config.ManagerConfig;
import binnie.core.mod.parser.FieldParser;
import binnie.core.mod.parser.ItemParser;
import binnie.core.network.BinnieCorePacketID;
import binnie.core.network.BinniePacketHandler;
import binnie.core.network.IPacketID;
import binnie.core.proxy.BinnieProxy;
import binnie.core.proxy.IBinnieProxy;
import binnie.core.resource.ManagerResource;
import binnie.core.triggers.ModuleTrigger;
import binnie.craftgui.minecraft.ModuleCraftGUI;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.ForestryEvent.SpeciesDiscovered;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.plugins.PluginManager.Module;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;

@Mod(modid="BinnieCore", name="Binnie Core", useMetadata=true)
public final class BinnieCore
  extends AbstractMod
{
  @Mod.Instance("BinnieCore")
  public static BinnieCore instance;
  @SidedProxy(clientSide="binnie.core.proxy.BinnieProxyClient", serverSide="binnie.core.proxy.BinnieProxyServer")
  public static BinnieProxy proxy;
  public static int multipassRenderID;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent evt)
  {
    Binnie.Configuration.registerConfiguration(ConfigurationMods.class, this);
    for (ManagerBase baseManager : Binnie.Managers) {
      addModule(baseManager);
    }
    addModule(new ModuleCraftGUI());
    addModule(new ModuleStorage());
    addModule(new ModuleItems());
    if (Loader.isModLoaded("BuildCraft|Silicon")) {
      addModule(new ModuleTrigger());
    }
    preInit();
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent evt)
  {
    init();
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent evt)
  {
    postInit();
  }
  
  public IBinnieGUID[] getGUIDs()
  {
    return BinnieCoreGUI.values();
  }
  
  public void preInit()
  {
    instance = this;
    for (FluidContainer container : FluidContainer.values())
    {
      Item item = new ItemFluidContainer(container);
      GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
    }
    FieldParser.parsers.add(new ItemParser());
    
    super.preInit();
  }
  
  public void init()
  {
    super.init();
    for (AbstractMod mod : getActiveMods()) {
      NetworkRegistry.INSTANCE.registerGuiHandler(mod, new BinnieGUIHandler(mod));
    }
    multipassRenderID = RenderingRegistry.getNextAvailableRenderId();
    RenderingRegistry.registerBlockHandler(new MultipassBlockRenderer());
    
    GameRegistry.registerTileEntity(TileEntityMetadata.class, "binnie.tile.metadata");
  }
  
  public static boolean isLepidopteryActive()
  {
    return PluginManager.Module.LEPIDOPTEROLOGY.isEnabled();
  }
  
  public static boolean isApicultureActive()
  {
    return PluginManager.Module.APICULTURE.isEnabled();
  }
  
  public static boolean isArboricultureActive()
  {
    return PluginManager.Module.ARBORICULTURE.isEnabled();
  }
  
  public static boolean isBotanyActive()
  {
    return ConfigurationMods.botany;
  }
  
  public static boolean isGeneticsActive()
  {
    return ConfigurationMods.genetics;
  }
  
  public static boolean isExtraBeesActive()
  {
    return (ConfigurationMods.extraBees) && (isApicultureActive());
  }
  
  public static boolean isExtraTreesActive()
  {
    return (ConfigurationMods.extraTrees) && (isArboricultureActive());
  }
  
  public void postInit()
  {
    super.postInit();
  }
  
  private static List<AbstractMod> modList = new ArrayList();
  public static MachineGroup packageCompartment;
  public static ItemGenesis genesis;
  public static ItemFieldKit fieldKit;
  
  static void registerMod(AbstractMod mod)
  {
    modList.add(mod);
  }
  
  private static List<AbstractMod> getActiveMods()
  {
    List<AbstractMod> list = new ArrayList();
    for (AbstractMod mod : modList) {
      if (mod.isActive()) {
        list.add(mod);
      }
    }
    return list;
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void handleSpeciesDiscovered(ForestryEvent.SpeciesDiscovered event)
  {
    try
    {
      EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(event.username.getName());
      if (player == null) {
        return;
      }
      event.tracker.synchToPlayer(player);
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setString("species", event.species.getUID());
    }
    catch (Exception e) {}
  }
  
  public String getChannel()
  {
    return "BIN";
  }
  
  public IBinnieProxy getProxy()
  {
    return proxy;
  }
  
  public String getModID()
  {
    return "binniecore";
  }
  
  public IPacketID[] getPacketIDs()
  {
    return BinnieCorePacketID.values();
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void handleTextureRemap(TextureStitchEvent.Pre event)
  {
    if (event.map.getTextureType() == 0) {
      Binnie.Liquid.reloadIcons(event.map);
    }
    Binnie.Resource.registerIcons(event.map, event.map.getTextureType());
  }
  
  public Class<?>[] getConfigs()
  {
    return new Class[] { ConfigurationMain.class };
  }
  
  protected Class<? extends BinniePacketHandler> getPacketHandler()
  {
    return PacketHandler.class;
  }
  
  public static class PacketHandler
    extends BinniePacketHandler
  {
    public PacketHandler()
    {
      super();
    }
  }
  
  public boolean isActive()
  {
    return true;
  }
}
