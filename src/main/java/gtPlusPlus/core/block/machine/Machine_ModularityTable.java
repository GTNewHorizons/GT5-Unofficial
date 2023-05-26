package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;

public class Machine_ModularityTable extends BlockContainer implements ITileTooltip {

    @SideOnly(Side.CLIENT)
    private IIcon textureTop;

    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;

    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    /**
     * Determines which tooltip is displayed within the itemblock.
     */
    private final int mTooltipID = 1;

    @Override
    public int getTooltipID() {
        return this.mTooltipID;
    }

    public Machine_ModularityTable() {
        super(Material.iron);
        this.setBlockName("blockModularity");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockModularity");
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
        this.blockIcon = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "ModularTable_side");
        this.textureTop = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "ModularTable_top");
        this.textureBottom = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "ModularTable_output");
        this.textureFront = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "ModularTable_side");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int side, final float lx, final float ly, final float lz) {
        if (world.isRemote) {
            return true;
        }
        final TileEntity te = world.getTileEntity(x, y, z);
        if ((te != null) && (te instanceof TileEntityModularityTable)) {
            player.openGui(GTplusplus.instance, 1, world, x, y, z);
            Logger.INFO("Player opened GUI");
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
        return new TileEntityModularityTable();
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
            final int z) {
        return false;
    }
}
