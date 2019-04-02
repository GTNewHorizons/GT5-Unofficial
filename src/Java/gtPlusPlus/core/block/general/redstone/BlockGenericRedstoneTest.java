package gtPlusPlus.core.block.general.redstone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.redstone.TileEntityRedstoneHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.world.explosions.ExplosionHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGenericRedstoneTest extends BlockGenericRedstone {

	public BlockGenericRedstoneTest() {
		super("test", "Redstone Test");
		setTickRandomly(true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityRedstoneTest();
	}
	
	public class TileEntityRedstoneTest extends TileEntityRedstoneHandler {
		public TileEntityRedstoneTest() {
			super(2);
		}

		@Override
		public boolean isScrewdriverable() {
			return true;
		}

		@Override
		public boolean onScrewdriverLMB() {
			// TODO Auto-generated method stub
			return super.onScrewdriverLMB();
		}

		@Override
		public boolean onScrewdriverRMB() {
			if (this.mLightValue + 0.0625f <= 1) {
				this.mLightValue += 0.0625f;
			}
			else {
				this.mLightValue = 0;
			}
			this.markForUpdate();
			return true;
		}

		@Override
		public boolean isMalletable() {
			return true;
		}

		@Override
		public boolean onMalletLMB() {
			// TODO Auto-generated method stub
			return super.onMalletLMB();
		}

		@Override
		public boolean onMalletRMB() {
			this.mLightMode = Utils.invertBoolean(mLightMode);
			this.markForUpdate();
			return mLightMode;
		}

		@Override
		public boolean isWrenchable() {
			return true;
		}

		@Override
		public boolean onWrenchLMB() {
			// TODO Auto-generated method stub
			return super.onWrenchLMB();
		}

		@Override
		public boolean onWrenchRMB() {
			Logger.INFO("Found Wrench");
			ExplosionHandler explode = new ExplosionHandler();
			explode.createExplosion(this.worldObj, null, this.xCoord, this.yCoord, this.zCoord, 1f, false, true);
			return true;
		}	

		@Override
		protected Class<? extends TileEntity> getTileEntityClass() {
			return this.getClass();
		}

		@Override
		protected String getTileEntityNameForRegistration() {
			// TODO Auto-generated method stub
			return "TileEntityRedstoneTest";
		}

		@Override
		public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
			// TODO Auto-generated method stub
			return super.isProvidingWeakPower(world, x, y, z, side);
		}

		@Override
		public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
			// TODO Auto-generated method stub
			return super.isProvidingStrongPower(world, x, y, z, side);
		}	
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List aList) {
		aList.add(ItemUtils.getSimpleStack(this));
	}


	@Override
	public void updateTick(World aWorld, int aX, int aY, int aZ, Random aRand) {		
		super.updateTick(aWorld, aX, aY, aZ, aRand);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		// TODO Auto-generated method stub
		return ItemUtils.getSimpleStack(this).getItem();
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		// TODO Auto-generated method stub
		return ItemUtils.getSimpleStack(this).getItem();
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return ItemUtils.simpleMetaStack(this, p_149644_1_, 1);
	}	
	
	public void generateTextureArray(final IIconRegister iicon) {
		HashMap<Integer, HashMap<ForgeDirection, IIcon>> aTextures = new HashMap<Integer, HashMap<ForgeDirection, IIcon>>();

		
		//New Block for Each Meta
		int aMeta = 0;
		{
			HashMap<ForgeDirection, IIcon> aTempMap = new HashMap<ForgeDirection, IIcon>();
			aTempMap.put(ForgeDirection.UP, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTempMap.put(ForgeDirection.DOWN, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTempMap.put(ForgeDirection.NORTH, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTempMap.put(ForgeDirection.SOUTH, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTempMap.put(ForgeDirection.EAST, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTempMap.put(ForgeDirection.WEST, iicon.registerIcon(CORE.MODID + ":" + "redstone/redstone_meter/" + "top"));
			aTextures.put(aMeta++, aTempMap);
		}
		
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		HashMap<ForgeDirection, IIcon> aTemp = getTextureArray().get(meta);
		if (aTemp != null) {
			IIcon aSide = aTemp.get(ForgeDirection.getOrientation(side));
			if (aSide != null) {
				return aSide;
			}
			else {
				//Smart calculate missing sides				
				if (side <= 1) {
					for (int ss = 0; ss < 2; ss++) {
						aSide = aTemp.get(ForgeDirection.getOrientation(side));
						if (aSide != null) {
							return aSide;
						}						
					}
				}					
				for (int ss = 2; ss < 6; ss++) {
					aSide = aTemp.get(ForgeDirection.getOrientation(side));
					if (aSide != null) {
						return aSide;
					}						
				}				
			}
		}
		return blockIcon;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> aDrops = new ArrayList<ItemStack>();
		aDrops.add(ItemUtils.getSimpleStack(this));
		return aDrops;
	}

}
