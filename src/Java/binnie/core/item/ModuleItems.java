package binnie.core.item;

import binnie.botany.Botany;
import binnie.botany.api.IAlleleFlowerSpecies;
import binnie.botany.api.IFlower;
import binnie.botany.api.IFlowerColour;
import binnie.botany.api.IFlowerGenome;
import binnie.botany.flower.TileEntityFlower;
import binnie.botany.network.PacketID;
import binnie.botany.proxy.Proxy;
import binnie.core.BinnieCore;
import binnie.core.IInitializable;
import binnie.core.network.packet.MessageNBT;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class ModuleItems
  implements IInitializable
{
  public void preInit()
  {
    BinnieCore.fieldKit = new ItemFieldKit();
    BinnieCore.genesis = new ItemGenesis();
  }
  
  public void init() {}
  
  public void postInit()
  {
    GameRegistry.addRecipe(new ItemStack(BinnieCore.fieldKit, 1, 63), new Object[] { "g  ", " is", " pi", Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('i'), Items.iron_ingot, Character.valueOf('p'), Items.paper, Character.valueOf('s'), new ItemStack(Items.dye, 1) });
  }
  
  @SubscribeEvent
  public void onUseFieldKit(PlayerInteractEvent event)
  {
    if (!BinnieCore.isBotanyActive()) {
      return;
    }
    if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if ((event.entityPlayer != null) && (event.entityPlayer.getHeldItem() != null) && (event.entityPlayer.getHeldItem().getItem() == BinnieCore.fieldKit) && (event.entityPlayer.isSneaking()))
    {
      TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
      if ((tile instanceof TileEntityFlower))
      {
        TileEntityFlower tileFlower = (TileEntityFlower)tile;
        IFlower flower = tileFlower.getFlower();
        if (flower != null)
        {
          NBTTagCompound info = new NBTTagCompound();
          info.setString("Species", flower.getGenome().getPrimary().getUID());
          info.setString("Species2", flower.getGenome().getSecondary().getUID());
          info.setFloat("Age", flower.getAge() / flower.getGenome().getLifespan());
          info.setShort("Colour", (short)flower.getGenome().getPrimaryColor().getID());
          info.setShort("Colour2", (short)flower.getGenome().getSecondaryColor().getID());
          info.setBoolean("Wilting", flower.isWilted());
          info.setBoolean("Flowered", flower.hasFlowered());
          
          Botany.proxy.sendToPlayer(new MessageNBT(PacketID.Encylopedia.ordinal(), info), event.entityPlayer);
          event.entityPlayer.getHeldItem().damageItem(1, event.entityPlayer);
        }
      }
    }
  }
}
