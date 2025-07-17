package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTile;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import ic2.core.item.tool.ItemToolWrench;

@Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = Mods.ModIDs.ENDER_I_O)
public class BlockProjectTable extends BlockContainer implements ITileTooltip {

    @SideOnly(Side.CLIENT)
    private IIcon textureTop;

    @SideOnly(Side.CLIENT)
    private IIcon textureBottom;

    @SideOnly(Side.CLIENT)
    private IIcon textureFront;

    /**
     * Determines which tooltip is displayed within the itemblock.
     */
    @Override
    public int getTooltipID() {
        return 0;
    }

    public BlockProjectTable() {
        super(Material.iron);
        this.setBlockName("blockProjectBench");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, ItemBlockBasicTile.class, "blockProjectBench");
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
        this.blockIcon = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/machine_top");
        this.textureTop = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/cover_crafting");
        this.textureBottom = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/machine_top");
        this.textureFront = p_149651_1_.registerIcon(GTPlusPlus.ID + ":TileEntities/machine_top");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly,
        float lz) {
        if (world.isRemote) return true;
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityProjectTable) {
            player.openGui(GTplusplus.instance, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
        return new TileEntityProjectTable();
    }

    public static boolean isWrench(final ItemStack item) {
        if (item.getItem() instanceof ItemToolWrench) {
            return true;
        }
        if (Mods.BuildCraftCore.isModLoaded()) {
            return item.getItem() instanceof buildcraft.api.tools.IToolWrench;
        }
        if (Mods.EnderIO.isModLoaded()) {
            return item.getItem() instanceof crazypants.enderio.api.tool.ITool;
        }
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }
}
