package com.github.technus.tectech.loader.recipe;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import com.github.technus.tectech.compatibility.gtpp.GtppAtomLoader;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IItemContainer;

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
            throw new Error(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static IItemContainer getItemContainer(String name) {
        // must never be called before the try catch block is ran
        return (IItemContainer) Enum.valueOf(CUSTOM_ITEM_LIST, name);
    }

    public static Materials getOrDefault(String name, Materials def) {
        Materials mat = Materials.get(name);
        return mat == Materials._NULL || mat == null ? def : mat;
    }

    public void run(EMTransformationRegistry transformationInfo) {
        EMAtomDefinition.setTransformations(transformationInfo);
        EMHadronDefinition.setTransformations(transformationInfo);
        if (GTPlusPlus.isModLoaded()) {
            new GtppAtomLoader().setTransformations(transformationInfo);
        }

        new Assembler().run();
        new AssemblyLine().run();
        new CircuitAssembler().run();
        new Crafting().run();
        new Extractor().run();
        new MachineEMBehaviours();
        new ResearchStationAssemblyLine().run();
    }
}
