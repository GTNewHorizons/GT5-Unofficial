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
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.Utils;
import ic2.core.Ic2Items;
import ic2.core.item.armor.ItemArmorHazmat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HazmatUtils {
	

	public static final GT_HashSet<GT_ItemStack> sHazmatList = new GT_HashSet<GT_ItemStack>();	
	
	private static final HashMap<ItemStack, AutoMap<String>> mToolTips = new HashMap<ItemStack, AutoMap<String>>();
	
	
	private static boolean mInit = false;
	private static HazmatUtils mInstance;
	
	public static void init() {
		if (mInit){
			return;
		}
		
		mInstance = new HazmatUtils();
		
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatHelmet", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatChestplate", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatLeggings", 1L, 32767));
		sHazmatList.add(GT_ModHandler.getIC2Item("hazmatBoots", 1L, 32767));

		//Make Nano a hazmat suit
		addProtection(Ic2Items.nanoHelmet);
		addProtection(Ic2Items.nanoBodyarmor);
		addProtection(Ic2Items.nanoLeggings);
		addProtection(Ic2Items.nanoBoots);
		
		//Make Quantum a hazmat suit
		addProtection(Ic2Items.quantumHelmet);
		addProtection(Ic2Items.quantumBodyarmor);
		addProtection(Ic2Items.quantumLeggings);
		addProtection(Ic2Items.quantumBoots);		
		
		Utils.registerEvent(mInstance);		
		
	}
	
	private final static String mToolTipText = "Provides protection from:";
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event){	
		if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED || GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.SERVER_START) {
			return;
		}
		if (event.itemStack == null || isVanillaHazmatPiece(event.itemStack)) {
			return;
		}
		else {
			ItemStack aStack = event.itemStack;
			String[] aTooltips = getTooltips(aStack);
			if (aTooltips == null || aTooltips.length == 0) {
				return;
			}
			else {				
				if (providesProtection(aStack)) {	
					event.toolTip.add("Provides full hazmat protection.");					
				}	
				else {
					event.toolTip.add(mToolTipText);		
					for (String r : aTooltips) {
						event.toolTip.add(" - "+r);		
					}					
				}				
			}			
		}		
	}
	
	
	/**
	 * Static function to replace {@link #ic2.core.item.armor.ItemArmorHazmat.hasCompleteHazmat(EntityLivingBase)}.
	 * Because IC2 doesn't let us register things ourself, anything registered via GT/GT++ will return true.
	 * @param living - Entity Wearing Armour
	 * @return - Does {@link EntityLivingBase} have a full hazmat suit on?
	 */
	public static boolean hasCompleteHazmat(EntityLivingBase living) {		
		//Entity is Null, cannot have Hazmat.
		if (living == null || living.isDead) {
			return false;
		}
		else {
			
			//Map All Player Armour slots
			AutoMap<ItemStack> aEquipment = new AutoMap<ItemStack>();
			for (int i = 1; i < 5; ++i) {
				ItemStack stack = living.getEquipmentInSlot(i);
				
				//Item is Null, cannot have full suit
				if (stack == null) {
					return false;
				}
				else {
					aEquipment.put(stack);
				}
			}
			
			//Compare Equipment to all items mapped for full hazmat.
			for (ItemStack aSlotStack : aEquipment) {
				if (!isHazmatPiece(aSlotStack)) {
					return false;
				}
			}
			
			//We are in some kind of full hazmat, huzzah!
			return true;			
		}		
	}
	
	
	
	public static boolean isVanillaHazmatPiece(ItemStack aArmour) {
		return aArmour != null ?  aArmour.getItem() instanceof ItemArmorHazmat : false;
	}	
	
	public static boolean isHazmatPiece(ItemStack aStack) {
		return isVanillaHazmatPiece(aStack) || providesProtection(aStack);
	}
	
	/**
	 * Registers the {@link ItemStack} to all types of protection.
	 * Provides full hazmat protection. Frost, Fire, Bio, Gas, Radioaton & Electricity.
	 * @param aStack - The Armour to provide protection.
	 * @return - Did we register this ItemStack properly?
	 */
	public static boolean addProtection(ItemStack aStack) {		
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
		sHazmatList.add(aStack);
		return true;		
	}
	public static boolean addProtection_Frost(ItemStack aStack) {
		registerTooltip(aStack, "Frost");
		return addProtection_Generic(sFrostHazmatList, aStack);
	}
	public static boolean addProtection_Fire(ItemStack aStack) {
		registerTooltip(aStack, "Heat");
		return addProtection_Generic(sHeatHazmatList, aStack);
	}
	public static boolean addProtection_Biohazard(ItemStack aStack) {
		registerTooltip(aStack, "Biohazards");
		return addProtection_Generic(sBioHazmatList, aStack);
	}
	public static boolean addProtection_Gas(ItemStack aStack) {
		registerTooltip(aStack, "Gas");
		return addProtection_Generic(sGasHazmatList, aStack);
	}
	public static boolean addProtection_Radiation(ItemStack aStack) {
		registerTooltip(aStack, "Radiation");
		return addProtection_Generic(sRadioHazmatList, aStack);
	}
	public static boolean addProtection_Electricty(ItemStack aStack) {
		registerTooltip(aStack, "Electricity");
		return addProtection_Generic(sElectroHazmatList, aStack);
	}
	
	private static boolean addProtection_Generic(GT_HashSet aSet, ItemStack aStack) {
		int aMapSize = aSet.size();
		aSet.add(aStack);
		return aMapSize < aSet.size();
	}
	
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
	
	private static String[] getTooltips(ItemStack aStack) {		
		AutoMap<String> aTempTooltipData = mToolTips.get(aStack);
		if (aTempTooltipData == null) {
			return new String[] {};
		}
		else {
			Collections.sort(aTempTooltipData);
			return aTempTooltipData.toArray();
		}
	}
	
	private static void registerTooltip(ItemStack aStack, String aTooltip) {
		AutoMap<String> aTempTooltipData = mToolTips.get(aStack);
		if (aTempTooltipData == null) {
			aTempTooltipData = new AutoMap<String>();
		}
		aTempTooltipData.add(aTooltip);
	}
	
}
