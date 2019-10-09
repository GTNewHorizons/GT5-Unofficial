package reactor.items;

import items.MetaItem_ReactorComponent;
import kekztech.GTRecipe;
import kekztech.Items;

public class CoolantCell {
	
	public static String TYPE = "CoolantCell";
	
	public static String[] RESOURCE_NAME = {
			"HeliumCoolantCell360k", "NaKCoolantCell360k"
	};
	
	public static int[] HEAT_CAPACITY = {
			360000, 360000
	};
	
	public static GTRecipe[] RECIPE = {
			new GTRecipe().setDuration(1200).setEUPerTick(480)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.HeliumCoolantCell360k.getMetaID())),
			new GTRecipe().setDuration(1200).setEUPerTick(480)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.NaKCoolantCell360k.getMetaID()))
	};
	
}
