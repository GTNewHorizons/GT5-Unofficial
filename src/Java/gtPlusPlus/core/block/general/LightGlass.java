package gtPlusPlus.core.block.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LightGlass extends BlockBreakable
{
	private int state = 0;
	private int a = 255;
	private int r = 255;
	private int g = 0;
	private int b = 0;
	private int hex;

	public LightGlass(Material mat, boolean bool)
	{
		super("blockMFEffect", mat, bool);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setBlockName("blockMFEffect");
		this.setLightLevel(12F);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
		this.setResistance(1);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 0;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
	 */
	@Override
	protected boolean canSilkHarvest()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIcon)
	{
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + "blockMFEffect");
	}

	@Override
	//http://stackoverflow.com/questions/31784658/how-can-i-loop-through-all-rgb-combinations-in-rainbow-order-in-java
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		if(state == 0){
			g++;
			if(g == 255)
				state = 1;
		}
		if(state == 1){
			r--;
			if(r == 0)
				state = 2;
		}
		if(state == 2){
			b++;
			if(b == 255)
				state = 3;
		}
		if(state == 3){
			g--;
			if(g == 0)
				state = 4;
		}
		if(state == 4){
			r++;
			if(r == 255)
				state = 5;
		}
		if(state == 5){
			b--;
			if(b == 0)
				state = 0;
		}
		hex = (a << 24) + (r << 16) + (g << 8) + (b);
		return hex;
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int posX, int posY, int posZ, Random random){
		Utils.spawnFX(world, posX, posY, posZ, "smoke", "cloud");
		
	}
}