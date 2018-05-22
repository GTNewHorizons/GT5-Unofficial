package gtPlusPlus.xmod.thaumcraft;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_Aspects.TC_AspectStack_Ex;
import gtPlusPlus.xmod.thaumcraft.util.ThaumcraftUtils;

public class HANDLER_Thaumcraft {	

	public static IThaumcraftCompat sThaumcraftCompat;
	public static final AutoMap<Pair<ItemStack, TC_AspectStack_Ex[]>> sItemsToGetAspects = new AutoMap<Pair<ItemStack, TC_AspectStack_Ex[]>>();

	static {
		sThaumcraftCompat = (IThaumcraftCompat) GT_Utility.callConstructor("gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectCompat", 0, null, GT_Values.D1, new Object[0]);
	}
	
	public static void preInit(){
		if (LoadedMods.Thaumcraft){
		}
	}

	public static void init(){
		if (LoadedMods.Thaumcraft){

		}
	}

	public static void postInit(){
		if (LoadedMods.Thaumcraft){
			if (!sItemsToGetAspects.isEmpty()) {
				for (Pair<ItemStack, TC_AspectStack_Ex[]> j : sItemsToGetAspects) {
					if (j .getKey() != null && (j.getValue() != null && j.getValue().length > 0)) {
						List<TC_AspectStack_Ex> list = Arrays.asList(j.getValue());
						if (ThaumcraftUtils.registerThaumcraftAspectsToItem(j.getKey(), list, true)) {
							Logger.INFO("[Aspect] Successfully added Aspects to "+j.getKey().getDisplayName()+".");
						}
						else {
							Logger.INFO("[Aspect] Failed adding Aspects to "+j.getKey().getDisplayName()+".");
						}
					}
				}
			}
		}
	}
	
}
