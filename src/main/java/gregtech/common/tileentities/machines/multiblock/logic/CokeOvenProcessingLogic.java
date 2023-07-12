package gregtech.common.tileentities.machines.multiblock.logic;

import static gregtech.api.enums.Mods.Railcraft;
import static net.minecraftforge.oredict.OreDictionary.getOreID;
import static net.minecraftforge.oredict.OreDictionary.getOreIDs;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class CokeOvenProcessingLogic extends ProcessingLogic {

    private static final int NORMAL_RECIPE_TIME = 1800;
    private static final int WOOD_ORE_ID = getOreID("logWood");
    private static final int COAL_ORE_ID = getOreID("coal");
    private static final int COAL_BLOCK_ORE_ID = getOreID("blockCoal");
    private static final int SUGARCANE_ORE_ID = getOreID("sugarcane");
    private static final int CACTUS_ORE_ID = getOreID("blockCactus");
    private static final int CACTUS_CHARCOAL_ORE_ID = getOreID("itemCharcoalCactus");
    private static final int SUGAR_CHARCOAL_ORE_ID = getOreID("itemCharcoalSugar");
    private int timeMultiplier = 1;

    @Override
    public @Nonnull CheckRecipeResult process() {
        if (inputItems == null || inputItems[0] == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        ItemStack input = inputItems[0];
        int originalStackSize = input.stackSize;
        ItemStack output = findRecipe(input);
        if (currentOutputItems != null && currentOutputItems[0] != null && !currentOutputItems[0].isItemEqual(output)) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        input.stackSize -= 1;
        setDuration(NORMAL_RECIPE_TIME * timeMultiplier);
        setOutputItems(output);

        return originalStackSize > input.stackSize ? CheckRecipeResultRegistry.SUCCESSFUL
            : CheckRecipeResultRegistry.NO_RECIPE;
    }

    protected ItemStack findRecipe(ItemStack input) {
        for (int oreId : getOreIDs(input)) {
            if (oreId == COAL_ORE_ID) {
                return GT_OreDictUnificator.get("fuelCoke", null, 1);
            } else if (oreId == COAL_BLOCK_ORE_ID) {
                timeMultiplier = 9;
                return GT_ModHandler.getModItem(Railcraft.ID, "cube", 1, 0);
            } else if (oreId == WOOD_ORE_ID) {
                return new ItemStack(Items.coal, 1, 1);
            } else if (oreId == SUGARCANE_ORE_ID) {
                return GT_OreDictUnificator.get("itemCharcoalSugar", null, 1);
            } else if (oreId == SUGAR_CHARCOAL_ORE_ID) {
                return GT_OreDictUnificator.get("itemCokeSugar", null, 1);
            } else if (oreId == CACTUS_ORE_ID) {
                return GT_OreDictUnificator.get("itemCharcoalCactus", null, 1);
            } else if (oreId == CACTUS_CHARCOAL_ORE_ID) {
                return GT_OreDictUnificator.get("itemCokeCactus", null, 1);
            }
        }
        return null;
    }
}
