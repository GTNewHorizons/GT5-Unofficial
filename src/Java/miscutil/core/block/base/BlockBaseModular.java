package miscutil.core.block.base;

import miscutil.core.lib.CORE;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBaseModular extends BasicBlock{

	protected int blockColour;
	protected BlockTypes thisBlock;

	public BlockBaseModular(String unlocalizedName, BlockTypes blockType, int colour) {
		super(unlocalizedName, Material.iron);
		this.setHarvestLevel(blockType.getHarvestTool(), 2);
		this.setBlockTextureName(CORE.MODID+":"+blockType.getTexture());
		this.blockColour = colour;
		this.thisBlock = blockType;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		if (thisBlock.name() == BlockTypes.FRAME.name()){
			return 1;			
		}
		return 0;
	}
	
	@Override
	public boolean isOpaqueCube()
    {
	    return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIcon)
	{
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + thisBlock.getTexture());
	}

	@Override
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
		return this.blockColour;
	}

}
