package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.tileentities.machines.TileEntityAdvPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityBaseFluidCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityPooCollector;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockPooCollector extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon textureTop;

    @SideOnly(Side.CLIENT)
    private IIcon textureTop2;

    @SideOnly(Side.CLIENT)
    private IIcon textureSide;

    @SideOnly(Side.CLIENT)
    private IIcon textureSide2;

    public BlockPooCollector() {
        super(Material.iron);
        this.setHardness(5f);
        this.setResistance(1f);
        this.setBlockName("blockPooCollector");
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        GameRegistry.registerBlock(this, ItemBlockMeta.class, "blockPooCollector");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        if (aMeta <= 7) {
            blockIcon = textureSide;
            return ordinalSide <= 1 ? this.textureTop : this.textureSide;
        } else {
            blockIcon = textureSide2;
            return ordinalSide <= 1 ? this.textureTop2 : this.textureSide2;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        this.textureTop = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "sewer_top");
        this.textureTop2 = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "sewer_adv_top");
        this.textureSide = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "sewer_sides");
        this.textureSide2 = p_149651_1_.registerIcon(GTPlusPlus.ID + ":" + "TileEntities/" + "sewer_adv_sides");
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
            TileEntityBaseFluidCollector tank = (TileEntityBaseFluidCollector) world.getTileEntity(x, y, z);
            if (tank != null) {
                if (!tank.mInventory.isEmpty()) {
                    GTUtility.sendChatToPlayer(player, "Inventory contains:");
                    GTUtility
                        .sendChatToPlayer(player, ItemUtils.getArrayStackNames(tank.mInventory.getRealInventory()));
                } else {
                    GTUtility.sendChatToPlayer(player, "No solids collected yet.");
                }
                if (tank.tank.getFluid() != null) {
                    GTUtility.sendChatToPlayer(
                        player,
                        "Tank contains " + tank.tank.getFluidAmount()
                            + "L of "
                            + tank.tank.getFluid()
                                .getLocalizedName());
                }
            }
        }
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return super.isOpaqueCube();
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int aMeta) {
        return aMeta <= 7 ? new TileEntityPooCollector() : new TileEntityAdvPooCollector();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public int getBlockColor() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int damageDropped(final int damage) {
        return damage;
    }

    @Override
    public int getRenderColor(int aMeta) {
        return super.getRenderColor(aMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 8));
    }
}
