package gtPlusPlus.xmod.thaumcraft.objects.wrapper.research;

import cpw.mods.fml.common.FMLLog;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;

public class TC_ResearchCategories_Wrapper {
	
	public static LinkedHashMap<String, TC_ResearchCategoryList_Wrapper> researchCategories = new LinkedHashMap<String, TC_ResearchCategoryList_Wrapper>();

	public static TC_ResearchCategoryList_Wrapper getResearchList(String key) {
		return (TC_ResearchCategoryList_Wrapper) researchCategories.get(key);
	}

	public static String getCategoryName(String key) {
		return StatCollector.translateToLocal("tc.research_category." + key);
	}

	public static TC_ResearchItem_Wrapper getResearch(String key) {
		Collection rc = researchCategories.values();
		Iterator i$ = rc.iterator();

		while (i$.hasNext()) {
			Object cat = i$.next();
			Collection rl = ((TC_ResearchCategoryList_Wrapper) cat).research.values();
			Iterator i$1 = rl.iterator();

			while (i$1.hasNext()) {
				Object ri = i$1.next();
				if (((TC_ResearchItem_Wrapper) ri).key.equals(key)) {
					return (TC_ResearchItem_Wrapper) ri;
				}
			}
		}

		return null;
	}

	public static void registerCategory(String key, ResourceLocation icon, ResourceLocation background) {
		if (getResearchList(key) == null) {
			TC_ResearchCategoryList_Wrapper rl = new TC_ResearchCategoryList_Wrapper(icon, background);
			researchCategories.put(key, rl);
		}

	}

	public static void addResearch(TC_ResearchItem_Wrapper ri) {
		TC_ResearchCategoryList_Wrapper rl = getResearchList(ri.category);
		if (rl != null && !rl.research.containsKey(ri.key)) {
			if (!ri.isVirtual()) {
				Iterator i$ = rl.research.values().iterator();

				while (i$.hasNext()) {
					TC_ResearchItem_Wrapper rr = (TC_ResearchItem_Wrapper) i$.next();
					if (rr.displayColumn == ri.displayColumn && rr.displayRow == ri.displayRow) {
						FMLLog.log(Level.FATAL,
								"[Thaumcraft] Research [" + ri.getName()
										+ "] not added as it overlaps with existing research [" + rr.getName() + "]",
								new Object[0]);
						return;
					}
				}
			}

			rl.research.put(ri.key, ri);
			if (ri.displayColumn < rl.minDisplayColumn) {
				rl.minDisplayColumn = ri.displayColumn;
			}

			if (ri.displayRow < rl.minDisplayRow) {
				rl.minDisplayRow = ri.displayRow;
			}

			if (ri.displayColumn > rl.maxDisplayColumn) {
				rl.maxDisplayColumn = ri.displayColumn;
			}

			if (ri.displayRow > rl.maxDisplayRow) {
				rl.maxDisplayRow = ri.displayRow;
			}
		}

	}
}