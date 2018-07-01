package gtPlusPlus.plugin.villagers.trade;

import java.util.Collections;
import java.util.Random;

import gtPlusPlus.core.recipe.common.CI;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TradeHandlerBanker extends TradeHandlerBase {


	@SuppressWarnings("unchecked")
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(CI.electricMotor_LV, CI.electricPiston_LV, CI.robotArm_LV));
		recipeList.add(new MerchantRecipe(CI.electricMotor_MV, CI.electricPiston_MV, CI.robotArm_MV));
		recipeList.add(new MerchantRecipe(CI.electricMotor_HV, CI.electricPiston_HV, CI.robotArm_HV));
		recipeList.add(new MerchantRecipe(CI.electricMotor_EV, CI.electricPiston_EV, CI.robotArm_EV));
		recipeList.add(new MerchantRecipe(CI.electricMotor_IV, CI.electricPiston_IV, CI.robotArm_IV));
		Collections.shuffle(recipeList);		
	}
	
}
