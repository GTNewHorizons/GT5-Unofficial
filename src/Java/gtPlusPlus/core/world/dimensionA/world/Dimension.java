package gtPlusPlus.core.world.dimensionA.world;

import gtPlusPlus.core.world.DimensionIDs;
import net.minecraftforge.common.DimensionManager;

public class Dimension {
	
	/**
	 * Register dimensions.
	 * @param register
	 */
	public static void registerDimensions(){
		DimensionManager.registerDimension(DimensionIDs.Dimension_A, DimensionIDs.Dimension_A);
	}
	
	/**
	 * Regster dimension world providers with the dimension manager.
	 */
	public static void registerWorldProvider(){
		DimensionManager.registerProviderType(DimensionIDs.Dimension_A, WorldProviderForest.class, true);
	}
}
