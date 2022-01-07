package gtPlusPlus.plugin.villagers.trade;

import java.util.Collections;
import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TradeHandlerTrader extends TradeHandlerBase {

	public TradeHandlerTrader() {
		Logger.INFO("Created Trade Manager for 'Trader' villager profession type.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		ItemStack Ore1 = null, Ore2 = null;
		if (Ore1 == null) {
			Ore1 = ELEMENT.getInstance().SILICON.getOre(1);
		}
		if (Ore2 == null) {
			Ore2 = ELEMENT.getInstance().ALUMINIUM.getOre(1);
		}
		if (Ore1 == null) {
			Ore1 = ELEMENT.getInstance().GOLD.getOre(1);
		}
		if (Ore2 == null) {
			Ore2 = ELEMENT.getInstance().LEAD.getOre(1);
		}		
		recipeList.add(new MerchantRecipe(ItemUtils.getItemStackOfAmountFromOreDict("logWood", 32), ELEMENT.getInstance().IRON.getOre(1)));
		recipeList.add(new MerchantRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 32), ELEMENT.getInstance().COPPER.getOre(1)));
		recipeList.add(new MerchantRecipe(ItemUtils.getSimpleStack(Blocks.obsidian, 6), ELEMENT.getInstance().TIN.getOre(1)));
		recipeList.add(new MerchantRecipe(ItemUtils.getSimpleStack(Blocks.glowstone, 32), Ore1));
		recipeList.add(new MerchantRecipe(ItemUtils.getSimpleStack(Blocks.piston, 32), Ore2));
		Collections.shuffle(recipeList);	
	}

}
