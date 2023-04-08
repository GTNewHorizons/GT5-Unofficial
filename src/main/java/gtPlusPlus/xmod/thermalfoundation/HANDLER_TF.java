package gtPlusPlus.xmod.thermalfoundation;

import static gregtech.api.enums.Mods.COFHCore;

import gtPlusPlus.xmod.thermalfoundation.block.TF_Blocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;
import gtPlusPlus.xmod.thermalfoundation.item.TF_Items;
import gtPlusPlus.xmod.thermalfoundation.recipe.TF_Gregtech_Recipes;

public class HANDLER_TF {

    public static void preInit() {
        if (COFHCore.isModLoaded()) {
            TF_Fluids.preInit();
            TF_Items.preInit();
            TF_Blocks.preInit();
        }
    }

    public static void init() {
        if (COFHCore.isModLoaded()) {
            TF_Fluids.init();
            TF_Blocks.init();
            TF_Items.init();
        }
    }

    public static void postInit() {
        if (COFHCore.isModLoaded()) {
            TF_Fluids.postInit();
            TF_Items.postInit();
            TF_Blocks.postInit();
            TF_Gregtech_Recipes.run();
        }
    }
}
