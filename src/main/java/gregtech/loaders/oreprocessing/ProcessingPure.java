package gregtech.loaders.oreprocessing;

import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingPure implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingPure INSTANCE;

    public ProcessingPure() {
        INSTANCE = this;
        OrePrefixes.crushedPurified.add(this);
        OrePrefixes.cleanGravel.add(this);
        OrePrefixes.reduced.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        if (ProcessingOreMachine.owns(material)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convert(
                    material,
                    GTOreDictUnificator.get(OrePrefixes.dustPure, MaterialUtils.macerateInto(material), 1L)))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convertOre(
                    material,
                    GTOreDictUnificator.get(
                        OrePrefixes.dustPure,
                        MaterialUtils.macerateInto(material),
                        GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L),
                        1L),
                    GTOreDictUnificator.get(
                        OrePrefixes.dust,
                        GTUtility.selectItemInList(
                            1,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        1L)))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);
    }
}
