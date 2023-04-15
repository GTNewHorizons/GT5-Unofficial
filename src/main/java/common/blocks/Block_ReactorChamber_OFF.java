package common.blocks;

import kekztech.KekzCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.common.registry.GameRegistry;

public class Block_ReactorChamber_OFF extends BaseGTUpdateableBlock {

    private static final Block_ReactorChamber_OFF instance = new Block_ReactorChamber_OFF();

    private Block_ReactorChamber_OFF() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_reactorchamberoff_block";
        instance.setBlockName(blockName);
        instance.setCreativeTab(CreativeTabs.tabMisc);
        instance.setBlockTextureName(KekzCore.MODID + ":" + "ReactorChamber_OFF");
        instance.setHardness(10.0f);
        instance.setResistance(16.0f);
        GameRegistry.registerBlock(instance, blockName);

        return instance;
    }
}
