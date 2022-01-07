package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;
import gregtech.common.blocks.GT_Material_Machines;
import gregtech.common.render.GT_Renderer_Block;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.render.GTPP_Render_MachineBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GTPP_Block_Machines extends GT_Generic_Block implements IDebugableBlock, ITileEntityProvider {
	public static ThreadLocal<IGregTechTileEntity> mTemporaryTileEntity = new ThreadLocal<IGregTechTileEntity>();

	public GTPP_Block_Machines() {
		super(GTPP_Item_Machines.class, "gtpp.blockmachines", new GT_Material_Machines());
		GregTech_API.registerMachineBlock(this, -1);
		this.setHardness(1.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(GregTech_API.TAB_GREGTECH);
		this.isBlockContainer = true;
	}

	public String getHarvestTool(int aMeta) {
		switch (aMeta / 4) {
			case 0 :
				return "wrench";
			case 1 :
				return "wrench";
			case 2 :
				return "cutter";
			case 3 :
				return "axe";
			default :
				return "wrench";
		}
	}

	public int getHarvestLevel(int aMeta) {
		return aMeta % 4;
	}

	protected boolean canSilkHarvest() {
		return false;
	}

	public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof BaseTileEntity) {
			((BaseTileEntity) tTileEntity).onAdjacentBlockChange(aTileX, aTileY, aTileZ);
		}

	}

	public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
		super.onBlockAdded(aWorld, aX, aY, aZ);
		if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
			GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
		}

	}

	public String getUnlocalizedName() {
		int tDamage = 0;
		String aUnlocalName = (tDamage >= 0 && tDamage < GregTech_API.METATILEENTITIES.length)
				? (GregTech_API.METATILEENTITIES[tDamage] != null
				? ("gtpp.blockmachines.name" + "." + GregTech_API.METATILEENTITIES[tDamage].getMetaName())
				: ("gtpp.blockmachines.name")) : "";
		
		Logger.INFO("Unlocal Name: "+aUnlocalName);
		return aUnlocalName;
	}

	public String getLocalizedName() {
		String aName = StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");;
		if (aName.toLowerCase().contains(".name")) {
			aName = StatCollector.translateToLocal(getUnlocalizedName() + ".name");
		}
		if (aName.toLowerCase().contains(".name")) {
			aName = StatCollector.translateToLocal("gt.blockmachines" + ".name");
		}
		if (aName.toLowerCase().contains(".name")) {
			aName = "BAD";
		}
		Logger.INFO("Name: "+aName);
		return aName;
	}

	public int getFlammability(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
		return 0;
	}

	public int getFireSpreadSpeed(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
		return GregTech_API.sMachineFlammable && aWorld.getBlockMetadata(aX, aY, aZ) == 0 ? 100 : 0;
	}

	public int getRenderType() {
		return GTPP_Render_MachineBlock.INSTANCE == null ? super.getRenderType() : GTPP_Render_MachineBlock.INSTANCE.mRenderID;
	}

	public boolean isFireSource(World aWorld, int aX, int aY, int aZ, ForgeDirection side) {
		return GregTech_API.sMachineFlammable && aWorld.getBlockMetadata(aX, aY, aZ) == 0;
	}

	public boolean isFlammable(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection face) {
		return GregTech_API.sMachineFlammable && aWorld.getBlockMetadata(aX, aY, aZ) == 0;
	}

	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess aWorld, int aX, int aY, int aZ) {
		return false;
	}

	public boolean canConnectRedstone(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return true;
	}

	public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
		return false;
	}

	public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
		return false;
	}

	public boolean hasTileEntity(int aMeta) {
		return true;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canProvidePower() {
		return true;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public TileEntity createNewTileEntity(World aWorld, int aMeta) {
		return this.createTileEntity(aWorld, aMeta);
	}

	public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int aSide) {
		return BlockIcons.MACHINE_LV_SIDE.getIcon();
	}

	public IIcon getIcon(int aSide, int aMeta) {
		return BlockIcons.MACHINE_LV_SIDE.getIcon();
	}

	public boolean onBlockEventReceived(World aWorld, int aX, int aY, int aZ, int aData1, int aData2) {
		super.onBlockEventReceived(aWorld, aX, aY, aZ, aData1, aData2);
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity != null ? tTileEntity.receiveClientEvent(aData1, aData2) : false;
	}

	public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB, List outputAABB,
			Entity collider) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity
				&& ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
			((IGregTechTileEntity) tTileEntity).addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB,
					collider);
		} else {
			super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity
				&& ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null
						? ((IGregTechTileEntity) tTileEntity).getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ)
						: super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity
				&& ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null
						? ((IGregTechTileEntity) tTileEntity).getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ)
						: super.getSelectedBoundingBoxFromPool(aWorld, aX, aY, aZ);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int aX, int aY, int aZ) {
		TileEntity tTileEntity = blockAccess.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity
				&& ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
			AxisAlignedBB bbb = ((IGregTechTileEntity) tTileEntity)
					.getCollisionBoundingBoxFromPool(((IGregTechTileEntity) tTileEntity).getWorld(), 0, 0, 0);
			this.minX = bbb.minX;
			this.minY = bbb.minY;
			this.minZ = bbb.minZ;
			this.maxX = bbb.maxX;
			this.maxY = bbb.maxY;
			this.maxZ = bbb.maxZ;
		} else {
			super.setBlockBoundsBasedOnState(blockAccess, aX, aY, aZ);
		}
	}

	public void setBlockBoundsForItemRender() {
		super.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity
				&& ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() != null) {
			((IGregTechTileEntity) tTileEntity).onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
		} else {
			super.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister aIconRegister) {

	}

	public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
		return super.getBlockHardness(aWorld, aX, aY, aZ);
	}

	public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof BaseMetaTileEntity && ((BaseMetaTileEntity) tTileEntity).privateAccess()
				&& !((BaseMetaTileEntity) tTileEntity).playerOwnsThis(aPlayer, true)
						? -1.0F
						: super.getPlayerRelativeBlockHardness(aPlayer, aWorld, aX, aY, aZ);
	}

	public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int aSide, float par1,
			float par2, float par3) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity == null) {
			return false;
		} else {
			if (aPlayer.isSneaking()) {
				ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
				if (tCurrentItem == null) {
					return false;
				}

				if (!GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)
						&& !GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {
					return false;
				}
			}

			return tTileEntity instanceof IGregTechTileEntity
					? (((IGregTechTileEntity) tTileEntity).getTimer() < 50L
							? false
							: (!aWorld.isRemote && !((IGregTechTileEntity) tTileEntity).isUseableByPlayer(aPlayer)
									? true
									: ((IGregTechTileEntity) tTileEntity).onRightclick(aPlayer, (byte) aSide, par1,
											par2, par3)))
					: false;
		}
	}

	public void onBlockClicked(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			((IGregTechTileEntity) tTileEntity).onLeftclick(aPlayer);
		}

	}

	public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity ? ((IGregTechTileEntity) tTileEntity).getMetaTileID() : 0;
	}

	public void onBlockExploded(World aWorld, int aX, int aY, int aZ, Explosion aExplosion) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof BaseMetaTileEntity) {
			((BaseMetaTileEntity) tTileEntity).doEnergyExplosion();
		}

		super.onBlockExploded(aWorld, aX, aY, aZ, aExplosion);
	}

	public void breakBlock(World aWorld, int aX, int aY, int aZ, Block par5, int par6) {
		GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			IGregTechTileEntity tGregTechTileEntity = (IGregTechTileEntity) tTileEntity;
			XSTR tRandom = new XSTR();
			mTemporaryTileEntity.set(tGregTechTileEntity);

			for (int i = 0; i < tGregTechTileEntity.getSizeInventory(); ++i) {
				ItemStack tItem = tGregTechTileEntity.getStackInSlot(i);
				if (tItem != null && tItem.stackSize > 0 && tGregTechTileEntity.isValidSlot(i)) {
					EntityItem tItemEntity = new EntityItem(aWorld,
							(double) ((float) aX + tRandom.nextFloat() * 0.8F + 0.1F),
							(double) ((float) aY + tRandom.nextFloat() * 0.8F + 0.1F),
							(double) ((float) aZ + tRandom.nextFloat() * 0.8F + 0.1F),
							new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
					if (tItem.hasTagCompound()) {
						tItemEntity.getEntityItem().setTagCompound((NBTTagCompound) tItem.getTagCompound().copy());
					}

					tItemEntity.motionX = tRandom.nextGaussian() * 0.0500000007450581D;
					tItemEntity.motionY = tRandom.nextGaussian() * 0.0500000007450581D + 0.2000000029802322D;
					tItemEntity.motionZ = tRandom.nextGaussian() * 0.0500000007450581D;
					aWorld.spawnEntityInWorld(tItemEntity);
					tItem.stackSize = 0;
					tGregTechTileEntity.setInventorySlotContents(i, (ItemStack) null);
				}
			}
		}

		super.breakBlock(aWorld, aX, aY, aZ, par5, par6);
		aWorld.removeTileEntity(aX, aY, aZ);
	}

	public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity
				? ((IGregTechTileEntity) tTileEntity).getDrops()
				: (mTemporaryTileEntity.get() == null
						? new ArrayList()
						: ((IGregTechTileEntity) mTemporaryTileEntity.get()).getDrops());
	}

	public int getComparatorInputOverride(World aWorld, int aX, int aY, int aZ, int aSide) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity
				? ((IGregTechTileEntity) tTileEntity).getComparatorValue((byte) aSide)
				: 0;
	}

	public int isProvidingWeakPower(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
		if (aSide >= 0 && aSide <= 5) {
			TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
			return tTileEntity instanceof IGregTechTileEntity
					? ((IGregTechTileEntity) tTileEntity).getOutputRedstoneSignal(GT_Utility.getOppositeSide(aSide))
					: 0;
		} else {
			return 0;
		}
	}

	public int isProvidingStrongPower(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
		if (aSide >= 0 && aSide <= 5) {
			TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
			return tTileEntity instanceof IGregTechTileEntity
					? ((IGregTechTileEntity) tTileEntity)
							.getStrongOutputRedstoneSignal(GT_Utility.getOppositeSide(aSide))
					: 0;
		} else {
			return 0;
		}
	}

	public void dropBlockAsItemWithChance(World aWorld, int aX, int aY, int aZ, int par5, float chance, int par7) {
		if (!aWorld.isRemote) {
			TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
			if (tTileEntity != null && chance < 1.0F) {
				if (tTileEntity instanceof BaseMetaTileEntity && GregTech_API.sMachineNonWrenchExplosions) {
					((BaseMetaTileEntity) tTileEntity).doEnergyExplosion();
				}
			} else {
				super.dropBlockAsItemWithChance(aWorld, aX, aY, aZ, par5, chance, par7);
			}
		}

	}

	public boolean isSideSolid(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection aSide) {
		if (aWorld.getBlockMetadata(aX, aY, aZ) == 0) {
			return true;
		} else {
			TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
			if (tTileEntity != null) {
				if (tTileEntity instanceof BaseMetaTileEntity) {
					return true;
				}

				if (tTileEntity instanceof BaseMetaPipeEntity
						&& (((BaseMetaPipeEntity) tTileEntity).mConnections & -64) != 0) {
					return true;
				}

				if (tTileEntity instanceof ICoverable
						&& ((ICoverable) tTileEntity).getCoverIDAtSide((byte) aSide.ordinal()) != 0) {
					return true;
				}
			}

			return false;
		}
	}

	public int getLightOpacity(IBlockAccess aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity == null
				? 0
				: (tTileEntity instanceof IGregTechTileEntity
						? ((IGregTechTileEntity) tTileEntity).getLightOpacity()
						: (aWorld.getBlockMetadata(aX, aY, aZ) == 0 ? 255 : 0));
	}

	public int getLightValue(IBlockAccess aWorld, int aX, int aY, int aZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof BaseMetaTileEntity ? ((BaseMetaTileEntity) tTileEntity).getLightValue() : 0;
	}

	public TileEntity createTileEntity(World aWorld, int aMeta) {
		return (TileEntity) (aMeta >= 4 ? Meta_GT_Proxy.constructBaseMetaTileEntity() : Meta_GT_Proxy.constructBaseMetaTileEntityCustomPower());
	}

	public float getExplosionResistance(Entity par1Entity, World aWorld, int aX, int aY, int aZ, double explosionX,
			double explosionY, double explosionZ) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity
				? ((IGregTechTileEntity) tTileEntity).getBlastResistance((byte) 6)
				: 10.0F;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i = 0; i < 100; ++i) {
			if (GregTech_API.METATILEENTITIES[(30400 + i)] != null) {
				par3List.add(new ItemStack(par1, 1, i));
			}
		}

	}

	public void onBlockPlacedBy(World aWorld, int aX, int aY, int aZ, EntityLivingBase aPlayer, ItemStack aStack) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity != null) {
			if (tTileEntity instanceof IGregTechTileEntity) {
				IGregTechTileEntity var6 = (IGregTechTileEntity) tTileEntity;
				if (aPlayer == null) {
					var6.setFrontFacing((byte) 1);
				} else {
					int var7 = MathHelper.floor_double((double) (aPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
					int var8 = Math.round(aPlayer.rotationPitch);
					if (var8 >= 65 && var6.isValidFacing((byte) 1)) {
						var6.setFrontFacing((byte) 1);
					} else if (var8 <= -65 && var6.isValidFacing((byte) 0)) {
						var6.setFrontFacing((byte) 0);
					} else {
						switch (var7) {
							case 0 :
								var6.setFrontFacing((byte) 2);
								break;
							case 1 :
								var6.setFrontFacing((byte) 5);
								break;
							case 2 :
								var6.setFrontFacing((byte) 3);
								break;
							case 3 :
								var6.setFrontFacing((byte) 4);
						}
					}
				}
			}

		}
	}

	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
		TileEntity tTileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof BaseMetaTileEntity
				? ((BaseMetaTileEntity) tTileEntity).getDebugInfo(aPlayer, aLogLevel)
				: (tTileEntity instanceof BaseMetaPipeEntity
						? ((BaseMetaPipeEntity) tTileEntity).getDebugInfo(aPlayer, aLogLevel)
						: null);
	}

	public boolean recolourBlock(World aWorld, int aX, int aY, int aZ, ForgeDirection aSide, int aColor) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			if (((IGregTechTileEntity) tTileEntity).getColorization() == (byte) (~aColor & 15)) {
				return false;
			} else {
				((IGregTechTileEntity) tTileEntity).setColorization((byte) (~aColor & 15));
				return true;
			}
		} else {
			return false;
		}
	}
}