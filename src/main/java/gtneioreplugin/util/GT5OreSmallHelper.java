package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SmallOres;
import gregtech.api.world.GTWorldgen;
import gregtech.common.SmallOreBuilder;
import gregtech.common.WorldgenGTOreSmallPieces;
import gregtech.common.blocks.BlockOres2;
import gregtech.common.blocks.BlockOres2.StoneType;

public class GT5OreSmallHelper {

    public static boolean restrictBiomeSupport = false;
    public static final List<ItemStack> SMALL_ORE_LIST = new ArrayList<>();
    public static final HashMap<String, OreSmallWrapper> SMALL_ORES_BY_NAME = new HashMap<>();
    public static final HashMap<String, Materials> ORE_DROP_TO_MAT = new HashMap<>();
    public static final HashMap<Materials, List<ItemStack>> ORE_MAT_TO_DROPS = new HashMap<>();
    /** {abbr dim name: wrapper} */
    public static final HashMap<String, SmallOreDimensionWrapper> SMALL_ORES_BY_DIM = new HashMap<>();

    public static void init() {
        Map<String, SmallOreBuilder> smallOreDefMap = new HashMap<>();

        for (SmallOres ore : SmallOres.values()) {
            smallOreDefMap.put(ore.smallOreBuilder.smallOreName, ore.smallOreBuilder);
        }

        Multimap<String, OreSmallWrapper> oreSpawning = MultimapBuilder.hashKeys().arrayListValues().build();

        for (GTWorldgen worldGen : GregTechAPI.sWorldgenList) {
            if (!worldGen.mWorldGenName.startsWith("ore.small.")) continue;
            if (!(worldGen instanceof WorldgenGTOreSmallPieces smallOreWorldGen)) continue;

            Materials material = smallOreWorldGen.mMaterial;

            OreSmallWrapper wrapper = new OreSmallWrapper(smallOreDefMap.get(smallOreWorldGen.mWorldGenName));
            SMALL_ORES_BY_NAME.put(worldGen.mWorldGenName, wrapper);

            if (ORE_MAT_TO_DROPS.containsKey(smallOreWorldGen.mMaterial)) {
                throw new IllegalStateException("Duplicate small ore world gen for material " + smallOreWorldGen.mMaterial);
            }

            for (String abbrDimName : wrapper.enabledDims) {
                oreSpawning.put(abbrDimName, wrapper);
            }

            List<ItemStack> stackList = BlockOres2.getPotentialDrops(material, true);

            ORE_MAT_TO_DROPS.put(material, stackList);

            for (ItemStack stack : stackList) {
                ORE_DROP_TO_MAT.put(stack.getUnlocalizedName(), material);
            }

            for (StoneType stoneType : StoneType.STONE_TYPES) {
                SMALL_ORE_LIST.add(BlockOres2.getStack(stoneType, material, true, true, 1));
            }
        }

        for (String abbrDimName : oreSpawning.keySet()) {
            SMALL_ORES_BY_DIM.put(abbrDimName, new SmallOreDimensionWrapper());
        }

        for (var e : oreSpawning.entries()) {
            SMALL_ORES_BY_DIM.get(e.getKey()).smallOres.add(e.getValue());
        }

        SMALL_ORES_BY_DIM.values().forEach(SmallOreDimensionWrapper::calculateWeights);
    }

    public static class OreSmallWrapper {

        public final SmallOreBuilder builder;
        public final String oreGenName;
        public final Materials material;
        public final String worldGenHeightRange;
        public final short amountPerChunk;

        /** {dimension name: enabled} */
        public final Set<String> allowedDimWithOrigNames;
        /** set of: abbriviated dim name */
        public final Set<String> enabledDims;

        public OreSmallWrapper(SmallOreBuilder ore) {
            this.builder = ore;
            this.oreGenName = ore.smallOreName;
            this.material = ore.ore;
            this.worldGenHeightRange = ore.minY + "-" + ore.maxY;
            this.amountPerChunk = (short) ore.amount;

            this.allowedDimWithOrigNames = ore.dimsEnabled;

            this.enabledDims = new HashSet<>();
    
            for (String dimName : ore.dimsEnabled) {
                if (!ore.dimsEnabled.contains(dimName)) {
                    continue;
                }

                this.enabledDims.add(DimensionHelper.getDimAbbreviatedName(dimName));
            }
        }

        public List<ItemStack> getMaterialDrops() {
            List<ItemStack> oreVariants = new ArrayList<>();

            for (StoneType stoneType : StoneType.STONE_TYPES) {
                oreVariants.add(BlockOres2.getStack(stoneType, material, true, false, 1));
            }

            return oreVariants;
        }

        public boolean generatesInDimension(String abbr) {
            return enabledDims.contains(abbr);
        }
    }

    /** Per-dimension small ore metadata for the EoH. */
    public static class SmallOreDimensionWrapper {

        public final ArrayList<OreSmallWrapper> smallOres = new ArrayList<>();
        public final HashMap<OreSmallWrapper, Double> oreVeinProbabilities = new HashMap<>();

        /** Calculate all weights of ore veins once dimension is initialised. */
        private void calculateWeights() {
            int totalWeight = 0;
            for (OreSmallWrapper oreVein : smallOres) {
                totalWeight += oreVein.amountPerChunk;
            }
            for (OreSmallWrapper oreVein : smallOres) {
                oreVeinProbabilities
                    .put(oreVein, ((double) oreVein.amountPerChunk) / ((double) totalWeight));
            }
        }
    }
}
