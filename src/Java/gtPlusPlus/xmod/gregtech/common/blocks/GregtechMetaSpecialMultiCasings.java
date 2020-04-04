package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;


public class GregtechMetaSpecialMultiCasings extends GregtechMetaCasingBlocksAbstract {


	public static class SpecialCasingItemBlock extends GregtechMetaCasingItems {

		public SpecialCasingItemBlock(Block par1) {
			super(par1);
		}

		@Override
		public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
			int aMeta = aStack.getItemDamage();
			if (aMeta < 10) {
				//aList.add("Tier: "+GT_Values.VN[aMeta]);
			}
			super.addInformation(aStack, aPlayer, aList, aF3_H);
		}
	}
	
	public GregtechMetaSpecialMultiCasings() {
		super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.1", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			//TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i)); 
			// Don't register these Textures, They already exist within vanilla GT. (May not exist in 5.08)
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Turbine Shaft");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Reinforced Steam Turbine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Reinforced HP Steam Turbine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Reinforced Gas Turbine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Reinforced Plasma Turbine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Tesla Containment Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Structural Solar Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Salt Containment Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Thermally Insulated Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Flotation Cell Casings");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Reinforced Engine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", ""); // Unused

		GregtechItemList.Casing_Turbine_Shaft.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Turbine_LP.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Turbine_HP.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_Turbine_Gas.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_Turbine_Plasma.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_TeslaTower.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_SolarTower_Structural.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_SolarTower_SaltContainment.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_SolarTower_HeatContainment.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_Reinforced_Engine_Casing.set(new ItemStack(this, 1, 9));
	}	

	public IIcon getIcon(int aSide, int aMeta) {

		switch (aMeta) {

		case 0:
			return TexturesGtBlock.Casing_Redox_1.getIcon();
		case 1:
			return Textures.BlockIcons.MACHINE_CASING_TURBINE.getIcon();
		case 2:
			return Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
		case 3:
			return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
		case 4:
			return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
		case 5:
			return TexturesGtBlock.Casing_Material_RedSteel.getIcon();
		case 6:
			return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();	
		case 7:
			return TexturesGtBlock.Casing_Material_Stellite.getIcon();
		case 8:
			return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
		case 9:
			return TexturesGtBlock.TEXTURE_CASING_FLOTATION.getIcon();
		case 10:
			return TexturesGtBlock.Casing_Material_Talonite.getIcon();
			
		}

		return Textures.BlockIcons.RENDERING_ERROR.getIcon();
	}

}
