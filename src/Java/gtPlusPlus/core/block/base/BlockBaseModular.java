package gtPlusPlus.core.block.base;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtFrameBox;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

public class BlockBaseModular extends BasicBlock{

	protected Material blockMaterial;
	
	protected int blockColour;
	protected BlockTypes thisBlock;
	protected String thisBlockMaterial;
	protected final String thisBlockType;

	public BlockBaseModular(final Material material, final BlockTypes blockType, final int colour) {
		this(material.getUnlocalizedName(), material.getLocalizedName(), net.minecraft.block.material.Material.iron, blockType, colour, 2);
	}
	
	public BlockBaseModular(final String unlocalizedName, final String blockMaterial,  final BlockTypes blockType, final int colour) {
		this(unlocalizedName, blockMaterial, net.minecraft.block.material.Material.iron, blockType, colour, 2);
	}

	public BlockBaseModular(final String unlocalizedName, final String blockMaterial, final net.minecraft.block.material.Material vanillaMaterial,  final BlockTypes blockType, final int colour, final int miningLevel) {
		super(unlocalizedName, vanillaMaterial);
		this.setHarvestLevel(blockType.getHarvestTool(), miningLevel);
		this.setBlockTextureName(CORE.MODID+":"+blockType.getTexture());
		this.blockColour = colour;
		this.thisBlock = blockType;
		this.thisBlockMaterial = blockMaterial;
		this.thisBlockType = blockType.name().toUpperCase();
		this.setBlockName(this.GetProperName());

		if (!CORE.DEBUG){
			//Utils.LOG_INFO("=============Block Info Dump=============");
			//Utils.LOG_INFO("thisBlock.name().toLowerCase() - "+thisBlock.name().toLowerCase());
			//Utils.LOG_INFO("This Blocks Type - "+thisBlockType);
			//Utils.LOG_INFO("BlockTypes.STANDARD.name().toLowerCase() - "+BlockTypes.STANDARD.name().toLowerCase());
			//Utils.LOG_INFO("BlockTypes.FRAME.name().toLowerCase() - "+BlockTypes.FRAME.name().toLowerCase());
			//Utils.LOG_INFO("blockMaterial - "+blockMaterial);
			//Utils.LOG_INFO("==========================================");
		}

		if (this.thisBlockType.equals(BlockTypes.STANDARD.name().toUpperCase())){
			//LanguageRegistry.addName(this, "Block of "+blockMaterial);
			//Utils.LOG_INFO("Registered Block in Language Registry as: "+"Block of "+blockMaterial);
		}
		else if (this.thisBlockType.equals(BlockTypes.FRAME.name().toUpperCase())){
			//LanguageRegistry.addName(this, blockMaterial+ " Frame Box");
			//Utils.LOG_INFO("Registered Block in Language Registry as: "+blockMaterial+ " Frame Box");
		}

		//setOreDict(unlocalizedName, blockType);
		if (this.thisBlockType.equals(BlockTypes.STANDARD.name().toUpperCase())){
			GameRegistry.registerBlock(this, ItemBlockGtBlock.class, Utils.sanitizeString(blockType.getTexture()+unlocalizedName));
			GT_OreDictUnificator.registerOre("block"+getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""), ItemUtils.getSimpleStack(this));
			//Utils.LOG_INFO("Registered Block in Block Registry as: "+"Block of "+blockMaterial);
		}
		else if (this.thisBlockType.equals(BlockTypes.FRAME.name().toUpperCase())){
			GameRegistry.registerBlock(this, ItemBlockGtFrameBox.class, Utils.sanitizeString(blockType.getTexture()+unlocalizedName));
			GT_OreDictUnificator.registerOre("frameGt"+getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), ItemUtils.getSimpleStack(this));
			//Utils.LOG_INFO("Registered Block in Block Registry as: "+blockMaterial+" Frame Box");
		}


	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		if (this.thisBlock == BlockTypes.FRAME){
			return 1;
		}
		return 0;
	}

	/*@Override
	public String getLocalizedName() {
		String tempIngot;
		if (thisBlock == BlockTypes.STANDARD){
			tempIngot = "Block of "+thisBlockMaterial;
		}
		else if (thisBlock == BlockTypes.FRAME){
			tempIngot = thisBlockMaterial + " Frame Box";
		}
		else {

			tempIngot = getUnlocalizedName().replace("tile.blockGt", "ingot");
		}
		return tempIngot;
	}*/

	public String GetProperName() {
		String tempIngot;
		if (this.thisBlock == BlockTypes.STANDARD){
			tempIngot = "Block of "+this.thisBlockMaterial;
		}
		else if (this.thisBlock == BlockTypes.FRAME){
			tempIngot = this.thisBlockMaterial + " Frame Box";
		}
		else if (this.thisBlock == BlockTypes.ORE){
			tempIngot = this.thisBlockMaterial + " Ore";
		}
		else {

			tempIngot = this.getUnlocalizedName().replace("tile.blockGt", "ingot");
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
