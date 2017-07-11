package gtPlusPlus.xmod.gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class GregtechMetaCasingBlocks
extends GregtechMetaCasingBlocksAbstract {

	CasingTextureHandler TextureHandler = new CasingTextureHandler();

	public GregtechMetaCasingBlocks() {
		super(GregtechMetaCasingItems.class, "miscutils.blockcasings", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Centrifuge Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Structural Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Heat Resistant Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Heat Proof Coke Oven Casing"); //60
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Material Press Machine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Electrolyzer Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Wire Factory Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Maceration Stack Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Matter Generation Coil"); //65
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Matter Fabricator Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Iron Plated Bricks");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Multitank Exterior Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Hastelloy-N Reactor Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Zeron-100 Reactor Shielding");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Blast Smelter Heat Containment Coil ");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Blast Smelter Casing Block");
		GregtechItemList.Casing_Centrifuge1.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_CokeOven.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_CokeOven_Coil1.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_CokeOven_Coil2.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_MaterialPress.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_Electrolyzer.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_WireFactory.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_MacerationStack.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_MatterGen.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_MatterFab.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_IronPlatedBricks.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_MultitankExterior.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_Reactor_I.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_Reactor_II.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_Coil_BlastSmelter.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_BlastSmelter.set(new ItemStack(this, 1, 15));
	}

	@Override
	public IIcon getIcon(final int aSide, final int aMeta) { //Texture ID's. case 0 == ID[57]
		return CasingTextureHandler.getIcon(aSide, aMeta);
	}



	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide) {
		final GregtechMetaCasingBlocks i = this;
		return CasingTextureHandler.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, aSide, i);
	}


}
