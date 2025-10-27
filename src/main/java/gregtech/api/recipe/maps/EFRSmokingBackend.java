package gregtech.api.recipe.maps;

import static gregtech.api.enums.Mods.EtFuturumRequiem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for EFR Blasting Recipe handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EFRSmokingBackend extends NonGTBackend {

    public EFRSmokingBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        if (items.length == 0 || items[0] == null || !EtFuturumRequiem.isModLoaded()) {
            return null;
        }
        if (cachedRecipe != null && cachedRecipe.isRecipeInputEqual(false, true, fluids, items)) {
            return cachedRecipe;
        }
        ItemStack output = GTModHandler.getEFRSmokingOutput(items[0], false, null);
        return output == null ? null
            : GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, items[0]))
                .itemOutputs(output)
                .duration(64)
                .eut(4)
                .build()
                .orElse(null);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return GTModHandler.getSmeltingOutput(item, false, null) != null;
    }
}
