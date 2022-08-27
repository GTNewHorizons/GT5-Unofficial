package com.github.bartimaeusnek.bartworks.API;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;

import com.gtnewhorizon.structurelib.structure.IStructureElement;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.List;
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
 * IStructureElements returned from this class should not have its methods called before the game start.
 */
public class BorosilicateGlass {

    private static Block block;
    private static List<Pair<Block, Integer>> representatives;

    private static Block getGlassBlock() {
        if (block == null) block = GameRegistry.findBlock("bartworks", "BW_GlasBlocks");
        return block;
    }

    private static List<Pair<Block, Integer>> getRepresentatives() {
        if (representatives == null) {
            ArrayList<Pair<Block, Integer>> ret = new ArrayList<>();
            Block block = getGlassBlock();
            ret.add(Pair.of(block, 0));
            ret.add(Pair.of(block, 1));
            ret.add(Pair.of(block, 2));
            ret.add(Pair.of(block, 3));
            ret.add(Pair.of(block, 4));
            ret.add(Pair.of(block, 5));
            ret.add(Pair.of(block, 13));
            ret.add(Pair.of(block, 14));
            representatives = ret;
        }
        return representatives;
    }

    private static byte checkWithinBound(byte val, byte lo, byte hi) {
        return val > hi || val < lo ? -1 : val;
    }

    /**
     * Check if there is at least one type of boroglass in that tier.
     */
    public static boolean hasGlassInTier(int tier) {
        return tier >= 3 && tier <= 10;
    }

    /**
     * Get a structure element for a certain tier of <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
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
     * <p>
     * Use this if you just want boroglass here and doesn't care what tier it is.
     */
    public static <T> IStructureElement<T> ofBoroGlassAnyTier() {
        return lazy(t -> ofBlockAnyMeta(getGlassBlock()));
    }

    /**
     * Get a structure element for <b>borosilicate</b> glass. DOES NOT accept other glass like reinforced glass, magic mirror, vanilla glass, etc.
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
     */
    public static byte getTier(Block block, int meta) {
        byte ret;
        switch (meta) {
            case 1:
                ret = 4;
                break;
            case 2:
            case 12:
                ret = 5;
                break;
            case 3:
                ret = 6;
                break;
            case 4:
                ret = 7;
                break;
            case 5:
                ret = 8;
                break;
            case 13:
                ret = 9;
                break;
            case 14:
                ret = 10;
                break;
            default:
                ret = 3;
        }
        return block == getGlassBlock() ? ret : -1;
    }
}
