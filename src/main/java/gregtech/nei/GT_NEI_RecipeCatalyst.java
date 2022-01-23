package gregtech.nei;

import codechicken.nei.api.API;
import codechicken.nei.recipe.IRecipeHandler;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.*;

public class GT_NEI_RecipeCatalyst {
    private static final boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");

    public static void registerManual() {
        // IC2
        registerIC2Catalyst(Arrays.asList("ironFurnace", "electroFurnace", "inductionFurnace"), "smelting");
        registerIC2Catalyst("blastfurnace", "blastfurnace");
        registerIC2Catalyst("blockcutter", "BlockCutter");
        registerIC2Catalyst("centrifuge", "centrifuge");
        registerIC2Catalyst("compressor", "compressor");
        registerIC2Catalyst("extractor", "extractor");
        registerIC2Catalyst("canner", "fluidcanner");
        registerIC2Catalyst("macerator", "macerator");
        registerIC2Catalyst("metalformer", "metalformer");
        registerIC2Catalyst("orewashingplant", "oreWashing");
        registerIC2Catalyst("solidcanner", "solidcanner");
    }

    public static void registerGTCatalyst(ItemStack stack, String handlerID) {
        if (!isNEILoaded) return;
        if (handlerID == null || handlerID.isEmpty()) return;
        API.addRecipeCatalyst(stack, handlerID);
    }

    private static void registerIC2Catalyst(String stackName, String handlerID) {
        registerIC2Catalyst(Collections.singletonList(stackName), handlerID);
    }

    private static void registerIC2Catalyst(List<String> stackNames, String handlerID) {
        if (!isNEILoaded) return;
        if (handlerID == null || handlerID.isEmpty()) return;
        stackNames.forEach(s -> API.addRecipeCatalyst(GT_ModHandler.getIC2Item(s, 1), handlerID));
    }
}
