package gtPlusPlus.core.item.tool.staballoy;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class StaballoyAxe extends ItemAxe {
	public String mat;

	// EXPLODE TREE
	byte[]	tre			= new byte[32000];

	byte	unchecked	= 0;
	byte	needcheck	= 1;
	byte	ignore		= 2;
	byte	harvest		= 3;
	public StaballoyAxe(final String unlocalizedName, final ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GOLD + "Fells entire trees in a single swipe!..");
		list.add(EnumChatFormatting.GRAY + "Ask Alkalus for new trees to be supported.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	private void breakMushroom(final World wld, final Block bit, final EntityPlayer plr, final boolean silk,
			final int x, final int y, final int z, final int met) {
		if (silk) {
			final ItemStack stk = null; // TODO
			/*
			 * if (bit==Blocks.brown_mushroom_block) stk = new
			 * ItemStack(LoonToolItems.brown_mushroom_block,1,met); else if
			 * (bit==Blocks.red_mushroom_block) stk = new
			 * ItemStack(LoonToolItems.red_mushroom_block,1,met); else stk = new
			 * ItemStack(bit,1,met);
			 */
			final EntityItem entityitem = new EntityItem(wld, x + 0.5, y + 0.5, z + 0.5, stk);
			entityitem.delayBeforeCanPickup = 10;
			wld.spawnEntityInWorld(entityitem);
		}
		else {
			bit.harvestBlock(wld, plr, x, y, z, met);
		}
		wld.setBlockToAir(x, y, z);
	}

	public boolean canIgnore(final Block bit) {
		if (bit instanceof BlockAir) {
			return true;
		}
		if (bit instanceof BlockGrass) {
			return true;
		}
		if (bit instanceof BlockSand) {
			return true;
		}
		if (bit instanceof BlockDirt) {
			return true;
		}
		if (bit instanceof BlockCocoa) {
			return true;
		}
		if (bit instanceof BlockVine) {
			return true;
		}
		if (bit instanceof BlockMushroom) {
			return true;
		}
		if (bit instanceof BlockSnow) {
			return true;
		}
		if (bit instanceof BlockSnowBlock) {
			return true;
		}
		if (bit instanceof BlockFlower) {
			return true;
		}
		if (bit instanceof BlockTallGrass) {
			return true;
		}
		if (bit instanceof BlockDoublePlant) {
			return true;
		}

		// LoonTools.log("Found uncuttable "+bit.getClass().getSimpleName());
		return false;
	}

	private int check(final World par1World, final int x, final int y, final int z, final int xo, final int yo,
			final int zo) {
		int f = 0;
		final int o = x + z * 20 + y * 400;
		if (this.tre[o] == this.needcheck) {
			this.tre[o] = this.ignore;
			final Block bit = par1World.getBlock(x + xo, y + yo, z + zo);
			if (bit instanceof BlockLog || bit instanceof BlockLeavesBase || bit instanceof BlockHugeMushroom
					|| bit.getUnlocalizedName().toLowerCase().contains("log")
					|| bit.getUnlocalizedName().toLowerCase().contains("wood")) {
				f = 1;
				this.tre[o] = this.harvest;
				// if (bit instanceof BlockLog){
				// LoonTools.log("^ Found log @ "+x+xo+" "+y+yo+" "+z+zo+" ");
				// }
				for (int xb = -1; xb < 2; xb++) {
					for (int yb = -1; yb < 2; yb++) {
						for (int zb = -1; zb < 2; zb++) {
							if (!this.setcheck(x + xb, y + yb, z + zb)) {
								return 3;
							}
						}
					}
				}
			}
			else {
				if (!this.canIgnore(bit)) {
					return 2;
				}
			}
		}
		return f;
	}

	private int check2(final World par1World, final int x, final int y, final int z, final int xo, final int yo,
			final int zo) {
		int f = 0;
		final int o = x + z * 20 + y * 400;
		if (this.tre[o] == this.needcheck) {
			this.tre[o] = this.ignore;
			final Block bit = par1World.getBlock(x + xo, y + yo, z + zo);
			if (bit instanceof BlockLog) {
				f = 1;
				this.tre[o] = this.harvest;
				// if (bit instanceof BlockLog){
				// LoonTools.log("^ Found log @ "+x+xo+" "+y+yo+" "+z+zo+" ");
				// }
				for (int xb = -1; xb < 2; xb++) {
					for (int yb = -1; yb < 2; yb++) {
						for (int zb = -1; zb < 2; zb++) {
							if (!this.setcheck(x + xb, y + yb, z + zb)) {
								return 3;
							}
						}
					}
				}
			}
			else if (bit instanceof BlockLeavesBase) {
			}
			else {
				if (!this.canIgnore(bit)) {
					return 2;
				}
			}
		}
		return f;
	}

	public int checkTree(final World par1World, final int xo, final int yo, final int zo) {
		boolean f;
		for (f = true; f == true;) {
			f = false;
			for (int y = 0; y < 80; y++) {
				for (int z = 0; z < 20; z++) {
					for (int x = 0; x < 20; x++) {
						final int r = this.check(par1World, x, y, z, xo, yo, zo);
						if (r == 3) {
							return 3;
						}
						if (r == 2) {
							return 2;
						}
						if (r == 1) {
							f = true;
						}
					}
				}
			}

			for (int y = 79; y >= 0; y--) {
				for (int z = 19; z >= 0; z--) {
					for (int x = 19; x >= 0; x--) {
						final int r = this.check(par1World, x, y, z, xo, yo, zo);
						if (r == 2) {
							return 3;
						}
						if (r == 2) {
							return 2;
						}
						if (r == 1) {
							f = true;
						}
					}
				}
			}
		}
		return 1;
	}

	public int checkTree2(final World par1World, final int xo, final int yo, final int zo) {
		boolean f;
		for (f = true; f == true;) {
			f = false;
			for (int y = 0; y < 80; y++) {
				for (int z = 0; z < 20; z++) {
					for (int x = 0; x < 20; x++) {
						final int r = this.check2(par1World, x, y, z, xo, yo, zo);
						if (r == 3) {
							return 3;
						}
						if (r == 2) {
							return 2;
						}
						if (r == 1) {
							f = true;
						}
					}
				}
			}

			for (int y = 79; y >= 0; y--) {
				for (int z = 19; z >= 0; z--) {
					for (int x = 19; x >= 0; x--) {
						final int r = this.check2(par1World, x, y, z, xo, yo, zo);
						if (r == 2) {
							return 3;
						}
						if (r == 2) {
							return 2;
						}
						if (r == 1) {
							f = true;
						}
					}
				}
			}
		}
		return 1;
	}

	public void exploadMushroom(final World par1World, final int xo, final int yo, final int zo, final EntityPlayer plr,
			final boolean silk) {
		for (int y = 0; y < 80; y++) {
			for (int z = 0; z < 20; z++) {
				for (int x = 0; x < 20; x++) {
					final int o = x + z * 20 + y * 400;
					if (this.tre[o] == this.harvest) {
						final Block bit = par1World.getBlock(x + xo, y + yo, z + zo);
						final int met = par1World.getBlockMetadata(x + xo, y + yo, z + zo);
						if (bit instanceof BlockHugeMushroom) {
							this.breakMushroom(par1World, bit, plr, silk, x + xo, y + yo, z + zo, met);
						}
						else {
							bit.harvestBlock(par1World, plr, x + xo, y + yo, z + zo, met);
							par1World.setBlockToAir(x + xo, y + yo, z + zo);
						}
					}
				}
			}
		}
	}

	public void exploadTree(final World par1World, final int xo, final int yo, final int zo, final EntityPlayer plr) {
		for (int y = 0; y < 80; y++) {
			for (int z = 0; z < 20; z++) {
				for (int x = 0; x < 20; x++) {
					final int o = x + z * 20 + y * 400;
					if (this.tre[o] == this.harvest) {
						final Block bit = par1World.getBlock(x + xo, y + yo, z + zo);
						final int met = par1World.getBlockMetadata(x + xo, y + yo, z + zo);

						if (bit instanceof BlockLog || bit instanceof BlockLeavesBase
								|| bit.getUnlocalizedName().toLowerCase().contains("log")
								|| bit.getUnlocalizedName().toLowerCase().contains("wood")) {
							bit.harvestBlock(par1World, plr, x + xo, y + yo, z + zo, met);
							par1World.setBlockToAir(x + xo, y + yo, z + zo);
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack itm, final World wld, final Block blk, final int x, final int y,
			final int z, final EntityLivingBase plr) {
		if (!wld.isRemote) {
			final Block bit = wld.getBlock(x, y, z);
			final boolean silk = EnchantmentHelper.getSilkTouchModifier(plr);
			if (bit instanceof BlockHugeMushroom) {
				for (int n = 0; n < 32000; n++) {
					this.tre[n] = this.unchecked;
				}
				final int met = wld.getBlockMetadata(x, y, z);
				this.breakMushroom(wld, bit, (EntityPlayer) plr, silk, x, y, z, met);
				wld.setBlockToAir(x, y, z);
				this.tre[2210] = this.needcheck;
				if (this.checkTree(wld, x - 10, y - 4, z - 10) == 1) {
					this.exploadMushroom(wld, x - 10, y - 4, z - 10, (EntityPlayer) plr, silk);
				}
			}

			if (bit instanceof BlockLog || bit.getUnlocalizedName().toLowerCase().contains("log")
					|| bit.getUnlocalizedName().toLowerCase().contains("wood")) {
				// LoonTools.log("cutting tree @ "+x+" "+y+" "+z+" ");
				for (int n = 0; n < 32000; n++) {
					this.tre[n] = this.unchecked;
				}
				final int met = wld.getBlockMetadata(x, y, z);
				bit.harvestBlock(wld, (EntityPlayer) plr, x, y, z, met);
				wld.setBlockToAir(x, y, z);
				this.tre[2210] = this.needcheck;
				if (this.checkTree(wld, x - 10, y - 4, z - 10) == 1) {
					this.exploadTree(wld, x - 10, y - 4, z - 10, (EntityPlayer) plr);
				}
				else {
					for (int n = 0; n < 32000; n++) {
						this.tre[n] = this.unchecked;
					}
					this.tre[2210] = this.needcheck;
					if (this.checkTree2(wld, x - 10, y - 4, z - 10) == 1) {
						this.exploadTree(wld, x - 10, y - 4, z - 10, (EntityPlayer) plr);
					}
				}
			}
		}
		return super.onBlockDestroyed(itm, wld, blk, x, y, z, plr);
	}

	private boolean setcheck(final int x, final int y, final int z) {
		if (x < 0 || x > 19 || z < 0 || z > 19 || y < 0 || y > 79) {
			return false;
		}
		final int o = x + z * 20 + y * 400;
		if (this.tre[o] == this.unchecked) {
			this.tre[o] = this.needcheck;
		}
		return true;
	}
}