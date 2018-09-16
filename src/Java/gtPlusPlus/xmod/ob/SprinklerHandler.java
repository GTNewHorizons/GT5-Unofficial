package gtPlusPlus.xmod.ob;

import java.util.HashMap;

import com.google.common.base.Objects;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
			Logger.INFO("Registering "+aFert.getDisplayName()+" as OB Sprinkler Fertilizer.");
			mValidFerts.put(aHash, aFert.copy());
		}
	}
	
	public static void registerModFerts() {
		ItemStack f;		

		f = new ItemStack(Items.dye, 1, 15);
		SprinklerHandler.registerSprinklerFertilizer(f);		
		
		if (LoadedMods.Forestry) {
			f = ItemUtils.getCorrectStacktype("Forestry:fertilizerBio", 1);
			if (f != null) {
				registerSprinklerFertilizer(f);
			}
			f = ItemUtils.getCorrectStacktype("Forestry:fertilizerCompound", 1);
			if (f != null) {
				registerSprinklerFertilizer(f);
			}
		}
		if (LoadedMods.IndustrialCraft2) {
			f = ItemUtils.getCorrectStacktype("IC2:itemFertilizer", 1);
			if (f != null) {
				registerSprinklerFertilizer(f);
			}
			
		}
	}
	
}
