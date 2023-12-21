package gregtech.api.modernmaterials.transition;

import com.colen.postea.API.TileEntityReplacementManager;
import com.colen.postea.Utility.BlockInfo;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TransitionBlocks {

    public static void fixWorldBlocks() {
        TileEntityReplacementManager.tileEntityTransformer("GT_TileEntity_Ores", TransitionBlocks::fixTileEntityGTOres);
    }

    private static BlockInfo fixTileEntityGTOres(NBTTagCompound nbtTagCompound, World world) {
        short metadata = nbtTagCompound.getShort("m");
        int materialID = metadata % 1000;

        // Get the new enum for this block depending on dimension.
        BlocksEnum blocksEnum = getBlocksEnumFromWorld(metadata, world);

        // Get the item/block for retrieving the ID in this world.
        ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
        if (material == null) throw new RuntimeException("Transition blocks in ModernMaterials failed to map GT ore with meta " + metadata + " to its new version. This implies that no material with ID + " + materialID + " exists.");
        Item item = blocksEnum.getItem(material);
        Block block = Block.getBlockFromItem(item);

        // null tile transformer indicates that we will remove this tile entity
        // and replace it with a "dumb block".
        return new BlockInfo(block, materialID, null);
    }

    private static BlocksEnum getBlocksEnumFromWorld(int metadata, World world) {
        int dimID = world.provider.dimensionId;
        // Normal ores are 0-6000
        // Small ores are 16000-22000
        if (metadata > 10000) {
            if (dimID == 1) {
                if (metadata / 1000 == 16) return BlocksEnum.EarthSmallOre;
                if (metadata / 1000 == 19) return BlocksEnum.BlackGraniteSmallOre;
                if (metadata / 1000 == 20) return BlocksEnum.RedGraniteSmallOre;
                if (metadata / 1000 == 21) return BlocksEnum.MarbleSmallOre;
                if (metadata / 1000 == 22) return BlocksEnum.BasaltSmallOre;
            }
            // Other dimensions. Defaults to Earth if none found.
            return smallOreWorldConverter(dimID);
        } else {
            if (dimID == 1) {
                if (metadata / 1000 == 0) return BlocksEnum.EarthNormalOre;
                if (metadata / 1000 == 3) return BlocksEnum.BlackGraniteNormalOre;
                if (metadata / 1000 == 4) return BlocksEnum.RedGraniteNormalOre;
                if (metadata / 1000 == 5) return BlocksEnum.MarbleNormalOre;
                if (metadata / 1000 == 6) return BlocksEnum.BasaltNormalOre;
            }
            // Other dimensions. Defaults to Earth if none found.
            return normalOreWorldConverter(dimID);
        }
    }

    private static BlocksEnum smallOreWorldConverter(int dimensionId) {

        switch (dimensionId) {
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

    private static BlocksEnum normalOreWorldConverter(int dimID) {
        switch (dimID) {
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
