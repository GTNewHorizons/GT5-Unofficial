package gregtech.api.util;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import net.minecraft.block.Block;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.VoltageIndex;

public class GlassTier {

    private static final Comparator<Pair<Integer, Integer>> tierComparator = Comparator
        .comparing(Pair<Integer, Integer>::getLeft)
        .thenComparing(Pair::getRight);
    private static final Map<Pair<Integer, Integer>, Pair<Block, Integer>> tierToGlass = new TreeMap<>(tierComparator);
    private static final HashMap<Pair<Block, Integer>, Integer> glassToTier = new HashMap<>();
    // For default tier ordering, so the primary (borosilicate) glasses come before the variants
    private static final int minTier = VoltageIndex.HV;
    private static final int maxTier = VoltageIndex.UMV;
    private static final List<Pair<Block, Integer>> mainGlass = new ArrayList<>(
        Collections.nCopies(maxTier + 1 - minTier, null));
    private static final List<Pair<Block, Integer>> glassList = new ArrayList<>();

    /**
     * @param modname              The modid owning the block
     * @param unlocalisedBlockName The name of the block itself
     * @param meta                 The meta of the block
     * @param tier                 the glasses Tier = Voltage tier (MIN 3)
     */
    public static void addCustomGlass(String modname, String unlocalisedBlockName, int meta, int tier, int subtier) {
        Block block = findBlock(modname, unlocalisedBlockName);
        if (block != null) {
            addCustomGlass(block, meta, tier, subtier);
        } else {
            new IllegalArgumentException(
                "Block: " + unlocalisedBlockName
                    + " of the Mod: "
                    + modname
                    + " was NOT found when attempting to register a glass!").printStackTrace();
        }
    }

    public static void addCustomGlass(@NotNull Block block, int meta, int tier, int subtier) {
        Objects.requireNonNull(block, "Glass block cannot be null");
        GlassTier.glassToTier.put(Pair.of(block, meta), tier);
        GlassTier.tierToGlass.put(Pair.of(tier, subtier), Pair.of(block, meta));
        if (subtier == 0) {
            mainGlass.set(tier - minTier, Pair.of(block, meta));
        }
    }

    public static HashMap<Pair<Block, Integer>, Integer> getGlassMap() {
        return glassToTier;
    }

    public static int getGlassTier(Block block, int meta) {
        return glassToTier.getOrDefault(Pair.of(block, meta), 0);
    }

    public static List<Pair<Block, Integer>> getGlassList() {
        if (glassList.isEmpty()) {
            glassList.addAll(mainGlass);
            for (Map.Entry<Pair<Integer, Integer>, Pair<Block, Integer>> entry : tierToGlass.entrySet()) {
                if (entry.getKey()
                    .getRight() == 0) continue;
                glassList.add(entry.getValue());
            }
            glassList.add(mainGlass.get(mainGlass.size() - 1));
        }
        return glassList;
    }
}
