package gregtech.common.render;

import java.util.*;

import fox.spiteful.avaritia.render.CosmicItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

public class CosmicItemRendererGT extends CosmicItemRenderer {

	private static CosmicItemRendererGT sInstance = null;
	public static HashMap<Item, List<Integer>> sCosmicItemRendererGtMapping = new HashMap<Item, List<Integer>>();
	private static boolean sInit = false;

	public static void registerItemWithMeta(Item aItem, List<Integer> aMeta) {
		if (aItem != null) {
			if (aMeta == null || aMeta.isEmpty()) {
				ArrayList<Integer> aWildCard = new ArrayList<Integer>();
				aWildCard.add(OreDictionary.WILDCARD_VALUE);
				sCosmicItemRendererGtMapping.put(aItem, aWildCard);
			}
			else {
				sCosmicItemRendererGtMapping.put(aItem, aMeta);
			}
		}
	}

	public static void registerItemWithMeta(Item aItem, int aMeta) {
		if (aItem != null) {
			ArrayList<Integer> aSingleMeta = new ArrayList<Integer>();
			aSingleMeta.add(aMeta);
			sCosmicItemRendererGtMapping.put(aItem, aSingleMeta);
		}
	}

	public static void init() {
		if (sInstance == null) {
			sInstance = new CosmicItemRendererGT();
		}
		if (!sInit) {
			for (Item aItem : sCosmicItemRendererGtMapping.keySet()) { MinecraftForgeClient.registerItemRenderer(aItem, sInstance); }
			sInit = true;
		}
	}

	private boolean isSupported(ItemStack aStack) {
		List<Integer> aMeta = sCosmicItemRendererGtMapping.get(aStack.getItem());
		if (aMeta != null && !aMeta.isEmpty()) {

			for (int meta : aMeta) {
				if (meta == OreDictionary.WILDCARD_VALUE) {
					return true;
				}
				else {
					if (aStack.getItemDamage() == meta) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (!isSupported(item)) {
			return;
		}
		super.renderItem(type, item, data);
	}
	
}
