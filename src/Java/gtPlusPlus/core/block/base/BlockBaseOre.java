package gtPlusPlus.core.block.base;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.material.Material;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockBaseOre extends BlockBaseModular implements ITexturedTileEntity {

	@SuppressWarnings("unused")
	private IIcon base;
	@SuppressWarnings("unused")
	private IIcon overlay;

	/*@Override
	public boolean renderAsNormalBlock() {
		return true;
	}*/

	protected Material blockMaterial;

	protected int blockColour;
	protected BlockTypes thisBlock;
	protected String thisBlockMaterial;
	protected final String thisBlockType;

	public BlockBaseOre(final Material material, final BlockTypes blockType, final int colour) {
		this(material.getUnlocalizedName(), material.getLocalizedName(), net.minecraft.block.material.Material.iron, blockType, colour, 3);
		blockMaterial = material;
	}


	public BlockBaseOre(final String unlocalizedName, final String blockMaterial,  final BlockTypes blockType, final int colour) {
		this(unlocalizedName, blockMaterial, net.minecraft.block.material.Material.iron, blockType, colour, 2);
	}

	public BlockBaseOre(final String unlocalizedName, final String blockMaterial, final net.minecraft.block.material.Material vanillaMaterial,  final BlockTypes blockType, final int colour, final int miningLevel) {
		super(unlocalizedName, blockMaterial, vanillaMaterial, blockType, colour, miningLevel);
		this.blockColour = colour;
		this.thisBlock = blockType;
		this.thisBlockMaterial = blockMaterial;
		this.thisBlockType = blockType.name().toUpperCase();
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

	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iIcon)
	{
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + this.thisBlock.getTexture());
		//this.base = iIcon.registerIcon(CORE.MODID + ":" + "blockStone");
		//this.overlay = iIcon.registerIcon(CORE.MODID + ":" + "blockOre_Overlay");
	}*/

	/*@Override
	public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4){
		if (this.blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.blockColour;
	}

	@Override
	public int getRenderColor(final int aMeta) {
		if (this.blockColour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.blockColour;
	}*/

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	@Override
	public int getRenderType() {
		return CustomOreBlockRenderer.INSTANCE.mRenderID;
	}

	@Override
	public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int aSide) {
		return Blocks.stone.getIcon(0, 0);
	}

	@Override
	public IIcon getIcon(int aSide, int aMeta) {
		return Blocks.stone.getIcon(0, 0);
	}	

	/**
	 * GT Texture Handler
	 */

	@Override
	public ITexture[] getTexture(Block block, byte side) {
		if (this.blockMaterial != null){
			GT_RenderedTexture aIconSet = new GT_RenderedTexture(Materials.Iron.mIconSet.mTextures[OrePrefixes.ore.mTextureIndex], this.blockMaterial.getRGBA());
			if (aIconSet != null){
				//Logger.INFO("[Render] Good Overlay.");
				return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), aIconSet};				
			}
		}
		return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.STONES[0], new short[]{240, 240, 240, 0})};			
	}



}
