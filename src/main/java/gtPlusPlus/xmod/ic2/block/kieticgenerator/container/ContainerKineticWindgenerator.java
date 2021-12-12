package gtPlusPlus.xmod.ic2.block.kieticgenerator.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import ic2.core.ContainerFullInv;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.slot.SlotInvSlot;

public class ContainerKineticWindgenerator
extends ContainerFullInv<TileEntityWindKineticGenerator>
{
	public ContainerKineticWindgenerator(final EntityPlayer entityPlayer, final TileEntityWindKineticGenerator tileEntity1)
	{
		super(entityPlayer, tileEntity1, 166);

		this.addSlotToContainer(new SlotInvSlot(tileEntity1.rotorSlot, 0, 80, 26));
	}

	@Override
	public List<String> getNetworkedFields()
	{
		final List<String> ret = super.getNetworkedFields();
		ret.add("windStrength");
		return ret;
	}
}
