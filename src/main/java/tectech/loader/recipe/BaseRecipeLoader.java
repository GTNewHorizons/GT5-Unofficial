package tectech.loader.recipe;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IItemContainer;
import tectech.TecTech;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class BaseRecipeLoader {

    @SuppressWarnings("rawtypes")
    private static Class CUSTOM_ITEM_LIST;

    static {
        try {
            CUSTOM_ITEM_LIST = Class.forName("com.dreammaster.gthandler.CustomItemList");
        } catch (Exception e) {
            TecTech.LOGGER.error("NHCoreMod not present. Disabling all the recipes");
        }
    }

    @SuppressWarnings("unchecked")
    public static IItemContainer getItemContainer(String name) {
        // must never be called before the try catch block is ran
        return (IItemContainer) Enum.valueOf(CUSTOM_ITEM_LIST, name);
    }

    public static Materials getOrDefault(String name, Materials def) {
        Materials mat = Materials.get(name);
        return mat == Materials._NULL ? def : mat;
    }

    public void run() {
        // todo: Move those recipes in NHCore
        if (NewHorizonsCoreMod.isModLoaded()) {
            new Assembler().run();
            new AssemblyLine().run();
            new CircuitAssembler().run();
            new Crafting().run();
            new Extractor().run();
            new ResearchStationAssemblyLine().run();
            new Godforge().run();
        } else {
            Godforge.runDevEnvironmentRecipes();
        }
        Godforge.addFakeUpgradeCostRecipes();
    }
}
