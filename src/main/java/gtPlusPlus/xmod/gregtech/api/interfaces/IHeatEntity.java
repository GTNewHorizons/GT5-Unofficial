package gtPlusPlus.xmod.gregtech.api.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

import ic2.api.energy.tile.IHeatSource;

public interface IHeatEntity extends IHeatSource, IHeatSink {

    public int getHeatBuffer();

    public void setHeatBuffer(int HeatBuffer);

    public void addtoHeatBuffer(int heat);

    public int getTransmitHeat();

    public int fillHeatBuffer(int maxAmount);

    public int getMaxHeatEmittedPerTick();

    public void updateHeatEntity();

    @Override
    public int maxrequestHeatTick(ForgeDirection directionFrom);

    @Override
    public int requestHeat(ForgeDirection directionFrom, int requestheat);
}
