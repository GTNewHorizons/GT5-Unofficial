package gtnhintergalactic.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gtnhintergalactic.GTNHIntergalactic;
import gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

/**
 * Block for the Space Elevator Cable
 *
 * @author minecraft7771
 */
public class BlockSpaceElevatorCable extends Block implements ITileEntityProvider {

    /** Texture of the block */
    public static IIcon[] textures;
    /** Texture for the motor glow */
    public static IIcon motorGlow;
    /** Render ID of this block type */
    private static int renderID;
    /** Number of steps which the cable light has */
    public static final int LIGHT_STEPS = 80;

    /**
     * Create a new Space Elevator cable block
     */
    public BlockSpaceElevatorCable() {
        super(Material.iron);
        setBlockName("SpaceElevatorCable");
        setCreativeTab(GTNHIntergalactic.tab);
        setHarvestLevel("pickaxe", 2);
        ItemList.SpaceElevatorCable.set(new ItemStack(this));
    }

    /**
     * Register the block icons
     *
     * @param register Register to which the icons will be added
     */
    @Override
    public void registerBlockIcons(IIconRegister register) {
        textures = new IIcon[2 + LIGHT_STEPS];
        textures[0] = register.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/CablePart");
        textures[1] = register.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/CableLight");
        motorGlow = register.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/motorGlow");
        for (int i = 1; i < LIGHT_STEPS + 1; i++) {
            textures[i + 1] = register
                .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/cable/CableLightBlinking" + i);
        }
    }

    /**
     * Get the icon for this block
     *
     * @param side Side for which the icon should be gotten
     * @param meta Meta of the block
     * @return Icon of the block
     */
    @Override
    public IIcon getIcon(int side, int meta) {
        return textures[0];
    }

    /**
     * Set the render ID of this block type
     *
     * @param id New ID
     */
    public static void setRenderID(int id) {
        renderID = id;
    }

    /**
     * @return render ID of this block type
     */
    public static int getRenderID() {
        return renderID;
    }

    /**
     * @return render type of this block instance
     */
    @Override
    public int getRenderType() {
        return renderID;
    }

    /**
     * @return Whether to render as normal block or not
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Explosion resistance of this block
     *
     * @param entity Entity that caused the explosion
     * @return Explosion resistance
     */
    @Override
    public float getExplosionResistance(Entity entity) {
        return 6.0f;
    }

    /**
     * Get the hardness value of this block
     *
     * @param world World in which the block exists
     * @param x     X coordinate of the block
     * @param y     Y coordinate of the block
     * @param z     Z coordinate of the block
     * @return Hardness value of this block
     */
    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 1.0f;
    }

    /**
     * The meta of the dropped item
     *
     * @param meta Meta of this block
     * @return Meta of the dropped item
     */
    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    /**
     * @return Whether this block is opaque or not
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Create a new tile entity for this block
     *
     * @param world World in which the block exists
     * @param meta  Meta of the block
     * @return New tile entity
     */
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySpaceElevatorCable();
    }
}
