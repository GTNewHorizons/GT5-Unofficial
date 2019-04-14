package gtPlusPlus.xmod.gregtech.api.interfaces;

import ic2.api.energy.tile.IHeatSource;
import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatEntity extends IHeatSource, IHeatSink {

	public int getHeatBuffer();

	public void setHeatBuffer(int HeatBuffer);

	public void addtoHeatBuffer(int heat);

	public int getTransmitHeat();

	public int fillHeatBuffer(int maxAmount);

	public int getMaxHeatEmittedPerTick();
	
	public void updateHeatEntity();

	public int maxrequestHeatTick(ForgeDirection directionFrom);

	public int requestHeat(ForgeDirection directionFrom, int requestheat);
	
}
