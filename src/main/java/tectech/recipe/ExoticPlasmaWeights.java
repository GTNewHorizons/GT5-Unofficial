package tectech.recipe;

import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.NUMBER_OF_INPUTS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraftforge.fluids.FluidStack;

/**
 * Weights of the plasmas an exotic module recipe can ask for, and how likely each is to be asked for in one roll.
 * <p>
 * The quark gluon plasma recipe first rolls how many of its inputs are fluids, then picks that many fluids and the rest
 * items, each from its own pool without repeats. The fluid pool is far smaller than the item pool, so equal weights are
 * not equally likely. The magmatter recipe rolls a single plasma, so its chances follow from the weights.
 */
public final class ExoticPlasmaWeights {

    private static final class Entry {

        final long weight;
        final boolean fromItem;
        long shownWeight;
        double chance;

        Entry(long weight, boolean fromItem) {
            this.weight = weight;
            this.fromItem = fromItem;
        }
    }

    private final boolean quarkGluon;
    private final Map<String, Entry> byFluid = new HashMap<>();

    public ExoticPlasmaWeights(boolean quarkGluon) {
        this.quarkGluon = quarkGluon;
    }

    /**
     * @param fromItem whether the plasma is asked for in place of an item rather than a fluid
     */
    public void add(FluidStack plasma, long weight, boolean fromItem) {
        byFluid.put(
            plasma.getFluid()
                .getName(),
            new Entry(weight, fromItem));
    }

    /** Divides the weights by their common divisor and works out the chances. Call once all plasmas are added. */
    public void finish() {
        BigInteger gcd = BigInteger.ZERO;
        for (Entry entry : byFluid.values()) gcd = gcd.gcd(BigInteger.valueOf(entry.weight));
        for (Entry entry : byFluid.values()) {
            entry.shownWeight = gcd.signum() > 0 ? entry.weight / gcd.longValue() : entry.weight;
        }
        calculateChances();
    }

    public long getWeight(FluidStack plasma) {
        Entry entry = byFluid.get(
            plasma.getFluid()
                .getName());
        return entry == null ? 0 : entry.shownWeight;
    }

    /**
     * @return the chance of the plasma being asked for in one roll, from 0 to 1; they add up to the number of inputs
     */
    public double getChance(FluidStack plasma) {
        Entry entry = byFluid.get(
            plasma.getFluid()
                .getName());
        return entry == null ? 0 : entry.chance;
    }

    private void calculateChances() {
        if (!quarkGluon) {
            long total = 0;
            for (Entry entry : byFluid.values()) total += entry.weight;
            for (Entry entry : byFluid.values()) entry.chance = total > 0 ? (double) entry.weight / total : 0;
            return;
        }

        List<Entry> items = new ArrayList<>();
        List<Entry> fluids = new ArrayList<>();
        for (Entry entry : byFluid.values()) (entry.fromItem ? items : fluids).add(entry);

        // Like MTEExoticModule#generateQuarkGluonRecipe: 0 to NUMBER_OF_INPUTS - 1 fluids, all equally likely
        for (int rolledFluids = 0; rolledFluids < NUMBER_OF_INPUTS; rolledFluids++) {
            int fluidCount = Math.min(rolledFluids, fluids.size());
            addChances(items, Math.min(NUMBER_OF_INPUTS - fluidCount, items.size()), 1d / NUMBER_OF_INPUTS);
            addChances(fluids, fluidCount, 1d / NUMBER_OF_INPUTS);
        }
    }

    /**
     * Adds the chances of the entries of a pool being among {@code picks} weighted picks without repeats. Entries of
     * the
     * same weight are interchangeable, so it is enough to follow how many were picked of each weight, and the chance of
     * one entry is the expected number picked divided by how many there are.
     */
    private static void addChances(List<Entry> pool, int picks, double factor) {
        Map<Long, List<Entry>> byWeight = new TreeMap<>();
        for (Entry entry : pool) byWeight.computeIfAbsent(entry.weight, w -> new ArrayList<>())
            .add(entry);
        List<Long> weights = new ArrayList<>(byWeight.keySet());
        int[] sizes = new int[weights.size()];
        for (int c = 0; c < sizes.length; c++) sizes[c] = byWeight.get(weights.get(c))
            .size();

        // Probability of each picked-per-weight state after the picks made so far
        Map<List<Integer>, Double> states = new HashMap<>();
        states.put(new ArrayList<>(Collections.nCopies(sizes.length, 0)), 1d);
        for (int pick = 0; pick < picks; pick++) {
            Map<List<Integer>, Double> next = new HashMap<>();
            for (Map.Entry<List<Integer>, Double> state : states.entrySet()) {
                List<Integer> picked = state.getKey();
                double remainingWeight = 0;
                for (int c = 0; c < sizes.length; c++) remainingWeight += (sizes[c] - picked.get(c)) * weights.get(c);
                if (remainingWeight == 0) {
                    next.merge(picked, state.getValue(), Double::sum);
                    continue;
                }
                for (int c = 0; c < sizes.length; c++) {
                    int left = sizes[c] - picked.get(c);
                    if (left == 0) continue;
                    List<Integer> after = new ArrayList<>(picked);
                    after.set(c, picked.get(c) + 1);
                    next.merge(after, state.getValue() * left * weights.get(c) / remainingWeight, Double::sum);
                }
            }
            states = next;
        }

        for (int c = 0; c < sizes.length; c++) {
            double expectedPicked = 0;
            for (Map.Entry<List<Integer>, Double> state : states.entrySet()) {
                expectedPicked += state.getValue() * state.getKey()
                    .get(c);
            }
            for (Entry entry : byWeight.get(weights.get(c))) entry.chance += factor * expectedPicked / sizes[c];
        }
    }
}
