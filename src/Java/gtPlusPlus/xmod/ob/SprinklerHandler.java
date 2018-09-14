package gtPlusPlus.xmod.ob;

import java.util.HashMap;

import com.google.common.base.Objects;

import net.minecraft.item.ItemStack;
import openmods.inventory.GenericInventory;
import openmods.inventory.TileEntityInventory;

/**
 *  Wrapper Class to assist in handling the OB Sprinkler.
 * @author Alkalus
 *
 */
public class SprinklerHandler {


	private static final HashMap<Integer, ItemStack> mValidFerts = new HashMap<Integer, ItemStack>();
	
	/**
	 * @return - A valid {@link Map} of all Fertilizers for the OB Sprinkler.
	 */
	public static HashMap<Integer, ItemStack> getValidFerts() {
		return mValidFerts;
	}
	
	/**
	 * @param aFert - An {@link ItemStack} which is to be registered for OB Sprinklers.
	 */
	public static void registerSprinklerFertilizer(ItemStack aFert) {
		int aHash = Objects.hashCode(aFert.getItem(), aFert.getItemDamage());
		if (!mValidFerts.containsKey(aHash)) {
			mValidFerts.put(aHash, aFert.copy());
		}
	}
	
}
