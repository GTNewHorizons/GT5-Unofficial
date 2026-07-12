package kekztech.common.recipeLoaders;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // Ceria Dust
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Cerium, Materials2Shapes.dust, (int) (1)))
            .circuit(6)
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

    }
}
