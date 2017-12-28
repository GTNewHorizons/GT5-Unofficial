package gtPlusPlus.core.block.base;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockBaseOre extends BlockBaseModular implements ITexturedTileEntity {

	@SuppressWarnings("unused")
	private IIcon base;
	@SuppressWarnings("unused")
	private IIcon overlay;

	protected Material blockMaterial;

	protected int blockColour;
	protected BlockTypes thisBlock;

	public BlockBaseOre(final Material material, final BlockTypes blockType, final int colour) {
		this(material.getUnlocalizedName(), material.getLocalizedName(), net.minecraft.block.material.Material.iron, blockType, colour, 3);
		blockMaterial = material;
	}

	public BlockBaseOre(final String unlocalizedName, final String blockMaterial,  final BlockTypes blockType, final int colour) {
		this(unlocalizedName, blockMaterial, net.minecraft.block.material.Material.iron, blockType, colour, 2);
	}

	public BlockBaseOre(final String unlocalizedName, final String blockMaterial, final net.minecraft.block.material.Material vanillaMaterial,  final BlockTypes blockType, final int colour, final int miningLevel) {
		super(unlocalizedName, blockMaterial, vanillaMaterial, blockType, colour, miningLevel);
		try {
		if (blockMaterial == null){
			Logger.DEBUG_MATERIALS("Failed to generate "+unlocalizedName+" due to invalid material variable.");
		}
		
		this.blockColour = colour;
		this.thisBlock = blockType;
		
		if (this == null || this.blockMaterial == null){
			Logger.DEBUG_MATERIALS("Issue during Ore construction, Material or Block is null.");
		}
		
		GameRegistry.registerBlock(this, ItemBlockGtBlock.class, Utils.sanitizeString("ore"+Utils.sanitizeString(this.blockMaterial.getLocalizedName())));
		GT_OreDictUnificator.registerOre("ore"+Utils.sanitizeString(this.blockMaterial.getLocalizedName()), ItemUtils.getSimpleStack(this));
		LanguageRegistry.addName(this, blockMaterial+ " Ore");
		}
		catch (Throwable t){
			t.printStackTrace();
		}
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 0;
	}

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

	public static class oldOreBlock extends BlockBaseModular{
		@SuppressWarnings("unused")
		private IIcon base;
		@SuppressWarnings("unused")
		private IIcon overlay;

		protected Material blockMaterial;

		protected int blockColour;
		protected BlockTypes thisBlock;
		protected String thisBlockMaterial;
		protected final String thisBlockType;

		public oldOreBlock(final Material material, final BlockTypes blockType, final int colour) {
			this(material.getUnlocalizedName(), material.getLocalizedName(), net.minecraft.block.material.Material.iron, blockType, colour, 3);
			blockMaterial = material;
		}

		public oldOreBlock(final String unlocalizedName, final String blockMaterial,  final BlockTypes blockType, final int colour) {
			this(unlocalizedName, blockMaterial, net.minecraft.block.material.Material.iron, blockType, colour, 2);
		}

		public oldOreBlock(final String unlocalizedName, final String blockMaterial, final net.minecraft.block.material.Material vanillaMaterial,  final BlockTypes blockType, final int colour, final int miningLevel) {
			super(unlocalizedName, blockMaterial, vanillaMaterial, blockType, colour, miningLevel);
			this.blockColour = colour;
			this.thisBlock = blockType;
			this.thisBlockMaterial = blockMaterial;
			this.thisBlockType = blockType.name().toUpperCase();
			this.setBlockTextureName(CORE.MODID+":"+blockType.getTexture());
		}

		/**
		 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
		 */

		@Override
		@SideOnly(Side.CLIENT)
		public int getRenderBlockPass(){
			return 0;
		}

		@Override
		public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
			return false;
		}

		@Override
		public int getRenderType() {
			return 0;
		}

		@Override
		public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int aSide) {
			return Blocks.stone.getIcon(0, 0);
		}

		@Override
		public IIcon getIcon(int aSide, int aMeta) {
			return Blocks.stone.getIcon(0, 0);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(final IIconRegister iIcon)
		{
			this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + this.thisBlock.getTexture());
		}

		@Override
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
		}

	}

}
