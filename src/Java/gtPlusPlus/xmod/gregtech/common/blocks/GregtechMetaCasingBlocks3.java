package gtPlusPlus.xmod.gregtech.common.blocks;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.GregtechMetaTileEntity_PowerSubStationController;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler3;

import java.util.List;

public class GregtechMetaCasingBlocks3
extends GregtechMetaCasingBlocksAbstract {

	CasingTextureHandler3 TextureHandler = new CasingTextureHandler3();

	public static class GregtechMetaCasingItemBlocks3 extends GregtechMetaCasingItems {

		public GregtechMetaCasingItemBlocks3(Block par1) {
			super(par1);
		}

		@Override
		public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
			int meta = aStack.getItemDamage();
			int tier = GregtechMetaTileEntity_PowerSubStationController.getCellTier(field_150939_a, meta);
			if (tier > 0) {
				long capacity = GregtechMetaTileEntity_PowerSubStationController.getCapacityFromCellTier(tier);
				aList.add("Energy Storage: " + GT_Utility.formatNumbers(capacity));
			}
			super.addInformation(aStack, aPlayer, aList, aF3_H);
		}
	}

	public GregtechMetaCasingBlocks3() {
		super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.3", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Aquatic Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Inconel Reinforced Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Multi-Use Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Trinium Plated Mining Platform Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Vanadium Redox Power Cell (IV)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Vanadium Redox Power Cell (LuV)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Vanadium Redox Power Cell (ZPM)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Vanadium Redox Power Cell (UV)");;
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Vanadium Redox Power Cell (MAX)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Placeholder");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Placeholder");
		GregtechItemList.Casing_FishPond.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Extruder.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Multi_Use.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_BedrockMiner.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_Vanadium_Redox_IV.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_Vanadium_Redox_LuV.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_Vanadium_Redox_ZPM.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_Vanadium_Redox_UV.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_Vanadium_Redox_MAX.set(new ItemStack(this, 1, 8));
		//GregtechItemList.Casing_WashPlant.set(new ItemStack(this, 1, 4));
		//GregtechItemList.Casing_Sifter.set(new ItemStack(this, 1, 5));
		//GregtechItemList.Casing_SifterGrate.set(new ItemStack(this, 1, 6));
		//GregtechItemList.Casing_Vanadium_Redox.set(new ItemStack(this, 1, 7));
		//GregtechItemList.Casing_Power_SubStation.set(new ItemStack(this, 1, 8));
		//GregtechItemList.Casing_Cyclotron_Coil.set(new ItemStack(this, 1, 9));
		//GregtechItemList.Casing_Cyclotron_External.set(new ItemStack(this, 1, 10));
		//GregtechItemList.Casing_ThermalContainment.set(new ItemStack(this, 1, 11));
		//GregtechItemList.Casing_Autocrafter.set(new ItemStack(this, 1, 12));
		//GregtechItemList.Casing_CuttingFactoryFrame.set(new ItemStack(this, 1, 13));
		//GregtechItemList.Casing_TeslaTower.set(new ItemStack(this, 1, 14));
		//GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.set(new ItemStack(this, 1, 15)); //Tree Farmer Textures
	}

	@Override
	public IIcon getIcon(final int aSide, final int aMeta) {
		return CasingTextureHandler3.getIcon(aSide, aMeta);
	}
}
