package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.oregen.BWOreLayer;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreMixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTLanguageManager;
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
        for (OreLayerWrapperTemp layerTemp : BWOreLayer.NEIList) {
            OreLayerWrapper layer = layerTemp.getWrapper();
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

        public final byte bwOres;
        public final boolean hasVariants;
        public final String veinName, worldGenHeightRange, localizedName;
        public final short[] Meta = new short[4];
        public final short randomWeight, size, density;
        public final Map<String, Boolean> allowedDimWithOrigNames;

        public final Materials mPrimaryVeinMaterial;
        public final Materials mSecondaryMaterial;
        public final Materials mBetweenMaterial;
        public final Materials mSporadicMaterial;

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
            this.bwOres = 0;
            this.hasVariants = true;
        }

        public List<ItemStack> getVeinLayerOre(int maximumMaterialIndex, int veinLayer) {
            List<ItemStack> stackList = new ArrayList<>();
            if (this.bwOres == 0) {
                for (int i = 0; i < maximumMaterialIndex; i++) {
                    stackList.add(getLayerOre(veinLayer, i));
                }
            } else stackList.add(getLayerOre(veinLayer, 0));
            return stackList;
        }

        public ItemStack getLayerOre(int veinLayer, int materialIndex) {
            if ((this.bwOres & 0b1000 >> veinLayer) == 0)
                return new ItemStack(GregTechAPI.sBlockOres1, 1, Meta[veinLayer] + materialIndex * 1000);
            return new ItemStack(WerkstoffLoader.BWOres, 1, Meta[veinLayer]);
        }

        private static Materials getMaterials(ISubTagContainer input) {
            if (input instanceof Werkstoff) return ((Werkstoff) input).getBridgeMaterial();
            else if (input instanceof Materials) return ((Materials) input);
            else throw new ClassCastException(); // If it cannot cast it will throw an error.
        }

        public OreLayerWrapper(String veinName, ISubTagContainer primary, ISubTagContainer secondary,
            ISubTagContainer between, ISubTagContainer sporadic, BWOreLayer layer) {
            this.veinName = veinName;
            this.localizedName = GTLanguageManager.getTranslation(veinName);
            this.Meta[0] = getMeta(primary);
            this.Meta[1] = getMeta(secondary);
            this.Meta[2] = getMeta(between);
            this.Meta[3] = getMeta(sporadic);
            this.bwOres = getBwOres(primary, secondary, between, sporadic);

            this.mPrimaryVeinMaterial = getMaterials(primary);
            this.mSecondaryMaterial = getMaterials(secondary);
            this.mBetweenMaterial = getMaterials(between);
            this.mSporadicMaterial = getMaterials(sporadic);

            this.size = (short) layer.mSize;
            this.density = (short) layer.mDensity;
            this.worldGenHeightRange = layer.mMinY + "-" + layer.mMaxY;
            this.randomWeight = (short) layer.mWeight;

            this.allowedDimWithOrigNames = new HashMap<>();
            this.hasVariants = false;
        }

        private short getMeta(ISubTagContainer material) {
            if (material instanceof Materials) return (short) ((Materials) material).mMetaItemSubID;
            else if (material instanceof Werkstoff) return (short) ((Werkstoff) material).getmID();
            else return 0;
        }

        private byte getBwOres(ISubTagContainer top, ISubTagContainer bottom, ISubTagContainer between,
            ISubTagContainer sprinkled) {
            byte bwOres = 0;
            if (top instanceof Werkstoff) bwOres = (byte) (bwOres | 0b1000);
            if (bottom instanceof Werkstoff) bwOres = (byte) (bwOres | 0b0100);
            if (between instanceof Werkstoff) bwOres = (byte) (bwOres | 0b0010);
            if (sprinkled instanceof Werkstoff) bwOres = (byte) (bwOres | 0b0001);
            return bwOres;
        }

        public void addDimension(String dim) {
            this.allowedDimWithOrigNames.put(dim, true);
        }

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
    }
    public static class OreLayerWrapperTemp {//Bridge Materials init not so fast.
        public final String veinName;
        public final ISubTagContainer primary;
        public final ISubTagContainer secondary;
        public final ISubTagContainer between;
        public final ISubTagContainer sporadic;
        public final BWOreLayer layer;
        public final List<String> allowedDimWithOrigNames = new ArrayList<>();

        public OreLayerWrapperTemp(String veinName, ISubTagContainer primary, ISubTagContainer secondary,
            ISubTagContainer between, ISubTagContainer sporadic, BWOreLayer layer) {
            this.veinName = veinName;
            this.primary = primary;
            this.secondary = secondary;
            this.between = between;
            this.sporadic = sporadic;
            this.layer = layer;
        }

        public OreLayerWrapper getWrapper(){//Should only runned once.
            OreLayerWrapper wrapper = new OreLayerWrapper(this.veinName, this.primary, this.secondary,
            this.between, this.sporadic, this.layer);
            for (String dim : this.allowedDimWithOrigNames) {
                wrapper.addDimension(dim);
            }
            return wrapper;
        }

        public bool isLayerEqual(BWOreLayer layer){
            return (this.layer.bwOres == layer.bwOres 
                && this.layer.mPrimaryMeta == layer.mPrimaryMeta
                && this.layer.mSecondaryMeta == layer.mSecondaryMeta
                && this.layer.mBetweenMeta == layer.mBetweenMeta
                && this.layer.mSporadicMeta == layer.mSporadicMeta
                && this.layer.mWeight == layer.mWeight
                && this.layer.mDensity == layer.mDensity
                && this.layer.mSize == layer.mSize
                && this.layer.mMinY == layer.mMinY
                && this.layer.mMaxY == layer.mMaxY);
        }
        
        public void addDimension(String dim) {
            this.allowedDimWithOrigNames.add(dim);
        }
    }
}
