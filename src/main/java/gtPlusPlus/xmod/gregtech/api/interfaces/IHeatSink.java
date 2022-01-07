package gtPlusPlus.xmod.gregtech.api.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatSink {
	
	
	int maxHeatInPerTick(ForgeDirection var1);

	int addHeat(ForgeDirection var1, int var2);
	
	
}