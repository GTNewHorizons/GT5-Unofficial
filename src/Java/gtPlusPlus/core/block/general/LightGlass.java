package gtPlusPlus.core.block.general;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightGlass extends BlockBreakable {
	private int			state	= 0;
	private final int	a		= 255;
	private int			r		= 255;
	private int			g		= 0;
	private int			b		= 0;
	private int			hex;

	public LightGlass(final Material mat, final boolean bool) {
		super("blockMFEffect", mat, bool);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setBlockName("blockMFEffect");
		this.setLightLevel(12F);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
		this.setResistance(1);
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly,
	 * and not its normal drops.
	 */
	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	// http://stackoverflow.com/questions/31784658/how-can-i-loop-through-all-rgb-combinations-in-rainbow-order-in-java
	public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
		if (this.state == 0) {
			this.g++;
			if (this.g == 255) {
				this.state = 1;
			}
		}
		if (this.state == 1) {
			this.r--;
			if (this.r == 0) {
				this.state = 2;
			}
		}
		if (this.state == 2) {
			this.b++;
			if (this.b == 255) {
				this.state = 3;
			}
		}
		if (this.state == 3) {
			this.g--;
			if (this.g == 0) {
				this.state = 4;
			}
		}
		if (this.state == 4) {
			this.r++;
			if (this.r == 255) {
				this.state = 5;
			}
		}
		if (this.state == 5) {
			this.b--;
			if (this.b == 0) {
				this.state = 0;
			}
		}
		this.hex = (this.a << 24) + (this.r << 16) + (this.g << 8) + this.b;
		return this.hex;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 0;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(final Random rand) {
		return 0;
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World world, final int posX, final int posY, final int posZ,
			final Random random) {
		Utils.spawnFX(world, posX, posY, posZ, "smoke", "cloud");

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iIcon) {
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + "blockMFEffect");
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}
}