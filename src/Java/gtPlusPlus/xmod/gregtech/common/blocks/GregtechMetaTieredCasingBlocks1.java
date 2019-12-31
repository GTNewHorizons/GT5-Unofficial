package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Material_Casings;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.GregtechMetaTileEntity_PowerSubStationController;


public class GregtechMetaTieredCasingBlocks1 extends GregtechMetaCasingBlocksAbstract {


	private static class TieredCasingItemBlock extends GregtechMetaCasingItems {

		public TieredCasingItemBlock(Block par1) {
			super(par1);
		}

		@Override
		public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
			int aMeta = aStack.getItemDamage();
			if (aMeta < 10) {
				aList.add("Tier: "+GT_Values.VN[aMeta]);
			}
			super.addInformation(aStack, aPlayer, aList, aF3_H);
		}
	}
	
	public GregtechMetaTieredCasingBlocks1() {
		super(TieredCasingItemBlock.class, "gtplusplus.blocktieredcasings.1", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			//TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i)); 
			// Don't register these Textures, Hatches should never need to use their Textures.
		}
		int aIndex = 0;
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Integral Encasement I");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Integral Encasement II");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Integral Encasement III");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Integral Encasement IV");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Integral Encasement V");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Integral Framework I");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Integral Framework II");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Integral Framework III");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Integral Framework IV"); 
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Integral Framework V");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Vacuum Casing");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Turbodyne Casing");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "");
		//GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", " ");
		
		GregtechItemList.GTPP_Casing_ULV.set(new ItemStack(this, 1, 0));
		GregtechItemList.GTPP_Casing_LV.set(new ItemStack(this, 1, 1));
		GregtechItemList.GTPP_Casing_MV.set(new ItemStack(this, 1, 2));
		GregtechItemList.GTPP_Casing_HV.set(new ItemStack(this, 1, 3));
		GregtechItemList.GTPP_Casing_EV.set(new ItemStack(this, 1, 4));
		GregtechItemList.GTPP_Casing_IV.set(new ItemStack(this, 1, 5));
		GregtechItemList.GTPP_Casing_LuV.set(new ItemStack(this, 1, 6));
		GregtechItemList.GTPP_Casing_ZPM.set(new ItemStack(this, 1, 7));
		GregtechItemList.GTPP_Casing_UV.set(new ItemStack(this, 1, 8));
		GregtechItemList.GTPP_Casing_MAX.set(new ItemStack(this, 1, 9));
		
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 10));
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 11));
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 12));
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 13));
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 14));
		//GregtechItemList.Casing_LV.set(new ItemStack(this, 1, 15));
	}	

	public IIcon getIcon(int aSide, int aMeta) {
		if (aMeta < 10) {
			return TexturesGtBlock.TIERED_MACHINE_HULLS[aMeta].getIcon();
		}
		switch (aMeta) {			
			case 10:
				return Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
			case 11:
				return Textures.BlockIcons.MACHINE_HEATPROOFCASING.getIcon();
			case 12:
				return Textures.BlockIcons.RENDERING_ERROR.getIcon();
			case 13:
				return Textures.BlockIcons.RENDERING_ERROR.getIcon();
			case 14:
				return Textures.BlockIcons.RENDERING_ERROR.getIcon();
			case 15:
				return Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR.getIcon();
		}		
		return TexturesGtBlock.TEXTURE_CASING_TIERED_ULV.getIcon();
	}

}
