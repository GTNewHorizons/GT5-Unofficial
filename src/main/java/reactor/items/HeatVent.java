package reactor.items;

public class HeatVent {
	
	public static final String TYPE = "HeatVent";
	public static final String HEAT = "HEAT";
	public static final String RESOURCE_NAME = "RESOURCE_NAME";
	public static final String HEAT_CAPACITY = "HEAT_CAPACITY";
	public static final String COMPONENT_VENT_RATE = "COMPONENT_VENT_RATE";
	public static final String HULL_DRAW_RATE = "HULL_DRAW_RATE";
	public static final String SELF_COOL_RATE = "SELF_COOL_RATE";
	
	
	public static String[] RESOURCE_NAMES = {
			"T1HeatVent", "T2HeatVent", "T3HeatVent", "T4HeatVent",
			"T1ComponentHeatVent", "T2ComponentHeatVent", "T3ComponentHeatVent", "T4ComponentHeatVent",
			"T1OverclockedHeatVent", "T2OverclockedHeatVent", "T3OverclockedHeatVent", "T4OverclockedHeatVent"
	};
	
	public static int[] HEAT_CAPACITIES = {
			1000, 4000, 8000, 32000,
			1000, 4000, 8000, 32000,
			1000, 4000, 8000, 32000
	};
	
	public static int[] COMPONENT_VENT_RATES = {
			0, 0, 0, 0,
			6, 12, 48, 96,
			0, 0, 0, 0
	};
	
	public static int[] HULL_DRAW_RATES = {
			0, 0, 0, 0,
			0, 0, 0, 0,
			18, 36, 144, 288
	};
	
	public static int[] SELF_COOL_RATES = {
			6, 12, 48, 96,
			0, 0, 0, 0,
			10, 20, 80, 160
	};
	
}
