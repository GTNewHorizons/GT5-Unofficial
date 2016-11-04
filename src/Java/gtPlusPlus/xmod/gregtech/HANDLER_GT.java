package gtPlusPlus.xmod.gregtech;

import gregtech.api.util.GT_Config;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.*;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;

public class HANDLER_GT {

	public static GT_Config mMaterialProperties = null;

	@SuppressWarnings("unused")
	public static void init() {

		// Load General Blocks and set up some Basic Meta Tile Entitie states
		Gregtech_Blocks.run();

		// Register Tile Entities
		COMPAT_HANDLER.registerGregtechMachines();

		// Add Custom Pipes, Wires and Cables.
		GregtechConduits.run();
		new MetaGeneratedGregtechTools();
		new ProcessingToolHeadChoocher().run();
		RecipeGen_BlastSmelterGT.generateRecipes();

	}

	public static void postInit() {

	}

	@SuppressWarnings("unused")
	public static void preInit() {
		new MetaGeneratedGregtechItems();
		if (HANDLER_GT.mMaterialProperties != null) {
			GT_Materials.init(HANDLER_GT.mMaterialProperties);
		}
		GregtechFluidHandler.run();
	}

}
