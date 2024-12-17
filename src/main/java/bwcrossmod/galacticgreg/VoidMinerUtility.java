package bwcrossmod.galacticgreg;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.configs.Configuration;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.enums.DimensionDef.DimNames;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.util.GTUtility;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.WorldgenGTOreSmallPieces;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class VoidMinerUtility {

    public static final FluidStack[] NOBLE_GASSES = { WerkstoffLoader.Neon.getFluidOrGas(1),
        WerkstoffLoader.Krypton.getFluidOrGas(1), WerkstoffLoader.Xenon.getFluidOrGas(1),
        WerkstoffLoader.Oganesson.getFluidOrGas(1) };
    public static final int[] NOBEL_GASSES_MULTIPLIER = { 4, 8, 16, 64 };

    public static class DropMap {

        private float totalWeight;
        private final Map<GTUtility.ItemId, Float> internalMap;

        private final Set<String> voidMinerBlacklistedDrops;

        public DropMap() {
            internalMap = new HashMap<>();
            totalWeight = 0;
            voidMinerBlacklistedDrops = Collections
                .unmodifiableSet(new HashSet<>(Arrays.asList(Configuration.multiblocks.voidMinerBlacklist)));
        }

        /**
         * Method used to add an ore to the DropMap
         *
         * @param weight   the non normalised weight
         * @param isBWOres true for BW ores, false for GT ores
         */
        public void addDrop(IMaterial material, float weight) {
            try (OreInfo<IMaterial> info = OreInfo.getNewInfo()) {
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
            internalMap.merge(ore, weight, Float::sum);
            totalWeight += weight;
        }

        public float getTotalWeight() {
            return totalWeight;
        }

        public Map<GTUtility.ItemId, Float> getInternalMap() {
            return internalMap;
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
        outer: for (WorldgenGTOreLayer layer : WorldgenGTOreLayer.sList) {
            if (!layer.mEnabled) continue;

            for (String dim : layer.mAllowedDimensions) {
                if (dim.equals(DimNames.ENDASTEROIDS)) {
                    if (layer.mAllowedDimensions.contains(DimNames.THE_END)) {
                        continue outer;
                    } else {
                        dim = DimNames.THE_END;
                    }
                }

                DropMap map = dropMapsByDimName.computeIfAbsent(dim, ignored -> new DropMap());

                map.addDrop(layer.mPrimary, layer.mWeight);
                map.addDrop(layer.mSecondary, layer.mWeight);
                map.addDrop(layer.mSporadic, layer.mWeight / 8f);
                map.addDrop(layer.mBetween, layer.mWeight / 8f);
            }
        }

        outer: for (WorldgenGTOreSmallPieces layer : WorldgenGTOreSmallPieces.sList) {
            if (!layer.mEnabled) continue;

            for (String dim : layer.mAllowedDimensions) {
                if (dim.equals(DimNames.ENDASTEROIDS)) {
                    if (layer.mAllowedDimensions.contains(DimNames.THE_END)) {
                        continue outer;
                    } else {
                        dim = DimNames.THE_END;
                    }
                }

                DropMap map = dropMapsByDimName.computeIfAbsent(dim, ignored -> new DropMap());

                map.addDrop(layer.mMaterial, layer.mAmount);
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
    public static void addMaterialToDimensionList(String dimName, IMaterial material, float weight) {
        DropMap map = dropMapsByDimName.computeIfAbsent(dimName, ignored -> new DropMap());

        map.addDrop(material, weight);
    }
}
