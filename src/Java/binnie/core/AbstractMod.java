package binnie.core;

import binnie.Binnie;
import binnie.core.gui.IBinnieGUID;
import binnie.core.mod.config.ManagerConfig;
import binnie.core.mod.parser.FieldParser;
import binnie.core.network.BinniePacketHandler;
import binnie.core.network.IPacketID;
import binnie.core.network.IPacketProvider;
import binnie.core.network.packet.MessageBinnie;
import binnie.core.proxy.IProxyCore;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;

public abstract class AbstractMod
  implements IPacketProvider, IInitializable
{
  private SimpleNetworkWrapper wrapper;
  
  public AbstractMod()
  {
    BinnieCore.registerMod(this);
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  public abstract boolean isActive();
  
  public abstract String getChannel();
  
  public IPacketID[] getPacketIDs()
  {
    return new IPacketID[0];
  }
  
  public IBinnieGUID[] getGUIDs()
  {
    return new IBinnieGUID[0];
  }
  
  public Class[] getConfigs()
  {
    return new Class[0];
  }
  
  public abstract IProxyCore getProxy();
  
  public abstract String getModID();
  
  public SimpleNetworkWrapper getNetworkWrapper()
  {
    return this.wrapper;
  }
  
  protected abstract Class<? extends BinniePacketHandler> getPacketHandler();
  
  public void preInit()
  {
    if (!isActive()) {
      return;
    }
    if (getConfigs() != null) {
      for (Class cls : getConfigs()) {
        Binnie.Configuration.registerConfiguration(cls, this);
      }
    }
    getProxy().preInit();
    for (IInitializable module : this.modules) {
      module.preInit();
    }
    for (Field field : getClass().getFields()) {
      this.fields.add(field);
    }
    for (Class cls : getClass().getClasses()) {
      for (Field field : getClass().getFields()) {
        this.fields.add(field);
      }
    }
    for (IInitializable module : this.modules) {
      for (Field field : module.getClass().getFields()) {
        this.fields.add(field);
      }
    }
    for (Field field : this.fields) {
      try
      {
        FieldParser.preInitParse(field, this);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
  }
  
  public void init()
  {
    if (!isActive()) {
      return;
    }
    getProxy().init();
    
    this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(getChannel());
    
    this.wrapper.registerMessage(getPacketHandler(), MessageBinnie.class, 1, Side.CLIENT);
    this.wrapper.registerMessage(getPacketHandler(), MessageBinnie.class, 1, Side.SERVER);
    for (IInitializable module : this.modules) {
      module.init();
    }
    for (Field field : this.fields) {
      try
      {
        FieldParser.initParse(field, this);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
  }
  
  private LinkedHashSet<Field> fields = new LinkedHashSet();
  
  public void postInit()
  {
    if (!isActive()) {
      return;
    }
    getProxy().postInit();
    for (IInitializable module : this.modules) {
      module.postInit();
    }
    for (Field field : this.fields) {
      try
      {
        FieldParser.postInitParse(field, this);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
  }
  
  protected final void addModule(IInitializable init)
  {
    this.modules.add(init);
    MinecraftForge.EVENT_BUS.register(init);
  }
  
  protected List<IInitializable> modules = new ArrayList();
}
