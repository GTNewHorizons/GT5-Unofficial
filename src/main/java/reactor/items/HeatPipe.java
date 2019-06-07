package reactor.items;

import kekztech.GTRecipe;
import kekztech.Items;
import kekztech.MetaItem_CraftingComponent;
import kekztech.Util;

public class HeatPipe {
	
	public static String[] RESOURCE_NAMES = {
			"CopperHeatPipe", "SilverHeatPipe", "BoronArsenideHeatPipe", "DiamondHeatPipe"
	};
	
	public static final GTRecipe[] RECIPE = {
			new GTRecipe().setEUPerTick(120).setDuration(120)
					.addInputItem(Util.getStackofAmountFromOreDict("stickCopper", 1))
					.addOutputItem(MetaItem_CraftingComponent.getInstance().getStackFromDamage(Items.CopperHeatPipe.getMetaID())),
			new GTRecipe().setEUPerTick(480).setDuration(120)
					.addInputItem(Util.getStackofAmountFromOreDict("stickSilver", 1))
					.addOutputItem(MetaItem_CraftingComponent.getInstance().getStackFromDamage(Items.SilverHeatPipe.getMetaID())),
			new GTRecipe().setEUPerTick(1920).setDuration(1200)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.BoronArsenideCrystal.getMetaID(), 4))
					.addOutputItem(MetaItem_CraftingComponent.getInstance().getStackFromDamage(Items.BoronArsenideHeatPipe.getMetaID())),
			new GTRecipe().setEUPerTick(7680).setDuration(1200)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondCrystal.getMetaID(), 4))
					.addOutputItem(MetaItem_CraftingComponent.getInstance().getStackFromDamage(Items.DiamondHeatPipe.getMetaID()))
	};
	
}
