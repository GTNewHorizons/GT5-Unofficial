package gtPlusPlus.core.block.general;

import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.itemblock.ItemBlockDoor;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityPlayerDoorBase;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PlayerDoors extends BlockDoor implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	private IIcon[] aTextureUpper;
	@SideOnly(Side.CLIENT)
	private IIcon[] aTextureLower;

	private final static HashMap<Material, BlockDoor> mDoorMap = new HashMap<Material, BlockDoor>();

	public PlayerDoors(Material aMaterial, String aTextureName, boolean vanillaType) {
		this(aMaterial, aTextureName, vanillaType, 0f, null, null);
	}

	public PlayerDoors(Material aMaterial, String aTextureName, boolean vanillaType, float aHardness,
			SoundType aStepSound, String aBlockExtensionName) {
		super(aMaterial);
		this.disableStats();
		this.isBlockContainer = true;
		if (mDoorMap.get(aMaterial) == null) {
			mDoorMap.put(aMaterial, this);
		}
		float f = 0.5F;
		float f1 = 1.0F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);

		this.setBlockName("playerDoor_" + aTextureName);
		if (aMaterial == Material.wood) {
			setHardness(3.0F);
			setStepSound(soundTypeWood);
			setBlockName("playerDoor" + "Wood");
			this.setHarvestLevel("axe", 1);
		} else if (aMaterial == Material.iron) {
			setHardness(5.0F);
			setStepSound(Block.soundTypeMetal);
			setBlockName("playerDoor" + "Iron");
			this.setHarvestLevel("pickaxe", 1);

		} else if (aMaterial == Material.glass) {
			setHardness(0.1F);
			setStepSound(Block.soundTypeGlass);
			setBlockName("playerDoor" + "Glass");
			this.setHarvestLevel("pickaxe", 1);

		} else if (aMaterial == Material.ice) {
			setHardness(0.5F);
			setStepSound(Block.soundTypeSnow);
			setBlockName("playerDoor" + "Ice");
			this.setHarvestLevel("pickaxe", 1);
		} else {
			setHardness(aHardness);
			setStepSound(aStepSound);
			setBlockName("playerDoor" + aBlockExtensionName);
			this.setHarvestLevel("axe", 1);

		}
		this.setBlockTextureName(vanillaType ? aTextureName : CORE.MODID + ":" + aTextureName);
		GameRegistry.registerBlock(this, ItemBlockDoor.class, Utils.sanitizeString(this.getUnlocalizedName()));
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return this.aTextureLower[0];
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess aAccess, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
		if (p_149673_5_ != 1 && p_149673_5_ != 0) {
			int i1 = this.getState(aAccess, p_149673_2_, p_149673_3_, p_149673_4_);
			int j1 = i1 & 3;
			boolean flag = (i1 & 4) != 0;
			boolean flag1 = false;
			boolean flag2 = (i1 & 8) != 0;

			if (flag) {
				if (j1 == 0 && p_149673_5_ == 2) {
					flag1 = !flag1;
				} else if (j1 == 1 && p_149673_5_ == 5) {
					flag1 = !flag1;
				} else if (j1 == 2 && p_149673_5_ == 3) {
					flag1 = !flag1;
				} else if (j1 == 3 && p_149673_5_ == 4) {
					flag1 = !flag1;
				}
			} else {
				if (j1 == 0 && p_149673_5_ == 5) {
					flag1 = !flag1;
				} else if (j1 == 1 && p_149673_5_ == 3) {
					flag1 = !flag1;
				} else if (j1 == 2 && p_149673_5_ == 4) {
					flag1 = !flag1;
				} else if (j1 == 3 && p_149673_5_ == 2) {
					flag1 = !flag1;
				}

				if ((i1 & 16) != 0) {
					flag1 = !flag1;
				}
			}

			return flag2 ? this.aTextureUpper[flag1 ? 1 : 0] : this.aTextureLower[flag1 ? 1 : 0];
		} else {
			return this.aTextureLower[0];
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.aTextureUpper = new IIcon[2];
		this.aTextureLower = new IIcon[2];
		this.aTextureUpper[0] = p_149651_1_.registerIcon(this.getTextureName() + "_upper");
		this.aTextureLower[0] = p_149651_1_.registerIcon(this.getTextureName() + "_lower");
		this.aTextureUpper[1] = new IconFlipped(this.aTextureUpper[0], true, false);
		this.aTextureLower[1] = new IconFlipped(this.aTextureLower[0], true, false);
	}

	public boolean getBlocksMovement(IBlockAccess aAccess, int aX, int aY, int aZ) {
		int l = this.getState(aAccess, aX, aY, aZ);
		return (l & 4) != 0;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 7;
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
		this.setBlockBoundsBasedOnState(aWorld, aX, aY, aZ);
		return super.getSelectedBoundingBoxFromPool(aWorld, aX, aY, aZ);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
		this.setBlockBoundsBasedOnState(aWorld, aX, aY, aZ);
		return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess aAccess, int aX, int aY, int aZ) {
		this.setBounds(this.getState(aAccess, aX, aY, aZ));
	}

	public int func_150013_e(IBlockAccess p_150013_1_, int p_150013_2_, int p_150013_3_, int p_150013_4_) {
		return this.getState(p_150013_1_, p_150013_2_, p_150013_3_, p_150013_4_) & 3;
	}

	public boolean func_150015_f(IBlockAccess p_150015_1_, int p_150015_2_, int p_150015_3_, int p_150015_4_) {
		return (this.getState(p_150015_1_, p_150015_2_, p_150015_3_, p_150015_4_) & 4) != 0;
	}

	private void setBounds(int aState) {
		float f = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		int j = aState & 3;
		boolean flag = (aState & 4) != 0;
		boolean flag1 = (aState & 16) != 0;

		if (j == 0) {
			if (flag) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				}
			} else {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
			}
		} else if (j == 1) {
			if (flag) {
				if (!flag1) {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				}
			} else {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
			}
		} else if (j == 2) {
			if (flag) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				}
			} else {
				this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
		} else if (j == 3) {
			if (flag) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
			} else {
				this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int p_149727_6_,
			float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (this.blockMaterial == Material.iron) {
			return false; // Allow items to interact with the door
		} else {
			int i1 = this.getState(aWorld, aX, aY, aZ);
			int j1 = i1 & 7;
			j1 ^= 4;

			if ((i1 & 8) == 0) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, j1, 2);
				aWorld.markBlockRangeForRenderUpdate(aX, aY, aZ, aX, aY, aZ);
			} else {
				aWorld.setBlockMetadataWithNotify(aX, aY - 1, aZ, j1, 2);
				aWorld.markBlockRangeForRenderUpdate(aX, aY - 1, aZ, aX, aY, aZ);
			}

			aWorld.playAuxSFXAtEntity(aPlayer, 1003, aX, aY, aZ, 0);
			return true;
		}
	}

	public void func_150014_a(World aWorld, int aX, int aY, int aZ, boolean aFlag) {
		int l = this.getState(aWorld, aX, aY, aZ);
		boolean flag1 = (l & 4) != 0;

		if (flag1 != aFlag) {
			int i1 = l & 7;
			i1 ^= 4;

			if ((l & 8) == 0) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, i1, 2);
				aWorld.markBlockRangeForRenderUpdate(aX, aY, aZ, aX, aY, aZ);
			} else {
				aWorld.setBlockMetadataWithNotify(aX, aY - 1, aZ, i1, 2);
				aWorld.markBlockRangeForRenderUpdate(aX, aY - 1, aZ, aX, aY, aZ);
			}

			aWorld.playAuxSFXAtEntity((EntityPlayer) null, 1003, aX, aY, aZ, 0);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * Block
	 */
	public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aNeighbour) {
		int l = aWorld.getBlockMetadata(aX, aY, aZ);

		if ((l & 8) == 0) {
			boolean flag = false;

			if (aWorld.getBlock(aX, aY + 1, aZ) != this) {
				aWorld.setBlockToAir(aX, aY, aZ);
				flag = true;
			}

			if (!World.doesBlockHaveSolidTopSurface(aWorld, aX, aY - 1, aZ)) {
				aWorld.setBlockToAir(aX, aY, aZ);
				flag = true;

				if (aWorld.getBlock(aX, aY + 1, aZ) == this) {
					aWorld.setBlockToAir(aX, aY + 1, aZ);
				}
			}

			if (flag) {
				if (!aWorld.isRemote) {
					this.dropBlockAsItem(aWorld, aX, aY, aZ, l, 0);
				}
			} else {
				boolean flag1 = aWorld.isBlockIndirectlyGettingPowered(aX, aY, aZ)
						|| aWorld.isBlockIndirectlyGettingPowered(aX, aY + 1, aZ);

				if ((flag1 || aNeighbour.canProvidePower()) && aNeighbour != this) {
					this.func_150014_a(aWorld, aX, aY, aZ, flag1);
				}
			}
		} else {
			if (aWorld.getBlock(aX, aY - 1, aZ) != this) {
				aWorld.setBlockToAir(aX, aY, aZ);
			}

			if (aNeighbour != this) {
				this.onNeighborBlockChange(aWorld, aX, aY - 1, aZ, aNeighbour);
			}
		}
	}

	public Item getItemDropped(int p_149650_1_, Random aRand, int p_149650_3_) {
		if ((p_149650_1_ & 8) != 0) {
			return null;
		} else {
			Block b = mDoorMap.get(this.blockMaterial);
			if (b != null) {
				return Item.getItemFromBlock(b);
			}
		}
		return null;
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector
	 * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
	 */
	public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_,
			Vec3 p_149731_5_, Vec3 p_149731_6_) {
		this.setBlockBoundsBasedOnState(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_);
		return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World aWorld, int aX, int aY, int aZ) {
		boolean aHeight = (aY < aWorld.getHeight() - 1);
		boolean aSolidTopSurface = World.doesBlockHaveSolidTopSurface(aWorld, aX, aY - 1, aZ);

		boolean aCanPlace = aWorld.getBlock(aX, aY, aZ).isReplaceable(aWorld, aX, aY, aZ);
		boolean aCanPlace2 = aWorld.getBlock(aX, aY, aZ).isReplaceable(aWorld, aX, aY + 1, aZ);

		// Logger.INFO(""+aY+"/"+aWorld.getHeight()+" | Trying to place door. Good
		// height? "+aHeight+" | Solid top surface? "+aSolidTopSurface+" | Can Place?
		// "+aCanPlace+"|"+aCanPlace2);

		return aHeight && aSolidTopSurface && aCanPlace && aCanPlace2;
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but
	 * can move over, 2 = total immobility and stop pistons
	 */
	public int getMobilityFlag() {
		return 1;
	}

	public int getState(IBlockAccess aAccess, int aX, int aY, int aZ) {
		int l = aAccess.getBlockMetadata(aX, aY, aZ);
		boolean flag = (l & 8) != 0;
		int i1;
		int j1;

		if (flag) {
			i1 = aAccess.getBlockMetadata(aX, aY - 1, aZ);
			j1 = l;
		} else {
			i1 = l;
			j1 = aAccess.getBlockMetadata(aX, aY + 1, aZ);
		}

		boolean flag1 = (j1 & 1) != 0;
		return i1 & 7 | (flag ? 8 : 0) | (flag1 ? 16 : 0);
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		Block b = mDoorMap.get(this.blockMaterial);
		if (b != null) {
			return Item.getItemFromBlock(b);
		}
		// return this.blockMaterial == Material.iron ? Items.iron_door :
		// Items.wooden_door;
		return null;
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_,
			EntityPlayer p_149681_6_) {
		if (p_149681_6_.capabilities.isCreativeMode && (p_149681_5_ & 8) != 0
				&& p_149681_1_.getBlock(p_149681_2_, p_149681_3_ - 1, p_149681_4_) == this) {
			p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
		p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
	}

	@Override
	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_,
			int p_149696_5_, int p_149696_6_) {
		super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
		TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
		return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityPlayerDoorBase(this, metadata);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityPlayerDoorBase(this, metadata);
	}
}
