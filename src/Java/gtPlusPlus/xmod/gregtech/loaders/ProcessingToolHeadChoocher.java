package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import net.minecraft.item.ItemStack;

public class ProcessingToolHeadChoocher implements Interface_OreRecipeRegistrator, Runnable {
	public ProcessingToolHeadChoocher() {
		GregtechOrePrefixes.toolSkookumChoocher.add(this);
	}

	@Override
	public void registerOre(final GregtechOrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName, final String aModName, final ItemStack aStack) {
		if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
			if (aMaterial != Materials.Rubber) {
				if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY)) && (!aMaterial.contains(SubTag.NO_SMASHING))) {
					GT_ModHandler.addCraftingRecipe(MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(16, 1, aMaterial, aMaterial, null), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"IhI", "III", " I ", Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial)});
				}
			}
		}
	}

	@Override
	public void registerOre(final GregtechOrePrefixes aPrefix,
			final GT_Materials aMaterial, final String aOreDictName, final String aModName,
			final ItemStack aStack) {
		// TODO Auto-generated method stub

	}

	public void materialsLoops(){
		final Materials[] i = Materials.values();
		final int size = i.length;
		Utils.LOG_WARNING("Materials to attempt tool gen. with: "+size);
		int used = 0;
		Materials aMaterial = null;
		for (int r=0;r<size;r++){
			aMaterial = i[r];
			if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint) && (aMaterial != Materials.Rubber) && (aMaterial != Materials._NULL)) {
				if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY)) && (!aMaterial.contains(SubTag.NO_SMASHING))&& (!aMaterial.contains(SubTag.TRANSPARENT))&& (!aMaterial.contains(SubTag.FLAMMABLE))&& (!aMaterial.contains(SubTag.MAGICAL))&& (!aMaterial.contains(SubTag.NO_SMELTING))) {
					Utils.LOG_WARNING("Found "+aMaterial.name()+" as a valid Skookum Choocher Material.");
					//Input 1
					final ItemStack plate = GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L);
					final ItemStack ingot = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L);
					final ItemStack screw = GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L);
					final ItemStack longrod = GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L);
					final ItemStack hammerhead = GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L);

					if ((null != plate) && (null != ingot) && (null != hammerhead) && (null != longrod) && (null != screw)){
						RecipeUtils.recipeBuilder(
								hammerhead, ToolDictNames.craftingToolScrewdriver.name(), plate,
								ingot, plate, plate,
								longrod, screw, null,
								MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(MetaGeneratedGregtechTools.SKOOKUM_CHOOCHER, 1, aMaterial, null, null));
						used++;
					}
					else {
						Utils.LOG_WARNING(""+aMaterial.name()+" could not be used for all input compoenents. [3x"+aMaterial.name()+" plates, 2x"+aMaterial.name()+" ingots, 1x"+aMaterial.name()+" Hard Hammer Head.");
					}
					//GT_ModHandler.addCraftingRecipe(, GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"P H", "PIP", " I ", Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial), Character.valueOf('P'), OrePrefixes.plate.get(aMaterial), Character.valueOf('H'), OrePrefixes.toolHeadHammer.get(aMaterial)});
				}
				else {
					Utils.LOG_WARNING(""+aMaterial.name()+" was not a valid Skookum Choocher Material.");
				}
			}
			else {
				Utils.LOG_WARNING(""+aMaterial.name()+" was not a valid Skookum Choocher Material.");
			}


		}

		Utils.LOG_INFO("Materials used for tool gen: "+used);
	}

	@Override
	public void run() {
		Utils.LOG_INFO("Generating Skookum Choochers of all GT Materials.");
		this.materialsLoops();
	}


}