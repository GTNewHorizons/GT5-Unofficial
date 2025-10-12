package gtneioreplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.SmallOres;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.world.GTWorldgen;
import gregtech.common.SmallOreBuilder;
import gregtech.common.WorldgenGTOreSmallPieces;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class GT5OreSmallHelper {

    public static final List<ItemStack> SMALL_ORE_LIST = new ArrayList<>();
    public static final HashMap<String, OreSmallWrapper> SMALL_ORES_BY_NAME = new HashMap<>();
    public static final HashMap<IOreMaterial, OreSmallWrapper> SMALL_ORES_BY_MAT = new HashMap<>();
    public static final HashMap<String, IOreMaterial> ORE_DROP_TO_MAT = new HashMap<>();
    public static final HashMap<IOreMaterial, List<ItemStack>> ORE_MAT_TO_DROPS = new HashMap<>();
    /** {abbr dim name: wrapper} */
    private static Map<String, SmallOreDimensionWrapper> SMALL_ORES_BY_DIM;

    public static void init() {
        Map<String, SmallOreBuilder> smallOreDefMap = new HashMap<>();

        for (SmallOres ore : SmallOres.values()) {
            smallOreDefMap.put(ore.smallOreBuilder.smallOreName, ore.smallOreBuilder);
        }

        Multimap<String, OreSmallWrapper> oreSpawning = MultimapBuilder.hashKeys()
            .arrayListValues()
            .build();

        OreInfo<IOreMaterial> info = OreInfo.getNewInfo();

        for (GTWorldgen worldGen : GregTechAPI.sWorldgenList) {
            if (!(worldGen instanceof WorldgenGTOreSmallPieces smallOreWorldGen)) continue;

            IOreMaterial material = smallOreWorldGen.getMaterial();

            OreSmallWrapper wrapper = new OreSmallWrapper(smallOreDefMap.get(smallOreWorldGen.mWorldGenName));
            SMALL_ORES_BY_NAME.put(worldGen.mWorldGenName, wrapper);
            SMALL_ORES_BY_MAT.put(smallOreWorldGen.getMaterial(), wrapper);

            if (ORE_MAT_TO_DROPS.containsKey(smallOreWorldGen.getMaterial())) {
                throw new IllegalStateException(
                    "Duplicate small ore world gen for material " + smallOreWorldGen.getMaterial());
            }

            for (String abbrDimName : wrapper.enabledDims) {
                oreSpawning.put(abbrDimName, wrapper);
            }

            info.stoneType = null;
            info.material = material;
            info.isSmall = true;

            List<ItemStack> stackList = OreManager.getPotentialDrops(info);

            ORE_MAT_TO_DROPS.put(material, stackList);

            for (ItemStack stack : stackList) {
                ORE_DROP_TO_MAT.put(stack.getUnlocalizedName(), material);
            }

            info.stoneType = null;
            SMALL_ORE_LIST.add(OreManager.getStack(info, 1));
        }

        info.release();

        HashMap<String, SmallOreDimensionWrapper> byDim = new HashMap<>();

        for (String abbrDimName : oreSpawning.keySet()) {
            byDim.put(abbrDimName, new SmallOreDimensionWrapper());
        }

        for (var e : oreSpawning.entries()) {
            byDim.get(e.getKey()).smallOres.add(e.getValue());
        }

        byDim.values()
            .forEach(SmallOreDimensionWrapper::calculateWeights);

        SMALL_ORES_BY_DIM = ImmutableMap.copyOf(byDim);
    }

    public static Map<String, SmallOreDimensionWrapper> getSmallOresByDim() {
        return SMALL_ORES_BY_DIM;
    }

    public static SmallOreDimensionWrapper getSmallOrebyDim(String abbrName) {
        return SMALL_ORES_BY_DIM.get(abbrName);
    }

    public static class OreSmallWrapper {

        public final String oreGenName;
        public final IOreMaterial material;
        public final String worldGenHeightRange;
        public final short amountPerChunk;

        /** {dimension name: enabled} */
        public final Set<String> allowedDimWithOrigNames;
        /** set of: abbriviated dim name */
        public final Set<String> enabledDims;

        public OreSmallWrapper(SmallOreBuilder ore) {
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

        public List<ItemStack> getOreVariants() {
            List<ItemStack> oreVariants = new ArrayList<>();

            try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
                info.material = material;
                info.isSmall = true;

                for (StoneType stoneType : StoneType.VISUAL_STONE_TYPES) {
                    info.stoneType = stoneType;
                    oreVariants.add(OreManager.getStack(info, 1));
                }
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
                oreVeinProbabilities.put(oreVein, ((double) oreVein.amountPerChunk) / ((double) totalWeight));
            }
        }
    }
}
