package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cleanroommc.modularui.factory.TileEntityGuiFactory;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.client.renderer.RenderDecayChest;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.util.minecraft.InventoryUtils;

public class DecayablesChest extends BlockContainer implements ITileTooltip {

    @SideOnly(Side.CLIENT)
    private IIcon textureTop;

    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;

    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    /**
     * Determines which tooltip is displayed within the itemblock.
     */
    private final int mTooltipID = 5;

    @Override
    public int getTooltipID() {
        return this.mTooltipID;
    }

    public DecayablesChest() {
        super(Material.iron);
        this.setBlockName("blockDecayablesChest");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        this.setHardness(5f);
        this.setResistance(1f);
        GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockDecayablesChest");
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        try {
            if (RenderDecayChest.INSTANCE != null) {
                return RenderDecayChest.INSTANCE.mRenderID;
            }
            return super.getRenderType();
        } catch (NullPointerException n) {
            return 0;
        }
    }

    /**
     * Updates the blocks bounds based on its current state.
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z - 1) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        } else if (world.getBlock(x, y, z + 1) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        } else if (world.getBlock(x - 1, y, z) == this) {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        } else if (world.getBlock(x + 1, y, z) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        } else {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int ordinalSide, final int meta) {
        return switch (ordinalSide) {
            case 0 -> textureBottom;
            case 1 -> textureTop;
            default -> textureFront;
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "DecayablesChest_top");
        this.textureTop = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "DecayablesChest_top");
        this.textureBottom = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "DecayablesChest_side");
        this.textureFront = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "DecayablesChest_bottom");
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
        final int side, final float lx, final float ly, final float lz) {
        if (world.isRemote || player == null || player.worldObj != world) {
            return true;
        }

        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityDecayablesChest) {
            TileEntityGuiFactory.open(player, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
        return new TileEntityDecayablesChest();
    }

    @Override
    public void breakBlock(final World world, final int x, final int y, final int z, final Block block,
        final int number) {
        InventoryUtils.dropInventoryItems(world, x, y, z, block);
        super.breakBlock(world, x, y, z, block, number);
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity,
        final ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((TileEntityDecayablesChest) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
        final int z) {
        return false;
    }
}
