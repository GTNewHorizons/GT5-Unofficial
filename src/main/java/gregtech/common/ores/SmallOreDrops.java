package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTUtility;

public enum SmallOreDrops {

    gemExquisite(OrePrefixes.gemExquisite, OrePrefixes.gem, 1),
    gemFlawless(OrePrefixes.gemFlawless, OrePrefixes.gem, 2),
    gem(OrePrefixes.gem, null, 12),
    gemFlawed(OrePrefixes.gemFlawed, OrePrefixes.crushed, 5),
    crushed(OrePrefixes.crushed, null, 10),
    gemChipped(OrePrefixes.gemChipped, OrePrefixes.dustImpure, 5),
    dustImpure(OrePrefixes.dustImpure, null, 10);

    public static final List<SmallOreDrops> DROPS = ImmutableList.copyOf(values());

    private final OrePrefixes primary;
    private final OrePrefixes fallback;
    private final int weight;

    SmallOreDrops(OrePrefixes primary, OrePrefixes fallback, int weight) {
        this.primary = primary;
        this.fallback = fallback;
        this.weight = weight;
    }

    public static ArrayList<ItemStack> getDropList(Material material) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        for (SmallOreDrops drop : DROPS) {
            ItemStack fallback = drop.fallback == null ? null : MaterialParts.stack(drop.fallback, material, 1L);
            ItemStack primary = GTUtility.copyAmount(1L, MaterialParts.stack(drop.primary, material, 1), fallback);

            if (primary != null) {
                for (int i = 0; i < drop.weight; i++) {
                    drops.add(primary);
                }
            }
        }

        return drops;
    }
}
