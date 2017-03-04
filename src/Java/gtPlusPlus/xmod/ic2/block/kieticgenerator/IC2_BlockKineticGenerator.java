package gtPlusPlus.xmod.ic2.block.kieticgenerator;

import org.apache.commons.lang3.mutable.MutableObject;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import ic2.core.block.BlockMultiID;
import ic2.core.block.kineticgenerator.tileentity.TileEntityManualKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemKineticGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class IC2_BlockKineticGenerator
extends BlockMultiID
{
	public IC2_BlockKineticGenerator(final InternalName internalName1)
	{
		super(internalName1, Material.iron, ItemKineticGenerator.class);

		this.setHardness(3.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setCreativeTab(AddToCreativeTab.tabMachines);

		GameRegistry.registerTileEntity(TileEntityWindKineticGenerator.class, "Advanced Kinetic Wind Generator");

	}

	@Override
	public String getTextureFolder(final int id)
	{
		return "kineticgenerator";
	}

	@Override
	public int damageDropped(final int meta)
	{
		return meta;
	}

	@Override
	public Class<? extends TileEntity> getTeClass(final int meta, final MutableObject<Class<?>[]> ctorArgTypes, final MutableObject<Object[]> ctorArgs)
	{
		try
		{
			switch (meta)
			{
			case 0:
				return TileEntityWindKineticGenerator.class;
			}
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer entityPlayer, final int side, final float a, final float b, final float c)
	{
		if (entityPlayer.isSneaking()) {
			return false;
		}
		final TileEntity te = this.getOwnTe(world, x, y, z);
		if ((te != null) && ((te instanceof TileEntityManualKineticGenerator))) {
			return ((TileEntityManualKineticGenerator)te).playerKlicked(entityPlayer);
		}
		return super.onBlockActivated(world, x, y, z, entityPlayer, side, a, b, c);
	}
}
