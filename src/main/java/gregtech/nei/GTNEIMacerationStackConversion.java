package gregtech.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;

import codechicken.nei.recipe.ShapelessRecipeHandler;
import gregtech.api.enums.ItemList;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.machines.multi.MTEIndustrialMacerator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMaceratorLegacy;

// TODO: Remove in 2.10
public class GTNEIMacerationStackConversion extends ShapelessRecipeHandler {

    @Override
    public String getOverlayIdentifier() {
        return "gt.recipe.macerator.conversion";
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
            loadAllRecipes();
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("crafting") || inputId.equals(getOverlayIdentifier())) {
            loadAllRecipes();
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!isNewMacerationStack(result)) return;
        loadRecipeForTier(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!isOldMacerationStack(ingredient)) return;
        loadRecipeForTier(ingredient);
    }

    private void loadRecipeForTier(ItemStack result) {
        if (!result.hasTagCompound() || result.getTagCompound()
            .getByte(MTEIndustrialMacerator.TIER) != 2) {
            loadRecipeT1();
        } else {
            loadRecipeT2();
        }
    }

    private void loadAllRecipes() {
        loadRecipeT1();
        loadRecipeT2();
    }

    private void loadRecipeT1() {
        arecipes.add(
            new CachedShapelessRecipe(
                new Object[] { GregtechItemList.Industrial_MacerationStack.get(1) },
                ItemList.MacerationStack.get(1)));
    }

    private void loadRecipeT2() {
        ItemStack oldController = GregtechItemList.Industrial_MacerationStack.get(1);
        ItemStack newController = ItemList.MacerationStack.get(1);
        NBTTagCompound display = new NBTTagCompound();
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString(StatCollector.translateToLocal("gt.macerator.upgraded")));
        display.setTag("Lore", lore);
        oldController.setTagInfo("display", display);
        oldController.setTagInfo(MTEIndustrialMacerator.TIER, new NBTTagByte((byte) 2));
        newController.setTagInfo("display", display);
        newController.setTagInfo(MTEIndustrialMacerator.TIER, new NBTTagByte((byte) 2));
        arecipes.add(new CachedShapelessRecipe(new Object[] { oldController }, newController));
    }

    private static boolean isOldMacerationStack(ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTEIndustrialMaceratorLegacy;
    }

    private static boolean isNewMacerationStack(ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTEIndustrialMacerator;
    }
}
