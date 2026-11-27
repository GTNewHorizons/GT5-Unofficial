package gregtech.loaders.postload.postea;

import static gregtech.loaders.postload.postea.Transformers.remapItem;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.material.MaterialParts;
import gregtech.loaders.postload.ForeignPartTable;

final class ForeignPartTransformers {

    private ForeignPartTransformers() {}

    static void register() {
        Map<String, Map<Integer, ForeignPartTable.Entry>> rowsByItem = new HashMap<>();
        for (ForeignPartTable.Entry entry : ForeignPartTable.ENTRIES) {
            rowsByItem.computeIfAbsent(entry.registryName(), k -> new HashMap<>())
                .put(entry.meta(), entry);
        }
        rowsByItem.forEach((registryName, rows) -> remapItem(registryName, damage -> {
            ForeignPartTable.Entry entry = rows.get(damage);
            return entry == null ? null : resolveForeignPartStack(entry);
        }));
    }

    static ItemStack resolveForeignPartStack(ForeignPartTable.Entry entry) {
        return MaterialParts.stack(entry.prefix(), MaterialLibAPI.getMaterial("gregtech", entry.materialName()), 1);
    }
}
