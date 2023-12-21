package gregtech.api.modernmaterials.blocks.registration;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import gregtech.api.modernmaterials.ModernMaterial;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialItemBlock;

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

}
