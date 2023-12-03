package gtPlusPlus.recipes;

import static gtPlusPlus.core.util.minecraft.MaterialUtils.getMaterialName;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import advsolar.common.AdvancedSolarPanel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeRemovals {

    public static void postInit() {
        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            RecipeUtils.removeRecipeByOutput(ItemUtils.getSimpleStack(AdvancedSolarPanel.blockMolecularTransformer));
        }
    }

    public static void onLoadComplete() {
        removeCrudeTurbineRotors();
    }

    // Doesn't actually remove recipes, just hide them
    private static void removeCrudeTurbineRotors() {
        int aRemoved = 0;
        int CUT = CORE.turbineCutoffBase;
        Item aU;
        Collection<GT_Recipe> aAssRecipes = RecipeMaps.assemblerRecipes.getAllRecipes();
        // 170, 172, 174, 176
        if (aAssRecipes.size() > 0) {
            for (GT_Recipe aG : aAssRecipes) {
                if (ArrayUtils.isNotEmpty(aG.mOutputs)) {
                    for (ItemStack aI : aG.mOutputs) {
                        if (aI == null) {
                            continue;
                        }
                        aU = aI.getItem();
                        if (aU == null) {
                            continue;
                        }
                        if (aU instanceof GT_MetaGenerated_Tool_01) {
                            int aMeta = aI.getItemDamage();
                            // Found a Turbine
                            if (aMeta >= 170 && aMeta <= 176) {
                                int aCutoff;
                                String aType;
                                switch (aMeta) {
                                    case 170 -> {
                                        aCutoff = CUT;
                                        aType = "Small ";
                                    }
                                    case 172 -> {
                                        aCutoff = 2 * CUT;
                                        aType = "";
                                    }
                                    case 174 -> {
                                        aCutoff = 3 * CUT;
                                        aType = "Large ";
                                    }
                                    default -> { // 176
                                        aCutoff = 4 * CUT;
                                        aType = "Huge ";
                                    }
                                }
                                Materials aMainMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(aI);
                                Materials aSecondaryMaterial = GT_MetaGenerated_Tool.getSecondaryMaterial(aI);
                                long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(aI);
                                if (rotorDurabilityMax < aCutoff) {
                                    Logger.WARNING(
                                            "[Turbine Cleanup] " + getMaterialName(aMainMaterial)
                                                    + " "
                                                    + aType
                                                    + "Turbines have "
                                                    + rotorDurabilityMax
                                                    + ", which is below the cutoff durability of "
                                                    + aCutoff
                                                    + ", disabling.");
                                    aG.mEnabled = false;
                                    aG.mHidden = true;
                                    aG.mCanBeBuffered = false;
                                    aRemoved++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        Logger.INFO("Removed " + aRemoved + " useless Turbines.");
    }
}
