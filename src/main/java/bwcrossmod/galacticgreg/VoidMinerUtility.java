package bwcrossmod.galacticgreg;

import static galacticgreg.registry.GalacticGregRegistry.getModContainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.configs.ConfigHandler;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.oregen.BWOreLayer;
import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.GalacticGreg;
import galacticgreg.WorldgenOreLayerSpace;
import galacticgreg.WorldgenOreSmallSpace;
import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTUtility;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.WorldgenGTOreSmallPieces;

public class VoidMinerUtility {

    public static final FluidStack[] NOBLE_GASSES = { WerkstoffLoader.Neon.getFluidOrGas(1),
        WerkstoffLoader.Krypton.getFluidOrGas(1), WerkstoffLoader.Xenon.getFluidOrGas(1),
        WerkstoffLoader.Oganesson.getFluidOrGas(1) };
    public static final int[] NOBEL_GASSES_MULTIPLIER = { 4, 8, 16, 64 };

    public static class DropMap {

        private float totalWeight;
        private final Map<GTUtility.ItemId, Float> internalMap;

        public DropMap() {
            internalMap = new HashMap<>();
            totalWeight = 0;
        }

        /**
         * Method used to add an ore to the DropMap
         *
         * @param weight   the non normalised weight
         * @param isBWOres true for BW ores, false for GT ores
         */
        public void addDrop(int meta, float weight, boolean isBWOres) {
            if (isBWOres) {
                addDrop(WerkstoffLoader.BWOres, meta, weight);
            } else {
                addDrop(GregTechAPI.sBlockOres1, meta, weight);
            }
        }

        /**
         * Method used to add any item to the DropMap. Will be blocked if blacklisted.
         *
         * @param weight the non normalised weight
         */
        public void addDrop(Block block, int meta, float weight) {
            if (ConfigHandler.voidMinerBlacklist.contains(
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
            if (ConfigHandler.voidMinerBlacklist.contains(
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

    public static final Map<Integer, DropMap> dropMapsByDimId = new HashMap<>();
    public static final Map<String, DropMap> dropMapsByChunkProviderName = new HashMap<>();
    public static final Map<Integer, DropMap> extraDropsDimMap = new HashMap<>();

    // Adds tellurium to OW to ensure a way to get it, as it's used in Magneto Resonatic
    // Dust and Circuit Compound MK3 Dust
    static {
        addMaterialToDimensionList(0, Materials.Tellurium, 8.0f);
    }

    /**
     * Computes the ores of the dims
     */
    public static void generateDropMaps() {
        // vanilla dims
        dropMapsByDimId.put(-1, getDropMapVanilla(-1));
        dropMapsByDimId.put(0, getDropMapVanilla(0));
        dropMapsByDimId.put(1, getDropMapVanilla(1));
        // Twilight Forest
        dropMapsByDimId.put(7, getDropMapVanilla(7));

        // ross dims
        dropMapsByDimId.put(ConfigHandler.ross128BID, getDropMapRoss(ConfigHandler.ross128BID));
        dropMapsByDimId.put(ConfigHandler.ross128BAID, getDropMapRoss(ConfigHandler.ross128BAID));

        // other space dims
        for (ModContainer modContainer : getModContainers()) {
            for (ModDimensionDef dimDef : modContainer.getDimensionList()) {
                dropMapsByChunkProviderName.put(dimDef.getChunkProviderName(), getDropMapSpace(dimDef));
            }
        }
    }

    /**
     * Method to generate a DropMap that contains ores of a vanilla GT worldgen
     */
    private static DropMap getDropMapVanilla(int dimId) {
        DropMap dropMap = new DropMap();

        // Ore Veins
        Predicate<WorldgenGTOreLayer> oreLayerPredicate = makeOreLayerPredicate(dimId);
        WorldgenGTOreLayer.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen))
            .forEach(element -> {
                dropMap.addDrop(element.mPrimaryMeta, element.mWeight, false);
                dropMap.addDrop(element.mSecondaryMeta, element.mWeight, false);
                dropMap.addDrop(element.mSporadicMeta, element.mWeight / 8f, false);
                dropMap.addDrop(element.mBetweenMeta, element.mWeight / 8f, false);
            });

        // Small Ores
        Predicate<WorldgenGTOreSmallPieces> smallOresPredicate = makeSmallOresPredicate(dimId);
        WorldgenGTOreSmallPieces.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen))
            .forEach(element -> dropMap.addDrop(element.mMeta, element.mAmount, false));
        return dropMap;
    }

    /**
     * Makes a predicate for the GT normal ore veins worldgen
     *
     * @return the predicate
     */
    private static Predicate<WorldgenGTOreLayer> makeOreLayerPredicate(int dimensionId) {
        return switch (dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd || gt_worldgen.mEndAsteroid;
            case 7 -> gt_worldgen -> gt_worldgen.twilightForest;
            default -> throw new IllegalStateException();
        };
    }

    /**
     * Makes a predicate for the GT normal small ore worldgen
     *
     * @return the predicate
     */
    private static Predicate<WorldgenGTOreSmallPieces> makeSmallOresPredicate(int dimensionId) {
        return switch (dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd;
            case 7 -> gt_worldgen -> gt_worldgen.twilightForest;
            default -> throw new IllegalStateException();
        };
    }

    /**
     * Create a DropMap that contains ores of Ross dims
     *
     * @param aID dim id of Ross128b or Ross128ba
     */
    private static DropMap getDropMapRoss(int aID) {
        DropMap dropMap = new DropMap();
        for (BWOreLayer oreLayer : BWOreLayer.sList) {
            if (oreLayer.mEnabled && oreLayer.isGenerationAllowed("", aID, 0)) {
                List<ItemStack> data = oreLayer.getStacks();
                dropMap.addDrop(data.get(0), oreLayer.mWeight);
                dropMap.addDrop(data.get(1), oreLayer.mWeight);
                dropMap.addDrop(data.get(2), oreLayer.mWeight / 8f);
                dropMap.addDrop(data.get(3), oreLayer.mWeight / 8f);
            }
        }
        return dropMap;
    }

    /**
     * Create a DropMap contains the ores from the galacticGreg space worldgen corresponding to the target dim
     *
     * @param finalDef ModDimensionDef corresponding to the target dim
     */
    private static DropMap getDropMapSpace(ModDimensionDef finalDef) {
        DropMap dropMap = new DropMap();

        // Normal Ore Veins
        GalacticGreg.oreVeinWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof WorldgenOreLayerSpace oreLayerSpace
                    && oreLayerSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (WorldgenOreLayerSpace) gt_worldgen)
            .forEach(element -> {
                dropMap.addDrop(element.mPrimaryMeta, element.mWeight, false);
                dropMap.addDrop(element.mSecondaryMeta, element.mWeight, false);
                dropMap.addDrop(element.mSporadicMeta, element.mWeight / 8f, false);
                dropMap.addDrop(element.mBetweenMeta, element.mWeight / 8f, false);
            });

        // Normal Small Ores
        GalacticGreg.smallOreWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof WorldgenOreSmallSpace oreSmallPiecesSpace
                    && oreSmallPiecesSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (WorldgenOreSmallSpace) gt_worldgen)
            .forEach(element -> dropMap.addDrop(element.mMeta, element.mAmount, false));
        return dropMap;
    }

    public static void addBlockToDimensionList(int dimId, Block block, int meta, float weight) {
        if (!extraDropsDimMap.containsKey(dimId)) {
            extraDropsDimMap.put(dimId, new DropMap());
        }
        extraDropsDimMap.get(dimId)
            .addDrop(block, meta, weight);
    }

    /**
     * Public method giving other mods the ability to add manually a material with an ore version into the external
     * dropMap for a specified dim id
     *
     * @param DimensionID the dim id targeted
     * @param Material    the material with an ore version
     * @param weight      the non normalised version of the given weight
     */
    public static void addMaterialToDimensionList(int DimensionID, ISubTagContainer Material, float weight) {
        if (Material instanceof Materials gtMaterial) {
            addBlockToDimensionList(DimensionID, GregTechAPI.sBlockOres1, gtMaterial.mMetaItemSubID, weight);
        } else if (Material instanceof Werkstoff werkstoff) {
            addBlockToDimensionList(DimensionID, WerkstoffLoader.BWOres, werkstoff.getmID(), weight);
        }
    }
}
