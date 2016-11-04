package gtPlusPlus.core.block.machine.heliumgen.container;

import java.util.List;

import gtPlusPlus.core.block.machine.heliumgen.tileentity.TileEntityHeliumGenerator;
import ic2.core.ContainerBase;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerHeliumGenerator
extends ContainerBase<TileEntityHeliumGenerator>
{
	public short size;

	public ContainerHeliumGenerator(InventoryPlayer player, TileEntityHeliumGenerator machine)
	{
		super(machine);
		//Utils.LOG_WARNING("containerHeliumGenerator");
		short sr = machine.getReactorSize();
		this.addSlotToContainer(new SlotFurnace(player.player, machine, 2, 80, 35));
		this.size = sr;
		int startX = 16;
		int startY = 16;
		int i = 0;
		for (i = 0; i < 9; i++)
		{
			int x = i % this.size;
			int y = i / this.size;

			addSlotToContainer(new SlotInvSlot(machine.reactorSlot, i, startX + 18 * x, startY + 18 * y));
		}
		startX = 108;
		startY = 16;
		for (i = 9; i < 18; i++)
		{
			int x = i % this.size;
			int y = (i-9) / this.size;

			addSlotToContainer(new SlotInvSlot(machine.reactorSlot, i, startX + 18 * x, startY + 18 * y));
		}
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
		}
		// addSlotToContainer(new SlotInvSlot(machine.coolantinputSlot, 0, 8, 25));
		//addSlotToContainer(new SlotInvSlot(machine.hotcoolinputSlot, 0, 188, 25));
		//addSlotToContainer(new SlotInvSlot(machine.coolantoutputSlot, 0, 8, 115));
		//addSlotToContainer(new SlotInvSlot(machine.hotcoolantoutputSlot, 0, 188, 115));
	}

	@Override
	public List<String> getNetworkedFields()
	{
		List<String> ret = super.getNetworkedFields();

		ret.add("heat");
		ret.add("maxHeat");
		ret.add("EmitHeat");
		/*ret.add("inputTank");
    ret.add("outputTank");
    ret.add("fluidcoolreactor");*/
		return ret;
	}   
}