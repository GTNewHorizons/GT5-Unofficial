package gtPlusPlus.xmod.sol;

import java.lang.reflect.Constructor;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;

public class HANDLER_SpiceOfLife {

	public static final void preInit() {
		if (LoadedMods.SpiceOfLife) {
			//Add a new Lunch Box with a reasonable amount of slots
			tryRegisterNewLunchBox("foodcrate", 12);
		}
	}

	public static final void init() {
		if (LoadedMods.SpiceOfLife) {

		}
	}

	public static final void postInit() {		
		if (LoadedMods.SpiceOfLife) {
			
		}
	}
	
	private static boolean tryRegisterNewLunchBox(String aItemName, int aSlots) {
		Item aNewBox = getNewLunchBox(aItemName, aSlots);
		if (aNewBox != null) {
			GameRegistry.registerItem(aNewBox, aItemName);
			Logger.INFO("[Spice of Life] Registered "+aItemName+" as a new food container.");
			return true;
		}		
		return false;
	}
	
	private static Item getNewLunchBox(String aItemName, int aSlots) {
		Class aItemFoodContainer = ReflectionUtils.getClass("squeek.spiceoflife.items.ItemFoodContainer");
		if (aItemFoodContainer != null) {
			Constructor aItemFoodContainerConstructor = ReflectionUtils.getConstructor(aItemFoodContainer, new Class[] {String.class, int.class});
			if (aItemFoodContainerConstructor != null) {
				Object aNewObject = ReflectionUtils.createNewInstanceFromConstructor(aItemFoodContainerConstructor, new Object[] {aItemName, aSlots});
				if (aNewObject instanceof Item) {
					Item aNewInstance = (Item) aNewObject;
					return aNewInstance;
				}
			}
		}
		return null;
	}
	
	
	
	

}
