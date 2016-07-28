package miscutil.core.block.base;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBaseModular extends BasicBlock{

	protected int blockColour;
	protected BlockTypes thisBlock;
	protected String thisBlockMaterial;

	public BlockBaseModular(String unlocalizedName, String blockMaterial,  BlockTypes blockType, int colour) {
		super(unlocalizedName, Material.iron);
		this.setHarvestLevel(blockType.getHarvestTool(), 2);
		this.setBlockTextureName(CORE.MODID+":"+blockType.getTexture());
		this.blockColour = colour;
		this.thisBlock = blockType;
		this.thisBlockMaterial = blockMaterial;
		this.setBlockName(getLocalizedName());
		LanguageRegistry.addName(this, getLocalizedName());
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		if (thisBlock == BlockTypes.FRAME){
			return 1;			
		}
		return 0;
	}
	
	@Override
	public String getLocalizedName() {
		String tempIngot = getUnlocalizedName().replace("tile.blockGtFrame", "");	
		if (thisBlock == BlockTypes.FRAME){
			tempIngot = thisBlockMaterial + " Frame Box";
		}
		else {

			tempIngot = getUnlocalizedName().replace("tile.blockGt", "ingot");
		}
		return tempIngot;
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
		
		if (this.blockColour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		
		return this.blockColour;
	}
	
    @Override
	public int getRenderColor(int aMeta) {
    	if (this.blockColour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		
		return this.blockColour;
    }

	@Override
	public int getBlockColor() {
		return this.blockColour;
	}

}
