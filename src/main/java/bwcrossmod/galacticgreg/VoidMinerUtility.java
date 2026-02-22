package bwcrossmod.galacticgreg;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.configs.Configuration;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.DimensionDef.DimNames;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.objects.DiscreteDistribution;
import gregtech.api.util.GTUtility;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.WorldgenGTOreSmallPieces;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;

public class VoidMinerUtility {

    public static final FluidStack[] NOBLE_GASSES = { WerkstoffLoader.Neon.getFluidOrGas(1),
        WerkstoffLoader.Krypton.getFluidOrGas(1), WerkstoffLoader.Xenon.getFluidOrGas(1),
        WerkstoffLoader.Oganesson.getFluidOrGas(1) };
    public static final int[] NOBLE_GASSES_MULTIPLIER = { 4, 8, 16, 64 };

    public static class DropMap {

        private boolean isAliasCached;
        private DiscreteDistribution discreteDistribution;

        private float totalWeight;
        private double[] oreWeights;
        private GTUtility.ItemId[] ores;
        private final Object2FloatOpenHashMap<GTUtility.ItemId> internalMap;

        private final Set<String> voidMinerBlacklistedDrops;

        public DropMap() {
            internalMap = new Object2FloatOpenHashMap<>();
            totalWeight = 0;
            voidMinerBlacklistedDrops = Collections
                .unmodifiableSet(new HashSet<>(Arrays.asList(Configuration.multiblocks.voidMinerBlacklist)));
        }

        /**
         * Method used to add an ore to the DropMap
         *
         * @param material the material
         * @param weight   the non normalised weight
         */
        public void addDrop(IOreMaterial material, float weight) {
            try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
                info.material = material;

                ItemStack stack = OreManager.getStack(info, 1);

                if (stack == null) {
                    GTMod.GT_FML_LOGGER.error("Could not add ore " + material + " to void miner drop map!");
                    return;
                }

                addDrop(stack, weight);
            }
        }

        /**
         * Method used to add any item to the DropMap. Will be blocked if blacklisted.
         *
         * @param weight the non normalised weight
         */
        public void addDrop(Block block, int meta, float weight) {
            if (this.voidMinerBlacklistedDrops.contains(
                String.format(
                    "%s:%d",
                    GameRegistry.findUniqueIdentifierFor(block)
                        .toString(),
                    meta)))
                return;
            Item item = Item.getItemFromBlock(block);
            addDrop(item, meta, weight);
        }

        /**
         * Method used to add any item to the DropMap. Will be blocked if blacklisted.
         *
         * @param weight the non normalised weight
         */
        public void addDrop(ItemStack itemStack, float weight) {
            Item item = itemStack.getItem();
            int meta = Items.feather.getDamage(itemStack);
            if (this.voidMinerBlacklistedDrops.contains(
                String.format(
                    "%s:%d",
                    GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(item))
                        .toString(),
                    meta)))
                return;
            addDrop(item, meta, weight);
        }

        private void addDrop(Item item, int meta, float weight) {
            GTUtility.ItemId ore = GTUtility.ItemId.createNoCopy(item, meta, null);
            internalMap.addTo(ore, weight);
            totalWeight += weight;
        }

        private void mergeDropMaps(DropMap dropMap) {
            if (dropMap == null || dropMap.internalMap == null || dropMap.internalMap.isEmpty()) return;

            for (Map.Entry<GTUtility.ItemId, Float> entry : dropMap.internalMap.entrySet()) {
                // We cant be sure that the extraDropMap entries are intentional duplicates of this DropMap
                this.internalMap.merge(entry.getKey(), entry.getValue(), Float::sum);
                totalWeight += entry.getValue();
            }
        }

        /**
         * Method used to compute the ore distribution for the VM if it doesn't exist.
         *
         * @param extraDropMap the extraDropMap that is related to this DropMap
         */
        public void isDistributionCached(@Nullable DropMap extraDropMap) {
            if (isAliasCached) return;
            if (internalMap == null || internalMap.isEmpty()) {
                if (extraDropMap == null || extraDropMap.internalMap == null || extraDropMap.internalMap.isEmpty())
                    return;
            }

            // Merge a related extraDropMap if it exists
            mergeDropMaps(extraDropMap);

            ores = new GTUtility.ItemId[internalMap.size()];
            oreWeights = new double[internalMap.size()];
            int i = 0;

            for (Map.Entry<GTUtility.ItemId, Float> entry : internalMap.entrySet()) {
                ores[i] = entry.getKey();
                oreWeights[i] = entry.getValue() / totalWeight;
                i++;
            }

            discreteDistribution = new DiscreteDistribution(oreWeights);
            isAliasCached = true;
        }

        public float getTotalWeight() {
            return totalWeight;
        }

        public GTUtility.ItemId[] getOres() {
            return ores;
        }

        public Map<GTUtility.ItemId, Float> getInternalMap() {
            return internalMap;
        }

        public GTUtility.ItemId nextOre() {
            return ores[discreteDistribution.next()];
        }
    }

    /** {full dim name: drop map} */
    public static final Map<String, DropMap> dropMapsByDimName = new HashMap<>();
    /** {full dim name: non-vein drop map} */
    public static final Map<String, DropMap> extraDropsByDimName = new HashMap<>();

    // Adds tellurium to OW to ensure a way to get it, as it's used in Magneto Resonatic
    // Dust and Circuit Compound MK3 Dust
    static {
        addMaterialToDimensionList(DimNames.OW, Materials.Tellurium, 8.0f);
    }

    /**
     * Computes the ores of the dims
     */
    public static void generateDropMaps() {
        for (WorldgenGTOreLayer layer : WorldgenGTOreLayer.sList) {
            if (!layer.mEnabled) continue;

            for (String dim : layer.getAllowedDimensions()) {
                ModDimensionDef dimensionDef = DimensionDef.getDefByName(dim);

                if (dimensionDef != null && !dimensionDef.canBeVoidMined()) continue;

                DropMap map = dropMapsByDimName.computeIfAbsent(dim, ignored -> new DropMap());

                map.addDrop(layer.mPrimary, layer.mWeight);
                map.addDrop(layer.mSecondary, layer.mWeight);
                map.addDrop(layer.mSporadic, layer.mWeight / 8f);
                map.addDrop(layer.mBetween, layer.mWeight / 8f);
            }
        }

        for (WorldgenGTOreSmallPieces layer : WorldgenGTOreSmallPieces.sList) {
            if (!layer.mEnabled) continue;

            for (String dim : layer.getAllowedDimensions()) {
                ModDimensionDef dimensionDef = DimensionDef.getDefByName(dim);

                if (dimensionDef != null && !dimensionDef.canBeVoidMined()) continue;

                DropMap map = dropMapsByDimName.computeIfAbsent(dim, ignored -> new DropMap());

                map.addDrop(layer.getMaterial(), layer.mAmount);
            }
        }
    }

    public static void addBlockToDimensionList(String dimName, Block block, int meta, float weight) {
        DropMap map = dropMapsByDimName.computeIfAbsent(dimName, ignored -> new DropMap());

        map.addDrop(block, meta, weight);
    }

    /**
     * Public method giving other mods the ability to add manually a material with an ore version into the external
     * dropMap for a specified dim id
     *
     * @param dimName  the full dim name of the dim to target
     * @param material the material with an ore version
     * @param weight   the non normalised version of the given weight
     */
    public static void addMaterialToDimensionList(String dimName, IOreMaterial material, float weight) {
        DropMap map = dropMapsByDimName.computeIfAbsent(dimName, ignored -> new DropMap());

        map.addDrop(material, weight);
    }
}
