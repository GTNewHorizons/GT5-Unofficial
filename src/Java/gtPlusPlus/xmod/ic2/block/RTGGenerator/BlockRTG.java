package gtPlusPlus.xmod.ic2.block.RTGGenerator;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.mutable.MutableObject;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.xmod.ic2.block.kieticgenerator.tileentity.TileEntityKineticWindGenerator;
import gtPlusPlus.xmod.ic2.item.IC2_Items;
import gtPlusPlus.xmod.ic2.item.ItemGenerators;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.init.InternalName;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRTG
extends BlockMultiID
{
	public BlockRTG(final InternalName internalName1)
	{
		super(internalName1, Material.iron, ItemGenerators.class);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setHardness(3.0F);
		this.setStepSound(soundTypeMetal);

		IC2_Items.blockRTG = new ItemStack(this, 1, 0);
		IC2_Items.blockKineticGenerator = new ItemStack(this, 1, 1);

		GameRegistry.registerTileEntity(TileEntityRTG.class, "RTG Mach II");
		GameRegistry.registerTileEntity(TileEntityKineticWindGenerator.class, "Wind Ripper Mach II");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubBlocks(final Item j, final CreativeTabs tabs, final List itemList) {
		final Item item = Item.getItemFromBlock(this);
		if (!item.getHasSubtypes()) {
			itemList.add(new ItemStack(this));
		} else {
			for (int i = 0; i < 16; i++)
			{
				final ItemStack is = new ItemStack(this, 1, i);
				if (is.getItem().getUnlocalizedName(is) == null) {
					break;
				}
				itemList.add(is);
			}
		}
	}


	@Override
	public String getTextureFolder(final int id)
	{
		return "generator";
	}

	@Override
	public int damageDropped(final int meta)
	{
		switch (meta)
		{
		case 2:
			return 2;
		}
		return 0;
	}

	@Override
	public Class<? extends TileEntity> getTeClass(final int meta, final MutableObject<Class<?>[]> ctorArgTypes, final MutableObject<Object[]> ctorArgs)
	{
		try
		{
			switch (meta)
			{
			case 0:
				return TileEntityRTG.class;
			case 1:
				return TileEntityKineticWindGenerator.class;
			}
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		return null;
	}

	/*
	 *
	 *  {
      case 0:
        return TileEntityGenerator.class;
      case 1:
        return TileEntityGeoGenerator.class;
      case 2:
        return TileEntityWaterGenerator.class;
      case 3:
        return TileEntitySolarGenerator.class;
      case 4:
        return TileEntityWindGenerator.class;
      case 5:
        return TileEntityNuclearReactorElectric.class;
      case 6:
        return TileEntityRTGenerator.class;
      case 7:
        return TileEntitySemifluidGenerator.class;
      case 8:
        return TileEntityStirlingGenerator.class;
      case 9:
        return TileEntityKineticGenerator.class;
      }
	 *
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#randomDisplayTick(net.minecraft.world.World, int, int, int, java.util.Random)
	 */

	@Override
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random)
	{
		if (!IC2.platform.isRendering()) {
			return;
		}
		final int meta = world.getBlockMetadata(x, y, z);
		if ((meta == 0) && (this.isActive(world, x, y, z)))
		{
			final TileEntityBlock te = (TileEntityBlock)this.getOwnTe(world, x, y, z);
			if (te == null) {
				return;
			}
			final int l = te.getFacing();
			final float f = x + 0.5F;
			final float f1 = y + 0.0F + ((random.nextFloat() * 6.0F) / 16.0F);
			final float f2 = z + 0.5F;
			final float f3 = 0.52F;
			final float f4 = (random.nextFloat() * 0.6F) - 0.3F;
			switch (l)
			{
			case 4:
				world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				break;
			case 5:
				world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				break;
			case 2:
				world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
				break;
			case 3:
				world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
			}
		}
		else if (meta == 5)
		{
			final TileEntityNuclearReactorElectric te = (TileEntityNuclearReactorElectric)this.getOwnTe(world, x, y, z);
			if (te == null) {
				return;
			}
			int puffs = te.heat / 1000;
			if (puffs <= 0) {
				return;
			}
			puffs = world.rand.nextInt(puffs);
			for (int n = 0; n < puffs; n++) {
				world.spawnParticle("smoke", x + random.nextFloat(), y + 0.95F, z + random.nextFloat(), 0.0D, 0.0D, 0.0D);
			}
			puffs -= world.rand.nextInt(4) + 3;
			for (int n = 0; n < puffs; n++) {
				world.spawnParticle("flame", x + random.nextFloat(), y + 1.0F, z + random.nextFloat(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int i, final int j, final int k, final EntityPlayer entityplayer, final int side, final float a, final float b, final float c)
	{
		if ((entityplayer.getCurrentEquippedItem() != null) && (entityplayer.getCurrentEquippedItem().isItemEqual(Ic2Items.reactorChamber))) {
			return false;
		}
		return super.onBlockActivated(world, i, j, k, entityplayer, side, a, b, c);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack stack)
	{
		return stack.getItemDamage() == 5 ? EnumRarity.uncommon : EnumRarity.common;
	}
}
