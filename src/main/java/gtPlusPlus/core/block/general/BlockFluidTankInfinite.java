package gtPlusPlus.core.block.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;

public class BlockFluidTankInfinite extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon textureTop;

    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;

    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    public BlockFluidTankInfinite() {
        super(Material.iron);
        this.setBlockName("blockInfiniteFluidTank");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, "blockInfiniteFluidTank");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int ordinalSide, final int meta) {
        return ordinalSide == 1 ? this.textureTop
            : (ordinalSide == 0 ? this.textureBottom
                : ((ordinalSide != 2) && (ordinalSide != 4) ? this.blockIcon : this.textureFront));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/Generic_Creative_Texture");
        this.textureTop = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/Generic_Creative_Texture");
        this.textureBottom = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/Generic_Creative_Texture");
        this.textureFront = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/Generic_Creative_Texture");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
        final int side, final float lx, final float ly, final float lz) {
        if (world.isRemote) {
            return true;
        } else {

            TileEntityInfiniteFluid tank = (TileEntityInfiniteFluid) world.getTileEntity(x, y, z);
            if (tank != null) {
                if (player.isSneaking()) {
                    switch (tank.changeMode()) {
                        case TileEntityInfiniteFluid.SINGLE_FLUID -> GTUtility
                            .sendChatToPlayer(player, "This tank is now in single fluid mode.");
                        case TileEntityInfiniteFluid.SUPPLY_ALL_FLUIDS -> GTUtility
                            .sendChatToPlayer(player, "This tank is now in supply all fluids mode.");
                    }
                    return true;
                }

                // get the held item
                ItemStack handStack = player.getHeldItem();
                Item handItem = handStack == null ? null : handStack.getItem();
                if (handItem == null) handStack = null;

                // only do things if we hold a container
                if (handStack != null) {
                    if (FluidContainerRegistry.isEmptyContainer(player.getHeldItem())) {
                        tank.setFluid(null);
                    } else if (FluidContainerRegistry.isFilledContainer(player.getHeldItem())) {
                        FluidStack newFluid = FluidContainerRegistry.getFluidForFilledItem(player.getHeldItem());
                        tank.setFluid(newFluid);
                    } else if (handItem instanceof IFluidContainerItem container) {
                        tank.setFluid(container.getFluid(player.getHeldItem()));
                    }
                }

                // report new stats after update
                if (tank.getFluid() != null) {
                    String fluidName = tank.getFluid()
                        .getLocalizedName();
                    switch (tank.mode) {
                        case TileEntityInfiniteFluid.SINGLE_FLUID -> GTUtility
                            .sendChatToPlayer(player, "This tank contains " + fluidName + ".");
                        case TileEntityInfiniteFluid.SUPPLY_ALL_FLUIDS -> GTUtility.sendChatToPlayer(
                            player,
                            "This tank contains " + fluidName + " and can supply any fluid that is requested from it.");
                    }
                } else {
                    switch (tank.mode) {
                        case TileEntityInfiniteFluid.SINGLE_FLUID -> GTUtility
                            .sendChatToPlayer(player, "This tank is empty.");
                        case TileEntityInfiniteFluid.SUPPLY_ALL_FLUIDS -> GTUtility
                            .sendChatToPlayer(player, "This tank can supply any fluid that is requested from it.");
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
        return new TileEntityInfiniteFluid();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }
}
