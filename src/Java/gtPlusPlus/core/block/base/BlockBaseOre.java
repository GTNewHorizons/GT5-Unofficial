package gtPlusPlus.core.block.base;

import java.lang.reflect.Field;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockBaseOre extends BasicBlock implements ITexturedTileEntity {

	private final Material blockMaterial;

	public BlockBaseOre(final Material material, final BlockTypes blockType, final int colour) {
		super(Utils.sanitizeString(material.getUnlocalizedName()), net.minecraft.block.material.Material.rock);
		this.blockMaterial = material;
		this.setHardness(2.0f);
		this.setResistance(6.0F);
		this.setLightLevel(0.0F);
		this.setHarvestLevel("pickaxe", 3);
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setStepSound(soundTypeStone);		
		this.setBlockName("Ore"+Utils.sanitizeString(Utils.sanitizeString(material.getUnlocalizedName())));


		//this.setBlockTextureName(CORE.MODID+":"+blockType.getTexture());
		//this.setBlockName(this.blockMaterial.getLocalizedName()+" Ore");

		try {			
			GameRegistry.registerBlock(this, ItemBlockOre.class, Utils.sanitizeString("ore"+Utils.sanitizeString(this.blockMaterial.getLocalizedName())));
			GT_OreDictUnificator.registerOre("ore"+Utils.sanitizeString(this.blockMaterial.getLocalizedName()), ItemUtils.getSimpleStack(this));
			LanguageRegistry.addName(this, blockMaterial.getLocalizedName()+ " Ore");
		}
		catch (Throwable t){
			t.printStackTrace();
		}
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	public Material getMaterialEx(){
		return this.blockMaterial;
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

	//.08 compat
	IIconContainer[] hiddenTextureArray;
	public ITexture[] getTexture(byte arg0) {
		return getTexture(null, arg0);
	}

	public ITexture[] getTexture(Block block, byte side) {
		if (this.blockMaterial != null){
			GT_RenderedTexture aIconSet = new GT_RenderedTexture(Materials.Iron.mIconSet.mTextures[OrePrefixes.ore.mTextureIndex], this.blockMaterial.getRGBA());
			if (aIconSet != null){
				//Logger.INFO("[Render] Good Overlay.");
				return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), aIconSet};				
			}
		}

		if (hiddenTextureArray == null){
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				hiddenTextureArray = Textures.BlockIcons.GRANITES;
			}
			else {
				try {
					Field o = ReflectionUtils.getField(Textures.BlockIcons.class, "STONES");
					if (o != null){
						hiddenTextureArray = (IIconContainer[]) o.get(Textures.BlockIcons.class);
						if (hiddenTextureArray != null){
							//Found
						}
						else {
							hiddenTextureArray = new IIconContainer[6];
						}
					}
				}
				catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				}
			}
		}

		//return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.STONES[0], new short[]{240, 240, 240, 0})};			
		return new ITexture[]{new GT_RenderedTexture(hiddenTextureArray[0], new short[]{240, 240, 240, 0})};			
	}

	public static class oldOreBlock extends BlockBaseModular{

		@SuppressWarnings("unused")
		private IIcon base;
		@SuppressWarnings("unused")
		private IIcon overlay;

		public oldOreBlock(final String unlocalizedName, final String blockMaterial,  final BlockTypes blockType, final int colour) {
			this(unlocalizedName, blockMaterial, net.minecraft.block.material.Material.iron, blockType, colour, 2);
		}

		public oldOreBlock(final String unlocalizedName, final String blockMaterial, final net.minecraft.block.material.Material vanillaMaterial,  final BlockTypes blockType, final int colour, final int miningLevel) {
			super(unlocalizedName, blockMaterial, vanillaMaterial, blockType, colour, miningLevel);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(final IIconRegister iIcon)
		{
			this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + this.thisBlock.getTexture());
			//this.base = iIcon.registerIcon(CORE.MODID + ":" + "blockStone");
			//this.overlay = iIcon.registerIcon(CORE.MODID + ":" + "blockOre_Overlay");
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

		@Override
		public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
			return false;
		}

	}	

}
