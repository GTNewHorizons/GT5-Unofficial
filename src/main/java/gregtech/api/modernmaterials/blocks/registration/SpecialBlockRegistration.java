package gregtech.api.modernmaterials.blocks.registration;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialItemBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.special.MasterItemBlockRenderer;
import gregtech.api.modernmaterials.blocks.dumbbase.special.MasterTESR;
import gregtech.api.modernmaterials.ModernMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class SpecialBlockRegistration {

    private static final MasterItemBlockRenderer masterItemBlockRenderer = new MasterItemBlockRenderer();
    private static final MasterTESR masterTESR = new MasterTESR();

    public static void registerTESRBlock(BlocksEnum blockType) {

        final HashSet<ModernMaterial> validMaterials = blockType.getSpecialBlockRenderAssociatedMaterials();

        if (validMaterials.isEmpty()) return;

        GameRegistry.registerTileEntity(blockType.getTileEntityClass(), "TileEntity." + blockType);
        ClientRegistry.bindTileEntitySpecialRenderer(blockType.getTileEntityClass(), masterTESR);

        BaseMaterialBlock block;

        try {
            block = blockType.getBlockClass()
                .getDeclaredConstructor(BlocksEnum.class, HashSet.class)
                .newInstance(blockType, validMaterials);

            GameRegistry.registerBlock(block, BaseMaterialItemBlock.class, "Special." + blockType);

            for (ModernMaterial material : blockType.getSpecialBlockRenderAssociatedMaterials()) {
                blockType.setItem(material, Item.getItemFromBlock(block));
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + blockType, e);
        }

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), masterItemBlockRenderer);

    }
}
