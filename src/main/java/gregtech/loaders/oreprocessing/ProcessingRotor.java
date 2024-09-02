package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingRotor implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRotor() {
        OrePrefixes.rotor.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_WORKING)) {
            ItemStack tPlate = GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L);
            ItemStack tRing = GTOreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L);
            if (GTUtility.isStackValid(tPlate) && GTUtility.isStackValid(tRing)) {

                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L),
                        GTProxy.tBits,
                        new Object[] { "PhP", "SRf", "PdP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'R', OrePrefixes.ring.get(aMaterial), 'S', OrePrefixes.screw.get(aMaterial) });
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GTUtility.getIntegratedCircuit(4))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.Tin.getMolten(32))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GTUtility.getIntegratedCircuit(4))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.Lead.getMolten(48))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GTUtility.getIntegratedCircuit(4))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.SolderingAlloy.getMolten(16))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);
            }

            if (GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 5L),
                        ItemList.Shape_Extruder_Rotor.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(extruderRecipes);
            }
            if (aMaterial.mStandardMoltenFluid != null) {
                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Rotor.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(612L))
                        .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 24))
                        .addTo(fluidSolidifierRecipes);
                }
            }
        }
    }
}
