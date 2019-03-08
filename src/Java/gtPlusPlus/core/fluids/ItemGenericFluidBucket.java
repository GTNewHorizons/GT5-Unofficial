package gtPlusPlus.core.fluids;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event;
import gtPlusPlus.api.objects.GregtechException;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class ItemGenericFluidBucket extends ItemBucket {

	private static IIcon mBaseBucketTexture;
	private static IIcon mOverlayBucketTexture;	
	private static AutoMap<Block> mInternalFluidCache = new AutoMap<Block>();	
	
	public ItemGenericFluidBucket(Block aFluid) {
		super(aFluid);
		this.setContainerItem(Items.bucket);
		this.maxStackSize = 1;
		mInternalFluidCache.put(aFluid);
	}
	
	public static ItemStack registerFluidForBucket(int aID) {
		
		if (FluidFactory.mMetaToBucketMap.containsKey(aID)) {
			try {
				throw new GregtechException(""+aID+" is already registered! Unable to register fluid: "+FluidFactory.mMetaToFluidMap.get(aID).getLocalizedName());
			} catch (GregtechException e) {
				System.exit(0);
			}
		}		
		mInternalFluidCache.put(FluidFactory.mMetaToBlockMap.get(aID));
		return ItemUtils.simpleMetaStack(FluidFactory.mGenericBucket, aID, 1);
	}

	Map<Integer, IIcon> mIconCache = new LinkedHashMap<Integer, IIcon>();

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {

		Block isFull = FluidFactory.mMetaToBlockMap.get(aStack.getItemDamage());

		boolean flag = isFull == Blocks.air;
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(aWorld, aPlayer, flag);

		if (movingobjectposition == null || isFull == null) {
			return aStack;
		} else {
			FillBucketEvent event = new FillBucketEvent(aPlayer, aStack, aWorld, movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return aStack;
			}

			if (event.getResult() == Event.Result.ALLOW) {
				if (aPlayer.capabilities.isCreativeMode) {
					return aStack;
				}

				if (--aStack.stackSize <= 0) {
					return event.result;
				}

				if (!aPlayer.inventory.addItemStackToInventory(event.result)) {
					aPlayer.dropPlayerItemWithRandomChoice(event.result, false);
				}

				return aStack;
			}
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!aWorld.canMineBlock(aPlayer, i, j, k)) {
					return aStack;
				}

				if (flag) {
					if (!aPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, aStack)) {
						return aStack;
					}

					Material material = aWorld.getBlock(i, j, k).getMaterial();
					int l = aWorld.getBlockMetadata(i, j, k);

					if (material == Material.water && l == 0) {
						aWorld.setBlockToAir(i, j, k);
						return this.func_150910_a(aStack, aPlayer, Items.water_bucket);
					}

					if (material == Material.lava && l == 0) {
						aWorld.setBlockToAir(i, j, k);
						return this.func_150910_a(aStack, aPlayer, Items.lava_bucket);
					}
				} else {
					if (isFull == Blocks.air) {
						return new ItemStack(Items.bucket);
					}

					if (movingobjectposition.sideHit == 0) {
						--j;
					}

					if (movingobjectposition.sideHit == 1) {
						++j;
					}

					if (movingobjectposition.sideHit == 2) {
						--k;
					}

					if (movingobjectposition.sideHit == 3) {
						++k;
					}

					if (movingobjectposition.sideHit == 4) {
						--i;
					}

					if (movingobjectposition.sideHit == 5) {
						++i;
					}

					if (!aPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, aStack)) {
						return aStack;
					}

					if (this.tryPlaceContainedLiquid(isFull, aWorld, i, j, k) && !aPlayer.capabilities.isCreativeMode) {
						return new ItemStack(Items.bucket);
					}
				}
			}

			return aStack;
		}
	}

	private ItemStack func_150910_a(ItemStack p_150910_1_, EntityPlayer p_150910_2_, Item p_150910_3_) {
		if (p_150910_2_.capabilities.isCreativeMode) {
			return p_150910_1_;
		} else if (--p_150910_1_.stackSize <= 0) {
			return new ItemStack(p_150910_3_);
		} else {
			if (!p_150910_2_.inventory.addItemStackToInventory(new ItemStack(p_150910_3_))) {
				p_150910_2_.dropPlayerItemWithRandomChoice(new ItemStack(p_150910_3_, 1, 0), false);
			}

			return p_150910_1_;
		}
	}

	/**
	 * Attempts to place the liquid contained inside the bucket.
	 */
	public boolean tryPlaceContainedLiquid(Block isFull, World aWorld, int aX, int aY, int aZ) {
		if (isFull == Blocks.air) {
			return false;
		} else {
			Material material = aWorld.getBlock(aX, aY, aZ).getMaterial();
			boolean flag = !material.isSolid();

			if (!aWorld.isAirBlock(aX, aY, aZ) && !flag) {
				return false;
			} else {

				if (!aWorld.isRemote && flag && !material.isLiquid()) {
					aWorld.func_147480_a(aX, aY, aZ, true);
				}

				aWorld.setBlock(aX, aY, aZ, isFull, 0, 3);

				return true;
			}
		}
	}

	@Override
	public IIcon getIconFromDamage(int aMeta) {
		IIcon aTemp = mIconCache.get(aMeta);
		return aTemp != null ? aTemp : super.getIconFromDamage(aMeta);
	}

	@Override
	public boolean getHasSubtypes() {
		return mInternalFluidCache.size() > 0;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));			
			for (Block f : mInternalFluidCache) {
				Integer aMeta;
				if (f != null) {
					aMeta = FluidFactory.mBlockToMetaMap.get(f);
					if (aMeta != null) {
						list.add(new ItemStack(item, 1, aMeta));						
					}
				}			
			}		
	}

	@Override
	public int getMaxDamage() {
		return 512;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		// TODO Auto-generated method stub
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {		
		if (stack != null && renderPass == 1) {
			return mOverlayBucketTexture;
		}
		else {
			return mBaseBucketTexture;
		}		
		/*IIcon aTemp = mIconCache.get(stack.getItemDamage());
		return aTemp != null ? aTemp : super.getIcon(stack, renderPass, player, usingItem, useRemaining);*/
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 512;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return false;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}	
	
	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if (pass == 1) {
			return mOverlayBucketTexture;
		}
		else {
			return mBaseBucketTexture;
		}
	}

	@Override
	public void registerIcons(final IIconRegister i) {
		mBaseBucketTexture = i.registerIcon("bucket_empty");
		mOverlayBucketTexture = i.registerIcon(CORE.MODID+":bucket.generic.overlay");		
	}

	@Override
	public boolean tryPlaceContainedLiquid(World p_77875_1_, int p_77875_2_, int p_77875_3_, int p_77875_4_) {
		return tryPlaceContainedLiquid(Blocks.air, p_77875_1_, p_77875_2_, p_77875_3_, p_77875_4_);
	}

	@Override
	public int getColorFromItemStack(ItemStack aStack, int aPass) {
		if (aPass == 0) {
			return super.getColorFromItemStack(aStack, aPass);
		}
		else {
			return FluidFactory.mMetaToColourMap.get(aStack.getItemDamage());
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	

}
