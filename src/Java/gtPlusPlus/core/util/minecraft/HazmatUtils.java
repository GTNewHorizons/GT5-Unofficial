package gtPlusPlus.core.util.minecraft;

import static gregtech.api.GregTech_API.sBioHazmatList;
import static gregtech.api.GregTech_API.sElectroHazmatList;
import static gregtech.api.GregTech_API.sFrostHazmatList;
import static gregtech.api.GregTech_API.sGasHazmatList;
import static gregtech.api.GregTech_API.sHeatHazmatList;
import static gregtech.api.GregTech_API.sRadioHazmatList;

import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.objects.GT_HashSet;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.Utils;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HazmatUtils {

	public static final GT_HashSet<GT_ItemStack> sHazmatList = new GT_HashSet<GT_ItemStack>();

	private static final HashMap<String, AutoMap<String>> mToolTips = new HashMap<String, AutoMap<String>>();

	private static boolean mInit = false;
	private static HazmatUtils mInstance;

	public static void init() {
		if (mInit) {
			return;
		}

		mInstance = new HazmatUtils();

		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatHelmet", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatChestplate", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatLeggings", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatBoots", 1L, 32767));

		// Make Nano a hazmat suit
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorNanoHelmet:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorNanoChestplate:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorNanoLegs:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorNanoBoots:27", 1));

		Logger.INFO("[Hazmat] Registered Nano as hazmat gear.");

		// Make Quantum a hazmat suit
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorQuantumHelmet:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorQuantumChestplate:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorQuantumLegs:27", 1));
		addProtection(ItemUtils.getItemStackFromFQRN("IC2:itemArmorQuantumBoots:27", 1));
		Logger.INFO("[Hazmat] Registered Quantum as hazmat gear.");

		Utils.registerEvent(mInstance);
		Logger.INFO("[Hazmat] Registered Tooltip handler for hazmat gear.");
		mInit = true;

	}

	private final static String mToolTipText = "Provides protection from:";

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		//Logger.INFO("Ticking Hazmat handler");
		if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED
				&& GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.SERVER_START) {
			//Logger.INFO("[Hazmat] Bad Phase : " + GTplusplus.CURRENT_LOAD_PHASE);
			return;
		}
		if (event.itemStack == null || isVanillaHazmatPiece(event.itemStack)) {
			//Logger.INFO("[Hazmat] Invalid Itemstack or vanilla hazmat");
			return;
		} else {
			ItemStack aStackTemp = event.itemStack;
			GT_ItemStack aStack = new GT_ItemStack(aStackTemp);
			if (isNanoArmourPiece(aStackTemp) || isQuantumArmourPiece(aStackTemp)) {
				event.toolTip.add(EnumChatFormatting.DARK_PURPLE+"Provides full hazmat protection.");				
			}
			else {
				//Logger.INFO("[Hazmat] Finding Tooltip Data");
				String[] aTooltips = getTooltips(aStack);
				if (aTooltips == null || aTooltips.length == 0) {
					//Logger.INFO("[Hazmat] No Info!");
					return;
				} else {
					//Logger.INFO("[Hazmat] Found Tooltips!");
					if (providesProtection(aStackTemp)) {
						event.toolTip.add(EnumChatFormatting.LIGHT_PURPLE+"Provides full hazmat protection.");
					} else {
						event.toolTip.add(mToolTipText);
						for (String r : aTooltips) {
							event.toolTip.add(" - " + r);
						}
					}
				}
			}		
		}
	}

	/**
	 * Static function to replace
	 * {@link #ic2.core.item.armor.ItemArmorHazmat.hasCompleteHazmat(EntityLivingBase)}.
	 * Because IC2 doesn't let us register things ourself, anything registered via
	 * GT/GT++ will return true.
	 * 
	 * @param living - Entity Wearing Armour
	 * @return - Does {@link EntityLivingBase} have a full hazmat suit on?
	 */
	public static boolean hasCompleteHazmat(EntityLivingBase living) {
		// Entity is Null, cannot have Hazmat.
		if (living == null || living.isDead) {
			return false;
		} else {

			// Map All Player Armour slots
			AutoMap<ItemStack> aEquipment = new AutoMap<ItemStack>();
			for (int i = 1; i < 5; ++i) {
				ItemStack stack = living.getEquipmentInSlot(i);

				// Item is Null, cannot have full suit
				if (stack == null) {
					return false;
				} else {
					aEquipment.put(stack);
				}
			}

			// Compare Equipment to all items mapped for full hazmat.
			for (ItemStack aSlotStack : aEquipment) {
				if (!isHazmatPiece(aSlotStack)) {
					return false;
				}
			}

			// We are in some kind of full hazmat, huzzah!
			return true;
		}
	}

	/**
	 * Is this item vanilla IC2 hazmat?
	 * 
	 * @param aArmour - The Armour to provide protection.
	 * @return
	 */
	public static boolean isVanillaHazmatPiece(ItemStack aArmour) {
		return aArmour != null ? aArmour.getItem() instanceof ItemArmorHazmat : false;
	}
	
	/**
	 * Is this item vanilla IC2 Nanosuit?
	 * 
	 * @param aArmour - The Armour to provide protection.
	 * @return
	 */
	public static boolean isNanoArmourPiece(ItemStack aArmour) {
		return aArmour != null ? aArmour.getItem() instanceof ItemArmorNanoSuit : false;
	}
	
	/**
	 * Is this item vanilla IC2 Quantum?
	 * 
	 * @param aArmour - The Armour to provide protection.
	 * @return
	 */
	public static boolean isQuantumArmourPiece(ItemStack aArmour) {
		return aArmour != null ? aArmour.getItem() instanceof ItemArmorQuantumSuit : false;
	}

	/**
	 * Is this item a registered piece of full hazmat? (Provides all 6 protections)
	 * 
	 * @param aStack - The Armour to provide protection.
	 * @return
	 */
	public static boolean isHazmatPiece(ItemStack aStack) {
		return isVanillaHazmatPiece(aStack) || providesProtection(aStack);
	}

	/**
	 * Registers the {@link ItemStack} to all types of protection. Provides full
	 * hazmat protection. Frost, Fire, Bio, Gas, Radioaton & Electricity.
	 * 
	 * @param aStack - The Armour to provide protection.
	 * @return - Did we register this ItemStack properly?
	 */
	public static boolean addProtection(ItemStack aVanStack) {
		Logger.INFO("[Hazmat] Registering " + aVanStack.getDisplayName() + " for full Hazmat protection.");
		GT_ItemStack aStack = getGtStackFromVanilla(aVanStack);
		AutoMap<Boolean> aAdded = new AutoMap<Boolean>();
		aAdded.put(addProtection_Frost(aStack));
		aAdded.put(addProtection_Fire(aStack));
		aAdded.put(addProtection_Biohazard(aStack));
		aAdded.put(addProtection_Gas(aStack));
		aAdded.put(addProtection_Radiation(aStack));
		aAdded.put(addProtection_Electricty(aStack));
		for (boolean b : aAdded) {
			if (!b) {
				return false;
			}
		}
		Logger.INFO("[Hazmat] Protection added for all 6 damage types, registering to master Hazmat list.");
		sHazmatList.add(aStack);
		return true;
	}

	public static boolean addProtection_Frost(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.AQUA + "Frost");
		return addProtection_Generic(sFrostHazmatList, aStack);
	}

	public static boolean addProtection_Fire(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.DARK_RED + "Heat");
		return addProtection_Generic(sHeatHazmatList, aStack);
	}

	public static boolean addProtection_Biohazard(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.GREEN + "Biohazards");
		return addProtection_Generic(sBioHazmatList, aStack);
	}

	public static boolean addProtection_Gas(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.WHITE + "Gas");
		return addProtection_Generic(sGasHazmatList, aStack);
	}

	public static boolean addProtection_Radiation(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.DARK_GREEN + "Radiation");
		return addProtection_Generic(sRadioHazmatList, aStack);
	}

	public static boolean addProtection_Electricty(GT_ItemStack aStack) {
		registerTooltip(aStack, EnumChatFormatting.YELLOW + "Electricity");
		return addProtection_Generic(sElectroHazmatList, aStack);
	}

	private static boolean addProtection_Generic(GT_HashSet<GT_ItemStack> aSet, GT_ItemStack aStack) {
		int aMapSize = aSet.size();
		aSet.add(aStack);
		return aMapSize < aSet.size();
	}

	/**
	 * Does this item provide hazmat protection? (Protection against Frost, Heat,
	 * Bio, Gas, Rads, Elec) An item may return false even if it protects against
	 * all six damage types. This is because it's not actually registered as hazmat
	 * correct.
	 * 
	 * @param aStack - The item to check for protection
	 * @return
	 */
	public static boolean providesProtection(ItemStack aStack) {
		return providesProtetion_Generic(sHazmatList, aStack);
	}

	public static boolean providesProtetion_Frost(ItemStack aStack) {
		return providesProtetion_Generic(sFrostHazmatList, aStack);
	}

	public static boolean providesProtetion_Fire(ItemStack aStack) {
		return providesProtetion_Generic(sHeatHazmatList, aStack);
	}

	public static boolean providesProtetion_Biohazard(ItemStack aStack) {
		return providesProtetion_Generic(sBioHazmatList, aStack);
	}

	public static boolean providesProtetion_Gas(ItemStack aStack) {
		return providesProtetion_Generic(sGasHazmatList, aStack);
	}

	public static boolean providesProtetion_Radiation(ItemStack aStack) {
		return providesProtetion_Generic(sRadioHazmatList, aStack);
	}

	public static boolean providesProtetion_Electricity(ItemStack aStack) {
		return providesProtetion_Generic(sElectroHazmatList, aStack);
	}

	private static boolean providesProtetion_Generic(GT_HashSet aSet, ItemStack aStack) {
		if (isVanillaHazmatPiece(aStack)) {
			return true;
		}
		return aSet.getMap().containsKey(aStack);
	}

	private static String[] getTooltips(GT_ItemStack aStack) {
		String aKey = convertGtItemstackToStringData(aStack);
		AutoMap<String> aTempTooltipData = mToolTips.get(aKey);
		if (aTempTooltipData == null) {
			//Logger.INFO("[Hazmat] Item was not mapped for TTs - "+aKey);
			return new String[] {};
		} else {
			//Logger.INFO("[Hazmat] Item was mapped for TTs");
			Collections.sort(aTempTooltipData);
			//Logger.INFO("[Hazmat] Sorted TTs");
			return aTempTooltipData.toArray();
		}
	}

	private static void registerTooltip(GT_ItemStack aStack, String aTooltip) {
		String aKey = convertGtItemstackToStringData(aStack);
		Logger.INFO("[Hazmat] Mapping " + aTooltip + " for " + aKey);
		AutoMap<String> aTempTooltipData = mToolTips.get(aKey);
		if (aTempTooltipData == null) {
			Logger.INFO("No data mapped yet, creating.");
			aTempTooltipData = new AutoMap<String>();
		}
		aTempTooltipData.add(aTooltip);
		mToolTips.put(convertGtItemstackToStringData(aStack), aTempTooltipData);
	}

	public static ItemStack getStackFromGtStack(GT_ItemStack aGtStack) {
		return ItemUtils.simpleMetaStack(aGtStack.mItem, aGtStack.mMetaData, aGtStack.mStackSize);
	}

	public static GT_ItemStack getGtStackFromVanilla(ItemStack aStack) {
		return new GT_ItemStack(aStack);
	}

	private static String convertGtItemstackToStringData(GT_ItemStack aStack) {
		if (aStack == null) {
			return "NULL";
		} else {
			return aStack.mItem.getUnlocalizedName() + "." + aStack.mMetaData + "." + aStack.mStackSize;
		}
	}
	
	private static String convertGtItemstackToStringDataIgnoreDamage(GT_ItemStack aStack) {
		if (aStack == null) {
			return "NULL";
		} else {
			return aStack.mItem.getUnlocalizedName() + "." + aStack.mStackSize;
		}
	}

}
