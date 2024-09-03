package gregtech.api.recipe.maps;

import static gregtech.api.enums.GTValues.W;
import static gregtech.api.util.GTRecipeConstants.EXPLODE;
import static gregtech.api.util.GTRecipeConstants.ON_FIRE;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Microwave Recipe handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MicrowaveBackend extends NonGTBackend {

    public MicrowaveBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        if (items.length == 0 || items[0] == null) {
            return null;
        }
        if (cachedRecipe != null && cachedRecipe.isRecipeInputEqual(false, true, fluids, items)) {
            return cachedRecipe;
        }

        ItemStack output = GTModHandler.getSmeltingOutput(items[0], false, null);

        if (GTUtility.areStacksEqual(items[0], new ItemStack(Items.book, 1, W))) {
            return GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, items[0]))
                .itemOutputs(GTUtility.getWrittenBook("Manual_Microwave", ItemList.Book_Written_03.get(1)))
                .duration(32)
                .eut(4)
                .noOptimize()
                .build()
                .orElse(null);
        }

        // Check Container Item of Input since it is around the Input, then the Input itself, then Container Item of
        // Output and last check the Output itself
        for (ItemStack item : new ItemStack[] { GTUtility.getContainerItem(items[0], true), items[0],
            GTUtility.getContainerItem(output, true), output }) {
            if (item == null) continue;
            if (GTUtility.areStacksEqual(item, new ItemStack(Blocks.netherrack, 1, W), true)
                || GTUtility.areStacksEqual(item, new ItemStack(Blocks.tnt, 1, W), true)
                || GTUtility.areStacksEqual(item, new ItemStack(Items.egg, 1, W), true)
                || GTUtility.areStacksEqual(item, new ItemStack(Items.firework_charge, 1, W), true)
                || GTUtility.areStacksEqual(item, new ItemStack(Items.fireworks, 1, W), true)
                || GTUtility.areStacksEqual(item, new ItemStack(Items.fire_charge, 1, W), true)) {
                GTLog.exp.println("Microwave Explosion due to TNT || EGG || FIREWORKCHARGE || FIREWORK || FIRE CHARGE");
                return GTRecipeBuilder.empty()
                    .metadata(EXPLODE, true)
                    .build()
                    .orElse(null);
            }

            ItemData itemData = GTOreDictUnificator.getItemData(item);
            if (itemData != null) {
                if (itemData.mMaterial != null && itemData.mMaterial.mMaterial != null) {
                    if (itemData.mMaterial.mMaterial.contains(SubTag.METAL)
                        || itemData.mMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
                        GTLog.exp.println("Microwave Explosion due to METAL insertion");
                        return GTRecipeBuilder.empty()
                            .metadata(EXPLODE, true)
                            .build()
                            .orElse(null);
                    }
                    if (itemData.mMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
                        GTLog.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                        return GTRecipeBuilder.empty()
                            .metadata(ON_FIRE, true)
                            .build()
                            .orElse(null);
                    }
                }
                for (MaterialStack materialStack : itemData.mByProducts) {
                    if (materialStack == null) continue;
                    if (materialStack.mMaterial.contains(SubTag.METAL)
                        || materialStack.mMaterial.contains(SubTag.EXPLOSIVE)) {
                        GTLog.exp.println("Microwave Explosion due to METAL insertion");
                        return GTRecipeBuilder.empty()
                            .metadata(EXPLODE, true)
                            .build()
                            .orElse(null);
                    }
                    if (materialStack.mMaterial.contains(SubTag.FLAMMABLE)) {
                        GTLog.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                        return GTRecipeBuilder.empty()
                            .metadata(ON_FIRE, true)
                            .build()
                            .orElse(null);
                    }
                }
            }
            if (TileEntityFurnace.getItemBurnTime(item) > 0) {
                GTLog.exp.println("Microwave INFLAMMATION due to BURNABLE insertion");
                return GTRecipeBuilder.empty()
                    .metadata(ON_FIRE, true)
                    .build()
                    .orElse(null);
            }
        }

        return output == null ? null
            : GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, items[0]))
                .itemOutputs(output)
                .duration(32)
                .eut(4)
                .noOptimize()
                .build()
                .orElse(null);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return GTModHandler.getSmeltingOutput(item, false, null) != null;
    }
}
