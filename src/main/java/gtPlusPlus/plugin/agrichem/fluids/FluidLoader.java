package gtPlusPlus.plugin.agrichem.fluids;

import gtPlusPlus.core.fluids.FluidFactory;

public class FluidLoader {
	
	private static final int ID_DIRTY_WATER = 50;
	private static final int ID_RAW_SEWERAGE = 51;
	private static final int ID_GUANO = 52;
	private static final int ID_POOPJUICE = 53;
	
	public static void generate() {

		FluidFactory.generate(ID_DIRTY_WATER, "dirtywater", new short[] {25, 25, 180});
		FluidFactory.generate(ID_RAW_SEWERAGE, "sewerage", new short[] {100, 45, 25});
		FluidFactory.generate(ID_GUANO, "guano", new short[] {175, 175, 180});
		FluidFactory.generate(ID_POOPJUICE, "poo", new short[] {75, 45, 10});
		
	}
	
}
