package gregtech.api.ModernMaterials.Blocks.Registration;

import static gregtech.api.ModernMaterials.Blocks.Registration.SimpleBlockRegistration.generateIDGroups;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumb;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumbItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.MasterTESR;
import gregtech.api.ModernMaterials.ModernMaterial;

public class SpecialBlockRegistration {

    public static void registerTESRBlock(BlocksEnum blockType) {

        // Extract and sort the IDs associated with the materials. We process this in generateIDGroups.
        List<Integer> sortedIDs = blockType.getSpecialBlockRenderAssociatedMaterials().stream()
            .map(ModernMaterial::getMaterialID)
            .sorted()
            .collect(Collectors.toList());

        GameRegistry.registerTileEntity(blockType.getTileEntityClass(), "TileEntity." + blockType);
        ClientRegistry.bindTileEntitySpecialRenderer(blockType.getTileEntityClass(), new MasterTESR());

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

                GameRegistry.registerBlock(block, NewDumbItemBlock.class, "Special." + blockType + "." + offset);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate block.", e);
            }
        }
    }
}
