package gregtech.api.ModernMaterials.Blocks.Registration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumb;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumbItemBlock;
import gregtech.api.ModernMaterials.ModernMaterial;

public abstract class SimpleBlockRegistration {

    /**
     * Registers a simple block with the game based on the provided block type and valid material IDs.
     *
     * @param blockType The type of block to be registered.
     */
    public static void registerSimpleBlock(BlocksEnum blockType) {

        // Extract the materials associated with the given block type
        HashSet<ModernMaterial> associatedMaterials = blockType.getSimpleBlockRenderAssociatedMaterials();

        // Extract and sort the IDs associated with the materials. We process this in generateIDGroups.
        List<Integer> sortedIDs = associatedMaterials.stream()
            .map(ModernMaterial::getMaterialID)
            .sorted()
            .collect(Collectors.toList());

        int offset = -1;
        for (List<Integer> IDs : generateIDGroups(sortedIDs)) {
            offset++;
            if (IDs.isEmpty()) {
                continue;
            }

            try {
                NewDumb block = blockType.getBlockClass()
                    .getDeclaredConstructor(int.class, List.class)
                    .newInstance(offset, IDs);
                GameRegistry.registerBlock(block, NewDumbItemBlock.class, blockType + "." + offset);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate block", e);
            }
        }
    }

    public static List<List<Integer>> generateIDGroups(List<Integer> sortedIDs) {
        // Handle null or empty input
        if (sortedIDs == null || sortedIDs.isEmpty()) {
            return Collections.emptyList();
        }

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
