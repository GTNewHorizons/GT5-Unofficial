package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import gregtech.api.enums.OreMixes;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.common.OreMixBuilder;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class GT5OreLayerHelper {

    public static final HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<>();
    public static final HashMap<OreLayerWrapper, Set<String>> bufferedDims = new HashMap<>();
    public static final HashMap<String, NormalOreDimensionWrapper> dimToOreWrapper = new HashMap<>();

    public static void init() {
        for (OreMixes mix : OreMixes.values()) {
            mapOreLayerWrapper.put(mix.oreMixBuilder.oreMixName, new OreLayerWrapper(mix.oreMixBuilder));
        }
        
        for (OreLayerWrapper layer : mapOreLayerWrapper.values()) {
            bufferedDims.put(layer, DimensionHelper.getDims(layer));
        }

        // --- Handling of dimToOreWrapper ---

        // Get dims as "Ow,Ne,Ma" etc.
        bufferedDims.forEach((veinInfo, dims) -> {

            for (String dim : dims) {
                NormalOreDimensionWrapper dimensionOres = dimToOreWrapper
                    .getOrDefault(dim, new NormalOreDimensionWrapper());
                dimensionOres.internalDimOreList.add(veinInfo);
                dimToOreWrapper.put(dim, dimensionOres);
            }

            // Calculate probabilities for each dim.
            for (String dim : dimToOreWrapper.keySet()) {
                dimToOreWrapper.get(dim)
                    .calculateWeights();
            }
        });
        // --- End of handling for dimToOreWrapper ---
    }

    public static class OreLayerWrapper {

        public final String veinName, worldGenHeightRange, localizedName;
        public final IMaterial[] ores = new IMaterial[4];
        public final short randomWeight, size, density;
        public final Set<String> allowedDimWithOrigNames;

        public final IMaterial mPrimaryVeinMaterial;
        public final IMaterial mSecondaryMaterial;
        public final IMaterial mBetweenMaterial;
        public final IMaterial mSporadicMaterial;

        public OreLayerWrapper(OreMixBuilder mix) {
            this.veinName = mix.oreMixName;
            this.localizedName = mix.localizedName;
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
            try (OreInfo<IMaterial> info = OreInfo.getNewInfo()) {
                info.material = ores[veinLayer];
                info.stoneType = stoneType;

                return Objects.requireNonNull(OreManager.getStack(info, 1), "getLayerOre: " + veinLayer + ", " + stoneType + ", " + Arrays.toString(ores));
            }
        }

        public boolean containsOre(IMaterial material) {
            return ores[OreVeinLayer.VEIN_PRIMARY] == material
                || ores[OreVeinLayer.VEIN_SECONDARY] == material
                || ores[OreVeinLayer.VEIN_BETWEEN] == material
                || ores[OreVeinLayer.VEIN_SPORADIC] == material;
        }
    }

    public static class NormalOreDimensionWrapper {

        public final ArrayList<OreLayerWrapper> internalDimOreList = new ArrayList<>();
        public final HashMap<OreLayerWrapper, Double> oreVeinToProbabilityInDimension = new HashMap<>();

        // Calculate all weights of ore veins once dimension is initialised.
        private void calculateWeights() {
            int totalWeight = 0;
            for (OreLayerWrapper oreVein : internalDimOreList) {
                totalWeight += oreVein.randomWeight;
            }
            for (OreLayerWrapper oreVein : internalDimOreList) {
                oreVeinToProbabilityInDimension.put(oreVein, ((double) oreVein.randomWeight) / ((double) totalWeight));
            }
        }
    }
}
