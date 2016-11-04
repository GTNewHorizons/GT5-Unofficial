package gtPlusPlus.core.block.machine;

import java.util.ArrayList;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityCharger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.util.Random;

public class Machine_Charger extends BlockContainer {
	private static final String	name	= "Charging Machine";

	private final Random		rand	= new Random();

	public Machine_Charger(final String unlocalizedName) {
		super(Material.iron);
		// GameRegistry.registerBlock(this, unlocalizedName);
		this.setBlockName(unlocalizedName);
		this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block,
			final int par6) {
		if (world.isRemote) {
			return;
		}

		final ArrayList drops = new ArrayList();

		final TileEntity teRaw = world.getTileEntity(x, y, z);

		if (teRaw != null && teRaw instanceof TileEntityCharger) {
			final TileEntityCharger te = (TileEntityCharger) teRaw;

			for (int i = 0; i < te.getSizeInventory(); i++) {
				final ItemStack stack = te.getStackInSlot(i);

				if (stack != null) {
					drops.add(stack.copy());
				}
			}
		}

		for (int i = 0; i < drops.size(); i++) {
			final EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, (ItemStack) drops.get(i));
			item.setVelocity((this.rand.nextDouble() - 0.5) * 0.25, this.rand.nextDouble() * 0.5 * 0.25,
					(this.rand.nextDouble() - 0.5) * 0.25);
			world.spawnEntityInWorld(item);
		}
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int par2) {
		return new TileEntityCharger();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float lx, final float ly, final float lz) {
		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityCharger) {
			player.openGui(GTplusplus.instance, 1, world, x, y, z);
			return true;
		}
		return false;
	}
}