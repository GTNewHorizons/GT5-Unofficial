package common.blocks;


import gregtech.api.GregTech_API;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Any blocks that are used as structure parts for GregTech multi machines
 * have to inherit from this class. Otherwise the checkMachine() method
 * that verifies a machine's structure won't be called correctly.
 */
public abstract class BaseGTUpdateableBlock extends Block {

    protected BaseGTUpdateableBlock(Material material) {
        super(material);
        GregTech_API.registerMachineBlock(this, -1);
        super.setHarvestLevel("wrench", 2);
    }

    @Override
    public int damageDropped(int meta){
        return meta;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }
}
