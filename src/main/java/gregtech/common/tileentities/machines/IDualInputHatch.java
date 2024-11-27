package gregtech.common.tileentities.machines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTRecipe;

public interface IDualInputHatch {

    boolean justUpdated();

    Iterator<? extends IDualInputInventory> inventories();

    void updateTexture(int id);

    void updateCraftingIcon(ItemStack icon);

    Optional<IDualInputInventory> getFirstNonEmptyInventory();

    boolean supportsFluids();

    List<MTEHatchCraftingInputME.recipeInputs> getPatternsInputs();

    void setSuperCribsRecipeList(ArrayList<GTRecipe> rList);

    ArrayList<GTRecipe> getSuperCribsRecipeList();
}
