package gregtech.api.registries;

import net.minecraft.item.Item;

import gregtech.common.items.MetaGeneratedItem01;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class RemovedMetaRegistry {

    private static final Short2ObjectOpenHashMap<IntOpenHashSet> _registry = new Short2ObjectOpenHashMap<>();

    public static void init() {
        _registry.clear();
        // Muffler Upgrade (IDMetaItem01 = 727)
        addItem((short) Item.getIdFromItem(MetaGeneratedItem01.INSTANCE), MetaGeneratedItem01.INSTANCE.mOffset + 727);
    }

    /**
     *
     * @param itemID item id to be removed
     * @param meta   metadata value of item to be removed
     */
    private static void addItem(short itemID, int meta) {
        var key = _registry.computeIfAbsent(itemID, (key1 -> new IntOpenHashSet()));
        key.add(meta);
    }

    /**
     * @param itemID
     * @param meta
     * @return if itemID + meta is in registry
     */
    public static boolean contains(short itemID, int meta) {
        if (!_registry.containsKey(itemID)) return false;

        return _registry.get(itemID)
            .contains(meta);
    }
}
