package gregtech.api.modernmaterials.transition;

import com.colen.postea.API.BlockReplacementManager;
import com.colen.postea.API.TileEntityReplacementManager;
import com.colen.postea.Utility.BlockConversionInfo;
import com.colen.postea.Utility.BlockInfo;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TransitionBlocks {

    public static void fixWorldBlocks() {
        //BlockReplacementManager.addBlockReplacement("gt:blockores", TransitionBlocks::fixBlock);
        TileEntityReplacementManager.tileEntityTransformer("GT_TileEntity_Ores", TransitionBlocks::eraseTileEntity);
    }

    private static BlockInfo eraseTileEntity(NBTTagCompound nbtTagCompound, World world) {
        boolean isSmall = nbtTagCompound.getShort("m") > 10000;
        int materialID = nbtTagCompound.getShort("m") % 1000;

        // Get the new enum for this block depending on dimension.
        BlocksEnum blocksEnum = getBlocksEnumFromWorld(isSmall, world);

        // Get the item/block for retrieving the ID in this world.
        ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
        if (material == null) return null;
        Item item = blocksEnum.getItem(material);
        Block block = Block.getBlockFromItem(item);

        // null tile transformer indicates that we will remove this tile entity
        // and replace it with a "dumb block".
        return new BlockInfo(block, materialID, null);
    }

//    private static BlockConversionInfo fixBlock(BlockConversionInfo blockConversionInfo, World world) {
//        blockConversionInfo.metadata = 35;
//        int materialID = blockConversionInfo.metadata;
//
//        // Get the new enum for this block depending on dimension.
//        BlocksEnum blocksEnum = getBlocksEnumFromWorld(blockConversionInfo.metadata, world);
//
//        // Get the item/block for retrieving the ID in this world.
//        Item item = blocksEnum.getItem(ModernMaterial.getMaterialFromID(materialID));
//        Block block = Block.getBlockFromItem(item);
//
//        blockConversionInfo.blockID = Block.getIdFromBlock(block);
//
//        return blockConversionInfo;
//    }

    private static BlocksEnum getBlocksEnumFromWorld(boolean isSmallOre, World world) {
        if (isSmallOre) {
            return smallOreWorldConverter(world);
        } else {
            return normalOreWorldConverter(world);
        }
    }

    private static BlocksEnum smallOreWorldConverter(World world) {

        switch (world.provider.dimensionId) {
            case -1 -> {
                return BlocksEnum.NetherSmallOre;
            }
            case 0 -> {
                return BlocksEnum.EarthSmallOre;
            }
            case 1 -> {
                return BlocksEnum.EndSmallOre;
            }
        }

        return BlocksEnum.EarthSmallOre;
    }

    private static BlocksEnum normalOreWorldConverter(World world) {
        switch (world.provider.dimensionId) {
            case -1 -> {
                return BlocksEnum.NetherNormalOre;
            }
            case 0 -> {
                return BlocksEnum.EarthNormalOre;
            }
            case 1 -> {
                return BlocksEnum.EndNormalOre;
            }
        }

        return BlocksEnum.EarthNormalOre;
    }
}
