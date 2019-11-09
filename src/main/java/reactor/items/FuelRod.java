package reactor.items;

public class FuelRod {

	public static final int METAOFFSET = 17;
	public static final int METAOFFSET_DEPLETED = 29;
	
	public static final String TYPE = "FuelRod";
	public static final String TYPE_DEPLETED = "DepletedFuelRod";
	public static final String LIFETIME = "LIFETIME";
	public static final String PULSES = "PULSES";
	public static final String HEAT_PER_SECOND = "HEAT_PER_SECOND";
	public static final String HEAT_PER_PULSE = "HEAT_PER_PULSE";
	public static final String HEAT_BOOST_RATE = "HEAT_BOOST_RATE";
	public static final String EU_PER_TICK = "EU_PER_TICK";
	public static final String EU_PER_PULSE = "EU_PER_PULSE";
	
	
	public static final String[] RESOURCE_NAME = {
			"UraniumFuelRod", "UraniumDualFuelRod", "UraniumQuadFuelRod",
			"ThoriumFuelRod", "ThoriumDualFuelRod", "ThoriumQuadFuelRod",
			"MOXFuelRod", "MOXDualFuelRod", "MOXQuadFuelRod",
			"NaquadahFuelRod", "NaquadahDualFuelRod", "NaquadahQuadFuelRod",
			"Th_MOXFuelRod", "Th_MOXDualFuelRod", "Th_MOXQuadFuelRod"
	};
	
	public static final String[] RESOURCE_NAME_DEPLETED = {
			"DepletedUraniumFuelRod", "DepletedUraniumDualFuelRod", "DepletedUraniumQuadFuelRod",
			"DepletedThoriumFuelRod", "DepletedThoriumDualFuelRod", "DepletedThoriumQuadFuelRod",
			"DepletedMOXFuelRod", "DepletedMOXDualFuelRod", "DepletedMOXQuadFuelRod",
			"DepletedNaquadahFuelRod", "DepletedNaquadahDualFuelRod", "DepletedNaquadahQuadFuelRod",
			"Th_DepletedMOXFuelRod", "Th_DepletedMOXDualFuelRod", "Th_DepletedMOXQuadFuelRod"
	};
	
	public static final int[] VALUES_LIFETIME = {
			20000, 20000, 20000, 100000, 100000, 100000,
			10000, 10000, 10000, 100000, 100000, 100000,
			50000, 50000, 50000
	};
	
	public static final int[] VALUES_PULSES = {
			1, 2, 4, 1, 2, 4,
			1, 2, 4, 1, 2, 4,
			1, 2, 4
	};
	
	public static final int[] VALUES_HEAT_PER_SECOND = {
			4, 24, 96, 1, 6, 24, 
			4, 24, 96, 80, 480, 1920,
			1, 6, 24
	};
	
	public static final int[] VALUES_HEAT_PER_PULSE = {
			4, 4, 4, 1, 1, 1, 
			4, 4, 4, 80, 80, 80,
			1, 1, 1
			
	};
	
	public static final double[] VALUES_HEAT_BOOST_RATE = {
			1, 1, 1, 1, 1, 1,
			5, 5, 5, 1, 1, 1,
			2, 2, 2
	};
	
	public static final int[] VALUES_EU_PER_TICK = {
			50, 200, 600, 10, 40, 120,
			50, 200, 600, 100, 400, 1200,
			10, 40, 120
	};
	
	public static final int[] VALUES_EU_PER_PULSE = {
			50, 50, 50, 10, 10, 10,
			50, 50, 50, 100, 100, 100,
			10, 10, 10
	};
	
}
