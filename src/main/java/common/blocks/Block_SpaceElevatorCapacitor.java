package common.blocks;

import common.itemBlocks.IB_SpaceElevator;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class Block_SpaceElevatorCapacitor extends BaseGTUpdateableBlock {

    private static final Block_SpaceElevatorCapacitor INSTANCE = new Block_SpaceElevatorCapacitor();

    private IIcon top;
    private IIcon side;

    private Block_SpaceElevatorCapacitor() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_spaceelevatorcapacitor_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(5.0f);
        INSTANCE.setResistance(3.0f);
        GameRegistry.registerBlock(INSTANCE, blockName);

        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        top = ir.registerIcon("kekztech:SpaceElevatorCapacitor_top_fullbase");
        side = ir.registerIcon("kekztech:SpaceElevatorCapacitor_side_fullbase");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side < 2) ? this.top : this.side;
    }

    @Override
    public int getLightValue() {
        return 2;
    }
}
