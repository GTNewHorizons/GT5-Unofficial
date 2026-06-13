package tectech.loader.recipe;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.loaders.postload.BiodomeCalibrationLoader;
import gregtech.loaders.postload.FoundryFakeModuleCostLoader;
import tectech.TecTech;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class BaseRecipeLoader {

    @SuppressWarnings("rawtypes")
    private static final Class NHCOREMOD_ITEM_LIST;
    private static final MethodHandle NHCOREMOD_GETTER;

    static {
        Class<?> itemList;
        MethodHandle getterHandle;

        try {
            itemList = Class.forName("com.dreammaster.item.NHItemList");
            getterHandle = MethodHandles.publicLookup()
                .findVirtual(itemList, "get", MethodType.methodType(ItemStack.class, int.class));
        } catch (Exception e) {
            TecTech.LOGGER.error("NHCoreMod not present. Disabling all the recipes");
            itemList = null;
            getterHandle = null;
        }

        NHCOREMOD_ITEM_LIST = itemList;
        NHCOREMOD_GETTER = getterHandle;
    }

    @SuppressWarnings("unchecked")
    public static ItemStack getNHCoreModItem(String name, int count) {
        // must never be called before the try catch block is ran
        try {
            return (ItemStack) NHCOREMOD_GETTER.invoke(Enum.valueOf(NHCOREMOD_ITEM_LIST, name), count);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
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
        FoundryFakeModuleCostLoader.load();
        BiodomeCalibrationLoader.load();
    }
}
