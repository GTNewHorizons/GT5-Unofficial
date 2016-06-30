package miscutil.core.xmod.gregtech.loaders;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.core.xmod.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import miscutil.core.xmod.gregtech.api.util.GregtechOreDictUnificator;
import net.minecraft.item.ItemStack;

public class Processing_Block implements Interface_OreRecipeRegistrator {

	private boolean isGem = false;

	public Processing_Block() {
		GregtechOrePrefixes.block.add(this);
	}

	@Override
	public void registerOre(GregtechOrePrefixes aPrefix, GT_Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
		Utils.LOG_INFO("Processing Blocks");
		GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GregtechOreDictUnificator.get(GregtechOrePrefixes.plate, aMaterial, 9L), null, (int) Math.max(aMaterial.getMass() * 10L, 1L), 30);

		ItemStack tStack1 = GregtechOreDictUnificator.get(GregtechOrePrefixes.ingot, aMaterial, 1L);
		ItemStack tStack2 = GregtechOreDictUnificator.get(GregtechOrePrefixes.gem, aMaterial, 1L);
		ItemStack tStack3 = GregtechOreDictUnificator.get(GregtechOrePrefixes.dust, aMaterial, 1L);

		GT_ModHandler.removeRecipe(new ItemStack[]{GT_Utility.copyAmount(1L, new Object[]{aStack})});

		if (tStack1 != null) {
			GT_ModHandler.removeRecipe(new ItemStack[]{tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1, tStack1});
		}
		if (tStack2 != null) {
			GT_ModHandler.removeRecipe(new ItemStack[]{tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2, tStack2});
		}
		if (tStack3 != null) {
			GT_ModHandler.removeRecipe(new ItemStack[]{tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3, tStack3});
		}
		if (aMaterial.mStandardMoltenFluid != null) {
			GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), aMaterial.getMolten(1296L), GregtechOreDictUnificator.get(GregtechOrePrefixes.block, aMaterial, 1L), 288, 8);
		}
		if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.storageblockcrafting, GregtechOrePrefixes.block.get(aMaterial).toString(), false)) {
			if ((tStack1 == null) && (tStack2 == null) && (tStack3 != null)){
				GT_ModHandler.addCraftingRecipe(GregtechOreDictUnificator.get(GregtechOrePrefixes.block, aMaterial, 1L), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), GregtechOrePrefixes.dust.get(aMaterial)});
			}
			if (tStack2 != null){ 
				GT_ModHandler.addCraftingRecipe(GregtechOreDictUnificator.get(GregtechOrePrefixes.block, aMaterial, 1L), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), GregtechOrePrefixes.gem.get(aMaterial)});
			}
			if (tStack1 != null) {
				GT_ModHandler.addCraftingRecipe(GregtechOreDictUnificator.get(GregtechOrePrefixes.block, aMaterial, 1L), new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), GregtechOrePrefixes.ingot.get(aMaterial)});
			}
		}

		if (aMaterial.contains(SubTag.CRYSTAL) && !aMaterial.contains(SubTag.METAL)){
			isGem = true;
		}
		else {
			isGem = false;
		}
		//Ingot
		if (tStack1 != null) {
			tStack1.stackSize = 9;
		}
		//Gem
		if (tStack2 != null && !isGem) {
			tStack2.stackSize = 9;
		}
		else if (tStack2 != null && isGem) {
			tStack2.stackSize = 0;
		}
		//Dust
		if (tStack3 != null && !isGem) {
			tStack3.stackSize = 9;
		}
		else if (tStack3 != null && isGem) {
			tStack3.stackSize = 0;
		}

		//Gems in FORGE HAMMER
		if (tStack2 != null && !isGem) {
			tStack2.stackSize = 9;
			GT_Values.RA.addForgeHammerRecipe(aStack, tStack2, 100, 24);
		}
		else if (tStack2 != null && isGem) {
			tStack2.stackSize = 9;
			GT_Values.RA.addForgeHammerRecipe(aStack, tStack2, 100, 24);
			tStack2.stackSize = 0;
		}


		if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.storageblockdecrafting, GregtechOrePrefixes.block.get(aMaterial).toString(), tStack2 != null)) {
			if (tStack3 != null)
				GT_ModHandler.addShapelessCraftingRecipe(tStack3, new Object[]{GregtechOrePrefixes.block.get(aMaterial)});
			if (tStack2 != null)
				GT_ModHandler.addShapelessCraftingRecipe(tStack2, new Object[]{GregtechOrePrefixes.block.get(aMaterial)});
			if (tStack1 != null) {
				GT_ModHandler.addShapelessCraftingRecipe(tStack1, new Object[]{GregtechOrePrefixes.block.get(aMaterial)});
			}
		}
	}
}
