package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;


public class GregtechMetaCasingBlocks4
extends GregtechMetaCasingBlocksAbstract {

	public GregtechMetaCasingBlocks4() {
		super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.4", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Naquadah Reactor Base"); //48
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Reactor Piping");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Naquadah Containment Chamber");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Tempered Arc Furnace Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Structural Solar Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Salt Containment Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Thermal Containment Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Turbine Shaft");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Low Pressure Turbine Casing"); 
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "High Pressure Turbine Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Vacuum Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Turbodyne Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", " ");
		GregtechItemList.Casing_Naq_Reactor_A.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Naq_Reactor_B.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Naq_Reactor_C.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_Industrial_Arc_Furnace.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_SolarTower_Structural.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_SolarTower_SaltContainment.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_SolarTower_HeatContainment.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_Turbine_Shaft.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_Turbine_LP.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_Turbine_HP.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_Vacuum_Furnace.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_RocketEngine.set(new ItemStack(this, 1, 11));
		/*GregtechItemList.Casing_Autocrafter.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_CuttingFactoryFrame.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_TeslaTower.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.set(new ItemStack(this, 1, 15));*/
	}	

	private static final LargeTurbineTextureHandler mTurbineTextures = new LargeTurbineTextureHandler();
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide) {
		final GregtechMetaCasingBlocks4 i = this;
		return mTurbineTextures.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, aSide, i);
	}
	
	@Override
	public IIcon getIcon(final int aSide, final int aMeta) {
		return getStaticIcon((byte) aSide, (byte) aMeta);
	}
	
	public static IIcon getStaticIcon(final byte aSide, final byte aMeta) {
		 //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
				//Centrifuge
				case 0:
					return TexturesGtBlock.Casing_Trinium_Titanium.getIcon();
					//Coke Oven Frame
				case 1:
					return TexturesGtBlock.TEXTURE_TECH_C.getIcon();
					//Coke Oven Casing Tier 1
				case 2:
					return TexturesGtBlock.TEXTURE_ORGANIC_PANEL_A_GLOWING.getIcon();
					//Coke Oven Casing Tier 2
				case 3:					
					return TexturesGtBlock.TEXTURE_METAL_PANEL_A.getIcon();
					//Material Press Casings
				case 4:
					return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();				
					//Sifter Structural
				case 5:
					return TexturesGtBlock.Casing_Material_Stellite.getIcon();
					//Sifter Sieve
				case 6:
					return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();

					
					//Vanadium Radox Battery
				case 7:
					return TexturesGtBlock.Casing_Redox_1.getIcon();
					//Power Sub-Station Casing
				case 8:
					return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
					//Cyclotron Coil
				case 9:
					return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
					
					
					//Cyclotron External Casing
				case 10:
					
					if (aSide <2) {
						return TexturesGtBlock.TEXTURE_STONE_RED_B.getIcon();					
					}
					else {
						return TexturesGtBlock.TEXTURE_STONE_RED_A.getIcon();					
					}
					
					//Multitank Exterior Casing
				case 11:
					return TexturesGtBlock.TEXTURE_CASING_ROCKETDYNE.getIcon();
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
				default:
					return TexturesGtBlock.Overlay_UU_Matter.getIcon();

			}
		}
		return TexturesGtBlock._PlaceHolder.getIcon();
	}
}
