package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingRotor implements PartRecipeGenerator {

    public static final ProcessingRotor INSTANCE = new ProcessingRotor();

    private ProcessingRotor() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            ItemStack tPlate = MaterialLibAPI.findStack(material, Shapes.plate, 4);
            ItemStack tRing = MaterialLibAPI.findStack(material, Shapes.ring, 1);
            if (GTUtility.isStackValid(tPlate) && GTUtility.isStackValid(tRing)) {

                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.rotor, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "PhP", "SRf", "PdP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'R', OrePrefixes.ring.ingredient(material), 'S', OrePrefixes.screw.ingredient(material) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy())
                    .circuit(4)
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.rotor, 1))
                    .fluidInputs(SubstituteFluidStack.soldering(1 * NUGGETS))
                    .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 24))
                    .addTo(assemblerRecipes);
            }

            if (MaterialLibAPI.findStack(material, Shapes.ingot, 1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.findStack(material, Shapes.ingot, 5),
                        ItemList.Shape_Extruder_Rotor.get(0L))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.rotor, 1))
                    .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 24))
                    .addTo(extruderRecipes);
            }
            if (MaterialUtils.hasMolten(material)) {
                if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Rotor.get(0L))
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.rotor, 1))
                        .fluidInputs(MaterialUtils.molten(material, 612L))
                        .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 24))
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }
    }
}
