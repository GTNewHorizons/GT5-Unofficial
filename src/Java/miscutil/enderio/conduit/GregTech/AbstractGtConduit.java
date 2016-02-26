package miscutil.enderio.conduit.GregTech;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import crazypants.enderio.conduit.AbstractConduitNetwork;
import crazypants.enderio.conduit.gas.AbstractGasConduit;
import crazypants.enderio.conduit.geom.CollidableComponent;

public class AbstractGtConduit extends AbstractGasConduit{

	@Override
	public ItemStack createItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractConduitNetwork<?, ?> getNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setNetwork(AbstractConduitNetwork<?, ?> network) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IIcon getTextureForState(CollidableComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTransmitionTextureForState(CollidableComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int receiveGas(ForgeDirection side, GasStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GasStack drawGas(ForgeDirection side, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, Gas type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrawGas(ForgeDirection side, Gas type) {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean canJoinNeighbour(IGtConduit n) {
		// TODO Auto-generated method stub
		return false;
	}

}
