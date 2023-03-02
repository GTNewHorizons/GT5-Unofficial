package com.minecraft7771.gtnhintergalactic.block;

import com.minecraft7771.gtnhintergalactic.GTNHIntergalactic;
import com.minecraft7771.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSpaceElevatorCable extends Block implements ITileEntityProvider {

    public static IIcon textures[];
    public static IIcon motorGlow;
    private static int renderID;
    public static final int LIGHT_STEPS = 80;

    public BlockSpaceElevatorCable() {
        super(Material.iron);
        setBlockName("SpaceElevatorCable");
        setCreativeTab(GTNHIntergalactic.tab);
        setHarvestLevel("pickaxe", 2);
    }

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

    @Override
    public IIcon getIcon(int side, int meta) {
        return textures[0];
    }

    public static void setRenderID(int id) {
        renderID = id;
    }

    public static int getRenderID() {
        return renderID;
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public float getExplosionResistance(Entity entity) {
        return 6.0f;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 1.0f;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySpaceElevatorCable();
    }
}
