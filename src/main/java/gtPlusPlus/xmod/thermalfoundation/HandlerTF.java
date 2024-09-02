package gtPlusPlus.xmod.thermalfoundation;

import static gregtech.api.enums.Mods.COFHCore;

import gtPlusPlus.xmod.thermalfoundation.block.TFBlocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtPlusPlus.xmod.thermalfoundation.item.TFItems;
import gtPlusPlus.xmod.thermalfoundation.recipe.TFGregtechRecipes;

public class HandlerTF {

    public static void preInit() {
        if (COFHCore.isModLoaded()) {
            TFFluids.preInit();
            TFItems.preInit();
            TFBlocks.preInit();
        }
    }

    public static void init() {
        if (COFHCore.isModLoaded()) {
            TFFluids.init();
            TFBlocks.init();
            TFItems.init();
        }
    }

    public static void postInit() {
        if (COFHCore.isModLoaded()) {
            TFFluids.postInit();
            TFItems.postInit();
            TFBlocks.postInit();
            TFGregtechRecipes.run();
        }
    }
}
