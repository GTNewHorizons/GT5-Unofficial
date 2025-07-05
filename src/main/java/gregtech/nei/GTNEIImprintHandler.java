package gregtech.nei;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.ItemMachines;

public class GTNEIImprintHandler extends ShapelessRecipeHandler {

    @Override
    public String getOverlayIdentifier() {
        return "gt.recipe.cal-imprinting";
    }

    @Override
    public String getRecipeTabName() {
        return getRecipeName();
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(getOverlayIdentifier());
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") || outputId.equals(getOverlayIdentifier())) {
            loadAllRecipes(null);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        ItemStack circuit = getCircuitFromCAL(result);

        if (circuit != null) {
            loadRecipe(null, getImprintForCircuit(circuit));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (isCAL(ingredient) && getCircuitFromCAL(ingredient) == null) {
            loadAllRecipes(ingredient);
        }

        if (getCircuitFromImprint(ingredient) != null) {
            loadRecipe(null, ingredient);
        }
    }

    private void loadAllRecipes(@Nullable ItemStack cal) {
        HashSet<GTUtility.ItemId> addedImprints = new HashSet<>();

        for (GTRecipe recipe : BartWorksRecipeMaps.circuitAssemblyLineRecipes.getAllRecipes()) {
            ItemStack imprint = (ItemStack) recipe.mSpecialItems;
            ItemStack circuit = getCircuitFromImprint(imprint);

            if (imprint == null) continue;
            if (circuit == null) continue;

            if (!addedImprints.add(GTUtility.ItemId.create(circuit))) continue;

            loadRecipe(cal, imprint);
        }
    }

    private void loadRecipe(@Nullable ItemStack cal, @NotNull ItemStack imprint) {
        if (cal == null) cal = ItemRegistry.cal;

        arecipes.add(
            new CachedShapelessRecipe(
                new Object[] { GTUtility.copyAmount(1, cal), GTUtility.copyAmount(1, imprint), },
                installImprint(GTUtility.copyAmount(1, cal), imprint)));
    }

    public static ItemStack installImprint(@NotNull ItemStack cal, @NotNull ItemStack imprint) {
        NBTTagCompound tag = cal.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            cal.setTagCompound(tag);
        }

        tag.setTag(MTECircuitAssemblyLine.IMPRINT_KEY, imprint.getTagCompound());

        return cal;
    }

    public static boolean isCAL(@Nullable ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTECircuitAssemblyLine;
    }

    public static ItemStack getImprintForCircuit(@NotNull ItemStack circuit) {
        ItemStack imprint = new ItemStack(BWMetaItems.getCircuitParts(), 1, 0);

        imprint.setTagCompound(circuit.writeToNBT(new NBTTagCompound()));

        return imprint;
    }

    public static ItemStack getCircuitFromCAL(@NotNull ItemStack cal) {
        if (!isCAL(cal)) return null;

        NBTTagCompound tag = cal.getTagCompound();

        if (tag == null || !tag.hasKey(MTECircuitAssemblyLine.IMPRINT_KEY)) return null;

        return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(MTECircuitAssemblyLine.IMPRINT_KEY));
    }

    public static ItemStack getCircuitFromImprint(@Nullable ItemStack imprint) {
        if (imprint == null) return null;
        if (imprint.getItem() == null) return null;
        if (!(imprint.getItem() instanceof BWMetaItems.BW_GT_MetaGenCircuits)) return null;
        if (imprint.getItemDamage() != 0) return null;
        if (imprint.getTagCompound() == null) return null;

        return ItemStack.loadItemStackFromNBT(imprint.getTagCompound());
    }
}
