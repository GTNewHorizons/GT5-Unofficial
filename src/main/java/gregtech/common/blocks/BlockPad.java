package gregtech.common.blocks;

import static java.lang.Math.abs;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IItemContainer;

public class BlockPad extends Block {

    protected String unlocalizedName;

    public BlockPad() {
        this(ItemBlockPad.class, "gt.blockpad", Material.cloth);

        register(0, ItemList.PadBouncy);
        register(1, ItemList.PadSticky);
    }

    protected BlockPad(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial) {
        super(aMaterial);
        setBlockName(unlocalizedName = aName);
        setHardness(1.0F);
        setResistance(1.0F);
        setStepSound(soundTypeCloth);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.isBlockContainer = false;
        GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.BLOCK_BOUNCE_PAD.getIcon();
            case 1 -> Textures.BlockIcons.BLOCK_STICKY_PAD.getIcon();
            default -> Textures.BlockIcons.BLOCK_BOUNCE_PAD.getIcon();
        };
    }

    protected final void register(int meta, @Nullable IItemContainer container) {
        ItemStack stack = new ItemStack(this, 1, meta);

        if (container != null) {
            container.set(stack.copy());
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!entity.boundingBox.intersectsWith(getCollisionBoundingBox(world, x, y, z))) return;
        int meta = world.getBlockMetadata(x, y, z);
        switch (meta) {
            case 0 -> {
                // bounce pad
                // too weakly supported, it breaks
                if (supportedSides(world, x, y, z) < 2) {
                    this.dropBlockAsItem(world, x, y, z, meta, 0);
                    world.setBlockToAir(x, y, z);
                    break;
                }

                float fallDist = entity.fallDistance;
                float motionMult = 0.35F;

                if (fallDist > 0F && fallDist <= 11F) {
                    if (entity instanceof EntityPlayerSP player) {
                        if (player.movementInput.jump) {
                            motionMult = 1.25F;
                        }
                    }
                    // bounce back 1/3 as high, but if player is holding jump, jump a little higher than fall
                    entity.addVelocity(0, abs(entity.motionY) * motionMult, 0);
                    entity.fallDistance = 0.0F;
                } else if (fallDist > 11F) {
                    this.dropBlockAsItem(world, x, y, z, meta, 0);
                    world.setBlockToAir(x, y, z);
                    // break fall a bit.
                    entity.motionY *= 0.66F;
                    entity.fallDistance *= 0.66F;
                }
            }
            case 1 -> {
                // sticky pad
                entity.motionZ *= 0.25F;
                entity.motionX *= 0.25F;
                entity.motionY *= .5F;
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(
            (double) x + this.minX,
            (double) y + this.minY,
            (double) z + this.minZ,
            (double) x + this.maxX,
            (double) y + this.maxY,
            (double) z + this.maxZ);
    }

    @Override
    public boolean canBlockStay(World worldIn, int x, int y, int z) {
        // check all four sides for supports
        for (int i = -1; i <= 1 && worldIn.getBlockMetadata(x, y, z) != 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (!worldIn.isAirBlock(x + i, y, z + j)) {
                    return true;
                }
            }
        }
        // If on bottom
        return !worldIn.isAirBlock(x, y - 1, z);
    }

    public int supportedSides(World worldIn, int x, int y, int z) {
        int supports = 0;
        // check all four sides for supports
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (!worldIn.isAirBlock(x + i, y, z + j)) {
                    supports++;
                }
            }
        }
        // 99 if supported on bottom, 0 to 4 otherwise
        return !worldIn.isAirBlock(x, y - 1, z) ? 99 : supports;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    };

    @Override
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        return super.canPlaceBlockAt(worldIn, x, y, z) && this.canBlockStay(worldIn, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        this.updateBlock(worldIn, x, y, z);
    }

    private void updateBlock(World worldIn, int x, int y, int z) {
        if (!this.canBlockStay(worldIn, x, y, z)) {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }
}
