package com.github.bartimaeusnek.bartworks.API;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import org.apache.commons.lang3.tuple.Pair;

/**
 * API for bartworks borosilicate glass.
 * <p>
 * You might have noticed this API does not expose any Block instance, but only IStructureElements. This is in case we
 * add more glass blocks later, and we run out of meta id for only one block.
 * <p>
 * IStructureElements returned from this class <b>SHOULD NOT</b> have its methods called before post init, or else
 * you might end up with wrong autoplace hints.
 */
public class BorosilicateGlass {

    private static Block block, block2;
    private static List<Pair<Block, Integer>> representatives;
    private static SetMultimap<Byte, Pair<Block, Integer>> allLevels;
    private static final Table<Block, Integer, Byte> allLevelsReverse = HashBasedTable.create();

    private static boolean isValidTier(int tier) {
        return tier > 0 && tier <= Byte.MAX_VALUE;
    }

    private static Block getGlassBlock() {
        if (block == null) block = GameRegistry.findBlock("bartworks", "BW_GlasBlocks");
        return block;
    }

    private static Block getGlassBlock2() {
        if (block2 == null) block2 = GameRegistry.findBlock("bartworks", "BW_GlasBlocks2");
        return block2;
    }

    private static void doRegister(
            byte level, Block block, int meta, SetMultimap<Byte, Pair<Block, Integer>> allLevels) {
        allLevels.put(level, Pair.of(block, meta));
        allLevelsReverse.put(block, meta, level);
    }

    private static SetMultimap<Byte, Pair<Block, Integer>> getAllLevels() {
        if (allLevels == null) {
            SetMultimap<Byte, Pair<Block, Integer>> ret = LinkedHashMultimap.create();
            Block block = getGlassBlock();
            doRegister((byte) 3, block, 0, ret);
            doRegister((byte) 4, block, 1, ret);
            doRegister((byte) 5, block, 12, ret);
            doRegister((byte) 5, block, 2, ret);
            doRegister((byte) 6, block, 3, ret);
            doRegister((byte) 7, block, 4, ret);
            doRegister((byte) 8, block, 5, ret);
            for (int i = 6; i < 12; i++) {
                doRegister((byte) 3, block, i, ret);
            }
            doRegister((byte) 9, block, 13, ret);
            doRegister((byte) 10, block, 14, ret);
            doRegister((byte) 11, block, 15, ret);
            block = getGlassBlock2();
            doRegister((byte) 12, block, 0, ret);
            allLevels = ret;
        }
        return allLevels;
    }

    private static List<Pair<Block, Integer>> getRepresentatives() {
        if (representatives == null) {
            SetMultimap<Byte, Pair<Block, Integer>> allLevels = getAllLevels();
            ArrayList<Pair<Block, Integer>> ret = new ArrayList<>();
            for (Byte level : new PriorityQueue<>(allLevels.keySet())) {
                ret.add(allLevels.get(level).iterator().next());
            }
            representatives = ret;
        }
        return representatives;
    }

    private static byte checkWithinBound(byte val, byte lo, byte hi) {
        return val > hi || val < lo ? -1 : val;
    }

    /**
     * Register a new block as valid borosilicate glass with given tier (even if it doesn't contain boron at all)
     *
     * Does not support matching by more complex stuff like tile entity!
     *
     * Can only be called at INIT stage.
     */
    public static void registerGlass(Block block, int meta, byte tier) {
        if (Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
            throw new IllegalStateException("register too late!");
        if (!Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
            throw new IllegalStateException("register too early!");
        if (!isValidTier(tier)) throw new IllegalArgumentException("not a valid tier: " + tier);
        doRegister(tier, block, meta, getAllLevels());
    }

    /**
     * Check if there is at least one type of boroglass in that tier.
     */
    public static boolean hasGlassInTier(int tier) {
        return getAllLevels().containsKey((byte) tier);
    }

    /**
     * Get a structure element for a certain tier of <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
     * unless these glass are explicitly registered as a borosilicate glass.
     * <p>
     * Use this if you just want boroglass here and doesn't care what tier it is.
     */
    public static <T> IStructureElement<T> ofBoroGlass(int tier) {
        if (!hasGlassInTier(tier)) throw new IllegalArgumentException();
        return lazy(t -> {
            Pair<Block, Integer> pair = getRepresentatives().get(tier - 3);
            return ofBlockAdder((t1, block1, meta) -> getTier(block1, meta) == tier, pair.getKey(), pair.getValue());
        });
    }

    /**
     * Get a structure element for any kind of <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
     * unless these glass are explicitly registered as a borosilicate glass.
     * <p>
     * Use this if you just want boroglass here and doesn't care what tier it is.
     */
    public static <T> IStructureElement<T> ofBoroGlassAnyTier() {
        return lazy(t -> ofBlockAnyMeta(getGlassBlock()));
    }

    /**
     * Get a structure element for <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
     * unless these glass are explicitly registered as a borosilicate glass.
     * <p>
     * This assumes you want all glass used to be of the same tier.
     * <p>
     * NOTE: This will accept the basic boron glass (HV tier) as well. You might not want this. Use the other overload to filter this out.
     *
     * @param initialValue the value set before structure check started
     */
    public static <T> IStructureElement<T> ofBoroGlass(
            byte initialValue, BiConsumer<T, Byte> setter, Function<T, Byte> getter) {
        return lazy(
                t -> ofBlocksTiered(BorosilicateGlass::getTier, getRepresentatives(), initialValue, setter, getter));
    }

    /**
     * Get a structure element for <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
     * unless these glass are explicitly registered as a borosilicate glass.
     *
     * @param initialValue the value set before structure check started
     * @param minTier      minimal accepted tier. inclusive. must be greater than 0.
     * @param maxTier      maximal accepted tier. inclusive.
     */
    public static <T> IStructureElement<T> ofBoroGlass(
            byte initialValue, byte minTier, byte maxTier, BiConsumer<T, Byte> setter, Function<T, Byte> getter) {
        if (minTier > maxTier || minTier < 0) throw new IllegalArgumentException();
        return lazy(t -> ofBlocksTiered(
                (block1, meta) -> checkWithinBound(getTier(block1, meta), minTier, maxTier),
                getRepresentatives().stream()
                        .skip(Math.max(minTier - 3, 0))
                        .limit(maxTier - minTier + 1)
                        .collect(Collectors.toList()),
                initialValue,
                setter,
                getter));
    }

    /**
     * Get the tier of this <b>borosilicate</b> glass. DOES NOT consider other glass like reinforced glass, magic mirror, vanilla glass, etc.
     * unless these glass are explicitly registered as a borosilicate glass.
     *
     * @return glass tier, or -1 if is not a borosilicate glass
     */
    public static byte getTier(Block block, int meta) {
        Byte ret = allLevelsReverse.get(block, meta);
        return ret == null ? -1 : ret;
    }
}
