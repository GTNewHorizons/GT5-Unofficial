package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechSimpleWasher {

    public static void run() {
        if (CORE.ConfigSwitches.enableMachine_SimpleWasher) {
            generateDirtyDustRecipes();
            generateDirtyCrushedRecipes();
            // Register the Simple Washer Entity.
            GregtechItemList.SimpleDustWasher_ULV.set(
                    new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            767,
                            "simplewasher.01.tier.01",
                            "Simple Washer I",
                            0,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                    CORE.GT_Tooltip.get() },
                            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            false,
                            false,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).getStackForm(1L));

            // People want them in higher tiers apparently
            GregtechItemList.SimpleDustWasher_MV.set(
                    new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            31017,
                            "simplewasher.01.tier.02",
                            "Simple Washer II",
                            2,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                    CORE.GT_Tooltip.get() },
                            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            false,
                            false,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).getStackForm(1L));
            GregtechItemList.SimpleDustWasher_EV.set(
                    new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            31018,
                            "simplewasher.01.tier.03",
                            "Simple Washer III",
                            4,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                    CORE.GT_Tooltip.get() },
                            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            false,
                            false,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).getStackForm(1L));
            GregtechItemList.SimpleDustWasher_LuV.set(
                    new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            31019,
                            "simplewasher.01.tier.04",
                            "Simple Washer IV",
                            6,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                    CORE.GT_Tooltip.get() },
                            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            false,
                            false,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).getStackForm(1L));
            GregtechItemList.SimpleDustWasher_UV.set(
                    new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            31020,
                            "simplewasher.01.tier.05",
                            "Simple Washer V",
                            8,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                    CORE.GT_Tooltip.get() },
                            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            false,
                            false,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).getStackForm(1L));
        }
    }

    private static boolean generateDirtyDustRecipes() {
        int mRecipeCount = 0;
        // Generate Recipe Map for the Dust Washer.
        ItemStack dustClean;
        ItemStack dustDirty;
        for (Materials v : Materials.values()) {
            if (v == Materials.Platinum || v == Materials.Osmium
                    || v == Materials.Iridium
                    || v == Materials.Palladium) {
                continue;
            }

            dustClean = GT_OreDictUnificator.get(OrePrefixes.dust, v, 1L);
            dustDirty = GT_OreDictUnificator.get(OrePrefixes.dustImpure, v, 1L);
            addSimpleWashRecipe(dustDirty, dustClean);
        }

        for (Werkstoff v : Werkstoff.werkstoffHashSet) {
            dustClean = v.hasItemType(OrePrefixes.dust) ? v.get(OrePrefixes.dust) : null;
            dustDirty = v.hasItemType(OrePrefixes.dustImpure) ? v.get(OrePrefixes.dustImpure) : null;
            addSimpleWashRecipe(dustDirty, dustClean);
        }

        return GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size() > mRecipeCount;
    }

    private static boolean generateDirtyCrushedRecipes() {
        int mRecipeCount = GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size();
        // Generate Recipe Map for the Dust Washer.
        ItemStack crushedClean;
        ItemStack crushedDirty;
        for (Materials v : Materials.values()) {
            crushedClean = GT_OreDictUnificator.get(OrePrefixes.crushedPurified, v, 1L);
            crushedDirty = GT_OreDictUnificator.get(OrePrefixes.crushed, v, 1L);
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        for (Werkstoff v : Werkstoff.werkstoffHashSet) {
            crushedClean = v.hasItemType(OrePrefixes.crushedPurified) ? v.get(OrePrefixes.crushedPurified) : null;
            crushedDirty = v.hasItemType(OrePrefixes.crushed) ? v.get(OrePrefixes.crushed) : null;
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        return GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size() > mRecipeCount;
    }

    private static void addSimpleWashRecipe(ItemStack aInput, ItemStack aOutput) {
        if (aInput != null && aOutput != null) {
            GTPP_Recipe aRecipe = new GTPP_Recipe(
                    false,
                    new ItemStack[] { aInput },
                    new ItemStack[] { aOutput },
                    null,
                    new int[] {},
                    new FluidStack[] { FluidUtils.getFluidStack("water", 100) },
                    new FluidStack[] {},
                    5,
                    8,
                    0);
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.addRecipe(aRecipe);
        }
    }
}
