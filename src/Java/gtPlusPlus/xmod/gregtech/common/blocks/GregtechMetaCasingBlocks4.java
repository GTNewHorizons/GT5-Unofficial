package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;


public class GregtechMetaCasingBlocks4
extends GregtechMetaCasingBlocksAbstract {

	public GregtechMetaCasingBlocks4() {
		super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.4", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Naquadah Reactor Base");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Naquadah Reactor Piping");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Naquadah Reactor Containment");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", ""); 
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", " ");
		GregtechItemList.Casing_Naq_Reactor_A.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Naq_Reactor_B.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Naq_Reactor_C.set(new ItemStack(this, 1, 2));
		/*GregtechItemList.Casing_Refinery_Internal.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_WashPlant.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_Sifter.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_SifterGrate.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_Vanadium_Redox.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_Power_SubStation.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_Cyclotron_Coil.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_Cyclotron_External.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_ThermalContainment.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_Autocrafter.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_CuttingFactoryFrame.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_TeslaTower.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.set(new ItemStack(this, 1, 15));*/
	}

	@Override
	public IIcon getIcon(final int aSide, final int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
				//Centrifuge
				case 0:
					return TexturesGtBlock.TEXTURE_ORGANIC_PANEL_A.getIcon();
					//Coke Oven Frame
				case 1:
					return TexturesGtBlock.TEXTURE_TECH_C.getIcon();
					//Coke Oven Casing Tier 1
				case 2:
					return TexturesGtBlock.TEXTURE_METAL_PANEL_E.getIcon();
					//Coke Oven Casing Tier 2
				case 3:
					return TexturesGtBlock.Casing_Material_Fluid_IncoloyDS.getIcon();
					//Material Press Casings
				case 4:
					return TexturesGtBlock.Casing_Material_Grisium.getIcon();				
					//Sifter Structural
				case 5:
					return TexturesGtBlock.Casing_Machine_Metal_Panel_A.getIcon();
					//Sifter Sieve
				case 6:
					return TexturesGtBlock.Casing_Machine_Metal_Grate_A.getIcon();

					//Vanadium Radox Battery
				case 7:
					return TexturesGtBlock.Casing_Redox_1.getIcon();
					//Power Sub-Station Casing
				case 8:
					return TexturesGtBlock.Casing_Machine_Metal_Sheet_A.getIcon();
					//Cyclotron Coil
				case 9:
					return TexturesGtBlock.Overlay_Machine_Cyber_A.getIcon();
					//Cyclotron External Casing
				case 10:
					return Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
					//Multitank Exterior Casing
				case 11:
					return TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
					//Reactor Casing I
				case 12:
					return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
					//Reactor Casing II
				case 13:
					if (aSide <2) {
						return TexturesGtBlock.TEXTURE_TECH_A.getIcon();					
					}
					else {
						return TexturesGtBlock.TEXTURE_TECH_B.getIcon();					
					}
				case 14:
					return TexturesGtBlock.Casing_Material_RedSteel.getIcon();
				case 15:
					if (aSide <2) {
						if (aSide == 1) {
							return TexturesGtBlock.Casing_Machine_Podzol.getIcon();					
						}
						return TexturesGtBlock.Casing_Machine_Acacia_Log.getIcon();							
					}
					else {
						return TexturesGtBlock.Casing_Machine_Farm_Manager.getIcon();					
					}
				default:
					return TexturesGtBlock.Overlay_UU_Matter.getIcon();

			}
		}
		return TexturesGtBlock._PlaceHolder.getIcon();
	}
}
