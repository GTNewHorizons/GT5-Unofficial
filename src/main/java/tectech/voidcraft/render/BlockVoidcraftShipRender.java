package tectech.voidcraft.render;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The invisible ship-hologram block (plan Phase 3): placed at the gateway (docked ship) or the USS anchor (ship in
 * flight), carrying the ship payload for {@link RenderVoidcraftShip}.
 *
 * <p>
 * Mirrors the legacy EoH render block (non-opaque, no collision, no drops, dedicated TE) but is an entirely
 * new block — none of the legacy render classes are touched.
 */
public class BlockVoidcraftShipRender extends Block {

    public BlockVoidcraftShipRender() {
        super(Material.iron);
        this.setResistance(20f);
        this.setHardness(-1.0f);
        this.setBlockName("Voidcraft Ship Render");
        this.setLightLevel(0.0f);
        registerOther(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("gregtech:iconsets/TRANSPARENT");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canRenderInPass(int a) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityVoidcraftShip();
    }

    public static void registerOther(Block block) {
        String name = block.getUnlocalizedName()
            .substring(
                block.getUnlocalizedName()
                    .indexOf(".") + 1);
        GameRegistry.registerBlock(block, name.substring(name.indexOf(":") + 1));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess worldIn, int x, int y, int z) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return null;
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}
