package common.blocks;

import common.tileentities.TE_BeamTransmitter;
import common.tileentities.TE_SpaceElevatorTether;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Block_BeamTransmitter extends Block {

    private static final Block_BeamTransmitter INSTANCE = new Block_BeamTransmitter();

    private Block_BeamTransmitter() {
        super(Material.glass);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_beamtransmitter_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(5.0f);
        INSTANCE.setResistance(5.0f);
        INSTANCE.setBlockTextureName(KekzCore.MODID + ":" + "Tether_top");
        GameRegistry.registerBlock(INSTANCE, blockName);

        return INSTANCE;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TE_BeamTransmitter();
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

}
