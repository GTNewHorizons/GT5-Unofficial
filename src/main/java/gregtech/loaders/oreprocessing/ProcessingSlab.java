package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.util.GTUtility;

public class ProcessingSlab implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSlab() {
        OrePrefixes.slab.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (oreDictName.startsWith("slabWood")) {
            if (Railcraft.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(3, stack))
                    .itemOutputs(ItemList.RC_Tie_Wood.get(3L))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Creosote,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (300)))
                    .duration(10 * SECONDS)
                    .eut(4)
                    .addTo(chemicalBathRecipes);
            }
        }
    }
}
