package miscutil.xmod.ic2.block.RTGGenerator;

import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.generator.tileentity.TileEntityKineticGenerator;
import ic2.core.block.generator.tileentity.TileEntityRTGenerator;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.init.InternalName;

import java.util.List;
import java.util.Random;

import miscutil.xmod.ic2.block.kieticgenerator.tileentity.TileEntityKineticWindGenerator;
import miscutil.xmod.ic2.item.IC2_Items;
import miscutil.xmod.ic2.item.ItemGenerators;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.apache.commons.lang3.mutable.MutableObject;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRTG
extends BlockMultiID
{
	public BlockRTG(InternalName internalName1)
	{
		super(internalName1, Material.iron, ItemGenerators.class);

		setHardness(3.0F);
		setStepSound(soundTypeMetal);

		IC2_Items.blockRTG = new ItemStack(this, 1, 0);
		IC2_Items.blockKineticGenerator = new ItemStack(this, 1, 1);

		GameRegistry.registerTileEntity(TileEntityRTG.class, "Radioisotope Thermoelectric Generator Mach II");
		GameRegistry.registerTileEntity(TileEntityKineticWindGenerator.class, "Kinetic Wind Generator Mach II");
	}

	@Override
	public void getSubBlocks(Item j, CreativeTabs tabs, List itemList) {
		Item item = Item.getItemFromBlock(this);
		if (!item.getHasSubtypes()) {
			itemList.add(new ItemStack(this));
		} else {
			for (int i = 0; i < 16; i++)
			{
				ItemStack is = new ItemStack(this, 1, i);
				if (is.getItem().getUnlocalizedName(is) == null) {
					break;
				}
				itemList.add(is);
			}
		}
	}


	@Override
	public String getTextureFolder(int id)
	{
		return "generator";
	}

	@Override
	public int damageDropped(int meta)
	{
		switch (meta)
		{
		case 2: 
			return 2;
		}
		return 0;
	}

	@Override
	public Class<? extends TileEntity> getTeClass(int meta, MutableObject<Class<?>[]> ctorArgTypes, MutableObject<Object[]> ctorArgs)
	{
		try
		{
			switch (meta)
			{      
			case 0: 
				return TileEntityRTGenerator.class;    
			case 1: 
				return TileEntityKineticGenerator.class;
			}
		}
		catch (Exception e)
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
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (!IC2.platform.isRendering()) {
			return;
		}
		int meta = world.getBlockMetadata(x, y, z);
		if ((meta == 0) && (isActive(world, x, y, z)))
		{
			TileEntityBlock te = (TileEntityBlock)getOwnTe(world, x, y, z);
			if (te == null) {
				return;
			}
			int l = te.getFacing();
			float f = x + 0.5F;
			float f1 = y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = z + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;
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
			TileEntityNuclearReactorElectric te = (TileEntityNuclearReactorElectric)getOwnTe(world, x, y, z);
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
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
	{
		if ((entityplayer.getCurrentEquippedItem() != null) && (entityplayer.getCurrentEquippedItem().isItemEqual(Ic2Items.reactorChamber))) {
			return false;
		}
		return super.onBlockActivated(world, i, j, k, entityplayer, side, a, b, c);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack)
	{
		return stack.getItemDamage() == 5 ? EnumRarity.uncommon : EnumRarity.common;
	}
}
