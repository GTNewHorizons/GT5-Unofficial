package gtPlusPlus.xmod.gregtech.api.recipe;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import net.minecraft.item.ItemStack;

public class ProcessingSkookumChoocherToolRecipes implements IOreRecipeRegistrator {
	public ProcessingSkookumChoocherToolRecipes() {
		// GregtechOrePrefixes.toolSkookumChoocher.add(this);
	}

	@Override
	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
			final String aModName, final ItemStack aStack) {
		GT_ModHandler.addShapelessCraftingRecipe(
				MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(7734, 1, aMaterial, aMaterial, null),
				new Object[] {
						aOreDictName, OrePrefixes.stick.get(aMaterial), OrePrefixes.screw.get(aMaterial),
						ToolDictNames.craftingToolScrewdriver
				});
	}
}
