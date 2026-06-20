package gregtech.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
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
    protected int subTypes = 0;

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
            case 0 -> ordinalSide >= 2 ? Textures.BlockIcons.BLOCK_BOUNCE_PAD_SIDE.getIcon()
                : Textures.BlockIcons.BLOCK_BOUNCE_PAD_TOP.getIcon();
            case 1 -> ordinalSide == 0 ? Textures.BlockIcons.BLOCK_STICKY_PAD_BOTTOM.getIcon()
                : ordinalSide == 1 ? Textures.BlockIcons.BLOCK_STICKY_PAD_TOP.getIcon()
                    : Textures.BlockIcons.BLOCK_STICKY_PAD_SIDE.getIcon();
            default -> Textures.BlockIcons.BLOCK_BOUNCE_PAD_TOP.getIcon();
        };
    }

    @Override
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < this.subTypes; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    protected final void register(int meta, @Nullable IItemContainer container) {
        this.subTypes++;
        ItemStack stack = new ItemStack(this, 1, meta);

        if (container != null) {
            container.set(stack.copy());
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        int meta = world.getBlockMetadata(x, y, z);
        // first of all, make sure it's actually colliding for the sticky pad. Stops it working when you're on a slab,
        // for example.
        if (meta == 1 && !entity.boundingBox.intersectsWith(getCollisionBoundingBox(world, x, y, z))) return;
        switch (meta) {
            case 0 -> {
                // bounce pad
                // too weakly supported, it breaks
                int supports = supportedSides(world, x, y, z);
                if (supports < 2) {
                    this.dropBlockAsItem(world, x, y, z, meta, 0);
                    world.setBlockToAir(x, y, z);
                    break;
                } else if (supports == 99) break; // No give, do nothing.

                float fallDist = entity.fallDistance;
                float motionMult = -0.9F;

                if (fallDist <= 15F) {
                    if (entity instanceof EntityPlayerSP) {
                        if (Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed()) {
                            motionMult = -1.35F;
                        } else if (Minecraft.getMinecraft().gameSettings.keyBindSneak.getIsKeyPressed()) {
                            motionMult = -0.3F;
                        }
                    }
                    // bounce back lower, but if player is holding jump, jump a little higher than fall. Sneak to mostly
                    // break fall.
                    if (entity.motionY < -0.32F) entity.motionY *= motionMult;
                    entity.fallDistance = 0.0F;
                } else if (fallDist > 15F) {
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
                if (entity.fallDistance < 1) entity.motionY *= .5F;
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
            (double) y + this.maxY * 2, // need some space for entity to collide with.
            (double) z + this.maxZ);
    }

    @Override
    public boolean canBlockStay(World worldIn, int x, int y, int z) {
        int meta = worldIn.getBlockMetadata(x, y, z);
        // check all four sides for supports for trampoline. Needs space for give.
        for (int i = -1; i <= 1 && meta != 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (!worldIn.isAirBlock(x + i, y, z + j)) {
                    return true;
                }
            }
        }
        // Sticky pad only if on bottom
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

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public static class ItemBlockPad extends ItemBlock {

        public ItemBlockPad(Block block) {
            super(block);
            setMaxDamage(0);
            setHasSubtypes(true);
        }

        @Override
        public String getUnlocalizedName(ItemStack aStack) {
            return this.field_150939_a.getUnlocalizedName() + "." + damageDropped(getDamage(aStack));
        }

        public int damageDropped(int metadata) {
            return metadata;
        }

        @Override
        public void getSubItems(Item aStack, CreativeTabs tabs, List<ItemStack> stackList) {
            this.field_150939_a.getSubBlocks(aStack, tabs, stackList);
        }

        @Override
        public String getItemStackDisplayName(ItemStack stack) {
            return super.getItemStackDisplayName(stack);
        }

        @Override
        public int getMetadata(int aMeta) {
            return aMeta;
        }
    }

}
