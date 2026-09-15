package kekztech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;

public class BlockYSZUnit extends BaseGTUpdateableBlock {

    private static final BlockYSZUnit instance = new BlockYSZUnit();

    private BlockYSZUnit() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_yszceramicelectrolyteunit_block";
        instance.setBlockName(blockName);
        instance.setCreativeTab(CreativeTabs.tabMisc);
        instance.setBlockTextureName(Mods.ModIDs.KEKZ_TECH + ":" + "YSZCeramicElectrolyteUnit");
        instance.setHardness(5.0f);
        instance.setResistance(6.0f);
        GameRegistry.registerBlock(instance, blockName);

        return instance;
    }
}
