package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import bartworks.system.oregen.BWOreLayer;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreMixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.common.OreMixBuilder;

public class GT5OreLayerHelper {

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

    public static final HashMap<String, OreLayerWrapper> mapOreLayerWrapper = new HashMap<>();
    public static final HashMap<OreLayerWrapper, Map<String, Boolean>> bufferedDims = new HashMap<>();
    public static final HashMap<String, NormalOreDimensionWrapper> dimToOreWrapper = new HashMap<>();

    public static void init() {
        for (OreMixes mix : OreMixes.values())
            mapOreLayerWrapper.put(mix.oreMixBuilder.oreMixName, new OreLayerWrapper(mix.oreMixBuilder));
        for (OreLayerWrapperBW layer : BWOreLayer.NEIList) {
            mapOreLayerWrapper.put(layer.veinName, layer);
        }
        for (OreLayerWrapper layer : mapOreLayerWrapper.values()) {
            bufferedDims.put(layer, DimensionHelper.getDims(layer));
        }

        // --- Handling of dimToOreWrapper ---

        // Get dims as "Ow,Ne,Ma" etc.
        bufferedDims.forEach((veinInfo, dims) -> {

            for (String dim : dims.keySet()) {
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
        public final short[] Meta = new short[4];
        public final short randomWeight, size, density;
        public final Map<String, Boolean> allowedDimWithOrigNames;

        public final ISubTagContainer mPrimaryVeinMaterial;
        public final ISubTagContainer mSecondaryMaterial;
        public final ISubTagContainer mBetweenMaterial;
        public final ISubTagContainer mSporadicMaterial;

        public OreLayerWrapper(OreMixBuilder mix) {
            this.veinName = mix.oreMixName;
            this.localizedName = mix.localizedName;
            this.Meta[0] = (short) mix.primary.mMetaItemSubID;
            this.Meta[1] = (short) mix.secondary.mMetaItemSubID;
            this.Meta[2] = (short) mix.between.mMetaItemSubID;
            this.Meta[3] = (short) mix.sporadic.mMetaItemSubID;

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

        public List<ItemStack> getVeinLayerOre(int maximumMaterialIndex, int veinLayer) {
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < maximumMaterialIndex; i++) {
                stackList.add(getLayerOre(veinLayer, i));
            }
            return stackList;
        }

        public ItemStack getLayerOre(int veinLayer, int materialIndex) {
            return new ItemStack(GregTechAPI.sBlockOres1, 1, Meta[veinLayer] + materialIndex * 1000);
        }

        public boolean containsOre(short materialIndex) {
            return Meta[OreVeinLayer.VEIN_PRIMARY] == materialIndex
                || Meta[OreVeinLayer.VEIN_SECONDARY] == materialIndex
                || Meta[OreVeinLayer.VEIN_BETWEEN] == materialIndex
                || Meta[OreVeinLayer.VEIN_SPORADIC] == materialIndex;
        }
    }

    public static class OreLayerWrapperBW extends OreLayerWrapper {
        public byte bwOres;
        public OreLayerWrapperBW(String veinName, ISubTagContainer primary, ISubTagContainer secondary, ISubTagContainer between, ISubTagContainer sporadic, BWOreLayer layer) {
            this.veinName = mix.oreMixName;
            this.localizedName = mix.localizedName;
            this.Meta[0] = getMeta(primary);
            this.Meta[1] = getMeta(secondary);
            this.Meta[2] = getMeta(between);
            this.Meta[3] = getMeta(sporadic);
            setBwOres(primary, secondary, between, sporadic);

            this.mPrimaryVeinMaterial = mix.primary;
            this.mSecondaryMaterial = mix.secondary;
            this.mBetweenMaterial = mix.between;
            this.mSporadicMaterial = mix.sporadic;

            this.size = (short) layer.mSize;
            this.density = (short) layer.mDensity;
            this.worldGenHeightRange = layer.mMinY + "-" + layer.mMaxY;
            this.randomWeight = (short) layer.mWeight;

            this.allowedDimWithOrigNames = new HashMap<>();
        }

        private short getMeta(ISubTagContainer material) {
            if (material instanceof Materials) return (short) ((Materials) material).mMetaItemSubID;
            else if (material instanceof Werkstoff) return (short) ((Werkstoff) material).getmID();
            else return 0;
        }

        private void setBwOres(ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between, ISubTagContainer sprinkled) {
            if (top instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b1000);
            if (bottom instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0100);
            if (between instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0010);
            if (sprinkled instanceof Werkstoff) this.bwOres = (byte) (this.bwOres | 0b0001);
        }

        public void addDimension(String dim) {
            this.allowedDimWithOrigNames.put(dim, true);
        }

        @Override
        public boolean containsOre(short materialIndex) {
            return ((this.bwOres & 0b1000) == 0 && Meta[OreVeinLayer.VEIN_PRIMARY] == materialIndex)
                || ((this.bwOres & 0b0100) == 0 && Meta[OreVeinLayer.VEIN_SECONDARY] == materialIndex)
                || ((this.bwOres & 0b0010) == 0 && Meta[OreVeinLayer.VEIN_BETWEEN] == materialIndex)
                || ((this.bwOres & 0b0001) == 0 && Meta[OreVeinLayer.VEIN_SPORADIC] == materialIndex);
        }

        public boolean containsOreBW(short materialIndex) {
            return ((this.bwOres & 0b1000) != 0 && Meta[OreVeinLayer.VEIN_PRIMARY] == materialIndex)
                || ((this.bwOres & 0b0100) != 0 && Meta[OreVeinLayer.VEIN_SECONDARY] == materialIndex)
                || ((this.bwOres & 0b0010) != 0 && Meta[OreVeinLayer.VEIN_BETWEEN] == materialIndex)
                || ((this.bwOres & 0b0001) != 0 && Meta[OreVeinLayer.VEIN_SPORADIC] == materialIndex);
        }

        @Override
        public List<ItemStack> getVeinLayerOre(int _unused, int veinLayer) {
            List<ItemStack> stackList = new ArrayList<>();
            stackList.add(getLayerOre(veinLayer, 0));
            return stackList;
        }
        
        @Override
        public ItemStack getLayerOre(int veinLayer, int _unused) {
            List<ItemStack> List = layer.getStacks();
            return List.get(veinLayer);
        }
    }
}
