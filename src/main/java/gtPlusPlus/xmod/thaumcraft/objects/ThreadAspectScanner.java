package gtPlusPlus.xmod.thaumcraft.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.util.StringUtils;
import gtPlusPlus.core.util.data.FileUtils;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;

public class ThreadAspectScanner extends Thread {

    public static boolean mDoWeScan = false;
    private static final Map<String, ArrayList<ItemStack>> mAllGameContent = new HashMap<>();
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
                nameKey = StringUtils.sanitizeString(
                    aStack.getDisplayName()
                        .toLowerCase());
            } catch (NullPointerException n2) {
                try {
                    nameKey = aStack.getItem()
                        .getUnlocalizedName();
                } catch (NullPointerException n3) {
                    nameKey = "BadItemsGalore";
                }
            }
        }
        ArrayList<ItemStack> m = new ArrayList<>();
        if (mAllGameContent.containsKey(nameKey)) {
            m = mAllGameContent.get(nameKey);
        }
        m.add(aStack);
        mAllGameContent.put(nameKey, m);
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void run() {
        if (mDoWeScan) {
            Iterator iterator;
            long mBlocksCounter = 0;
            long mItemsCounter = 0;

            // First, find blocks
            iterator = Block.blockRegistry.getKeys()
                .iterator();
            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                Block block = (Block) Block.blockRegistry.getObject(s);
                if (block != null) {
                    tryCacheObject(new ItemStack(block));
                    mBlocksCounter++;
                }
            }

            // Second Find items, Skipping things that exist.
            iterator = Item.itemRegistry.getKeys()
                .iterator();
            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                Item item = (Item) Item.itemRegistry.getObject(s);
                if (item != null) {
                    if (item.getHasSubtypes()) {
                        List q1 = new ArrayList();
                        item.getSubItems(item, item.getCreativeTab(), q1);
                        if (q1 != null && !q1.isEmpty()) {
                            for (int e = 0; e < q1.size(); e++) {
                                ItemStack check = new ItemStack(item, 1, e);
                                if (check != null) {
                                    tryCacheObject(check);
                                    mItemsCounter++;
                                }
                            }
                        } else {
                            tryCacheObject(new ItemStack(item));
                            mItemsCounter++;
                        }
                    } else {
                        tryCacheObject(new ItemStack(item));
                        mItemsCounter++;
                    }
                }
            }

            Set<String> y = mAllGameContent.keySet();

            for (String key : y) {
                if (mAllGameContent.containsKey(key)) {
                    ArrayList<ItemStack> group = mAllGameContent.get(key);
                    if (group == null || group.size() == 0) {
                        continue;
                    }
                    for (ItemStack stack : group) {
                        thaumcraft.api.aspects.AspectList a = thaumcraft.common.lib.crafting.ThaumcraftCraftingManager
                            .getObjectTags(stack);
                        if (a != null) {
                            ArrayList<Pair<String, Integer>> aspectPairs = new ArrayList<>();
                            for (thaumcraft.api.aspects.Aspect c : a.getAspectsSortedAmount()) {
                                if (c != null) {
                                    aspectPairs.add(Pair.of(c.getName(), a.getAmount(c)));
                                }
                            }
                            try {
                                List<String> mList = new ArrayList<>();
                                mList.add(
                                    stack.getDisplayName() + " | Meta: "
                                        + stack.getItemDamage()
                                        + " | Unlocal: "
                                        + stack.getUnlocalizedName());
                                for (Pair<String, Integer> r : aspectPairs) {
                                    if (r != null) {
                                        mList.add(r.getKey() + " x" + r.getValue());
                                    }
                                }
                                mList.add("");
                                if (mAspectCacheFile != null && mList.size() >= 3) {
                                    FileUtils.appendListToFile(mAspectCacheFile, mList);
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
            CommandDumpAspects.mLastScanTime = System.currentTimeMillis();
        }
    }
}
