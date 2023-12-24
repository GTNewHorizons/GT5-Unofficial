package gregtech.api.modernmaterials.transition;

import com.colen.postea.API.TileEntityReplacementManager;
import com.colen.postea.Utility.BlockInfo;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.BasaltNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.BasaltSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.BlackGraniteNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.BlackGraniteSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.EarthNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.EarthSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.EndNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.EndSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.MarbleNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.MarbleSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.MoonNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.MoonSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.NetherNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.NetherSmallOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.RedGraniteNormalOre;
import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.RedGraniteSmallOre;

public class TransitionBlocks {

    public static void fixWorldBlocks() {
        TileEntityReplacementManager.tileEntityTransformer("GT_TileEntity_Ores", TransitionBlocks::fixTileEntityGTOres);
        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", TransitionBlocks::fixFrameBoxGT);
    }

    private static BlockInfo fixFrameBoxGT(NBTTagCompound nbtTagCompound, World world) {
        short metadata = nbtTagCompound.getShort("mID");

        int materialID = metadata - 4096;

        // Get the item/block for retrieving the ID in this world.
        ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
        if (material == null) throw new RuntimeException("Transition blocks in ModernMaterials failed to map GT framebox with meta " + metadata + " to its new version. This implies that no material with ID + " + materialID + " exists.");
        Item item = BlocksEnum.FrameBox.getItem(material);
        Block block = Block.getBlockFromItem(item);

        // null tile transformer indicates that we will remove this tile entity
        // and replace it with a "dumb block".
        return new BlockInfo(block, materialID, null);

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
            if (dimID == 0) {
                if (metadata / 1000 == 16) return EarthSmallOre;
                if (metadata / 1000 == 19) return BlackGraniteSmallOre;
                if (metadata / 1000 == 20) return RedGraniteSmallOre;
                if (metadata / 1000 == 21) return MarbleSmallOre;
                if (metadata / 1000 == 22) return BasaltSmallOre;
            }
            // Other dimensions. Defaults to Earth if none found.
            return smallOreWorldConverter(dimID);
        } else {
            if (dimID == 0) {
                if (metadata / 1000 == 0) return EarthNormalOre;
                if (metadata / 1000 == 3) return BlackGraniteNormalOre;
                if (metadata / 1000 == 4) return RedGraniteNormalOre;
                if (metadata / 1000 == 5) return MarbleNormalOre;
                if (metadata / 1000 == 6) return BasaltNormalOre;
            }
            // Other dimensions. Defaults to Earth if none found.
            return normalOreWorldConverter(dimID);
        }
    }

    private static BlocksEnum smallOreWorldConverter(int dimensionId) {

        switch (dimensionId) {
            case -1 -> {
                return NetherSmallOre;
            }
            case 0 -> {
                return EarthSmallOre;
            }
            case 1 -> {
                return EndSmallOre;
            }
            case 28 -> {
                return MoonSmallOre;
            }
        }

        return EarthSmallOre;
    }

    private static BlocksEnum normalOreWorldConverter(int dimID) {
        switch (dimID) {
            case -1 -> {
                return NetherNormalOre;
            }
            case 0 -> {
                return EarthNormalOre;
            }
            case 1 -> {
                return EndNormalOre;
            }
            case 28 -> {
                return MoonNormalOre;
            }
        }

        return EarthNormalOre;
    }
}
