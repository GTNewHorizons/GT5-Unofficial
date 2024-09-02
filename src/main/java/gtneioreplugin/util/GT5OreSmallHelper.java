package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SmallOres;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.world.GTWorldgen;
import gregtech.common.SmallOreBuilder;
import gregtech.common.WorldgenGTOreSmallPieces;

public class GT5OreSmallHelper {

    private static final int SMALL_ORE_BASE_META = 16000;
    public static boolean restrictBiomeSupport = false;
    public static final List<ItemStack> oreSmallList = new ArrayList<>();
    public static final HashMap<String, OreSmallWrapper> mapOreSmallWrapper = new HashMap<>();
    public static final HashMap<String, Short> mapOreDropUnlocalizedNameToOreMeta = new HashMap<>();
    public static final HashMap<Short, List<ItemStack>> mapOreMetaToOreDrops = new HashMap<>();
    public static final HashMap<OreSmallWrapper, Map<String, Boolean>> bufferedDims = new HashMap<>();
    public static final HashMap<String, SmallOreDimensionWrapper> dimToSmallOreWrapper = new HashMap<>();

    public static class SmallOreDimensionWrapper {

        public final ArrayList<OreSmallWrapper> internalDimOreList = new ArrayList<>();
        public final HashMap<OreSmallWrapper, Double> oreVeinToProbabilityInDimension = new HashMap<>();

        // Calculate all weights of ore veins once dimension is initialised.
        private void calculateWeights() {
            int totalWeight = 0;
            for (OreSmallWrapper oreVein : internalDimOreList) {
                totalWeight += oreVein.amountPerChunk;
            }
            for (OreSmallWrapper oreVein : internalDimOreList) {
                oreVeinToProbabilityInDimension
                    .put(oreVein, ((double) oreVein.amountPerChunk) / ((double) totalWeight));
            }
        }
    }

    public static void init() {
        checkExtraSupport();
        ItemStack stack;
        Materials material;
        short meta;
        Map<String, SmallOreBuilder> smallOreDefMap = new HashMap<>();

        for (SmallOres ore : SmallOres.values()) {
            smallOreDefMap.put(ore.smallOreBuilder.smallOreName, ore.smallOreBuilder);
        }

        for (GTWorldgen worldGen : GregTechAPI.sWorldgenList) {
            if (!worldGen.mWorldGenName.startsWith("ore.small.") || !(worldGen instanceof WorldgenGTOreSmallPieces)) {
                continue;
            }

            WorldgenGTOreSmallPieces worldGenSmallPieces = (WorldgenGTOreSmallPieces) worldGen;
            meta = worldGenSmallPieces.mMeta;
            if (meta < 0) break;
            material = GregTechAPI.sGeneratedMaterials[meta];
            mapOreSmallWrapper.put(
                worldGen.mWorldGenName,
                new OreSmallWrapper(smallOreDefMap.get(worldGenSmallPieces.mWorldGenName)));
            if (mapOreMetaToOreDrops.containsKey(meta)) {
                continue;
            }

            List<ItemStack> stackList = new ArrayList<>();
            stack = GTOreDictUnificator
                .get(OrePrefixes.gemExquisite, material, GTOreDictUnificator.get(OrePrefixes.gem, material, 1L), 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawless, material, GTOreDictUnificator.get(OrePrefixes.gem, material, 1L), 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator.get(OrePrefixes.gem, material, 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawed, material, GTOreDictUnificator.get(OrePrefixes.crushed, material, 1L), 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator.get(OrePrefixes.crushed, material, 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator.get(
                OrePrefixes.gemChipped,
                material,
                GTOreDictUnificator.get(OrePrefixes.dustImpure, material, 1L),
                1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            stack = GTOreDictUnificator.get(OrePrefixes.dustImpure, material, 1L);
            if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                stackList.add(stack);
            }
            oreSmallList.add(new ItemStack(GregTechAPI.sBlockOres1, 1, meta + SMALL_ORE_BASE_META));
            mapOreMetaToOreDrops.put(meta, stackList);

        }
        for (OreSmallWrapper oreSmallWrapper : mapOreSmallWrapper.values()) {
            bufferedDims.put(oreSmallWrapper, DimensionHelper.getDims(oreSmallWrapper));
        }

        // --- Handling of dimToOreWrapper ---

        // Get dims as "Ow,Ne,Ma" etc.
        bufferedDims.forEach((veinInfo, dims) -> {
            for (String dim : dims.keySet()) {
                SmallOreDimensionWrapper dimensionSmallOres = dimToSmallOreWrapper
                    .getOrDefault(dim, new SmallOreDimensionWrapper());
                dimensionSmallOres.internalDimOreList.add(veinInfo);
                dimToSmallOreWrapper.put(dim, dimensionSmallOres);
            }

            // Calculate probabilities for each dim.
            for (String dim : dimToSmallOreWrapper.keySet()) {
                dimToSmallOreWrapper.get(dim)
                    .calculateWeights();
            }
        });
        // --- End of handling for dimToOreWrapper ---
    }

    private static void checkExtraSupport() {
        Class<?> clazzGTOreSmall = null;
        try {
            clazzGTOreSmall = Class.forName("gregtech.common" + ".GT_Worldgen_GT_Ore_SmallPieces");
        } catch (ClassNotFoundException ignored) {}
        if (clazzGTOreSmall != null) {
            try {
                clazzGTOreSmall.getField("mRestrictBiome");
                restrictBiomeSupport = true;
            } catch (Exception ignored) {}
        }
    }

    public static Materials[] getDroppedDusts() {
        return new Materials[] { Materials.Stone, Materials.Netherrack, Materials.Endstone, Materials.GraniteBlack,
            Materials.GraniteRed, Materials.Marble, Materials.Basalt, Materials.Stone };
    }

    public static class OreSmallWrapper {

        public final String oreGenName;
        public final short oreMeta;
        public final String worldGenHeightRange;
        public final short amountPerChunk;

        public final Map<String, Boolean> allowedDimWithOrigNames;

        @SuppressWarnings("unused")
        public Materials getOreMaterial() {
            return oreMaterial;
        }

        private final Materials oreMaterial;

        public OreSmallWrapper(SmallOreBuilder ore) {
            this.oreGenName = ore.smallOreName;
            this.oreMeta = (short) ore.ore.mMetaItemSubID;
            this.worldGenHeightRange = ore.minY + "-" + ore.maxY;
            this.amountPerChunk = (short) ore.amount;
            this.oreMaterial = ore.ore;

            this.allowedDimWithOrigNames = ore.dimsEnabled;
        }

        public List<ItemStack> getMaterialDrops(int maximumIndex) {
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < maximumIndex; i++)
                stackList.add(new ItemStack(GregTechAPI.sBlockOres1, 1, oreMeta + SMALL_ORE_BASE_META + i * 1000));
            return stackList;
        }
    }
}
