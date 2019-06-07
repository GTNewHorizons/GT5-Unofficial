package reactor.items;

import kekztech.GTRecipe;
import kekztech.Items;
import kekztech.MetaItem_ReactorComponent;

public class NeutronReflector {
	
	public static String TYPE = "NeutronReflector";
	
	public static String[] RESOURCE_NAME = {
			"T1NeutronReflector", "T2NeutronReflector"
	};
	
	public static int[] DURABILITY = {
			512000, 2147483647
	};
	
	public static GTRecipe[] RECIPE = {
			new GTRecipe().setDuration(1200).setEUPerTick(480)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1NeutronReflector.getMetaID())),
			new GTRecipe().setDuration(4800).setEUPerTick(7680)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T2NeutronReflector.getMetaID()))
	};
	
}
