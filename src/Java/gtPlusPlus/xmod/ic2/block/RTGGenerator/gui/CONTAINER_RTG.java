package gtPlusPlus.xmod.ic2.block.RTGGenerator.gui;

import java.util.List;

import gtPlusPlus.xmod.ic2.block.RTGGenerator.TileEntityRTG;
import ic2.core.block.generator.container.ContainerRTGenerator;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class CONTAINER_RTG
extends ContainerRTGenerator
{
	public CONTAINER_RTG(final EntityPlayer entityPlayer, final TileEntityRTG tileEntity1)
	{
		super(entityPlayer, tileEntity1);
		for (int i = 0; i < 4; i++) {
			this.addSlotToContainer(new SlotInvSlot(tileEntity1.fuelSlot, i, 36 + (i * 18), 18));
		}
		for (int i = 4; i < 8; i++) {
			this.addSlotToContainer(new SlotInvSlot(tileEntity1.fuelSlot, i, 36 + ((i - 4) * 18), 36));
		}
		for (int i = 8; i < 12; i++) {
			this.addSlotToContainer(new SlotInvSlot(tileEntity1.fuelSlot, i, 36 + ((i - 8) * 18), 54));
		}
	}

	@Override
	public List<String> getNetworkedFields()
	{
		final List<String> ret = super.getNetworkedFields();

		ret.add("storage");

		return ret;
	}
}
