package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import common.items.ErrorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Util {
	
	public static ItemStack getStackofAmountFromOreDict(String oredictName, final int amount){
		final ArrayList<ItemStack> list = OreDictionary.getOres(oredictName);
		if(!list.isEmpty()) {
			final ItemStack ret = list.get(0).copy();
			ret.stackSize = amount;
			return ret;
		}
		System.err.println("Failed to find " + oredictName + " in OreDict");
		return new ItemStack(ErrorItem.getInstance(), amount);
	}
	
	public static ItemStack[] toItemStackArray(List<ItemStack> stacksList) {
		if(stacksList.size() == 0) {
			return null;
		}
		
		ItemStack[] ret = new ItemStack[stacksList.size()];
		Iterator<ItemStack> iterator = stacksList.iterator();
		for(int i = 0; i < ret.length; i++	) {
			ret[i] = iterator.next();
		}
		return ret;
	}
	
	public static FluidStack[] toFluidStackArray(List<FluidStack> stacksList) {
		if(stacksList.size() == 0) {
			return null;
		}
		
		FluidStack[] ret = new FluidStack[stacksList.size()];
		Iterator<FluidStack> iterator = stacksList.iterator();
		for(int i = 0; i < ret.length; i++	) {
			ret[i] = iterator.next();
		}
		return ret;
	}
	
}
