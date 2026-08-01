package kekztech.common.blocks;

import gregtech.api.enums.Mods;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;

public class BlockGDCUnit extends BaseGTUpdateableBlock {

    private static final BlockGDCUnit instance = new BlockGDCUnit();

    private BlockGDCUnit() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_gdcceramicelectrolyteunit_block";
        instance.setBlockName(blockName);
        instance.setCreativeTab(CreativeTabs.tabMisc);
        instance.setBlockTextureName(Mods.ModIDs.KEKZ_TECH + ":" + "GDCCeramicElectrolyteUnit");
        instance.setHardness(5.0f);
        instance.setResistance(6.0f);
        GameRegistry.registerBlock(instance, blockName);

        return instance;
    }
}
