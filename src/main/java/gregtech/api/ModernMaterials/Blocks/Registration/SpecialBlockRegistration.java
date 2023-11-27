package gregtech.api.ModernMaterials.Blocks.Registration;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.MasterItemBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.MasterTESR;
import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.lang.reflect.InvocationTargetException;

public class SpecialBlockRegistration {

    private static final MasterItemBlockRenderer masterItemBlockRenderer = new MasterItemBlockRenderer();
    private static final MasterTESR masterTESR = new MasterTESR();

    public static void registerTESRBlock(BlocksEnum blockType) {

        if (blockType.getSpecialBlockRenderAssociatedMaterials().isEmpty()) return;

        GameRegistry.registerTileEntity(blockType.getTileEntityClass(), "TileEntity." + blockType);
        ClientRegistry.bindTileEntitySpecialRenderer(blockType.getTileEntityClass(), masterTESR);

        BaseMaterialBlock block;

        try {
            block = blockType.getBlockClass()
                .getDeclaredConstructor(BlocksEnum.class)
                .newInstance(blockType);

            GameRegistry.registerBlock(block, BaseMaterialItemBlock.class, "Special." + blockType);

            for (ModernMaterial material : blockType.getSpecialBlockRenderAssociatedMaterials()) {
                blockType.setItem(material, Item.getItemFromBlock(block));
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate block.", e);
        }

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), masterItemBlockRenderer);

    }
}
