package gregtech.loaders.preload;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.BWMetaGeneratedBlocksCasing;
import bartworks.system.material.BWMetaGeneratedWerkstoffBlocks;
import bartworks.system.material.BWTileEntityMetaGeneratedBlocksCasing;
import bartworks.system.material.BWTileEntityMetaGeneratedBlocksCasingAdvanced;
import bartworks.system.material.BWTileEntityMetaGeneratedWerkstoffBlock;
import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.common.ores.BWOreAdapter;

/// The storage, casing and ore blocks the bartworks werkstoff materials were placed as. Their MaterialLib
/// shapes supersede them, but the blocks stay registered: a saved world still holds them, and the Postea
/// migration and the ore adapters address them by their original registration names and metadata.
public class LoaderLegacyBartworksBlocks {

    public static Block storageBlocks;
    public static Block casings;
    public static Block casingsAdvanced;

    private LoaderLegacyBartworksBlocks() {}

    public static void register() {
        GameRegistry.registerTileEntity(BWTileEntityMetaGeneratedWerkstoffBlock.class, "bw.werkstoffblockTE");
        GameRegistry.registerTileEntity(BWTileEntityMetaGeneratedBlocksCasing.class, "bw.werkstoffblockcasingTE");
        GameRegistry.registerTileEntity(
            BWTileEntityMetaGeneratedBlocksCasingAdvanced.class,
            "bw.werkstoffblockscasingadvancedTE");

        BWOreAdapter.INSTANCE.init();

        storageBlocks = new BWMetaGeneratedWerkstoffBlocks(
            net.minecraft.block.material.Material.iron,
            BWTileEntityMetaGeneratedWerkstoffBlock.class,
            "bw.werkstoffblocks");
        casings = new BWMetaGeneratedBlocksCasing(
            net.minecraft.block.material.Material.iron,
            BWTileEntityMetaGeneratedBlocksCasing.class,
            "bw.werkstoffblockscasing",
            OrePrefixes.blockCasing);
        casingsAdvanced = new BWMetaGeneratedBlocksCasing(
            net.minecraft.block.material.Material.iron,
            BWTileEntityMetaGeneratedBlocksCasingAdvanced.class,
            "bw.werkstoffblockscasingadvanced",
            OrePrefixes.blockCasingAdvanced);
    }

    /// Hides the legacy slot of any material whose MaterialLib block resolves, so only the replacement lists.
    public static void hideSupersededSlots() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            hideSlot(material, OrePrefixes.block, storageBlocks);
            hideSlot(material, OrePrefixes.blockCasing, casings);
            hideSlot(material, OrePrefixes.blockCasingAdvanced, casingsAdvanced);
        }
    }

    private static void hideSlot(Material material, OrePrefixes prefix, Block legacyBlock) {
        if (legacyBlock == null) return;
        if (!LegacyWerkstoffIndex.generatesPrefix(material, prefix)) return;
        if (MaterialParts.stack(prefix, material, 1) == null) return;
        API.hideItem(new ItemStack(legacyBlock, 1, LegacyWerkstoffIndex.idOf(material)));
    }
}
