package pers.gwyog.gtneioreplugin.util;

import static pers.gwyog.gtneioreplugin.util.GT5CFGHelper.oreVeinNotInAnyDim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;

public class GT5OreSmallHelper {

    private static final int SMALL_ORE_BASE_META = 16000;
    public static boolean restrictBiomeSupport = false;
    public static final List<ItemStack> oreSmallList = new ArrayList<>();
    public static final HashMap<String, OreSmallWrapper> mapOreSmallWrapper = new HashMap<>();
    public static final HashMap<String, Short> mapOreDropUnlocalizedNameToOreMeta = new HashMap<>();
    public static final HashMap<Short, List<ItemStack>> mapOreMetaToOreDrops = new HashMap<>();
    public static final HashMap<OreSmallWrapper, String> bufferedDims = new HashMap<>();
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
        for (GT_Worldgen worldGen : GregTech_API.sWorldgenList)
            if (worldGen.mWorldGenName.startsWith("ore.small.") && worldGen instanceof GT_Worldgen_GT_Ore_SmallPieces) {
                GT_Worldgen_GT_Ore_SmallPieces worldGenSmallPieces = (GT_Worldgen_GT_Ore_SmallPieces) worldGen;
                meta = worldGenSmallPieces.mMeta;
                if (meta < 0) break;
                material = GregTech_API.sGeneratedMaterials[meta];
                mapOreSmallWrapper.put(worldGen.mWorldGenName, new OreSmallWrapper(worldGenSmallPieces));
                if (!mapOreMetaToOreDrops.containsKey(meta)) {
                    List<ItemStack> stackList = new ArrayList<>();
                    stack = GT_OreDictUnificator.get(
                            OrePrefixes.gemExquisite,
                            material,
                            GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L),
                            1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(
                            OrePrefixes.gemFlawless,
                            material,
                            GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L),
                            1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(OrePrefixes.gem, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(
                            OrePrefixes.gemFlawed,
                            material,
                            GT_OreDictUnificator.get(OrePrefixes.crushed, material, 1L),
                            1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(OrePrefixes.crushed, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(
                            OrePrefixes.gemChipped,
                            material,
                            GT_OreDictUnificator.get(OrePrefixes.dustImpure, material, 1L),
                            1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    stack = GT_OreDictUnificator.get(OrePrefixes.dustImpure, material, 1L);
                    if (stack != null && !mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
                        mapOreDropUnlocalizedNameToOreMeta.put(stack.getUnlocalizedName(), meta);
                        stackList.add(stack);
                    }
                    oreSmallList.add(new ItemStack(GregTech_API.sBlockOres1, 1, meta + SMALL_ORE_BASE_META));
                    mapOreMetaToOreDrops.put(meta, stackList);
                }
            }
        for (OreSmallWrapper oreSmallWrapper : mapOreSmallWrapper.values()) {
            bufferedDims.put(oreSmallWrapper, GT5CFGHelper.GT5CFGSmallOres(oreSmallWrapper.oreGenName));
        }

        // --- Handling of dimToOreWrapper ---

        // Get dims as "Ow,Ne,Ma" etc.
        bufferedDims.forEach((veinInfo, dims) -> {
            if (dims.equals(oreVeinNotInAnyDim)) {
                return;
            }

            for (String dim : dims.split(",")) {
                if (!dim.isEmpty()) {
                    SmallOreDimensionWrapper dimensionSmallOres = dimToSmallOreWrapper
                            .getOrDefault(dim, new SmallOreDimensionWrapper());
                    dimensionSmallOres.internalDimOreList.add(veinInfo);
                    dimToSmallOreWrapper.put(dim, dimensionSmallOres);
                }
            }

            // Calculate probabilities for each dim.
            for (String dim : dimToSmallOreWrapper.keySet()) {
                dimToSmallOreWrapper.get(dim).calculateWeights();
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

        @SuppressWarnings("unused")
        public Materials getOreMaterial() {
            return oreMaterial;
        }

        private final Materials oreMaterial;

        public OreSmallWrapper(GT_Worldgen_GT_Ore_SmallPieces worldGen) {
            this.oreGenName = worldGen.mWorldGenName;
            this.oreMeta = worldGen.mMeta;
            this.worldGenHeightRange = worldGen.mMinY + "-" + worldGen.mMaxY;
            this.amountPerChunk = worldGen.mAmount;
            ItemData oreData = GT_OreDictUnificator
                    .getAssociation(new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.mMeta));
            this.oreMaterial = oreData != null ? oreData.mMaterial.mMaterial : null;
        }

        public List<ItemStack> getMaterialDrops(int maximumIndex) {
            List<ItemStack> stackList = new ArrayList<>();
            for (int i = 0; i < maximumIndex; i++)
                stackList.add(new ItemStack(GregTech_API.sBlockOres1, 1, oreMeta + SMALL_ORE_BASE_META + i * 1000));
            return stackList;
        }
    }
}
