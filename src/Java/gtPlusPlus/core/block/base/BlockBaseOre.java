package gtPlusPlus.core.block.base;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBaseOre extends BlockBaseModular{

	@SuppressWarnings("unused")
	private IIcon base;
	@SuppressWarnings("unused")
	private IIcon overlay;

	/*@Override
	public boolean renderAsNormalBlock() {
		return true;
	}*/

	public BlockBaseOre(String unlocalizedName, String blockMaterial,  BlockTypes blockType, int colour) {
		this(unlocalizedName, blockMaterial, Material.iron, blockType, colour, 2);
	}
	
	public BlockBaseOre(String unlocalizedName, String blockMaterial, Material vanillaMaterial,  BlockTypes blockType, int colour, int miningLevel) {
		super(unlocalizedName, blockMaterial, vanillaMaterial, blockType, colour, miningLevel);		
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 0;
	}	*/

	/*@Override
	public boolean isOpaqueCube()
    {
	    return true;
    }*/

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIcon)
	{
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + thisBlock.getTexture());
		//this.base = iIcon.registerIcon(CORE.MODID + ":" + "blockStone");	
		//this.overlay = iIcon.registerIcon(CORE.MODID + ":" + "blockOre_Overlay");
	}

	@Override
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
		if (this.blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}		
		return this.blockColour;
	}
	
    @Override
	public int getRenderColor(int aMeta) {
    	if (this.blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.blockColour;
    }
    
	
	

}
