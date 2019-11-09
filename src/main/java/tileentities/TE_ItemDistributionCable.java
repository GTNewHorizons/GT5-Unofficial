package tileentities;

import kekztech.IConduit;
import kekztech.ItemDistributionNetworkController;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemDistributionCable extends TileEntity implements IConduit {
	
	private ItemDistributionNetworkController network;
	
	public TE_ItemDistributionCable() {
		ItemDistributionNetworkController.placeConduit(this);
	}
	
	@Override
	public void setNetwork(ItemDistributionNetworkController network) {
		this.network = network;
	}

	@Override
	public ItemDistributionNetworkController getNetwork() {
		return network;
	}

}
