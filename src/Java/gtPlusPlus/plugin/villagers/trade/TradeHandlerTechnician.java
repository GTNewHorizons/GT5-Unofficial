package gtPlusPlus.plugin.villagers.trade;

import java.util.Collections;
import java.util.Random;

import gtPlusPlus.core.recipe.common.CI;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TradeHandlerTechnician extends TradeHandlerBase {


	@SuppressWarnings("unchecked")
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(CI.machineHull_LV, CI.electricMotor_LV, CI.machineHull_LV));
		recipeList.add(new MerchantRecipe(CI.machineHull_MV, CI.electricMotor_MV, CI.machineHull_MV));
		recipeList.add(new MerchantRecipe(CI.machineHull_HV, CI.electricMotor_HV, CI.machineHull_HV));
		recipeList.add(new MerchantRecipe(CI.machineHull_EV, CI.electricMotor_EV, CI.machineHull_EV));
		recipeList.add(new MerchantRecipe(CI.machineHull_IV, CI.electricMotor_IV, CI.machineHull_IV));
		Collections.shuffle(recipeList);		
	}
	
}
