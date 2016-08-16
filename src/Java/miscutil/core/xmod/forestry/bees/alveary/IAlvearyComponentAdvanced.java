package miscutil.core.xmod.forestry.bees.alveary;

import forestry.api.apiculture.IBeeModifier;
import forestry.api.core.IClimateControlled;
import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockLogicAlveary;

public abstract interface IAlvearyComponentAdvanced<T extends IMultiblockLogicAlveary>
extends IMultiblockComponent
{
	@Override
	public abstract T getMultiblockLogic();

	
	public static abstract interface FrameHouse
	extends IAlvearyComponentAdvanced
	{
		public abstract void changeClimate(int paramInt, IClimateControlled paramIClimateControlled);
	}

	
	public static abstract interface BeeModifier
	extends IAlvearyComponentAdvanced
	{
		public abstract IBeeModifier getBeeModifier();
	}

}
