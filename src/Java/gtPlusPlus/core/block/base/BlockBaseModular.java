package gtPlusPlus.core.block.base;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtFrameBox;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockBaseModular extends BasicBlock {

	protected Material blockMaterial;

	protected int blockColour;
	protected BlockTypes thisBlock;
	protected String thisBlockMaterial;
	protected final String thisBlockType;

	public BlockBaseModular(final Material material, final BlockTypes blockType) {
		this(material, blockType, material.getRgbAsHex());
	}
	
	public BlockBaseModular(final Material material, final BlockTypes blockType, final int colour) {
		this(material.getUnlocalizedName(), material.getLocalizedName(), net.minecraft.block.material.Material.iron,
				blockType, colour, Math.min(Math.max(material.vTier, 1), 5));
		blockMaterial = material;
	}

	protected BlockBaseModular(final String unlocalizedName, final String blockMaterial,
			final net.minecraft.block.material.Material vanillaMaterial, final BlockTypes blockType, final int colour,
			final int miningLevel) {
		super(unlocalizedName, vanillaMaterial);
		this.setHarvestLevel(blockType.getHarvestTool(), miningLevel);
		this.setBlockTextureName(CORE.MODID + ":" + blockType.getTexture());
		this.blockColour = colour;
		this.thisBlock = blockType;
		this.thisBlockMaterial = blockMaterial;
		this.thisBlockType = blockType.name().toUpperCase();
		this.setBlockName(this.GetProperName());

		if (this.thisBlockType.equals(BlockTypes.STANDARD.name().toUpperCase())) {
			GameRegistry.registerBlock(this, ItemBlockGtBlock.class,
					Utils.sanitizeString(blockType.getTexture() + unlocalizedName));
			GT_OreDictUnificator.registerOre(
					"block" + getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "")
							.replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""),
					ItemUtils.getSimpleStack(this));
		}
		else if (this.thisBlockType.equals(BlockTypes.FRAME.name().toUpperCase())) {
			GameRegistry.registerBlock(this, ItemBlockGtBlock.class,
					Utils.sanitizeString(blockType.getTexture() + unlocalizedName));
			GT_OreDictUnificator.registerOre(
					"frameGt" + getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "")
							.replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""),
					ItemUtils.getSimpleStack(this));
		}
		else if (this.thisBlockType.equals(BlockTypes.ORE.name().toUpperCase())) {
			GameRegistry.registerBlock(this, ItemBlockGtBlock.class,
					Utils.sanitizeString(blockType.getTexture() + unlocalizedName));
			GT_OreDictUnificator.registerOre(
					"block" + getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "")
							.replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""),
					ItemUtils.getSimpleStack(this));
		}

	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		if (this.thisBlock == BlockTypes.FRAME) {
			return 1;
		}
		return 0;
	}

	public String GetProperName() {
		String tempIngot;
		if (this.thisBlock == BlockTypes.STANDARD) {
			tempIngot = "Block of " + this.thisBlockMaterial;
		}
		else if (this.thisBlock == BlockTypes.FRAME) {
			tempIngot = this.thisBlockMaterial + " Frame Box";
		}
		else if (this.thisBlock == BlockTypes.ORE) {
			tempIngot = this.thisBlockMaterial + " Ore [Old]";
		}
		else {

			tempIngot = this.getUnlocalizedName().replace("tile.blockGt", "ingot");
		}
		return tempIngot;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public Material getMaterialEx(){
		return this.blockMaterial;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iIcon) {
			if (!CORE.ConfigSwitches.useGregtechTextures || this.blockMaterial == null || this.thisBlock == BlockTypes.ORE){
				this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + this.thisBlock.getTexture());
			}
			String metType = "9j4852jyo3rjmh3owlhw9oe"; 
			if (this.blockMaterial != null) {
				TextureSet u = this.blockMaterial.getTextureSet();
				if (u != null) {
					metType = u.mSetName;				
				}
			}		
			metType = (metType.equals("9j4852jyo3rjmh3owlhw9oe") ? "METALLIC" : metType);	
			int tier = this.blockMaterial.vTier;
			String aType = (this.thisBlock == BlockTypes.FRAME) ? "frameGt" : (tier < 3 ? "block1" : tier < 6 ? "block6" : "block5");			
			this.blockIcon = iIcon.registerIcon("gregtech" + ":" + "materialicons/"+ "METALLIC" +"/" + aType);
	}

	@Override
	public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {

		if (this.blockColour == 0) {
			return MathUtils.generateSingularRandomHexValue();
		}

		return this.blockColour;
	}

	@Override
	public int getRenderColor(final int aMeta) {
		if (this.blockColour == 0) {
			return MathUtils.generateSingularRandomHexValue();
		}

		return this.blockColour;
	}

}
