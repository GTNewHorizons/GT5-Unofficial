package miscutil.xmod.ic2.block.RTGGenerator;

import ic2.core.ContainerBase;
import ic2.core.Ic2Items;
import ic2.core.block.generator.tileentity.TileEntityRTGenerator;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotConsumableId;
import miscutil.xmod.ic2.block.RTGGenerator.gui.CONTAINER_RTG;
import miscutil.xmod.ic2.block.RTGGenerator.gui.GUI_RTG;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityRTG
extends TileEntityRTGenerator
{
	public final InvSlotConsumable fuelSlot;

	public TileEntityRTG()
	{
		this.fuelSlot = new InvSlotConsumableId(this, "fuelSlot", 0, 12, new Item[] { Ic2Items.RTGPellets.getItem() });
	}

	@Override
	public int gaugeFuelScaled(int i)
	{
		return i;
	}

	@Override
	 public boolean gainEnergy()
	  {
	    int counter = 0;
	    for (int i = 0; i < this.fuelSlot.size(); i++) {
	      if (this.fuelSlot.get(i) != null) {
	        counter++;
	      }
	    }
	    if (counter == 0) {
	      return false;
	    }
	    this.storage += (int)Math.pow(2.0D, counter - 1);
	    return true;
	  }

	@Override
	public boolean gainFuel()
	{
		return false;
	}

	@Override
	public boolean needsFuel()
	{
		return true;
	}

	@Override
	public String getInventoryName()
	{
		return "RTG";
	}
	
	@Override
	public ContainerBase<TileEntityRTGenerator> getGuiContainer(EntityPlayer entityPlayer)
	{
		return new CONTAINER_RTG(entityPlayer, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
	{
		return new GUI_RTG(new CONTAINER_RTG(entityPlayer, this));
	}

	@Override
	public boolean delayActiveUpdate()
	{
		return true;
	}

	
}
