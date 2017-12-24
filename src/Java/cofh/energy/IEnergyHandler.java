package cofh.energy;

import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IEnergyHandler
extends IEnergyProvider, IEnergyReceiver
{
	@Override
	public abstract int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);

	@Override
	public abstract int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);

	@Override
	public abstract int getEnergyStored(ForgeDirection paramForgeDirection);

	@Override
	public abstract int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}
