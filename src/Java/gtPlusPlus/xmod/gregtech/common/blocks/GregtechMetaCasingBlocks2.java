package gtPlusPlus.xmod.gregtech.common.blocks;

import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler2;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GregtechMetaCasingBlocks2
extends GregtechMetaCasingBlocksAbstract {

	public final static int GTID = 73;

	CasingTextureHandler2 TextureHandler = new CasingTextureHandler2();

	public GregtechMetaCasingBlocks2() {
		super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.2", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			Textures.BlockIcons.CASING_BLOCKS[GTID + i] = new GT_CopiedBlockTexture(this, 6, i);
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Structural Glass Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Hastelloy-N Sealant Block");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Hastelloy-X Structural Block");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Incoloy-DS Fluid Containment Block"); //76
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Placeholder Casing"); //IS A PLACEHOLDER DO NOT CHANGE
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Incoloy Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Incoloy Casing"); //65
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Placeholder Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Placeholder Casing ");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Placeholder Casing ");
		GregtechItemList.Casing_StructuralGlass.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Refinery_External.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Refinery_Structural.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_Refinery_Internal.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_PlaceHolder4.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_PlaceHolder5.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_PlaceHolder6.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_PlaceHolder7.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_PlaceHolder8.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_PlaceHolder9.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_PlaceHolder10.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_PlaceHolder11.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_PlaceHolder12.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_PlaceHolder13.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_PlaceHolder14.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_PlaceHolder15.set(new ItemStack(this, 1, 15));
	}

	@Override
	public IIcon getIcon(final int aSide, final int aMeta) { //Texture ID's. case 0 == ID[57]
		return CasingTextureHandler2.getIcon(aSide, aMeta);
	}
}
