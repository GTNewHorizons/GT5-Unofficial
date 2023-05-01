package gtPlusPlus.core.block.machine;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.CubicObject;
import gtPlusPlus.core.block.base.BasicTileBlockWithTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockRoundRobinator;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class Machine_RoundRobinator extends BasicTileBlockWithTooltip {

    public Machine_RoundRobinator() {
        super(Material.iron);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
            final int ordinalSide, final float lx, final float ly, final float lz) {
        if (world.isRemote) {
            return true;
        } else {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            boolean mDidScrewDriver = false;
            // Check For Screwdriver
            try {
                final ItemStack mHandStack = PlayerUtils.getItemStackInPlayersHand(world, player.getDisplayName());
                if (ItemUtils.isToolScrewdriver(mHandStack)) {
                    final TileEntityRoundRobinator tile = (TileEntityRoundRobinator) world.getTileEntity(x, y, z);
                    if (tile != null) {
                        mDidScrewDriver = tile.onScrewdriverRightClick(side, player, x, y, z);
                    }
                }
            } catch (final Throwable t) {}

            if (!mDidScrewDriver) {
                final TileEntity te = world.getTileEntity(x, y, z);
                if ((te != null) && (te instanceof TileEntityRoundRobinator)) {
                    return ((TileEntityRoundRobinator) te).onRightClick(side, player, x, y, z);
                }
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
        return new TileEntityRoundRobinator();
    }

    @Override
    public int getMetaCount() {
        return 5;
    }

    @Override
    public String getUnlocalBlockName() {
        return "blockRoundRobinator";
    }

    @Override
    protected float initBlockHardness() {
        return 1;
    }

    @Override
    protected float initBlockResistance() {
        return 1;
    }

    @Override
    protected CreativeTabs initCreativeTab() {
        return AddToCreativeTab.tabMachines;
    }

    @Override
    public int getTooltipID() {
        return -1;
    }

    @Override
    protected String getTileEntityName() {
        return "Round Robinator";
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockRoundRobinator.class;
    }

    @Override
    public CubicObject<String>[] getCustomTextureDirectoryObject() {
        AutoMap<String[]> aTemp = new AutoMap<String[]>();
        for (int i = 0; i < 5; i++) {
            String[] aTexData = new String[] { GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Top_" + i,
                    GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Top_" + i,
                    GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Side_" + i,
                    GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Side_" + i,
                    GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Side_" + i,
                    GTPlusPlus.ID + ":" + "TileEntities/RoundRobinator/Side_" + i, };
            aTemp.put(aTexData);
        }
        AutoMap<CubicObject<String>> aTemp2 = new AutoMap<CubicObject<String>>();
        for (String[] y : aTemp) {
            aTemp2.put(new CubicObject<String>(y));
        }
        CubicObject<String>[] aTextureData = new CubicObject[] { aTemp2.get(0), aTemp2.get(1), aTemp2.get(2),
                aTemp2.get(3), aTemp2.get(4) };
        return aTextureData;
    }
}
