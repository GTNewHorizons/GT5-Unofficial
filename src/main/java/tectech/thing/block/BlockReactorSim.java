package tectech.thing.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityBlock;
import tectech.Reference;
import tectech.thing.tileEntity.TileEntityReactorSim;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class BlockReactorSim extends Block implements ITileEntityProvider {

    public static BlockReactorSim INSTANCE;
    public static IIcon stuff;

    public BlockReactorSim() {
        super(Material.iron);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("reactorSim");
        setHarvestLevel("wrench", 3);
        setHardness(50);
        setResistance(30);
        setLightOpacity(0);
        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(Reference.MODID + ":blockReactorSimulator");
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean getCanBlockGrass() {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        stuff = blockIcon;
    }

    public static void run() {
        INSTANCE = new BlockReactorSim();
        GameRegistry.registerBlock(INSTANCE, ItemReactorSim.class, INSTANCE.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityReactorSim.class, Reference.MODID + "_reactorSim");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityReactorSim();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a,
        float b, float c) {
        if (entityPlayer.isSneaking()) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(x, y, z);
            return te instanceof IHasGui
                && (!IC2.platform.isSimulating() || IC2.platform.launchGui(entityPlayer, (IHasGui) te));
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block srcBlock) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityBlock) {
            ((TileEntityBlock) te).onNeighborUpdate(srcBlock);
        }
    }
}
