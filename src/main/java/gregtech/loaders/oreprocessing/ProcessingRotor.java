package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.objects.SubstituteFluidStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingRotor implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRotor INSTANCE;

    public ProcessingRotor() {
        INSTANCE = this;
        OrePrefixes.rotor.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if ((material.mUnifiable) && (material.mMaterialInto == material)
            && !MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            ItemStack tPlate = GTOreDictUnificator.get(OrePrefixes.plate, material, 4L);
            ItemStack tRing = GTOreDictUnificator.get(OrePrefixes.ring, material, 1L);
            if (GTUtility.isStackValid(tPlate) && GTUtility.isStackValid(tRing)) {

                if (material.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.rotor, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "PhP", "SRf", "PdP", 'P',
                            material == Materials.Wood ? OrePrefixes.plank.ingredient(material)
                                : OrePrefixes.plate.ingredient(material),
                            'R', OrePrefixes.ring.ingredient(material), 'S', OrePrefixes.screw.ingredient(material) });
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy())
                    .circuit(4)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, material, 1L))
                    .fluidInputs(SubstituteFluidStack.soldering(1 * NUGGETS))
                    .duration(((int) Math.max(material.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 24))
                    .addTo(assemblerRecipes);
            }

            if (GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.ingot, material, 5L),
                        ItemList.Shape_Extruder_Rotor.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, material, 1L))
                    .duration(((int) Math.max(material.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 24))
                    .addTo(extruderRecipes);
            }
            if (material.mStandardMoltenFluid != null) {
                if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Rotor.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, material, 1L))
                        .fluidInputs(material.getMolten(612L))
                        .duration(((int) Math.max(material.getMass(), 1L)) * TICKS)
                        .eut(calculateRecipeEU(material, 24))
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }
    }
}
