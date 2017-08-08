package gtPlusPlus.preloader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class Preloader_GT_OreDict {
	@SubscribeEvent
	public void onItemRegisteredToOreDictionary(final OreRegisterEvent event) throws Throwable {
		String name = event.Name;
		ItemStack ore = event.Ore;
		// Check Item is Valid
		if (ore != null && (name != null && !name.equals(""))) {
			// Check Item has a Valid MODID
			String MODID = "";

			try {
				MODID = ItemUtils.getModId(ore);
			}
			catch (NullPointerException n) {
				Utils.LOG_INFO("[Bug] Found Item with Bad MODID - " + ore.getDisplayName()
				+ " | If you recognise this item, please inform the developer.");
			}

			if (MODID != null) {
				// Check Item has a Valid MODID
				if (!MODID.equals("")) {
					// Check Item is from Gregtech
					if (MODID.equals("gregtech")) {
						//Utils.LOG_INFO("Found GT ITEM - " + ore.getDisplayName());
						// Circuit Removal
						if (Meta_GT_Proxy.areWeUsingGregtech5uExperimental() && CORE_Preloader.enableOldGTcircuits) {
							if (removeCircuit(ore)) {
								Utils.LOG_INFO("[Old Feature - Circuits] Preventing " + ore.getDisplayName()
								+ " from registering itself with oredict tag " + name + ".");
								//Do Magic
								if (event.isCancelable()){
									event.setCanceled(true);
								}
								else {
									try {
										ore = ItemUtils.getSimpleStack(ModItems.AAA_Broken);
										name = "null";
										//event.setCanceled(true);
										throw new OreDictCancelledException("[Old Feature - Circuits] Circuit OreDict event cancelled.");
									}
									catch(OreDictCancelledException | UnsupportedOperationException | IllegalArgumentException  o){
										Utils.LOG_INFO("[Old Feature - Circuits] Circuit OreDict event cancelled.");
									}
									finally{

									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean removeCircuit(ItemStack circuit) {
		int damageValue = circuit.getItemDamage() - 32000;
		if (circuit.getItem() instanceof GT_MetaGenerated_Item_01) { // 700-720
			if (damageValue >= 700 && damageValue <= 720) {
				return true;
			}
		}
		else {
			try {
				if (Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03") != null) { // 6/11/12/14/16/20/30-57/69-73/79-96
					Class MetaItem03 = Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03");
					if (isInstanceOf(MetaItem03, circuit.getItem())) {
						if (damageValue == 6 || damageValue == 11 || damageValue == 12 || damageValue == 14
								|| damageValue == 16 || damageValue == 20) {
							return true;
						}
						else if (damageValue >= 30 && damageValue <= 57) {
							return true;
						}
						else if (damageValue >= 69 && damageValue <= 73) {
							return true;
						}
						else if (damageValue >= 79 && damageValue <= 96) {
							return true;
						}
					}
				}
			}
			catch (ClassNotFoundException e) {
			}
		}
		return false;
	}

	// Simplification of Life.
	private boolean isInstanceOf(Class clazz, Object obj) {
		return clazz.isInstance(obj);
	}

	class OreDictCancelledException extends Exception {
		public OreDictCancelledException(String msg){
			super(msg);
		}
	}
}
