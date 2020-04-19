package items;

import cpw.mods.fml.common.registry.GameRegistry;
import thaumcraft.common.blocks.ItemJarFilled;

public class Item_IchorJarFilled extends ItemJarFilled {
	
	private static final Item_IchorJarFilled instance = new Item_IchorJarFilled();
	
	private Item_IchorJarFilled() {
		super();
	}
	
	public static Item_IchorJarFilled getInstance() {
		return instance;
	}
	
	public void registerItem() {
		super.setHasSubtypes(false);
		final String unlocalizedName = "kekztech_ichorjarfilled_item";
		super.setUnlocalizedName(unlocalizedName);
		GameRegistry.registerItem(getInstance(), unlocalizedName);
	}
}
