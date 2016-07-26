package miscutil.core.item.init;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.item.base.foods.BaseItemFood;
import miscutil.core.item.base.foods.BaseItemHotFood;
import miscutil.core.lib.CORE;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemsFoods {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		//Raisin Bread		
		ModItems.itemIngotRaisinBread = new BaseItemFood("itemIngotRaisinBread", "Raisin Bread", 3, 1.5f, false, new PotionEffect(Potion.weakness.id, 40, 1)).setAlwaysEdible();
		GT_OreDictUnificator.registerOre("foodRaisinBread", UtilsItems.getItemStack(CORE.MODID+":itemIngotRaisinBread", 1));
		//Hot Raisin Bread 
		ModItems.itemHotIngotRaisinBread = new BaseItemHotFood("itemHotIngotRaisinBread", 1, 0.5f, "Raisin Bread", 120, ModItems.itemIngotRaisinBread);
		GT_OreDictUnificator.registerOre("foodHotRaisinBread", UtilsItems.getItemStack(CORE.MODID+":itemHotIngotRaisinBread", 1));

		//Raisin Bread		
		ModItems.itemFoodRaisinToast = new BaseItemFood("itemFoodRaisinToast", "Raisin Toast", 1, 0.5f, false).setAlwaysEdible();
		GT_OreDictUnificator.registerOre("foodRaisinToast", UtilsItems.getItemStack(CORE.MODID+":itemFoodRaisinToast", 1));
		//Hot Raisin Bread 
		ModItems.itemHotFoodRaisinToast = new BaseItemHotFood("itemHotFoodRaisinToast", 1, 0.5f, "Raisin Toast", 20, ModItems.itemFoodRaisinToast);
		GT_OreDictUnificator.registerOre("foodHotRaisinToast", UtilsItems.getItemStack(CORE.MODID+":itemHotFoodRaisinToast", 1));

		//Raisin Bread		
		ModItems.itemFoodCurriedSausages = new BaseItemFood("itemFoodCurriedSausages", "Curried Sausages", 5, 2f, false);
		GT_OreDictUnificator.registerOre("foodCurriedSausages", UtilsItems.getItemStack(CORE.MODID+":itemFoodCurriedSausages", 1));
		//Hot Raisin Bread 
		ModItems.itemHotFoodCurriedSausages = new BaseItemHotFood("itemHotFoodCurriedSausages", 1, 0.5f, "Curried Sausages", 240, ModItems.itemFoodCurriedSausages);
		GT_OreDictUnificator.registerOre("foodHotCurriedSausages", UtilsItems.getItemStack(CORE.MODID+":itemHotFoodCurriedSausages", 1));
		
	}
	
}
