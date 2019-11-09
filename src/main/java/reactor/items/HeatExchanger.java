package reactor.items;

public class HeatExchanger {
	
	public static String TYPE = "HeatExchanger";
	
	public static String[] RESOURCE_NAME = {
			"T1HeatExchanger", "T2HeatExchanger", "T3HeatExchanger", "T4HeatExchanger"
	};
	
	public static int[] HEAT_CAPACITY = {
			2000, 8000, 32000, 128000
	};
	
	public static int[] COMPONENT_EXCHANGE_RATE = {
			12, 24, 96, 384
	};
	
	public static int[] HULL_EXCHANGE_RATE = {
			4, 8, 32, 128
	};
	
}
