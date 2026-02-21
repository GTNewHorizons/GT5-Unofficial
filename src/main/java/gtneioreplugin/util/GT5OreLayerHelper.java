package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;

import gregtech.GTMod;
import gregtech.api.enums.OreMixes;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.common.OreMixBuilder;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class GT5OreLayerHelper {

    /** {vein ore mix name: wrapper} */
    private static Map<String, OreLayerWrapper> ORE_VEINS_BY_NAME;

    /** {abbr dim name: wrapper} */
    private static Map<String, NormalOreDimensionWrapper> ORE_VEINS_BY_DIM;

    public static void init() {
        HashMap<String, OreLayerWrapper> byName = new HashMap<>();
        HashMap<String, NormalOreDimensionWrapper> byDim = new HashMap<>();

        for (OreMixes mix : OreMixes.values()) {
            OreLayerWrapper wrapper = new OreLayerWrapper(mix.oreMixBuilder);
            byName.put(mix.oreMixBuilder.oreMixName, wrapper);

            for (String dim : wrapper.abbrDimNames) {
                NormalOreDimensionWrapper dimensionOres = byDim.getOrDefault(dim, new NormalOreDimensionWrapper());
                dimensionOres.oreVeins.add(wrapper);
                byDim.put(dim, dimensionOres);
            }
        }

        // Calculate probabilities for each dim.
        byDim.values()
            .forEach(NormalOreDimensionWrapper::calculateWeights);

        ORE_VEINS_BY_NAME = ImmutableMap.copyOf(byName);
        ORE_VEINS_BY_DIM = ImmutableMap.copyOf(byDim);
    }

    public static OreLayerWrapper getVeinByName(String name) {
        return ORE_VEINS_BY_NAME.get(name);
    }

    public static NormalOreDimensionWrapper getVeinByDim(String abbrName) {
        return ORE_VEINS_BY_DIM.get(abbrName);
    }

    public static Map<String, OreLayerWrapper> getOreVeinsByName() {
        return ORE_VEINS_BY_NAME;
    }

    public static Map<String, NormalOreDimensionWrapper> getOreVeinsByDim() {
        return ORE_VEINS_BY_DIM;
    }

    public static class OreLayerWrapper {

        public final String veinName, worldGenHeightRange;
        public final IOreMaterial[] ores = new IOreMaterial[4];
        public final short randomWeight, size, density;
        /** {full dim name} */
        public final Set<String> allowedDimWithOrigNames;
        /** {abbr dim name} */
        public final Set<String> abbrDimNames;

        public final IOreMaterial mPrimaryVeinMaterial;
        public final IOreMaterial mSecondaryMaterial;
        public final IOreMaterial mBetweenMaterial;
        public final IOreMaterial mSporadicMaterial;
        public final Supplier<String> localizedName;

        public OreLayerWrapper(OreMixBuilder mix) {
            this.veinName = mix.oreMixName;
            this.localizedName = mix::getLocalizedName;
            this.ores[0] = mix.primary;
            this.ores[1] = mix.secondary;
            this.ores[2] = mix.between;
            this.ores[3] = mix.sporadic;

            this.mPrimaryVeinMaterial = mix.primary;
            this.mSecondaryMaterial = mix.secondary;
            this.mBetweenMaterial = mix.between;
            this.mSporadicMaterial = mix.sporadic;

            this.size = (short) mix.size;
            this.density = (short) mix.density;
            this.worldGenHeightRange = mix.minY + "-" + mix.maxY;
            this.randomWeight = (short) mix.weight;

            this.allowedDimWithOrigNames = mix.dimsEnabled;
            this.abbrDimNames = mix.dimsEnabled.stream()
                .map(DimensionHelper::getDimAbbreviatedName)
                .collect(Collectors.toSet());
        }

        public List<ItemStack> getVeinLayerOre(int veinLayer) {
            List<ItemStack> stackList = new ArrayList<>();
            for (IStoneType stoneType : ores[veinLayer].getValidStones()) {
                if (!stoneType.isExtraneous()) {
                    stackList.add(getLayerOre(veinLayer, stoneType));
                }
            }
            return stackList;
        }

        public ItemStack getLayerOre(int veinLayer, IStoneType stoneType) {
            try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
                info.material = ores[veinLayer];
                info.stoneType = stoneType;

                ItemStack stack = OreManager.getStack(info, 1);

                if (stack == null) {
                    GTMod.GT_FML_LOGGER.warn(
                        "Ore stack for vein was null: {} (vein: {}, index: {})",
                        ores[veinLayer],
                        veinName,
                        veinLayer);
                    stack = new ItemStack(Blocks.fire);
                }

                return stack;
            }
        }

        public boolean containsOre(IOreMaterial material) {
            return ores[OreVeinLayer.VEIN_PRIMARY] == material || ores[OreVeinLayer.VEIN_SECONDARY] == material
                || ores[OreVeinLayer.VEIN_BETWEEN] == material
                || ores[OreVeinLayer.VEIN_SPORADIC] == material;
        }

        public String getLocalizedName() {
            return this.localizedName.get();
        }
    }

    public static class NormalOreDimensionWrapper {

        public final ArrayList<OreLayerWrapper> oreVeins = new ArrayList<>();
        public final HashMap<OreLayerWrapper, Double> oreVeinToProbabilityInDimension = new HashMap<>();

        // Calculate all weights of ore veins once dimension is initialised.
        private void calculateWeights() {
            int totalWeight = 0;
            for (OreLayerWrapper oreVein : oreVeins) {
                totalWeight += oreVein.randomWeight;
            }
            for (OreLayerWrapper oreVein : oreVeins) {
                oreVeinToProbabilityInDimension.put(oreVein, ((double) oreVein.randomWeight) / ((double) totalWeight));
            }
        }
    }
}
