package gregtech.api.modernmaterials.blocks.registration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialItemBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.special.MasterItemBlockRenderer;
import gregtech.api.modernmaterials.blocks.dumbbase.special.MasterTESR;

public class SpecialBlockRegistration {

    public static void registerTESRBlock(BlocksEnum blockType) {

        final HashSet<ModernMaterial> validMaterials = blockType.getSpecialBlockRenderAssociatedMaterials();

        if (validMaterials.isEmpty()) return;

        GameRegistry.registerTileEntity(blockType.getTileEntityClass(), "TileEntity." + blockType);

        BaseMaterialBlock block;
        Item itemBlock;

        try {
            block = blockType.getBlockClass()
                .getDeclaredConstructor(BlocksEnum.class, HashSet.class)
                .newInstance(blockType, validMaterials);

            itemBlock = Item.getItemFromBlock(block);

            GameRegistry.registerBlock(block, BaseMaterialItemBlock.class, "Special." + blockType);

            for (ModernMaterial material : blockType.getSpecialBlockRenderAssociatedMaterials()) {
                itemBlock = Item.getItemFromBlock(block);
                blockType.setItem(material, itemBlock);
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + blockType, e);
        }

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {
            registerTESRRenderer(blockType, itemBlock);
        }
    }

    // We must do all of this on the client only or it will crash in multiplayer.
    @SideOnly(Side.CLIENT)
    private static MasterTESR masterTESR;

    @SideOnly(Side.CLIENT)
    private static MasterItemBlockRenderer masterItemBlockRenderer;

    @SideOnly(Side.CLIENT)
    private static void registerTESRRenderer(BlocksEnum blockType, Item itemBlock) {

        // This is needed to stop instantiation on server side.
        if (masterTESR == null) masterTESR = new MasterTESR();
        if (masterItemBlockRenderer == null) masterItemBlockRenderer = new MasterItemBlockRenderer();

        ClientRegistry.bindTileEntitySpecialRenderer(blockType.getTileEntityClass(), masterTESR);
        MinecraftForgeClient.registerItemRenderer(itemBlock, masterItemBlockRenderer);
    }
}
