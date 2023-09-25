package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidfierRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingRotor implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRotor() {
        OrePrefixes.rotor.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_WORKING)) {
            ItemStack tPlate = GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L);
            ItemStack tRing = GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L);
            if (GT_Utility.isStackValid(tPlate) && GT_Utility.isStackValid(tRing)) {

                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "PhP", "SRf", "PdP", 'P',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                : OrePrefixes.plate.get(aMaterial),
                            'R', OrePrefixes.ring.get(aMaterial), 'S', OrePrefixes.screw.get(aMaterial) });
                }

                GT_Values.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.Tin.getMolten(32))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.Lead.getMolten(48))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(tPlate.copy(), tRing.copy(), GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .fluidInputs(Materials.SolderingAlloy.getMolten(16))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(assemblerRecipes);
            }

            if (GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 5L),
                        ItemList.Shape_Extruder_Rotor.get(0L))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 24))
                    .addTo(extruderRecipes);
            }
            if (aMaterial.mStandardMoltenFluid != null) {
                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {

                    GT_Values.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Rotor.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.rotor, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(612L))
                        .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 24))
                        .addTo(fluidSolidfierRecipes);
                }
            }
        }
    }
}
