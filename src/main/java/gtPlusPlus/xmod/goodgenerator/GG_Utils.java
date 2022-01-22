package gtPlusPlus.xmod.goodgenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.bartworks.BW_Utils.NonMeta_MaterialItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GG_Utils {

	private static final Class sClassFuelRodLoader;
	private static final Field[] sClassFuelRodLoaderFields;
	
	static {
		sClassFuelRodLoader = ReflectionUtils.getClass("goodgenerator.loader.FuelRodLoader");
		sClassFuelRodLoaderFields = ReflectionUtils.getAllFields(sClassFuelRodLoader);
	}
	
	public enum GG_Fuel_Rod {
		rodCompressedUranium,
		rodCompressedUranium_2,
		rodCompressedUranium_4,
		rodCompressedPlutonium,
		rodCompressedPlutonium_2,
		rodCompressedPlutonium_4,
		rodCompressedUraniumDepleted,
		rodCompressedUraniumDepleted_2,
		rodCompressedUraniumDepleted_4,
		rodCompressedPlutoniumDepleted,
		rodCompressedPlutoniumDepleted_2,
		rodCompressedPlutoniumDepleted_4,;
	}
	
	public static ItemStack getGG_Fuel_Rod(GG_Fuel_Rod aItem, int aAmount) {
		if (sClassFuelRodLoader != null) {
			return ItemUtils.getSimpleStack((Item) ReflectionUtils.getFieldValue(ReflectionUtils.getField(sClassFuelRodLoader, aItem.name())), aAmount);
		}
		return null;
	}
	
	public static ArrayList<ItemStack> getAll(int aStackSize){
		ArrayList<ItemStack> aItems = new ArrayList<ItemStack>();
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium, aStackSize));
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium_2, aStackSize));
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium_4, aStackSize));
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium, aStackSize));
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium_2, aStackSize));
		aItems.add(getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium_4, aStackSize));
		return aItems;
	}
	
}
