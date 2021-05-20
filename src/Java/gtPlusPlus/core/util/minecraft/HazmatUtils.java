package gtPlusPlus.core.util.minecraft;

import static gregtech.api.GregTech_API.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.objects.GT_HashSet;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import ic2.core.Ic2Items;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HazmatUtils {

	public static final GT_HashSet<GT_ItemStack> sHazmatList = new GT_HashSet<GT_ItemStack>();

	private static final HashMap<String, AutoMap<String>> mToolTips = new HashMap<String, AutoMap<String>>();

	private static boolean mInit = false;
	private static HazmatUtils mInstance;

	@SuppressWarnings("rawtypes")
	public static void init() {
		if (mInit) {
			return;
		}

		mInstance = new HazmatUtils();

		sHazmatList.add(ItemUtils.getSimpleStack(Ic2Items.hazmatHelmet, 1));
		sHazmatList.add(ItemUtils.getSimpleStack(Ic2Items.hazmatChestplate, 1));
		sHazmatList.add(ItemUtils.getSimpleStack(Ic2Items.hazmatLeggings, 1));
		sHazmatList.add(ItemUtils.getSimpleStack(Ic2Items.hazmatBoots, 1));

		// Make Nano a hazmat suit
		// Make Quantum a hazmat suit		
		

		if (LoadedMods.IndustrialCraft2 || LoadedMods.IndustrialCraft2Classic) {
			AutoMap<ItemStack> aVanillaIC2Armour = new AutoMap<ItemStack>();
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.nanoHelmet, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.nanoBodyarmor, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.nanoLeggings, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.nanoBoots, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.quantumHelmet, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.quantumBodyarmor, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.quantumLeggings, 1));
			aVanillaIC2Armour.add(ItemUtils.getSimpleStack(Ic2Items.quantumBoots, 1));
			for (ItemStack aItem : aVanillaIC2Armour) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered IC2 Items as hazmat gear.");
		}
		
		if (LoadedMods.isModLoaded("EMT")) {
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsEMT = ReflectionUtils.getClass("emt.init.EMTItems");			
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "nanoThaumicHelmet"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "nanoWing"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "nanoBootsTraveller"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "quantumThaumicHelmet"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "quantumWing"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "quantumArmor"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "quantumBootsTraveller"));			
			AutoMap<ItemStack> aEMT = new AutoMap<ItemStack>();
			for (Field aItemField : aItemFields) {
				Item aItemObject = null;
				if (aItemField != null) {
					try {
						aItemObject = (Item) aItemField.get(null);
					}
					catch (Exception t) {
						t.printStackTrace();
					}
				}
				if (aItemObject != null) {
					aEMT.add(ItemUtils.getSimpleStack(aItemObject));
				}
				else {
					Logger.INFO("[Hazmat] Could not get "+aItemField.getName()+" from "+aItemsEMT.getName());					
				}
			}
			Logger.INFO("[Hazmat] Registering "+aEMT.size()+" EMT Items as hazmat gear.");
			for (ItemStack aItem : aEMT) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered EMT Items as hazmat gear.");
		}		
		
		if (LoadedMods.isModLoaded("DraconicEvolution")) {
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsDE = ReflectionUtils.getClass("com.brandon3055.draconicevolution.ModItems");		
			
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "draconicHelm"));
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "draconicChest"));
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "draconicLeggs"));
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "draconicBoots"));
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "wyvernHelm"));
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "wyvernChest"));	
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "wyvernLeggs"));	
			aItemFields.add(ReflectionUtils.getField(aItemsDE, "wyvernBoots"));		
			AutoMap<ItemStack> aItemMap = new AutoMap<ItemStack>();			
			for (Field aItemField : aItemFields) {
				Item aItemObject = null;
				if (aItemField != null) {
					try {
						aItemObject = (Item) aItemField.get(null);
					}
					catch (Exception t) {
						t.printStackTrace();
					}
				}
				if (aItemObject != null) {
					aItemMap.add(ItemUtils.getSimpleStack(aItemObject));
				}
				else {
					Logger.INFO("[Hazmat] Could not get "+aItemField.getName()+" from "+aItemsDE.getName());					
				}
			}
			Logger.INFO("[Hazmat] Registering "+aItemMap.size()+" Draconic Evolution Items as hazmat gear.");
			for (ItemStack aItem : aItemMap) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Draconic Evolution Items as hazmat gear.");
		}	
		
		if (LoadedMods.isModLoaded("TaintedMagic")) {
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsTaintedMagic = ReflectionUtils.getClass("taintedmagic.common.registry.ItemRegistry");		
			
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemShadowFortressHelmet"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemShadowFortressChestplate"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemShadowFortressLeggings"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemVoidwalkerBoots"));	
			AutoMap<ItemStack> aItemMap = new AutoMap<ItemStack>();			
			for (Field aItemField : aItemFields) {
				Item aItemObject = null;
				if (aItemField != null) {
					try {
						aItemObject = (Item) aItemField.get(null);
					}
					catch (Exception t) {
						t.printStackTrace();
					}
				}
				if (aItemObject != null) {
					aItemMap.add(ItemUtils.getSimpleStack(aItemObject));
				}
				else {
					Logger.INFO("[Hazmat] Could not get "+aItemField.getName()+" from "+aItemsTaintedMagic.getName());					
				}
			}
			Logger.INFO("[Hazmat] Registering "+aItemMap.size()+" Tainted Magic Items as hazmat gear.");
			for (ItemStack aItem : aItemMap) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Tainted Magic Items as hazmat gear.");
		}	
		
		if (LoadedMods.isModLoaded("WitchingGadgets")) {
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsTaintedMagic = ReflectionUtils.getClass("witchinggadgets.common.WGContent");		
			
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemPrimordialHelm"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemPrimordialChest"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemPrimordialLegs"));
			aItemFields.add(ReflectionUtils.getField(aItemsTaintedMagic, "ItemPrimordialBoots"));	
			AutoMap<ItemStack> aItemMap = new AutoMap<ItemStack>();			
			for (Field aItemField : aItemFields) {
				Item aItemObject = null;
				if (aItemField != null) {
					try {
						aItemObject = (Item) aItemField.get(null);
					}
					catch (Exception t) {
						t.printStackTrace();
					}
				}
				if (aItemObject != null) {
					aItemMap.add(ItemUtils.getSimpleStack(aItemObject));
				}
				else {
					Logger.INFO("[Hazmat] Could not get "+aItemField.getName()+" from "+aItemsTaintedMagic.getName());					
				}
			}
			Logger.INFO("[Hazmat] Registering "+aItemMap.size()+" Witching Gadgets Items as hazmat gear.");
			for (ItemStack aItem : aItemMap) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Witching Gadgets Items as hazmat gear.");
		}	
		
		if (LoadedMods.isModLoaded("ThaumicTinkerer")) {
			/*
			AutoMap<Item> aItems = new AutoMap<Item>();
			Class aMainTT = ReflectionUtils.getClass("thaumic.tinkerer.common.ThaumicTinkerer");		
			Class aItemRegistryTT = ReflectionUtils.getClass("thaumic.tinkerer.common.registry.TTRegistry");
			Field aRegistryInstance = ReflectionUtils.getField(aMainTT, "registry");
			Object aRegistry = ReflectionUtils.getFieldValue(aRegistryInstance);
			Method aFuckingStupidMethodHandlingMethod = ReflectionUtils.getMethod(aItemRegistryTT, "getFirstItemFromClass", new Class[] {Class.class});
			Item aIchorHelm = (Item) ReflectionUtils.invokeNonBool(aRegistry, aFuckingStupidMethodHandlingMethod, new Object[] {ReflectionUtils.getClass("thaumic.tinkerer.common.item.kami.armor.ItemGemHelm")});
			Item aIchorChest = (Item) ReflectionUtils.invokeNonBool(aRegistry, aFuckingStupidMethodHandlingMethod, new Object[] {ReflectionUtils.getClass("thaumic.tinkerer.common.item.kami.armor.ItemGemChest")});
			Item aIchorLegs = (Item) ReflectionUtils.invokeNonBool(aRegistry, aFuckingStupidMethodHandlingMethod, new Object[] {ReflectionUtils.getClass("thaumic.tinkerer.common.item.kami.armor.ItemGemLegs")});
			Item aIchorBoots = (Item) ReflectionUtils.invokeNonBool(aRegistry, aFuckingStupidMethodHandlingMethod, new Object[] {ReflectionUtils.getClass("thaumic.tinkerer.common.item.kami.armor.ItemGemBoots")});
			aItems.add(aIchorHelm);
			aItems.add(aIchorChest);
			aItems.add(aIchorLegs);
			aItems.add(aIchorBoots);			
			AutoMap<ItemStack> aItemMap = new AutoMap<ItemStack>();	
			int aIndex = 0;
			for (Item aItem : aItems) {
			Item aItemObject = null;
			if (aItem != null) {
			aItemMap.add(ItemUtils.getSimpleStack(aItemObject));
			}
			else {
			Logger.INFO("[Hazmat] Could not get item "+aIndex+" from "+aItemRegistryTT.getName());					
			}
			aIndex++;
			}
			Logger.INFO("[Hazmat] Registering "+aItemMap.size()+" Thaumic Tinkerer Items as hazmat gear.");
			for (ItemStack aItem : aItemMap) {	
			addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Thaumic Tinkerer Items as hazmat gear.");
			*/
			Logger.INFO("[Hazmat] Did not register Thaumic Tinkerer Items as hazmat gear.");
			}
		
		if (LoadedMods.isModLoaded("GraviSuite")) {			
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsGravisuite = ReflectionUtils.getClass("gravisuite.GraviSuite");			
			aItemFields.add(ReflectionUtils.getField(aItemsGravisuite, "advNanoChestPlate"));
			aItemFields.add(ReflectionUtils.getField(aItemsGravisuite, "graviChestPlate"));			
			AutoMap<ItemStack> aGravisuite = new AutoMap<ItemStack>();
			for (Field aItemField : aItemFields) {
				Item aItemObject = (Item) ReflectionUtils.getFieldValue(aItemField);
				if (aItemObject != null) {
					aGravisuite.add(ItemUtils.getSimpleStack(aItemObject));
				}
			}
			Logger.INFO("[Hazmat] Registering "+aGravisuite.size()+" Gravisuit Items as hazmat gear.");
			for (ItemStack aItem : aGravisuite) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Gravisuit Items as hazmat gear.");
		}
		
		if (LoadedMods.isModLoaded("AdvancedSolarPanel")) {
			AutoMap<Field> aItemFields = new AutoMap<Field>();
			Class aItemsEMT = ReflectionUtils.getClass("advsolar.common.AdvancedSolarPanel");
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "advancedSolarHelmet"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "hybridSolarHelmet"));
			aItemFields.add(ReflectionUtils.getField(aItemsEMT, "ultimateSolarHelmet"));			
			AutoMap<ItemStack> aASP = new AutoMap<ItemStack>();
			for (Field aItemField : aItemFields) {
				Item aItemObject = (Item) ReflectionUtils.getFieldValue(aItemField);
				if (aItemObject != null) {
					aASP.add(ItemUtils.getSimpleStack(aItemObject));
				}
			}
			Logger.INFO("[Hazmat] Registering "+aASP.size()+" Adv. Solar Items as hazmat gear.");
			for (ItemStack aItem : aASP) {	
				addProtection(aItem);
			}
			Logger.INFO("[Hazmat] Registered Adv. Solar Items as hazmat gear.");
		}	

		Utils.registerEvent(mInstance);
		Logger.INFO("[Hazmat] Registered Tooltip handler for hazmat gear.");
		mInit = true;

	}

	private final static String mToolTipText = "Provides protection from:";

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		//Logger.INFO("Ticking Hazmat handler");
		if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
			
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
					//Logger.INFO("Found item which is not hazmat. "+ItemUtils.getItemName(aSlotStack));
					return false;
				}
			}

			// We are in some kind of full hazmat, huzzah!
			//Logger.INFO("Has full hazmat.");
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
		if (!ItemUtils.checkForInvalidItems(aVanStack)) {
			Logger.INFO("=================Bad Hazmat Addition======================");
			Logger.INFO("Called from: "+ReflectionUtils.getMethodName(0));
			Logger.INFO(ReflectionUtils.getMethodName(1));
			Logger.INFO(ReflectionUtils.getMethodName(2));
			Logger.INFO(ReflectionUtils.getMethodName(3));
			Logger.INFO(ReflectionUtils.getMethodName(4));
			Logger.INFO(ReflectionUtils.getMethodName(5));
			Logger.INFO(ReflectionUtils.getMethodName(6));	
			Logger.INFO("==========================================================");
			return false;
		}
		Logger.INFO("[Hazmat] Registering " + ItemUtils.getItemName(aVanStack) + " for full Hazmat protection.");
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

	private static boolean providesProtetion_Generic(GT_HashSet<GT_ItemStack> aSet, ItemStack aStack) {
		if (isVanillaHazmatPiece(aStack)) {
			return true;
		}
		for (GT_ItemStack o : aSet) {
			if (o != null && o.mItem != null && aStack != null && aStack.getItem() != null) {
				if (GT_Utility.areStacksEqual(o.toStack(), aStack, true)) {
					return true;
				}
				if (o.isStackEqual(aStack)){
					return true;
				}
				if (o.mItem == aStack.getItem() && EnergyUtils.EU.isElectricItem(aStack)) {
					return true;
				}
			}
		}
		return false;
	}

	private static String[] getTooltips(GT_ItemStack aStack) {
		String aKey = convertGtItemstackToStringDataIgnoreDamage(aStack);
		AutoMap<String> aTempTooltipData = mToolTips.get(aKey);
		if (aTempTooltipData == null || aTempTooltipData.isEmpty()) {
			//Logger.INFO("[Hazmat] Item was not mapped for TTs - "+aKey);
			return new String[] {};
		} else {
			//Logger.INFO("[Hazmat] Item was mapped for TTs");
			//Collections.sort(aTempTooltipData);
			//Logger.INFO("[Hazmat] Sorted TTs");
			
			String[] mBuiltOutput = new String[aTempTooltipData.size()];
			int aIndex = 0;
			for (String i : aTempTooltipData) {
				mBuiltOutput[aIndex++] = i;
			}
			
			return mBuiltOutput;
		}
	}

	private static void registerTooltip(GT_ItemStack aStack, String aTooltip) {
		String aKey = convertGtItemstackToStringDataIgnoreDamage(aStack);
		Logger.INFO("[Hazmat] Mapping " + aTooltip + " for " + aKey);
		AutoMap<String> aTempTooltipData = mToolTips.get(aKey);
		if (aTempTooltipData == null) {
			Logger.INFO("No data mapped yet, creating.");
			aTempTooltipData = new AutoMap<String>();
			mToolTips.put(aKey, aTempTooltipData);
		}
		aTempTooltipData.add(aTooltip);
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
