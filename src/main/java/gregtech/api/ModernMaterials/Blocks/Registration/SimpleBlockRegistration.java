package gregtech.api.ModernMaterials.Blocks.Registration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;

public abstract class SimpleBlockRegistration {

    /**
     * Registers a simple block with the game based on the provided block type and valid material IDs.
     *
     * @param blockType The type of block to be registered.
     */
    public static void registerSimpleBlock(BlocksEnum blockType) {

        final HashSet<ModernMaterial> validMaterials = blockType.getSimpleBlockRenderAssociatedMaterials();

        if (validMaterials.isEmpty()) return;

        try {
            BaseMaterialBlock block = blockType.getBlockClass()
                .getDeclaredConstructor(BlocksEnum.class, HashSet.class)
                .newInstance(blockType, validMaterials);
            GameRegistry.registerBlock(block, BaseMaterialItemBlock.class, String.valueOf(blockType));

            for (ModernMaterial material : blockType.getSimpleBlockRenderAssociatedMaterials()) {
                blockType.setItem(material, Item.getItemFromBlock(block));
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + blockType, e);
        }
    }

    public static List<List<Integer>> generateIDGroups(List<Integer> sortedIDs) {

        List<List<Integer>> groupedIDs = new ArrayList<>();

        // Process each ID from the sorted list
        for (int currentID : sortedIDs) {
            int groupIndex = calculateGroupIndex(currentID);

            // Ensure the groupedIDs list has a list initialized for this groupIndex
            ensureGroupExists(groupedIDs, groupIndex);

            groupedIDs.get(groupIndex)
                .add(currentID);
        }

        return groupedIDs;
    }

    // Calculate the group index for the given ID
    private static int calculateGroupIndex(int id) {
        return id / 16;
    }

    // Ensure that the groupedIDs list has a list initialized for the given groupIndex
    private static void ensureGroupExists(List<List<Integer>> groupedIDs, int groupIndex) {
        while (groupedIDs.size() <= groupIndex) {
            groupedIDs.add(new ArrayList<>());
        }
    }

}
