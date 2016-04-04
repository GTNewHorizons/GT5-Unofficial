package miscutil.gregtech.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Item_Casings1;
import gregtech.common.blocks.GT_Material_Casings;
import miscutil.gregtech.enums.GregtechItemList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class GregtechMetaCasingBlocks
extends GT_Block_Casings_Abstract {
	public GregtechMetaCasingBlocks() {
		super(GT_Item_Casings1.class, "miscutils.blockcasings", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			Textures.BlockIcons.CASING_BLOCKS[i] = new GT_CopiedBlockTexture(this, 6, i);
		}
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "UNUSED");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "LV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "MV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "HV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "EV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "IV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "LuV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "ZPM Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "UV Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "MAX Machine Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Iron Plated Bricks");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Heat Proof Machine Casing");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Cupronickel Coil Block");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Kanthal Coil Block");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Nichrome Coil Block");
		//GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Superconducting Coil Block");
		GregtechItemList.Casing_Shielding.set(new ItemStack(this, 1, 0));
		/* ItemList.Casing_LV.set(new ItemStack(this, 1, 1));
        ItemList.Casing_MV.set(new ItemStack(this, 1, 2));
        ItemList.Casing_HV.set(new ItemStack(this, 1, 3));
        ItemList.Casing_EV.set(new ItemStack(this, 1, 4));
        ItemList.Casing_IV.set(new ItemStack(this, 1, 5));
        ItemList.Casing_LuV.set(new ItemStack(this, 1, 6));
        ItemList.Casing_ZPM.set(new ItemStack(this, 1, 7));
        ItemList.Casing_UV.set(new ItemStack(this, 1, 8));
        ItemList.Casing_MAX.set(new ItemStack(this, 1, 9));*/
		GregtechItemList.Casing_IronPlatedBricks.set(new ItemStack(this, 1, 10));
       /*ItemList.Casing_HeatProof.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Coil_Cupronickel.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Coil_Kanthal.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Coil_Nichrome.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Coil_Superconductor.set(new ItemStack(this, 1, 15));*/
	}

	public IIcon getIcon(int aSide, int aMeta) {
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			case 1:
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
			case 10:
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
			case 11:
				return Textures.BlockIcons.MACHINE_HEATPROOFCASING.getIcon();
			case 12:
				return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
			case 13:
				return Textures.BlockIcons.MACHINE_COIL_KANTHAL.getIcon();
			case 14:
				return Textures.BlockIcons.MACHINE_COIL_NICHROME.getIcon();
			case 15:
				return Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR.getIcon();
			}
			if (aSide == 0) {
				return Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
			}
			if (aSide == 1) {
				return Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
			}
			return Textures.BlockIcons.MACHINECASINGS_SIDE[aMeta].getIcon();
		}
		return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
	}

	public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
		return aWorld.getBlockMetadata(aX, aY, aZ) > 9 ? super.colorMultiplier(aWorld, aX, aY, aZ) : gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[0] << 16 | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[1] << 8 | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[2];
	}
}
