package gtPlusPlus.xmod.thaumcraft;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectCompat;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HANDLER_Thaumcraft {	

	public static GTPP_AspectCompat sThaumcraftCompat;
	public static Item mResearchNotes;
	public static final AutoMap<Pair<ItemStack, GTPP_AspectStack[]>> sItemsToGetAspects = new AutoMap<Pair<ItemStack, GTPP_AspectStack[]>>();
	
	public static void preInit(){
		if (LoadedMods.Thaumcraft){
		}
	}

	public static void init(){
		if (LoadedMods.Thaumcraft){
			try {
				mResearchNotes = (Item) ReflectionUtils.getField(ReflectionUtils.getClass("thaumcraft.common.config.ConfigItems"), "itemResearchNotes").get(null);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				mResearchNotes = Items.paper;
			}
		}
	}

	public static void postInit(){
		if (LoadedMods.Thaumcraft){
			//Add Custom Aspects	
			
			
			//sThaumcraftCompat = (IThaumcraftCompat) GT_Utility.callConstructor("gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectCompat", 0, null, GT_Values.D1, new Object[0]);
			//sThaumcraftCompat = new GTPP_AspectCompat();	
			
			/*if (!sItemsToGetAspects.isEmpty() && false) {
				for (Pair<ItemStack, GTPP_AspectStack[]> j : sItemsToGetAspects) {
					if (j .getKey() != null && (j.getValue() != null && j.getValue().length > 0)) {
						List<GTPP_AspectStack> list = Arrays.asList(j.getValue());
						if (ThaumcraftUtils.registerThaumcraftAspectsToItem(j.getKey(), list, true)) {
							Logger.WARNING("[Aspect] Successfully added Aspects to "+j.getKey().getDisplayName()+".");
						}
						else {
							Logger.WARNING("[Aspect] Failed adding Aspects to "+j.getKey().getDisplayName()+".");
						}
					}
				}
			}*/
		}
	}
	
}
