package gtPlusPlus.xmod.galacticraft.system.objects;

import java.util.Map;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraft.block.Block;

public interface IPlanetBlockRegister extends Runnable {

	public abstract Map<Integer, Block> getBlocks();

	public abstract Block getWaterBlock();
	
	public abstract Block getTopLayer();
	
	public abstract Block getSoil();
	
	public abstract Block getSoil2();
	
	public abstract Block getStone();
	
	public abstract void register();

	@Override
	default void run() {
		register();
	}
	
	
	
}
