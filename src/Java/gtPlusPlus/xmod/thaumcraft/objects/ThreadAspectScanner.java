package gtPlusPlus.xmod.thaumcraft.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.FileUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ThreadAspectScanner extends Thread {

	public static boolean mDoWeScan = false;
	private static final Map<String, AutoMap<ItemStack>> mAllGameContent = new HashMap<String, AutoMap<ItemStack>>();
	public final File mAspectCacheFile;

	public ThreadAspectScanner() {
		mAspectCacheFile = FileUtils.getFile("config/GTplusplus", "AspectInfo", "txt");
		mDoWeScan = true;		
	}

	private void tryCacheObject(ItemStack aStack) {
		if (aStack == null) {
			return;
		}
		String nameKey;
		try {
			nameKey = aStack.getUnlocalizedName();
		} catch (NullPointerException n) {
			try {
				nameKey = Utils.sanitizeString(aStack.getDisplayName().toLowerCase());
			} catch (NullPointerException n2) {
				try {
					nameKey = aStack.getItem().getUnlocalizedName();
				} catch (NullPointerException n3) {
					nameKey = "BadItemsGalore";
				}
			}
		}
		AutoMap<ItemStack> m = new AutoMap<ItemStack>();
		if (mAllGameContent.containsKey(nameKey)) {
			m = mAllGameContent.get(nameKey);
		}
		m.put(aStack);
		mAllGameContent.put(nameKey, m);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void run() {
		if (mDoWeScan) {
			Iterator iterator;
			Logger.INFO("Finding Blocks and Items to scan for Aspect data.");
			long mBlocksCounter = 0;
			long mItemsCounter = 0;

			// First, find blocks
			iterator = Block.blockRegistry.getKeys().iterator();
			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				Block block = (Block) Block.blockRegistry.getObject(s);
				if (block != null) {
					tryCacheObject(ItemUtils.getSimpleStack(block));
					mBlocksCounter++;
				}
			}
			Logger.INFO("Completed Block Scan. Counted "+mBlocksCounter);

			// Second Find items, Skipping things that exist.
			iterator = Item.itemRegistry.getKeys().iterator();
			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				Item item = (Item) Item.itemRegistry.getObject(s);
				if (item != null) {
					if (item.getHasSubtypes()) {
						List q1 = new ArrayList();
						item.getSubItems(item, item.getCreativeTab(), q1);
						if (q1 != null && q1.size() > 0) {
							for (int e = 0; e < q1.size(); e++) {
								ItemStack check = ItemUtils.simpleMetaStack(item, e, 1);
								if (check != null) {
									tryCacheObject(check);
									mItemsCounter++;
								}
							}
						} else {
							tryCacheObject(ItemUtils.getSimpleStack(item));
							mItemsCounter++;
						}
					} else {
						tryCacheObject(ItemUtils.getSimpleStack(item));
						mItemsCounter++;
					}
				}
			}
			Logger.INFO("Completed Item Scan. Counted "+mItemsCounter);

			Set<String> y = mAllGameContent.keySet();
			Logger.INFO("Beginning iteration of "+y.size()+" itemstacks for aspect information.");

			for (String key : y) {
				//Logger.INFO("Looking for key: "+key);
				if (mAllGameContent.containsKey(key)) {
					AutoMap<ItemStack> group = mAllGameContent.get(key);
					if (group == null || group.size() <= 0) {
						continue;
					}
					for (ItemStack stack : group) {
						thaumcraft.api.aspects.AspectList a = thaumcraft.common.lib.crafting.ThaumcraftCraftingManager
								.getObjectTags(stack);
						if (a == null) {
							continue;
						} else {
							AutoMap<Pair<String, Integer>> aspectPairs = new AutoMap<Pair<String, Integer>>();
							for (thaumcraft.api.aspects.Aspect c : a.getAspectsSortedAmount()) {
								if (c != null) {
									aspectPairs.put(new Pair<String, Integer>(c.getName(), a.getAmount(c)));
								}
							}
							try {
								List<String> mList = new ArrayList<String>();
								mList.add(stack.getDisplayName() + " | Meta: " + stack.getItemDamage()
								+ " | Unlocal: " + stack.getUnlocalizedName());
								for (Pair<String, Integer> r : aspectPairs) {
									if (r != null) {
										mList.add(r.getKey() + " x" + r.getValue());
									}
								}
								mList.add("");
								if (mAspectCacheFile != null && mList.size() >= 3) {
									FileUtils.appendListToFile(mAspectCacheFile, mList);
								}
							}
							catch (Throwable t) {
								Logger.INFO("Error while iterating one item. "+t);
							}
						}
					}
				}
			}
			Logger.INFO("Completed Aspect Iteration. AspectInfo.txt is now available to process in the GTplusplus configuration folder.");
			CommandDumpAspects.mLastScanTime = System.currentTimeMillis();
		}
		return;
	}

}
