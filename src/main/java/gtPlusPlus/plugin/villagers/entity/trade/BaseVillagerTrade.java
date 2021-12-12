package gtPlusPlus.plugin.villagers.entity.trade;

import java.util.Random;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

public abstract class BaseVillagerTrade implements IVillageTradeHandler {


	/*
	Recently in my mod I started working on a new villager and after doing so I could not figure out to assign new trades to this villager.
	Registering the villager
	VillagerRegistry.instance().registerVillageTradeHandler(i, new TradeHandler());}
    VillagerRegistry.instance().registerVillagerId(8);
    VillagerRegistry.instance().registerVillagerSkin(8, new ResourceLocation("chow", "textures/dealer.png"));
    VillagerRegistry.instance().getRegisteredVillagers();
	In my trade handler for vanilla villagers it goes off case 0 for instance being the farmer. I thought that it being case 8 would represent the id 8 for the custom villager but it still doesn't work. Could anyone help me out with this please?
	 * 
	 * 
	First, you'll need to make a new class that extends IVillageTradeHandler.
	In the constructor, add ItemStacks of the items you want it to trade to an ArrayList,
	then in the manipulateTradesForVillager method, have it make sure the villager is yours by using villager.getProfession() 
	and your villager ID, then in a for loop use recipeList.addToListWithCheck to add new instances of MerchantRecipe to your villager's trade list.
	Then, in your mod's main class, register the trade handler with VillageRegistry.instance().registerVillagerTradeHandler(villagerId, instanceOfTradeHandler);
	 */

	@Override
	public abstract void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random);

}
