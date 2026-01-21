package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.google.common.collect.BiMap;

import gregtech.GTMod;
import gregtech.api.objects.GTUODimension;
import gregtech.api.objects.GTUOFluid;
import gtneioreplugin.GTNEIOrePlugin;

public class GT5UndergroundFluidHelper {

    /**
     * Need to store fluid name instead of fluid because fluid instance might be different between gas and liquid
     */
    private static final Map<String, List<UndergroundFluidWrapper>> fluidMap = new HashMap<>();

    public static void init() {
        BiMap<String, GTUODimension> dimensionList = GTMod.proxy.mUndergroundOil.getDimensionList();
        for (Map.Entry<String, GTUODimension> dimensionEntry : dimensionList.entrySet()) {
            String rawDimension = dimensionEntry.getKey();
            String dimension;
            try {
                dimension = getDimensionFromID(Integer.parseInt(rawDimension));
            } catch (NumberFormatException ignored) {
                dimension = getDimensionForEdgeCase(rawDimension);
                if (dimension == null) {
                    for (DimensionHelper.Dimension record : DimensionHelper.getAllDim()) {
                        if (record.trimmedName()
                            .equalsIgnoreCase(rawDimension)) {
                            dimension = record.abbr();
                            break;
                        }
                    }
                }
            }
            if (dimension == null) {
                GTNEIOrePlugin.LOG.warn("Unknown dimension found in GT5 config: " + rawDimension);
                continue;
            }

            BiMap<String, GTUOFluid> fluids = dimensionEntry.getValue()
                .getFluids();

            int maxChance = 0;
            for (Map.Entry<String, GTUOFluid> fluidEntry : fluids.entrySet()) {
                maxChance += fluidEntry.getValue().Chance;
            }

            for (Map.Entry<String, GTUOFluid> fluidEntry : fluids.entrySet()) {
                Fluid fluid = FluidRegistry.getFluid(fluidEntry.getKey());
                if (fluid != null) {
                    UndergroundFluidWrapper wrapper = new UndergroundFluidWrapper(
                        dimension,
                        fluidEntry.getValue().Chance * 10000 / maxChance,
                        fluidEntry.getValue().MaxAmount,
                        fluidEntry.getValue().MinAmount);
                    if (fluidMap.containsKey(fluid.getName())) {
                        fluidMap.get(fluid.getName())
                            .add(wrapper);
                    } else {
                        fluidMap.put(fluid.getName(), new ArrayList<>(Collections.singletonList(wrapper)));
                    }
                }
            }
        }

        for (List<UndergroundFluidWrapper> wrappers : fluidMap.values()) {
            wrappers.sort(Comparator.comparingInt(w -> DimensionHelper.getIndexByAbbr(w.dimension)));
        }
    }

    public static List<UndergroundFluidWrapper> getEntry(String fluidName) {
        return fluidMap.get(fluidName);
    }

    public static Map<String, List<UndergroundFluidWrapper>> getAllEntries() {
        return fluidMap;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private static String getDimensionFromID(int id) {
        return switch (id) {
            case 0 -> "Ow";
            default -> null;
        };
    }

    private static String getDimensionForEdgeCase(String rawDimension) {
        return switch (rawDimension) {
            case "aCentauriBb" -> "CB";
            case "BarnardaC" -> "BC";
            case "BarnardaE" -> "BE";
            case "BarnardaF" -> "BF";
            case "TCetiE" -> "TE";
            default -> {
                GTNEIOrePlugin.LOG.warn("Unknown dimension name while parsing: " + rawDimension);
                yield null;
            }
        };
    }

    public static class UndergroundFluidWrapper {

        /**
         * Using {@link DimensionHelper#getAllDisplayedNames()}
         */
        public final String dimension;
        /**
         * Chance of this fluid field being generated. 10000 means 100% of the dimension
         */
        public final int chance;

        public final int maxAmount;
        public final int minAmount;

        public UndergroundFluidWrapper(String dimension, int chance, int maxAmount, int minAmount) {
            this.dimension = dimension;
            this.chance = chance;
            this.maxAmount = maxAmount;
            this.minAmount = minAmount;
        }
    }
}
