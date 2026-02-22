package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

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

    private SmallOreDrops(OrePrefixes primary, OrePrefixes fallback, int weight) {
        this.primary = primary;
        this.fallback = fallback;
        this.weight = weight;
    }

    public static ArrayList<ItemStack> getDropList(Materials material) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        for (SmallOreDrops drop : DROPS) {
            ItemStack fallback = drop.fallback == null ? null : GTOreDictUnificator.get(drop.fallback, material, 1L);
            ItemStack primary = GTOreDictUnificator.get(drop.primary, material, fallback, 1L);

            if (primary != null) {
                for (int i = 0; i < drop.weight; i++) {
                    drops.add(primary);
                }
            }
        }

        return drops;
    }
}
