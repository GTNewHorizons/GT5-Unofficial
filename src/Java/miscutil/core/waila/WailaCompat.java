package miscutil.core.waila;

import java.util.List;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.impl.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;
import crazypants.enderio.EnderIO;
import crazypants.enderio.TileEntityEio;
import crazypants.enderio.block.BlockDarkSteelAnvil;
import crazypants.enderio.conduit.ConduitUtil;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.conduit.liquid.AbstractTankConduit;
import crazypants.enderio.conduit.liquid.ConduitTank;
import crazypants.enderio.conduit.power.IPowerConduit;
import crazypants.enderio.fluid.Fluids;
import crazypants.enderio.gui.IAdvancedTooltipProvider;
import crazypants.enderio.gui.IResourceTooltipProvider;
import crazypants.enderio.gui.TooltipAddera;
import crazypants.enderio.machine.IIoConfigurable;
import crazypants.enderio.machine.IoMode;
import crazypants.enderio.machine.capbank.TileCapBank;
import crazypants.enderio.machine.power.TileCapacitorBank;
import crazypants.enderio.power.IInternalPoweredTile;
import crazypants.util.IFacade;
import crazypants.util.Lang;

public class WailaCompat
  implements IWailaDataProvider
{
  private class WailaWorldWrapper
    extends World
  {
    private World wrapped;
    
    private WailaWorldWrapper(World wrapped)
    {
      super(null, wrapped.getWorldInfo().getWorldName(), wrapped.provider, new WorldSettings(wrapped.getWorldInfo()), wrapped.theProfiler);
      this.wrapped = wrapped;
      this.isRemote = wrapped.isRemote;
    }
    
    public Block getBlock(int x, int y, int z)
    {
      Block block = this.wrapped.getBlock(x, y, z);
      if ((block instanceof IFacade)) {
        return ((IFacade)block).getFacade(this.wrapped, x, y, z, -1);
      }
      return block;
    }
    
    public int getBlockMetadata(int x, int y, int z)
    {
      Block block = this.wrapped.getBlock(x, y, z);
      if ((block instanceof IFacade)) {
        return ((IFacade)block).getFacadeMetadata(this.wrapped, x, y, z, -1);
      }
      return this.wrapped.getBlockMetadata(x, y, z);
    }
    
    public TileEntity getTileEntity(int x, int y, int z)
    {
      int meta = getBlockMetadata(x, y, z);
      Block block = getBlock(x, y, z);
      if ((block == null) || (!block.hasTileEntity(meta))) {
        return null;
      }
      TileEntity te = block.createTileEntity(this, meta);
      if (te == null) {
        return null;
      }
      te.setWorldObj(this);
      te.xCoord = x;
      te.yCoord = y;
      te.zCoord = z;
      
      return te;
    }
    
    protected IChunkProvider createChunkProvider()
    {
      return null;
    }
    
    protected int func_152379_p()
    {
      return 0;
    }
    
    public Entity getEntityByID(int p_73045_1_)
    {
      return null;
    }
  }
  
  public static final WailaCompat INSTANCE = new WailaCompat();
  private static IWailaDataAccessor _accessor = null;
  
  public static void load(IWailaRegistrar registrar)
  {
    registrar.registerStackProvider(INSTANCE, IFacade.class);
    registrar.registerStackProvider(INSTANCE, BlockDarkSteelAnvil.class);
    
    registrar.registerHeadProvider(INSTANCE, Block.class);
    registrar.registerBodyProvider(INSTANCE, Block.class);
    registrar.registerTailProvider(INSTANCE, Block.class);
    
    registrar.registerNBTProvider(INSTANCE, TileEntityEio.class);
    
    registrar.registerSyncedNBTKey("controllerStoredEnergyRF", TileCapacitorBank.class);
    



    ConfigHandler.instance().addConfig("MiscUtils", "facades.hidden", Lang.localize("waila.config.hiddenfacades"));
    IWailaInfoProvider.fmt.setMaximumFractionDigits(1);
  }
  
  public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    MovingObjectPosition pos = accessor.getPosition();
    if (config.getConfig("facades.hidden"))
    {
      if ((accessor.getBlock() instanceof IFacade))
      {
        if (((accessor.getTileEntity() instanceof IConduitBundle)) && (ConduitUtil.isFacadeHidden((IConduitBundle)accessor.getTileEntity(), accessor.getPlayer()))) {
          return null;
        }
        IFacade bundle = (IFacade)accessor.getBlock();
        Block facade = bundle.getFacade(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ, accessor.getSide().ordinal());
        if (facade != null)
        {
          ItemStack ret = facade.getPickBlock(pos, new WailaWorldWrapper(accessor.getWorld()), pos.blockX, pos.blockY, pos.blockZ);
          return ret;
        }
      }
    }
    else if ((accessor.getBlock() instanceof BlockDarkSteelAnvil)) {
      return accessor.getBlock().getPickBlock(accessor.getPosition(), accessor.getWorld(), accessor.getPosition().blockX, accessor.getPosition().blockY, accessor.getPosition().blockZ);
    }
    return null;
  }
  
  public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    return currenttip;
  }
  
  public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    _accessor = accessor;
    
    EntityPlayer player = accessor.getPlayer();
    MovingObjectPosition pos = accessor.getPosition();
    int x = pos.blockX;int y = pos.blockY;int z = pos.blockZ;
    World world = accessor.getWorld();
    Block block = world.getBlock(x, y, z);
    TileEntity te = world.getTileEntity(x, y, z);
    Item item = Item.getItemFromBlock(block);
    if (((te instanceof IIoConfigurable)) && (block == accessor.getBlock()))
    {
      IIoConfigurable machine = (IIoConfigurable)te;
      ForgeDirection side = accessor.getSide();
      IoMode mode = machine.getIoMode(side);
      currenttip.add(EnumChatFormatting.YELLOW + String.format(Lang.localize("gui.machine.side"), new Object[] { EnumChatFormatting.WHITE + Lang.localize(new StringBuilder().append("gui.machine.side.").append(side.name().toLowerCase()).toString()) }));
      
      currenttip.add(EnumChatFormatting.YELLOW + String.format(Lang.localize("gui.machine.ioMode"), new Object[] { mode.colorLocalisedName() }));
    }
    if ((block instanceof IWailaInfoProvider))
    {
      IWailaInfoProvider info = (IWailaInfoProvider)block;
      if ((block instanceof IAdvancedTooltipProvider))
      {
        int mask = info.getDefaultDisplayMask(world, pos.blockX, pos.blockY, pos.blockZ);
        boolean basic = (mask & 0x1) == 1;
        boolean common = (mask & 0x2) == 2;
        boolean detailed = (mask & 0x4) == 4;
        
        IAdvancedTooltipProvider adv = (IAdvancedTooltipProvider)block;
        if (common) {
          adv.addCommonEntries(itemStack, player, currenttip, false);
        }
        if ((TooltipAddera.showAdvancedTooltips()) && (detailed)) {
          adv.addDetailedEntries(itemStack, player, currenttip, false);
        } else if (detailed) {
          TooltipAddera.addShowDetailsTooltip(currenttip);
        }
        if ((!TooltipAddera.showAdvancedTooltips()) && (basic)) {
          adv.addBasicEntries(itemStack, player, currenttip, false);
        }
      }
      else if ((block instanceof IResourceTooltipProvider))
      {
        TooltipAddera.addInformation((IResourceTooltipProvider)block, itemStack, player, currenttip);
      }
      if (currenttip.size() > 0) {
        currenttip.add("");
      }
      info.getWailaInfo(currenttip, player, world, pos.blockX, pos.blockY, pos.blockZ);
    }
    else if ((block instanceof IAdvancedTooltipProvider))
    {
      TooltipAddera.addInformation((IAdvancedTooltipProvider)block, itemStack, player, currenttip, false);
    }
    else if ((item instanceof IAdvancedTooltipProvider))
    {
      TooltipAddera.addInformation((IAdvancedTooltipProvider)item, itemStack, player, currenttip, false);
    }
    else if ((block instanceof IResourceTooltipProvider))
    {
      TooltipAddera.addInformation((IResourceTooltipProvider)block, itemStack, player, currenttip);
    }
    boolean removeRF = false;
    if (((te instanceof IInternalPoweredTile)) && (block == accessor.getBlock()) && (accessor.getNBTData().hasKey("storedEnergyRF")) && (!(te instanceof TileCapBank)))
    {
      removeRF = true;
      IInternalPoweredTile power = (IInternalPoweredTile)te;
      if (power.displayPower())
      {
        if (currenttip.size() > 4) {
          currenttip.add("");
        }
        int stored = (accessor.getTileEntity() instanceof TileCapacitorBank) ? power.getEnergyStored() : accessor.getNBTData().getInteger("storedEnergyRF");
        int max = power.getMaxEnergyStored();
        
        currenttip.add(String.format("%s%s%s / %s%s%s RF", new Object[] { EnumChatFormatting.WHITE, IWailaInfoProvider.fmt.format(stored), EnumChatFormatting.RESET, EnumChatFormatting.WHITE, IWailaInfoProvider.fmt.format(max), EnumChatFormatting.RESET }));
      }
    }
    else if (((te instanceof IConduitBundle)) && (itemStack != null) && (itemStack.getItem() == EnderIO.itemPowerConduit))
    {
      removeRF = true;
      NBTTagCompound nbtRoot = accessor.getNBTData();
      short nbtVersion = nbtRoot.getShort("nbtVersion");
      NBTTagList conduitTags = (NBTTagList)nbtRoot.getTag("conduits");
      if (conduitTags != null) {
        for (int i = 0; i < conduitTags.tagCount(); i++)
        {
          NBTTagCompound conduitTag = conduitTags.getCompoundTagAt(i);
          IConduit conduit = ConduitUtil.readConduitFromNBT(conduitTag, nbtVersion);
          if ((conduit instanceof IPowerConduit)) {
            currenttip.add(String.format("%s%s%s / %s%s%s RF", new Object[] { EnumChatFormatting.WHITE, IWailaInfoProvider.fmt.format(((IPowerConduit)conduit).getEnergyStored()), EnumChatFormatting.RESET, EnumChatFormatting.WHITE, IWailaInfoProvider.fmt.format(((IConduitBundle)te).getMaxEnergyStored()), EnumChatFormatting.RESET }));
          }
        }
      }
    }
    else if (((te instanceof IConduitBundle)) && (itemStack != null) && (itemStack.getItem() == EnderIO.itemLiquidConduit))
    {
      NBTTagCompound nbtRoot = accessor.getNBTData();
      short nbtVersion = nbtRoot.getShort("nbtVersion");
      NBTTagList conduitTags = (NBTTagList)nbtRoot.getTag("conduits");
      if (conduitTags != null) {
        for (int i = 0; i < conduitTags.tagCount(); i++)
        {
          NBTTagCompound conduitTag = conduitTags.getCompoundTagAt(i);
          IConduit conduit = ConduitUtil.readConduitFromNBT(conduitTag, nbtVersion);
          if ((conduit instanceof AbstractTankConduit))
          {
            AbstractTankConduit tankConduit = (AbstractTankConduit)conduit;
            ConduitTank tank = tankConduit.getTank();
            if (tank.getFluid() == null) {
              break;
            }
            String lockedStr = tankConduit.isFluidTypeLocked() ? Lang.localize("itemLiquidConduit.lockedWaila") : "";
            String fluidName = tank.getFluid().getLocalizedName();
            int fluidAmount = tank.getFluidAmount();
            if (fluidAmount > 0) {
              currenttip.add(String.format("%s%s%s%s %s%s%s %s", new Object[] { lockedStr, EnumChatFormatting.WHITE, fluidName, EnumChatFormatting.RESET, EnumChatFormatting.WHITE, IWailaInfoProvider.fmt.format(fluidAmount), EnumChatFormatting.RESET, Fluids.MB() }));
            } else if (tankConduit.isFluidTypeLocked()) {
              currenttip.add(String.format("%s%s%s%s", new Object[] { lockedStr, EnumChatFormatting.WHITE, fluidName, EnumChatFormatting.RESET }));
            }
            break;
          }
        }
      }
    }
    if (removeRF) {
      ((ITaggedList)currenttip).removeEntries("RFEnergyStorage");
    }
    return currenttip;
  }
  
  public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
  {
    return currenttip;
  }
  
  public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
  {
    if ((te instanceof IWailaNBTProvider)) {
      ((IWailaNBTProvider)te).getData(tag);
    }
    tag.setInteger("x", x);
    tag.setInteger("y", y);
    tag.setInteger("z", z);
    return tag;
  }
  
  public static NBTTagCompound getNBTData()
  {
    return _accessor.getNBTData();
  }
}
