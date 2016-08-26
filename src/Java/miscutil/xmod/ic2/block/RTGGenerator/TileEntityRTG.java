package miscutil.xmod.ic2.block.RTGGenerator;

import ic2.core.ContainerBase;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.invslot.InvSlot;
import miscutil.core.util.Utils;
import miscutil.xmod.ic2.block.RTGGenerator.gui.CONTAINER_RadioThermalGenerator;
import miscutil.xmod.ic2.block.RTGGenerator.gui.GUI_RadioThermalGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityRTG
extends TileEntityBaseGenerator
{
	public final InvSlot fuelSlot;

	public TileEntityRTG()
	{
		super(Math.round(16.0F * efficiency), 1, 20000);

		this.fuelSlot = this.invSlots.get(0);
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
		double tempInt = (this.storage += (int)(Math.pow(2.0D, counter - 1) * efficiency));
		Utils.LOG_INFO("int of some sort: "+tempInt);
		this.storage += (int)(Math.pow(2.0D, counter - 1) * efficiency);
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
		return "RTGenerator";
	}


	public Object getGui(EntityPlayer player, int data)
	{
		return new GUI_RadioThermalGenerator(this, player);
	}


	public Object getGuiContainer(EntityPlayer player, int data)
	{
		return new CONTAINER_RadioThermalGenerator(this, player);
	}

	@Override
	public boolean delayActiveUpdate()
	{
		return true;
	}

	private static final float efficiency = 100;

	@Override
	public GuiScreen getGui(EntityPlayer arg0, boolean arg1) {
		getGui(arg0, 1);
		return null;
	}

	@Override
	public ContainerBase<?> getGuiContainer(EntityPlayer arg0) {
		getGuiContainer(arg0, 1);
		return null;
	}
}
