package pers.gwyog.gtneioreplugin.util;

import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.LOG;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.google.common.collect.BiMap;

import gregtech.GT_Mod;
import gregtech.api.objects.GT_UO_Dimension;
import gregtech.api.objects.GT_UO_DimensionList;
import gregtech.api.objects.GT_UO_Fluid;

public class GT5UndergroundFluidHelper {

    /**
     * Need to store fluid name instead of fluid because fluid instance might be different between gas and liquid
     */
    private static final Map<String, List<UndergroundFluidWrapper>> fluidMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public GT5UndergroundFluidHelper() {
        try {
            Field fieldDimensionList = GT_UO_DimensionList.class.getDeclaredField("fDimensionList");
            fieldDimensionList.setAccessible(true);
            BiMap<String, GT_UO_Dimension> dimensionList = (BiMap<String, GT_UO_Dimension>) fieldDimensionList
                    .get(GT_Mod.gregtechproxy.mUndergroundOil);
            for (Map.Entry<String, GT_UO_Dimension> dimensionEntry : dimensionList.entrySet()) {
                String rawDimension = dimensionEntry.getKey();
                String dimension = null;
                try {
                    dimension = getDimensionFromID(Integer.parseInt(rawDimension));
                } catch (NumberFormatException ignored) {
                    Field fieldProviders = DimensionManager.class.getDeclaredField("providers");
                    fieldProviders.setAccessible(true);
                    Hashtable<Integer, Class<? extends WorldProvider>> providers = (Hashtable<Integer, Class<? extends WorldProvider>>) fieldProviders
                            .get(null);

                    // some short dimension names like Io might be caught by multiple dimension classes,
                    // so we'll check them all.
                    // List<WorldProvider> dimensionCandidates = providers.values()
                    List<Class<? extends WorldProvider>> dimensionCandidates = providers.values().stream()
                            .filter(p -> p.getName().contains(rawDimension)).collect(Collectors.toList());
                    loop: for (Class<? extends WorldProvider> candidate : dimensionCandidates) {
                        for (int i = 0; i < DimensionHelper.DimNameTrimmed.length; i++) {
                            if (DimensionHelper.DimNameTrimmed[i]
                                    .equalsIgnoreCase(candidate.getConstructor().newInstance().getDimensionName())) {
                                dimension = DimensionHelper.DimNameDisplayed[i];
                                break loop;
                            }
                        }
                    }
                }

                if (dimension == null) {
                    dimension = getDimensionForEdgeCase(rawDimension);
                    if (dimension == null) {
                        LOG.warn("Unknown dimension found in GT5 config: " + rawDimension);
                        continue;
                    }
                }

                Field fieldFluids = GT_UO_Dimension.class.getDeclaredField("fFluids");
                fieldFluids.setAccessible(true);
                BiMap<String, GT_UO_Fluid> fluids = (BiMap<String, GT_UO_Fluid>) fieldFluids
                        .get(dimensionEntry.getValue());

                int maxChance = 0;
                for (Map.Entry<String, GT_UO_Fluid> fluidEntry : fluids.entrySet()) {
                    maxChance += fluidEntry.getValue().Chance;
                }

                for (Map.Entry<String, GT_UO_Fluid> fluidEntry : fluids.entrySet()) {
                    Fluid fluid = FluidRegistry.getFluid(fluidEntry.getKey());
                    if (fluid != null) {
                        UndergroundFluidWrapper wrapper = new UndergroundFluidWrapper(
                                dimension,
                                fluidEntry.getValue().Chance * 10000 / maxChance,
                                fluidEntry.getValue().MaxAmount,
                                fluidEntry.getValue().MinAmount);
                        if (fluidMap.containsKey(fluid.getName())) {
                            fluidMap.get(fluid.getName()).add(wrapper);
                        } else {
                            fluidMap.put(fluid.getName(), new ArrayList<>(Collections.singletonList(wrapper)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (List<UndergroundFluidWrapper> wrappers : fluidMap.values()) {
            wrappers.sort(
                    Comparator.comparingInt(w -> Arrays.asList(DimensionHelper.DimNameDisplayed).indexOf(w.dimension)));
        }
    }

    public static List<UndergroundFluidWrapper> getEntry(String fluidName) {
        return fluidMap.get(fluidName);
    }

    public static Map<String, List<UndergroundFluidWrapper>> getAllEntries() {
        return fluidMap;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private String getDimensionFromID(int id) {
        switch (id) {
            case 0:
                return "Ow";
            default:
                return null;
        }
    }

    private String getDimensionForEdgeCase(String rawDimension) {
        switch (rawDimension) {
            case "aCentauriBb":
                return "CB";
            case "BarnardaC":
                return "BC";
            case "BarnardaE":
                return "BE";
            case "BarnardaF":
                return "BF";
            case "TCetiE":
                return "TE";
            default:
                LOG.warn("Unknown dimension name while parsing: " + rawDimension);
                return null;
        }
    }

    public static class UndergroundFluidWrapper {

        /**
         * Using {@link DimensionHelper#DimNameDisplayed}
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
