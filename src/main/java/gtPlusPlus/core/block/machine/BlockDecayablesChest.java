package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.GuiFactories;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.client.renderer.RenderDecayChest;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.util.minecraft.InventoryUtils;

public class BlockDecayablesChest extends BlockContainer implements ITileTooltip {

    private static final ForgeDirection[] validRotationAxes = new ForgeDirection[] { UP, DOWN };

    /**
     * Determines which tooltip is displayed within the itemblock.
     */
    private final int mTooltipID = 3;

    @Override
    public int getTooltipID() {
        return this.mTooltipID;
    }

    public BlockDecayablesChest() {
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

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister reg) {
        this.blockIcon = reg.registerIcon(GTPlusPlus.ID + ":TileEntities/DecayablesChest_top");
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
        final int side, final float lx, final float ly, final float lz) {
        if (world.isRemote || player == null || player.worldObj != world) {
            return true;
        }

        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityDecayablesChest) {
            GuiFactories.tileEntity()
                .open(player, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int meta) {
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
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0) {
            chestFacing = 2;
        }
        if (facing == 1) {
            chestFacing = 5;
        }
        if (facing == 2) {
            chestFacing = 3;
        }
        if (facing == 3) {
            chestFacing = 4;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityDecayablesChest tileEntityChest) {
            tileEntityChest.setFacing(chestFacing);
            world.markBlockForUpdate(x, y, z);
        }
        if (stack.hasDisplayName()) {
            ((TileEntityDecayablesChest) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
        final int z) {
        return false;
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        if (worldObj.isRemote) {
            return false;
        }
        if (axis == UP || axis == DOWN) {
            TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityDecayablesChest tileEntityChest) {
                tileEntityChest.rotateAround(axis);
            }
            return true;
        }
        return false;
    }
}
