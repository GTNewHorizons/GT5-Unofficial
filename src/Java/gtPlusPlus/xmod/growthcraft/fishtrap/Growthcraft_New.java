package gtPlusPlus.xmod.growthcraft.fishtrap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;

public class Growthcraft_New {

	Method addTrapJunk;
	Method addTrapTreasure;
	Method addTrapFish;
	Object FishTrapRegistryO;
	
	public Growthcraft_New(){
		setFishTrapRegistry();
	}
	
	void setFishTrapRegistry(){
		try {
			Class<?> FishTrapRegistryClass = Class.forName("gtPlusPlus.xmod.growthcraft.fishtrap.FishTrapHandler.mFishingRegistry");
			Class<?> FishTrapEntry = Class.forName("growthcraft.api.fishtrap.FishTrapEntry");
			if (FishTrapRegistryClass.isInstance(FishTrapHandler.getFishingRegistry())){
				addTrapJunk = FishTrapRegistryClass.getDeclaredMethod("addTrapJunk", FishTrapEntry);
				addTrapTreasure = FishTrapRegistryClass.getDeclaredMethod("addTrapTreasure", FishTrapEntry);
				addTrapFish = FishTrapRegistryClass.getDeclaredMethod("addTrapFish", FishTrapEntry);
				FishTrapRegistryO = FishTrapHandler.getFishingRegistry();
			}
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private Object createFishTrapEntry(ItemStack loot, int chance){
		try {
			Class<?> FishTrapEntry = Class.forName("growthcraft.api.fishtrap.FishTrapEntry");
			Object o = FishTrapEntry.getConstructor(ItemStack.class, int.class);
			if (FishTrapEntry != null){
				Constructor[] constructors = FishTrapEntry.getDeclaredConstructors();
				constructors[0].setAccessible(true);
				Object x = constructors[0].newInstance(loot, chance);
				if (x != null){
					return x;
				}				
			}
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		
		return null;
	}
	
	private boolean invoke(Method m, ItemStack o, int p){
		try {
			Object I = createFishTrapEntry(o, p);
			m.invoke(FishTrapRegistryO, I);
			return true;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		return false;
	}

	public void addTrapJunk(final ItemStack loot, final int lootChance){
		//FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(loot, lootChance));
		if (addTrapJunk != null){
			invoke(addTrapJunk, loot, lootChance);
		}

	}

	public void addTrapTreasure(final ItemStack loot, final int lootChance){
		//FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(loot, lootChance));
		if (addTrapTreasure != null){
			invoke(addTrapTreasure, loot, lootChance);
		}
	}

	public void addTrapFish(final ItemStack loot, final int lootChance){
		//FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(loot, lootChance));
		if (addTrapFish != null){
			invoke(addTrapFish, loot, lootChance);
		}
	}

}
