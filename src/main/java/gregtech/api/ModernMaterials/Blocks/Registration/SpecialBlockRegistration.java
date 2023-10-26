package gregtech.api.ModernMaterials.Blocks.Registration;

import static gregtech.api.ModernMaterials.Blocks.Registration.SimpleBlockRegistration.generateIDGroups;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.MasterItemBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.MasterTESR;
import gregtech.api.ModernMaterials.ModernMaterial;

public class SpecialBlockRegistration {

    private static final MasterItemBlockRenderer masterItemBlockRenderer = new MasterItemBlockRenderer();
    private static final MasterTESR masterTESR = new MasterTESR();

    public static void registerTESRBlock(BlocksEnum blockType) {

        // Extract and sort the IDs associated with the materials. We process this in generateIDGroups.
        List<Integer> sortedIDs = blockType.getSpecialBlockRenderAssociatedMaterials()
            .stream()
            .map(ModernMaterial::getMaterialID)
            .sorted()
            .collect(Collectors.toList());

        if (sortedIDs.isEmpty()) return;

        GameRegistry.registerTileEntity(blockType.getTileEntityClass(), "TileEntity." + blockType);
        ClientRegistry.bindTileEntitySpecialRenderer(blockType.getTileEntityClass(), masterTESR);

        int offset = -1;
        for (List<Integer> IDs : generateIDGroups(sortedIDs)) {
            offset++;
            if (IDs.isEmpty()) {
                continue;
            }

            BaseMaterialBlock block;

            try {
                block = blockType.getBlockClass()
                    .getDeclaredConstructor(int.class, List.class)
                    .newInstance(offset, IDs);

                GameRegistry.registerBlock(block, BaseMaterialItemBlock.class, "Special." + blockType + "." + offset);

            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate block.", e);
            }

            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), masterItemBlockRenderer);

        }
    }
}
